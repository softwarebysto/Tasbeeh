package com.softwarebysto.tasbeeh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.Timer;
import java.util.TimerTask;

public class HomeScreen extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static int totalCountInHome2, counter2;
    private final Timer _timer= new Timer();
    public float x1, x2, y1, y2;
    private static final int MIN_DISTANCE=180;
    private GestureDetector gestureDetector;
    private boolean running;
    Vibrator vb;
    LinearLayout bottomPanel,topPanel,timePanel,counterPanel;
    ImageView profile,settings,tasbeeh;
    TextView countTextView, totalCountTextview,time;


    //From profile activity



    public int counter=0, totalCountInProfile =0, totalCountInHome;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        profile = findViewById(R.id.profile);
        settings= findViewById(R.id.settings);
        tasbeeh = findViewById(R.id.tasbeeh);
        countTextView = findViewById(R.id.count);
        totalCountTextview = findViewById(R.id.total);
        time=findViewById(R.id.timeTextview);
        bottomPanel= findViewById(R.id.buttonPanel1);
        topPanel=findViewById(R.id.topPanel);
        timePanel=findViewById(R.id.timePanel);
        counterPanel=findViewById(R.id.counterpanel);
        vb= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        profile.setOnClickListener(this::openProfile);
        settings.setOnClickListener(this::openSettings);
        bottomPanel.setOnClickListener(this::Nothing);
        topPanel.setOnClickListener(this::Nothing);
        timePanel.setOnClickListener(this::Nothing);
        counterPanel.setOnClickListener(this::Nothing);



        stopWhenNotCounting();

        readCountOfInProfileSharedPreferences();

        //Initialize gestureDetector
        this.gestureDetector = new GestureDetector(HomeScreen.this,this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onBackPressed() {

    }

    /* @Override
    protected void onResume() {

        super.onResume();
    }*/

    @Override
    protected void onStart() {
        totalCountInHome=totalCountInHome2;
        counter=counter2;
        countTextView.setText(String.valueOf(counter));
        totalCountTextview.setText(String.valueOf(totalCountInHome));

        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        counter2=counter;
        totalCountInHome2=totalCountInHome;
        pauseTimer();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        totalCountInHome=totalCountInHome2;
        counter=counter2;
        countTextView.setText(String.valueOf(counter));
        totalCountTextview.setText(String.valueOf(totalCountInHome));
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void Nothing(View view) {

    }


    //override touch event

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            //Start swipe gesture
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            //End of swipe gesture
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                //getting value for horizontal swipe
                float valueX=x2-x1;
                //getting value for vertical swipe
                float valueY=y2-y1;

                if (Math.abs(valueX)>300){
                    if (x2>x1){
                        //This code for Left swipe
                        SettingsPage();
                        Animatoo.animateSwipeRight(HomeScreen.this);

                    }
                    else {
                        //This code for Right swipe
                        ProfilePage();
                        Animatoo.animateSwipeLeft(HomeScreen.this);
                    }
                }
                //Swipe Horizontal
                else if (Math.abs(valueY)>MIN_DISTANCE){
                    //Bottom Swipe detector
                    if (y2>y1){
                        vb.vibrate(15);

                        try {
                            if (!running){
                                startTimer();
                            }
                            //Animation for when tasbeh down
                            tasbeeh.setImageResource(R.drawable.tasbeeh_yellow_down);
                            TimerTask t = new TimerTask() {

                                @Override
                                public void run() {
                                    runOnUiThread(() -> tasbeeh.setImageResource(R.drawable.tasbeeh_yellow_up));

                                }
                            };
                            _timer.schedule(t, 150);
                            //calculating count
                            counter=counter+1;
                            totalCountInProfile = totalCountInProfile +1;
                            totalCountInHome = totalCountInHome+1;
                            saveCountInSharedPreferences(totalCountInProfile);

                            countTextView.setText(String.valueOf(counter));
                            totalCountTextview.setText(String.valueOf(totalCountInHome));
                            if (counter==33) vb.vibrate(150);
                            if (counter>=34){

                                countTextView.setText("1");
                                counter=1;
                            }
                        }catch (Exception ignored){

                        }
                    }
                    //Top Swipe detector
                    else{
                        try {
                            TimerTask t = new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(() -> tasbeeh.setImageResource(R.drawable.tasbeeh_yellow_down));
                                }
                            };
                            _timer.schedule(t,50);
                            TimerTask t2 = new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(()-> tasbeeh.setImageResource(R.drawable.tasbeeh_yellow_up));
                                }
                            };
                            vb.vibrate(15);
                            _timer.schedule(t2,250);
                            counter=counter-1;
                            totalCountInProfile = totalCountInProfile -1;
                            countTextView.setText(String.valueOf(counter));
                            totalCountTextview.setText(String.valueOf(totalCountInHome));
                            totalCountInHome = totalCountInHome-1;
                            saveCountInSharedPreferences(totalCountInProfile);


                            if (totalCountInProfile <=0){
                                totalCountInProfile =0;
                                totalCountTextview.setText(String.valueOf(totalCountInProfile));
                            }
                            if (counter<=0){
                                countTextView.setText("0");
                                counter=0;
                                if (totalCountInProfile == 1){
                                    totalCountInProfile =0;
                                    totalCountTextview.setText(String.valueOf(totalCountInProfile));
                                }

                            }
                        }catch (Exception ignored){

                        }
                    }
                }

        }

        return super.onTouchEvent(event);
    }






    public void startTimer(){
        if (!running){
            running=true;
        }
    }

    public void pauseTimer(){
        if (running){
            running=false;

        }
    }
    public void stopWhenNotCounting(){
        //Stop timer when not counting
        TimerTask t1 = new TimerTask() {

            @Override
            public void run() {
                pauseTimer();

            }
        };
        _timer.schedule(t1, 8000);
    }
    private void savingTimeValueInSharedPreferences(Long chronometerTime){
        SharedPreferences sharedPreferences = getSharedPreferences("ChronoTime", MODE_PRIVATE);
        sharedPreferences.edit().putLong("chronometerTime",chronometerTime).apply();
    }

    private void readTime(){

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        vb.vibrate(15);
        startTimer();
        tasbeeh.setImageResource(R.drawable.tasbeeh_yellow_down);
        TimerTask t = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(() -> tasbeeh.setImageResource(R.drawable.tasbeeh_yellow_up));

            }
        };
        _timer.schedule(t, 150);
        counter=counter+1;
        totalCountInProfile = totalCountInProfile +1;
        totalCountInHome = totalCountInHome+1;
        saveCountInSharedPreferences(totalCountInProfile);
        countTextView.setText(String.valueOf(counter));
        totalCountTextview.setText(String.valueOf(totalCountInHome));

        if (counter==33){
            vb.vibrate(250);
        }
        if (counter>=34){
            countTextView.setText("1");
            counter=1;
        }
        return false;
    }

//It will save integer values totalCountInProfile++
    private void saveCountInSharedPreferences(Integer totalZikrsInProfile ){
        SharedPreferences sharedPreferences=getSharedPreferences("total",MODE_PRIVATE);
        sharedPreferences.edit().putInt("totalZikrsInProfile",totalZikrsInProfile).apply();
    }


//making totalCountInProfile sharedPreferences value
// on start of activity it wont be displayed in home screen

    private void  readCountOfInProfileSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("total",MODE_PRIVATE);
        totalCountInProfile =sharedPreferences.getInt("totalZikrsInProfile",0);
    }



















    //Button Reactions
    private void ProfilePage(){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
    private void SettingsPage(){
        Intent i = new Intent(this, sozlamalar.class);
        startActivity(i);

    }

    private void openProfile(View v) {
        ProfilePage();
        Animatoo.animateSwipeLeft(HomeScreen.this);
    }
    private void openSettings(View view) {
        SettingsPage();
        Animatoo.animateSwipeRight(HomeScreen.this);
    }























    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


}







