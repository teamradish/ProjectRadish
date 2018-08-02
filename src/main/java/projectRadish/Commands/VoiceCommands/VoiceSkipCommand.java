package projectRadish.Commands.VoiceCommands;


import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

public final class VoiceSkipCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Skips the current track, and starts playback of the next.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE)) {
            event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
            return;
        }

        event.getChannel().sendMessage("Track Skipped.").queue();
        MessageListener.vp.skipTrack(event.getTextChannel());
    }
}
