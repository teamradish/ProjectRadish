package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

public final class VoiceDisconnectCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Disconnects from the Voice Channel and pauses playback. The queue and progress through current track will both be preserved.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE)) {
            event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
            return;
        }

        MessageListener.vp.disconnectFromVoiceChannel(event.getTextChannel());
    }
}
