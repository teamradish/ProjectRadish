package projectRadish;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.EmbedBuilder;

import projectRadish.BaseCommand;
import projectRadish.MessageInfoWrapper;

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
