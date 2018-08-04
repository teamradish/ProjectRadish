package projectRadish.Commands.MetaCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.AdminCommand;
import projectRadish.Configuration;
import projectRadish.Constants;
import projectRadish.DidYouMean;

import java.util.HashMap;

public final class NewCmdCommand extends AdminCommand
{
    @Override
    public String getDescription() {
        return "Adds a new command, using the name and Java Class you input (in that order).\n" +
                "Java Class name is case-sensitive.\n" +
                "Currently doesn't work for command names containing spaces.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        String[] args = contents.split(" ");

        //Ignore messages without exactly two arguments
        if (args.length != 2)
        {
            event.getChannel().sendMessage("Usage: \"command\" \"ClassName\"").queue();
            return;
        }

        HashMap<String, String> cmds = Configuration.getCommands();
        String cmdToLower = args[0].toLowerCase();

        String className = args[1];

        //Check if it already exists and return if so
        if (cmds.containsKey(cmdToLower) == true)
        {
            event.getChannel().sendMessage("A command called `" + cmdToLower + "` already exists").queue();
            return;
        }

        // Check whether that Command Class actually exists (across all subpackages in Commands)
        String fullClassName = "";
        boolean match_found = false;
        for (Package p: Package.getPackages()) {
            if (p.getName().startsWith(Constants.CommandPkgName)) { // Only look in [Commands] and its subpackages
                try {
                    Class.forName(p.getName() + "." + className); // Throws error if class not in this subpackage
                    // Didn't throw error, so it's a match
                    fullClassName = p.getName() + "." + className;
                    fullClassName = fullClassName.replaceFirst(Constants.CommandPkgName+".", "");
                    match_found = true;
                    break; // We found it, don't bother looking any further
                }
                catch (ClassNotFoundException e) { /* If no match, do nothing, and keep looping over subclasses */ }
                catch (NoClassDefFoundError e2)  { /* This error can be thrown if the name is right but not capitalised */ }
            }
        }

        //Kimimaru: Don't allow adding another NewCmdCommand, since you can't remove it
        if (args[1].endsWith(NewCmdCommand.class.getSimpleName()))
        {
            event.getChannel().sendMessage("You can't add another NewCmdCommand since it can't be removed!").queue();
            return;
        }

        if (match_found) {
            cmds.put(cmdToLower, fullClassName);
            Configuration.saveConfiguration();
            Configuration.loadConfiguration();

            event.getChannel().sendMessage("Added command `" + Configuration.getCommandPrefix() + cmdToLower + "`").queue();
        } else {
            event.getChannel().sendMessage("`" + className + "` is not a valid Command Type.").queue();
        }
    }
}
