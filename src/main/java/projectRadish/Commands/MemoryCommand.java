package projectRadish.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MemoryCommand extends BaseCommand
{
    private long cachedMemory = -1L;
    private String cachedMemString = "";

    @Override
    public boolean canBeUsedViaPM() { return true; }

    @Override
    public String getDescription() { return "Tells how much memory the bot is using."; }

    @Override
    protected void ExecuteCommand(String contents, MessageReceivedEvent event)
    {
        Runtime runtime = Runtime.getRuntime();
        long memory = runtime.totalMemory() - runtime.freeMemory();

        //Kimimaru: freeMemory has a precision that's different for all machines
        //If we're not allocating enough memory, freeMemory will stay the same for a while, so caching is beneficial here
        if (memory != cachedMemory)
        {
            cachedMemory = memory;
            cachedMemString = String.format("**Memory Usage:** %d KB/%d MB", cachedMemory / 1024, ((cachedMemory / 1024) / 1024));
        }

        event.getChannel().sendMessage(cachedMemString).queue();
    }
}
