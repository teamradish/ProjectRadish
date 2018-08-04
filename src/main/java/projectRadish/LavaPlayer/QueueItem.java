package projectRadish.LavaPlayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class QueueItem {
    private AudioTrack track;
    private String requester;
    private int plays;        // Default 1 - play once.
    private int playsCount;


    public QueueItem(AudioTrack track, String requester, int plays) {
        this.track = track;
        this.requester = requester;
        this.plays = plays;
        if (this.plays < 1) { this.plays = 1; }
        if (this.plays > 999) {this.plays = 999; }

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

    public long getPosition() { return this.track.getDuration() * this.playsCount + this.track.getPosition(); }
    public long getLength() { return this.track.getDuration() * this.plays; }
}
