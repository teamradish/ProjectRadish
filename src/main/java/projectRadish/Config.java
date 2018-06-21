package projectRadish;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Config class for easily converting to and from JSON.
 */
public final class Config
{
    public String BotToken;
    public Set<String> RadishAdmin  = new HashSet<>();
    public Set<String> TPEAdmin  = new HashSet<>();
    public String CurrentGame;
    public HashMap<String, String> Docs = new HashMap<>();

    public Config()
    {

    }
}
