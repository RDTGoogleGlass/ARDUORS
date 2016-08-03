package com.RDTGoogleGlass.ARDUORS;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;

/**
 * Created by rdtintern on 7/22/16.
 */
@SuppressWarnings("ALL")
class App extends Application implements TextToSpeech.OnInitListener {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onInit(int i) {

    }
}
