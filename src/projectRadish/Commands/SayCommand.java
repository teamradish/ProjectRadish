package projectRadish.Commands;


import projectRadish.MessageInfoWrapper;

public final class SayCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        if (msgInfo.getCmdArgs().size() <= 0) return;

        //Repeat the message
        msgInfo.getChannel().sendMessage(msgInfo.getMsgContent()).queue();
    }
}
