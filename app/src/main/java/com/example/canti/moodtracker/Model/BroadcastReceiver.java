package com.example.canti.moodtracker.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.canti.moodtracker.R;
import com.example.canti.moodtracker.Utils.SharedPreferencesUtils;

import java.util.ArrayList;

import static com.example.canti.moodtracker.Controller.MainActivity.listMood;


public class BroadcastReceiver extends android.content.BroadcastReceiver {
    public ArrayList<Mood> historyListMood = new ArrayList<>();

    /**
     * When this method is called, add a new mood in an array historyListMood
     * the new mood take as parameters the comment, the background and the position previously saved in SharedPreferencesUtils
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast toast = Toast.makeText(context,"onReceive 1",Toast.LENGTH_LONG);
        toast.show();

        if (SharedPreferencesUtils.containsMood(context)) {
            historyListMood.add(new Mood(SharedPreferencesUtils.getComment(context), ContextCompat.getColor(context, listMood.get(SharedPreferencesUtils.getMoodPosition(context)).getBackgroundColor()), SharedPreferencesUtils.getMoodPosition(context)));
        } else {
            historyListMood.add(new Mood("", R.color.light_sage, 3));
        }

        SharedPreferencesUtils.saveArrayList(context, historyListMood);
        resetMood(context);
        removeMood();

       Toast toast1 = Toast.makeText(context,"onReceive 2",Toast.LENGTH_LONG);
       toast1.show();

    }

    /**
     * When the new mood is save in the array historyListMood, remove the preferences for a new day
     * @param context
     */

    public void resetMood(Context context) {
        SharedPreferencesUtils.removeMood(context, SharedPreferencesUtils.MY_FILE, SharedPreferencesUtils.KEY_MOOD_POSITION);
        SharedPreferencesUtils.removeMood(context, SharedPreferencesUtils.MY_FILE, SharedPreferencesUtils.KEY_COMMENT);
    }

    /**
     * When the size of the array equals 8 remove the mood of index 0
     */
    public void removeMood() {
        if (historyListMood.size() == 8) {
            historyListMood.remove(0);
        }
    }

}

