package projectRadish.StreamChat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Controllers {
    private static Map<String, Set<String>> controllers = initControllers();


    public static Set<String> getController(String controller) {
        // Return the controller requested - or, if it's not present, an empty set.
        return controllers.getOrDefault(controller.toLowerCase(), new HashSet<>());
    }

    public static Set<String> getControllerNames() {
        return controllers.keySet();
    }

    private static Map<String,Set<String>> initControllers() {
        Map<String,Set<String>> controllers = new HashMap<>();

        // Make sure keys are all lowercase.
        controllers.put("wiimote", wiimote());
        controllers.put("wiimote_and_nunchuk", wiimoteAndNunchuk());
        controllers.put("gamecube", gamecubeController());
        controllers.put("n64", n64Controller());
        controllers.put("snes", snesController());

        controllers.put("gba", gba());

        controllers.put("none", new HashSet<>());

        return controllers;
    }

    public static Set<String> wiimoteAndNunchuk() {
        HashSet<String> btns = new HashSet<>();

        // Remote
        btns.add("a");      btns.add("b");
        btns.add("1");      btns.add("2");
        btns.add("plus");   btns.add("minus");
        btns.add("home");
        btns.add("dup");    btns.add("ddown");
        btns.add("dleft");  btns.add("dright");

        // Nunchuk
        btns.add("up");     btns.add("down");
        btns.add("left");   btns.add("right");
        btns.add("z");      btns.add("c");

        // Don't forget Motion Controls!
        btns.add("shake");
        btns.add("point");
        btns.add("pup");    btns.add("pdown");
        btns.add("pleft");  btns.add("pright");

        return btns;
    }

    public static Set<String> wiimote() {
        HashSet<String> btns = new HashSet<>();
        btns.add("a");      btns.add("b");
        btns.add("1");      btns.add("2");
        btns.add("plus");   btns.add("minus");
        btns.add("home");
        btns.add("up");     btns.add("down");
        btns.add("left");   btns.add("right");

        // Don't forget Motion Controls!
        btns.add("shake");
        btns.add("point");
        btns.add("pup");    btns.add("pdown");
        btns.add("pleft");  btns.add("pright");
        return btns;
    }

    public static Set<String> gamecubeController() {
        HashSet<String> btns = new HashSet<>();
        btns.add("a");      btns.add("b");
        btns.add("x");      btns.add("y");
        btns.add("l");      btns.add("r");
        btns.add("z");
        btns.add("start");
        btns.add("up");     btns.add("down");
        btns.add("left");   btns.add("right");
        btns.add("dup");    btns.add("ddown");
        btns.add("dleft");  btns.add("dright");
        btns.add("cup");    btns.add("cdown");
        btns.add("cleft");  btns.add("cright");
        return btns;
    }

    public static Set<String> n64Controller() {
        HashSet<String> btns = new HashSet<>();
        btns.add("a");      btns.add("b");
        btns.add("l");      btns.add("r");
        btns.add("z");
        btns.add("start");
        btns.add("up");     btns.add("down");
        btns.add("left");   btns.add("right");
        btns.add("dup");    btns.add("ddown");
        btns.add("dleft");  btns.add("dright");
        btns.add("cup");    btns.add("cdown");
        btns.add("cleft");  btns.add("cright");
        return btns;
    }

    public static Set<String> snesController() {
        HashSet<String> btns = new HashSet<>();
        btns.add("a");      btns.add("b");
        btns.add("x");      btns.add("y");
        btns.add("l");      btns.add("r");
        btns.add("start");  btns.add("select");
        btns.add("up");     btns.add("down");
        btns.add("left");   btns.add("right");
        return btns;
    }

    public static Set<String> gba() {
        HashSet<String> btns = new HashSet<>();
        btns.add("a");      btns.add("b");
        btns.add("l");      btns.add("r");
        btns.add("start");  btns.add("select");
        btns.add("up");     btns.add("down");
        btns.add("left");   btns.add("right");
        return btns;
    }

    private Controllers(){} // No Instantiation
}
