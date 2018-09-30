package projectRadish.StreamChat;

import projectRadish.Constants;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.TimeUnit;


public class ChatManager {
    TwitchChat TPEchat;
    TwitchChat LINKchat;

    public void mainloop() {
        connect();

        // We successfully connected
        while (true) {

            // Send & Receive Data
            try {
                TPEchat.update();
                LINKchat.update();
            } catch (IOException e) { // We lost connection
                connect();
            }

            // Read any messages we got from TPE's Chat (detect valid inputs)
            Queue<TwitchMessage> messages = TPEchat.popMessages();
            while (messages.size() > 0) {
                TwitchMessage m = messages.poll();
                if (m.getMessageType() == MessageType.PRIVMSG) {
                    boolean valid = ValidInput.isValidInput(m.getContents());
                    if (valid) { System.out.println(m.getContents());
                    } else { System.err.println(m.getContents()); }
                } else { m.print(); }
            }

            // Read and handle any messages sent to the bot's chat (handle profile linking)
            messages = LINKchat.popMessages();
            while (messages.size() > 0) {
                TwitchMessage m = messages.poll();
                if (m.getMessageType() == MessageType.PRIVMSG) {
                    boolean valid = ValidInput.isValidInput(m.getContents());
                    if (valid) {
                        System.out.println(m.getContents());
                        LINKchat.send("Valid!");
                    } else {
                        System.err.println(m.getContents());
                        LINKchat.send("Invalid.");}
                } else { m.print(); }
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
