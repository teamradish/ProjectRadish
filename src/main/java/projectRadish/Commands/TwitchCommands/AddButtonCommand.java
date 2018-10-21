package projectRadish.Commands.TwitchCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class AddButtonCommand extends AdminCommand
{
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

        StringBuilder reply = new StringBuilder();
        content = content.toLowerCase();

        Set<String> btns = Main.chatManager.inputValidator.getButtons();

        Set<String> args = new HashSet<>(Arrays.asList(content.split("\\s")));
        Set<String> notAdded = new HashSet<>();
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
            reply.append(String.format("Successfully added %d/%d buttons.\n", additions, args.size()));
        }
        if (notAdded.size() > 0) {
            reply.append("`"+String.join("`, `", notAdded)+"` already present.\n");
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
