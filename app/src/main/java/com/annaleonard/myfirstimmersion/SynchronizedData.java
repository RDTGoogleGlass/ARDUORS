package com.annaleonard.myfirstimmersion;

import java.nio.ByteBuffer;

/**
 * Created by rdtintern on 7/20/16.
 */
class SynchronizedData {

    private static boolean isConnected = false;
    private static boolean receivingData = false;
    private static ByteBuffer packetData;


    public void setIsConnected (boolean connectStatus)
    {
        synchronized (this)
        {isConnected = connectStatus;}
    }

    public synchronized boolean getIsConnected(){
        return isConnected;
    }

    public void setReceivingData (boolean dataReceived)
    {
        synchronized (this)
        {receivingData = dataReceived;}
    }
    public synchronized boolean getReceivingData(){
        return receivingData;
    }

    public void setPacketData(ByteBuffer newPacket)
    {
        synchronized (this)
        {packetData = newPacket;}
    }

    public synchronized ByteBuffer getPacketData() {return packetData;}

}
