package com.annaleonard.myfirstimmersion;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by rdtintern on 7/19/16.
 */
@SuppressWarnings("ALL")
public class JointDataActivity extends DataActivity {
    /**TextViews and ids that are used to update the xml layout displayed on the glass*/
    private final TextView[] DATA_VIEW_ARRAY = new TextView[7];
    /**Array containing text views for all joints view*/
    private TextView desiredJoint, desiredJointPos;
    /**Text Views for single joints view*/

    @Override
    protected void onCreate(Bundle savedInstanceState){

        allValsLayout = R.layout.all_joints;
        dataMenu = R.menu.menu_jointdata;
        numData = 7;
        viewIDs = new int[]{R.id.joint_a_val, R.id.joint_b_val, R.id.joint_c_val, R.id.joint_d_val, R.id.joint_e_val, R.id.joint_f_val, R.id.joint_g_val};    //xml locations of views for all joints view


        super.onCreate(savedInstanceState);

    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case (R.id.showAllJoints):
                setContentView(R.layout.all_joints);
                makeAllDataTextViews();
                whichData = -1;
                App.setContext(this);
                return true;
            case (R.id.single_joint_option):
                //sets view to single joint layout, but does not set views
                return true;

            //each option below individually sets the views in the single joint view to display the name and data for that particular joint.
            case (R.id.showJoint1):

                setContentView(R.layout.show_1_joint);
                makeSingleDataTextViews();

                whichData = 1;
                desiredJoint.setText("Joint 1");
                App.setContext(this);
                return true;

            case (R.id.showJoint2):

                setContentView(R.layout.show_1_joint);
                makeSingleDataTextViews();

                whichData = 2;
                desiredJoint.setText("Joint 2");
                App.setContext(this);
                return true;

            case (R.id.showJoint3):

                setContentView(R.layout.show_1_joint);
                makeSingleDataTextViews();

                whichData = 3;
                desiredJoint.setText("Joint 3");
                App.setContext(this);
                return true;

            case (R.id.showJoint4):

                setContentView(R.layout.show_1_joint);
                makeSingleDataTextViews();

                whichData = 4;
                desiredJoint.setText("Joint 4");
                App.setContext(this);
                return true;

            case (R.id.showJoint5):

                setContentView(R.layout.show_1_joint);
                makeSingleDataTextViews();

                whichData = 5;
                desiredJoint.setText("Joint 5");
                App.setContext(this);
                return true;

            case (R.id.showJoint6):

                setContentView(R.layout.show_1_joint);
                makeSingleDataTextViews();

                whichData = 6;
                desiredJoint.setText("Joint 6");
                App.setContext(this);
                return true;

            case (R.id.showJoint7):

                setContentView(R.layout.show_1_joint);
                makeSingleDataTextViews();

                whichData = 7;
                desiredJoint.setText("Joint 7");
                App.setContext(this);
                return true;

            default:
                return false;

        }
    }

    @Override
    void updateSingleDataUI() {
        desiredJointPos.setText(String.valueOf(DATA_POS_FORMAT.format(mData.getDouble((whichData-1)*8))));

    }


    @Override
    public void makeSingleDataTextViews(){
        super.makeSingleDataTextViews();
        desiredJoint = (TextView) findViewById(R.id.desired_joint);//attaches each view to its xml id
        desiredJointPos = (TextView) findViewById(R.id.desired_joint_pos);//attaches each view to its xml id
    }

}
