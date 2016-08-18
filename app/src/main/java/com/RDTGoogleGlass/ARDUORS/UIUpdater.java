package com.RDTGoogleGlass.ARDUORS;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * A class used to perform periodical updates,
 * specified inside a runnable object. An update interval
 * may be specified (otherwise, the class will perform the
 * update every .015 seconds).
 *
 * @author Carlos Simões
 */
class UIUpdater {
    // Create a Handler that uses the Main Looper to run in
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Context context;
    private final Runnable mStatusChecker;
    private int UPDATE_INTERVAL = 50;
    private final SynchronizedData notificationSynchronizedData = new SynchronizedData();


    private static NetworkIssue noInternet;
    //    noInternet.setCancelable(false);

    private static NetworkIssue noData;
//    noData.setCancelable(false);

    /**
     * Creates an UIUpdater object, that can be used to
     * perform UIUpdates on a specified time interval.
     *
     * @param uiUpdater A runnable containing the update routine.
     */
    public UIUpdater(final Runnable uiUpdater) {
        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                // Run the passed runnable
                context = App.getContext();
                Log.i("UI verifying netConnect", String.valueOf(notificationSynchronizedData.getUsingData()));
                if(noInternet==null)
                {
                    noInternet = new NetworkIssue(context, R.drawable.ic_cloud_sad_150, R.string.no_internet_text, R.string.alert_footnote_text);
                }

                if(noData==null) {
                    noData = new NetworkIssue(context, R.drawable.ic_warning_150, R.string.no_data_text, R.string.alert_footnote_text);
                }

                if (notificationSynchronizedData.getUsingData())
                {


                    if (notificationSynchronizedData.getIsConnected())
                    {

                        if(noInternet.isShowing())
                        {
                            noInternet.dismiss();
                        }

                        if (notificationSynchronizedData.getReceivingData())
                        {
                            if (noData.isShowing())
                            {
                                noData.dismiss();
                            }

                            //UiUpdater instance created in the Activity method and updates the visible data.
                            uiUpdater.run();

                            //run notificationChecker here:
                            ByteBuffer currentData = notificationSynchronizedData.getPacketData();
                            //parse notifications out of ByteBuffer

                            //update notification footer
//                        footer.setText(mNotificationCheck.getNotificationText());
//                        footer.setBackgroundColor(mNotificationCheck.getNotificationColor());
//                        handle multiple notifications here too.

                        }
                        else
                        {

                            Log.i("show page", String.valueOf((!noData.isShowing())&&notificationSynchronizedData.getUsingData()));

                            if((!noData.isShowing())&&notificationSynchronizedData.getUsingData())
                            {
                                noData.show();
                            }

                        }
                    }
                    else
                    {


                        if(!noInternet.isShowing()&&notificationSynchronizedData.getUsingData())
                        {
                            noInternet.show();
                        }
                    }
                }
                else if (noInternet.isShowing())
                {
                    noInternet.dismiss();
                }
                else if(noData.isShowing())
                {
                    noData.dismiss();
                }

                // Re-run it after the update interval
                mHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
    }

    /**
     * The same as the default constructor, but specifying the
     * intended update interval.
     *
     * @param uiUpdater A runnable containing the update routine.
     * @param interval  The interval over which the routine                  should run (milliseconds).
     */
    public UIUpdater(Runnable uiUpdater, int interval){
        this(uiUpdater);
        UPDATE_INTERVAL = interval;

    }

    /**
     * Starts the periodical update routine (mStatusChecker
     * adds the callback to the handler).
     */
    public synchronized void startUpdates(){
        mStatusChecker.run();
    }

    /**
     * Stops the periodical update routine from running,
     * by removing the callback.
     */
    public synchronized void stopUpdates(){

        mHandler.removeCallbacks(mStatusChecker);
    }



}
