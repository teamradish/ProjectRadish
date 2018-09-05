package projectRadish.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import projectRadish.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoicePlayer {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public VoicePlayer() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(final MessageReceivedEvent event, String link, int plays) {
        TextChannel channel = event.getTextChannel();
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        String requester = event.getAuthor().getName();

        playerManager.loadItemOrdered(musicManager, link, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                QueueItem item = new QueueItem(track, requester, plays);

                String lenString = Utilities.getTimeStringFromMs(item.getLength());
                if (item.isStream()) { lenString = "Stream"; }

                String plays = (item.getPlays() == 1) ? "" : "x"+String.valueOf(item.getPlays()); // Hide if only 1 play
                channel.sendMessage(String.format(
                        "Adding to queue: **%s** %s\n" +
                        "**[ %s ]**", item.getTitle(), plays, lenString)).queue();

                play(channel.getGuild(), musicManager, item);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) { // Still dunno how these work.
                long totalDuration = 0;
                for (AudioTrack track : playlist.getTracks()) {
                    QueueItem item = new QueueItem(track, requester, 1);
                    totalDuration += item.getLength();
                    play(channel.getGuild(), musicManager, item);
                }

                String durationString = Utilities.getTimeStringFromMs(totalDuration);
                channel.sendMessage(String.format(
                        "Adding playlist to queue: **%s**\n" +
                        "Size: %s Tracks\n" +
                        "**[ %s ]**", playlist.getName(), playlist.getTracks().size(), durationString)).queue();
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Couldn't find a song at `" + link + "`").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public QueueItem getItem(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        return musicManager.scheduler.getCurrentItem();
    }

    public boolean isPlayingTrack(TextChannel channel)
    {
        return (getItem(channel) != null);
    }

    public boolean isPaused(TextChannel channel)
    {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        return musicManager.player.isPaused();
    }

    public boolean isPlaying(TextChannel channel)
    {
        return (isPlayingTrack(channel) == true && isPaused(channel) == false);
    }

    public QueueItem peekItem(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        return musicManager.scheduler.peekTrack();
    }

    public List<QueueItem> getQueue(TextChannel channel) {
        List<QueueItem> copy = new ArrayList<>();

        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        musicManager.scheduler.getQueue(copy);
        return copy;
    }

    public void getQueue(TextChannel channel, List<QueueItem> list) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        musicManager.scheduler.getQueue(list);
    }

    private void play(Guild guild, GuildMusicManager musicManager, QueueItem item) {
        connectToVoiceChannel(guild.getAudioManager(), musicManager.textChannel);
        musicManager.scheduler.queue(item);
    }

    public void pause(TextChannel channel)
    {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        musicManager.scheduler.pause();
    }

    public void resume(TextChannel channel)
    {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        musicManager.scheduler.resume();
    }

    public void skipItem(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        musicManager.scheduler.nextItem();
    }

    public void clearQueue(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.textChannel = channel;
        musicManager.scheduler.clear();
    }

    public static void disconnectFromVoiceChannel(TextChannel channel) {
        AudioManager audioManager = channel.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
        }
    }

    public static void connectToVoiceChannel(AudioManager audioManager, TextChannel channel) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                if (voiceChannel.getName().toLowerCase().contains("music")) {
                    audioManager.openAudioConnection(voiceChannel);
                    return; // Exit function so we don't play in multiple channels
                }
            }

            // If we make it this far, we didn't find any "music" channels
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                // So just play in first audio channel we find
                audioManager.openAudioConnection(voiceChannel);
                return;
            }

            // If we make it this far, there were no voice channels
            channel.sendMessage("No voice channels found.\n" +
                    "This could be due to the voice channels' visibility permissions.").queue();

        }
    }
}
