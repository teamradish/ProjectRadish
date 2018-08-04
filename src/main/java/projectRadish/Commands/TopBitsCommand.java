package projectRadish.Commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.net.*;
import java.io.*;
import java.time.*;
import java.text.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import projectRadish.Constants;
import projectRadish.Twitch.*;
import projectRadish.Utilities;

public class TopBitsCommand extends BaseCommand
{
    private static final String TWITCH_GQL =  "https://gql.twitch.tv/gql";

    private ObjectMapper objMapper = new ObjectMapper();

    @Override
    public void Initialize()
    {
        super.Initialize();

        //Map the object to the class
        objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
    }

    @Override
    public void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        HashSet<BitsLeaderboardUser> leaderboard = GetUserLeaderboard();
        if (leaderboard.isEmpty())
        {
            event.getChannel().sendMessage("The leaderboard is empty.").queue();
            return;
        }
        String reply = "Cheer Leaderboard:\n";
        for (BitsLeaderboardUser user : leaderboard)
        {
            reply += user.rank + " - " +
                    GetUserData(user.userId).data[0].display_name + " - " +
                    user.score + " bits\n";
        }
        event.getChannel().sendMessage(reply).queue();
    }

    public HashSet<BitsLeaderboardUser> GetUserLeaderboard()
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

            HashSet<BitsLeaderboardUser> users = new HashSet<>();
            for (JsonNode item : jsonLeaderboard)
            {
                BitsLeaderboardUser user = new BitsLeaderboardUser();
                user.userId = item.get("node").get("entryKey").asText();
                user.rank = item.get("node").get("rank").asLong();
                user.score = item.get("node").get("score").asLong();
                users.add(user);
            }

            return users;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //This should be optimized to request multiple users at once.
    public UserData GetUserData(String userID)
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
