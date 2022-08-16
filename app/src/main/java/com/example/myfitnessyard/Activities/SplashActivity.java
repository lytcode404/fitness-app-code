package com.example.myfitnessyard.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfitnessyard.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView txt;
    private static int splashTimeOut=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);


        logo=(ImageView)findViewById(R.id.logo);
        txt=findViewById(R.id.txt);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_in);
                finish();
            }
        },splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.slide_in);
        Animation animtxt = AnimationUtils.loadAnimation(this,R.anim.slide_from_right);
        logo.startAnimation(myanim);
        txt.startAnimation(animtxt);




    }
}