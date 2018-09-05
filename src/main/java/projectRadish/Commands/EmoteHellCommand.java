package projectRadish.Commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Utilities;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public final class EmoteHellCommand extends BaseCommand
{
    @Override
    public String getDescription() {
        return "Creates a big (but responsibly big) wall of random emotes. Has a per-user cooldown system.";
    }

    @Override
    public boolean canBeUsedViaPM() { return false; }

    private static DecimalFormat dp1 = new DecimalFormat(".#");
    static private final long cooldown = 1*60*60*1000; // 1 hour, in milliseconds
    private HashMap<String, Long> lastTimes = new HashMap<>();
    private StringBuilder replyBuilder = new StringBuilder(2000);

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        long now = System.currentTimeMillis();
        String user = event.getAuthor().getId();

        if (!lastTimes.containsKey(user)){ // user hasn't used it since last restart
            lastTimes.put(user, 0L);
        }

        long lastTime = lastTimes.get(user);

        long timeLeft = lastTime + cooldown - now;

        if (timeLeft > 0) {
            String timeLeftString;

            if (timeLeft > 60*60*1000) {
                timeLeftString = dp1.format(timeLeft / (60*60*1000.0f)) + " hours";
            } else {
                timeLeftString = dp1.format(timeLeft / (60*1000.0f)) + " minutes";
            }

            event.getChannel().sendMessage(String.format("%s's emotehell is on cooldown for another %s.",
                    event.getAuthor().getAsMention(),
                    timeLeftString
            )).queue();
            return;
        }

        int x = 10;
        int y = 6;
        String reply;

        lastTimes.replace(user, now);

        try
        {
            List<Emote> emotes = event.getGuild().getEmotes();
            if (emotes.size() == 0) {
                event.getChannel().sendMessage(
                        String.format("Server \"%s\" has no emotes :(", event.getGuild().getName())
                ).queue();
                return;
            }

            replyBuilder.setLength(0);
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    int emoteNum = Utilities.randRange(0, emotes.size() - 1);
                    replyBuilder.append(emotes.get(emoteNum).getAsMention());
                    replyBuilder.append(' ');
                }
                replyBuilder.append('\n');
            }
            reply = replyBuilder.toString();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());

            reply = "Sorry, something went wrong.";
        }

        event.getChannel().sendMessage(reply).queue();
    }
}
