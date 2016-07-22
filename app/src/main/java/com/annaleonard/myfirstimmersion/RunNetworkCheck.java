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
     * Boolean saving connection state.
     */
    private static boolean isConnected = false;
    private Context context;

    /**
     * Gets isConnected.
     *
     * @return isConnected
     */
    public synchronized boolean getIsConnected() {
        return isConnected;
    }


    /**
     * Instantiates a new Run network check.
     *
     * @param c the Context
     */
    public RunNetworkCheck(Context c){context = c;}

    public void run() {
        Log.i("RunNetworkcheck", "doobdoo");
        ConnectivityManager cm = (ConnectivityManager)App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());


    }

}
