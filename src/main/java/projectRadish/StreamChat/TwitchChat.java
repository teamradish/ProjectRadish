package projectRadish.StreamChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitchChat {
    private Socket sock;
    private BufferedReader in;
    private PrintWriter out;

    private String channel;

    private Queue<String> pendingChats; // Messages to send
    private String last_message_sent = "";
    private long last_chat_time = 0;
    private long last_response_time;
    public Queue<TwitchMessage> messages; // Messages received

    public TwitchChat(String channel) {
        this.channel = channel;
        this.messages = new LinkedBlockingQueue<>();
        this.pendingChats = new LinkedBlockingQueue<>();
        this.last_response_time = System.currentTimeMillis();
    }

    public void connect() throws IOException {
        this.sock = new Socket(ChatConfig.HOST, ChatConfig.PORT);
        this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.out = new PrintWriter(sock.getOutputStream(), true);

        // Log in to Twitch
        out.println(String.format("PASS %s\r\n", ChatConfig.PASS));
        out.println(String.format("NICK %s\r\n", ChatConfig.NICK));
        out.println(String.format("JOIN #%s\r\n", this.channel));
    }

    public void disconnect() {
        try {
            in.close();
            sock.close();
        } catch (IOException e) { /* We tried */ }
    }

    private void pong() {
        out.println(String.format(ChatConfig.PONG));
    }

    public void send(String message) {
        pendingChats.add(message);
    }

    /**
     * Call this function often to keep the connection to Twitch alive
     * This function also handles the "admin" of the connection such as receiving and sending messages.
     *
     * @throws IOException if the connection dies
     */
    public void update() throws IOException {
        // Receive messages from twitch
        try {
            if (in.ready()) {
                String message = in.readLine();
                this.last_response_time = System.currentTimeMillis();
                if (message.equals(ChatConfig.PING)) {
                    System.out.println("-PING-");
                    pong();
                } else {
                    messages.add(new TwitchMessage(message));
                }
            }
        } catch (IOException e) {
            System.err.println("There was a problem receiving data from Twitch.");
            e.printStackTrace();
        }

        // Send messages to twitch
        long inactivity_length = System.currentTimeMillis() - last_chat_time;
        boolean off_cooldown = inactivity_length > ChatConfig.CHAT_WAIT_TIME;
        if (off_cooldown && pendingChats.size() > 0) {
            String msg = pendingChats.poll(); // Take first reply from pending list
            if (msg.equals(last_message_sent) && inactivity_length < 31000) { msg = msg + " ."; } // Bypass same-message-twice filter

            out.println(String.format("PRIVMSG #%s :%s\r\n", channel, msg));
            this.last_message_sent = msg;
            this.last_chat_time = System.currentTimeMillis();
        }

        // Reconnect if we suspect we've been disconnected
        long silence_time = System.currentTimeMillis() - last_response_time;
        if (ChatConfig.RECONNECT_TIMEOUT > 0 && ChatConfig.RECONNECT_TIMEOUT < silence_time) {
            System.err.println("Connection timed out, attemting to reconnect...");
            disconnect();
            connect(); // This is where IOException can occur
            System.err.println("Reconnect seems to have succeeded.");
            last_response_time = System.currentTimeMillis();
        }
    }

    public Queue<TwitchMessage> popMessages() {
        Queue<TwitchMessage> messageQueue = new LinkedBlockingQueue<>(messages);
        messages.clear();
        return messageQueue;
    }

    public String getChannel() { return this.channel; }
}
