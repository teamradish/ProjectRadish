package projectRadish;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import projectRadish.Commands.BaseCommand;

import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class Configuration
{
    // Private Constructor to prevent instantiation
    private Configuration() {}

    /**
     * The reference to the config object used.
     */
    private static Config config = null;

    public static Set<String> getRadishAdmin() {
        return config.RadishAdmin;
    }

    public static Set<String> getTPEAdmin() {
        return config.TPEAdmin;
    }

    public static String getBotToken() {
        return config.BotToken;
    }

    public static String getCurrentGame() {
        return config.CurrentGame;
    }

    public static void setCurrentGame(String currentGame) {
        config.CurrentGame = currentGame;
    }

    public static HashMap<String, String> getDocs() {
        return config.Docs;
    }

    public static HashMap<String, String> getCommands() { return config.Commands; }

    public static void setDocs(HashMap<String,String> docs) { config.Docs = docs; }

    public static void loadConfiguration()
    {
        ObjectMapper mapper = new ObjectMapper();

        //Don't fail on unknown properties
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try
        {
            //Convert from JSON to our object
            config = mapper.readValue(new String(Files.readAllBytes(Paths.get("config.json"))), Config.class);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to load [config.json]. The file may be missing or corrupted.");
            System.exit(2);
        }
    }

    public static void saveConfiguration()
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();

            //Write JSON as pretty so we can read it
            String data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
            Files.write(Paths.get("config.json"), data.getBytes());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Couldn't save [config.json]");
        }
    }
}
