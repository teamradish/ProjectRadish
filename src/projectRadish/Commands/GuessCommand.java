package projectRadish.Commands;


import projectRadish.DidYouMean;
import projectRadish.MessageInfoWrapper;
import projectRadish.Utilities;

public final class GuessCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        //Don't bother if there's no input
        if (msgInfo.getCmdArgs().size() == 0) return;

        String input = msgInfo.getMsgContent().toLowerCase();

        String game = Utilities.findGame(input);

        String prefix = "Matched: ";
        if (game == null) {    // still no match found
            game = DidYouMean.getBest(input);
            prefix = "Best guess: ";
        }
        String abbr = DidYouMean.abbreviate(game);
        String reply = prefix + String.format("%s [%s]", game, abbr);
        msgInfo.getChannel().sendMessage(reply).queue();
    }
}
