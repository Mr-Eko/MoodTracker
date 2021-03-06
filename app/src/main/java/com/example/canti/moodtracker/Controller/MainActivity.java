package com.example.canti.moodtracker.Controller;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.canti.moodtracker.Model.BroadcastReceiver;
import com.example.canti.moodtracker.Model.Mood;
import com.example.canti.moodtracker.R;
import com.example.canti.moodtracker.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private ImageView mCommentButton;
    private ImageView mHistoryButton;
    private ImageView mSmiley;

    public int mPosition;

    private RelativeLayout mBackgroundLayout;

    private GestureDetector mGestureDetector;

    public ArrayList<Mood> listMood = new ArrayList<>();

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
        setAlarmMidnight();

        mGestureDetector = new GestureDetector(this, new GestureListener());
        mBackgroundLayout.setOnTouchListener(this);

    }

    /**
     * Init buttons by ID
     */

    private void initButtons() {
        mCommentButton = findViewById(R.id.comment_btn);
        mHistoryButton = findViewById(R.id.history_btn);
        mSmiley = findViewById(R.id.main_smiley);
        mBackgroundLayout = findViewById(R.id.main_layout);
    }

    /**
     * Init moods by Mood class and add them in an array
     */
    private void initMoods() {
        Mood superHappyMood = new Mood(R.drawable.smiley_super_happy, R.color.banana_yellow, 4,R.raw.superhappy);
        Mood happyMood = new Mood(R.drawable.smiley_happy, R.color.light_sage, 3,R.raw.happy);
        Mood normalMood = new Mood(R.drawable.smiley_normal, R.color.cornflower_blue_65, 2,R.raw.neutre);
        Mood disappointedMood = new Mood(R.drawable.smiley_disappointed, R.color.warm_grey, 1,R.raw.disappointed);
        Mood sadMood = new Mood(R.drawable.smiley_sad, R.color.faded_red, 0,R.raw.sad);

        listMood.add(sadMood);
        listMood.add(disappointedMood);
        listMood.add(normalMood);
        listMood.add(happyMood);
        listMood.add(superHappyMood);
    }


    /**
     * Get images and backgrounds by listMood
     * @param position put the rights images and backgrounds depending of the position in the array ListMood
     */

    private void setScreenFromMood(int position) {
        mSmiley.setImageResource(listMood.get(position).getSmileyResource());
        mBackgroundLayout.setBackgroundColor(ContextCompat.getColor(this, listMood.get(position).getBackgroundColor()));
        MediaPlayer sound = MediaPlayer.create(MainActivity.this,listMood.get(position).getSound());
        sound.start();
    }

    /**
     * Restrain moods between the first one and the last one
     */
    private void changeMood() {

        if (mPosition >= listMood.size()) {
            mPosition = listMood.size() -1;
        } else if (mPosition < 0) {
            mPosition = 0;
        }
    }


    /**
     * Configure the comment button and save the comment in preferences
     */

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
                            Toast.makeText(MainActivity.this, "Commentaire enregistré !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Nouveau Commentaire enregistré !", Toast.LENGTH_SHORT).show();
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

    /**
     * Configure the history button to intent HistoryActivity
     */

    private void configureHistoryBtn(){
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivity);
            }
        });
    }

    /**
     * Execute code contain in BroadcastReceiver at 23h 59min 59sec
     */
    private void setAlarmMidnight() {

        //CREATION OF A CALENDAR to get time in millis and pass it to the AlarmManager to set
        //the time when the alarm has to start working (same day the app runs for the first time
        //at midnight).
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        //DECLARATION OF the AlarmManager and
        // the Intent and PendingIntent necessary for the AlarmManager.setRepeating method.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //timeInMillis: specifies when we have to start the alarm (calendar gives this information).
        //INTERVAL_DAY: makes the alarm be repeated every day.
        if (alarmManager != null) {

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

            }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    /**
     * Class GestureListener who permit to change the mood position depending on the swipe action of the user
     */
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

        private void onSwipeTop() {
            --mPosition;
            setScreenFromMood(mPosition);
            changeMood();


        }

        private void onSwipeBottom() {
            ++mPosition;
            setScreenFromMood(mPosition);
            changeMood();


        }

    }

    /**
     * Save mood position when the app close
     */

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

