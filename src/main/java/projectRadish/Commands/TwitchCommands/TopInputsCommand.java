package projectRadish.Commands.TwitchCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Commands.BaseCommand;
import projectRadish.Configuration;

import java.util.*;

public final class TopInputsCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Displays the Input Count leaderboard.";
    }

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        int boardSize; // Number of positions to show on the leaderboard
        try {
            boardSize = Integer.parseInt(content);
            if (boardSize > 10) { boardSize = 5; }
        } catch (Exception e){
            boardSize = 5;
        }

        Map<String, Long> inputCounts = Configuration.getInputCounts();
        if (boardSize > inputCounts.size()) { boardSize = inputCounts.size(); }

        // Sort into descending order by Inputs
        List<Map.Entry<String, Long>> entries = new ArrayList<>(inputCounts.entrySet());
        Collections.sort(entries, (c1, c2) -> c2.getValue().compareTo(c1.getValue()));

        // Quick first pass over leaders to get longest name length
        int longestNameLen = 0;
        for (int i = 0; i < boardSize; i++) {
            int namelen = entries.get(i).getKey().length();
            if (namelen > longestNameLen) {
                longestNameLen = namelen;
            }
        }

        // Construct Leaderboard
        StringBuilder reply = new StringBuilder();
        reply.append("Inputs Leaderboard:```\n");

        for (int i = 0; i < boardSize; i++) {
            Map.Entry<String, Long> entry = entries.get(i);
            String name = entry.getKey();
            Long inputs = entry.getValue();

            String score;
            if (inputs == 1) { score = "1 input"; }
            else { score = String.format("%d inputs", inputs); }

            reply.append(String.format("#%d | %-"+(longestNameLen+3)+"s | %s\n", i+1, name, score));
        }
        reply.append("```");
        event.getChannel().sendMessage(reply.toString()).queue();
    }
}
