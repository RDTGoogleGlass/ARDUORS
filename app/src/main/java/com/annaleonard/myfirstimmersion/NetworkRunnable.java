package com.annaleonard.myfirstimmersion;


import android.util.Log;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by gglass on 6/24/16.
 */
public class NetworkRunnable implements Runnable {

    private SynchronizedData networkSynchronizedData = new SynchronizedData();
    private RunNetworkCheck networkCheck;
    private RunPacketCollector packetCollector;

//    public boolean getCollectData(){return collectData;}

    /**
     * The Socket.
     */
    private static DatagramSocket socket = setUpSocket(); //Use Glass IP address here

    /**
     * The Poll network.
     */
    static private boolean collectData = true;



    /**
     * Set poll network.
     *
     * @param a the poll network
     */
    public void setCollectData(boolean a){
        Log.i("NetworkRunnable", "setCollectData " + String.valueOf(a));
        collectData = a;}

    /**
     * Get datagram socket.
     *
     * @return the datagram socket
     */
    public static DatagramSocket getSocket(){return socket;}


    public NetworkRunnable(){
        networkCheck = new RunNetworkCheck(App.getContext());
        packetCollector = new RunPacketCollector();

    }

    public NetworkRunnable(RunNetworkCheck mNetworkCheck, RunPacketCollector mPacketCollector) {
        this.networkCheck = mNetworkCheck;
        this.packetCollector = mPacketCollector;
    }



    private static DatagramSocket setUpSocket() {

        DatagramSocket mSocket;
        try {
            mSocket = new DatagramSocket(61557, InetAddress.getByName("10.0.0.15"));
        } catch (Exception e) {
            mSocket = null;
            e.printStackTrace();
        }
        return mSocket;
    }

    int count;
    @Override
    public void run() {
        Log.i("NetworkRunnable", "run called");

        if (socket.isBound())
        {
            while(collectData) {
                //NetworkCheck
                count++;
                networkCheck.run();
                networkSynchronizedData.setIsConnected(networkCheck.getIsConnected());
                if (networkCheck.getIsConnected()) {
                    Log.i("NetworkRunnable", "internet connected");
                    packetCollector.run();
                    networkSynchronizedData.setReceivingData(packetCollector.getReceivingData());
                    if (packetCollector.getReceivingData()) {
                        networkSynchronizedData.setPacketData(packetCollector.getDataBytes());
                    }
                }
                Log.i("NetworkRunnable", "While loop running " + String.valueOf(count));
            }
        }

        try {
            socket.close();
            Log.i("NetworkRunnable", "while loop exited");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
