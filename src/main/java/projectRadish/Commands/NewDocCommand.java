package projectRadish.Commands;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Configuration;
import projectRadish.Constants;
import projectRadish.Utilities;

import java.util.Arrays;
import java.util.HashMap;

public final class NewDocCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        String args[] = content.split(" ");

        if (args.length >= 2 && !args[0].startsWith("docs.google") && !args[0].startsWith("https://docs")) {
            String link = args[args.length-1];

            try {
                link = formatLink(link);
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(
                        "Must be a Google Doc (ie. begin with `https://docs.google.com/document/d/...`)").queue();
                return;
            }

            args = Arrays.copyOfRange(args, 0, args.length-1);
            String name = String.join(" ", args);

            HashMap<String, String> docs = Configuration.getDocs();
            docs.put(name, link);
            Configuration.setDocs(docs);
            Configuration.saveConfiguration();

            MessageEmbed e = Utilities.formatOutput("Doc Added", name, link + "/preview");
            event.getChannel().sendMessage(e).queue();
        } else {
            event.getChannel().sendMessage(String.format(
                    "Usage: `%snewdoc Game Name https://docs.google.com/document/d/...`", Constants.COMMAND_PREFIX)
            ).queue();
        }
    }

    private String formatLink(String link) throws IllegalArgumentException {
        if (!link.startsWith("https://")) { link = "https://" + link; }

        if (!link.startsWith("https://docs.google.com/document/d/")) { throw new IllegalArgumentException(); }

        link = "https://docs.google.com/document/d/" + link.split("/")[5]; // extract doc ID and add into template

        return link;
    }
}
