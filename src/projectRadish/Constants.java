package projectRadish;

import net.dv8tion.jda.core.OnlineStatus;

public final class Constants {

	public static final String COMMAND_PREFIX = "mods are asleep post !";

	public static final OnlineStatus DEFAULT_STATUS = OnlineStatus.DO_NOT_DISTURB;
	public static final String DEFAULT_GAME = "\"" + COMMAND_PREFIX + "\" = Command Prefix"; // Set to null for no initial game


	// Private Constructor to prevent instantiation
	private Constants() {}
}
