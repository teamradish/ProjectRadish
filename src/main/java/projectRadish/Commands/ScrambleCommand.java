package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Utilities;

import java.util.Vector;

public final class ScrambleCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Rearranges your input into unintelligible gibberish.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        StringBuilder strBuilder = new StringBuilder(content.length());

        //Remove all spaces before scrambling
        String noSpaces = content.replaceAll(" ", "");
        char[] charArr = noSpaces.toCharArray();
        int curStrLen = noSpaces.length();

        //Fisher-Yates shuffle the contents
        for (int i = 0; i < curStrLen; i++)
        {
            int rand = Utilities.randRange(i, curStrLen - 1);
            char temp = charArr[rand];

            charArr[rand] = charArr[i];
            charArr[i] = temp;
            strBuilder.append(temp);
        }

        //Re-insert the spaces
        for (int i = 0; i < content.length(); i++)
        {
            char c = content.charAt(i);

            if (c == ' ')
                strBuilder.insert(i, c);
        }

        //Get our result and send it
        String result = strBuilder.toString();

        event.getChannel().sendMessage(result).queue();
    }
}
