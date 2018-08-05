package projectRadish.Commands.ModerationCommands;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Utilities;

public final class SilenceCommand extends AdminCommand
{
    @Override
    public String getDescription()
    {
        return "Silences a user by ID, preventing them from using bot commands.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

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
        User silencedUser = null;

        //This tries to cast to a User even if it can't find the user, so we have to handle an exception
        try
        {
            silencedUser = event.getJDA().getUserById(userID);
        }
        catch (Exception e)
        {
            event.getChannel().sendMessage("User cannot be found.").queue();
            return;
        }

        //Make sure the user isn't an admin
        if (Utilities.isAdmin(silencedUser) == true)
        {
            event.getChannel().sendMessage("You can't silence an admin!").queue();
            return;
        }

        //Make sure the user isn't already silenced
        if (Utilities.isSilenced(silencedUser) == true)
        {
            event.getChannel().sendMessage("This user is already silenced!").queue();
            return;
        }

        //Silence the user and save the config
        Configuration.getSilencedUsers().add(userID);
        Configuration.saveConfiguration();

        event.getChannel().sendMessage("User silenced.").queue();
    }
}
