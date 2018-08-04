package projectRadish.Commands.MetaCommands;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;

public class SetPrefixCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Sets the prefix to the text you input.\n" +
                "Input must be enclosed in quotes to register leading or trailing whitespace.\n" +
                "Note this command will only work via PM.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        // Ignore messages that aren't PMs (otherwise you'll accidentally change the prefix for the live bot while testing)
        if (!event.isFromType(ChannelType.PRIVATE))
        {
            event.getChannel().sendMessage("This command can only be used via PM.").queue();
            return;
        }

        String prefix = contents;

        // Detect if quotes were used and remove them
        if ( contents.length() > 1 && contents.startsWith("\"") && contents.endsWith("\"")) {
            prefix = contents.substring(1, contents.length()-1);
        }

        Configuration.setCommandPrefix(prefix);
        Configuration.saveConfiguration();
        Configuration.loadConfiguration();

        event.getChannel().sendMessage("Changed prefix to " + "\"" + prefix + "\". Usage:\n" + prefix + "doc SMS").queue();
    }
}
