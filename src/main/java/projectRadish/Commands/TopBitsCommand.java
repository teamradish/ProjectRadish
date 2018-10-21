package projectRadish.Commands;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import projectRadish.Constants;
import projectRadish.Twitch.*;

import static java.util.Objects.isNull;

public class TopBitsCommand extends BaseCommand
{
    private static final String TWITCH_GQL =  "https://gql.twitch.tv/gql";
    private final int boardSize = 3;
    private final HashSet<BitsLeaderboardUser> leaderboard = new HashSet<>();
    private BitsLeaderboardUser[] bitsLeaderboardUsers = new BitsLeaderboardUser[boardSize];
    private StringBuilder reply = new StringBuilder();

    private List<String> leaderNames = new ArrayList<>(boardSize);
    private List<Long> bitAmounts = new ArrayList<>(boardSize);
    private UserData cachedUserData = new UserData();

    private ObjectMapper objMapper = new ObjectMapper();

    @Override
    public String getDescription() {
        return "Displays the current Top Cheers leaderboard for TPE's stream.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void Initialize()
    {
        //Map the object to the class
        objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);

        //Populate lists
        for (int i = 0; i < boardSize; i++)
        {
            leaderNames.add(null);
            bitAmounts.add(0L);
        }
    }

    @Override
    public void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        GetUserLeaderboard();
        if (leaderboard.isEmpty())
        {
            event.getChannel().sendMessage("The leaderboard is empty.").queue();
            return;
        }

        //Reset collections
        for (int i = 0; i < boardSize; i++)
        {
            leaderNames.set(i, null);
            bitAmounts.set(i, 0L);
        }
        cachedUserData.data = null;

        int longestNameLen = 5;
        int userCount = 0;

        String userQuery = "";

        for (BitsLeaderboardUser user : leaderboard)
        {
            if (user.rank <= boardSize)
            {
                //Kimimaru: Build the query for more users
                userQuery += user.userId + "&id=";

                bitsLeaderboardUsers[userCount] = user;
                userCount++;
            }
        }

        //Remove the hanging ID from the end of the query - "&id="
        userQuery = userQuery.substring(0, userQuery.length() - 4);

        GetUserData(userQuery);
        for (int i = 0; i < cachedUserData.data.length; i++)
        {
            //Kimimaru: The indices line up since Twitch returns the users in the order their IDs were in the query
            String leaderName = cachedUserData.data[i].display_name;

            if (leaderName.length() > longestNameLen)
            {
                longestNameLen = leaderName.length();
            }
            int rank = (int)bitsLeaderboardUsers[i].rank -1;
            leaderNames.set(rank, leaderName);
            bitAmounts.set(rank, bitsLeaderboardUsers[i].score);
        }

        boolean promoAdded = false;
        reply.append("Cheer Leaderboard:\n");
        reply.append("<https://www.twitch.tv/twitchplays_everything>```\n");

        for (int i = 0; i < boardSize; i++) {
            String name = !isNull(leaderNames.get(i)) ? leaderNames.get(i) : "---"; // Name, or "-" if name is null

            Long bits = bitAmounts.get(i);
            String score;
            if (isNull(bits)) {
                if (!promoAdded && i < 3)
                {
                    score = "Cheer to take #"+(i+1)+"!"; // If this is the first empty slot
                    promoAdded = true;
                } else { score = "-"; } // If this slot is empty but we already added the promo
            }
            else if (bits == 1) { score = "1 bit"; }
            else { score = String.format("%d bits", bits); }

            reply.append(String.format("#%d | %-"+(longestNameLen+3)+"s | %s\n", i+1, name, score));
        }
        reply.append("```");

        event.getChannel().sendMessage(reply).queue();
    }

    public void GetUserLeaderboard()
    {
        try
        {
            ArrayNode operationQueryArray = objMapper.createArrayNode();
            ObjectNode theQuery = objMapper.createObjectNode();
            theQuery.put("operationName", "BitsPinnedCheerV2_Leaderboards");
            ObjectNode operationQueryVariables = objMapper.createObjectNode();
            operationQueryVariables.put("channelID", Constants.STREAM_ID);
            theQuery.putPOJO("variables", operationQueryVariables);
            ObjectNode operationQueryExtensions = objMapper.createObjectNode();
            ObjectNode operationQueryPersistedQuery = objMapper.createObjectNode();
            operationQueryPersistedQuery.put("version", 1);
            operationQueryPersistedQuery.put("sha256Hash", "1b83daceb82d09f8868cd5f937b3f531ddd8de2be60ec99574b93f627d407448");
            operationQueryExtensions.putPOJO("persistedQuery", operationQueryPersistedQuery);
            theQuery.putPOJO("extensions", operationQueryExtensions);
            operationQueryArray.addPOJO(theQuery);

            //Send request as POST.
            String strRequestBody = objMapper.writer().writeValueAsString(operationQueryArray);
            URL url = new URL(TWITCH_GQL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Client-ID", Constants.CLIENT_ID);
            conn.setRequestProperty("Content-Length", Integer.toString(strRequestBody.length()));
            conn.setDoOutput(true);

            //Send body
            BufferedOutputStream bw = new BufferedOutputStream(conn.getOutputStream());
            bw.write(strRequestBody.getBytes(StandardCharsets.UTF_8));
            bw.close();

            //Read information
            BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
            String inputLine = br.readLine();
            br.close();

            JsonNode jsonLeaderboard = objMapper.readTree(inputLine);
            jsonLeaderboard = jsonLeaderboard.get(0)
                    .get("data").get("user").get("cheer").get("leaderboard")
                    .get("items").get("edges");

            leaderboard.clear();
            for (JsonNode item : jsonLeaderboard)
            {
                BitsLeaderboardUser user = new BitsLeaderboardUser();
                user.userId = item.get("node").get("entryKey").asText();
                user.rank = item.get("node").get("rank").asLong();
                user.score = item.get("node").get("score").asLong();
                leaderboard.add(user);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void GetUserData(String userIDs)
    {
        try
        {
            //URL to Twitch user information
            URL url = new URL(Constants.USER_ENDPOINT + userIDs);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //Add the client ID as the header
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Client-ID", Constants.CLIENT_ID);

            //Read information
            BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
            String inputLine = br.readLine();
            br.close();

            objMapper.readerForUpdating(cachedUserData).readValue(inputLine);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
