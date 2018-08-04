package projectRadish.Commands;

import net.dv8tion.jda.core.entities.ChannelType;
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
        // Check for issues
        if (!Utilities.isAdmin(event.getAuthor())) { // User must be admin
            event.getChannel().sendMessage("Sorry, this command is only for admins.").queue();
            return;
        }

        if (event.isFromType(ChannelType.PRIVATE) && !canBeUsedViaPM()) { // If PM'd, check command is PM-compatible
            event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
            return;
        }

        // If we made it this far, there are no issues, so execute
        ExecuteCommand(contents, event);
    }
}
