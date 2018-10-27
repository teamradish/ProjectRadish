package projectRadish;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Config class for easily converting to and from JSON.
 */
public final class Config
{
    public String BotToken;
    public Set<String> RadishAdmin  = new HashSet<>();
    public String CurrentGame;
    public String databaseIP;
    public String databaseUsername;
    public String databasePassword;
    public String databaseName;
    public HashMap<String, String> Docs = new HashMap<>();
    public HashMap<String, String> Commands = new HashMap<>();
    public String CommandPrefix = "!";
    public Vector<String> SilencedUsers = new Vector<>();
    public HashMap<String, Long> InputCounts = new HashMap<>();

    public Config()
    {

    }
}
