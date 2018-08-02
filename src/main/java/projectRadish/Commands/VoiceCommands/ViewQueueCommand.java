package projectRadish.Commands.VoiceCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;
import projectRadish.Utilities;

import java.util.List;

public class ViewQueueCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Displays the current music queue.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE)) {
            event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
            return;
        }

        List<AudioTrack> queue = MessageListener.vp.getQueue(event.getTextChannel());

        StringBuilder replyB = new StringBuilder();

        AudioTrack curTrack = MessageListener.vp.getTrack(event.getTextChannel());

        if (curTrack != null) {
            String curTitle = curTrack.getInfo().title;
            String curPos = Utilities.getTimeStringFromMs(curTrack.getPosition());
            String curLen = Utilities.getTimeStringFromMs(curTrack.getInfo().length);

            // Horrible hack to fix case when curLen includes hours but curPos doesn't
            if (curLen.length() >= "H:mm:ss".length()) {
                if (curPos.length() == "m:ss".length()) { curPos = "0:0" + curPos; }
                else if (curPos.length() == "mm:ss".length()) { curPos = "0:" + curPos; }
            }

            replyB.append(String.format("Playing: %s `%s/%s`\n", curTitle, curPos, curLen));
        } else {
            replyB.append("No track currently playing.\n");
        }

        if (queue.size() > 0) {
            long duration = 0;
            replyB.append("```");
            int i = 0;
            for (AudioTrack t: queue) {
                i++;
                String title = t.getInfo().title;
                if (title.length() > 64) { title = title.substring(0, 61) + "..."; }
                String len = Utilities.getTimeStringFromMs(t.getInfo().length);
                duration += t.getInfo().length;
                replyB.append(String.format("#%0$-3d| %1s\n____| %2s\n", i, title, len));
            }
            if (queue.size() > 1) {
                replyB.append("\nTotal Duration\n"+ Utilities.getTimeStringFromMs(duration) +"\n");
            }
            replyB.append("```");
        } else {
            replyB.append("```No tracks in queue.```");
        }

        event.getChannel().sendMessage(replyB.toString()).queue();
    }
}
