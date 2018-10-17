package projectRadish.Commands.TwitchCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.Configuration;

import java.util.Map;

public final class InputCountCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Outputs the total number of valid inputs the given user has sent to TPE's chat.\n" +
                "Remember to use their Twitch username, not their Discord one.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if(content.equals("") || content.contains(" ")) { // Twitch name is blank or contains spaces
            String usage = String.format("Usage: `%sinputs <twitch_username>`", Configuration.getCommandPrefix());
            event.getChannel().sendMessage(usage).queue();
            return;
        }
        String twitchName = content;
        Map<String, Long> inputCounts = Configuration.getInputCounts();
        Long inputs = inputCounts.getOrDefault(twitchName, 0L); // If they're not even registered, return 0
        String reply = String.format("%s: %d Inputs", twitchName, inputs);
        event.getChannel().sendMessage(reply).queue();
    }
}
