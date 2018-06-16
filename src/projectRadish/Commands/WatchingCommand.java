package projectRadish.Commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;

public final class WatchingCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if ((Configuration.getRadishAdmin().contains(event.getAuthor().getId()))) {
            String game = content;
            if (game.toLowerCase().equals("none") || game.toLowerCase().equals("nothing"))
            {
                event.getJDA().getPresence().setGame(null);
            }
            else
            {
                event.getJDA().getPresence().setGame(Game.of(
                        GameType.WATCHING, game, "https://twitch.tv/twitchplays_everything"));
            }
        }
    }
}
