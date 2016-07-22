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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;

import static com.annaleonard.myfirstimmersion.R.id.force_pitch_val;
import static com.annaleonard.myfirstimmersion.R.id.force_roll_val;
import static com.annaleonard.myfirstimmersion.R.id.force_x_val;
import static com.annaleonard.myfirstimmersion.R.id.force_y_val;
import static com.annaleonard.myfirstimmersion.R.id.force_yaw_val;
import static com.annaleonard.myfirstimmersion.R.id.force_z_val;

/**
 * Created by rdtintern on 7/19/16.
 */
public class ForceDataActivity extends Activity{
    /**TextSwitchers and ids that are used to update the xml layout displayed on the glass*/
    private TextSwitcher[] forceSwitcherArray = new TextSwitcher[6];
    /**Array containing text switchers for all forces view*/
    private TextSwitcher footer;
    /**Text Switchers for single forces view*/

    private int[] switcherId = new int[]{R.id.force_x_val, R.id.force_y_val, R.id.force_z_val, R.id.force_roll_val, R.id.force_pitch_val, R.id.force_yaw_val};    //xml locations of switchers for all forces view

    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    private String[] forceTitles = {"X", "Y", "Z", "Roll", "Pitch", "Yaw"};

    SynchronizedData forceSyncData = new SynchronizedData();

    DecimalFormat forceValformat = new DecimalFormat("0.00");   //format to specify sig figs


    ByteBuffer forceData;

    /**
     * The Which force.
     */
    int whichForce = -1;
    /**
     * The M force data.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps activity awake

        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);//Enable voice activated menu
        setContentView(R.layout.all_forces);  //Set the desired layout to display on screen

        //Set up 7 text switchers for all forces view.  One for each force.
        makeAllForceTextSwitchers();
        App.setContext(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mUpdateForceVals.startUpdates();

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
        mUpdateForceVals.stopUpdates();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_forcedata, menu);
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
            case (R.id.showAllForces):
                setContentView(R.layout.all_joints);
                makeAllForceTextSwitchers();
                App.setContext(this);
                whichForce = -1;
                return true;
            case (R.id.graph_single_force_option):
                //sets view to single force layout, but does not set switchers
                return true;

            //each option below individually sets the switchers in the single force view to display the name and data for that particular force.
            case (R.id.showForceX):
                whichForce = 1;

                setContentView(R.layout.show_general_graph);
                setUpGraph();
                App.setContext(this);
                return true;

            case (R.id.showForceY):
                whichForce = 2;

                setContentView(R.layout.show_general_graph);
                setUpGraph();

                App.setContext(this);
                return true;

            case (R.id.showForceZ):
                whichForce = 3;

                setContentView(R.layout.show_general_graph);
                setUpGraph();

                App.setContext(this);
                return true;

            case (R.id.showForceRoll):
                whichForce = 4;

                setContentView(R.layout.show_general_graph);
                setUpGraph();

                App.setContext(this);
                return true;

            case (R.id.showForcePitch):
                whichForce = 5;

                setContentView(R.layout.show_general_graph);
                setUpGraph();

                App.setContext(this);
                return true;

            case (R.id.showForceYaw):
                whichForce = 6;

                setContentView(R.layout.show_general_graph);
                setUpGraph();

                App.setContext(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }

    /**
     * The M update force vals.
     */
    UIUpdater mUpdateForceVals = new UIUpdater(new Runnable() {
        @Override
        public void run() {
            // do stuff ...
            forceData = forceSyncData.getPacketData();
            if (whichForce > 0) {
                try {
                    series.appendData(new DataPoint(lastX++, forceData.getDouble(8*(whichForce-1))), true, 10);
                }
                catch (NullPointerException e)
                {
                    Log.i("ForceDataActivity", "data not appended");
                    e.printStackTrace();
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    forceSwitcherArray[i].setText(String.valueOf(forceValformat.format(forceData.getDouble(i*8))));

                }
            }
        }

    });


    public void makeAllForceTextSwitchers() {
        for (int count = 0; count < 6; count++) {
            forceSwitcherArray[count] = (TextSwitcher) findViewById(switcherId[count]);
            Log.i("makeSwitcherCount", String.valueOf(forceSwitcherArray[count]!=null));
            forceSwitcherArray[count].setFactory(new ViewSwitcher.ViewFactory() {
                public View makeView() {
                    TextView tv = new TextView(ForceDataActivity.this);
                    tv.setTextSize(30);
                    return tv;
                }
            });
            forceSwitcherArray[count].setText("0.00");
        }
        makeNotificationTextSwitcher();
    }

    /**
     * Make single joint text switchers.
     */
    void setUpGraph() {
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
        graph.setTitle(forceTitles[whichForce-1]);
        graph.setTitleTextSize(40);
        makeNotificationTextSwitcher();
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
