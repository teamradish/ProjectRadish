package projectRadish.StreamChat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitchMessage {

    private MessageType messageType;
    private String sender;
    private String contents;
    private String channel;


    public TwitchMessage(String raw_input) { process(raw_input); }

    private void process(String raw_input) {
        // Create a Pattern object
        Matcher chatRegex = Pattern.compile(ChatConfig.CHAT_MSG).matcher(raw_input);
        Matcher joinRegex = Pattern.compile(ChatConfig.JOIN_MSG).matcher(raw_input);
        Matcher serverRegex = Pattern.compile(ChatConfig.SERVER_MSG).matcher(raw_input);
        Matcher server2Regex = Pattern.compile(ChatConfig.SERVER_MSG_2).matcher(raw_input);

        if (serverRegex.find()) {
            messageType = MessageType.SERVER;
            channel = "SERVER";
            sender = "Server";
            contents = serverRegex.group(1);
        } else if (server2Regex.find()) {
            messageType = MessageType.SERVER;
            channel = server2Regex.group(1);
            sender = "Server";
            contents = server2Regex.group(2);
        } else if (joinRegex.find()) {
            messageType = MessageType.JOIN;
            channel =  joinRegex.group(2);
            sender = "Server";
            contents = joinRegex.group(1) + " joined chat.";
        } else if (chatRegex.find()) {
            messageType = MessageType.PRIVMSG;
            channel = chatRegex.group(2);
            sender = chatRegex.group(1);
            contents = chatRegex.group(3);
            if (contents.startsWith("\u0001ACTION")) {
                contents = contents.replaceFirst("\u0001ACTION", "/me");
                contents = contents.substring(0, contents.length()-1);
            }
        } else {
            messageType = MessageType.UNKNOWN;
            sender = "Unrecognised format";
            contents = raw_input;
        }
    }

    public void print() {
        String output = "#"+this.channel + " | " + this.sender + ": " + this.contents;
        if (this.messageType == MessageType.PRIVMSG) {
            System.out.println(output);
        } else {
            System.err.println(output);
        }
    }

    // Getters
    public MessageType getMessageType() { return messageType; }
    public String getSender() { return sender; }
    public String getContents() { return contents; }
}
