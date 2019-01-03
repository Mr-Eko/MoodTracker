package com.example.canti.moodtracker.Controller;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.canti.moodtracker.Model.Mood;
import com.example.canti.moodtracker.R;
import com.example.canti.moodtracker.Utils.SharedPreferencesUtils;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RelativeLayout mRelativeLayout1WA;
    private RelativeLayout mRelativeLayout6DA;
    private RelativeLayout mRelativeLayout5DA;
    private RelativeLayout mRelativeLayout4DA;
    private RelativeLayout mRelativeLayout3DA;
    private RelativeLayout mRelativeLayout2DA;
    private RelativeLayout mRelativeLayoutY;

    private ImageView mImageView1WA;
    private ImageView mImageView6DA;
    private ImageView mImageView5DA;
    private ImageView mImageView4DA;
    private ImageView mImageView3DA;
    private ImageView mImageView2DA;
    private ImageView mImageViewY;


    public ArrayList<Mood> historicList = new ArrayList<>();
    public ArrayList<RelativeLayout> layoutsList = new ArrayList<>();
    public ArrayList<ImageView> imageList = new ArrayList<>();
    public ArrayList<Mood> historyListMood = new ArrayList<>();


    public String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initImages();
        initLayouts();
        initHistoryMoods();

        if (SharedPreferencesUtils.containsArrayList(this)) {
            historicList = SharedPreferencesUtils.getArrayList(this);
            setLayoutsFromMood();
            displayCommentary();
            setWidth();
        }

    }


    /**
     * Init layouts by ID and add them in an array
     */
    private void initLayouts() {
        mRelativeLayout1WA = findViewById(R.id.history_layout_one_week_ago);
        mRelativeLayout6DA = findViewById(R.id.history_layout_six_days_ago);
        mRelativeLayout5DA = findViewById(R.id.history_layout_five_days_ago);
        mRelativeLayout4DA = findViewById(R.id.history_layout_four_days_ago);
        mRelativeLayout3DA = findViewById(R.id.history_layout_three_days_ago);
        mRelativeLayout2DA = findViewById(R.id.history_layout_two_days_ago);
        mRelativeLayoutY = findViewById(R.id.history_layout_yesterday);

        layoutsList.add(mRelativeLayoutY);
        layoutsList.add(mRelativeLayout2DA);
        layoutsList.add(mRelativeLayout3DA);
        layoutsList.add(mRelativeLayout4DA);
        layoutsList.add(mRelativeLayout5DA);
        layoutsList.add(mRelativeLayout6DA);
        layoutsList.add(mRelativeLayout1WA);

    }

    /**
     * Init images by ID and add them in an array
     */
    private void initImages() {
        mImageView1WA = findViewById(R.id.comment_OWA);
        mImageView6DA = findViewById(R.id.comment_btn_6DA);
        mImageView5DA = findViewById(R.id.comment_btn_5DA);
        mImageView4DA = findViewById(R.id.comment_btn_4DA);
        mImageView3DA = findViewById(R.id.comment_btn_3DA);
        mImageView2DA = findViewById(R.id.comment_btn_2DA);
        mImageViewY = findViewById(R.id.comment_btn_1DA);

        imageList.add(mImageViewY);
        imageList.add(mImageView2DA);
        imageList.add(mImageView3DA);
        imageList.add(mImageView4DA);
        imageList.add(mImageView5DA);
        imageList.add(mImageView6DA);
        imageList.add(mImageView1WA);

    }

    private void initHistoryMoods() {

        Mood superHappyMood = new Mood(R.color.banana_yellow);
        Mood happyMood = new Mood(R.color.light_sage);
        Mood normalMood = new Mood(R.color.cornflower_blue_65);
        Mood disappointedMood = new Mood(R.color.warm_grey);
        Mood sadMood = new Mood(R.color.faded_red);

        historyListMood.add(sadMood);
        historyListMood.add(disappointedMood);
        historyListMood.add(normalMood);
        historyListMood.add(happyMood);
        historyListMood.add(superHappyMood);

    }

    /**
     * Get the backgrounds color previously saved in historicList and set them to the layouts depending on the size of historicList
     * Set the layouts to be visible depending on the size of historicList
     */
    private void setLayoutsFromMood() {

        for (int i = 0; i < historicList.size(); i++) {
            RelativeLayout layout = layoutsList.get(i);
            int mPosition = historicList.get(i).getPosition();
            layout.setBackgroundColor(ContextCompat.getColor(this, historyListMood.get(mPosition).getBackgroundColor()));
            layout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Get width
     * @return The absolute height of the available display size in pixels
     */
    private int getWidthScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;

    }

    /**
     * Set width of the layouts depending of the size of historicList
     */
    private void setWidth() {

        for (int i = 0; i < historicList.size(); i++) {
            RelativeLayout layout = layoutsList.get(i);

            ViewGroup.LayoutParams params = layout.getLayoutParams();
            int position = historicList.get(i).position;
            WidthScreen(position, params);
        }
    }

    /**
     * Set width of the layouts depending on the mood position
     * @param position position of the mood
     * @param params params of the layouts
     */
    private void WidthScreen(int position, ViewGroup.LayoutParams params) {

        int widthScreen = getWidthScreen();

        switch (position) {

            case 0:
                params.width = widthScreen / 5;
                break;

            case 1:
                params.width = (2 * widthScreen) / 5;
                break;
            case 2:
                params.width = (3 * widthScreen) / 5;
                break;
            case 3:
                params.width = (4 * widthScreen) / 5;
                break;
            case 4:
                params.width = widthScreen;
                break;
        }
    }

    /**
     * Get the comments previously saved in historicList
     * Display the comments by clicking on the comment imageView
     * If there is no comment, leave the comment button invisible
     */
    private void displayCommentary() {


        for (int i = 0; i < historicList.size(); i++) {
            ImageView commentView = imageList.get(i);

            comment = historicList.get(i).comment;

            if (!comment.isEmpty() ){
                commentView.setVisibility(View.VISIBLE);
                commentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(getApplicationContext(), comment, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        }

    }
}


