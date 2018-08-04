package projectRadish.Commands.VoiceCommands;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

public final class VoiceSkipCommand extends BaseCommand
{
    @Override
    public String getDescription() { return "Skips the current song, and starts playback of the next."; }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        event.getChannel().sendMessage("Track Skipped.").queue();
        MessageListener.vp.skipItem(event.getTextChannel());
    }
}
