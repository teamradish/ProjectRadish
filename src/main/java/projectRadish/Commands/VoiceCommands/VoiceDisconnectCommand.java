package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

public final class VoiceDisconnectCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Disconnects from the Voice Channel and pauses playback.\nThe queue and progress through the current song will both be preserved.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        MessageListener.vp.disconnectFromVoiceChannel(event.getTextChannel());
    }
}
