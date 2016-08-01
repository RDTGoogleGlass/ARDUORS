package com.annaleonard.myfirstimmersion;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * Created by rdtintern on 7/21/16.
 */
class RunPacketCollector implements Runnable{

    /**
     * The data double array.
     */
    private ByteBuffer dataBytes;

    /**
     * The boolean receiving data
     */
    private boolean receivingData = false;

    public ByteBuffer getDataBytes(){return dataBytes;}
    public boolean getReceivingData(){return receivingData;}

    @Override
    public void run() {

        byte[] buf = new byte[56];
        /*
      The packet.
     */
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            Thread.sleep(10, 0);

            try {
                NetworkRunnable.getSocket().receive(packet);   //receive UDP packet
                receivingData = true;
            } catch (Exception e) {
                receivingData = false;
                e.printStackTrace();
            }

            //Get data from UDP packet and convert to shared byte array
            if (receivingData) {
                dataBytes = ByteBuffer.wrap(packet.getData()).order(ByteOrder.LITTLE_ENDIAN);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
