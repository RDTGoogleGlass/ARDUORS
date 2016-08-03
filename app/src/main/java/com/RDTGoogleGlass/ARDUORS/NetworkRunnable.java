package com.RDTGoogleGlass.ARDUORS;

import android.util.Log;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by gglass on 6/24/16.
 */
class NetworkRunnable implements Runnable {


    private final SynchronizedData NET_SYNC_DATA = new SynchronizedData();
    private final RunNetworkCheck NET_CHECK;
    private final RunPacketCollector PACKET_COLLECTOR;

//    public boolean getCollectData(){return collectData;}

    /**
     * The Socket.
     */

    private static final DatagramSocket SOCKET = setUpSocket(); //Use Glass IP address here

    /**
     * The Poll network.
     */
    static private boolean collectData = true;



    /**
     * Set poll network.
     *
     */

    public void stopCollectingData(){
        collectData = false;}

    /**
     * Get datagram SOCKET.
     *
     * @return the datagram SOCKET
     */
    public static DatagramSocket getSocket(){return SOCKET;}


    public NetworkRunnable(){
        NET_CHECK = new RunNetworkCheck();
        PACKET_COLLECTOR = new RunPacketCollector();

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

    private int count;
    @Override
    public void run() {
        Log.i("NetworkRunnable", "run called");

        if (SOCKET.isBound())
        {
            while(collectData) {
                //NetworkCheck
                count++;
                NET_CHECK.run();
                NET_SYNC_DATA.setIsConnected(NET_CHECK.getIsConnected());
                if (NET_CHECK.getIsConnected()) {
                    Log.i("NetworkRunnable", "internet connected");
                    PACKET_COLLECTOR.run();
                    NET_SYNC_DATA.setReceivingData(PACKET_COLLECTOR.getReceivingData());
                    if (PACKET_COLLECTOR.getReceivingData()) {
                        NET_SYNC_DATA.setPacketData(PACKET_COLLECTOR.getDataBytes());
                    }
                }
                Log.i("NetworkRunnable", "While loop running " + String.valueOf(count));
            }
        }

        try {
            SOCKET.close();
            Log.i("NetworkRunnable", "while loop exited");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
