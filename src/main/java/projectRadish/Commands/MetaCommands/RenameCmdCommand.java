package projectRadish.Commands.MetaCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;

import java.util.HashMap;

public final class RenameCmdCommand extends AdminCommand
{
    @Override
    public void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        String[] args = contents.split(" ");

        //Ignore messages without exactly two arguments
        if (args.length <= 1 || args.length > 2)
        {
            event.getChannel().sendMessage("Usage: \"commandName\" \"newCommandName\"").queue();
            return;
        }

        HashMap<String, String> cmds = Configuration.getCommands();
        String cmdToLower = args[0].toLowerCase();

        //Check if it doesn't exist and return if so
        if (cmds.containsKey(cmdToLower) == false)
        {
            event.getChannel().sendMessage("Command " + cmdToLower + " does not exist").queue();
            return;
        }

        //Store the original's class name
        String origClassName = cmds.get(cmdToLower);

        //Get the new name
        String newNameToLower = args[1].toLowerCase();

        //Remove the old one and put the new one in
        cmds.remove(cmdToLower);
        cmds.put(newNameToLower, origClassName);

        Configuration.saveConfiguration();
        Configuration.loadConfiguration();

        event.getChannel().sendMessage("Renamed command " + "\"" + cmdToLower + "\" to \"" + newNameToLower + "\"").queue();
    }
}