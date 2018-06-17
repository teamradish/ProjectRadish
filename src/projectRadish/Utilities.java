package projectRadish;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.HashMap;
import java.util.Random;

/**
 * Contains various utilities.
 */
public final class Utilities
{
    //Initialize a single Random object with a time-dependent seed
    //Reuse this instance, as new instances will have the same seeds if created at approximately the same time (Ex. in a loop)
    private static Random Rand = new Random();

    /**
     * Gets a random integer in a range.
     * @param min The minimum value in the range (inclusive).
     * @param max The maximum value in the range (inclusive).
     * @return A random number in the range of min and max.
     */
    public static int randRange(int min, int max)
    {
        int realMin = min;
        int realMax = max;

        //Swap if min > max
        if (min > max)
        {
            realMax = min;
            realMin = max;
        }

        //Test for the exception thrown by nextInt
        try
        {
            return Rand.nextInt((realMax - realMin) + 1) + realMin;
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());

            //Return 0 if we encounter an exception
            return 0;
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
    public static MessageEmbed formatOutput(String header, String game, String link){
        String abbr = DidYouMean.abbreviate(game);
        String thumbnailPrefix = "https://drive.google.com/thumbnail?authuser=0&sz=w320&id=";
        String docId = link.replaceFirst("https://docs.google.com/document/d/", ""); // Remove prefix
        docId = docId.substring(0, docId.indexOf('/')); // Remove suffix (/view, /edit, etc.)

        if (header != null) {   // if we actually have a header
            if (header.length() > 100) { header = header.substring(0, 96) + "..."; } // limit it to 100 chars
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
    public static String findGame(String input){
        input = input.toLowerCase();
        String game = null;
        for (String doc : Configuration.getDocs().keySet()) { // If full name matches
            if (input.equals(doc.toLowerCase())) {
                game = doc;
            }
        }

        if (game == null) { // if no luck with full names
            for (String doc : Configuration.getDocs().keySet()) { // check if any abbreviations match
                String abbr = DidYouMean.abbreviate(doc).toLowerCase();
                if (input.equals(abbr)) {
                    game = doc;
                }
            }
        }
        return game; // null if no match was found
    }

    public static HashMap<String, String> recycleBin = new HashMap<>();

    private Utilities() {}
}
