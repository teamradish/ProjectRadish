package projectRadish.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import projectRadish.Utilities;

public class QueueItem {
    private AudioTrack track;
    private String requester;
    private final int plays;        // Default 1 - play once.
    private int playsCount;


    public QueueItem(AudioTrack track, String requester, int plays) {
        this.track = track;
        this.requester = requester;

        if (this.isStream()) {
            this.plays = 1;
        } else {
            this.plays = Utilities.clamp(plays, 1, 999);
        }

        this.playsCount = 0;
    }

    public boolean repeat() {
        this.track = this.track.makeClone();
        this.playsCount++;
        return (this.playsCount < this.plays);
    }

    public AudioTrack getTrack() { return this.track; }
    public String getLink() { return this.track.getInfo().uri; }
    public String getTitle() { return this.track.getInfo().title; }
    public String getRequester() { return this.requester; }
    public int getPlays() { return this.plays; }

    public boolean isSeekable() { return this.track.isSeekable(); }
    public boolean isStream() { return this.track.getInfo().isStream; }

    public long getPosition() { return this.track.getInfo().length * this.playsCount + this.track.getPosition(); }

    public void setPosition(Long pos) {
        if (pos >= this.getLength() || pos < 0) { return; } // Ignore nonsense inputs

        this.playsCount = (int) (pos / this.track.getInfo().length); // integer division
        this.track.setPosition(pos % this.track.getInfo().length);
    }

    public long getLength() {
        if (this.isStream()) {
            return 0;
        } else {
            return this.track.getInfo().length * this.plays;
        }
    }
}
