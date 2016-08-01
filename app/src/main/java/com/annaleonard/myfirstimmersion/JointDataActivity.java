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
@SuppressWarnings("ALL")
public class JointDataActivity extends Activity {
    /**TextViews and ids that are used to update the xml layout displayed on the glass*/
    private final TextView[] JOINT_VIEW_ARRAY = new TextView[7];
    /**Array containing text views for all joints view*/
    private TextView desiredJoint, desiredJointPos;
    /**Text Views for single joints view*/
    private final int[] VIEW_ID = {R.id.joint_a_val, R.id.joint_b_val, R.id.joint_c_val, R.id.joint_d_val, R.id.joint_e_val, R.id.joint_f_val, R.id.joint_g_val};    //xml locations of views for all joints view

    /**
     * The Joint pos format.
     */
    private final DecimalFormat JOINT_POS_FORMAT = new DecimalFormat("0.00");   //format to specify sig figs

    private final SynchronizedData JOINT_SYNC_DATA = new SynchronizedData();


    /**
     * The Which joint.
     */
    private int whichJoint = -1;


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
        UPDATE_JOINT_VALS.startUpdates();


    }


    @Override
    protected void onStop(){
        super.onStop();
        UPDATE_JOINT_VALS.stopUpdates();
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
    private final UIUpdater UPDATE_JOINT_VALS = new UIUpdater(new Runnable() {
        @Override
        public void run() {
            // do stuff ...
            ByteBuffer jointData = JOINT_SYNC_DATA.getPacketData();
            if (whichJoint > 0) {
                desiredJointPos.setText(String.valueOf(JOINT_POS_FORMAT.format(jointData.getDouble((whichJoint-1)*8))));
            } else {
                for (int i = 0; i < 7; i++) {
                    JOINT_VIEW_ARRAY[i].setText(String.valueOf(JOINT_POS_FORMAT.format(jointData.getDouble(i*8))));
                }
            }
        }

    });

    /**
     * Make all joint text views.
     */
    private void makeAllJointTextViews() {
        makeNotificationTextSwitcher();
        for (int count = 0; count < 7; count++) {
            JOINT_VIEW_ARRAY[count] = (TextView) findViewById(VIEW_ID[count]);
            Log.i("View " + String.valueOf(count), String.valueOf(JOINT_VIEW_ARRAY[count]));
        }
    }

    /**
     * Make single force text views.
     */
    private void makeSingleJointTextViews() {
        makeNotificationTextSwitcher();
        desiredJoint = (TextView) findViewById(R.id.desired_joint);//attaches each view to its xml id
        desiredJointPos = (TextView) findViewById(R.id.desired_joint_pos);//attaches each view to its xml id);
    }

    private void makeNotificationTextSwitcher(){
        TextSwitcher footer = (TextSwitcher) findViewById(R.id.footer);
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
