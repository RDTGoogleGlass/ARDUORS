package com.annaleonard.myfirstimmersion;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by gglass on 6/15/16.
 */
public class RunJointData implements Runnable {
    DatagramPacket packet;
    final String[] jointStringArray = new String[7];
    DecimalFormat jointPosFormat = new DecimalFormat("0.00");   //format to specify sig figs
    double[] jointDoubleArray = new double[7];
    boolean receivingData;
    Random r = new Random();



    public double[] getJointDoubles(){
        return jointDoubleArray;
    }
    public String[] getJointStringArray() {return jointStringArray;}
    public boolean getReceivingData(){return receivingData;}



    public void run() {

        Thread thisThread = Thread.currentThread(); //set flag to current thread

        byte[] buf = new byte[56];
        packet = new DatagramPacket(buf, buf.length);
        Log.i("RunJointData", "packet shell created");

        try {
            Thread.sleep(10, 0);

            try {
                Log.i("RunJointData", "try entered");
                NetworkRunnable.getSocket().receive(packet);   //receive UDP packet
                receivingData = true;
            } catch (NullPointerException e) {
                receivingData = false;
                e.printStackTrace();
            } catch (IOException e) {
                receivingData = false;
                e.printStackTrace();
            }
//

            //Get data from UDP packet and convert to user-ready information    (joint values are in degrees)
            if (receivingData) {
                for (int i = 0; i < 7; i++) {
                    jointDoubleArray[i] = ByteBuffer.wrap(packet.getData()).order(ByteOrder.LITTLE_ENDIAN).getDouble(i * 8);
                    jointStringArray[i] = String.valueOf(jointPosFormat.format(jointDoubleArray[i]));
                }
                Log.i("RunJointData", "arrays created");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
