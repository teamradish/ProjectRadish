package projectRadish.Commands.ModerationCommands;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Utilities;

public final class UnsilenceCommand extends AdminCommand
{
    @Override
    public String getDescription()
    {
        return "Unsilences a user by ID, allowing them to use bot commands once again.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        String[] args = contents.split(" ");

        //Ignore messages without exactly one argument
        if (args.length != 1)
        {
            event.getChannel().sendMessage("Usage: \"userID\"").queue();
            return;
        }

        String userID = args[0];
        User unSilencedUser = null;

        //This tries to cast to a User even if it can't find the user, so we have to handle an exception
        try
        {
            unSilencedUser = event.getJDA().getUserById(userID);
        }
        catch (Exception e)
        {
            event.getChannel().sendMessage("User cannot be found.").queue();
            return;
        }

        //Make sure the user is already silenced
        if (Utilities.isSilenced(unSilencedUser) == false)
        {
            event.getChannel().sendMessage("This user isn't silenced!").queue();
            return;
        }

        //Unsilence the user and save the config
        Configuration.getSilencedUsers().remove(userID);
        Configuration.saveConfiguration();

        event.getChannel().sendMessage("User unsilenced!").queue();
    }
}
