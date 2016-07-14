package com.annaleonard.myfirstimmersion;

import android.util.Log;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by gglass on 6/24/16.
 */

public class NetworkRunnable implements Runnable {

    private RunNetworkCheck networkCheck;
    private Runnable visibleData;
    private RunNotificationCheck notificationCheck;
    static DatagramSocket socket;
    static boolean collectData = true;

//    public boolean getCollectData(){return collectData;}
    public static void setCollectData(boolean a){collectData = a;}
    public static DatagramSocket getSocket(){return socket;}


    public NetworkRunnable(RunNetworkCheck mNetworkCheck, Runnable mVisibleData, RunNotificationCheck mNotificationCheck) {
        this.networkCheck = mNetworkCheck;
        this.visibleData = mVisibleData;
        this.notificationCheck = mNotificationCheck;
    }

    @Override
    public void run() {
        int count = 0;
        //check that the socket does not exist already before creating and binding it
        if (socket == null) {
            try {
                socket = new DatagramSocket(61557, InetAddress.getByName("10.0.0.15")); //Use Glass IP address here
                collectData = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        while(collectData) {
            //alternate between these two
            networkCheck.run();
            if (networkCheck.getIsConnected()) {
                visibleData.run();
                notificationCheck.run();
            }
        }

        try {
            socket.close();
        } catch (NullPointerException e) {
        }
    }
}
