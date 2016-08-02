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
 * Created by $jlong16 on 8/1/16.
 */

public abstract class DataActivity extends Activity {

    int allValsLayout, dataMenu, numData;
    /**TextViews and ids that are used to update the xml layout displayed on the glass*/
    TextView[] dataViewArray;
    /**Array containing text views for all data view*/
    int[] viewIDs;    //xml locations of views for all data view
    /**
     * The Data pos format.
     */
    final DecimalFormat DATA_POS_FORMAT = new DecimalFormat("0.00");   //format to specify sig figs
    static ByteBuffer mData;
    private final SynchronizedData SYNC_DATA = new SynchronizedData();
    /**
     * The Which data.
     */
    int whichData = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps activity awake

        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);//Enable voice activated menu
        setContentView(allValsLayout);  //Set the desired layout to display on screen

        //Set up numData text views for all data view.  One for each data.
        makeAllDataTextViews();
        App.setContext(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        UPDATE_DATA_VALS.startUpdates();


    }


    @Override
    protected void onStop(){
        super.onStop();
        UPDATE_DATA_VALS.stopUpdates();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(dataMenu, menu);
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
    public abstract boolean onOptionsItemSelected(MenuItem item);

    /**
     * The M update data vals.
     */
    private final UIUpdater UPDATE_DATA_VALS = new UIUpdater(new Runnable() {
        @Override
        public void run() {
            // do stuff ...
            mData = SYNC_DATA.getPacketData();
            if (whichData > 0) {
                updateSingleDataUI();
            } else {
                for (int i = 0; i < numData; i++) {
                    dataViewArray[i].setText(String.valueOf(DATA_POS_FORMAT.format(mData.getDouble(i*8))));
                }
            }
        }

    });

    abstract void updateSingleDataUI();

    /**
     * Make all data text views.
     */
    void makeAllDataTextViews() {
        makeNotificationTextSwitcher();
        dataViewArray=new TextView[numData];
        for (int count = 0; count < numData; count++) {
            Log.i("DataActivity", String.valueOf(count) + "/" + String.valueOf(numData));
            dataViewArray[count] = (TextView) findViewById(viewIDs[count]);
            Log.i("View " + String.valueOf(count), String.valueOf(dataViewArray[count]));
        }
    }

    /**
     * Make single force text views.
     */
    void makeSingleDataTextViews(){
        makeNotificationTextSwitcher();
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
