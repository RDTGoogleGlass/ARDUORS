package com.annaleonard.myfirstimmersion;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by jlong on 7/13/16.
 */
public class RunNotificationCheck implements Runnable {
    DatagramPacket packet;
    String notificationText;
    int notificationColor;
    boolean notificationRecieved;

    public String getNotificationText (){return notificationText;}
    public int getNotificationColor (){return notificationColor;}


    public void run() {

        Thread thisThread = Thread.currentThread(); //set flag to current thread

        //create packet shell


        try {
            Thread.sleep(10, 0);

            /*
            Receive TCP packets here
            Set notificationReceived boolean to true if packet recieved
             */


            //Get data from TCP packet and convert to user-ready information
            if (notificationRecieved) {
               /*
               readd packet and determine type of notification
               set notificationText and notificationColor based on tyoe of notification
                */
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
