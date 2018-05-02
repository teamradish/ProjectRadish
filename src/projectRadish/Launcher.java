package projectRadish;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.*;
import net.dv8tion.jda.core.exceptions.*;

public class Launcher {
	public static void main(String[] args) throws LoginException, RateLimitedException, InterruptedException {
		JDA api = new JDABuilder(AccountType.BOT)
				.setToken("NDMxNjcwNDI3NDg0Njg0MzEx.DctCUg.ziZXxz5YefmK_qGFyssiU8_qLP4")
				.addEventListener(new PingPongBot())
				.buildBlocking();
	}

}
