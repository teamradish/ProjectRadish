package projectRadish.Commands;


import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;

import projectRadish.Configuration;
import projectRadish.MessageInfoWrapper;

public final class GameCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo) {
        if (Configuration.getRadishAdmin().contains(msgInfo.getAuthor().getId())) {
            String game = msgInfo.getMsgContent();
            if (game.toLowerCase().equals("none") || game.toLowerCase().equals("nothing")) {
                msgInfo.getMsgEvent().getJDA().getPresence().setGame(null);
            } else {
                msgInfo.getMsgEvent().getJDA().getPresence().setGame(Game.of(GameType.DEFAULT, game));
            }
        } else {
            msgInfo.getChannel().sendMessage("You're not allowed to use this command").queue();
        }
    }
}
