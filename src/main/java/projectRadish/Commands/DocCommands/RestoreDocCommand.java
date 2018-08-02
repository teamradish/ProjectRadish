package projectRadish.Commands.DocCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

import java.util.HashMap;

public final class RestoreDocCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Undoes the removal of a doc from the bot's memory (if done reasonably soon after deletion) using the name you input.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.equals("")) {
            event.getChannel().sendMessage(String.format(
                    "Usage: `%srestoredoc Game Name`", Configuration.getCommandPrefix())
            ).queue();
            return;
        }
        if (Utilities.recycleBin.size() == 0) {
            event.getChannel().sendMessage("Recycle Bin is empty.").queue();
            return;
        }

        String game = DidYouMean.getBest(content, Utilities.recycleBin.keySet(), true);

        HashMap<String, String> docs = Configuration.getDocs();
        docs.put(game, Utilities.recycleBin.get(game));
        Configuration.setDocs(docs);
        Configuration.saveConfiguration();

        String reply = "The document for " + game + " has been rescued!";
        event.getChannel().sendMessage(reply).queue();
    }
}
