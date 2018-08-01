package projectRadish.Commands.VoiceCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;
import projectRadish.Utilities;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class VoiceNextCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE)) {
            event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
            return;
        }

        AudioTrack track = MessageListener.vp.peekTrack(event.getTextChannel());

        if (track == null)
        {
            event.getChannel().sendMessage("No track is next in the queue").queue();
            return;
        }

        int queueNum = MessageListener.vp.getQueueSize(event.getTextChannel());

        AudioTrackInfo trackInfo = track.getInfo();

        String title = trackInfo.title;
        String duration = Utilities.getTimeStringFromMs(trackInfo.length);

        event.getChannel().sendMessage("Next: `" + title + "`\nDuration: `" + duration + "`\nQueue Size: `" + queueNum + "`").queue();
    }
}
