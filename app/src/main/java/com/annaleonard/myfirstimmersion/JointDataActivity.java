package com.annaleonard.myfirstimmersion;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.view.WindowUtils;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by rdtintern on 7/19/16.
 */
public class JointDataActivity extends Activity {
    /**TextSwitchers and ids that are used to update the xml layout displayed on the glass*/
    private TextSwitcher[] jointSwitcherArray = new TextSwitcher[7];
    /**Array containing text switchers for all joints view*/
    private TextSwitcher desiredJoint, desiredJointPos;
    /**Text Switchers for single joints view*/

    private int[] switcherId = {R.id.joint_a_val, R.id.joint_b_val, R.id.joint_c_val, R.id.joint_d_val, R.id.joint_e_val, R.id.joint_f_val, R.id.joint_g_val};    //xml locations of switchers for all joints view

    private LineGraphSeries<DataPoint> series;

    /**
     * Integer lastX sets the x value when graphing data points
     */
    private int lastX = 0;

    /**
     * The Joint string array.
     */
    String[] jointStringArray = new String[7];


    /**
     * The Joint double array.
     */
    double[] jointDoubleArray = new double[7];

    /**
     * The Which joint.
     */
    int whichJoint = -1;
    /**
     * The M joint data.
     */
    RunJointData mJointData = new RunJointData();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps activity awake

        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);//Enable voice activated menu
        setContentView(R.layout.all_joints);  //Set the desired layout to display on screen

        //Set up 7 text switchers for all joint view.  One for each joint.
        makeAllJointTextSwitchers();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        mUpdateJointVals.startUpdates();

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }


    @Override
    protected void onStop(){
        super.onStop();
        mUpdateJointVals.stopUpdates();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_jointdata, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.playSoundEffect(Sounds.TAP);
            openOptionsMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case (R.id.showAllJoints):
                setContentView(R.layout.all_joints);
                makeAllJointTextSwitchers();
                whichJoint = -1;
                return true;
            case (R.id.single_joint_option):
                //sets view to single joint layout, but does not set switchers
                return true;

            //each option below individually sets the switchers in the single joint view to display the name and data for that particular joint.
            case (R.id.showJoint1):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextSwitchers();

                whichJoint = 1;
                desiredJoint.setText("Joint 1");
                return true;

            case (R.id.showJoint2):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextSwitchers();

                whichJoint = 2;
                desiredJoint.setText("Joint 2");
                return true;

            case (R.id.showJoint3):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextSwitchers();

                whichJoint = 3;
                desiredJoint.setText("Joint 3");
                return true;

            case (R.id.showJoint4):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextSwitchers();

                whichJoint = 4;
                desiredJoint.setText("Joint 4");
                return true;

            case (R.id.showJoint5):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextSwitchers();

                whichJoint = 5;
                desiredJoint.setText("Joint 5");
                return true;

            case (R.id.showJoint6):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextSwitchers();

                whichJoint = 6;
                desiredJoint.setText("Joint 6");
                return true;

            case (R.id.showJoint7):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextSwitchers();

                whichJoint = 7;
                desiredJoint.setText("Joint 7");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * The M update joint vals.
     */
    UIUpdater mUpdateJointVals = new UIUpdater(new Runnable() {
        @Override
        public void run() {
            // do stuff ...
            jointStringArray = mJointData.getJointStringArray();
            jointDoubleArray = mJointData.getJointDoubles();
            if (whichJoint > 0) {
                series.appendData(new DataPoint(lastX++, jointDoubleArray[whichJoint-1]), true, 10);
            } else {
                for (int i = 0; i < 7; i++) {
                    jointSwitcherArray[i].setText(jointStringArray[i]);
                }
            }
        }

    });

    /**
     * Make all joint text switchers.
     */
    public void makeAllJointTextSwitchers() {
        for (int count = 0; count < 7; count++) {
            final int i = count;
            jointSwitcherArray[count] = (TextSwitcher) findViewById(switcherId[count]);
            jointSwitcherArray[count].setFactory(new ViewSwitcher.ViewFactory() {
                public View makeView() {
                    TextView tv = new TextView(JointDataActivity.this);
                    tv.setTextSize(26);
                    return tv;
                }
            });
            jointSwitcherArray[count].setText("0.00");
        }
    }

    /**
     * Make single force text switchers.
     */
    void makeSingleJointTextSwitchers() {
        desiredJoint = (TextSwitcher) findViewById(R.id.desired_data);//attaches each switcher to its xml id
        desiredJoint.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                TextView t = new TextView(getApplicationContext());
//                t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZO
                t.setTextSize(40);
                return t;
            }
        });


        desiredJointPos = (TextSwitcher) findViewById(R.id.desired_data_val);//attaches each switcher to its xml id
        desiredJointPos.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                TextView t = new TextView(getApplicationContext());
//                t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                t.setTextSize(90);
                return t;
            }
        });
        desiredJointPos.setText("0.00");
    }
}
