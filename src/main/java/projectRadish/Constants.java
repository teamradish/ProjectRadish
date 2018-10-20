package projectRadish;

import net.dv8tion.jda.core.OnlineStatus;

import java.time.ZoneId;

public final class Constants {

	public static final String BOT_NAME = "Twink";

	public static final OnlineStatus DEFAULT_STATUS = OnlineStatus.DO_NOT_DISTURB;
	public static String getDefaultGame()
    {
        return "\"" + Configuration.getCommandPrefix() + "\" = Command Prefix"; // Set to null for no initial game
    }

    public static final String PkgName = "projectRadish";
    public static final String CommandPkgName = PkgName + ".Commands";

    /**
     * Client ID for the Project Radish bot.
     * This is generated from Twitch when adding a developer application.
     * More info: https://dev.twitch.tv/dashboard
     */
    public static final String CLIENT_ID = "rrixp6h00ku9ic34l1mbvilkl7qi8c";
    public static final String CLIENT_SECRET = "qqjodp0493c67ebng2yhurjakh4jk5";

    public static final String STREAM_ENDPOINT =  "https://api.twitch.tv/helix/streams?user_login=";
    public static final String GAME_ENDPOINT = "https://api.twitch.tv/helix/games?id=";
    public static final String USER_ENDPOINT = "https://api.twitch.tv/helix/users?id=";
    public static final String BITS_ENDPOINT = "https://api.twitch.tv/helix/bits/leaderboard?count={count}";

    public static final String OAUTH_ENDPOINT = "https://id.twitch.tv/oauth2/token?client_id={clientid}&client_secret={clientsecret}&grant_type=client_credentials&scope={scopes}";

    public static final String STREAM_NAME = "twitchplays_everything";
    public static final String STREAM_ID = "57950757";

    public static final ZoneId UTC_ZoneID = ZoneId.of("UTC");

    public static final String YOUTUBE_API_KEY = "AIzaSyDPbQ16j2mzjhpByclKK78u0sQD0Q6W7s8";
    public static final String YOUTUBE_API_URL = "https://content.googleapis.com/youtube/v3/search";

	// Private Constructor to prevent instantiation
	private Constants() {}
}
