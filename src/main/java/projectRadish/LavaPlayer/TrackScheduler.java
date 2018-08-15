package projectRadish.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Objects.isNull;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<QueueItem> queue;
    private final GuildMusicManager manager;

    private QueueItem currentItem;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player, GuildMusicManager musicManager) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.manager = musicManager;
    }

    /**
     * Add the next item to the queue, and play right away if nothing is in the queue.
     *
     * @param item The QueueItem to play and/or add to queue.
     */
    public void queue(QueueItem item) {
        if (isNull(currentItem)) {
            // If queue was empty
            currentItem = item;
            player.startTrack(item.getTrack(), true);
        } else {
            // Queue already had something in it
            queue.offer(item);
        }
    }

    /**
     * Start the next track.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        boolean shouldRepeat;
        if (!isNull(currentItem)) {
            shouldRepeat = currentItem.repeat();
        } else {
            shouldRepeat = false;
        }

        if (shouldRepeat) {
            player.startTrack(currentItem.getTrack(), false);
        } else {
            nextItem();
        }
    }

    /**
     * Start the next item, stopping the current one if it is playing.
     */
    public void nextItem() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        QueueItem next = queue.poll();
        currentItem = next;
        if (isNull(next)) {
            player.startTrack(null, false); // Stop player
            this.manager.sendMessage("End of queue reached.");
        } else {
            player.startTrack(next.getTrack(), false);
        }
    }

    /**
     * Returns the next AudioTrack in the queue without removing it.
     *
     * @return The AudioTrack at the start of the queue. null if none exists.
     */
    public QueueItem peekTrack() {
        return queue.peek();
    }

    /**
     * Returns a copy of the queue.
     *
     * @return a List copy of the music queue
     */
    public List<QueueItem> getQueue() {
        List<QueueItem> queueCopy = new ArrayList<>();
        for (QueueItem item : queue) {
            queueCopy.add(item);
        }
        return queueCopy;
    }

    public void pause()
    {
        player.setPaused(true);
    }

    public void resume()
    {
        player.setPaused(false);
    }

    public void clear() {
        queue.clear();
    }

    public QueueItem getCurrentItem() {
        return this.currentItem;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}
