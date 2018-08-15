package projectRadish.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
  /**
   * Audio player for the guild.
   */
  public final AudioPlayer player;
  /**
   * Track scheduler for the player.
   */
  public final TrackScheduler scheduler;

  public TextChannel textChannel;

  /**
   * Creates a player and a track scheduler.
   * @param manager Audio player manager to use for creating the player.
   */
  public GuildMusicManager(AudioPlayerManager manager) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player, this);
    player.addListener(scheduler);
  }

    /**
     * Sends a message via the text channel
     * Used to report queue ending or errors.
     * @param message The message to send.
     */
  public void sendMessage(String message) {
      this.textChannel.sendMessage(message).queue();
  }

  /**
   * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
   */
  public AudioPlayerSendHandler getSendHandler() {
    return new AudioPlayerSendHandler(player);
  }
}
