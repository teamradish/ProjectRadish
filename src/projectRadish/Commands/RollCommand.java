package projectRadish;

import projectRadish.BaseCommand;
import projectRadish.MessageInfoWrapper;
import projectRadish.Utilities;

import java.util.Vector;

public class RollCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        Vector<String> args = msgInfo.getCmdArgs();

        //Default values
        int min = 1;
        int max = 6;

        //Allow user to specify the range
        if (args.size() == 2)
        {
            try
            {
                min = Integer.parseInt(args.elementAt(0));
                max = Integer.parseInt(args.elementAt(1));
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        int roll = Utilities.RandRange(min, max);
        msgInfo.getChannel().sendMessage("Your roll: " + roll).queue();
    }
}
