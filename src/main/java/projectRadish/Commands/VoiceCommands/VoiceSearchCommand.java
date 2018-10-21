package projectRadish.Commands.VoiceCommands;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.Constants;
import projectRadish.MessageListener;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import projectRadish.YouTube.SearchResponse;

public class VoiceSearchCommand extends BaseCommand
{
    private ObjectMapper objMapper = new ObjectMapper();

    @Override
    public String getDescription()
    {
        return "Searches for a video on YouTube and plays the audio of the first result found.";
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
        if (contents.isEmpty() == true)
        {
            event.getChannel().sendMessage("Usage: \"YouTube video search\"").queue();
            return;
        }

        try
        {
            String encodedContent = URLEncoder.encode(contents, "UTF-8");
            URL url = new URL(Constants.YOUTUBE_API_URL + "?maxResults=1&part=id&type=video&q=" + encodedContent + "&key=" + Constants.YOUTUBE_API_KEY);

            SearchResponse searchResponse = objMapper.readValue(url, SearchResponse.class);

            if (searchResponse.pageInfo.totalResults == 0)
            {
                event.getChannel().sendMessage("Couldn't find a video.").queue();
            }
            else
            {
                String videoId = searchResponse.items.get(0).id.videoId;
                event.getChannel().sendMessage("**Result:** <http://youtu.be/"+videoId+">").queue();
                MessageListener.vp.loadAndPlay(event, "http://youtu.be/" + videoId, 1);

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
