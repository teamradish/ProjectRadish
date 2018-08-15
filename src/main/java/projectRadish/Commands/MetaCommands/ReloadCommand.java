package projectRadish.Commands.MetaCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;

/**
 * Reloads the config.
 */
public class ReloadCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Reloads the config file, to make sure any changes are pushed through.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    protected void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        Configuration.loadConfiguration();
        event.getChannel().sendMessage("Config reloaded!").queue();
    }
}
