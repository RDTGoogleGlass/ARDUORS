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

/**
 * Created by rdtintern on 6/19/16.
 */
public class ForceDataActivity extends Activity {
    /**TextViews and ids that are used to update the xml layout displayed on the glass*/
    private TextView[] forceViewArray = new TextView[6];
    /**Array containing text views for all forces view*/
    private TextView desiredForce, desiredForcePos;
    private TextSwitcher footer;
    /**Text Views for single forces view*/
    private String[] forceTitles = {"X", "Y", "Z", "Roll", "Pitch", "Yaw"};
    private int[] viewId = {R.id.force_x_val, R.id.force_y_val, R.id.force_z_val, R.id.force_roll_val, R.id.force_pitch_val, R.id.force_yaw_val};    //xml locations of views for all forces view
    private int lastX = 0;

    /**
     * The Force pos format.
     */
    DecimalFormat forcePosFormat = new DecimalFormat("0.00");   //format to specify sig figs

    SynchronizedData forceSyncData = new SynchronizedData();

    private LineGraphSeries<DataPoint> series;

    ByteBuffer forceData;

    /**
     * The Force double array.
     */
    double[] forceDataArray = new double[6];

    /**
     * The Which force.
     */
    int whichForce = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps activity awake

        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);//Enable voice activated menu
        setContentView(R.layout.all_forces);  //Set the desired layout to display on screen

        //Set up 6 text views for all force view.  One for each force.
        makeAllForceTextViews();
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
                setContentView(R.layout.all_forces);
                makeAllForceTextViews();
                whichForce = -1;
                App.setContext(this);
                return true;
            case (R.id.graph_single_force_option):
                //sets view to single force layout, but does not set views
                return true;

            //each option below individually sets the views in the single force view to display the name and data for that particular force.
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
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    forceViewArray[i].setText(String.valueOf(forcePosFormat.format(forceData.getDouble(i*8))));
                }
            }
        }

    });

    /**
     * Make all force text views.
     */
    public void makeAllForceTextViews() {
        makeNotificationTextSwitcher();
        for (int count = 0; count < 6; count++) {
            final int i = count;
            forceViewArray[count] = (TextView) findViewById(viewId[count]);
            Log.i("View " + String.valueOf(count), String.valueOf(forceViewArray[count]));
            forceViewArray[count].setText("0.00");
        }
    }

    /**
     * Make single force text views.
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
