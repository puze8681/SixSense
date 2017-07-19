package com.example.parktaejun.sixsense.SMSListActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.parktaejun.sixsense.BroadCastReceiver.BroadCastReceiver;
import com.example.parktaejun.sixsense.Font.Font;
import com.example.parktaejun.sixsense.GestureListener.GestureListenerPhoneBookActivity;
import com.example.parktaejun.sixsense.IO.Hangul;
import com.example.parktaejun.sixsense.DataClass.PhoneBookData;
import com.example.parktaejun.sixsense.R;
import com.example.parktaejun.sixsense.databinding.ActivityPhoneBookBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PhoneBookActivity extends AppCompatActivity {

    BroadcastReceiver receiver = new BroadCastReceiver();
    IntentFilter intentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    private static Vibrator vibrator;
    static Context context;
    static ActivityPhoneBookBinding binding;
    public static ArrayList<PhoneBookData> PBitems = new ArrayList<>();
    private static boolean epMode = false;
    static TextToSpeech tts;

    LinearLayout gestureOverlay;
    GestureListenerPhoneBookActivity gestureListener;
    GestureDetector gestureDetector;

    public static int position = 0;
    static String returnValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d("puze","PERMISSION");
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS,
                            Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_PHONE_STATE}, 200);
                    Log.d("puze","initApp0");
                    initApp(position);
                }
            } else {
                Log.d("puze","initApp1");
                initApp(position);
            }
            initApp(position);
        } else {
            Log.d("puze","initApp2");
            initApp(position);
        }
    }

    private void initApp(int p) {
        Font.setGlobalFont(this, getWindow().getDecorView());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_book);

        int position = 0;
        this.position = position;

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initContacts();
        Log.d("puze","initContacts");

        initGesture();
        Log.d("puze","initGesture");

        initReceiver();
        Log.d("puze","initReceiver");

        initDisplay(p);
        Log.d("puze","initDisplay");

        initBraille();
        Log.d("puze","initBraille");

        initTTS();
        Log.d("puze","initTTS");

    }

    private void initContacts(){
        Log.d("puze","log");
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보 가져오는데 사용
                ContactsContract.CommonDataKinds.Phone.NUMBER,        // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }; // 연락처 이름.

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-", "");
                PhoneBookData acontact = new PhoneBookData();
                acontact.setPhoneNum(phonenumber);
                acontact.setDisplayName(contactCursor.getString(2));

                PBitems.add(acontact);
            } while (contactCursor.moveToNext());
        }
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
        gestureOverlay = (LinearLayout) findViewById(R.id.activity_phone_book);
        gestureListener = new GestureListenerPhoneBookActivity(PhoneBookActivity.this, gestureDetector);
        if (gestureOverlay == null) {
            Toast.makeText(PhoneBookActivity.this, "gestureOverlay object is null bro", Toast.LENGTH_SHORT).show();
        }
        gestureOverlay.setOnTouchListener(gestureListener);
    }

    private void initReceiver() {
        receiver = new BroadCastReceiver();
        intentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//        intentFilter.addAction("SMS_DELIVERED_ACTION");
//        intentFilter.addAction("SMS_SENT_ACTION");
        registerReceiver(receiver, intentFilter);
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

    private static void initDisplay(int p) {
        binding.number.setText(PBitems.get(p).getPhoneNum());
        binding.name.setText(PBitems.get(p).getDisplayName());
    }

    public static void speakTTS(String s) {
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void nextDisplay(char g) {
        if (g == 'n') {
            position++;
            if (position > PBitems.size()) {
                position--;
                vibrator.vibrate(500);
            } else {
                initDisplay(position);
            }
        } else if (g == 'b') {
            position--;
            if (position < PBitems.size()) {
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

    public static void setEarPlugMode(boolean b) {
        epMode = b;
    }

    public static boolean getEarPlugMode() {
        return epMode;
    }

    private static void initBraille() {
        if (Hangul.IsHangul(getInfo())) {
            for (int i = 0; i < getInfo().length(); i++) {
                String alphabet = Hangul.HangulAlphabet(Hangul.split(getInfo().charAt(i)));
                returnValue += alphabet;
            }
        }
    }

    public static String getInfo() {
        return PBitems.get(position).getPhoneNum() + " " + PBitems.get(position).getDisplayName();
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
    protected void onPostResume(){
        registerReceiver(receiver, intentFilter);
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}
