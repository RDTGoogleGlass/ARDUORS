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

    private Runnable networkCheck;
    private Runnable jointData;
    static DatagramSocket socket;
    static boolean pollNetwork =true;

    public boolean getCollectData(){return pollNetwork;}
    public static void setPollNetwork(boolean a){pollNetwork = a;}
    public static DatagramSocket getSocket(){return socket;}


    public NetworkRunnable(Runnable runnable1, Runnable runnable2) {
        this.networkCheck = runnable1;
        this.jointData = runnable2;
    }

    @Override
    public void run() {
        int count = 0;
        //check that the socket does not exist already before creating and binding it
        if (socket == null) {
            try {
                socket = new DatagramSocket(61557, InetAddress.getByName("10.0.0.15")); //Use Glass IP address here
                pollNetwork = true;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }


        while(pollNetwork) {
            //alternate between these two
            networkCheck.run();
            jointData.run();
        }

        try {
            socket.close();
        } catch (NullPointerException e) {
        }
    }
}
