package com.example.canti.moodtracker.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.canti.moodtracker.Model.Mood;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class SharedPreferencesUtils {

    public static final String MY_FILE = "MySharedPreference.xml";
    public static final String KEY_COMMENT = "KEY_COMMENT";
    public static final String KEY_MOOD_POSITION = "KEY_MOOD_POSITION";
    private static final String KEY_LIST = "KEY_LIST";


    public static void saveComment(Context context, String message) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_COMMENT, message);
        editor.apply();
    }

    public static String getComment(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_COMMENT, "");
    }

    public static boolean containsComment(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.contains(KEY_COMMENT);
    }


    public static void saveMoodPosition(Context context, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_MOOD_POSITION, position);
        editor.apply();
    }

    public static int getMoodPosition(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_MOOD_POSITION, 0);
    }

    public static void removeMood(Context context, String prefsName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean containsMood(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.contains(KEY_MOOD_POSITION);
    }

    public static void saveArrayList(Context context,ArrayList historyListMood) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(historyListMood);
        editor.putString(KEY_LIST, json);
        editor.apply();
    }

    public static ArrayList<Mood> getArrayList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_LIST, null);
        Type type = new TypeToken<ArrayList<Mood>>() {
        }.getType();
        ArrayList<Mood> historyListM = gson.fromJson(json, type);
        return historyListM;
    }

    public static boolean containsArrayList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.contains(KEY_LIST);
    }




}
