package projectRadish.Commands;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public final class SayCommand extends BaseCommand
{
    private final String usageStr = "Usage: \"Message\"";
    private String description = null;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void Initialize() {
        super.Initialize();

        description = "Sends your input right back at you.\n" +
                "Largely useless since most bots ignore other bots, so you can't even get Radish to play PokeCord or anything.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.length() > 0) {
            event.getChannel().sendMessage(content).queue();
        }
        else
        {
            event.getChannel().sendMessage(usageStr);
        }
    }
}
