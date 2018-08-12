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
        return "Unsilences a user by ID or mention, allowing them to use bot commands once again.";
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
        User unSilencedUser = null;

        //If it's a number, it's a user ID
        boolean isNumber = (Utilities.TryParse(input, -1L) != -1L);

        //Check for user ID
        if (isNumber == true)
        {
            //This tries to cast to a User even if it can't find the user, so we have to handle an exception
            try
            {
                unSilencedUser = event.getJDA().getUserById(input);
            }
            catch (Exception e)
            {
                unSilencedUser = null;
            }
        }
        //Check for mentions
        else if (event.getMessage().getMentionedUsers().isEmpty() == false)
        {
            unSilencedUser = event.getMessage().getMentionedUsers().get(0);
        }

        //If null, the user wasn't found
        if (unSilencedUser == null)
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
        Configuration.getSilencedUsers().remove(unSilencedUser.getId());
        Configuration.saveConfiguration();

        event.getChannel().sendMessage("User unsilenced!").queue();
    }
}
