package com.softwarebysto.tasbeeh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final TimerTask t = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(() -> moveToSecondary());
        }
    };
    private final Timer _timer=new Timer();



    int numberOfDays;

    String appLanguage;

    TextView name,salom;

    Calendar calendar=Calendar.getInstance();

    int year=calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH);
    int day=calendar.get(Calendar.DAY_OF_MONTH);
    String todayString=year+""+month+""+day;



    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _timer.schedule(t, 2000);
        name=findViewById(R.id.nameTXT);
        salom=findViewById(R.id.salom);

        readNameFromSharedPreferences();
        readLanguageFromSharedPreferences();
        checkLanguageAndPutAsDefault();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        read();
        checkEnteredToday();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void checkEnteredToday(){
        SharedPreferences preferences = getSharedPreferences("Pref",0);
        boolean currentDay = preferences.getBoolean(todayString,false);
        if (!currentDay){
            numberOfDays++;
            checkUserEnteredToday(numberOfDays);
            preferences.edit().putBoolean(todayString,true).apply();
        }

    }


    private void checkUserEnteredToday(Integer numberOfDayss){
        SharedPreferences sharedPreferences=getSharedPreferences("enteredDays",MODE_PRIVATE);
        sharedPreferences.edit().putInt("numberOfDays",numberOfDayss).apply();
    }
    private void read(){
        SharedPreferences sharedPreferences=getSharedPreferences("enteredDays",MODE_PRIVATE);
        numberOfDays=sharedPreferences.getInt("numberOfDays",0);
    }






    private void moveToSecondary() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void readLanguageFromSharedPreferences() {
        SharedPreferences readSharedPreferences=getSharedPreferences("Language",MODE_PRIVATE);
        appLanguage=readSharedPreferences.getString("Lang","");
    }


    private void readNameFromSharedPreferences() {
        SharedPreferences readSharedPreferences=getSharedPreferences("name",MODE_PRIVATE);
        String MyName=readSharedPreferences.getString("MyName","");
        if (MyName.isEmpty()){
            name.setText(null);

        }else
        {
            name.setText(MyName);
        }

    }
    @SuppressLint("SetTextI18n")
    private void checkLanguageAndPutAsDefault() {
        switch (appLanguage){
            case "uz":
                setLocale("uz");
                salom.setText("Assalomu Alaykum");
                break;
            case "ru":
                setLocale("ru");
                salom.setText("Ассаляму Алейкум");
                break;
            case "ki":
                setLocale("ki");
                salom.setText("Ассалому Алайкум");
                break;
            case "en":
                setLocale("en");
                salom.setText("Assalamu Alaikum");
                break;
        }
    }
    private void setLocale(String language) {
        Resources resources= getResources();
        DisplayMetrics metrics= resources.getDisplayMetrics();
        Configuration configuration= resources.getConfiguration();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);
    }

}