package com.example.canti.moodtracker.Controller;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.canti.moodtracker.Model.BroadcastReceiver;
import com.example.canti.moodtracker.Model.Mood;
import com.example.canti.moodtracker.R;
import com.example.canti.moodtracker.Utils.SharedPreferencesUtils;


import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private ImageView mCommentButton;
    private ImageView mHistoryButton;
    private ImageView mSmiley;

    public MediaPlayer mediaPlayer;

    public int mPosition;

    private LinearLayout mBackgroundLayout;

    private GestureDetector mGestureDetector;

    public static ArrayList<Mood> listMood = new ArrayList<>();
    public  ArrayList<Mood> historyListMood = new ArrayList<>();

    //Erreur Lint ?

    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPreferencesUtils.containsMood(this)){
            mPosition = SharedPreferencesUtils.getMoodPosition(this);
        } else {
            mPosition = 3;
        }

        initButtons();
        initMoods();
        configureCommentBtn();
        configureHistoryBtn();
        changeMood();
        setScreenFromMood(mPosition);
        setAlarmMidnight(this);
        removeMood();


        mGestureDetector = new GestureDetector(this, new GestureListener());
        mBackgroundLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });


    }



    //Initialisation boutons par id
    private void initButtons() {
        mCommentButton = findViewById(R.id.comment_btn);
        mHistoryButton = findViewById(R.id.history_btn);
        mSmiley = findViewById(R.id.main_smiley);
        mBackgroundLayout = findViewById(R.id.main_layout);
    }

    //Initialisations humeurs via la classe mood et ajout dans un Array ListMood
    private void initMoods() {
        Mood superHappyMood = new Mood(R.drawable.smiley_super_happy, R.color.banana_yellow, 4,R.raw.test);
        Mood happyMood = new Mood(R.drawable.smiley_happy, R.color.light_sage, 3,R.raw.test);
        Mood normalMood = new Mood(R.drawable.smiley_normal, R.color.cornflower_blue_65, 2,R.raw.test);
        Mood disappointedMood = new Mood(R.drawable.smiley_disappointed, R.color.warm_grey, 1,R.raw.test);
        Mood sadMood = new Mood(R.drawable.smiley_sad, R.color.faded_red, 0,R.raw.test);

        listMood.add(sadMood);
        listMood.add(disappointedMood);
        listMood.add(normalMood);
        listMood.add(happyMood);
        listMood.add(superHappyMood);
    }


    //Méthode permettant de récuperer les images et les background via listMood et de les placer
    private void setScreenFromMood(int position) {
        mSmiley.setImageResource(listMood.get(position).getSmileyResource());
        mBackgroundLayout.setBackgroundColor(ContextCompat.getColor(this, listMood.get(position).getBackgroundColor()));
        mPosition = position;
    }

    //Méthode permettant de restreindre le swipe entre la premiere et la derniere humeur
    private void changeMood() {

        if (mPosition >= listMood.size()) {
            mPosition = listMood.size() -1;
        } else if (mPosition < 0) {
            mPosition = 0;
        }
    }


    //Configuration du bouton de commentaire
    private void configureCommentBtn() {
        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Commentaire");
                final EditText comment = new EditText(MainActivity.this);
                builder.setView(comment);
                builder.setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesUtils.saveComment(MainActivity.this, comment.getText().toString());
                        if (SharedPreferencesUtils.containsComment(getBaseContext())) {
                            Toast.makeText(MainActivity.this, "Nouveau Commentaire enregistré !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Commentaire enregistré !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    //Configuration du bouton renvoyant sur HistoryActivity
    private void configureHistoryBtn(){
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyactivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyactivity);
            }
        });
    }

    private void setAlarmMidnight(Context context) {

            Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(calendar.DAY_OF_MONTH);
            SharedPreferences settings = getSharedPreferences("PREFS", 0);
            int lastDay = settings.getInt("day",0);

        if (lastDay != currentDay){
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", currentDay);
            editor.commit();

            if (SharedPreferencesUtils.containsMood(context)) {
                historyListMood.add(new Mood(SharedPreferencesUtils.getComment(this), ContextCompat.getColor(this, listMood.get(SharedPreferencesUtils.getMoodPosition(this)).getBackgroundColor()), SharedPreferencesUtils.getMoodPosition(context)));
            } else {
                historyListMood.add(new Mood("Humeur par défaut enregistrée", R.color.light_sage, 3));
            }
        }

            SharedPreferencesUtils.saveArrayList(this, historyListMood);
            resetMood(context);

            }


    public void resetMood(Context context) {
        SharedPreferencesUtils.removeMood(context, SharedPreferencesUtils.MY_FILE, SharedPreferencesUtils.KEY_MOOD_POSITION);
        SharedPreferencesUtils.removeMood(context, SharedPreferencesUtils.MY_FILE, SharedPreferencesUtils.KEY_COMMENT);
    }

    public void removeMood() {
        if (historyListMood.size() == 8) {
            historyListMood.remove(0);
        }
    }



    //Class GestureListener permettant de recuperer l'action de l'utilisateur
    public final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 20;

        @Override
        public boolean onDown (MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffY) > SWIPE_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        public void onSwipeTop() {
            --mPosition;
            setScreenFromMood(mPosition);
            changeMood();

        }

        public void onSwipeBottom() {
            ++mPosition;
            setScreenFromMood(mPosition);
            changeMood();

        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferencesUtils.saveMoodPosition(MainActivity.this, mPosition);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.saveMoodPosition(MainActivity.this, mPosition);

    }
}

