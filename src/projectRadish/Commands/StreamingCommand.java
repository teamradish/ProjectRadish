package projectRadish.Commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;

import projectRadish.Configuration;
import projectRadish.MessageInfoWrapper;

public final class StreamingCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo) {
        if ((Configuration.getTPEAdmin().contains(msgInfo.getAuthor().getId())) // Allowed for TPE admins
                || Configuration.getRadishAdmin().contains(msgInfo.getAuthor().getId())) // and Radish admins
        {
            String game = msgInfo.getMsgContent();
            if (game.toLowerCase().equals("none") || game.toLowerCase().equals("nothing"))
            {
                msgInfo.getMsgEvent().getJDA().getPresence().setGame(null);
            }
            else
            {
                msgInfo.getMsgEvent().getJDA().getPresence().setGame(Game.of(
                        GameType.STREAMING, game, "https://twitch.tv/twitchplays_everything")
                );
            }
        }
        else
        {
            msgInfo.getChannel().sendMessage("You're not allowed to use this command").queue();
        }
    }
}
