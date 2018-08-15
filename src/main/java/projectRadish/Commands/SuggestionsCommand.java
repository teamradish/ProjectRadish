package projectRadish.Commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class SuggestionsCommand extends BaseCommand
{
    @Override
    public boolean canBeUsedViaPM()
    {
        return true;
    }

    @Override
    public String getDescription()
    {
        return "Links to the TPE game suggestions document.";
    }

    @Override
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("TPE Game Suggestions", "https://docs.google.com/document/d/1Em-Lq4BKyvBICX1RF-4Ndt-P2mZeY4x9VZ1k63miMb8/edit?usp=sharing");
        eb.setDescription("A document to suggest games that may be played in the future on TPE!");
        eb.setColor(new Color(101, 112, 64));
        MessageEmbed embed = eb.build();

        event.getChannel().sendMessage(embed).queue();
    }
}
