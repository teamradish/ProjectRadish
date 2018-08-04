package projectRadish.Commands.VoiceCommands;



import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.LavaPlayer.VoicePlayer;
import projectRadish.MessageListener;

public final class VoiceClearCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Stops playback and removes all items from the music queue.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        MessageListener.vp.clearQueue(event.getTextChannel());
        MessageListener.vp.skipTrack(event.getTextChannel());
        event.getChannel().sendMessage("Queue Cleared.").queue();
    }
}
