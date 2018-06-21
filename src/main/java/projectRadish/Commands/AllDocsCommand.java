package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public final class AllDocsCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        event.getChannel().sendMessage("http://twitchplays.wikia.com/wiki/Game_Documents_(Mobile)").queue();
    }
}
