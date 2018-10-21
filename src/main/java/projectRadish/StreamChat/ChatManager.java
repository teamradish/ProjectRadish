package projectRadish.StreamChat;

import projectRadish.Configuration;
import projectRadish.Constants;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class ChatManager {
    TwitchChat TPEchat;

    public InputValidator inputValidator;

    public ChatManager() {
        inputValidator = new InputValidator();
    }

    public void mainloop() {
        connect();

        // We successfully connected
        while (true) {

            // Send & Receive Data
            try {
                TPEchat.update();
            } catch (IOException e) { // We lost connection
                connect();
            }

            // Read any messages we got from TPE's Chat (detect valid inputs)
            Queue<TwitchMessage> messages = TPEchat.popMessages();
            while (messages.size() > 0) {
                TwitchMessage m = messages.poll();
                if (m.getMessageType() == MessageType.PRIVMSG) {
                    boolean valid = inputValidator.isValidInput(m.getContents());
                    if (valid) {
                        System.out.println(m.getSender()+": "+m.getContents());
                        Map<String, Long> inputCounts = Configuration.getInputCounts();
                        Long currentCount = inputCounts.getOrDefault(m.getSender(), 0L); // Treat as 0 for a new user
                        inputCounts.put(m.getSender(), currentCount+1);
                    } else {
                        System.err.println(m.getSender()+": "+m.getContents());
                    }
                }
            }

            // Java's mechanisms for selecting on multiple sockets kinda suck
            // So sleep the thread a little to mitigate the spin-wait - we don't need to respond THAT fast anyway.
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e2) { /* Doesn't matter */ }
        }
    }

    private void connect() {
        TPEchat = new TwitchChat("twink_pr");//Constants.STREAM_NAME);
        System.err.println("ERROR\nERROR\nERROR\nERROR\nERROR\nERROR\nERROR\nERROR\nERROR\nWRONG TWITCH CHANNEL");
        boolean connected = false;
        while (!connected) {
            try { // Attempt to connect to the chats
                TPEchat.connect();
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
