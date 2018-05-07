package projectRadish;

import projectRadish.BaseCommand;
import projectRadish.MessageInfoWrapper;

public final class OwoCommand extends BaseCommand
{
    @Override
    public void ExecuteCommand(MessageInfoWrapper msgInfo)
    {
        String reply = msgInfo.getMsgContent();

        reply = reply.replaceAll("l", "w");
        reply = reply.replaceAll("r", "w");
        reply = reply.replaceAll(" :\\)", " OwO");
        reply = reply.replaceAll(" :'\\(", " QnQ");
        reply = reply.replaceAll(" ;\\)", " ;3");
        reply = reply.replaceAll(" -_-", " UwU");
        reply = reply.replaceAll(" \\^_\\^", " ^w^");
        reply = reply.replaceAll(" >:\\)", " >:3");

        msgInfo.getChannel().sendMessage(reply).queue();
    }
}
