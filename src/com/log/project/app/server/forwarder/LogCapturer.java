package com.log.project.app.server.forwarder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class LogCapturer implements Runnable
{
    public LogCapturer(String hostName) throws IOException
    {
        getForwarder(hostName);
        File logFile = new File(hostName + ".log");
        logFile.createNewFile();
        mBufferedReader = new BufferedReader(new FileReader(logFile));
        mBufferedReader.skip(mForwarder.getLastReadPosition());
    }

    @Override
    public void run()
    {
        while(!isCancelled())
        {
            String line = null;
            try {
                line = mBufferedReader.readLine();
                if(line != null && !line.isEmpty())
                {
                    mLastReadPosition += line.length();
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
            System.out.println("lastReadPosition = " + mLastReadPosition);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            String payload = mGson.toJson(new RequestBodyLog(line, mForwarder.getId(), mLastReadPosition));

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

    public boolean isCancelled() {
        return mCancelled;
    }

    private void getForwarder(String hostName)
    {
        try
        {
            URL url = new URL ("http://localhost:8054/getForwarder?hostName=" + hostName);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            int requestCode = con.getResponseCode();
            if(requestCode == 200)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String responseBody = br.lines().collect(Collectors.joining());
                mForwarder = mGson.fromJson(responseBody, Forwarder.class);
            }
            else
            {
                //TODO throw error
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Forwarder getForwarder() {
        return mForwarder;
    }

    private final BufferedReader mBufferedReader;
    private volatile boolean mCancelled;
    private Forwarder mForwarder;
    private final Gson mGson = new GsonBuilder().create();
    private long mLastReadPosition;
}
