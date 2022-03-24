package com.softwarebysto.tasbeeh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class Profile extends AppCompatActivity  implements GestureDetector.OnGestureListener{
    public float x1, x2, y1, y2;
    private int length;
    private GestureDetector gestureDetector;
    Integer numberOfDaysEntered,averageZikrs;

    Resources resources;
    SharedPreferences sharedPreferences;
    AlertDialog dialog;
    Button reNameBTN,shareBTN,saveBTN,do_not_saveBTN;
    TextView totalDhikrsTextView,name,textLength,average_zikrTextView,usedDays;
    ImageView home,settings;
    EditText nameEditTXT;

    int totalCount;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
        }catch (Exception ignored){

        }
        home= findViewById(R.id.home);
        settings= findViewById(R.id.settings);
        totalDhikrsTextView =findViewById(R.id.total_dhikr);
        reNameBTN=findViewById(R.id.reNameBTN);
        shareBTN=findViewById(R.id.shareBTN);
        average_zikrTextView=findViewById(R.id.average_zikr);
        usedDays=findViewById(R.id.usedDays);

        name=findViewById(R.id.name);
        sharedPreferences=getSharedPreferences("",MODE_PRIVATE);
        shareBTN.setOnClickListener(this::share);
        home.setOnClickListener(this::openHome);
        settings.setOnClickListener(this::openSettings);
/////////////////////////////////////////////////////////////////////////////////////////////////
        readNameFromSharedPreferences();

        readCountFromSharedPreferences();

        readNumberOfDaysFromSharedPreferences();
/////////////////////////////////////////////////////////////////////////////////////////////////

        this.gestureDetector = new GestureDetector(Profile.this,this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //  Getting strings from XML file to our project

        resources=getResources();
        String noTextFound,name_hint,save,do_not_save,saved_Successfully,moreThen25;

        noTextFound=resources.getString(R.string.noTextFound);

        name_hint=resources.getString(R.string.name_hint);
        save=resources.getString(R.string.save);
        do_not_save=resources.getString(R.string.don_t_save);
        saved_Successfully=resources.getString(R.string.saved_Successfully);
        moreThen25=resources.getString(R.string.moreThen25);

        // Rename button click code

        reNameBTN.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Profile.this);
            View view2 = getLayoutInflater().inflate(R.layout.layout_dialog, null);

            nameEditTXT= view2.findViewById(R.id.nameEditTXT);
            saveBTN=view2.findViewById(R.id.saveBTN);
            do_not_saveBTN=view2.findViewById(R.id.do_notSaveBTN);
            textLength=view2.findViewById(R.id.textLength);

            nameEditTXT.setHint(name_hint);
            saveBTN.setText(save);
            do_not_saveBTN.setText(do_not_save);

            nameEditTXT.setHintTextColor(this.getResources().getColor(R.color.hint));

            nameEditTXT.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (nameEditTXT.length()>25){
                        length=nameEditTXT.length();
                        textLength.setText(String.valueOf(length));
                        textLength.setVisibility(View.VISIBLE);
                    }
                    if (nameEditTXT.length()<25){
                        textLength.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            saveBTN.setOnClickListener(v2 ->{
                if (nameEditTXT.length()<=0){
                    nameEditTXT.setHint(noTextFound);
                    nameEditTXT.setHintTextColor(this.getResources().getColor(R.color.red));
                    dialog.setCancelable(false);
                }
                if (nameEditTXT.length()>25){
                    Toast.makeText(this, moreThen25, Toast.LENGTH_SHORT).show();
                }
                else if (nameEditTXT.length()<=25 && nameEditTXT.length()>0){
                    name.setText(nameEditTXT.getText().toString());
                    saveNameInSharedPreferences(nameEditTXT.getText().toString());
                    dialog.dismiss();
                    Toast.makeText(this, saved_Successfully, Toast.LENGTH_SHORT).show();
                }
                    }
                );

            do_not_saveBTN.setOnClickListener(v3 ->{
                nameEditTXT.setText("");
                dialog.dismiss();
            });

            builder1.setView(view2);
            dialog = builder1.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        });

    }

    @Override
    protected void onStart() {
        calculationOfAverageZikrs();
        super.onStart();
    }

    private void readNameFromSharedPreferences() {

        Resources resources= getResources();
        String Rename,Naming;

        Rename=resources.getString(R.string.Rename);
        Naming=resources.getString(R.string.naming);

        SharedPreferences readSharedPreferences=getSharedPreferences("name",MODE_PRIVATE);
        String MyName=readSharedPreferences.getString("MyName","");
        if (MyName.isEmpty()){
            reNameBTN.setText(Naming);

        }else
        {
            reNameBTN.setText(Rename);
            name.setText(MyName);
        }
    }




    //This code for saving name to shared preferences
    private void saveNameInSharedPreferences(String name) {
        SharedPreferences sharedPreferences=getSharedPreferences("name",MODE_PRIVATE);
        SharedPreferences.Editor myName=sharedPreferences.edit();
        myName.putString("MyName", name);
        myName.apply();

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
                        openHomePage();
                        Animatoo.animateSwipeRight(Profile.this);

                    }
                    else {
                        //This code for Right swipe
                        openSettingsPage();
                        Animatoo.animateSwipeLeft(Profile.this);
                    }
                }
        }

        return super.onTouchEvent(event);
    }




    private void readCountFromSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("total",MODE_PRIVATE);
        totalCount =sharedPreferences.getInt("totalZikrsInProfile",0);
        totalDhikrsTextView.setText(String.valueOf(totalCount));
    }

    private void readNumberOfDaysFromSharedPreferences(){
        SharedPreferences sharedPreferencesNumber=getSharedPreferences("enteredDays",MODE_PRIVATE);
        numberOfDaysEntered =sharedPreferencesNumber.getInt("numberOfDays",0);
        usedDays.setText(String.valueOf(numberOfDaysEntered));
    }

    private void calculationOfAverageZikrs(){
        averageZikrs=totalCount/numberOfDaysEntered;
        average_zikrTextView.setText(String.valueOf(averageZikrs));
    }



    public void openSettingsPage(){
        Intent intent = new Intent(this, sozlamalar.class);
        startActivity(intent);
    }
    public void openHomePage(){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);

    }

    private void share(View view) {

    }



    private void openSettings(View v) {
        startActivity(new Intent(this, sozlamalar.class));
        Animatoo.animateSwipeRight(Profile.this);
    }

    private void openHome (View v){
        startActivity(new Intent(this, HomeScreen.class));
        Animatoo.animateZoom(Profile.this);

    }

































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