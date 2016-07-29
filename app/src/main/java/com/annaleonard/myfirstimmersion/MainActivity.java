package com.annaleonard.myfirstimmersion;

import com.google.android.glass.media.Sounds;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



/**
 * This {@link Activity} contains the splash screen that the user will see on entering the app.
 <p/>Navigate to the JointDataActivity, the ForceDataActivity or the InformationActivity from here.
 */
public class MainActivity extends Activity {

    Thread mThread;
    NetworkRunnable networkRunnable = new NetworkRunnable();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        setContentView(R.layout.main);
        App.setContext(this);
        mThread = new Thread(networkRunnable);
        mThread.start();


    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy()  {
        super.onDestroy();
        Log.i("MainActivity", "onDestroy called");
        networkRunnable.setCollectData(false);
        Log.i("MainActivity", "Flag set");
        try {
            Log.i("MainActivity, OnDestroy", "Try entered");
            mThread.join();
            Log.i("MainActivity", "Thread Killed");
        }catch (Exception e){
            e.printStackTrace();
            Log.i("MainActivity", "Thread failed to die");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case (R.id.joint_menu_item):
                startActivity(new Intent(this, JointDataActivity.class));
                return true;

            case (R.id.force_menu_item):
                startActivity(new Intent(this, ForceDataActivity.class));
                return true;

            case (R.id.information_menu_item):
                startActivity(new Intent(this, InformationActivity.class));
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
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

}
