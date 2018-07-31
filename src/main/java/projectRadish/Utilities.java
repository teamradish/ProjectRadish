package projectRadish;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import java.lang.reflect.*;
import projectRadish.Commands.BaseCommand;

/**
 * Contains various utilities.
 */
public final class Utilities
{
    //Initialize a single Random object with a time-dependent seed
    //Reuse this instance, as new instances will have the same seeds if created at approximately the same time (Ex. in a loop)
    private static Random Rand = new Random();

    /**
     * Converts a string into a LocalDateTime.
     * @param timeString A string representing a date and time.
     * @return A LocalDateTime using the information from the string. If it could not be parsed, then the current UTC time is returned.
     */
    public static LocalDateTime parseDateTimeFromString(String timeString)
    {
        try
        {
            //Parse information by removing parts of Twitch's datetime timeString
            LocalDate streamDate = LocalDate.parse(timeString.substring(0, 10));
            LocalTime streamTime = LocalTime.parse(timeString.substring(11, timeString.length() - 1));
            LocalDateTime streamDateTime = LocalDateTime.of(streamDate, streamTime);

            return streamDateTime;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        //If we encountered an error, simply return now in UTC time
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    /**
     * Formats a time value in milliseconds into a time format ("HH:mm:ss").
     * @param time The time value, in milliseconds.
     * @return A String containing the formatted time.
     */
    public static String getTimeStringFromMs(final long time)
    {
        //Format to hours, minutes, and seconds
        Instant instant = Instant.ofEpochMilli(time);
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return formatter.format(zdt);
    }

    /**
     * Tries to instantiate a command from a string.
     * @param commandName The case-sensitive class name of the BaseCommand to instantiate.
     * @return An instance of the BaseCommand specified by commandName. null if the class couldn't be found or commandName was invalid.
     */
    public static BaseCommand createCommandFromString(String commandName)
    {
        //Get the fully qualified name of the command
        String fullCmdName = Constants.CommandPkgName + commandName;

        try
        {
            Class cmdClass = Class.forName(fullCmdName);

            BaseCommand newCmd = (BaseCommand)cmdClass.newInstance();
            return newCmd;
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            //System.out.println("Could not create instance of " + commandName);
        }

        return null;
    }

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
