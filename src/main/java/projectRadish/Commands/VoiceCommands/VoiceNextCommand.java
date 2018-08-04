package projectRadish.Commands.VoiceCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.LavaPlayer.QueueItem;
import projectRadish.MessageListener;
import projectRadish.Utilities;

import static java.util.Objects.isNull;

public class VoiceNextCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Displays the info of the next item in the queue.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        QueueItem item = MessageListener.vp.peekItem(event.getTextChannel());
        if (isNull(item))
        {
            event.getChannel().sendMessage("There are no more items in the queue.").queue();
            return;
        }

        AudioTrack track = item.getTrack();
        AudioTrackInfo trackInfo = track.getInfo();

        String title = trackInfo.title;
        String duration = Utilities.getTimeStringFromMs(trackInfo.length);
        String link = track.getInfo().uri;

        String req = "***`requested by "+item.getRequester()+"`***";

        event.getChannel().sendMessage(String.format(
                "Next up: %s\n" +
                "Link: <%s>\n" +
                "**[ %s ]**%70s", title, link, duration, req)).queue();
    }
}
