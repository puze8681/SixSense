package com.example.parktaejun.sixsense.SMSFunction;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.parktaejun.sixsense.Font.Font;
import com.example.parktaejun.sixsense.GestureListener.GestureListenerReceiveMessageActivity;
import com.example.parktaejun.sixsense.IO.Hangul;
import com.example.parktaejun.sixsense.R;
import com.example.parktaejun.sixsense.databinding.ActivityReceiverMessageBinding;

import java.util.Locale;

public class ReceiveMessageActivity extends AppCompatActivity {

    Context context;
    public static Vibrator vibrator;
    private static boolean epMode = false;
    static TextToSpeech tts;
    String returnValue = "";
    ActivityReceiverMessageBinding binding;
    static String time;
    public static String number;
    static String person;
    static String body;

    LinearLayout gestureOverlay;
    GestureListenerReceiveMessageActivity gestureListener;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_message);

        initApp();
    }

    private void initApp() {
        Font.setGlobalFont(this, getWindow().getDecorView());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receiver_message);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        initGesture();
        initDisplay();
        initBraille();
        initTTS();
    }

    public static void vibrate(int a) {
        switch (a) {
            case 0:
                break;
            case 1:
                shortVibe();
                break;
            case 2:
                longVibe();
                break;
            case 11:
                shortVibe();
                shortVibe();
                break;
            case 111:
                shortVibe();
                shortVibe();
                shortVibe();
                break;
            default:
                break;
        }
    }

    //짧은 진동 (0.5초
    private static void shortVibe() {
        vibrator.vibrate(500);
        breakVibe();
    }

    //긴 진동 (1.5초)
    private static void longVibe() {
        vibrator.vibrate(1500);
        breakVibe();
    }

    private static void breakVibe() {
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 250);
    }

    private void initGesture() { //activity gesture recognition
        gestureOverlay = (LinearLayout) findViewById(R.id.activity_receiver_message);
        gestureListener = new GestureListenerReceiveMessageActivity(context, gestureDetector);
        if (gestureOverlay == null) {
            Toast.makeText(context, "gestureOverlay object is null bro", Toast.LENGTH_SHORT).show();
        }
        gestureOverlay.setOnTouchListener(gestureListener);
    }

    private void initTTS() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    public static void speakTTS(String s) {
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void setEarPlugMode(boolean b) {
        epMode = b;
    }

    public static boolean getEarPlugMode() {
        return epMode;
    }

    private void initDisplay() {
        Intent getIntent = new Intent();
        time = getIntent.getExtras().getString("time");
        number = getIntent.getExtras().getString("name");
        person = getIntent.getExtras().getString("person");
        body = getIntent.getExtras().getString("body");

        binding.time.setText(time);
        binding.number.setText(number);
        binding.person.setText(person);
        binding.body.setText(body);
    }

    private void initBraille() {
        returnValue = "";
        if (Hangul.IsHangul(getInfo())) {
            for (int i = 0; i < (getInfo().length()); i++) {
                String alphabet = Hangul.HangulAlphabet(Hangul.split((getInfo().charAt(i))));
                returnValue += alphabet;
            }
        }
    }

    public static String getInfo(){
        return time + " " + number + " " + person + " " + body;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
