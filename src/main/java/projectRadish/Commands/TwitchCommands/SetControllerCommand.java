package projectRadish.Commands.TwitchCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Main;
import projectRadish.StreamChat.Controllers;
import projectRadish.Utilities;

import java.util.Set;

public final class SetControllerCommand extends AdminCommand
{
    private String description;
    private StringBuilder reply = new StringBuilder(500);

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void Initialize() {
        description = "Select a pre-made set of buttons for the Inputs counter to recognise.\n" +
                "Saves having to manually add/remove buttons when you just want a" +
                "standard controller setup.";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        String prefix = Configuration.getCommandPrefix();
        content = content.toLowerCase();
        if(Controllers.getControllerNames().contains(content)) { // Match Found
            Main.chatManager.inputValidator.setButtons(Controllers.getController(content));
            reply.setLength(0);
            reply.append("Successfully changed button config to `");
            reply.append(content);
            reply.append("`.\n");

            Set<String> btns = Main.chatManager.inputValidator.getButtons();
            if (btns.isEmpty()) {
                reply.append("No buttons set. You can use `");
                reply.append(prefix);
                reply.append("addbutton` to add more.");
            } else {
                reply.append("Valid buttons are now:\n`");
                Utilities.joinStringBuilder(reply, "`, `", btns);
                reply.append("`");
            }

            event.getChannel().sendMessage(reply).queue();
        } else { // Doesn't match any preconfigured controllers
            reply.setLength(0);
            reply.append("No matching controllers found.\n");
            reply.append("The current presets available are:\n`");
            Utilities.joinStringBuilder(reply, "`, `", Controllers.getControllerNames());
            reply.append("`\nIf none of these are what you want, you can use `");
            reply.append(prefix);
            reply.append("addbutton` ");
            reply.append("and `");
            reply.append(prefix);
            reply.append("delbutton` to manually configure the controller.");

            event.getChannel().sendMessage(reply.toString()).queue();
        }

    }
}
