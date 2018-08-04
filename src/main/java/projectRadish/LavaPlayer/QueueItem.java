package projectRadish.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class QueueItem {
    private AudioTrack track;
    private String requester;
    private int repeats;        // Default 0 - play once.
    private int playsLeft;


    public QueueItem(AudioTrack track, String requester, int repeats) {
        this.track = track;
        this.requester = requester;
        this.repeats = repeats;
        this.playsLeft = repeats;
    }

    public boolean repeat() {
        this.playsLeft -= 1;
        return (this.playsLeft >= 0);
    }

    public AudioTrack getTrack() { return this.track; }
    public String getRequester() { return this.requester; }

    public int getRepeats() { return this.repeats; }
    public int getRepeatsLeft() { return this.playsLeft; }
}
