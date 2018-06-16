package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;

/**
 * Reloads the config.
 */
public class ReloadCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (Configuration.getRadishAdmin().contains(event.getAuthor().getId()))
        {
            Configuration.loadConfiguration();
            event.getChannel().sendMessage("Config reloaded!").queue();
        }
        else
        {
            event.getChannel().sendMessage("You're not allowed to use this command!").queue();
        }
    }
}
