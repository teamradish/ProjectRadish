package projectRadish.Commands.PresenceCommands;


import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;

public final class GameCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Set the bot's status message to \"Playing <input>\". Set to \"none\" or \"nothing\" for no status message.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    protected void ExecuteCommand(String content, MessageReceivedEvent event) {
        String game = content;
        if (game.toLowerCase().equals("none") || game.toLowerCase().equals("nothing")) {
            event.getJDA().getPresence().setGame(null);
        } else {
            event.getJDA().getPresence().setGame(Game.of(GameType.DEFAULT, game));
        }
    }
}
