package com.RDTGoogleGlass.ARDUORS;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gglass on 6/24/16.
 */
class RunNetworkCheck implements Runnable {

    /**
     * Boolean saving connection state.
     */
    private static boolean isConnected = false;

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
     */
    public RunNetworkCheck(){}

    public void run() {
        ConnectivityManager cm = (ConnectivityManager)App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());


    }

}
