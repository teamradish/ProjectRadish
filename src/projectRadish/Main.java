package projectRadish;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

import javax.security.auth.login.LoginException;

public class Main
{
    /**
     * This is the method where the program starts.
     */
    public static void main(String[] args) {
        Configuration.loadConfiguration();
        //We construct a builder for a BOT account. If we wanted to use a CLIENT account
        // we would use AccountType.CLIENT

        JDA jda;
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(Configuration.getBotToken())           //The token of the account that is logging in.
                    .addEventListener(new MessageListener())  //An instance of a class that will handle events.
                    .buildBlocking();  //There are 2 ways to login, blocking vs async. Blocking guarantees that JDA will be completely loaded.
            initialise(jda);
        } catch (LoginException e) {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        } catch (InterruptedException e) {
            //Due to the fact that buildBlocking is a blocking method, one which waits until JDA is fully loaded,
            // the waiting can be interrupted. This is the exception that would fire in that situation.
            //As a note: in this extremely simplified example this will never occur. In fact, this will never occur unless
            // you use buildBlocking in a thread that has the possibility of being interrupted (async thread usage and interrupts)
            e.printStackTrace();
        }
    }

    public static void initialise(JDA jda) {
        if (Constants.DEFAULT_GAME != null) {
            jda.getPresence().setGame(Game.of(GameType.DEFAULT, Constants.DEFAULT_GAME));
        }
        if (Constants.DEFAULT_STATUS != null) {
            jda.getPresence().setStatus(Constants.DEFAULT_STATUS);
        }
    }
}