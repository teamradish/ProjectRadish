package projectRadish.Commands;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * The base class for commands.
 */
public abstract class BaseCommand {
    protected BaseCommand() {
    }

    /**
     * Used to get the String returned by !help for each command.
     *
     * @return !help's description
     */
    public abstract String getDescription();

    /**
     * Simply override this to "return false;" to automatically restrict the command.
     *
     * @return Whether or not this command can be used in a PM channel.
     */
    public abstract boolean canBeUsedViaPM();


    /**
     * Use for any initialization.
     */
    public void Initialize() {
    }


    /**
     * This is only here so that AdminCommand (and maybe other subclasses in future) can override it.
     * It's very possibly bad style I just don't know how to avoid it.
     *
     * @param contents the message, without the prefix and the command (can be the empty string)
     * @param event    the JDA event causing the command to execute
     */
    public void ProcessCommand(String contents, MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE) && !canBeUsedViaPM()) {
            event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
            return;
        }

        // No issues
        try {
            ExecuteCommand(contents, event);
        }
        catch (IllegalArgumentException e) {
            if (e.getMessage().contains("text for message must be less than")) { // character limit alert
                event.getChannel().sendMessage("Sorry, my reply hit the character limit.").queue();
            } else { throw e; } // Don't hide other IllegalArgumentErrors
        }
    }

    /**
     * Executes the command
     *
     * @param contents the message, without the prefix and the command (can be the empty string)
     * @param event    the JDA event causing the command to execute
     */
    protected abstract void ExecuteCommand(String contents, MessageReceivedEvent event);
}
