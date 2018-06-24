package projectRadish.Commands;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.apache.http.client.methods.HttpGet;
import projectRadish.Twitch.*;

public class StreamInfoCommand extends BaseCommand
{
    private static final String CLIENT_ID = "rrixp6h00ku9ic34l1mbvilkl7qi8c";
    private static final String TWITCH_STREAM =  "https://api.twitch.tv/helix/streams?user_login=twitchplays_everything";
    private static final String GAME_ENDPOINT = "https://api.twitch.tv/helix/games?id=";

    private static final int thumbnailWidth = 256;
    private static final int thumnailHeight = 256;

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
        StreamData streamInfo = GetStreamInfo();

        String infoString = null;

        StreamResponse response = null;
        String uptime = "N/A";
        String gameName = null;

        if (streamInfo == null || streamInfo.data == null || streamInfo.data.length == 0)
        {
            event.getChannel().sendMessage("TPE is currently not live").queue();
            return;
        }
        else
        {
            response = streamInfo.data[0];

            gameName = response.game_id;

            try
            {
                //Get the uptime from the difference of the current UTC time and the stream's time (which is in UTC)
                LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

                System.out.println(now.toString());

                //Parse stream time from the string given
                LocalDate streamDate = LocalDate.parse(response.started_at.substring(0, 10));
                LocalTime streamTime = LocalTime.parse(response.started_at.substring(11, response.started_at.length() - 1));
                LocalDateTime streamDateTime = LocalDateTime.of(streamDate, streamTime);

                System.out.println(streamDateTime.toString());

                Duration duration = Duration.between(now, streamDateTime);

                System.out.println(duration.toString());

                final long secondsPerMinute = 60;
                final long secondsPerHour = 60 * 60;
                final long secondsPerDay = secondsPerHour * 24;

                long totalSeconds = Math.abs(duration.getSeconds());

                long days = totalSeconds / secondsPerDay;
                long hours = totalSeconds / secondsPerHour;
                int minutes = (int) ((totalSeconds % secondsPerHour) / secondsPerMinute);
                int seconds = (int) (totalSeconds % secondsPerMinute);

                uptime = days + " days, " + hours + " hrs, " + minutes + " min, " + seconds + "sec";

                GameData gameData = GetGameData(response.game_id);
                if (gameData != null && gameData.data != null && gameData.data.length > 0)
                    gameName = gameData.data[0].name;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            //Get our embed
            MessageEmbed embed = GetEmbed(response, uptime, gameName);

            MessageBuilder msgBuilder = new MessageBuilder();
            msgBuilder.setEmbed(embed);

            Message msg = msgBuilder.build();

            event.getChannel().sendMessage(msg).queue();
        }
    }

    private MessageEmbed GetEmbed(StreamResponse response, String uptime, String gameName)
    {
        //Replace these strings in the thumbnail URL to get an image with the size we want
        String thumbnail = response.thumbnail_url.replace("{width}", Integer.toString(thumbnailWidth));
        thumbnail = thumbnail.replace("{height}", Integer.toString(thumnailHeight));

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(response.title, "https://twitch.tv/twitchplays_everything");
        embedBuilder.setThumbnail(thumbnail);
        embedBuilder.addField("**GAME**", gameName, false);
        embedBuilder.addField("**VIEWERS**", Integer.toString(response.viewer_count), true);
        embedBuilder.addField("**UPTIME**", uptime, true);

        //Twitch Purple
        embedBuilder.setColor(new Color(100, 65, 164));
        return embedBuilder.build();
    }

    public StreamData GetStreamInfo()
    {
        try
        {
            //URL to Twitch stream information
            URL url = new URL(TWITCH_STREAM);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //Add the client ID as the header
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Client-ID", CLIENT_ID);

            //Read information
            BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
            String inputLine = br.readLine();
            br.close();

            StreamData streamData = objMapper.readValue(inputLine, StreamData.class);

            return streamData;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public GameData GetGameData(String gameID)
    {
        try
        {
            //URL to Twitch stream information
            URL url = new URL(GAME_ENDPOINT + gameID);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //Add the client ID as the header
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Client-ID", CLIENT_ID);

            //Read information
            BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
            String inputLine = br.readLine();
            br.close();

            GameData gameData = objMapper.readValue(inputLine, GameData.class);

            return gameData;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
