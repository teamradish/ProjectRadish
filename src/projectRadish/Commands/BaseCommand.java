package projectRadish.Commands;

import projectRadish.MessageInfoWrapper;


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
     * Executes the command.
     * @param msgInfo The message information.
     */
    public abstract void ExecuteCommand(MessageInfoWrapper msgInfo);
}
