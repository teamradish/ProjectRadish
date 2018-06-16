package projectRadish.Commands;

import net.dv8tion.jda.core.entities.*;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

public final class DocCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        //No arguments - show current game doc
        if (content.length() == 0)
        {
            String game = Configuration.getCurrentGame();
            String link = Configuration.getCurrentDoc();

            MessageEmbed e = Utilities.formatOutput("Current Game Document:", game, link);
            event.getChannel().sendMessage(e).queue();
        }
        else
        {
            String input = content;
            String game = Utilities.findGame(input);

            boolean isGuess = false;
            if (game == null) {    // if no match found
                game = DidYouMean.getBest(input);
                isGuess = true;
            }

            String link = Configuration.getDocs().get(game);
            String header;
            if (isGuess) { header = "Best Guess for "+input+":"; } else { header = null; }

            MessageEmbed e = Utilities.formatOutput(header, game, link);
            event.getChannel().sendMessage(e).queue();
        }
    }
}
