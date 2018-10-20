package projectRadish.StreamChat;

public class ChatConfig {
    public static final String NICK = "twink_pr"; // your Twitch username, lowercase
    public static final String PASS = "oauth:x2ckcx9t3weg8iugeuwgdrgfko3nku"; // your Twitch OAuth token

    public static final String HOST = "irc.twitch.tv";              // the Twitch IRC server
    public static final int PORT = 6667;                         // always use port 6667!

    public static final String PING = "PING :tmi.twitch.tv";
    public static final String PONG = "PONG :tmi.twitch.tv\r\n";

    // Message Regexes
    public static final String CHAT_MSG = "\\A:(\\w+)!\\w+@\\w+\\Q.tmi.twitch.tv PRIVMSG #\\E(\\w+) :(.*)\\Z";
    public static final String JOIN_MSG = "\\A:(\\w+)!\\w+@\\w+\\Q.tmi.twitch.tv JOIN #\\E(.*)\\Z";
    public static final String SERVER_MSG = "\\A\\Q:tmi.twitch.tv\\E \\d\\d\\d \\w+ :(.*)\\Z";
    public static final String SERVER_MSG_2 = "\\A:\\w+\\Q.tmi.twitch.tv\\E \\d\\d\\d \\w+(?: =)? #(\\w+) :(.*)\\Z";

    public static final int CHAT_WAIT_TIME = 2000; // (Less than 1600ms between messages can get you spam-banned)

    public static final int RECONNECT_TIMEOUT = 500 * 1000;    // If we don't receive any packets from twitch for this many millis, try to refresh connection
                                                        // Usual delay between pings is about 300,000ms
                                                        // Set to 0 to never refresh connection
}
