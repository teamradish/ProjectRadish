package projectRadish.Commands;


import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;
import projectRadish.DidYouMean;
import projectRadish.MessageListener;
import projectRadish.Utilities;

import java.util.HashMap;
import java.util.Map;

public final class HelpCommand extends BaseCommand
{
    //Kimimaru: Cached data
    private Map<String, BaseCommand> nonAdminCommands = null;
    private String cachedAdminStr = "";
    private String cachedNonAdminStr = "";

    @Override
    public String getDescription() { return "This."; }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event) {

        String prefix = Configuration.getCommandPrefix();

        // Find the user's PM channel, to send our reply to.
        MessageChannel channel;
        if (event.isFromType(ChannelType.PRIVATE)) {
            channel = event.getChannel();
        } else {
            channel = event.getMember().getUser().openPrivateChannel().complete();
        }

        // Use the command set that the requesting person can use.
        Map<String, BaseCommand> cmds = MessageListener.getCommands();
        boolean isAdmin = Utilities.isAdmin(event.getAuthor());

        if (isAdmin == false)
        {
            //Populate the non-admin command map if null
            if (nonAdminCommands == null) {
                initNonAdminCommands();
            }

            cmds = nonAdminCommands;
        }

        // Generate response
        if (content.startsWith(prefix)) {
            content = content.substring(prefix.length());
        }
        if (content.equals("")) {
            sendList(channel, cmds, isAdmin); // List all commands
        } else {
            String reply; // Give info on one command
            if (cmds.keySet().contains(content)) {
                reply = String.format("`%s%s`\n%s",
                        prefix, content, cmds.get(content).getDescription());
            } else {
                String best_guess = DidYouMean.getBest(content, cmds.keySet(), false);
                reply = String.format("No matches for `%s%s`.\nBest Guess: `%s%s`\n%s",
                        prefix, content, prefix, best_guess, cmds.get(best_guess).getDescription());
            }
            channel.sendMessage(reply).queue();
        }
    }

    private void sendList(MessageChannel channel, Map<String, BaseCommand> cmds, boolean isAdmin)
    {
        String str = (isAdmin == true) ? cachedAdminStr : cachedNonAdminStr;

        //Kimimaru: Create string if not already cached
        if (str.isEmpty() == true)
        {
            StringBuilder reply = new StringBuilder(1000);
            reply.append("Type " + Configuration.getCommandPrefix() + "help <command name> for the full description.\n");
            reply.append("```\n");

            int i = 0;
            for (String cmdName : cmds.keySet()) {
                i++;
                reply.append(Configuration.getCommandPrefix());

                if (i < 4) {
                    reply.append(String.format("%-16s", cmdName));
                } else {
                    reply.append(String.format("%s", cmdName));
                    i = 0;
                    reply.append("\n");
                }
            }
            reply.append("```");

            str = reply.toString();
            if (isAdmin == true)
                cachedAdminStr = str;
            else
                cachedNonAdminStr = str;
        }

        channel.sendMessage(str).queue();
    }

    private void initNonAdminCommands()
    {
        Map<String, BaseCommand> allCommands = MessageListener.getCommands();

        nonAdminCommands = new HashMap<>();
        for (String s : allCommands.keySet()) {
            BaseCommand cmd = allCommands.get(s);
            if (cmd instanceof AdminCommand == false)
            {
                // If it's not an admin command
                nonAdminCommands.put(s, cmd);
            }
        }
    }
}
