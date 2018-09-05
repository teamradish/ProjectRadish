package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Utilities;

public final class RollCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Outputs a random number. Default 1-6 inclusive, but you can input 2 numbers to define your own range.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        //Default values
        int min = 1;
        int max = 6;

        //Don't bother if there's nothing to parse
        if (content.isEmpty() == false)
        {
            String args[] = content.split(" ");

            //Allow user to specify the range
            if (args.length == 2)
            {
                try {
                    min = Integer.parseInt(args[0]);
                    max = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    event.getChannel().sendMessage("Usage: \"Min\" \"Max\"").queue();
                    return;
                }
            }
            else
            {
                event.getChannel().sendMessage("Usage: \"Min\" \"Max\"").queue();
                return;
            }
        }

        int roll = Utilities.randRange(min, max);
        event.getChannel().sendMessage("Your roll is " + roll).queue();
    }
}
