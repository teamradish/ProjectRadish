package projectRadish;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import projectRadish.Commands.*;

import java.util.HashMap;
import java.util.Map;

public class MessageListener extends ListenerAdapter {

    //Hardcoded for now, but we might want to load them in from config somewhere later
    private HashMap<String, BaseCommand> Commands = new HashMap<String, BaseCommand>();

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
        Commands.put("game", new GameCommand());
        Commands.put("streaming", new StreamingCommand());
        Commands.put("listen", new ListenCommand());
        Commands.put("watch", new WatchingCommand());
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

        // Message was sent by a bot
        if (author.isBot()) { ignore = true; }

        return ignore;
    }

    private void handleMessage(MessageReceivedEvent event) {

        //Event specific information
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
        //  This could be a TextChannel, PrivateChannel, or Group!

        String msg = message.getContentDisplay();              //This returns a human readable version of the Message. Similar to
        // what you would see in the client.

        if (event.isFromType(ChannelType.TEXT))         //If this message was sent to a Guild TextChannel
        {
            //Because we now know that this message was sent in a Guild, we can do guild specific things
            // Note, if you don't check the ChannelType before using these methods, they might return null due
            // the message possibly not being from a Guild!

            Guild guild = event.getGuild();             //The Guild that this message was sent in. (note, in the API, Guilds are Servers)
            TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
            Member member = event.getMember();          //This Member that sent the message. Contains Guild specific information about the User!

            String name;
            if (message.isWebhookMessage())
            {
                name = author.getName();                //If this is a Webhook message, then there is no Member associated
            }                                           // with the User, thus we default to the author for name.
            else
            {
                name = member.getEffectiveName();       //This will either use the Member's nickname if they have one,
            }                                           // otherwise it will default to their username. (User#getName())

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        }
        else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
        {
            //The message was sent in a PrivateChannel.
            //In this example we don't directly use the privateChannel, however, be sure, there are uses for it!

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }
        else if (event.isFromType(ChannelType.GROUP))   //If this message was sent to a Group. This is CLIENT only!
        {
            //The message was sent in a Group. It should be noted that Groups are CLIENT only.
            Group group = event.getGroup();
            String groupName = group.getName() != null ? group.getName() : "";  //A group name can be null due to it being unnamed.

            System.out.printf("[GRP: %s]<%s>: %s\n", groupName, author.getName(), msg);
        }

        //Find and execute the command for message
        carryOutCommandForMsg(event, msg);
    }

    private void carryOutCommandForMsg(MessageReceivedEvent event, String msg)
    {
        //If the message starts with the command character, look for a command to perform
        if (msg.startsWith(Constants.COMMAND_PREFIX))
        {
            //Find the command - get the lowercase version so the command is not case-sensitive
            String[] msgArgs = msg.toLowerCase().split(" ");
            if (msgArgs.length > 0)
            {
                //Take the first one split
                String command = msgArgs[0];

                //If the length of the command found is greater than 1 (meaning it's more than just the command character),
                //look for it and execute the command associated
                if (command.length() > 1)
                {
                    command = command.substring(1);

                    //Check for the command in our hash map and execute it if so
                    //getOrDefault saves a hash lookup
                    BaseCommand curCommand = Commands.getOrDefault(command, null);

                    if (curCommand != null)
                    {
                        curCommand.ExecuteCommand(new MessageInfoWrapper(event));
                    }
                }
            }
        }
    }
}
        