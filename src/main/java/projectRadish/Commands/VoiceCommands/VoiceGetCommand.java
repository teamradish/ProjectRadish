package projectRadish.Commands.VoiceCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.LavaPlayer.QueueItem;
import projectRadish.MessageListener;
import projectRadish.Utilities;

import static java.util.Objects.isNull;

public class VoiceGetCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Displays the info of the currently playing track.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        QueueItem item = MessageListener.vp.getItem(event.getTextChannel());

        if (isNull(item))
        {
            event.getChannel().sendMessage("No item is currently being played.").queue();
            return;
        }

        //Kimimaru: Duration is returned in milliseconds!
        AudioTrack track = item.getTrack();
        String curTitle = track.getInfo().title;
        String curPos = Utilities.getTimeStringFromMs(track.getPosition());
        String curLen = Utilities.getTimeStringFromMs(track.getInfo().length);
        String curLink = track.getInfo().uri;

        // Horrible hack to fix case when curLen includes hours but curPos doesn't
        if (curLen.length() >= "H:mm:ss".length()) {
            if (curPos.length() == "m:ss".length()) { curPos = "0:0" + curPos; }
            else if (curPos.length() == "mm:ss".length()) { curPos = "0:" + curPos; }
        }

        String req = "***`requested by "+item.getRequester()+"`***";

        event.getChannel().sendMessage(String.format(
                "Playing: %s \n" +
                "Link: <%s>\n" +
                "**[ %s / %s ]**%70s", curTitle, curLink, curPos, curLen, req)).queue();
    }
}
