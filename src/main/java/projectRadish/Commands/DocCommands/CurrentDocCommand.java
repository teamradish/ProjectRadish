package projectRadish.Commands.DocCommands;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.Configuration;
import projectRadish.Constants;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

public final class CurrentDocCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.equals("")) {
            event.getChannel().sendMessage(String.format(
                    "Usage: `%scurrentdoc Game Name`", Configuration.getCommandPrefix())
            ).queue();
            return;
        }

        Configuration.setCurrentGame(content);

        String game = DidYouMean.getBest(content, Configuration.getDocs().keySet());
        String link = Configuration.getDocs().get(game) + "/preview";

        MessageEmbed e = Utilities.formatOutput("Current Doc set to:", game, link);
        event.getChannel().sendMessage(e).queue();
    }
}
