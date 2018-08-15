package projectRadish.Commands.ModerationCommands;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Utilities;

import java.util.Set;
import java.util.Vector;

public final class GetSilencedCommand extends AdminCommand
{
    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    public String getDescription()
    {
        return "Lists all silenced users.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        Vector<String> silencedUsers = Configuration.getSilencedUsers();
        MessageChannel channel = event.getChannel();

        //Send the reply in a DM
        if (channel.getType() != ChannelType.PRIVATE)
        {
            channel = event.getMember().getUser().openPrivateChannel().complete();
        }

        if (silencedUsers.size() == 0)
        {
            channel.sendMessage("No users are currently silenced.").queue();
            return;
        }

        //Clear our StringBuilder and put all the text inside a Markdown code block
        stringBuilder.setLength(0);
        stringBuilder.append("```");

        for (int i = 0; i < silencedUsers.size(); i++)
        {
            String userID = silencedUsers.elementAt(i);
            String userName = "N/A";

            try
            {
                User user = event.getJDA().getUserById(userID);
                userName = user.getName();
            }
            //Continue if we couldn't find the user's name (possibly due to the command being sent in a PM)
            catch (Exception e)
            {
            }

            stringBuilder.append(userID);
            stringBuilder.append(" : ");
            stringBuilder.append(userName);

            //Don't append new line if it's the last one
            if ((i + 1) < silencedUsers.size())
                stringBuilder.append('\n');
        }

        stringBuilder.append("```");

        channel.sendMessage(stringBuilder.toString()).queue();
    }
}
