package projectRadish;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import projectRadish.Commands.*;
import projectRadish.LavaPlayer.VoicePlayer;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class MessageListener extends ListenerAdapter implements ConfigListener
{
    private static Map<String, BaseCommand> Commands = new HashMap<>();

    public static VoicePlayer vp = new VoicePlayer();

    private static final CrashHandler crashHandler = new CrashHandler();

    /**
     * Constructor.
     */
    public MessageListener()
    {
        //Load commands
        LoadCommandList();

        Configuration.removeConfigListener(this);
        Configuration.addConfigListener(this);
    }

    public void configLoaded()
    {
        //Reload the command list
        //Kimimaru: We probably don't want to do this every time the config is reloaded - just if the commands are modified
        LoadCommandList();
    }

    public void configSaved()
    {

    }

    private void LoadCommandList()
    {
        //Clear all commands
        Commands.clear();

        //Create the commands from the config
        HashMap<String, String> cmds = Configuration.getCommands();

        //Iterate with Iterator to allow modification while iterating
        for (Iterator<Entry<String,String>> it = cmds.entrySet().iterator(); it.hasNext();)
        {
            Entry<String, String> cmdEntry = it.next();

            String cmdName = cmdEntry.getKey();
            BaseCommand cmdObj = Utilities.createCommandFromString(cmdEntry.getValue());

            //Log if we couldn't instantiate a command
            if (cmdObj == null)
            {
                System.out.println("MessageListener.loadCommandList()\n" +
                        "COMMAND " + cmdName + " WAS NOT ADDED BECAUSE IT'S NULL!");
            }
            else
            {
                //Add the command and intialize it
                Commands.put(cmdName, cmdObj);
                cmdObj.Initialize();
            }
        }
    }

    public static Map<String, BaseCommand> getCommands() { return Commands; }

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
        //Kimimaru: Set unhandled exception handler
        Thread.currentThread().setUncaughtExceptionHandler(crashHandler);

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

        //Ignore if the user is silenced
        if (Utilities.isSilenced(author)) { ignore = true; }

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

            String lowerMsg = event.getMessage().getContentDisplay().toLowerCase();
            String meme = "this is so sad.? " + Constants.BOT_NAME.toLowerCase() + " play (.+)";
            Matcher memeRegex = Pattern.compile(meme).matcher(lowerMsg);
            if (memeRegex.matches()) { // Vital Functionality
                HashMap<String, String> cmds = Configuration.getCommands();
                String searchCommand = null;
                for(String cmdName: cmds.keySet()) {
                    if (cmds.get(cmdName).equals("VoiceCommands.VoiceSearchCommand")) {
                        searchCommand = cmdName;
                    }
                }
                if (!isNull(searchCommand)) {
                    System.out.println("MEME DETECTED\nINITIALISING MEME PROTOCOL");
                    msg = Configuration.getCommandPrefix() + searchCommand + " " + memeRegex.group(1);
                }
            }
        }
        else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
        {
            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
            String lowerMsg = event.getMessage().getContentDisplay().toLowerCase();
            if(lowerMsg.equals("prefix")){ // Taxi's failsafe for forgetting the prefix
                event.getChannel().sendMessage("\""+Configuration.getCommandPrefix()+"\"").queue();
            }
            if(lowerMsg.equals("inputbackup") && // Gotta back up them input counts
                    Configuration.getRadishAdmin().contains(event.getAuthor().getId())) {
                String reply = String.format("@ %d\n", System.currentTimeMillis());
                reply = reply + Configuration.getInputCounts().toString();
                event.getChannel().sendMessage(reply).queue();
            }
        }

        //Find and execute the command for message
        carryOutCommandForMsg(event, msg);
    }

    private void carryOutCommandForMsg(MessageReceivedEvent event, String msg)
    {
        String prefix = Configuration.getCommandPrefix();//Constants.COMMAND_PREFIX;
        //If the message starts with the command character, look for a command to perform
        if (msg.startsWith(prefix))
        {
            msg = msg.substring(prefix.length()); // remove prefix

            String cmdMatch = null;
            //Iterate with Iterator to allow modification while iterating
            for (Iterator<String> it = Commands.keySet().iterator(); it.hasNext();) {
                String cmdName = it.next();
                String lowerMsg = msg.toLowerCase();
                if (lowerMsg.startsWith(cmdName+" ") || lowerMsg.equals(cmdName)) { // match (with || without) args
                    cmdMatch = cmdName;
                }
            }

            if (cmdMatch != null) {
                msg = msg.substring(cmdMatch.length()); // Remove command name
                msg = msg.trim(); // remove leading and trailing whitespace
                Commands.get(cmdMatch).ProcessCommand(msg, event);
            }
        }
    }
}
        