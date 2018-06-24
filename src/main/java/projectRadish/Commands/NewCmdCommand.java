package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;

import java.util.HashMap;

public final class NewCmdCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        //Admin-only command
        if ((Configuration.getRadishAdmin().contains(event.getAuthor().getId())) == false)
        {
            event.getChannel().sendMessage("You're not allowed to use this command!").queue();
            return;
        }

        String[] args = contents.split(" ");

        //Ignore messages without exactly two arguments
        if (args.length <= 1 || args.length > 2)
        {
            event.getChannel().sendMessage("Usage: \"command\" \"ClassName\"").queue();
            return;
        }

        HashMap<String, String> cmds = Configuration.getCommands();
        String cmdToLower = args[0].toLowerCase();

        //Check if it already exists and return if so
        if (cmds.containsKey(cmdToLower) == true)
        {
            event.getChannel().sendMessage("Command " + cmdToLower + " already exists").queue();
            return;
        }

        cmds.put(cmdToLower, args[1]);
        Configuration.saveConfiguration();
        Configuration.loadConfiguration();

        event.getChannel().sendMessage("Added command " + "\"" + cmdToLower + "\"").queue();
    }
}
