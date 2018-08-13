package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;
import sun.plugin2.message.Message;

public final class VoicePauseCommand extends BaseCommand
{
    @Override
    public String getDescription()
    {
        return "Pauses the current track if one is playing.";
    }

    @Override
    public boolean canBeUsedViaPM()
    {
        return false;
    }

    @Override
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        if (MessageListener.vp.isPlayingTrack(event.getTextChannel()) == false)
        {
            event.getChannel().sendMessage("No track is playing!").queue();
            return;
        }

        if (MessageListener.vp.isPaused(event.getTextChannel()) == true)
        {
            event.getChannel().sendMessage("The player is already paused!").queue();
            return;
        }

        MessageListener.vp.pause(event.getTextChannel());
        event.getChannel().sendMessage("Paused current track.").queue();
    }
}
