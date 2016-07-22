package com.annaleonard.myfirstimmersion;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.NoRouteToHostException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


/**
 * This {@link Activity} contains the splash screen that the user will see on entering the app.
 <p/>Navigate to the MeasuringActivity or the InformationActivity from here.
 */
public class MainActivity extends Activity {


    Thread mThread;
    NetworkRunnable networkRunnable = new NetworkRunnable();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Log.i("MainAct super.onCreate", " ");

        setContentView(R.layout.main);
        App.setContext(this);
        mThread = new Thread(networkRunnable);
        mThread.start();
        Log.i("Thread started", "tada!");


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy()  {
        super.onDestroy();
        networkRunnable.setCollectData(false);
        try {
            mThread.join();
        }catch (Exception e){
            e.printStackTrace();
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
