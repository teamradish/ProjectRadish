package projectRadish;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class Configuration
{
    // Private Constructor to prevent instantiation
    private Configuration() {}

    private static String BotToken;
    private static Set<String> RadishAdmin  = new HashSet<>();
    private static Set<String> TPEAdmin  = new HashSet<>();
    private static String CurrentGame;
    private static String CurrentDoc;
    private static HashMap<String, String> Docs = new HashMap<>();


    public static Set<String> getRadishAdmin() {
        return RadishAdmin;
    }

    public static Set<String> getTPEAdmin() {
        return TPEAdmin;
    }

    public static String getBotToken() {
        return BotToken;
    }

    public static String getCurrentDoc() {
        return CurrentDoc;
    }

    public static void setCurrentDoc(String currentDoc) {
        Configuration.CurrentDoc = currentDoc;
    }

    public static String getCurrentGame() {
        return CurrentGame;
    }

    public static void setCurrentGame(String currentGame) {
        CurrentGame = currentGame;
    }

    public static HashMap<String, String> getDocs() {
        return Docs;
    }

    public static void loadConfiguration()
    {
        try {
            JSONObject configJSON = new JSONObject(new String(Files.readAllBytes(Paths.get("config.json"))));
            BotToken = configJSON.getString("BotToken");
            CurrentDoc = configJSON.getString("CurrentDoc");
            CurrentGame = configJSON.getString("CurrentGame");
            JSONArray radishAdminJSON = configJSON.getJSONArray("RadishAdmin");
            for (Object key : radishAdminJSON)
                RadishAdmin.add((String) key);
            JSONArray tpeAdminJSON = configJSON.getJSONArray("TPEAdmin");
            for (Object key : tpeAdminJSON)
                TPEAdmin.add((String) key);
            JSONObject docsJSON = configJSON.getJSONObject("Docs");
            Iterator<String> keysItr = docsJSON.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = docsJSON.get(key);
                Docs.put(key, (String)value);
            }
        } catch (IOException e) {
            RadishAdmin.add("<RadishAdmin #1 ID>");
            RadishAdmin.add("<RadishAdmin #2 ID>");
            TPEAdmin.add("<TPEAdmin #1 ID>");
            TPEAdmin.add("<TPEAdmin #2 ID>");
            Docs.put("<DOC NAME #1>", "<DOC URL #1>");
            Docs.put("<DOC NAME #2>", "<DOC URL #2>");
            JSONObject configJSON = new JSONObject();
            configJSON.put("BotToken","<INSERT BOT KEY HERE>");
            configJSON.put("RadishAdmin", RadishAdmin);
            configJSON.put("TPEAdmin", TPEAdmin);
            configJSON.put("CurrentGame", "<Current game name>");
            configJSON.put("CurrentDoc", "<Current document URL>");
            configJSON.put("Docs", Docs);

            try {
                Files.write(Paths.get("config.json"), configJSON.toString(4).getBytes());
                System.out.println("config.json not found. A new file was created.");
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.println("config.json not found. Error creating new file.");
            }
        }
    }

    public static void saveConfiguration()
    {
        try {
            JSONObject configJSON = new JSONObject();
            configJSON.put("BotToken", BotToken);
            configJSON.put("RadishAdmin", RadishAdmin);
            configJSON.put("TPEAdmin", TPEAdmin);
            configJSON.put("CurrentDoc", CurrentDoc);
            configJSON.put("CurrentGame", CurrentGame);
            configJSON.put("Docs", Docs);
            Files.write(Paths.get("config.json"), configJSON.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't save config.json.");
        }
    }
}
