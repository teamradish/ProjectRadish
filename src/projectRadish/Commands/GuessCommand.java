package projectRadish.Commands;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.DidYouMean;
import projectRadish.Utilities;

public final class GuessCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        //Don't bother if there's no input
        if (content.length() == 0) return;

        String input = content;

        String game = Utilities.findGame(input);

        String prefix = "Matched: ";
        if (game == null) {    // still no match found
            game = DidYouMean.getBest(input);
            prefix = "Best guess: ";
        }
        String abbr = DidYouMean.abbreviate(game);
        String reply = prefix + String.format("%s [%s]", game, abbr);
        event.getChannel().sendMessage(reply).queue();
    }
}
