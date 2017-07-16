package com.example.parktaejun.sixsense.ContactFunction;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.parktaejun.sixsense.ContactFunction.Broadcastreceiver;
import com.example.parktaejun.sixsense.MainFunction.Hangul;
import com.example.parktaejun.sixsense.MainFunction.Vibrate;
import com.example.parktaejun.sixsense.R;
import com.example.parktaejun.sixsense.databinding.ActivitySmsreceiverBinding;

public class SMSreceiver extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private ActivitySmsreceiverBinding smsreceiverBinding;
    private GestureDetectorCompat mDetector;
    public static Vibrator vibrator;
    private static Context mContext;
    String returnValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreceiver);

        smsreceiverBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        smsreceiverBinding.txtContent.setText(Broadcastreceiver.smsText);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mDetector = new GestureDetectorCompat(this, this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

        if(Hangul.IsHangul(smsreceiverBinding.txtContent.getText().toString())) {
            for (int i = 0; i < smsreceiverBinding.txtContent.getText().length(); i++) {
                String alphabet = Hangul.HangulAlphabet(Hangul.split(smsreceiverBinding.txtContent.getText().charAt(i)));
                returnValue += alphabet;
            }
            smsreceiverBinding.alphabet.setText(returnValue);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

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
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e2.getX() - e1.getX() > 0)) {
            //오른쪽 드래그
            Toast.makeText(getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();
        } else if ((e2.getX() - e1.getX() > 0) && (e2.getY() - e1.getY() > 0)){
            //오른쪽 아래 대각선 드래그
            Toast.makeText(getApplicationContext(), "RIGHT DOWN", Toast.LENGTH_SHORT).show();
            finish();
        }

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    public static final void throwToast() {
        Toast.makeText(mContext, "Drag 모션이 없습니다", Toast.LENGTH_SHORT).show();
    }
}
