package com.log.project.app.server;

import com.log.project.app.server.forwarder.LogCapturer;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class AppServerInstance implements Runnable
{
    public AppServerInstance(String hostName) throws FileNotFoundException {
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
            BufferedWriter os = new BufferedWriter(new FileWriter("A.txt"));
            for (int i = 0; i < 10; i++)
            {
                os.append("Hello, sending log to stdout loop = " + i).append("\n");
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

    public void stopAppServer()
    {
        mLogCapturer.cancel();
    }

    LogCapturer mLogCapturer;
}
