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
        String args[] = content.split(" ");

        //Default values
        int min = 1;
        int max = 6;

        //Allow user to specify the range
        if (args.length == 2)
        {
            try
            {
                min = Integer.parseInt(args[0]);
                max = Integer.parseInt(args[1]);
            }
            catch (Exception e)
            {
                event.getChannel().sendMessage("One of your inputs wasn't valid.").queue();
                return;
            }
        }

        int roll = Utilities.randRange(min, max);
        event.getChannel().sendMessage("Your roll: " + roll).queue();
    }
}
