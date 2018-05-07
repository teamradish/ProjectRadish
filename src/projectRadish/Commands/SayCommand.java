package projectRadish;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public final class SayCommand extends BaseCommand
{
    public SayCommand()
    {

    }

    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        if (msgInfo.getCmdArgs().size() <= 0) return;

        //Repeat the message
        msgInfo.getChannel().sendMessage(msgInfo.getMsgContent()).queue();
    }
}
