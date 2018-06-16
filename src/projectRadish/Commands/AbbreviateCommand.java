package projectRadish.Commands;

import projectRadish.DidYouMean;
import projectRadish.MessageInfoWrapper;

public final class AbbreviateCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        msgInfo.getChannel().sendMessage(DidYouMean.abbreviate(msgInfo.getMsgContent())).queue();
    }
}
