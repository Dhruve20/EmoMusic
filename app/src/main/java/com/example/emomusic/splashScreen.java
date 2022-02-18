package com.example.emomusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splashScreen extends AppCompatActivity {

    private  TextView text,text1 ;
    private  ImageView img,img1;
    private Animation anim,anim1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        text = (TextView) findViewById(R.id.textView);
        text.setText("Emo Music");
        img = (ImageView) findViewById(R.id.imageview1);
        img1 = (ImageView) findViewById(R.id.imageView);
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        text1 = (TextView) findViewById(R.id.textView1);
        img1.startAnimation(anim);
        img.startAnimation(anim);
        text.startAnimation(anim1);
        text1.startAnimation(anim1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), SendOTP.class);
                startActivity(intent);
                finish();
            }
        },2300);

    }
}