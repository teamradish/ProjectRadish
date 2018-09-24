package projectRadish.Commands.VoiceCommands;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import projectRadish.YouTube.SearchResponse;

public class VoiceSearchCommand extends BaseCommand
{
    //Move to constants if this command works
    private static String API_URL = "https://content.googleapis.com/youtube/v3/search";
    private static String API_KEY = "AIzaSyDPbQ16j2mzjhpByclKK78u0sQD0Q6W7s8";
    private ObjectMapper objMapper = new ObjectMapper();

    @Override
    public String getDescription()
    {
        return "This commands searchs for videos on YouTube and plays the first result found.";
    }

    @Override
    public boolean canBeUsedViaPM()
    {
        return false;
    }

    @Override
    public void Initialize()
    {
        //Map the object to the class
        objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
    }

    @Override
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        try
        {
            String encodedContent = URLEncoder.encode(contents, "UTF-8");
            URL url = new URL(API_URL + "?maxResults=1&part=id&type=video&q=" + encodedContent + "&key=" + API_KEY);

            SearchResponse searchResponse = objMapper.readValue(url, SearchResponse.class);

            if (searchResponse.pageInfo.totalResults == 0)
            {
                event.getChannel().sendMessage("Couldn't find a video.").queue();
            }
            else
            {
                String videoId = searchResponse.items.get(0).id.videoId;
                MessageListener.vp.loadAndPlay(event, "http://youtu.be/" + videoId, 1);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
