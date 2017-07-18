package com.example.parktaejun.sixsense.GestureListener;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.parktaejun.sixsense.IO.Input;
import com.example.parktaejun.sixsense.IO.Output;
import com.example.parktaejun.sixsense.SMSFunction.ReceiveMessageActivity;
import com.example.parktaejun.sixsense.SMSFunction.SendMessageActivity;

/**
 * Created by parktaejun on 2017. 7. 18..
 */

public class GestureListenerSendMessageActivity extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    Context mContext;
    GestureDetector gDetector;

    public GestureListenerSendMessageActivity(Context context) {
        this.mContext = context;
    }

    public GestureListenerSendMessageActivity(Context c, AttributeSet attrs) {
        super();
    }

    public GestureListenerSendMessageActivity(Context context, GestureDetector gDetector) {
        if (gDetector == null){
            gDetector = new GestureDetector(context, this);
        }
        this.mContext = context;
        this.gDetector = gDetector;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(mContext, "Fling", Toast.LENGTH_SHORT).show();
        if (Math.abs(e1.getX() - e2.getX()) < 250 && (e1.getY() - e2.getY() > 0)) {
            //위로 드래그
            Toast.makeText(mContext, "UP", Toast.LENGTH_SHORT).show();
            SendMessageActivity.countBraille.setText(Integer.toString((Input.countCheck())));
            SendMessageActivity.setBraiile(Input.countCheck(), 'u');
            Input.recGesture(true);
        } else if (Math.abs(e1.getX() - e2.getX()) < 250 && (e2.getY() - e1.getY() > 0)) {
            //아래로 드래그
            Toast.makeText(mContext, "DOWN", Toast.LENGTH_SHORT).show();
            SendMessageActivity.countBraille.setText(Integer.toString((Input.countCheck())));
            SendMessageActivity.setBraiile(Input.countCheck(), 'd');
            Input.recGesture(false);
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e1.getX() - e2.getX() > 0)) {
            //왼쪽 드래그
            Toast.makeText(mContext, " left : erase ", Toast.LENGTH_SHORT).show();
            SendMessageActivity.eraseText();
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e2.getX() - e1.getX() > 0)) {
            //오른쪽 드래그
            Toast.makeText(mContext, " right : send SMS ", Toast.LENGTH_SHORT).show();
            SendMessageActivity.sendSMS(SendMessageActivity.smsText.getText().toString());
        } else if ((e2.getX() - e1.getX() > 0) && (e2.getY() - e1.getY() > 0)) {
            //오른쪽 아래 대각선 드래그
            Toast.makeText(mContext, "right/down : stt-on ", Toast.LENGTH_SHORT).show();
            SendMessageActivity.init();
            SendMessageActivity.initSTT();
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            Toast.makeText(mContext, "left/up : erase-all ", Toast.LENGTH_SHORT).show();
            SendMessageActivity.initText();
        } else {
            Toast.makeText(mContext, "nothing on gesture", Toast.LENGTH_SHORT).show();
        }

        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Toast.makeText(mContext, "Touch", Toast.LENGTH_SHORT).show();
        if (ReceiveMessageActivity
                .getEarPlugMode()) {
            ReceiveMessageActivity.speakTTS(ReceiveMessageActivity.getInfo());
        } else {
            ReceiveMessageActivity.vibrate(Output.makeVibe(1));
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gDetector.onTouchEvent(event);
    }

    public GestureDetector getDetector() {
        return gDetector;
    }

}