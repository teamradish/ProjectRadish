package projectRadish.Commands;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public final class SayCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.length() > 0) {
            event.getChannel().sendMessage(content).queue();
        }
    }
}
