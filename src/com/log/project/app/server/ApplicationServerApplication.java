package com.log.project.app.server;

import com.log.project.app.server.forwarder.LogCapturer;

import java.io.*;

public class ApplicationServerApplication {

    public static void main(String[] args) throws IOException, InterruptedException
    {
        AppServerInstance appServerInstance1 = startAppServer("AppServer1");
        //AppServerInstance appServerInstance2 = startAppServer("AppServer2");

        Thread.sleep(1000);
        appServerInstance1.stopAppServer();

        Thread.sleep(1000);
        appServerInstance1 = startAppServer("AppServer1");
        Thread.sleep(12000);
        appServerInstance1.stopAppServer();
        //appServerInstance2.stopAppServer();

        System.out.println("Shutting down system");
    }

    private static AppServerInstance startAppServer(String appServerHostName) throws IOException
    {
        AppServerInstance appServer = new AppServerInstance(appServerHostName);
        Thread appServerThread = new Thread(appServer);
        appServerThread.start();
        return appServer;
    }

}
