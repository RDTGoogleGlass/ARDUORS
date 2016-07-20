package com.annaleonard.myfirstimmersion;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by gglass on 6/24/16.
 */
public class NetworkRunnable implements Runnable {

    private RunNetworkCheck networkCheck;
    private Runnable visibleData;
    private RunNotificationCheck notificationCheck;

//    public boolean getCollectData(){return collectData;}

    /**
     * The Socket.
     */
    static DatagramSocket socket;
    /**
     * The Poll network.
     */
    static boolean collectData =true;

    /**
     * Get the boolean data.
     *
     * @return the boolean
     */
    public boolean getCollectData(){return collectData;}

    /**
     * Set poll network.
     *
     * @param a the poll network
     */
    public static void setCollectData(boolean a){
        collectData = a;}

    /**
     * Get datagram socket.
     *
     * @return the datagram socket
     */
    public static DatagramSocket getSocket(){return socket;}


    public NetworkRunnable(RunNetworkCheck mNetworkCheck, Runnable mVisibleData, RunNotificationCheck mNotificationCheck) {
        this.networkCheck = mNetworkCheck;
        this.visibleData = mVisibleData;
        this.notificationCheck = mNotificationCheck;
    }

    public NetworkRunnable(Runnable mVisibleData)
    {visibleData=mVisibleData;}


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
