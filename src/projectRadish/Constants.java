package projectRadish;

import net.dv8tion.jda.core.OnlineStatus;

public final class Constants {

	public static final String COMMAND_PREFIX = "?";

	public static final OnlineStatus DEFAULT_STATUS = OnlineStatus.DO_NOT_DISTURB;
	public static final String DEFAULT_GAME = "\"" + COMMAND_PREFIX + "\" = Command Prefix"; // Set to null for no initial game

    public static final String CommandPkgName = "projectRadish.Commands.";

	// Private Constructor to prevent instantiation
	private Constants() {}
}
