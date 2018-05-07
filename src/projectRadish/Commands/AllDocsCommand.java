package projectRadish;

import projectRadish.BaseCommand;
import projectRadish.MessageInfoWrapper;

public class AllDocsCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        msgInfo.getChannel().sendMessage("http://twitchplays.wikia.com/wiki/Game_Documents_(Mobile)").queue();
    }
}
