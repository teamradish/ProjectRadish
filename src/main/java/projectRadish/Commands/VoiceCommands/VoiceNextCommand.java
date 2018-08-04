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
    public String getDescription() {
        return "Displays the info of the next track in the queue.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        AudioTrack track = MessageListener.vp.peekTrack(event.getTextChannel());

        if (track == null)
        {
            event.getChannel().sendMessage("There are no more tracks in the queue.").queue();
            return;
        }

        AudioTrackInfo trackInfo = track.getInfo();

        String title = trackInfo.title;
        String duration = Utilities.getTimeStringFromMs(trackInfo.length);
        String link = track.getInfo().uri;

        event.getChannel().sendMessage(String.format("Next up: %s\nLink: `%s`\n**%s**", title, link, duration)).queue();
    }
}
