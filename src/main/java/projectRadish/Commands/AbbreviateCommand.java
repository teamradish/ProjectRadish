package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.DidYouMean;

public final class AbbreviateCommand extends BaseCommand
{
    private StringBuilder stringBuilder = new StringBuilder(100);

    @Override
    public String getDescription() {
        return "Turns the input you give it into an abbreviation.\n- Or rather, Ttiygiiaa.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        event.getChannel().sendMessage(DidYouMean.abbreviate(content, stringBuilder)).queue();
    }
}
