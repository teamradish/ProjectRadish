package projectRadish.Commands.DocCommands;

import net.dv8tion.jda.core.entities.*;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.Configuration;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

import static java.util.Objects.isNull;

public final class DocCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Provides the link to the doc for the game you input. Leave blank for current/most recent game's doc.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        //No arguments - show current game doc
        if (content.length() == 0)
        {
            if (isNull(Configuration.getCurrentGame())) {
                String prefix = Configuration.getCommandPrefix();
                event.getChannel().sendMessage("No currently active docs.\n" +
                        "Use `"+prefix+"doc Game Name` or `"+prefix+"alldocs` to browse past docs."
                ).queue();
                return;
            }
            String game = Configuration.getCurrentGame();
            String best_match = DidYouMean.getBest(game, Configuration.getDocs().keySet(), true);
            String link = Configuration.getDocs().get(best_match) + "/edit";

            MessageEmbed e = Utilities.formatOutput("Current Game Document:", best_match, link);
            event.getChannel().sendMessage(e).queue();
        }
        else
        {
            String input = content;
            String game = Utilities.findGame(input);

            boolean isGuess = false;
            if (game == null) {    // if no match found
                game = DidYouMean.getBest(input, Configuration.getDocs().keySet(), true);
                isGuess = true;
            }

            String link = Configuration.getDocs().get(game) + "/preview";
            String header;
            if (isGuess) { header = "Best Guess for "+input+":"; } else { header = null; }

            MessageEmbed e = Utilities.formatOutput(header, game, link);
            event.getChannel().sendMessage(e).queue();
        }
    }
}
