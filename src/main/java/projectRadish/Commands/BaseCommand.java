package projectRadish.Commands;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * The base class for commands.
 */
public abstract class BaseCommand
{
    protected BaseCommand(){}

    /**
     * Used to get the String returned by !help for each command.
     * @return !help's description
     */
    public abstract String getDescription();

    /**
     * Simply override this to "return false;" to automatically restrict the command.
     * @return Whether or not this command can be used in a PM channel.
     */
    public boolean canBeUsedViaPM() { return true; }


    /**
     * Use for any initialization.
     */
    public void Initialize(){}


    /**
     * This is only here so that AdminCommand (and maybe other subclasses in future) can override it.
     * It's very possibly bad style I just don't know how to avoid it.
     *
     * @param contents the message, without the prefix and the command (can be the empty string)
     * @param event the JDA event causing the command to execute
     */
    public void ProcessCommand(String contents, MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE) && !canBeUsedViaPM()) {
            event.getChannel().sendMessage("This command cannot be used in a PM.").queue();
        } else {
            ExecuteCommand(contents, event);
        }
    }

    /**
     * Executes the command
     * @param contents the message, without the prefix and the command (can be the empty string)
     * @param event the JDA event causing the command to execute
     */
    protected abstract void ExecuteCommand(String contents, MessageReceivedEvent event);
}
