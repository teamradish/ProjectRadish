package projectRadish.Commands.TwitchCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Main;

import java.util.Map;
import java.util.Set;

public final class ShowButtonsCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Outputs all buttons currently recognised as valid by the input detector.\n" +
                "For example, if a SNES game is currently running, the output should be:\n" +
                "`a, b, x, y, l, r, start, select, up, right, down, left`.\n" +
                "Note that TPE models Control Sticks as four buttons, so the Gamecube" +
                "c-stick would be output here as `cup, cright, cdown, cleft`.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        StringBuilder reply = new StringBuilder();
        Set<String> btns = Main.chatManager.inputValidator.getButtons();
        if (btns.isEmpty()) {
            reply.append("No buttons currently set.");
        } else {
            reply.append("Valid buttons are now:\n`");
            reply.append(String.join("`, `", btns));
            reply.append("`");
        }
        event.getChannel().sendMessage(reply).queue();
    }
}
