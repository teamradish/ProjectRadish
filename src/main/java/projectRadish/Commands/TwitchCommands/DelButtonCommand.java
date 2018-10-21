package projectRadish.Commands.TwitchCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class DelButtonCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        String prefix = Configuration.getCommandPrefix();
        return "Deletes the given button(s), so that the input detector won't detect it as valid.\n" +
                "For example, when switching from a Gamecube game to SNES game, the Z button will" +
                "have to be removed, as it is no longer a valid button press.\n" +
                "Of course, you could just use `"+prefix+"setcontroller snes` in that case but still.\n\n" +
                "Note that TPE models Control Sticks as four buttons, so they must be removed using" +
                "this command too.\n" +
                "eg. `"+prefix+"delbutton cup cright cdown cleft` to remove the Gamecube c-stick.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.equals("")) {
            String prefix = Configuration.getCommandPrefix();
            String usage = "Usage: `"+prefix+"delbutton <button1> <button2> <button3>...`";
            event.getChannel().sendMessage(usage).queue();
            return;
        }

        StringBuilder reply = new StringBuilder();
        content = content.toLowerCase();

        Set<String> btns = Main.chatManager.inputValidator.getButtons();

        Set<String> args = new HashSet<>(Arrays.asList(content.split("\\s")));
        Set<String> notRemoved = new HashSet<>();
        int removals = 0;
        for (String button: args) {
            if (btns.contains(button)) {
                btns.remove(button);
                removals++;
            } else {
                notRemoved.add(button);
            }
        }

        if (removals > 0) {
            Main.chatManager.inputValidator.setButtons(btns);
            reply.append(String.format("Successfully removed %d/%d buttons.\n", removals, args.size()));
        }
        if (notRemoved.size() > 0) {
            reply.append("`"+String.join("`, `", notRemoved)+"` not present to begin with.\n");
        }

        if (btns.isEmpty()) {
            reply.append("No buttons currently set.");
        } else {
            reply.append("Valid buttons are now:\n`");
            reply.append(String.join("`, `", btns));
            reply.append("`");
        }

        event.getChannel().sendMessage(reply.toString()).queue();
    }
}
