package projectRadish;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.*;

/**
 * A wrapper for a MessageReceivedEvent for easily accessible information.
 */
public class MessageInfoWrapper
{
    private MessageReceivedEvent msgEvent = null;
    private String CmdMsgContent = ""; //Trimmed message content without the command in it
    private Vector<String> CommandArgs = new Vector<String>(); //Command arguments

    public MessageInfoWrapper(MessageReceivedEvent event)
    {
        msgEvent = event;

        //We don't want null here
        if (msgEvent == null)
        {
            throw new IllegalArgumentException("event is null.");
        }

        //Parse to retrive info
        ParseInfo();
    }

    public MessageReceivedEvent getMsgEvent()
    {
        return msgEvent;
    }

    public MessageChannel getChannel()
    {
        return msgEvent.getChannel();
    }

    public User getAuthor()
    {
        return msgEvent.getAuthor();
    }

    public Message getMessage()
    {
        return msgEvent.getMessage();
    }

    /**
     * The message content without the command.
     * @return
     */
    public String getMsgContent()
    {
        return CmdMsgContent;
    }

    public Vector<String> getCmdArgs()
    {
        return CommandArgs;
    }

    private void ParseInfo()
    {
        final String separator = " ";

        //Trim string
        String trimmedMsg = getMessage().getContentDisplay().trim();

        //Split by separator character
        String[] arr = trimmedMsg.split(separator);

        //Add all arguments, which excludes the command part of the string
        for (int i = 1; i < arr.length; i++)
        {
            CmdMsgContent += arr[i];
            CommandArgs.add(arr[i]);

            //Add the separator if this iteration isn't the last
            if ((i + 1) < arr.length) CmdMsgContent += separator;
        }
    }
}
