package projectRadish.Commands.VoiceCommands;

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
            String curTitle = curItem.getTitle();
            String curPos = Utilities.getTimeStringFromMs(curItem.getPosition());
            String curLen = Utilities.getTimeStringFromMs(curItem.getLength());

            // Horrible hack to fix case when curLen includes hours but curPos doesn't
            if (curLen.length() >= "H:mm:ss".length()) {
                if (curPos.length() == "m:ss".length()) { curPos = "0:0" + curPos; }
                else if (curPos.length() == "mm:ss".length()) { curPos = "0:" + curPos; }
            }

            String time = curPos + " / " + curLen;
            if (curItem.isStream()) { time = "Stream"; }

            String plays = (curItem.getPlays() == 1) ? "" : "x"+String.valueOf(curItem.getPlays()); // Hide if only 1 play

            replyB.append(String.format(
                    "Playing: **%s** %s\n" +
                    "**[ %s ]**\n", curTitle, plays, time));
        } else {
            replyB.append("No item currently playing.\n");
        }

        if (queue.size() > 0) {
            long duration = 0;
            int and_X_More = 0; // Number of tracks that couldn't be shown
            int numStreams = 0;
            replyB.append("```");
            int i = 0;
            for (QueueItem item: queue) {
                i++;
                if (item.isStream()) {
                    numStreams++;
                } else {
                    duration += item.getLength();
                }
                if (i <= maxItemsShown && replyB.length() <= 1800) { // watch out for that character limit
                    String title = item.getTitle();
                    if (title.length() > maxTitleLength) { title = title.substring(0, maxTitleLength-3) + "..."; }

                    String len = Utilities.getTimeStringFromMs(item.getLength());
                    if (item.isStream()) { len = "Stream"; }
                    String req = item.getRequester();
                    String plays = (item.getPlays() == 1) ? "" : "x"+String.valueOf(item.getPlays()); // Hide if only 1 play

                    replyB.append(String.format(
                            "#%-3d| %s %s\n" +
                            "____| %-8s %s\n", i, title, plays, len, req));
                } else {
                    and_X_More++;
                }
            }
            if (and_X_More > 0) {
                replyB.append("...and "+and_X_More+" more.\n");
            }

            if (queue.size() > 1) {
                String durString = Utilities.getTimeStringFromMs(duration);
                String streamString = String.format("%d Stream%s", numStreams, new String (numStreams == 1 ? "" : "s"));

                String outputString;
                if (numStreams == 0) { outputString = durString; } // No streams
                else if (numStreams == queue.size()) { outputString = streamString; } // All streams
                else { outputString = durString + " + " + streamString; }// Some streams, some normal tracks
                replyB.append("\nTotal Duration\n"+outputString+"\n");
            }
            replyB.append("```");
        } else {
            replyB.append("```No items in queue.```");
        }

        event.getChannel().sendMessage(replyB.toString()).queue();
    }
}
