package projectRadish.Commands;

import projectRadish.MessageInfoWrapper;

public final class AllDocsCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        msgInfo.getChannel().sendMessage("http://twitchplays.wikia.com/wiki/Game_Documents_(Mobile)").queue();
    }
}
