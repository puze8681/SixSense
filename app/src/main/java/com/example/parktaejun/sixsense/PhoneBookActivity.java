package com.example.parktaejun.sixsense;

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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.parktaejun.sixsense.ContactFunction.SMS_BroadCastReceiver;
import com.example.parktaejun.sixsense.MainFunction.Hangul;
import com.example.parktaejun.sixsense.MainFunction.Vibrate;
import com.example.parktaejun.sixsense.PhoneBook.PhoneBookData;
import com.example.parktaejun.sixsense.databinding.ActivityPhoneBookBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class PhoneBookActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetectorCompat mDetector;
    private Vibrator vibrator;
    SMS_BroadCastReceiver receiver;
    Context context;
    ActivityPhoneBookBinding binding;
    private static ArrayList<PhoneBookData> PBitems = new ArrayList<>();

    int position = 0;
    String returnValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_book);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int position = 0;
        this.position = position;

        mDetector = new GestureDetectorCompat(this, this);

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

        receiver = new SMS_BroadCastReceiver();
        registerReceiver(receiver, new IntentFilter());

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

        initDisplay(p);
        initBraille();
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
        unregisterReceiver(receiver);
    }

    private void initDisplay(int p) {
        binding.phoneNumber.setText(PBitems.get(p).getPhoneNum());
        binding.displayName.setText(PBitems.get(p).getDisplayName());
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

    private void initBraille() {
        if (Hangul.IsHangul(PBitems.get(position).getPhoneNum() + " " + PBitems.get(position).getDisplayName())) {
            for (int i = 0; i < (PBitems.get(position).getPhoneNum() + " " + PBitems.get(position).getDisplayName()).length(); i++) {
                String alphabet = Hangul.HangulAlphabet(Hangul.split((PBitems.get(position).getPhoneNum() + " " + PBitems.get(position).getDisplayName()).charAt(i)));
                returnValue += alphabet;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Toast.makeText(getApplicationContext(), "Touch", Toast.LENGTH_SHORT).show();
        Vibrate.makeVibe(1);
        return true;
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
        Toast.makeText(getApplicationContext(), "Fling", Toast.LENGTH_SHORT).show();
        if (Math.abs(e1.getX() - e2.getX()) < 250 && (e1.getY() - e2.getY() > 0)) {
            //위로 드래그
            Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
            Vibrate.makeVibe(2);
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
