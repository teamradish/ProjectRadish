package projectRadish.Commands.DocCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;

public final class AllDocsCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        event.getChannel().sendMessage("http://twitchplays.wikia.com/wiki/Game_Documents_(Mobile)").queue();
    }
}
