package projectRadish.Commands.TwitchCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Main;
import projectRadish.StreamChat.Controllers;

import java.util.Set;

public final class SetControllerCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Select a pre-made set of buttons for the Inputs counter to recognise.\n" +
                "Saves having to manually add/remove buttons when you just want a" +
                "standard controller setup.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        String prefix = Configuration.getCommandPrefix();
        content = content.toLowerCase();
        if(Controllers.getControllerNames().contains(content)) { // Match Found
            Main.chatManager.inputValidator.setButtons(Controllers.getController(content));
            StringBuilder reply = new StringBuilder("Successfully changed button config to `"+content+"`.\n");

            Set<String> btns = Main.chatManager.inputValidator.getButtons();
            if (btns.isEmpty()) {
                reply.append("No buttons set. You can use `"+prefix+"addbutton` to add more.");
            } else {
                reply.append("Valid buttons are now:\n`");
                reply.append(String.join("`, `", btns));
                reply.append("`");
            }
            event.getChannel().sendMessage(reply).queue();
        } else { // Doesn't match any preconfigured controllers
            StringBuilder reply = new StringBuilder("No matching controllers found.\n");
            reply.append("The current presets available are:\n`");
            reply.append(String.join("`, `", Controllers.getControllerNames()));
            reply.append("`\nIf none of these are what you want, you can use `"+prefix+"addbutton` ");
            reply.append("and `"+prefix+"delbutton` to manually configure the controller.");
            event.getChannel().sendMessage(reply.toString()).queue();
        }

    }
}
