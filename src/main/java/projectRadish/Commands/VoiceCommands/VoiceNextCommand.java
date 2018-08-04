package projectRadish.Commands.VoiceCommands;

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
        return "Displays the info of the next song in the queue.";
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

        String title = item.getTitle();
        String duration = Utilities.getTimeStringFromMs(item.getLength());
        String link = item.getLink();

        String req = "***`requested by "+item.getRequester()+"`***";
        String plays = (item.getPlays() == 1) ? "" : "x"+String.valueOf(item.getPlays()); // Hide if only 1 play

        event.getChannel().sendMessage(String.format(
                "Next up: **%s** %s\n" +
                "Link: <%s>\n" +
                "**[ %s ]**%70s", title, plays, link, duration, req)).queue();
    }
}
