package projectRadish.Commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;

public final class StreamingCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event) {
        if ((Configuration.getTPEAdmin().contains(event.getAuthor().getId())) // Allowed for TPE admins
                || Configuration.getRadishAdmin().contains(event.getAuthor().getId())) // and Radish admins
        {
            String game = content;
            if (game.toLowerCase().equals("none") || game.toLowerCase().equals("nothing"))
            {
                event.getJDA().getPresence().setGame(null);
            }
            else
            {
                event.getJDA().getPresence().setGame(Game.of(
                        GameType.STREAMING, game, "https://twitch.tv/twitchplays_everything")
                );
            }
        }
        else
        {
            event.getChannel().sendMessage("You're not allowed to use this command").queue();
        }
    }
}
