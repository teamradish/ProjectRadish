package projectRadish;

import net.dv8tion.jda.core.OnlineStatus;

public final class Constants {

	public static final OnlineStatus DEFAULT_STATUS = OnlineStatus.DO_NOT_DISTURB;
	public static final String DEFAULT_GAME = "? = Command Prefix"; // Set to null for no initial game

    /**
     * The command character used.
     */
    public static final String COMMAND_CHAR = "?";

	// Private Constructor to prevent instantiation
	private Constants() {}
}
