package com.annaleonard.myfirstimmersion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by gglass on 6/24/16.
 */
public class RunNetworkCheck implements Runnable {

    /**
     * The Is connected.
     */
    public boolean isConnected;
    private Context context;

    /**
     * Gets is connected.
     *
     * @return the is connected
     */
    public boolean getIsConnected() {
        return isConnected;
    }


    /**
     * Instantiates a new Run network check.
     *
     * @param c the c
     */
    public RunNetworkCheck(Context c){context = c;}

    public void run() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


    }

}
