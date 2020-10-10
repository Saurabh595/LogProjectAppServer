package com.log.project.app.server.forwarder;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogCapturer implements Runnable
{
    public LogCapturer(String mHostName) throws FileNotFoundException
    {
        this.mHostName = mHostName;
        mBufferedReader = new BufferedReader(new FileReader("A.txt"));
    }

    @Override
    public void run()
    {
        while(!ismCancelled())
        {
            String line = null;
            try {
                line = mBufferedReader.readLine();
                if(line != null && !line.isEmpty())
                {
                    sendDataToLogServer(line);
                }
                else
                {
                    System.out.println("no data found");
                    Thread.sleep(500);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendDataToLogServer(String line)
    {
        System.out.println(line);
        try
        {
            URL url = new URL ("http://localhost:8054/addLog");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            Gson gson = new Gson();
            String payload = gson.toJson(new Log(System.currentTimeMillis(), mHostName, line));

            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(payload);
            osw.flush();
            osw.close();
            System.out.println(con.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel()
    {
        System.out.println("in log capturer cancel");
        mCancelled = true;
    }

    public boolean ismCancelled() {
        return mCancelled;
    }

    private final BufferedReader mBufferedReader;
    private volatile boolean mCancelled;
    private final String mHostName;
}
