package projectRadish.StreamChat;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

// Allergen Advice - Warning! Contains regexes. Lots of them. Some quite complex.

public class InputValidator {

    private Set<String> buttons;

    public InputValidator() {
        // Gamecube is a decent default, since it has a lot of buttons in common with other systems
        buttons = Controllers.gamecubeController();
    }

    public Set<String> getButtons() {
        return new HashSet<>(this.buttons);
    }

    public void setButtons(Set<String> btns) {
        this.buttons = btns;
    }

    public String getInputPattern() { return inputPattern(); }

    public boolean isValidInput(String input) {
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

    // ==================================================================
    //  Here Endeth the functions you need to care about.
    //
    //  Everything below this point is just private helper functions for
    //      creating the Validity Regex
    // ==================================================================

    private static final String delay = "(?:(?:kappa)|\\Q.\\E|#)"; // #, "kappa" and the period all denote a delay

    private String inputPattern(){
        String molecule = "(?:(?:"+anyAtom()+")|(?:"+argMacro()+"))";
        String inputPattern = "\\A(?:\\A\\Z)|(?:"+molecule+"(?:\\Q+\\E?"+molecule+")*)\\Z";

        return inputPattern;
    }

    private String argMacro() {
        // Cannot use "+" to combine buttons in a macro arg
        String fullMacroArg =  "(?:"+macroArgAtom()+"+)";

        String argMacro = "#[a-zA-Z_][A-Za-z0-9_]*\\Q(\\E"+fullMacroArg+"(?:,"+fullMacroArg+")*\\Q)\\E";
        return argMacro;
    }

    private String macroArgAtom() {
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

    private String anyAtom() {
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

    private String anyButton() {
        Set<String> buttons = getButtons();
        StringBuilder sb = new StringBuilder("(?:");
        for (String button: buttons) {
            sb.append("(?:"+button+")|");
        }
        sb.append("(?:pause))"); // also a valid button, usually an alias for "start"
        return sb.toString();
    }
}
