package projectRadish.StreamChat;

import projectRadish.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class ChatManager {
    TwitchChat TPEchat;
    TwitchChat LINKchat;

    public void mainloop() {
        connect();

        long last_wp = 0;
        System.out.println(ValidInput.getInputPattern().replaceAll("\\Q(?:\\E", "("));

        boolean running = true;
        // We successfully connected
        while (running) {

            // Send & Receive Data
            try {
                TPEchat.update();
            } catch (IOException e) { // We lost connection
                connect();
            }

            // Read any messages we got from TPE's Chat (detect valid inputs)
            Queue<TwitchMessage> messages = TPEchat.popMessages();
            List<String> admins = Arrays.asList("taximadish", "kimimaru4000", "lucasortizny", "toagac");
            while (messages.size() > 0) {
                TwitchMessage m = messages.poll();
                if (m.getMessageType() == MessageType.PRIVMSG) {
                    if (!admins.contains(m.getSender()) && !m.getSender().contains("bot")) {
                        // USER ALERT, SHUT DOWN EVERYTHING
                        running = false;
                    }
                }
            }

            if (System.currentTimeMillis() - last_wp > 20*1000) {
                TPEchat.send("right1sleftupupright500msdown1s#2500msup");
                last_wp = System.currentTimeMillis();
            }

            // Java's mechanisms for selecting on multiple sockets kinda suck
            // So sleep the thread a little to mitigate the spin-wait - we don't need to respond THAT fast anyway.
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e2) { /* Doesn't matter */ }
        }
    }

    private void connect() {
        TPEchat = new TwitchChat(Constants.STREAM_NAME);
        LINKchat = new TwitchChat(ChatConfig.NICK);

        boolean connected = false;
        while (!connected) {
            try { // Attempt to connect to the chats
                TPEchat.connect();
                LINKchat.connect();
                connected = true;
            } catch (IOException e) { // We couldn't connect
                System.err.println("Couldn't connect to Twitch Chat. Retrying in 10secs...");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e1) { /* Doesn't matter */ }
                connected = false;
            }
        }
    }
}
