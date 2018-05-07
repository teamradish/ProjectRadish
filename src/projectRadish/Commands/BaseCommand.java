package projectRadish;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Random;

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
