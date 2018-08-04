package projectRadish.Commands.DocCommands;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.Configuration;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

public final class GuessCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Takes your input and finds the closest match from all doc titles.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        //Don't bother if there's no input
        if (content.length() == 0) return;

        String input = content;

        String game = Utilities.findGame(input);

        String prefix = "Matched: ";
        if (game == null) {    // still no match found
            game = DidYouMean.getBest(input, Configuration.getDocs().keySet(), true);
            prefix = "Best guess: ";
        }
        String abbr = DidYouMean.abbreviate(game);
        String reply = prefix + String.format("%s [%s]", game, abbr);
        event.getChannel().sendMessage(reply).queue();
    }
}
