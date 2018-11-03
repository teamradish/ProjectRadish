package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public final class BrokenCommand extends BaseCommand
{
    private String message = "";

    @Override
    public String getDescription() {
        return "There's some kind of issue with this command, so it's currently unavailable.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void Initialize() {
        message = "There's currently a known issue with this command, so it's unavailable for now.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        event.getChannel().sendMessage(message).queue();
    }
}
