package projectRadish.Commands;



import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.MessageListener;

public final class VoicePlayCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        MessageListener.vp.loadAndPlay(event.getTextChannel(), content);
    }
}
