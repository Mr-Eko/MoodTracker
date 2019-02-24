package com.example.canti.moodtracker.Model;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.example.canti.moodtracker.Utils.SharedPreferencesUtils;

import java.util.ArrayList;


public class BroadcastReceiver extends android.content.BroadcastReceiver {
    public ArrayList<Mood> historyListMood;

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

        historyListMood = SharedPreferencesUtils.getArrayList(context);
        if (historyListMood == null) {
            historyListMood = new ArrayList<>();
        }

            if (SharedPreferencesUtils.containsMood(context)) {
                historyListMood.add(new Mood(SharedPreferencesUtils.getComment(context), SharedPreferencesUtils.getMoodPosition(context)));
            } else {
                historyListMood.add(new Mood("Humeur par défaut", 3));
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

