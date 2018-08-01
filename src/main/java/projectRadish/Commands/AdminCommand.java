package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Utilities;


/**
 * The base class for admin commands.
 */
public abstract class AdminCommand extends BaseCommand
{
    @Override
    public void ProcessCommand(String contents, MessageReceivedEvent event)
    {
        if (Utilities.isAdmin(event.getAuthor())) {
            ExecuteCommand(contents, event);
        } else {
            event.getChannel().sendMessage("Sorry, this command is only for admins.").queue();
        }
    }
}
