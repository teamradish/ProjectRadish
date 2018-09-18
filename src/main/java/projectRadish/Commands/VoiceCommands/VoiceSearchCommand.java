package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;

public class VoiceSearchCommand extends BaseCommand
{
    //Move to constants if this command works
    private static String API_URL = "https://content.googleapis.com/youtube/v3/search";
    private static String API_KEY = "AIzaSyDPbQ16j2mzjhpByclKK78u0sQD0Q6W7s8";

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
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        try
        {
            String encodedContent = URLEncoder.encode(contents, "UTF-8");
            URL url = new URL(API_URL + "?maxResults=1&part=id&type=video&q=" + encodedContent + "&key=" + API_KEY);

            JSONObject jsonResult = new JSONObject(IOUtils.toString(url, "UTF-8"));

            if (jsonResult.getJSONObject("pageInfo").getInt("totalResults") == 0)
            {
                event.getChannel().sendMessage("Couldn't find a video.").queue();
            }
            else
            {
                String videoId = jsonResult.getJSONArray("items").
                        getJSONObject(0).getJSONObject("id").getString("videoId");

                MessageListener.vp.loadAndPlay(event, "http://youtu.be/" + videoId, 1);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
