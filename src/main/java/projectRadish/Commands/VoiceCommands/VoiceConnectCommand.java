package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

public final class VoiceConnectCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Rejoins the Voice Channel, and resumes playing music, unless the queue is empty.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE)) {
            event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
            return;
        }

        MessageListener.vp.connectToVoiceChannel(event.getGuild().getAudioManager());
    }
}
