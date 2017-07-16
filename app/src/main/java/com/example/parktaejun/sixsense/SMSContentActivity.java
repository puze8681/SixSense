package com.example.parktaejun.sixsense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.example.parktaejun.sixsense.ContactFunction.SMS_BroadCastReceiver;
import com.example.parktaejun.sixsense.MainFunction.Hangul;
import com.example.parktaejun.sixsense.MainFunction.Vibrate;
import com.example.parktaejun.sixsense.PhoneBook.SMSContentData;
import com.example.parktaejun.sixsense.databinding.ActivitySmscontentBinding;

import java.util.ArrayList;


public class SMSContentActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetectorCompat mDetector;
    private Vibrator vibrator;
    SMS_BroadCastReceiver receiver;
    Context context;
    ActivitySmscontentBinding binding;
    private static ArrayList<SMSContentData> SCitems = new ArrayList<>();

    int position = 0;
    String returnValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smscontent);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_smscontent);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int position = 0;
        this.position = position;

        mDetector = new GestureDetectorCompat(this, this);

        initApp(position);
    }

    private void initApp(int p) {

        receiver = new SMS_BroadCastReceiver();
        registerReceiver(receiver, new IntentFilter());

        readSMSMessage();
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

    private void initDisplay(int p) {
        binding.phoneNumber.setText(SCitems.get(p).getPhoneNum());
        binding.displayName.setText(SCitems.get(p).getDisplayName());
        binding.body.setText(SCitems.get(p).getBody());
    }

    private void nextDisplay(char g) {
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
            Toast.makeText(this, "Draw Gesture Again, Please", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(1200);
        }
        initBraille();
    }

    private void initBraille() {
        returnValue = "";
        if (Hangul.IsHangul(SCitems.get(position).getPhoneNum() + " " + SCitems.get(position).getDisplayName() + " " + SCitems.get(position).getBody())) {
            for (int i = 0; i < (SCitems.get(position).getPhoneNum() + " " + SCitems.get(position).getDisplayName() + " " + SCitems.get(position).getBody()).length(); i++) {
                String alphabet = Hangul.HangulAlphabet(Hangul.split((SCitems.get(position).getPhoneNum() + " " + SCitems.get(position).getDisplayName() + " " + SCitems.get(position).getBody()).charAt(i)));
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
            Intent smActivityIntent = new Intent(SMSContentActivity.this, SendMessageActivity.class);
            smActivityIntent.putExtra("number", this.SCitems.get(position).getPhoneNum());
            startActivity(smActivityIntent);
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            Toast.makeText(getApplicationContext(), "go to phone-book activity", Toast.LENGTH_SHORT).show();
            Intent phActivityIntent = new Intent(SMSContentActivity.this, PhoneBookActivity.class);
            startActivity(phActivityIntent);
            finish();
        } else {
            Toast.makeText(getApplication(), "nothing on gesture", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
