package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;

import java.util.HashMap;

public class SetPrefixCommand extends BaseCommand
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

        //Ignore messages without exactly one argument
        if (args.length != 1)
        {
            event.getChannel().sendMessage("Usage: \"prefix\"").queue();
            return;
        }

        String prefix = args[0];

        Configuration.setCommandPrefix(prefix);
        Configuration.saveConfiguration();
        Configuration.loadConfiguration();

        event.getChannel().sendMessage("Changed prefix to " + "\"" + prefix + "\"").queue();
    }
}
