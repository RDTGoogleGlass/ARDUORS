package com.RDTGoogleGlass.ARDUORS;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;


/**
 * Created by gglass on 6/14/16.
 */
class NetworkIssue extends Dialog {
//    private final DialogInterface.OnClickListener mOnClickListener;
    private final AudioManager mAudioManager;
    private final GestureDetector mGestureDetector;

    /**
     * Handles the tap gesture to call the dialog's
     * onClickListener if one is provided.
     */
    private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {

                @Override
                public boolean onGesture(Gesture gesture) {
                    if (gesture == Gesture.TAP) {
                        mAudioManager.playSoundEffect(Sounds.TAP);
                            // Since Glass dialogs do not have buttons,
                            // the index passed to onClick is always 0.
                            mOnClickListener.onClick(NetworkIssue.this, 0);
                        return true;
                    }
                    return false;
                }
            };

    /**
     * Instantiates a new No internet.
     *
     * @param context         the context
     * @param iconResId       the icon res id
     * @param textResId       the text res id
     * @param footnoteResId   the footnote res id
     */
    public NetworkIssue(Context context, int iconResId, int textResId, int footnoteResId) {
        super(context);

        mAudioManager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector =
                new GestureDetector(context).setBaseListener(mBaseListener);

        setContentView(new CardBuilder(context, CardBuilder.Layout.ALERT)
                .setIcon(iconResId)
                .setText(textResId)
                .setFootnote(footnoteResId)
                .getView());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps activity awake

    }

    /** Overridden to let the gesture detector handle a possible tap event. */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event)
                || super.onGenericMotionEvent(event);
    }

    private final DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int button) {
            // Open WiFi Settings
            getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ((Activity) App.getContext()).finish();
            this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
