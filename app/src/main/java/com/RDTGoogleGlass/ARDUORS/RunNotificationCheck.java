package com.RDTGoogleGlass.ARDUORS;

import java.net.DatagramPacket;

/**
 * Created by jlong on 7/13/16.
 */
class RunNotificationCheck implements Runnable {
    DatagramPacket packet;
    private String notificationText;
    private int notificationColor;
    private boolean notificationReceived = false;

    public String getNotificationText (){return notificationText;}
    public int getNotificationColor (){return notificationColor;}
    public boolean getNotificationReceived(){return notificationReceived;}



    @SuppressWarnings("StatementWithEmptyBody")
    public void run() {

        //create packet shell


        try {
            Thread.sleep(10, 0);

            /*
            Receive TCP packets here
            Set notificationReceived boolean to true if packet received
             */


            //Get data from TCP packet and convert to user-ready information
            //noinspection StatementWithEmptyBody
            if (notificationReceived) {
               /*
               read packet and determine type of notification
               set notificationText and notificationColor based on type of notification
                */
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
