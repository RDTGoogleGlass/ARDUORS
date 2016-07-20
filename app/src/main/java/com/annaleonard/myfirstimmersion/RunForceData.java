package com.annaleonard.myfirstimmersion;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by rdtintern on 7/19/16.
 */
public class RunForceData {

    /**
     * The Packet.
     */
    DatagramPacket packet;
    /**
     * The Force string array.
     */
    final String[] forceStringArray = new String[7];
    /**
     * The Force pos format.
     */
    DecimalFormat forcePosFormat = new DecimalFormat("0.00");   //format to specify sig figs
    /**
     * The Force double array.
     */
    double[] forceDoubleArray = new double[7];
    /**
     * The Receiving data.
     */
    boolean receivingData;
    Random r = new Random();


    /**
     * Get force double [ ].
     *
     * @return the double [ ]
     */
    public synchronized double[] getForceDoubles(){
        return forceDoubleArray;
    }

    /**
     * Get force string [ ].
     *
     * @return the string [ ]
     */
    public synchronized String[] getForceStringArray() {return forceStringArray;}

    /**
     * Get receiving data boolean.
     *
     * @return the boolean
     */
    public synchronized boolean getReceivingData(){return receivingData;}



    public void run() {

        Thread thisThread = Thread.currentThread(); //set flag to current thread

        byte[] buf = new byte[48];
        packet = new DatagramPacket(buf, buf.length);
        Log.i("RunForceData", "packet shell created");

        try {
            Thread.sleep(10, 0);

            try {
                Log.i("RunForceData", "try entered");
                NetworkRunnable.getSocket().receive(packet);   //receive UDP packet
                receivingData = true;
            } catch (Exception e) {
                receivingData = false;
                e.printStackTrace();
            }
//

            //Get data from UDP packet and convert to user-ready information    (force values are in degrees)
            if (receivingData) {
                for (int i = 0; i < 7; i++) {
                    forceDoubleArray[i] = ByteBuffer.wrap(packet.getData()).order(ByteOrder.LITTLE_ENDIAN).getDouble(i * 8);
                    forceStringArray[i] = String.valueOf(forcePosFormat.format(forceDoubleArray[i]));
                }
                Log.i("RunForceData", "arrays created");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
