package projectRadish;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import projectRadish.StreamChat.ChatConfig;
import projectRadish.StreamChat.ChatManager;
import projectRadish.StreamChat.TwitchChat;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main
{
    /**
     * JDA reference.
     */
    private static JDA jda = null;

    /**
     * This is the method where the program starts.
     */
    public static void main(String[] args) {
        //Kimimaru: In the event of an unhandled exception, use this handler
        CrashHandler crashHandler = new CrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        Thread.currentThread().setUncaughtExceptionHandler(crashHandler);

        Configuration.loadConfiguration();
        //We construct a builder for a BOT account. If we wanted to use a CLIENT account
        // we would use AccountType.CLIENT

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(Configuration.getBotToken())           //The token of the account that is logging in.
                    .addEventListener(new MessageListener())  //An instance of a class that will handle events.
                    .setAudioSendFactory(new NativeAudioSendFactory())
                    .build().awaitReady();  //There are 2 ways to login, blocking vs async. Blocking guarantees that JDA will be completely loaded.
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

        // JDA runs in a different thread, so we'll use this thread to manage twitch chats
        ChatManager chatManager = new ChatManager();
        chatManager.mainloop(); // Never returns
    }

    public static void initialise(JDA jda) {
        String defaultGame = Constants.getDefaultGame();

        if (defaultGame != null) {
            jda.getPresence().setGame(Game.of(GameType.DEFAULT, defaultGame));
        }
        if (Constants.DEFAULT_STATUS != null) {
            jda.getPresence().setStatus(Constants.DEFAULT_STATUS);
        }
    }
}