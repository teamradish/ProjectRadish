package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;
import projectRadish.Constants;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

import java.util.HashMap;

public final class RemoveDocCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.equals("")) {
            event.getChannel().sendMessage(String.format(
                    "Usage: `%sremovedoc Game Name`", Configuration.getCommandPrefix())
            ).queue();
            return;
        }

        String game = Utilities.findGame(content);

        if (game == null) {
            game = DidYouMean.getBest(content, Configuration.getDocs().keySet());
            String e = "You must get the doc name exactly right to remove a doc.\n" +
                    "Did you mean: " + game;
            event.getChannel().sendMessage(e).queue();

        } else {
            HashMap<String, String> docs = Configuration.getDocs();
            Utilities.recycleBin.put(game, docs.get(game));
            docs.remove(game);
            Configuration.setDocs(docs);
            Configuration.saveConfiguration();

            String e = "The document for " + game + " has been removed.";
            event.getChannel().sendMessage(e).queue();
        }
    }
}
