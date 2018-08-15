package projectRadish.Commands.VoiceCommands;

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
        return "Displays the info of the currently playing song.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        QueueItem item = MessageListener.vp.getItem(event.getTextChannel());

        if (isNull(item))
        {
            event.getChannel().sendMessage("No song is currently being played.").queue();
            return;
        }

        //Kimimaru: Duration is returned in milliseconds!
        String curTitle = item.getTitle();
        String curPos = Utilities.getTimeStringFromMs(item.getPosition());
        String curLen = Utilities.getTimeStringFromMs(item.getLength());
        String curLink = item.getLink();

        // Horrible hack to fix case when curLen includes hours but curPos doesn't
        if (curLen.length() >= "H:mm:ss".length()) {
            if (curPos.length() == "m:ss".length()) { curPos = "0:0" + curPos; }
            else if (curPos.length() == "mm:ss".length()) { curPos = "0:" + curPos; }
        }

        String time = curPos + " / " + curLen;
        if (item.isStream()) { time = "Stream"; }

        String req = "***`requested by "+item.getRequester()+"`***";
        String plays = (item.getPlays() == 1) ? "" : "x"+String.valueOf(item.getPlays()); // Hide if only 1 play

        event.getChannel().sendMessage(String.format(
                "Playing: **%s** %s\n" +
                "Link: <%s>\n" +
                "**[ %s ]**%70s", curTitle, plays, curLink, time, req)).queue();
    }
}
