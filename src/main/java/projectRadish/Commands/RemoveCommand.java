package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;

import java.util.HashMap;

public class RemoveCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String contents, MessageReceivedEvent event) {
        //Admin-only command
        if ((Configuration.getRadishAdmin().contains(event.getAuthor().getId())) == false)
        {
            event.getChannel().sendMessage("You're not allowed to use this command!").queue();
            return;
        }

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
        if (commandClass.equals(NewCmdCommand.class.getSimpleName()))
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
