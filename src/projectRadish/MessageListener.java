package projectRadish;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import projectRadish.Commands.*;

import java.util.HashMap;
import java.util.Map;

public class MessageListener extends ListenerAdapter {

    //Hardcoded for now, but we might want to load them in from config somewhere later
    private HashMap<String, BaseCommand> Commands = new HashMap<>();

    /**
     * Constructor.
     */
    public MessageListener()
    {
        //Initialize commands
        SetUpCommandList();

        InitCommandList();
    }

    private void SetUpCommandList()
    {
        Commands.put("say", new SayCommand());
        Commands.put("abbreviate", new AbbreviateCommand());
        Commands.put("doc", new DocCommand());
        Commands.put("guess", new GuessCommand());
        Commands.put("owo", new OwoCommand());
        Commands.put("roll", new RollCommand());
        Commands.put("alldocs", new AllDocsCommand());
        Commands.put("status", new StatusCommand());
        Commands.put("playing", new GameCommand());
        Commands.put("streaming", new StreamingCommand());
        Commands.put("listening to", new ListenCommand());
        Commands.put("watching", new WatchingCommand());
        Commands.put("emotehell", new EmoteHellCommand());
        Commands.put("reload", new ReloadCommand());
    }

    private void InitCommandList()
    {
        for (Map.Entry<String, BaseCommand> entry : Commands.entrySet())
        {
            entry.getValue().Initialize();
        }
    }

    /**
     * NOTE THE @Override!
     * This method is actually overriding a method in the ListenerAdapter class! We place an @Override annotation
     *  right before any method that is overriding another to guarantee to ourselves that it is actually overriding
     *  a method from a super class properly. You should do this every time you override a method!
     *
     * As stated above, this method is overriding a hook method in the
     * {@link net.dv8tion.jda.core.hooks.ListenerAdapter ListenerAdapter} class. It has convenience methods for all JDA events!
     * Consider looking through the events it offers if you plan to use the ListenerAdapter.
     *
     * In this example, when a message is received it is printed to the console.
     *
     * @param event
     *          An event containing information about a {@link net.dv8tion.jda.core.entities.Message Message} that was
     *          sent in a channel.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!shouldIgnore(event)) {
            handleMessage(event);
        }
    }

    /**
     * Ignore the message if...
     * @param event MessageReceivedEvent
     * @return Whether to ignore the message (bool)
     */
    private boolean shouldIgnore(MessageReceivedEvent event) {
        boolean ignore = false;
        User author = event.getAuthor();
        String myId = event.getJDA().getSelfUser().getId();

        // We sent the message (don't talk to yourself)
        if (author.getId().equals(myId)) { ignore = true; }

        // Standard practice is to ignore other bots (avoids loops and things)
        if (author.isBot()) { ignore = true; }

        return ignore;
    }

    private void handleMessage(MessageReceivedEvent event) {

        //Event specific information
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.

        String msg = message.getContentDisplay();              //This returns a human readable version of the Message. Similar to
        // what you would see in the client.

        // Print message to console
        if (event.isFromType(ChannelType.TEXT))         //If this message was sent to a Guild TextChannel
        {
            TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
            System.out.printf("(%s)[%s]<%s>: %s\n",
                    event.getGuild().getName(), textChannel.getName(), event.getMember().getEffectiveName(), msg);
        }
        else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
        {
            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }

        //Find and execute the command for message
        carryOutCommandForMsg(event, msg);
    }

    private void carryOutCommandForMsg(MessageReceivedEvent event, String msg)
    {
        String prefix = Constants.COMMAND_PREFIX;
        //If the message starts with the command character, look for a command to perform
        if (msg.startsWith(prefix))
        {
            msg = msg.substring(prefix.length()); // remove prefix

            String cmdMatch = null;
            for (String cmdName: Commands.keySet()) {
                if (msg.startsWith(cmdName)) { // potential match
                    try {
                        if (msg.charAt(cmdName.length()) == ' ') {
                            cmdMatch = cmdName; // match with arguments
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        cmdMatch = cmdName; // match with no arguments
                    }
                }
            }

            if (cmdMatch != null) {
                msg = msg.substring(cmdMatch.length()); // Remove command name
                msg = msg.trim(); // remove leading and trailing whitespace
                Commands.get(cmdMatch).ExecuteCommand(msg, event);
            }
        }
    }
}
        