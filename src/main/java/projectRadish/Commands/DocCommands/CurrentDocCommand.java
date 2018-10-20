package projectRadish.Commands.DocCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

public final class CurrentDocCommand extends AdminCommand
{
    private EmbedBuilder embedBuilder = new EmbedBuilder();

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

        String lower = content.toLowerCase();
        if (lower.equals("nothing") || lower.equals("none")) {
            content = null;
            event.getChannel().sendMessage("There is now no Current Doc.").queue();
        } else {
            String game = DidYouMean.getBest(content, Configuration.getDocs().keySet(), true);
            String link = Configuration.getDocs().get(game) + "/preview";

            MessageEmbed e = Utilities.formatOutput("Current Doc set to:", game, link, embedBuilder);
            event.getChannel().sendMessage(e).queue();
        }

        Configuration.setCurrentGame(content);
        Configuration.saveConfiguration();
        Configuration.loadConfiguration();
    }
}
