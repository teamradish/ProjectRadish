package projectRadish.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
  private final AudioPlayer player;
  private final BlockingQueue<AudioTrack> queue;
  private final GuildMusicManager manager;

  /**
   * @param player The audio player this scheduler uses
   */
  public TrackScheduler(AudioPlayer player, GuildMusicManager musicManager) {
    this.player = player;
    this.queue = new LinkedBlockingQueue<>();
    this.manager = musicManager;
  }

  /**
   * Add the next track to queue or play right away if nothing is in the queue.
   *
   * @param track The track to play or add to queue.
   */
  public void queue(AudioTrack track) {
    // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
    // something is playing, it returns false and does nothing. In that case the player was already playing so this
    // track goes to the queue instead.
    if (!player.startTrack(track, true)) {
      queue.offer(track);
    }
  }

  /**
   * Start the next track, stopping the current one if it is playing.
   */
  public void nextTrack() {
      // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
      // giving null to startTrack, which is a valid argument and will simply stop the player.
      AudioTrack next = queue.poll();
      if (next == null) { this.manager.sendMessage("End of queue reached."); }
      player.startTrack(next, false);
  }

  /**
   * Returns the next AudioTrack in the queue without removing it.
   * @return The AudioTrack at the start of the queue. null if none exists.
   */
  public AudioTrack peekTrack()
  {
      return queue.peek();
  }

  /**
   * Returns a copy of the queue.
   * @return a List copy of the music queue
   */
  public List<AudioTrack> getQueue() {
    List<AudioTrack> queueCopy = new ArrayList<>();
    for (AudioTrack t: queue) {
      queueCopy.add(t);
    }
    return queueCopy;
  }

  public void clear() {
    queue.clear();
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
    if (endReason.mayStartNext) {
      nextTrack();
    }
  }
}
