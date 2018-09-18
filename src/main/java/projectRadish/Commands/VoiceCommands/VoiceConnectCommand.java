package projectRadish.Commands.VoiceCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.MessageListener;

public final class VoiceConnectCommand extends BaseCommand
{
    private String description = null;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    @Override
    public void Initialize() {
        super.Initialize();

        description = "Rejoins the Voice Channel, and resumes playing music, unless the queue is empty.\n" +
                "If used when already connected, briefly disconnects and rejoins - this can solve a few issues with audio playback";
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        MessageListener.vp.disconnectFromVoiceChannel(event.getTextChannel());
        MessageListener.vp.connectToVoiceChannel(event.getGuild().getAudioManager(), event.getTextChannel());
    }
}
