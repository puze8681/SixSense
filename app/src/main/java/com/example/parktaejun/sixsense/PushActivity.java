package com.example.parktaejun.sixsense;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.parktaejun.sixsense.MainFunction.Hangul;
import com.example.parktaejun.sixsense.MainFunction.Vibrate;
import com.example.parktaejun.sixsense.databinding.ActivityPushBinding;

import java.util.Locale;

public class PushActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    private GestureDetectorCompat mDetector;
    private Vibrator vibrator;
    private static boolean epMode = false;
    TextToSpeech tts;
    String returnValue = "";
    ActivityPushBinding binding;
    String time;
    String number;
    String person;
    String body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        initApp();
    }

    private void initApp() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_push);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mDetector = new GestureDetectorCompat(this, this);

        initDisplay();
        initBraille();
        initTTS();
    }

    private void initTTS(){
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    private void speakTTS(String s){
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void setEarPlugMode(boolean b){
        epMode = b;
    }

    public static boolean getEarPlugMode(){
        return epMode;
    }

    private void initDisplay() {
        Intent getIntent = new Intent();
        String time = getIntent.getExtras().getString("time");
        String number = getIntent.getExtras().getString("name");
        String person = getIntent.getExtras().getString("person");
        String body = getIntent.getExtras().getString("body");

        binding.time.setText(time);
        binding.number.setText(number);
        binding.person.setText(person);
        binding.body.setText(body);
    }

    private void initBraille() {
        returnValue = "";
        if (Hangul.IsHangul(time + " " + number + " " + person + " " + body)) {
            for (int i = 0; i < ((time + " " + number + " " + person + " " + body).length()); i++) {
                String alphabet = Hangul.HangulAlphabet(Hangul.split((time + " " + number + " " + person + " " + body).charAt(i)));
                returnValue += alphabet;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {this.mDetector.onTouchEvent(event);return super.onTouchEvent(event);}
    @Override public void onShowPress(MotionEvent e) {}
    @Override public boolean onSingleTapUp(MotionEvent e) {return false;}
    @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}
    @Override public void onLongPress(MotionEvent e) {}
    @Override public boolean onDown(MotionEvent e) {
        Toast.makeText(getApplicationContext(), "Touch", Toast.LENGTH_SHORT).show();
        if(getEarPlugMode()){
            speakTTS((time + " " + number + " " + person + " " + body));
        } else{
            Vibrate.makeVibe(1);
        }
        return true;
    }
    @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(getApplicationContext(), "Fling", Toast.LENGTH_SHORT).show();
        if (Math.abs(e1.getX() - e2.getX()) < 250 && (e1.getY() - e2.getY() > 0)) {
            //위로 드래그
            Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
            if(!getEarPlugMode())Vibrate.makeVibe(2);
        } else if (Math.abs(e1.getX() - e2.getX()) < 250 && (e2.getY() - e1.getY() > 0)) {
            //아래로 드래그
            Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e1.getX() - e2.getX() > 0)) {
            //왼쪽 드래그
            Toast.makeText(getApplication(), "LEFT", Toast.LENGTH_SHORT).show();
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e2.getX() - e1.getX() > 0)) {
            //오른쪽 드래그
            Toast.makeText(getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();
        } else if ((e2.getX() - e1.getX() > 0) && (e2.getY() - e1.getY() > 0)) {
            //오른쪽 아래 대각선 드래그
            Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            Toast.makeText(getApplicationContext(), "go to phone-book activity", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "nothing on gesture", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
