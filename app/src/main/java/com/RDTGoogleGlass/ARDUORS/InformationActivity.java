package com.RDTGoogleGlass.ARDUORS;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Information activity.
 */
public class InformationActivity extends Activity {

    private List<CardBuilder> mCards;   //a list to store cards made with CardBuilder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createCards();

        publishCards();


    }

    private void createCards(){
        //create new instance of array of cards
        mCards = new ArrayList<CardBuilder>();
        //get data for cards to display from the background thread

        //add cards to array
        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT).setText("Tap to open a menu. Swipe forward and backward to navigate between menu items. Tap on a menu item to select.  Swipe down to dismiss a menu item or page."));
//        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT).setText("If you are confused, refer to the tutorial google_glass_tutorial. Ask Nick Scott where to find Glass documents."));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT).setText("This application was created in January 2015 by Anna Leonard. Improvements were made during Summer 2015 with the help of Julianna Long.  Version 2.0 was completed in Summer 2016 by Julianna Long and Sreya Vangara"));
    }

    private void publishCards(){
        CardScrollView mCardScrollView = new CardScrollView(this);
        CardScrollAdapter mCardScrollAdapter = new CardScrollAdapter() {
            @Override
            public int getCount() {
                return mCards.size();
            }

            @Override
            public Object getItem(int position) {
                return mCards.get(position);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return mCards.get(position).getView(convertView, parent);
            }

            @Override
            public int getPosition(Object item) {
                return mCards.indexOf(item);
            }

            @Override
            public int getItemViewType(int position) {
                return mCards.get(position).getItemViewType();
            }

            @Override
            public int getViewTypeCount() {
                return CardBuilder.getViewTypeCount();
            }
        };
        mCardScrollView.setAdapter(mCardScrollAdapter);
        // Handle the TAP event.
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Plays TAP sound to indicate that TAP actions are now supported.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.DISALLOWED);
            }
        });
        mCardScrollView.activate();
        setContentView(mCardScrollView);
    }

}
