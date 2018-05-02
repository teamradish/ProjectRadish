package projectRadish;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.*;
import net.dv8tion.jda.core.exceptions.*;

public class Launcher {
	public static void main(String[] args) throws LoginException, RateLimitedException, InterruptedException {
		JDA api = new JDABuilder(AccountType.BOT)
				.setToken(Constants.getBotToken())
				.addEventListener(new PingPongBot())
				.buildBlocking();
	}

}
