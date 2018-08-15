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
        return "Silences a user by ID or mention, preventing them from using bot commands.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        String[] args = contents.split(" ");

        //Ignore messages without exactly one argument
        if (args.length != 1 || args[0].isEmpty() == true)
        {
            event.getChannel().sendMessage("Usage: \"ID\" or \"@user\"").queue();
            return;
        }

        //Presume it's an ID first
        String input = args[0];
        User silencedUser = null;

        //If it's a number, it's a user ID
        boolean isNumber = (Utilities.TryParse(input, -1L) != -1L);

        //Check for user ID
        if (isNumber == true)
        {
            //This tries to cast to a User even if it can't find the user, so we have to handle an exception
            try
            {
                silencedUser = event.getJDA().getUserById(input);
            }
            catch (Exception e)
            {
                silencedUser = null;
            }
        }
        //Check for mentions
        else if (event.getMessage().getMentionedUsers().isEmpty() == false)
        {
            silencedUser = event.getMessage().getMentionedUsers().get(0);
        }

        //If null, the user wasn't found
        if (silencedUser == null)
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
        Configuration.getSilencedUsers().add(silencedUser.getId());
        Configuration.saveConfiguration();

        event.getChannel().sendMessage("User silenced.").queue();
    }
}
