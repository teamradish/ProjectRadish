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
        return "Silences a user by mention, preventing them from using bot commands.";
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
        User silencedUser = event.getMessage().getMentionedUsers().get(0);
        String userID = silencedUser.getId();

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
