package projectRadish.Commands;


import net.dv8tion.jda.core.OnlineStatus;

import projectRadish.Configuration;
import projectRadish.MessageInfoWrapper;

public final class StatusCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo) {
        if ((Configuration.getRadishAdmin().contains(msgInfo.getAuthor().getId()))) {
            String status = msgInfo.getMsgContent();
            try {
                msgInfo.getMsgEvent().getJDA().getPresence().setStatus(OnlineStatus.fromKey(status));
            } catch (IllegalArgumentException e) {
                msgInfo.getChannel().sendMessage("Invalid Status. Must be Online, Idle, DND, Invisible, or Offline.").queue();
            }
        } else {
            msgInfo.getChannel().sendMessage("You're not allowed to use this command").queue();
        }
    }
}
