package projectRadish;

import net.dv8tion.jda.core.OnlineStatus;

public final class Constants {
	private static final String BotToken = "NDMxNjcwNDI3NDg0Njg0MzEx.DctCUg.ziZXxz5YefmK_qGFyssiU8_qLP4";
	public static String getBotToken() { return BotToken; }

	//private static final String AdminRoleID = "441484541899833355"; not needed for now
	
	private static String defaultGame = "VRchat"; // Set to null for no initial game
	public static String getDefaultGame() { return defaultGame; }

	private static OnlineStatus defaultStatus = OnlineStatus.DO_NOT_DISTURB;
	public static OnlineStatus getDefaultStatus() { return defaultStatus; }

	// Private Constructor to prevent instantiation
	private Constants() {}
}
