package projectRadish.Commands.VoiceCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;
import projectRadish.Utilities;

public class VoiceGetCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Displays the info of the currently playing track.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE)) {
        event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
        return;
    }

        AudioTrack track = MessageListener.vp.getTrack(event.getTextChannel());

        if (track == null)
        {
            event.getChannel().sendMessage("No track is currently being played.").queue();
            return;
        }

        //Kimimaru: Duration is returned in milliseconds!
        String curTitle = track.getInfo().title;
        String curPos = Utilities.getTimeStringFromMs(track.getPosition());
        String curLen = Utilities.getTimeStringFromMs(track.getInfo().length);

        // Horrible hack to fix case when curLen includes hours but curPos doesn't
        if (curLen.length() >= "H:mm:ss".length()) {
            if (curPos.length() == "m:ss".length()) { curPos = "0:0" + curPos; }
            else if (curPos.length() == "mm:ss".length()) { curPos = "0:" + curPos; }
        }

        event.getChannel().sendMessage(String.format("Playing: %s `%s/%s`\n", curTitle, curPos, curLen)).queue();
    }
}
