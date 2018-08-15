package projectRadish.Commands.DocCommands;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

public final class CurrentDocCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Sets the current (default) doc to the one that best matches your input.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.equals("") || content.contains("www.")) {
            event.getChannel().sendMessage(String.format(
                    "Usage: `%scurrentdoc Game Name`", Configuration.getCommandPrefix())
            ).queue();
            return;
        }

        Configuration.setCurrentGame(content);
        Configuration.saveConfiguration();
        Configuration.loadConfiguration();

        String game = DidYouMean.getBest(content, Configuration.getDocs().keySet(), true);
        String link = Configuration.getDocs().get(game) + "/preview";

        MessageEmbed e = Utilities.formatOutput("Current Doc set to:", game, link);
        event.getChannel().sendMessage(e).queue();
    }
}
