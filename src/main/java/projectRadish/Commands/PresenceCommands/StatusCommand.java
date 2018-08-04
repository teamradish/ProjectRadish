package projectRadish.Commands.PresenceCommands;


import net.dv8tion.jda.core.OnlineStatus;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;

public final class StatusCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Set the bot's visibility status - can be Online, Idle, DND, or Offline.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event) {
        String status = content;
        try {
            event.getJDA().getPresence().setStatus(OnlineStatus.fromKey(status));
        } catch (IllegalArgumentException e) {
            event.getChannel().sendMessage("Invalid Status. Must be Online, Idle, DND, Invisible, or Offline.").queue();
        }
    }
}
