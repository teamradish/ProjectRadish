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

public final class AddButtonCommand extends AdminCommand
{
    private StringBuilder reply = new StringBuilder(500);
    private Set<String> args = new HashSet<>();
    private Set<String> notAdded = new HashSet<>();

    @Override
    public String getDescription() {
        String prefix = Configuration.getCommandPrefix();
        return "Adds the given button(s) to be recognised as valid by the input detector.\n" +
                "For example, when switching from a SNES to Gamecube game, the Z button will have" +
                "to be added as a valid button.\n" +
                "Of course, you could just use `"+prefix+"setcontroller gamecube` in that case but still.\n\n"+
                "Note that TPE models Control Sticks as four buttons, so they must be added using" +
                "this command too.\n" +
                "eg. `"+prefix+"addbutton cup cright cdown cleft` to add the Gamecube c-stick.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        if (content.equals("")) {
            String prefix = Configuration.getCommandPrefix();
            String usage = "Usage: `"+prefix+"addbutton <button1> <button2> <button3>...`";
            event.getChannel().sendMessage(usage).queue();
            return;
        }

        reply.setLength(0);
        content = content.toLowerCase();

        Set<String> btns = Main.chatManager.inputValidator.getButtons();

        args.clear();
        notAdded.clear();

        args.addAll(Arrays.asList(content.split("\\s")));

        int additions = 0;
        for (String button: args) {
            if (!btns.contains(button)) {
                btns.add(button);
                additions++;
            } else {
                notAdded.add(button);
            }
        }

        if (additions > 0) {
            Main.chatManager.inputValidator.setButtons(btns);
            reply.append("Successfully added ");
            reply.append(additions);
            reply.append('/');
            reply.append(args.size());
            reply.append(" buttons.\n");
        }
        if (notAdded.size() > 0) {
            reply.append("`");
            Utilities.joinStringBuilder(reply, "`, `", notAdded);
            reply.append("` already present.\n");
        }

        if (btns.isEmpty()) {
            reply.append("No buttons currently set.");
        } else {
            reply.append("Valid buttons are now:\n`");
            Utilities.joinStringBuilder(reply,"`, `", btns);
            reply.append("`");
        }

        event.getChannel().sendMessage(reply.toString()).queue();
    }
}
