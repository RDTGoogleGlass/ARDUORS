package com.annaleonard.myfirstimmersion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;

/**
 * Created by gglass on 6/15/16.
 */
public class BackgroundThread extends Thread {
    DatagramSocket socket;
    DatagramPacket packet;
    final String[] jointStringArray = new String[7];
    DecimalFormat jointPosFormat = new DecimalFormat("0.00");   //format to specify sig figs
    double[] jointDoubleArray = new double[7];



    BackgroundThread(){
    }


    public void start() {
       run();
    }

    public double[] getJointDoubles(){
        return jointDoubleArray;
    }

    public void run(){
        Thread thisThread = Thread.currentThread(); //set flag to current thread

        //check that the socket does not exist already before creating and binding it
        if (socket == null) {
            try {
                socket = new DatagramSocket(61557, InetAddress.getByName("10.0.0.15")); //Use Glass IP address here
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        while (this == thisThread) {
            byte[] buf = new byte[56];
            packet = new DatagramPacket(buf, buf.length);

            try {
                Thread.sleep(10, 0);

                try {
                    socket.receive(packet);   //receive UDP packet
                } catch (NullPointerException e) {
                    break;
                }

                //Get data from UDP packet and convert to user-ready information    (joint values are in degrees)
                for (int i = 0; i < 7; i++) {
                    jointDoubleArray[i] = ByteBuffer.wrap(packet.getData()).order(ByteOrder.LITTLE_ENDIAN).getDouble(i * 8);
                    jointStringArray[i] = String.valueOf(jointPosFormat.format(jointDoubleArray[i]));
                }

            } catch (InterruptedException e) {
//                Log.i("InterruptedException",e.getMessage());
            } catch (IOException e) {
//                Log.i("IOException",e.getMessage());
            }
        }   //Justin Brannan is awesome and helps poor lost souls with git.

    }


}
