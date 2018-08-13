package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

public final class VoiceResumeCommand extends BaseCommand
{
    @Override
    public String getDescription()
    {
        return "Resumes the current track if one is paused.";
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

        if (MessageListener.vp.isPaused(event.getTextChannel()) == false)
        {
            event.getChannel().sendMessage("The player is not paused!").queue();
            return;
        }

        MessageListener.vp.resume(event.getTextChannel());
        event.getChannel().sendMessage("Resumed current track.").queue();
    }
}