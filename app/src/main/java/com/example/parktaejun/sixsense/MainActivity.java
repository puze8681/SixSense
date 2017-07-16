package com.example.parktaejun.sixsense;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parktaejun.sixsense.ContactFunction.SMSreceiver;
import com.example.parktaejun.sixsense.MainFunction.Braille;
import com.example.parktaejun.sixsense.databinding.ActivityMainBinding;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener { //

    ActivityMainBinding mainBinding;

    private GestureDetectorCompat mDetector;
    private static EditText smsText;
    private Button smsSend;
    private TextView txtResult;
    private TextView countBraille;
    public boolean dragUpDown;
    public boolean dragRightLeft;
    private static TextView ind_one;
    private static TextView ind_two;
    private static TextView ind_three;
    private static TextView ind_four;
    private static TextView ind_five;
    private static TextView ind_six;
    public static Vibrator vibrator;
    private Context mContext;
    public static boolean IsSendSMS = false;
    private static String SMS_Content = "";
    String returnValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toast.makeText(getApplicationContext(),"Run",Toast.LENGTH_SHORT).show();

        smsText = mainBinding.txtSend;
        smsSend = mainBinding.btnSend;

        txtResult = mainBinding.txtResult;
        countBraille = mainBinding.countBraille;
        ind_one = mainBinding.indexOne;
        ind_two = mainBinding.indexTwo;
        ind_three = mainBinding.indexThree;
        ind_four = mainBinding.indexFour;
        ind_five = mainBinding.indexFive;
        ind_six = mainBinding.indexSix;
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getApplicationContext(),smsText.getText(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        smsText.addTextChangedListener(textWatcher);
        smsSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });

        countBraille.setText(Integer.toString((Braille.countCheck())));
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        Toast.makeText(getApplicationContext(),"Touch",Toast.LENGTH_SHORT).show();
        return true;
    }
    @Override
    public void onShowPress(MotionEvent e) {  }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
    @Override
    public void onLongPress(MotionEvent e) {   }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(getApplicationContext(),"Fling",Toast.LENGTH_SHORT).show();
        countBraille.setText(Integer.toString((Braille.countCheck())));
        if(Math.abs(e1.getX() - e2.getX()) < 250) {
            if (e1.getY() - e2.getY() > 0) {
                Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
                dragUpDown = true;
                switch (Braille.countCheck()){
                    case 0:
                        ind_one.setText("UP");
                        break;
                    case 1:
                        ind_two.setText("UP");
                        break;
                    case 2:
                        ind_three.setText("UP");
                        break;
                    case 3:
                        ind_four.setText("UP");
                        break;
                    case 4:
                        ind_five.setText("UP");
                        break;
                    case 5:
                        ind_six.setText("UP");
                        break;
                }
                Braille.recGesture(dragUpDown);

            } else if (e2.getY() - e1.getY() > 0) {
                Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
                countBraille.setText(Integer.toString((Braille.countCheck())));
                dragUpDown = false;
                switch (Braille.countCheck()){
                    case 0:
                        ind_one.setText("DOWN");
                        break;
                    case 1:
                        ind_two.setText("DOWN");
                        break;
                    case 2:
                        ind_three.setText("DOWN");
                        break;
                    case 3:
                        ind_four.setText("DOWN");
                        break;
                    case 4:
                        ind_five.setText("DOWN");
                        break;
                    case 5:
                        ind_six.setText("DOWN");
                        break;
                }
                Braille.recGesture(dragUpDown);
            }
        } else if(Math.abs(e1.getY() - e2.getY())< 250){
            if (e1.getX() - e2.getX() > 0) {
                Toast.makeText(getApplication(), "LEFT", Toast.LENGTH_SHORT).show();
                dragRightLeft = false;
            } else if (e2.getX() - e1.getX() > 0) {
                Toast.makeText(getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();
                dragRightLeft = true;
            }
        }
        return true;
    }

    //텍스트뷰에 텍스트를 추가함
    public static void addFirstText(char t){smsText.setText(smsText.getText().toString()+t);}
    public static void addMiddleText(char t){smsText.setText(smsText.getText().toString().substring(0,smsText.length()-1)+t);}
    public static void addLastText(char t){smsText.setText(smsText.getText().toString().substring(0,smsText.length()-1)+t);}
    public static void addNumber(int n){smsText.setText(smsText.getText().toString()+Integer.toString(n));}

    //점자를 초기화함
    public static void initBraille(){
        ind_one.setText("");
        ind_two.setText("");
        ind_three.setText("");
        ind_four.setText("");
        ind_five.setText("");
        ind_six.setText("");
    }

    public void sendSMS(){
        String smsNum = "01097908310";
        String smsText = SMS_Content;

        if (smsNum.length()>0 && smsText.length()>0){
            sendSMS(smsNum, smsText);
        }else{
            Toast.makeText(this, "모두 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS(String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(mContext, "전송 실패"                                                                         , Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(mContext, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        Intent smsIntent = new Intent(MainActivity.this, SMSreceiver.class);
                        startActivity(smsIntent);
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(mContext, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }
}