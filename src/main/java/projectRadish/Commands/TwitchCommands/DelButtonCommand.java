package projectRadish.Commands.TwitchCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Main;
import projectRadish.Utilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class DelButtonCommand extends AdminCommand
{
    private StringBuilder reply = new StringBuilder(500);
    private Set<String> args = new HashSet<>();
    private Set<String> notRemoved = new HashSet<>();

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

        reply.setLength(0);
        content = content.toLowerCase();

        Set<String> btns = Main.chatManager.inputValidator.getButtons();

        args.clear();
        notRemoved.clear();

        args.addAll(Arrays.asList(content.split("\\s")));

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
            reply.append("Successfully removed ");
            reply.append(removals);
            reply.append('/');
            reply.append(args.size());
            reply.append(" buttons.\n");
        }
        if (notRemoved.size() > 0) {
            reply.append("`");
            Utilities.joinStringBuilder(reply, "`, `", notRemoved);
            reply.append("` not present to begin with.\n");
        }

        if (btns.isEmpty()) {
            reply.append("No buttons currently set.");
        } else {
            reply.append("Valid buttons are now:\n`");
            Utilities.joinStringBuilder(reply, "`, `", btns);
            reply.append("`");
        }

        event.getChannel().sendMessage(reply.toString()).queue();
    }
}
