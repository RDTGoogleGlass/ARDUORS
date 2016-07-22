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
    static DatagramSocket socket = setUpSocket(); //Use Glass IP address here

    /**
     * The Poll network.
     */
    static boolean collectData = true;



    /**
     * Set poll network.
     *
     * @param a the poll network
     */
    public void setCollectData(boolean a){
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

    @Override
    public void run() {
        //check that the socket does not exist already before creating and binding it
//        if (socket == null) {
//            try {
//                socket = new DatagramSocket(61557, setUpAddress()); //Use Glass IP address here
//                collectData = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        Log.i("NetworkRunnable", "Runin");
        while(collectData) {
            //NetworkCheck
            networkCheck.run();
            networkSynchronizedData.setIsConnected(networkCheck.getIsConnected());
            Log.i("isconnectset", String.valueOf(networkCheck.getIsConnected()));
            if (networkCheck.getIsConnected())
            {
                packetCollector.run();
                networkSynchronizedData.setReceivingData(packetCollector.getReceivingData());

                if(packetCollector.getReceivingData())
                {
                    networkSynchronizedData.setPacketData(packetCollector.getDataBytes());
                }
            }
        }

        try {
            socket.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
