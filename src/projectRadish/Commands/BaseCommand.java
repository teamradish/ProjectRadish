package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * The base class for commands.
 */
public abstract class BaseCommand
{
    protected BaseCommand()
    {

    }

    /**
     * Use for any initialization.
     */
    public void Initialize()
    {

    }

    /**
     * Executes the command
     * @param contents the message, without the prefix and the command (can be the empty string)
     * @param event the JDA event causing the command to execute
     */
    public abstract void ExecuteCommand(String contents, MessageReceivedEvent event);
}
