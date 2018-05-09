package projectRadish;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Context;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public static String getCurrentDoc() {
        return config.CurrentDoc;
    }

    public static void setCurrentDoc(String currentDoc) {
        config.CurrentDoc = currentDoc;
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

            //Handle not being able to read or parse JSON
            config = new Config();
            config.RadishAdmin.add("<RadishAdmin #1 ID>");
            config.RadishAdmin.add("<RadishAdmin #2 ID>");
            config.TPEAdmin.add("<TPEAdmin #1 ID>");
            config.TPEAdmin.add("<TPEAdmin #2 ID>");
            config.Docs.put("<DOC NAME #1>", "<DOC URL #1>");
            config.Docs.put("<DOC NAME #2>", "<DOC URL #2>");

            //If it doesn't exist, create and save the new file
            //Kimimaru: Do we really want to do this? It might be a problem if it couldn't
            //read the JSON for some reason but the file still has all the info
            saveConfiguration();
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
            System.out.println("Couldn't save config.json.");
        }
    }
}
