package projectRadish.Commands.VoiceCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.LavaPlayer.QueueItem;
import projectRadish.MessageListener;
import projectRadish.Utilities;

import java.util.List;

import static java.util.Objects.isNull;

public class ViewQueueCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Displays the current music queue.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        int maxTitleLength = 50;
        int maxItemsShown = 10;

        List<QueueItem> queue = MessageListener.vp.getQueue(event.getTextChannel());

        StringBuilder replyB = new StringBuilder();

        QueueItem curItem = MessageListener.vp.getItem(event.getTextChannel());

        if (!isNull(curItem)) {
            AudioTrack curTrack = curItem.getTrack();
            String curTitle = curTrack.getInfo().title;
            String curPos = Utilities.getTimeStringFromMs(curTrack.getPosition());
            String curLen = Utilities.getTimeStringFromMs(curTrack.getInfo().length);

            // Horrible hack to fix case when curLen includes hours but curPos doesn't
            if (curLen.length() >= "H:mm:ss".length()) {
                if (curPos.length() == "m:ss".length()) { curPos = "0:0" + curPos; }
                else if (curPos.length() == "mm:ss".length()) { curPos = "0:" + curPos; }
            }

            replyB.append(String.format(
                    "Playing: %s \n" +
                    "**[ %s / %s ]**\n", curTitle, curPos, curLen));
        } else {
            replyB.append("No item currently playing.\n");
        }

        if (queue.size() > 0) {
            long duration = 0;
            int and_X_More = 0; // Number of tracks that couldn't be shown
            replyB.append("```");
            int i = 0;
            for (QueueItem item: queue) {
                i++;
                AudioTrack t = item.getTrack();
                duration += t.getInfo().length;

                if (i <= maxItemsShown && replyB.length() <= 1800) { // watch out for that character limit
                    String title = t.getInfo().title;
                    if (title.length() > maxTitleLength) { title = title.substring(0, maxTitleLength-3) + "..."; }

                    String len = Utilities.getTimeStringFromMs(t.getInfo().length);
                    String req = item.getRequester();

                    replyB.append(String.format(
                            "#%-3d| %s\n" +
                            "____| %-9s%"+(maxTitleLength-13)+"s\n", i, title, len, req));
                } else {
                    and_X_More++;
                }
            }
            if (and_X_More > 0) {
                replyB.append("...and "+and_X_More+" more.\n");
            }

            if (queue.size() > 1) {
                replyB.append("\nTotal Duration\n"+ Utilities.getTimeStringFromMs(duration) +"\n");
            }
            replyB.append("```");
        } else {
            replyB.append("```No items in queue.```");
        }

        event.getChannel().sendMessage(replyB.toString()).queue();
    }
}
