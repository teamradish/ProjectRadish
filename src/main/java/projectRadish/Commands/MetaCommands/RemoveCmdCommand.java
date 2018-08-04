package projectRadish.Commands.MetaCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;

import java.util.HashMap;

public class RemoveCmdCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Removes a command, using the name you input. Currently doesn't work for command names containing spaces.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String contents, MessageReceivedEvent event) {
        String[] args = contents.split(" ");

        //Ignore messages without exactly one argument
        if (args.length != 1)
        {
            event.getChannel().sendMessage("Usage: \"command\"").queue();
            return;
        }

        HashMap<String, String> cmds = Configuration.getCommands();
        String cmdToLower = args[0].toLowerCase();

        //Get the command
        String commandClass = cmds.getOrDefault(cmdToLower, "");

        if (commandClass.isEmpty() == true)
        {
            event.getChannel().sendMessage("Command does not exist!").queue();
            return;
        }

        System.out.println("CMD: " + commandClass + " REAL: " + NewCmdCommand.class.getSimpleName());

        //Kimimaru: Don't allow removing the add command!
        if (commandClass.endsWith(NewCmdCommand.class.getSimpleName()))
        {
            event.getChannel().sendMessage("Don't remove the command that adds commands!").queue();
            return;
        }

        cmds.remove(cmdToLower);
        Configuration.saveConfiguration();
        Configuration.loadConfiguration();

        event.getChannel().sendMessage("Removed command " + "\"" + cmdToLower + "\"").queue();
    }
}
