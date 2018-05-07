package projectRadish;

import net.dv8tion.jda.core.entities.*;

import java.util.List;

public final class EmoteHellCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        int x = 10;
        int y = 6;
        String reply = "";

        try
        {
            List<Emote> emotes = msgInfo.getMsgEvent().getGuild().getEmotes();
            System.out.println(emotes.size());

            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    int emoteNum = Utilities.randRange(0, emotes.size() - 1);
                    reply += emotes.get(emoteNum).getAsMention() + ' ';
                }
                reply += '\n';
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());

            reply = "Sorry, I cannot do that for you now.";
        }

        msgInfo.getChannel().sendMessage(reply).queue();
    }
}
