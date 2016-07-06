package com.annaleonard.myfirstimmersion;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import com.google.android.glass.media.Sounds;
import com.google.android.glass.view.WindowUtils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by svangara on 7/6/16.
 */

public class GraphActivity extends Activity implements ViewSwitcher.ViewFactory, View.OnClickListener{

    //Might not need textswitchers
    private TextSwitcher[] jointSwitcherArray = new TextSwitcher[7];    //Array containing text switchers for all joints view
    private TextSwitcher desiredJoint, desiredJointPos;    //Text Switchers for single joints view
    private int[] switcherId = {R.id.joint_a_val, R.id.joint_b_val, R.id.joint_c_val, R.id.joint_d_val, R.id.joint_e_val, R.id.joint_f_val, R.id.joint_g_val};    //xml locations of switchers for all joints view
    private int[] layoutId = {R.id.joint_a, R.id.joint_b, R.id.joint_c, R.id.joint_d, R.id.joint_e, R.id.joint_f, R.id.joint_g};


    String[] jointStringArray = new String[7];
    int whichJoint = -1;
    RunJointData mJointData = new RunJointData();
    Thread mThread;
    RunNetworkCheck mNetworkCheck = new RunNetworkCheck(GraphActivity.this);
    NoInternet mNoInternet;
    NetworkRunnable mNetworkRunnable;

    //Previous fields
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps activity awake

        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);//Enable voice activated menu

        setContentView(R.layout.show_general_graph);

        // we get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);

        //makeAllJointTextSwitchers();
        mNetworkRunnable = new NetworkRunnable(mNetworkCheck, mJointData);
        mThread = new Thread(mNetworkRunnable);
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {

        super.onStart();

        mThread.start();
        mNoInternet = new NoInternet(GraphActivity.this, R.drawable.ic_cloud_sad_150, R.string.alert_text, R.string.alert_footnote_text, mOnClickListener);
        mUpdateNoNetwork.startUpdates();
        mUpdateJointVals.startUpdates();

    }

    protected void onPause() {
        super.onPause();
        try{
            mNetworkRunnable.setPollNetwork(false);
            mThread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    UIUpdater mUpdateJointVals = new UIUpdater(new Runnable() {
        @Override
        public void run() {
            /*
            if(mNetworkCheck.getIsConnected()) {
                mNetworkRunnable.setPollNetwork(true);
                jointStringArray = mJointData.getJointStringArray();
                if (whichJoint > 0) {
                    addEntry(Double.parseDouble(jointStringArray[whichJoint - 1]));
                }
            }
            else{
                mNetworkRunnable.setPollNetwork(false);

            } */

            addEntry(6);

        }

    });

    UIUpdater mUpdateNoNetwork = new UIUpdater(new Runnable() {
        @Override
        public void run() {
            if((!mNetworkCheck.getIsConnected()&&!mNoInternet.isShowing()) || !mJointData.getReceivingData())
            {
                mNoInternet.show();
            }
            else if (mNetworkCheck.getIsConnected()&&mNoInternet.isShowing()&& mJointData.getReceivingData())
            {
                mNoInternet.dismiss();
            }

        }
    });

    // add random data to graph
    private void addEntry(double data) {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, data), true, 10);
    }


    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if ((featureId == WindowUtils.FEATURE_VOICE_COMMANDS) || (featureId == Window.FEATURE_OPTIONS_PANEL)) {
            getMenuInflater().inflate(R.menu.menu_measuring, menu);
            return true;
        }
        // Pass through to super to setup touch menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_measuring, menu);
        return true;
    }

/* This code is only useful for the Measuring Activity, as you cannot run multiple joint data streams at once on a graph
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int[] showJoints = {R.id.showJoint1, R.id.showJoint2, R.id.showJoint3, R.id.showJoint4, R.id.showJoint5, R.id.showJoint6, R.id.showJoint7};
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS || featureId == Window.FEATURE_OPTIONS_PANEL) {
            switch (item.getItemId()) {
                case (R.id.showAllJoints):
                    //returns to layout with all 7 joints.
                    setContentView(R.layout.activity_measuring);
                    makeAllJointTextSwitchers();
                    whichJoint = -1;
                    return true;

//                case (R.id.double_joint_option):
//                    setContentView(R.layout.show_2_joints);
//                    break;

                case (R.id.single_joint_option):
                    //sets view to single joint layout, but does not set switchers
                    break;

                //each option below individually sets the switchers in the single joint view to display the name and data for that particular joint.
                case (R.id.showJoint1):

                    setContentView(R.layout.show_1_joint);
                    makeSingleJointTextSwitchers();

                    am.playSoundEffect(Sounds.TAP);
                    whichJoint = 1;
                    desiredJoint.setText("Joint 1");
                    break;

                case (R.id.showJoint2):

                    setContentView(R.layout.show_1_joint);
                    makeSingleJointTextSwitchers();

                    am.playSoundEffect(Sounds.TAP);
                    whichJoint = 2;
                    desiredJoint.setText("Joint 2");
                    break;

                case (R.id.showJoint3):

                    setContentView(R.layout.show_1_joint);
                    makeSingleJointTextSwitchers();

                    am.playSoundEffect(Sounds.TAP);
                    whichJoint = 3;
                    desiredJoint.setText("Joint 3");
                    break;

                case (R.id.showJoint4):

                    setContentView(R.layout.show_1_joint);
                    makeSingleJointTextSwitchers();

                    am.playSoundEffect(Sounds.TAP);
                    whichJoint = 4;
                    desiredJoint.setText("Joint 4");
                    break;

                case (R.id.showJoint5):

                    setContentView(R.layout.show_1_joint);
                    makeSingleJointTextSwitchers();

                    am.playSoundEffect(Sounds.TAP);
                    whichJoint = 5;
                    desiredJoint.setText("Joint 5");
                    break;

                case (R.id.showJoint6):

                    setContentView(R.layout.show_1_joint);
                    makeSingleJointTextSwitchers();

                    am.playSoundEffect(Sounds.TAP);
                    whichJoint = 6;
                    desiredJoint.setText("Joint 6");
                    break;

                case (R.id.showJoint7):

                    setContentView(R.layout.show_1_joint);
                    makeSingleJointTextSwitchers();

                    am.playSoundEffect(Sounds.TAP);
                    whichJoint = 7;
                    desiredJoint.setText("Joint 7");
                    break;

                default:
                    return super.onMenuItemSelected(featureId, item);

            }
        }

        return super.onMenuItemSelected(featureId, item);
    }

    */

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.playSoundEffect(Sounds.TAP);
            openOptionsMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
/*
    public void makeAllJointTextSwitchers() {
        for (int count = 0; count < 7; count++) {
            final int i = count;
            jointSwitcherArray[count] = (TextSwitcher) findViewById(switcherId[count]);
            jointSwitcherArray[count].setFactory(new ViewSwitcher.ViewFactory() {
                public View makeView() {
                    TextView tv = new TextView(GraphActivity.this);
                    tv.setTextSize(26);
                    return tv;
                }
            });
            jointSwitcherArray[count].setText("0.00");
        }
    }

    void makeSingleJointTextSwitchers() {
        desiredJoint = (TextSwitcher) findViewById(R.id.desired_joint);//attaches each switcher to its xml id
        desiredJoint.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                TextView t = new TextView(getApplicationContext());
                t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                t.setTextSize(40);
                return t;
            }
        });


        desiredJointPos = (TextSwitcher) findViewById(R.id.desired_joint_pos);//attaches each switcher to its xml id
        desiredJointPos.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                TextView t = new TextView(getApplicationContext());
                t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                t.setTextSize(90);
                return t;
            }
        });
        desiredJointPos.setText("0.0");
    }

    */

    public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(20);
        return t;
    }

    public void onClick(View v) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.playSoundEffect(Sounds.TAP);
    }

    private final DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int button) {
            // Open WiFi Settings
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }
    };

}
