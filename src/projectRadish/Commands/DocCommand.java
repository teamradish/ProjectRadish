package projectRadish.Commands;

import net.dv8tion.jda.core.entities.*;

import projectRadish.Configuration;
import projectRadish.DidYouMean;
import projectRadish.MessageInfoWrapper;
import projectRadish.Utilities;

public final class DocCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        //No arguments - show current game doc
        if (msgInfo.getCmdArgs().size() == 0)
        {

            String game = Configuration.getCurrentGame();
            String link = Configuration.getCurrentDoc();

            MessageEmbed e = Utilities.formatOutput("Current Game Document:", game, link);
            msgInfo.getChannel().sendMessage(e).queue();
        }
        else
        {
            String input = msgInfo.getMsgContent();
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
            msgInfo.getChannel().sendMessage(e).queue();
        }
    }
}
