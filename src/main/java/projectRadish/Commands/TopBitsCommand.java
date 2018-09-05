package projectRadish.Commands;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.net.*;
import java.io.*;
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

        //Kimimaru: We can optimize memory usage here; I think Twitch sorts the results by rank, so if that's the case, we shouldn't need these lists
        List<String> leaderNames = Arrays.asList(new String[boardSize]);    // List of nulls
        List<Long> bitAmounts = Arrays.asList(new Long[boardSize]);         // List of nulls

        int longestNameLen = 5;

        for (BitsLeaderboardUser user : leaderboard)
        {
            if (user.rank <= boardSize) {
                String leaderName = GetUserData(user.userId).data[0].display_name;
                int rank = (int)(user.rank-1);

                if (leaderName.length() > longestNameLen) { longestNameLen = leaderName.length(); }
                leaderNames.set(rank, leaderName);
                bitAmounts.set(rank, user.score);
            }
        }

        boolean promoAdded = false;
        StringBuilder reply = new StringBuilder("Cheer Leaderboard:\n");
        reply.append("<https://www.twitch.tv/twitchplays_everything>```\n");
        for (int i = 0; i < boardSize; i++) {
            String name = !isNull(leaderNames.get(i)) ? leaderNames.get(i) : "---"; // Name, or "-" if name is null

            Long bits = bitAmounts.get(i);
            String score;
            if (isNull(bits)) {
                if (!promoAdded && i < 3) {
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
            bw.write(strRequestBody.getBytes("UTF-8"));
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

    //This should be optimized to request multiple leaderboard at once.
    private UserData GetUserData(String userID)
    {
        try
        {
            //URL to Twitch user information
            URL url = new URL(Constants.USER_ENDPOINT + userID);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //Add the client ID as the header
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Client-ID", Constants.CLIENT_ID);

            //Read information
            BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
            String inputLine = br.readLine();
            br.close();

            UserData userData = objMapper.readValue(inputLine, UserData.class);

            return userData;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
