package com.softwarebysto.tasbeeh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.Locale;

public class sozlamalar extends AppCompatActivity implements GestureDetector.OnGestureListener {
    public float x1, x2, y1, y2;

    private GestureDetector gestureDetector;
    public String systemLanguage,appLanguage;


    AlertDialog dialog;

    RadioButton en,uz,ru,ki;

    ImageView home,profile;

    LinearLayout language;

    TextView SettingsTXT,dhikrTXT,tasbeehTXT,languageTXT,aboutTXT;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sozlamalar);
        home=findViewById(R.id.home);
        profile=findViewById(R.id.profile);
        language=findViewById(R.id.language);
        SettingsTXT=findViewById(R.id.settingsTXT);
        dhikrTXT=findViewById(R.id.dhikrTXT);
        tasbeehTXT=findViewById(R.id.tasbeehTXT);
        languageTXT=findViewById(R.id.languageTXT);
        aboutTXT=findViewById(R.id.aboutTXT);


        View view2 = getLayoutInflater().inflate(R.layout.language_dialog, null);
        en=view2.findViewById(R.id.en);
        uz=view2.findViewById(R.id.uz);
        ru=view2.findViewById(R.id.ru);
        ki=view2.findViewById(R.id.ki);

        home.setOnClickListener(this::openHomePage);
        profile.setOnClickListener(this::openProfilePage);
        language.setOnClickListener(this::languageSet);

        this.gestureDetector = new GestureDetector(sozlamalar.this,this);

        getSystemLanguage();
        readLanguageFromSharedPreferences();


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


    @Override
    public void onBackPressed() {

    }

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

                if (Math.abs(valueX)>300){
                    //detect left to right swipe
                    if (x2>x1){
                        //This code for Left swipe
                        openProfilePage();
                        Animatoo.animateSwipeRight(sozlamalar.this);
                    }
                    else {
                        //This code for Right swipe
                        openHomePage();
                        Animatoo.animateSwipeLeft(sozlamalar.this);
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    private void openProfilePage(View view) {
        openProfilePage();
        Animatoo.animateSwipeLeft(sozlamalar.this);
    }

    private void openHomePage(View view) {
        openHomePage();
    }
    private void languageSet(View view) {
        chooseLanguage();
    }

    @SuppressLint("SetTextI18n")
    private void chooseLanguage() {
        //Getting text strings from strings.xml file
        Resources resources= getResources();

        //Radio buttons for choosing language


        //Buttons for saving don't saving info

        Button save, don_t_save;


        AlertDialog.Builder builder = new AlertDialog.Builder(sozlamalar.this);
        //dialog showing
        View view2 = getLayoutInflater().inflate(R.layout.language_dialog, null);

        en=view2.findViewById(R.id.en);
        uz=view2.findViewById(R.id.uz);
        ru=view2.findViewById(R.id.ru);
        ki=view2.findViewById(R.id.ki);



        save=view2.findViewById(R.id.saveBTN);
        don_t_save=view2.findViewById(R.id.do_notSaveBTN);

        switch (appLanguage){
            case "uz":
                uz.setChecked(true);
                break;
            case "ru":
                ru.setChecked(true);
                break;
            case "ki":
                ki.setChecked(true);
                break;
            case "en":
                en.setChecked(true);
                break;
        }

        save.setOnClickListener(v -> {
            if (en.isChecked()){
                setLocale("en");
                dialog.dismiss();
                appLanguage="en";
                saveLanguageInSharedPreferences(appLanguage);
            }
            else if (uz.isChecked()){
                setLocale("uz");
                dialog.dismiss();
                appLanguage="uz";
                saveLanguageInSharedPreferences(appLanguage);
            }
            else if (ru.isChecked()){
                setLocale("ru");
                dialog.dismiss();
                appLanguage="ru";
                saveLanguageInSharedPreferences(appLanguage);
            }
            else if (ki.isChecked()){
                setLocale("ki");
                dialog.dismiss();
                appLanguage="ki";
                saveLanguageInSharedPreferences(appLanguage);
            }
            else{

            }

        });
        don_t_save.setOnClickListener(v -> {
            dialog.dismiss();
        });
        builder.setView(view2);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        //radioBoxChecking();
    }


    private void readLanguageFromSharedPreferences() {
        SharedPreferences readSharedPreferences=getSharedPreferences("Language",MODE_PRIVATE);
        appLanguage=readSharedPreferences.getString("Lang","");
    }


    private void setLocale(String language) {
        Resources resources= getResources();
        DisplayMetrics metrics= resources.getDisplayMetrics();
        Configuration configuration= resources.getConfiguration();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SettingsTXT.setText(R.string.settings);
        dhikrTXT.setText(R.string.zikr_sozlamalari);
        tasbeehTXT.setText(R.string.tasbeh_ko_rinishi);
        languageTXT.setText(R.string.til_sozlamalari);
        aboutTXT.setText(R.string.dastur_haqida);
    }

    public void openHomePage(){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        Animatoo.animateZoom(sozlamalar.this);

    }
    private void openProfilePage(){
        Intent intent = new Intent(this, Profile.class);
        Animatoo.animateSwipeLeft(sozlamalar.this);
        startActivityForResult(intent,0);

    }

    private void getSystemLanguage() {
        systemLanguage=Locale.getDefault().getDisplayLanguage();
    }

    private void saveLanguageInSharedPreferences(String appLanguage){
        SharedPreferences sharedPreferences=getSharedPreferences("Language",MODE_PRIVATE);
        SharedPreferences.Editor Lang=sharedPreferences.edit();
        Lang.putString("Lang",appLanguage);
        Lang.apply();
    }


    // @SuppressLint("SetTextI18n")
    // private void radioBoxChecking() {
    //     View view2 = getLayoutInflater().inflate(R.layout.language_dialog, null);
    //     Button save, don_t_save;
    //
    //     save=view2.findViewById(R.id.saveBTN);
    //     don_t_save=view2.findViewById(R.id.do_notSaveBTN);
    //
    //     en.setOnClickListener(v ->{
    //
    //         en.setText("English");
    //         uz.setText("Uzbek (latin)");
    //         ru.setText("Russian");
    //         ki.setText("Uzbek (kiril)");
    //         save.setText("Save");
    //         don_t_save.setText("Don't save");
    //
    //     });
    //     uz.setOnClickListener(v ->{
    //
    //         en.setText("Inglizcha");
    //         uz.setText("O'zbekcha (lotin)");
    //         ru.setText("Ruscha");
    //         ki.setText("O'zbekcha (kiril)");
    //         save.setText("Saqlansin");
    //         don_t_save.setText("Saqlanmasin");
    //
    //     });
    //     ru.setOnClickListener(v ->{
    //
    //         en.setText("Английский");
    //         uz.setText("Узбекский (лат.)");
    //         ru.setText("Русский");
    //         ki.setText("Узбек (кирил)");
    //         save.setText("Сохранять");
    //         don_t_save.setText("Не сохранять");
    //
    //     });
    //     ki.setOnClickListener(v ->{
    //
    //         en.setText("Инглизча");
    //         uz.setText("Ўзбекча (лотин)");
    //         ru.setText("Русча");
    //         ki.setText("Ўзбекча (кирил)");
    //         save.setText("Сақлансин");
    //         don_t_save.setText("Сақланмасин");
    //
    //     });
    // }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
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