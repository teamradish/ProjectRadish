package projectRadish.Commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import projectRadish.Utilities;

import java.util.List;

public final class EmoteHellCommand extends BaseCommand
{
    private int lastInvocation = 0;

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        int x = 10;
        int y = 6;
        String reply;

        try
        {
            List<Emote> emotes = event.getGuild().getEmotes();
            System.out.println(emotes.size());

            StringBuilder replyBuilder = new StringBuilder(1);
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

            reply = "Sorry, I cannot do that for you now.";
        }

        event.getChannel().sendMessage(reply).queue();
    }
}
