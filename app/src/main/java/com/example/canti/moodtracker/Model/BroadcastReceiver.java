package com.example.canti.moodtracker.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.example.canti.moodtracker.R;
import com.example.canti.moodtracker.Utils.SharedPreferencesUtils;

import java.util.ArrayList;

import static com.example.canti.moodtracker.Controller.MainActivity.listMood;


public class BroadcastReceiver extends android.content.BroadcastReceiver {
    public ArrayList<Mood> historyListMood;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (SharedPreferencesUtils.containsMood(context)) {
            historyListMood.add(new Mood(SharedPreferencesUtils.getComment(context), ContextCompat.getColor(context, listMood.get(SharedPreferencesUtils.getMoodPosition(context)).getBackgroundColor()), SharedPreferencesUtils.getMoodPosition(context)));
        } else {

            historyListMood.add(new Mood("Humeur par défaut enregistrée", R.color.light_sage, 3));
        }

        SharedPreferencesUtils.saveArrayList(context, historyListMood);
        resetMood(context);

    }

    public void resetMood(Context context) {
        SharedPreferencesUtils.removeMood(context, SharedPreferencesUtils.MY_FILE, SharedPreferencesUtils.KEY_MOOD_POSITION);
        SharedPreferencesUtils.removeMood(context, SharedPreferencesUtils.MY_FILE, SharedPreferencesUtils.KEY_COMMENT);
    }

}

