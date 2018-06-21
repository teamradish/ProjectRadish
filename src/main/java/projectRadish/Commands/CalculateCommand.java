package projectRadish.Commands;

import org.apache.el.lang.FunctionMapperImpl;
import org.mariuszgromada.math.mxparser.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public final class CalculateCommand extends BaseCommand
{
    private Expression expression = new Expression();

    @Override
    public void Initialize()
    {
        super.Initialize();

        //Add new arguments, functions, and such here

        //Remove Fibonacci since it can take an extremely long time to calculate higher numbers
        mXparser.removeBuiltinTokens("Fib");
    }

    @Override
    public void ExecuteCommand(String content, MessageReceivedEvent event)
    {
        expression.setExpressionString(content);

        double val = 0d;
        val = expression.calculate();

        //Make sure we get a valid expression
        if (expression.getSyntaxStatus() == false)
        {
            event.getChannel().sendMessage("Invalid expression").queue();
            return;
        }

        event.getChannel().sendMessage(Double.toString(val) + "\nCalculated in " + expression.getComputingTime() + "ms").queue();
    }
}
