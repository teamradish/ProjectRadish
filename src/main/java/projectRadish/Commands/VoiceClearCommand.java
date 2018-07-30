package projectRadish.Commands;



import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.LavaPlayer.VoicePlayer;
import projectRadish.MessageListener;

public final class VoiceClearCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        MessageListener.vp.skipTrack(event.getTextChannel());
        MessageListener.vp.clearQueue(event.getTextChannel());
        event.getChannel().sendMessage("Queue Cleared.").queue();
    }
}
