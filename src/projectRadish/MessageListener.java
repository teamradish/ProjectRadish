package projectRadish;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Random;

public class MessageListener extends ListenerAdapter {
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

        //Remember, in all of these .equals checks it is actually comparing
        // message.getContentDisplay().equals, which is comparing a string to a string.
        // If you did message.equals() it will fail because you would be comparing a Message to a String!
        if (msg.startsWith("!say ")) {
            String reply = msg.replaceFirst("!say ", "");
            channel.sendMessage(reply).queue();
        }
        else if (msg.startsWith("!abbreviate ")) {
            String reply = msg.replaceFirst("!abbreviate ", "");
            channel.sendMessage(DidYouMean.abbreviate(reply)).queue();
        }
        else if (msg.equals("!doc")) {
            String game = Constants.getCurrentDoc()[0];
            String link = Constants.getCurrentDoc()[1];

            MessageEmbed e = formatOutput("Current Game Document:", game, link);
            channel.sendMessage(e).queue();
        }

        else if (msg.startsWith("!doc ")) {
            String input = msg.replaceFirst("!doc ", "");

            String game = findGame(input);

            boolean isGuess = false;
            if (game == null) {    // if no match found
                game = DidYouMean.getBest(input);
                isGuess = true;
            }

            String link = Constants.getDocs().get(game);
            String header;
            if (isGuess) { header = "Best Guess for "+input+":"; } else { header = null; }

            MessageEmbed e = formatOutput(header, game, link);
            channel.sendMessage(e).queue();
        }

        else if (msg.startsWith("!guess ")) {
            String input = msg.replaceFirst("!guess ", "");
            input = input.toLowerCase();

            String game = findGame(input);

            String prefix = "Matched: ";
            if (game == null) {    // still no match found
                game = DidYouMean.getBest(input);
                prefix = "Best guess: ";
            }
            String abbr = DidYouMean.abbreviate(game);
            String reply = prefix + String.format("%s [%s]", game, abbr);
            channel.sendMessage(reply).queue();
        }

        else if (msg.startsWith("!owo ")) {
            String reply = msg.replaceFirst("!owo ", "");
            reply = reply.replaceAll("l", "w");
            reply = reply.replaceAll("r", "w");
            reply = reply.replaceAll(" :\\)", " OwO");
            reply = reply.replaceAll(" :'\\(", " QnQ");
            reply = reply.replaceAll(" ;\\)", " ;3");
            reply = reply.replaceAll(" -_-", " UwU");
            reply = reply.replaceAll(" \\^_\\^", " ^w^");
            reply = reply.replaceAll(" >:\\)", " >:3");
            channel.sendMessage(reply).queue();
        }

        else if (msg.equals("!roll"))
        {
            Random rand = new Random();
            int roll = rand.nextInt(6) + 1; //This results in 1 - 6 (instead of 0 - 5)
            channel.sendMessage("Your roll: " + roll).queue();
        }
        else if (msg.equals("!alldocs"))
        {
            channel.sendMessage("http://twitchplays.wikia.com/wiki/Game_Documents_(Mobile)").queue();
        }

        else if (msg.startsWith("!status ")) {
            if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                String status = msg.replaceFirst("!status ", "").toLowerCase();
                try {
                    event.getJDA().getPresence().setStatus(OnlineStatus.fromKey(status));
                } catch (IllegalArgumentException e) {
                    channel.sendMessage("Invalid Status. Must be Online, Idle, DND, Invisible, or Offline.").queue();
                }
            } else {
                channel.sendMessage("You're not allowed to use this command").queue();
            }
        }

        else if (msg.startsWith("!game ")) {
        	if (Constants.getRadishAdmin().contains(event.getAuthor().getId())) {
        	    String game = msg.replaceFirst("!game ", "");
        	    if (!game.toLowerCase().equals("none")) {
                    event.getJDA().getPresence().setGame(Game.of(GameType.DEFAULT, game));
                } else {
                    event.getJDA().getPresence().setGame(null);
                }
        	} else {
                channel.sendMessage("You're not allowed to use this command").queue();
            }
        }
        
        
        else if (msg.startsWith("!streaming ")) {
        	
        	
        	if ((Constants.getTPEAdmin().contains(event.getAuthor().getId())) || Constants.getRadishAdmin().contains(event.getAuthor().getId())) {
        		String game = msg.replaceFirst("!streaming ", "");
        		if (!game.toLowerCase().equals("none")) {
        			event.getJDA().getPresence().setGame(Game.of(GameType.STREAMING, game, "https://twitch.tv/twitchplays_everything"));
        			
        		} else {
        			event.getJDA().getPresence().setGame(null);
        		}
        	}
        	else {
        		channel.sendMessage("You're not allowed to use this command").queue();
        	}
        }
        else if (msg.startsWith("!listening to ")) {
        	if ((Constants.getRadishAdmin().contains(event.getAuthor().getId()))) {
        		String game = msg.replaceFirst("!listening to ", "");
        		if (!game.toLowerCase().equals("none")) {
        			event.getJDA().getPresence().setGame(Game.of(GameType.LISTENING, game));
        		}
        		else {
        			event.getJDA().getPresence().setGame(null);
        		}
        	}
        }
        else if (msg.startsWith("!watching ")) {
        	if ((Constants.getRadishAdmin().contains(event.getAuthor().getId()))) {
        		String game = msg.replaceFirst("!watching ", "");
        		if (!game.toLowerCase().equals("none")) {
        			event.getJDA().getPresence().setGame(Game.of(GameType.WATCHING, game));
        		}
        		else {
        			event.getJDA().getPresence().setGame(null);
        		}
        	}
        }
    }

    /**
     * Function to create the reply's message embed, from !doc command results
     * This function handles Guessed replies.
     *
     * @param header Any text to display in the header (null if none);
     * @param game Game title (eg. "Super Mario Sunshine")
     * @param link Link to the game's document (eg. "https://docs.google.com/document/d/...")
     * @return The response, formatted as an Embed, to be sent back to the channel.
     */
    private static MessageEmbed formatOutput(String header, String game, String link){
        String abbr = DidYouMean.abbreviate(game);
        String thumbnailPrefix = "https://drive.google.com/thumbnail?authuser=0&sz=w320&id=";
        String docId = link.replaceFirst("https://docs.google.com/document/d/", ""); // Remove prefix
        docId = docId.substring(0, docId.indexOf('/')); // Remove suffix (/view, /edit, etc.)

        if (header != null) {   // if we actually have a header
            if (header.length() > 100) { header = header.substring(0, 99) + "..."; } // limit it to 100 chars
        }

        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("%s [%s] Doc", game, abbr), link);
        if (header != null) { eb.setAuthor(header, null); }
        eb.setThumbnail(thumbnailPrefix+docId);
        eb.setDescription("The TPE community document for\n"+game+"!");
        eb.setColor(6570404);
        return eb.build();
    }

    /**
     * Takes an input string and compares it against all known docs' game names and abbreviations
     *
     * @param input The string we're attempting to find a matching game for.
     * @return If found, the Key for that game in the Constants.doc map. null if not found.
     */
    private static String findGame(String input){
        input = input.toLowerCase();
        String game = null;
        for (String doc : Constants.getDocs().keySet()) { // If full name matches
            if (input.equals(doc.toLowerCase())) {
                game = doc;
            }
        }

        if (game == null) { // if no luck with full names
            for (String doc : Constants.getDocs().keySet()) { // check if any abbreviations match
                String abbr = DidYouMean.abbreviate(doc).toLowerCase();
                if (input.equals(abbr)) {
                    game = doc;
                }
            }
        }
        return game; // null if no match was found
    }
}
        