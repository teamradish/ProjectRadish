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
        return "Unsilences a user by mention, allowing them to use bot commands once again.";
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
            event.getChannel().sendMessage("Usage: \"@user\"").queue();
            return;
        }

        if (event.getMessage().getMentionedUsers().isEmpty())
        {
            event.getChannel().sendMessage("An user was not specified.").queue();
            return;
        }

        //This could be expanded to silence more than one user (???)
        User unSilencedUser = event.getMessage().getMentionedUsers().get(0);
        String userID = unSilencedUser.getId();

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
