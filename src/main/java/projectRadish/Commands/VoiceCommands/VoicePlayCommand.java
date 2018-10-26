package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.Configuration;
import projectRadish.MessageListener;

import java.util.Map;

public final class VoicePlayCommand extends BaseCommand {
    private String description;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean canBeUsedViaPM() {
        return false;
    }

    @Override
    public void Initialize() {
        super.Initialize();

        description = "Queues up the song your input links to.\n" +
                "The bot will resume playback and join the Voice Channel if it was not already connected.\n" +
                "You can also use eg. `<link> x3` to have the song loop 3 times.\n" +
                "To add a YouTube playlist, just link any video that's part of it (has `&list=` in the link)\n" +
                "Playlists can't be looped though because that'd be going too far.\n";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event) {
        String link = content;
        String playsArg = "1"; // safe default

        if (content.isEmpty()) {
            event.getChannel().sendMessage("Please provide a link to the audio you want to play.").queue();
            return;
        }

        String[] args = content.split(" ");

        // Check if we were given a link, if not, the user likely meant to use "search" instead
        if(!args[0].contains(".")) { // If the first word doesn't contain a period, it can't be a link
            Map<String, String> cmds = Configuration.getCommands();
            for (Map.Entry<String, String> cmd: cmds.entrySet()) {              // Make sure !search hasn't been removed
                if (cmd.getValue().equals("VoiceCommands.VoiceSearchCommand")) {// and if it hasn't
                                                                                // Then swap to !search instead
                    MessageListener.getCommands().get(cmd.getKey()).ProcessCommand(content, event);
                    return; // No need to continue with this command.
                }
            }
        }


        if (args.length == 2) {
            link = args[0];
            playsArg = args[1].toLowerCase();
            // Remove the x from before the number if present
            if (playsArg.startsWith("x")) { playsArg = playsArg.substring(1); }
        }

        if (args.length == 3 && (args[1].toLowerCase().equals("x"))) { // they put a space between the x and the number
            link = args[0];
            playsArg = args[2];
        }


        // Strip <tag sign things> if present
        if (link.startsWith("<") && link.endsWith(">")) { link = link.substring(1, link.length() - 1); }

        int plays;
        try {
            plays = Integer.parseInt(playsArg);
            if (plays < 1) { // Someone's trying to be cheeky
                link = content; // Just play it once like usual
                plays = 1;
            }
        } catch(NumberFormatException e) { // No repeat, or invalid number
            link = content;
            plays = 1;
        }

        //Kimimaru: Unpause if no track is being played
        if (MessageListener.vp.isPlayingTrack(event.getTextChannel()) == false)
        {
            MessageListener.vp.resume(event.getTextChannel());
        }

        MessageListener.vp.loadAndPlay(event, link, plays);
    }
}
