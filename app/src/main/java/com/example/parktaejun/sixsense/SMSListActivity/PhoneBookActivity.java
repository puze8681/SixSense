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
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.parktaejun.sixsense.BroadCastReceiver.BroadCastReceiver;
import com.example.parktaejun.sixsense.Function.Hangul;
import com.example.parktaejun.sixsense.Function.IO.Output;
import com.example.parktaejun.sixsense.DataClass.PhoneBookData;
import com.example.parktaejun.sixsense.R;
import com.example.parktaejun.sixsense.SMSFunction.SendMessageActivity;
import com.example.parktaejun.sixsense.databinding.ActivityPhoneBookBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PhoneBookActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetectorCompat mDetector;
    private Vibrator vibrator;
    BroadCastReceiver receiver;
    Context context;
    ActivityPhoneBookBinding binding;
    public static ArrayList<PhoneBookData> PBitems = new ArrayList<>();
    private static boolean epMode = false;
    TextToSpeech tts;

    int position = 0;
    String returnValue = "";

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_book);

        int position = 0;
        this.position = position;

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mDetector = new GestureDetectorCompat(this, this);

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

        initReceiver();
        initDisplay(p);
        initBraille();
        initTTS();
    }

    private void initReceiver(){
        receiver = new BroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.addAction("SMS_DELIVERED_ACTION");
        intentFilter.addAction("SMS_SENT_ACTION");
        registerReceiver(receiver, intentFilter);
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

    private void initDisplay(int p) {
        binding.number.setText(PBitems.get(p).getPhoneNum());
        binding.name.setText(PBitems.get(p).getDisplayName());
    }

    private void nextDisplay(char g) {
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
            Toast.makeText(this, "Draw Gesture Again, Please", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(1200);
        }
        initBraille();
    }

    public static void setEarPlugMode(boolean b){
        epMode=b;
    }

    public static boolean getEarPlugMode(){
        return epMode;
    }

    private void initBraille() {
        if(Hangul.IsHangul(PBitems.get(position).getPhoneNum() + " " + PBitems.get(position).getDisplayName())) {
            for (int i = 0; i < (PBitems.get(position).getPhoneNum() + " " + PBitems.get(position).getDisplayName()).length(); i++) {
                String alphabet = Hangul.HangulAlphabet(Hangul.split((PBitems.get(position).getPhoneNum() + " " + PBitems.get(position).getDisplayName()).charAt(i)));
                returnValue += alphabet;
            }
        }
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
    protected void onPause(){
        super.onPause();
        unregisterReceiver(receiver);

        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);

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
            speakTTS(PBitems.get(position).getPhoneNum() + " " + PBitems.get(position).getDisplayName());
        } else{
            Output.makeVibe(1);
        }
        return true;
    }
    @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(getApplicationContext(), "Fling", Toast.LENGTH_SHORT).show();
        if (Math.abs(e1.getX() - e2.getX()) < 250 && (e1.getY() - e2.getY() > 0)) {
            //위로 드래그
            Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
            if(!getEarPlugMode()) Output.makeVibe(2);
        } else if (Math.abs(e1.getX() - e2.getX()) < 250 && (e2.getY() - e1.getY() > 0)) {
            //아래로 드래그
            Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e1.getX() - e2.getX() > 0)) {
            //왼쪽 드래그
            Toast.makeText(getApplication(), "LEFT", Toast.LENGTH_SHORT).show();
            nextDisplay('b');
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e2.getX() - e1.getX() > 0)) {
            //오른쪽 드래그
            Toast.makeText(getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();
            nextDisplay('n');
        } else if ((e2.getX() - e1.getX() > 0) && (e2.getY() - e1.getY() > 0)) {
            //오른쪽 아래 대각선 드래그
            Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
            Intent smActivityIntent = new Intent(PhoneBookActivity.this, SendMessageActivity.class);
            smActivityIntent.putExtra("number", this.PBitems.get(position).getPhoneNum());
            startActivity(smActivityIntent);
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
            Intent scActivityIntent = new Intent(PhoneBookActivity.this, SMSContentActivity.class);
            startActivity(scActivityIntent);
        } else {
            Toast.makeText(getApplication(), "nothing on gesture", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
