package projectRadish.Commands;


import net.dv8tion.jda.core.OnlineStatus;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;

public final class StatusCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event) {
        if ((Configuration.getRadishAdmin().contains(event.getAuthor().getId()))) {
            String status = content;
            try {
                event.getJDA().getPresence().setStatus(OnlineStatus.fromKey(status));
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage("Invalid Status. Must be Online, Idle, DND, Invisible, or Offline.").queue();
            }
        } else {
            event.getChannel().sendMessage("You're not allowed to use this command").queue();
        }
    }
}
