package com.example.parktaejun.sixsense.SMSListActivity;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.parktaejun.sixsense.Font.Font;
import com.example.parktaejun.sixsense.GestureListener.GestureListenerPhoneBookActivity;
import com.example.parktaejun.sixsense.GestureListener.GestureListenerSMSContentActivity;
import com.example.parktaejun.sixsense.IO.Hangul;
import com.example.parktaejun.sixsense.IO.Output;
import com.example.parktaejun.sixsense.R;
import com.example.parktaejun.sixsense.DataClass.SMSContentData;
import com.example.parktaejun.sixsense.SMSFunction.SendMessageActivity;
import com.example.parktaejun.sixsense.databinding.ActivitySmscontentBinding;

import java.util.ArrayList;
import java.util.Locale;


public class SMSContentActivity extends AppCompatActivity {

    private static Vibrator vibrator;

    static Context context;
    static ActivitySmscontentBinding binding;
    public static ArrayList<SMSContentData> SCitems = new ArrayList<>();

    private static boolean epMode = false;
    static TextToSpeech tts;

    public static int position = 0;
    static String returnValue = "";

    LinearLayout gestureOverlay;
    GestureListenerSMSContentActivity gestureListener;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smscontent);


        initApp(position);
    }

    private void initApp(int p) {
        Font.setGlobalFont(this, getWindow().getDecorView());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_smscontent);

        int position = 0;
        this.position = position;

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initGesture();
        readSMSMessage();
        initDisplay(p);
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
        gestureOverlay = (LinearLayout) findViewById(R.id.activity_smscontent);
        gestureListener = new GestureListenerSMSContentActivity(SMSContentActivity.this, gestureDetector);
        if (gestureOverlay == null) {
            Toast.makeText(SMSContentActivity.this, "gestureOverlay object is null bro", Toast.LENGTH_SHORT).show();
        }
        gestureOverlay.setOnTouchListener(gestureListener);
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

    public static void speakTTS(String s){
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void readSMSMessage() {
        SMSContentData data = new SMSContentData();
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(allMessage, new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null, null, "date DESC");

        String string = "";
        int count = 0;
        while (c.moveToNext()) {
            long messageId = c.getLong(0);
            long threadId = c.getLong(1);
            String address = c.getString(2);
            long contactId = c.getLong(3);
            String contactId_string = String.valueOf(contactId);
            long timestamp = c.getLong(4);
            String body = c.getString(5);

            string = String.format("msgid:%d, threadid:%d, address:%s, " + "contactid:%d, contackstring:%s, timestamp:%d, body:%s", messageId, threadId, address, contactId,
                    contactId_string, timestamp, body);

            Log.d("heylee", ++count + "st, Message: " + body);

            data.setPhoneNum((c.getString(c.getColumnIndex("address"))));
            data.setDisplayName((c.getString(c.getColumnIndex("person"))));
            data.setBody((c.getString(c.getColumnIndex("body"))));
            SCitems.add(data);
        }
    }

    public static void setEarPlugMode(boolean b){
        epMode = b;
    }

    public static boolean getEarPlugMode(){
        return epMode;
    }

    private static void initDisplay(int p) {
        binding.number.setText(SCitems.get(p).getPhoneNum());
        binding.name.setText(SCitems.get(p).getDisplayName());
        binding.body.setText(SCitems.get(p).getBody());
    }

    public static void nextDisplay(char g) {
        if (g == 'n') {
            position++;
            if (position > SCitems.size()) {
                position--;
                vibrator.vibrate(500);
            } else {
                initDisplay(position);
            }
        } else if (g == 'b') {
            position--;
            if (position < SCitems.size()) {
                position++;
                vibrator.vibrate(500);
            } else {
                initDisplay(position);
            }
        } else {
            Toast.makeText(context, "Draw Gesture Again, Please", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(1200);
        }
        initBraille();
    }

    private static void initBraille() {
        returnValue = "";
        if (Hangul.IsHangul(getInfo())) {
            for (int i = 0; i < getInfo().length(); i++) {
                String alphabet = Hangul.HangulAlphabet(Hangul.split(getInfo().charAt(i)));
                returnValue += alphabet;
            }
        }
    }

    public static String getInfo(){
        return SCitems.get(position).getPhoneNum() + " " + SCitems.get(position).getDisplayName() + " " + SCitems.get(position).getBody();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initApp(position);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
