package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * The base class for commands.
 */
public abstract class BaseCommand
{
    public abstract String getDescription();

    /**
     * Use for any initialization.
     */
    public void Initialize(){}

    protected BaseCommand(){}

    /**
     * This is only here so that AdminCommand (and maybe other subclasses in future) can override it.
     * It's very possibly bad style I just don't know how to avoid it.
     *
     * @param contents the message, without the prefix and the command (can be the empty string)
     * @param event the JDA event causing the command to execute
     */
    public void ProcessCommand(String contents, MessageReceivedEvent event)
    {
        ExecuteCommand(contents, event);
    }

    /**
     * Executes the command
     * @param contents the message, without the prefix and the command (can be the empty string)
     * @param event the JDA event causing the command to execute
     */
    protected abstract void ExecuteCommand(String contents, MessageReceivedEvent event);
}
