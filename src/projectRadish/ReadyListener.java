package projectRadish;



import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.exceptions.*;
import net.dv8tion.jda.core.hooks.*;
public class ReadyListener implements EventListener
{
    public static void main(String[] args)
            throws LoginException, RateLimitedException, InterruptedException
    {
        // Note: It is important to register your ReadyListener before building
        JDA jda = new JDABuilder(AccountType.BOT)
            .setToken(Constants.getBotToken())
            .addEventListener(new ReadyListener())
            .buildBlocking();
        System.out.println("done");
    }

	@Override
	public void onEvent(Event event) {
		//Work in progress
		
	}

    
}