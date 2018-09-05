package projectRadish.Commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class SuggestionsCommand extends BaseCommand
{
    private MessageEmbed EmbeddedMsg = null;

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
    public void Initialize()
    {
        super.Initialize();

        //Kimimaru: This message is always the same, so create it once here
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.clear();
        embedBuilder.setTitle("TPE Game Suggestions", "https://docs.google.com/document/d/1Em-Lq4BKyvBICX1RF-4Ndt-P2mZeY4x9VZ1k63miMb8/edit?usp=sharing");
        embedBuilder.setDescription("A document to suggest games that may be played in the future on TPE!");
        embedBuilder.setColor(new Color(101, 112, 64));

        EmbeddedMsg = embedBuilder.build();
    }

    @Override
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        event.getChannel().sendMessage(EmbeddedMsg).queue();
    }
}
