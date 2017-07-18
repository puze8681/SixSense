package com.example.parktaejun.sixsense;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

import com.example.parktaejun.sixsense.SMSListActivity.PhoneBookActivity;

public class SplashActivity extends Activity {

    public static Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //전체화면 만들기

        setContentView(R.layout.activity_splash);

        initVibe();

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashActivity.this, PhoneBookActivity.class);
                startActivity(loginIntent);
                vibrate();
                finish();       // 2 초후 이미지를 닫아버림
            }
        }, 2000);

    }

    private void initVibe(){
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void vibrate(){
        vibrator.vibrate(300);
        breakVibe();
        vibrator.vibrate(300);
    }

    private static void breakVibe() {
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 100);
    }
}