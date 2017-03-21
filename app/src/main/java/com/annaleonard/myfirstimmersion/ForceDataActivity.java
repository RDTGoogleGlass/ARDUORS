package com.annaleonard.myfirstimmersion;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by rdtintern on 6/19/16.
 */
@SuppressWarnings("ALL")
public class ForceDataActivity extends DataActivity {




    /**TextViews and ids that are used to update the xml layout displayed on the glass*/
    /**Text Views for single forces view*/
    private final String[] FORCE_TITLES = {"X", "Y", "Z", "Roll", "Pitch", "Yaw"};
    private int lastX = 0;

    /**
     * The Force pos format.
     */
    private LineGraphSeries<DataPoint> series;

    /**
     * The Which force.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState){

        allValsLayout = R.layout.all_forces;
        dataMenu = R.menu.menu_forcedata;
        numData = 6;
        dataViewArray =  new TextView[6];
        viewIDs = new int[]{R.id.force_x_val, R.id.force_y_val, R.id.force_z_val, R.id.force_roll_val, R.id.force_pitch_val, R.id.force_yaw_val};    //xml locations of views for all forces view

        super.onCreate(savedInstanceState);

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
                makeAllDataTextViews();
                whichData = -1;
                App.setContext(this);
                return true;
            case (R.id.graph_single_force_option):
                //sets view to single force layout, but does not set views
                return true;

            //each option below individually sets the views in the single force view to display the name and data for that particular force.
            case (R.id.showForceX):

                whichData = 1;
                makeSingleDataTextViews();
                return true;

            case (R.id.showForceY):

                whichData = 2;
                makeSingleDataTextViews();
                return true;

            case (R.id.showForceZ):

                whichData = 3;
                makeSingleDataTextViews();
                return true;

            case (R.id.showForceRoll):

                whichData = 4;
                makeSingleDataTextViews();
                return true;

            case (R.id.showForcePitch):
                whichData = 5;
                makeSingleDataTextViews();
                return true;

            case (R.id.showForceYaw):
                whichData = 6;
                makeSingleDataTextViews();
                return true;
            

            default:
                return false;

        }
    }

    @Override
    void updateSingleDataUI() {
        try {
            series.appendData(new DataPoint(lastX++, mData.getDouble(8*(whichData-1))), true, 10);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public void makeSingleDataTextViews() {
        setContentView(R.layout.show_general_graph);
        super.makeSingleDataTextViews();
        setUpGraph();
        App.setContext(this);

    }



    private void setUpGraph() {
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
        graph.setTitle(FORCE_TITLES[whichData-1]);
        graph.setTitleTextSize(40);
    }



}
