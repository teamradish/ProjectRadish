package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;
import sun.plugin2.message.Message;

public final class VoicePlayCommand extends BaseCommand {
    private String description = null;

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

        String[] args = content.split(" ");
        if (args.length == 2) {
            link = args[0];
            playsArg = args[1].toLowerCase();
            // Remove the x from before the number if present
            if (playsArg.startsWith("x")) { playsArg = playsArg.substring(1); }
        }

        if (args.length == 3 && (args[1].equals("x") || args[1].equals("X"))) { // they put a space between the x and the number
            link = args[0];
            playsArg = args[2];
        }


        // Strip <tag sign things> if present
        if (link.startsWith("<") && link.endsWith(">")) { link = link.substring(1, link.length() - 1); }

        int plays;
        try {
            plays = Integer.parseInt(playsArg);
        } catch(NumberFormatException e) {
            link = content;
            plays = 1;
        }

        if (plays < 1) {
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
