package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Utilities;

import java.util.Vector;

public class ScrambleCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        char[] charArr = content.toCharArray();
        int curStrLen = content.length();

        //Fisher-Yates shuffle the contents
        for (int j = 0; j < curStrLen; j++)
        {
            //Ignore spaces
            if (charArr[j] == ' ') continue;

            //Keep going until you don't find a space; this won't loop indefinitely since we trim the string beforehand
            int rand;
            char temp;
            do
            {
                rand = Utilities.randRange(j, curStrLen - 1);

                temp = charArr[rand];
            }
            while (temp == ' ');

            charArr[rand] = charArr[j];
            charArr[j] = temp;
        }

        //Get our result and send it
        String result = new String(charArr);

        event.getChannel().sendMessage(result).queue();
    }
}
