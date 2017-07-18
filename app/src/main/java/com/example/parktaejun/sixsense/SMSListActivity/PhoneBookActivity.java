package com.example.parktaejun.sixsense.SMSListActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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


public class PhoneBookActivity extends AppCompatActivity{

    private static Vibrator vibrator;
    BroadCastReceiver receiver;
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
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS,
                            Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_PHONE_STATE}, 200);
                }
            } else {
                initApp(position);
            }
        } else {
            initApp(position);
        }
    }

    private void initApp(int p) {
        Font.setGlobalFont(this, getWindow().getDecorView());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_book);

        int position = 0;
        this.position = position;

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        SharedPreferences sharedPreferences = getSharedPreferences("hello world", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isFill", false)) {

            String[] arrProjection = {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor clsCursor = context.getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    arrProjection,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + "= 1",
                    null, null
            );

            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

            while (clsCursor.moveToNext()) {
                PhoneBookData data = new PhoneBookData();

                String name;
                String num = null;

                String contactID = clsCursor.getString(0);
                Log.d("puze", "연락처 ID : " + clsCursor.getString(0));
                Log.d("puze", "연락처 이름 : " + clsCursor.getString(1));

                Cursor nCursor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactID,
                        null, null
                );
                while (nCursor.moveToNext()) {
                    Log.d("puze", "연락처 번호 : " + nCursor.getString(0));
                    num = nCursor.getString(0);
                }

                name = clsCursor.getString(1);

                String nnum = num.replace("-", "");

                sharedPreferencesEditor.putString(num, name);
                sharedPreferencesEditor.putString(nnum, name);
                sharedPreferencesEditor.apply();

                data.setDisplayName(name);
                data.setPhoneNum(num);

                PBitems.add(data);

                nCursor.close();
            }
            clsCursor.close();

            Gson gson = new Gson();
            String json = gson.toJson(PBitems);
            Log.d("puze", json);
            sharedPreferencesEditor.putString("contacts", json);
            sharedPreferencesEditor.apply();

            sharedPreferencesEditor.putBoolean("isFill", true);
            sharedPreferencesEditor.apply();
        } else {
            String json = sharedPreferences.getString("contacts", null);
            Log.d("puze", json);
            if (json != null) {
                Gson gson = new Gson();
                PBitems = gson.fromJson(json, new TypeToken<List<PhoneBookData>>() {
                }.getType());
                Log.d("puze", PBitems.get(0).toString());
                PBitems.addAll(PBitems);
            }
        }

        initGesture();
        initReceiver();
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
        gestureOverlay = (LinearLayout) findViewById(R.id.activity_phone_book);
        gestureListener = new GestureListenerPhoneBookActivity(PhoneBookActivity.this, gestureDetector);
        if (gestureOverlay == null) {
            Toast.makeText(PhoneBookActivity.this, "gestureOverlay object is null bro", Toast.LENGTH_SHORT).show();
        }
        gestureOverlay.setOnTouchListener(gestureListener);
    }

    private void initReceiver() {
        receiver = new BroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
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

    public static String getInfo(){
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
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
