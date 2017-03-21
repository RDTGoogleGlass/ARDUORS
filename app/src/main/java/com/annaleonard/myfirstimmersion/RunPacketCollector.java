package com.annaleonard.myfirstimmersion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.apache.commons.io.IOUtils;




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
            ByteArrayInputStream packet = new ByteArrayInputStream(buf){

            public void write(int oneByte) throws IOException {

            }
        };

        try {
            Thread.sleep(10, 0);

            try {
                NetworkRunnable.getSocket().getInputStream();   //receive TCP packet
                receivingData = true;
            } catch (Exception e) {
                receivingData = false;
                e.printStackTrace();
            }

            //Get data from TCP packet and convert to shared byte array
            if (receivingData) {
                dataBytes = ByteBuffer.wrap(IOUtils.toByteArray(packet)).order(ByteOrder.LITTLE_ENDIAN);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
