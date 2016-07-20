//package com.annaleonard.myfirstimmersion;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.AudioManager;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.TextSwitcher;
//import android.widget.TextView;
//import android.widget.ViewSwitcher;
//
//import com.google.android.glass.media.Sounds;
//import com.google.android.glass.view.WindowUtils;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.Viewport;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
//
//
///**
// * The type Measuring activity.
// */
//public class MeasuringActivity extends Activity implements ViewSwitcher.ViewFactory, View.OnClickListener {
//
//
//
//    private TextSwitcher footer;
//
//
//
//    String notificationText;
//    int notificationColor;
//
//
//    /**
//     * The M thread.
//     */
//    Thread mThread;
//
//    RunNotificationCheck mNotificationCheck = new RunNotificationCheck();
//
//    /**
//     * The M network check.
//     */
//    RunNetworkCheck mNetworkCheck = new RunNetworkCheck(MeasuringActivity.this);
//    /**
//     * The M no internet.
//     */
//    NoInternet mNoInternet;
//    /**
//     * The M network runnable.
//     */
//    NetworkRunnable mNetworkRunnable;
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        mNetworkRunnable = new NetworkRunnable(mNetworkCheck, mJointData, mNotificationCheck);
//        mThread = new Thread(mNetworkRunnable);
//
//
//    }
//
//
//    @Override
//    public void onAttachedToWindow() {
//        openOptionsMenu();
//    }
//
//    @Override
//    protected void onStart() {
//
//        super.onStart();
//
//        mThread.start();
//        mNoInternet = new NoInternet(MeasuringActivity.this, R.drawable.ic_cloud_sad_150, R.string.alert_text, R.string.alert_footnote_text, mOnClickListener);
//        mUpdateNoNetwork.startUpdates();
//        mSendNotification.startUpdates();
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        try{
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try{
//            mNetworkRunnable.setCollectData(false);
//            mThread.join();
//        }catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//
//
//
//
//
//
//    // add random data to graph
////    private void addEntry(double data) {
////        // here, we choose to display max 10 points on the viewport and we scroll to end
//        series.appendData(new DataPoint(lastX++, data), true, 10);
////    }
//
//    /**
//     * The M update no network.
//     */
//    UIUpdater mUpdateNoNetwork = new UIUpdater(new Runnable() {
//        @Override
//        public void run() {
//            if((!mNetworkCheck.getIsConnected()&&!mNoInternet.isShowing()))
//            {
//                mNoInternet.show();
//            }
//            else if (mNetworkCheck.getIsConnected()&&mNoInternet.isShowing())
//            {
//                mNoInternet.dismiss();
//            }
//
//        }
//    });
//
//    UIUpdater mSendNotification = new UIUpdater(new Runnable() {
//        @Override
//        public void run() {
//            if(mNotificationCheck.getNotificationReceived())
//            {
//                footer.setText(mNotificationCheck.getNotificationText());
//                footer.setBackgroundColor(mNotificationCheck.getNotificationColor());
//            }
//
//        }
//    });
//
//
//    @Override
//    public boolean onCreatePanelMenu(int featureId, Menu menu) {
//        if ((featureId == WindowUtils.FEATURE_VOICE_COMMANDS) || (featureId == Window.FEATURE_OPTIONS_PANEL)) {
//            getMenuInflater().inflate(R.menu.menu_measuring, menu);
//            return true;
//        }
//        // Pass through to super to setup touch menu.
//        return super.onCreatePanelMenu(featureId, menu);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_measuring, menu);
//        return true;
//    }
//    /**Overridden to be able to select which xml view to switch to based on menu input*/
//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int[] showJoints = {R.id.showJoint1, R.id.showJoint2, R.id.showJoint3, R.id.showJoint4, R.id.showJoint5, R.id.showJoint6, R.id.showJoint7};
//        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS || featureId == Window.FEATURE_OPTIONS_PANEL) {
//            switch (item.getItemId()) {
//                case (R.id.startGraph):
//
//
//                    // we get graph view instance
//                    GraphView graph = (GraphView) findViewById(R.id.graph);
//                    // data
//                    series = new LineGraphSeries<DataPoint>();
//                    graph.addSeries(series);
//                    // customize a little bit viewport
//                    Viewport viewport = graph.getViewport();
//                    viewport.setYAxisBoundsManual(true);
//                    viewport.setMinY(0);
//                    viewport.setMaxY(10);
//                    viewport.setScrollable(true);                    return true;
//
//
//                default:
//                    return super.onMenuItemSelected(featureId, item);
//
//            }
//        }
//
//        return super.onMenuItemSelected(featureId, item);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            am.playSoundEffect(Sounds.TAP);
//            openOptionsMenu();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//
//    void makeNotificationTextSwitcher(){
//        footer = (TextSwitcher) findViewById(R.id.footer);
//        footer.setFactory(new ViewSwitcher.ViewFactory() {
//
//            @Override
//            public View makeView() {
//                TextView t = new TextView(getApplicationContext());
//                t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//                return t;
//            }
//        });
//    }
//
//
//
//    public View makeView() {
//        TextView t = new TextView(this);
//        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
//        t.setTextSize(20);
//        return t;
//    }
//
//    public void onClick(View v) {
//        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        am.playSoundEffect(Sounds.TAP);
//    }
//
//    private final DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int button) {
//            // Open WiFi Settings
//            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//        }
//    };
//
//
//}
//
