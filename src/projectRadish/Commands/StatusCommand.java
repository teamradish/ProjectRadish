package projectRadish;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.EmbedBuilder;

import projectRadish.BaseCommand;
import projectRadish.MessageInfoWrapper;

public class StatusCommand extends BaseCommand
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
