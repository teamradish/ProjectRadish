package projectRadish.StreamChat;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Allergen Advice - Warning! Contains regexes. Lots of them. Some quite complex.

public class ValidInput {

    public static boolean isValidInput(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("\\s", ""); // remove spaces, since TPE ignores them anyway

        // Step 1: Find all the argument-taking macros
        String argMacro = "#[A-Za-z0-9]+\\Q(\\E[^)]+\\Q)\\E";

        // Step 2: Validate their arguments (being mindful of nested repeats)

        // Step 3: If invalid, the whole thing's invalid, else remove them from the equation somehow

        // Step 4: Find, validate, and eliminate all repeats from the string

        // Step 5: The following regex can now validate the rest
        String validityPattern = anyAtom()+"(?:\\Q+\\E?"+anyAtom()+")*";
        Matcher validityRegex = Pattern.compile(validityPattern).matcher(input);

        return validityRegex.matches();
    }

    private static Set<String> getButtons() {
        HashSet<String> btns = new HashSet<>();

        // Currently SNES so no axis testing unfortunately
        btns.add("a");      btns.add("b");
        btns.add("x");      btns.add("y");
        btns.add("l");      btns.add("r");
        btns.add("start");  btns.add("select");
        btns.add("up");     btns.add("down");
        btns.add("left");   btns.add("right");

        return btns;
    }

    private static String molecule(){ return ""; }

    private static String fullMacroArg() {
        // ArgMolecules and argMolecules in Repeats
        // Note that you cannot deal with nested repeats with regexes alone
        // Best plan is probably regex to find an inner repeat, replace it with something valid like "a"
        //      or some kind of special char, then repeat until no more inner repeats are found

        String inner_repeat = "["+macroArgMolecule()+"]\\Q*\\E\\d+";
        return "";
    }

    private static String macroArgMolecule() {
        // Cannot use "+" to combine buttons in a macro arg
        return "(?:"+macroArgAtom()+"+)";
    }

    private static String macroArgAtom() {
        // An "atom" is an irreducible/unsimplifiable piece of input
        // For macro args, only non-durational button presses and the delay dot are allowed
        // No other macros or # delays

        String anyButton = anyButton();

        Set<String> allAtoms = new HashSet<>();

        allAtoms.add(anyButton); // Only Basic Button Press works in a macro arg
        allAtoms.add("\\Q.\\E"); // Our boi delay dot works, but #200ms does not. Can't give it duration here either

        StringBuilder sb = new StringBuilder("(?:");
        for (String atom: allAtoms) {
            sb.append("(?:"+atom+")|");
        }
        sb.deleteCharAt(sb.length()-1); // remove final "|"
        sb.append(")");

        return sb.toString();
    }

    private static String anyAtom() {
        // An "atom" is an irreducible/unsimplifiable piece of input
        // Eg. a delay <#300ms>, a button action <_right2s>, a no-argument macro call <#jump>

        String duration = "(?:\\d+m?s)";
        String anyButton = anyButton();

        Set<String> allAtoms = new HashSet<>();

        allAtoms.add("[_-]?"+anyButton+duration+"?"); // Button Action eg. <_right2s>
        allAtoms.add("#"+duration); // Delay eg. <#300ms>
        allAtoms.add("#[A-Za-z0-9]+"); // No-argument macro
        allAtoms.add("\\Q.\\E"+duration+"?"); // Our boi, delay dot (can have duration for some reason)

        StringBuilder sb = new StringBuilder("(?:");
        for (String atom: allAtoms) {
            sb.append("(?:"+atom+")|");
        }
        sb.deleteCharAt(sb.length()-1); // remove final "|"
        sb.append(")");

        return sb.toString();
    }

    private static String anyButton() {
        Set<String> buttons = getButtons();
        StringBuilder sb = new StringBuilder("(?:");
        for (String button: buttons) {
            sb.append("(?:"+button+")|");
        }
        sb.deleteCharAt(sb.length()-1); // remove final "|"
        sb.append(")");
        return sb.toString();
    }

    private ValidInput() {} // No instantiation please
}
