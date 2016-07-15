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

<<<<<<< HEAD
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
=======
    private Runnable networkCheck;
    private Runnable jointData;
    /**
     * The Socket.
     */
    static DatagramSocket socket;
    /**
     * The Poll network.
     */
    static boolean pollNetwork =true;

    /**
     * Get the boolean data.
     *
     * @return the boolean
     */
    public boolean getCollectData(){return pollNetwork;}

    /**
     * Set poll network.
     *
     * @param a the poll network
     */
    public static void setPollNetwork(boolean a){pollNetwork = a;}

    /**
     * Get datagram socket.
     *
     * @return the datagram socket
     */
    public static DatagramSocket getSocket(){return socket;}


    /**
     * Instantiates a new Network runnable.
     *
     * @param runnable1 the first runnable- Network Check
     * @param runnable2 the second runnable 2- Joint Data
     */
    public NetworkRunnable(Runnable runnable1, Runnable runnable2) {
        this.networkCheck = runnable1;
        this.jointData = runnable2;
>>>>>>> fcf04d10e20a8e8cddb84c49fcaf403b58d83ed7
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
