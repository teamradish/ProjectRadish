package projectRadish;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CrashHandler implements Thread.UncaughtExceptionHandler
{
    @Override
    public void uncaughtException(Thread t, Throwable e)
    {
        //Get the stack trace as a string
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stackTrace = result.toString();

        //Get the current path
        String workingDir = System.getProperty("user.dir");

        //Get a time-stamped file and remove characters that can't be in filenames
        String newFileName = "PRCrash - " + LocalDateTime.now();
        newFileName = newFileName.replace(":", "-");
        newFileName = newFileName.replace('.', '-');
        newFileName += ".txt";

        //Combine paths
        Path filePath = Paths.get(workingDir, newFileName);

        //File contents
        List<String> lines = Arrays.asList("Thread \"" + t.getName() + "\" encountered an unhandled exception\n",
                "Message: " + e.getMessage() + "\n", "Stack Trace: ", stackTrace);

        try
        {
            //Write to file
            Files.write(filePath, lines, Charset.forName("UTF-8"), StandardOpenOption.CREATE_NEW);
        }
        catch (Exception exception)
        {

        }

        System.out.println("Wrote crash file to: " + filePath);
    }
}