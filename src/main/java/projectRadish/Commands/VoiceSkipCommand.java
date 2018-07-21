package projectRadish.Commands;



import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.MessageListener;

public final class VoiceSkipCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        MessageListener.vp.skipTrack(event.getTextChannel());
        event.getChannel().sendMessage("Track Skipped.").queue();
    }
}
