package com.annaleonard.myfirstimmersion;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.view.MotionEvent;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import android.util.Log;


/**
 * Created by gglass on 6/14/16.
 */
public class NoInternet extends Dialog {
    private final DialogInterface.OnClickListener mOnClickListener;
    private final AudioManager mAudioManager;
    private final GestureDetector mGestureDetector;

    /**
     * Handles the tap gesture to call the dialog's
     * onClickListener if one is provided.
     */
    private final GestureDetector.BaseListener mBaseListener =
            new GestureDetector.BaseListener() {

                @Override
                public boolean onGesture(Gesture gesture) {
                    if (gesture == Gesture.TAP) {
                        mAudioManager.playSoundEffect(Sounds.TAP);
                        if (mOnClickListener != null) {
                            // Since Glass dialogs do not have buttons,
                            // the index passed to onClick is always 0.
                            mOnClickListener.onClick(NoInternet.this, 0);
                        }
                        return true;
                    }
                    return false;
                }
            };

    public NoInternet(Context context, int iconResId,
                       int textResId, int footnoteResId,
                       DialogInterface.OnClickListener onClickListener) {

        super(context);
        Log.i("method called", "to super");
        mOnClickListener = onClickListener;
        mAudioManager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector =
                new GestureDetector(context).setBaseListener(mBaseListener);
        Log.i("click list,am,gesture", "setup");

        setContentView(new CardBuilder(context, CardBuilder.Layout.ALERT)
                .setIcon(iconResId)
                .setText(textResId)
                .setFootnote(footnoteResId)
                .getView());
        Log.i("contenet view", "set");

    }

    /** Overridden to let the gesture detector handle a possible tap event. */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event)
                || super.onGenericMotionEvent(event);
    }
}
