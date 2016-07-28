package com.annaleonard.myfirstimmersion;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
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

import java.nio.ByteBuffer;
import java.text.DecimalFormat;

/**
 * Created by rdtintern on 7/19/16.
 */
public class JointDataActivity extends Activity {
    /**TextViews and ids that are used to update the xml layout displayed on the glass*/
    private TextView[] jointViewArray = new TextView[7];
    /**Array containing text views for all joints view*/
    private TextView desiredJoint, desiredJointPos;
    private TextSwitcher footer;
    /**Text Views for single joints view*/
    private int[] viewId = {R.id.joint_a_val, R.id.joint_b_val, R.id.joint_c_val, R.id.joint_d_val, R.id.joint_e_val, R.id.joint_f_val, R.id.joint_g_val};    //xml locations of views for all joints view

    /**
     * The Joint pos format.
     */
    DecimalFormat jointPosFormat = new DecimalFormat("0.00");   //format to specify sig figs

    SynchronizedData jointSyncData = new SynchronizedData();


    ByteBuffer jointData;

    /**
     * The Joint double array.
     */
    double[] jointDataArray = new double[7];

    /**
     * The Which joint.
     */
    int whichJoint = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps activity awake

        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);//Enable voice activated menu
        setContentView(R.layout.all_joints);  //Set the desired layout to display on screen

        //Set up 7 text views for all joint view.  One for each joint.
        makeAllJointTextViews();
        App.setContext(this);
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUpdateJointVals.startUpdates();


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
                makeAllJointTextViews();
                whichJoint = -1;
                App.setContext(this);
                return true;
            case (R.id.single_joint_option):
                //sets view to single joint layout, but does not set views
                return true;

            //each option below individually sets the views in the single joint view to display the name and data for that particular joint.
            case (R.id.showJoint1):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextViews();

                whichJoint = 1;
                desiredJoint.setText("Joint 1");
                App.setContext(this);
                return true;

            case (R.id.showJoint2):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextViews();

                whichJoint = 2;
                desiredJoint.setText("Joint 2");
                App.setContext(this);
                return true;

            case (R.id.showJoint3):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextViews();

                whichJoint = 3;
                desiredJoint.setText("Joint 3");
                App.setContext(this);
                return true;

            case (R.id.showJoint4):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextViews();

                whichJoint = 4;
                desiredJoint.setText("Joint 4");
                App.setContext(this);
                return true;

            case (R.id.showJoint5):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextViews();

                whichJoint = 5;
                desiredJoint.setText("Joint 5");
                App.setContext(this);
                return true;

            case (R.id.showJoint6):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextViews();

                whichJoint = 6;
                desiredJoint.setText("Joint 6");
                App.setContext(this);
                return true;

            case (R.id.showJoint7):

                setContentView(R.layout.show_1_joint);
                makeSingleJointTextViews();

                whichJoint = 7;
                desiredJoint.setText("Joint 7");
                App.setContext(this);
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
            jointData = jointSyncData.getPacketData();
            if (whichJoint > 0) {
                desiredJointPos.setText(String.valueOf(jointPosFormat.format(jointData.getDouble((whichJoint-1)*8))));
            } else {
                for (int i = 0; i < 7; i++) {
                    jointViewArray[i].setText(String.valueOf(jointPosFormat.format(jointData.getDouble(i*8))));
                }
            }
        }

    });

    /**
     * Make all joint text views.
     */
    public void makeAllJointTextViews() {
        makeNotificationTextSwitcher();
        for (int count = 0; count < 7; count++) {
            final int i = count;
            jointViewArray[count] = (TextView) findViewById(viewId[count]);
            Log.i("View " + String.valueOf(count), String.valueOf(jointViewArray[count]));
            jointViewArray[count].setText("0.00");
        }
    }

    /**
     * Make single force text views.
     */
    void makeSingleJointTextViews() {
        makeNotificationTextSwitcher();
        desiredJoint = (TextView) findViewById(R.id.desired_data);//attaches each view to its xml id
        desiredJointPos = (TextView) findViewById(R.id.desired_data_val);//attaches each view to its xml id
        desiredJointPos.setText("0.00");
    }

    void makeNotificationTextSwitcher(){
        footer = (TextSwitcher) findViewById(R.id.footer);
        footer.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                TextView t = new TextView(getApplicationContext());
                t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                return t;
            }
        });
    }
}
