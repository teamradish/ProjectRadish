package projectRadish;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.EmbedBuilder;

import projectRadish.BaseCommand;
import projectRadish.MessageInfoWrapper;

public final class StreamingCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo) {
        if ((Configuration.getTPEAdmin().contains(msgInfo.getAuthor().getId())) || Configuration.getRadishAdmin().contains(msgInfo.getAuthor().getId()))
        {
            String game = msgInfo.getMsgContent();
            if (!game.toLowerCase().equals("none"))
            {
                msgInfo.getMsgEvent().getJDA().getPresence().setGame(Game.of(GameType.STREAMING, game, "https://twitch.tv/twitchplays_everything"));
            }
            else
            {
                msgInfo.getMsgEvent().getJDA().getPresence().setGame(null);
            }
        }
        else
        {
            msgInfo.getChannel().sendMessage("You're not allowed to use this command").queue();
        }
    }
}
