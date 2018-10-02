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