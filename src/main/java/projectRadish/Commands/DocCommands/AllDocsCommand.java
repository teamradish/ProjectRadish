package projectRadish.Commands.DocCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.Configuration;

public final class AllDocsCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Provides the link to the TPE Wiki's game documents hub.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        event.getChannel().sendMessage("http://twitchplays.wikia.com/wiki/Game_Documents_(Mobile)").queue();
    }
}
