package projectRadish.Commands.VoiceCommands;



import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

public final class VoiceClearCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Stops playback and removes all songs from the music queue.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (MessageListener.vp.isPlayingTrack(event.getTextChannel()) == false)
        {
            event.getChannel().sendMessage("No track is being played, and the queue is empty!").queue();
            return;
        }

        event.getChannel().sendMessage("Queue Cleared.").queue();
        MessageListener.vp.clearQueue(event.getTextChannel());
        MessageListener.vp.skipItem(event.getTextChannel());

        //Kimimaru: If it's paused, we should resume it so next time we queue up a song it plays
        MessageListener.vp.resume(event.getTextChannel());
    }
}
