package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;
import projectRadish.Constants;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

import java.util.HashMap;

public final class RestoreDocCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.equals("")) {
            event.getChannel().sendMessage(String.format(
                    "Usage: `%srestoredoc Game Name`", Constants.COMMAND_PREFIX)
            ).queue();
            return;
        }
        if (Utilities.recycleBin.size() == 0) {
            event.getChannel().sendMessage("Recycle Bin is empty.").queue();
            return;
        }

        String game = DidYouMean.getBest(content, Utilities.recycleBin.keySet());

        HashMap<String, String> docs = Configuration.getDocs();
        docs.put(game, Utilities.recycleBin.get(game));
        Configuration.setDocs(docs);
        Configuration.saveConfiguration();

        String e = "The document for " + game + " has been rescued!";
        event.getChannel().sendMessage(e).queue();
    }
}
