import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

public class Example {

    if (event.isFromType(ChannelType.TEXT))         //If this message was sent to a Guild TextChannel
    {
        //Because we now know that this message was sent in a Guild, we can do guild specific things
        // Note, if you don't check the ChannelType before using these methods, they might return null due
        // the message possibly not being from a Guild!

        Guild guild = event.getGuild();             //The Guild that this message was sent in. (note, in the API, Guilds are Servers)
        TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
        Member member = event.getMember();          //This Member that sent the message. Contains Guild specific information about the User!

        String name;
        if (message.isWebhookMessage())
        {
            name = author.getName();                //If this is a Webhook message, then there is no Member associated
        }                                           // with the User, thus we default to the author for name.
        else
        {
            name = member.getEffectiveName();       //This will either use the Member's nickname if they have one,
        }                                           // otherwise it will default to their username. (User#getName())

        System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
    }
        else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
    {
        //The message was sent in a PrivateChannel.
        //In this example we don't directly use the privateChannel, however, be sure, there are uses for it!

        System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
    }
        else if (event.isFromType(ChannelType.GROUP))   //If this message was sent to a Group. This is CLIENT only!
    {
        //The message was sent in a Group. It should be noted that Groups are CLIENT only.
        Group group = event.getGroup();
        String groupName = group.getName() != null ? group.getName() : "";  //A group name can be null due to it being unnamed.

        System.out.printf("[GRP: %s]<%s>: %s\n", groupName, author.getName(), msg);
    }


}
