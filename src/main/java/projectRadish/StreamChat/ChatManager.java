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

        long last_wp = System.currentTimeMillis() - 17*1000;
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
            List<String> admins = Arrays.asList("taximadish", "kimimaru4000", "lucasortizny", "yukioyamoto");
            while (messages.size() > 0) {
                TwitchMessage m = messages.poll();
                m.print();
                if (m.getMessageType() == MessageType.PRIVMSG) {
                    if (m.getContents().toLowerCase().equals("kill")) {
                        running = false;
                        TPEchat.send("Shutting down... :)");
                    }

                    if (!admins.contains(m.getSender()) && !m.getSender().contains("bot")) {
                        // USER ALERT, SHUT DOWN EVERYTHING
                        TPEchat.send("Yay, a person! KonCha Guess I can take a break! Pausing... :) ");
                        last_wp = Long.MAX_VALUE;
                    } else if (m.getContents().toLowerCase().equals("pause")) {
                        last_wp = Long.MAX_VALUE;
                    } else if (m.getContents().toLowerCase().equals("resume")) {
                        last_wp = 0;
                    }
                }
            }

            if (System.currentTimeMillis() - last_wp > 20*1000) {
                String myChat = "[ I am an auto-grind bot! I grind levels automatically when nobody is talking in chat :) ]*0right1sleftupupright500msdown1s#2500msup";
                TPEchat.send(myChat);
                System.out.println(myChat);
                last_wp = System.currentTimeMillis();
            }

            // Java's mechanisms for selecting on multiple sockets kinda suck
            // So sleep the thread a little to mitigate the spin-wait - we don't need to respond THAT fast anyway.
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e2) { /* Doesn't matter */ }
        }

        // Send & Receive Data
        try {
            TPEchat.update();
        } catch (IOException e) { // We lost connection
            // lmao who cAREAS
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
