package projectRadish.Commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.MessageListener;

import java.sql.Time;
import java.time.*;
import java.time.format.*;

public class VoiceGetCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        AudioTrack track = MessageListener.vp.getTrack(event.getTextChannel());

        if (track == null)
        {
            event.getChannel().sendMessage("No track is currently being played").queue();
            return;
        }

        AudioTrackInfo trackInfo = track.getInfo();

        //Kimimaru: Duration is returned in milliseconds!
        String title = trackInfo.title;
        long remainingDur = trackInfo.length - track.getPosition();

        //Format to hours, minutes, and seconds
        Instant instant = Instant.ofEpochMilli(remainingDur);
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String output = formatter.format(zdt);

        event.getChannel().sendMessage("Name: `" + title + "`\nTime Left: `" + output + "`").queue();
    }
}
