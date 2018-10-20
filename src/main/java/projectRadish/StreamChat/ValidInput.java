package projectRadish.StreamChat;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

// Allergen Advice - Warning! Contains regexes. Lots of them. Some quite complex.

public class ValidInput {

    private static String delay = "(?:(?:kappa)|\\Q.\\E|#)"; // #, "kappa" and the period all denote a delay

    public static boolean isValidInput(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("\\s", ""); // remove spaces, since TPE ignores them anyway

        // Step 1: Expand out the repeats
        String inner_repeat = "(\\Q[\\E([^\\Q[]\\E]*)\\Q]*\\E(\\d+))";
        Matcher repeatRegex = Pattern.compile(inner_repeat).matcher(input);
        while (repeatRegex.find()) {
            String foundRepeat = repeatRegex.group(1);
            StringBuilder replacement = new StringBuilder();
            for (int i = 0; i < parseInt(repeatRegex.group(3)); i++) {
                replacement.append(repeatRegex.group(2));
            }
            input = input.replaceAll("\\Q"+foundRepeat+"\\E", replacement.toString());
            repeatRegex = Pattern.compile(inner_repeat).matcher(input);
        }

        // Step 2: Regex the rest
        String validityPattern = inputPattern();
        Matcher validityRegex = Pattern.compile(validityPattern).matcher(input);

        return validityRegex.matches();
    }

    public static String getInputPattern() { return inputPattern(); }

    private static Set<String> getButtons() {
        HashSet<String> btns = new HashSet<>();

        btns.add("a");      btns.add("b");
        btns.add("x");      btns.add("y");
        btns.add("l");      btns.add("r");
        btns.add("z");
        btns.add("start");
        btns.add("up");     btns.add("down");
        btns.add("left");   btns.add("right");
        btns.add("dup");     btns.add("ddown");
        btns.add("dleft");   btns.add("dright");
        btns.add("cup");     btns.add("cdown");
        btns.add("cleft");   btns.add("cright");

        return btns;
    }

    private static String inputPattern(){
        String molecule = "(?:(?:"+anyAtom()+")|(?:"+argMacro()+"))";
        String inputPattern = "\\A(?:\\A\\Z)|(?:"+molecule+"(?:\\Q+\\E?"+molecule+")*)\\Z";

        return inputPattern;
    }

    private static String argMacro() {
        // Cannot use "+" to combine buttons in a macro arg
        String fullMacroArg =  "(?:"+macroArgAtom()+"+)";

        String argMacro = "#[a-zA-Z_][A-Za-z0-9_]*\\Q(\\E"+fullMacroArg+"(?:,"+fullMacroArg+")*\\Q)\\E";
        return argMacro;
    }

    private static String macroArgAtom() {
        // An "atom" is an irreducible/unsimplifiable piece of input
        // For macro args, only non-durational button presses and the delay dot are allowed
        // No other macros or # delays

        String anyButton = anyButton();

        Set<String> allAtoms = new HashSet<>();

        allAtoms.add(anyButton); // Only Basic Button Press works in a macro arg
        allAtoms.add(delay); // Delays work but can't give them duration
        allAtoms.add("\\d+"); // Some macros accept number inputs

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
        String percentageNo = "(?:\\d|(?:100)|(?:[1-9]\\d))"; // Match 0-100, no preceding 0s
        String percentage = "(?:"+percentageNo+"\\Q%\\E)";
        String anyButton = anyButton();

        Set<String> allAtoms = new HashSet<>();

        allAtoms.add("[_-]?"+anyButton+percentage+"?"+duration+"?"); // Button Action eg. <_right70%2s>
        allAtoms.add(delay+duration+"?"); // Delay eg. <.> or <#> or <#300ms>
        allAtoms.add("#[a-zA-Z_][A-Za-z0-9_]*"); // No-argument macro

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
        sb.append("(?:pause))"); // also a valid button, usually an alias for "start"
        return sb.toString();
    }

    private ValidInput() {} // No instantiation please
}
