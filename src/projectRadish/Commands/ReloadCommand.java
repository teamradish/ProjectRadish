package projectRadish.Commands;

import net.dv8tion.jda.core.entities.Game;
import projectRadish.BaseCommand;
import projectRadish.Configuration;
import projectRadish.MessageInfoWrapper;

/**
 * Reloads the config.
 */
public class ReloadCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        if (Configuration.getRadishAdmin().contains(msgInfo.getAuthor().getId()))
        {
            Configuration.loadConfiguration();
            msgInfo.getChannel().sendMessage("Config reloaded!").queue();
        }
        else
        {
            msgInfo.getChannel().sendMessage("You're not allowed to use this command!").queue();
        }
    }
}
