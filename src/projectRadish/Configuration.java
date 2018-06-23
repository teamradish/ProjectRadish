package projectRadish;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public final class Configuration
{
    // Private Constructor to prevent instantiation
    private Configuration() {}

    /**
     * The reference to the config object used.
     */
    private static Config config = null;

    /**
     * The objects listening for when the config is loaded.
     */
    private static Vector<ConfigListener> configListeners = new Vector<>();

    private static ObjectMapper objMapper = new ObjectMapper();

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

    public static void addConfigListener(ConfigListener configListener)
    {
        configListeners.add(configListener);
    }

    public static void removeConfigListener(ConfigListener configListener)
    {
        configListeners.remove(configListener);
    }

    public static void loadConfiguration()
    {
        //Don't fail on unknown properties
        objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try
        {
            //Convert from JSON to our object
            config = objMapper.readValue(new String(Files.readAllBytes(Paths.get("config.json"))), Config.class);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to load [config.json]. The file may be missing or corrupted.");
            System.exit(2);
        }

        //Notify all listeners that the config was loaded
        for (int i = 0; i < configListeners.size(); i++)
        {
            configListeners.elementAt(i).configLoaded();
        }
    }

    public static void saveConfiguration()
    {
        try
        {
            //Write JSON as pretty so we can read it
            String data = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
            Files.write(Paths.get("config.json"), data.getBytes());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Couldn't save [config.json]");
            return;
        }

        //Notify all listeners that the config was saved
        for (int i = 0; i < configListeners.size(); i++)
        {
            configListeners.elementAt(i).configSaved();
        }
    }
}
