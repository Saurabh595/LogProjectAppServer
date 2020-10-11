package com.log.project.app.server;

import com.log.project.app.server.forwarder.LogCapturer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AppServerInstance implements Runnable
{
    public AppServerInstance(String hostName) throws IOException {
        mLogCapturer = new LogCapturer(hostName);
        Thread t = new Thread(mLogCapturer);
        t.start();
    }

    @Override
    public void run() {
        // Creating a File object that represents the disk file.
        //PrintStream o = new PrintStream(new File("A.txt"));

        // Assign o to output stream
        //System.setOut(o);

        try
        {
            BufferedWriter os = new BufferedWriter(new FileWriter(mLogCapturer.getForwarder().getHostName() + ".log"));
            os.write("");
            os.flush();
            for (int i = 0; i < 10; i++)
            {
                os.append(getFormattedDate() + ", Hello, sending log to stdout loop = " + i).append("\n");
                os.flush();
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException | IOException e)
        {
            e.printStackTrace();
        }
        //System.out.println("Hello, sending log to stdout");
    }

    private String getFormattedDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(new Date());
    }

    public void stopAppServer()
    {
        mLogCapturer.cancel();
    }

    LogCapturer mLogCapturer;
}
