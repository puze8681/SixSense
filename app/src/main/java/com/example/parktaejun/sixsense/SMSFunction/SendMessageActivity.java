package com.example.parktaejun.sixsense.SMSFunction;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parktaejun.sixsense.Function.IO.Input;
import com.example.parktaejun.sixsense.R;
import com.example.parktaejun.sixsense.databinding.ActivitySendMessageBinding;

import java.util.ArrayList;

public class SendMessageActivity extends Activity implements GestureDetector.OnGestureListener { //

    ActivitySendMessageBinding mainBinding;

    private GestureDetectorCompat mDetector;
    private static TextView smsText;
    private TextView countBraille;
    Intent sttIntent;
    SpeechRecognizer stt;
    private static TextView ind_one;
    private static TextView ind_two;
    private static TextView ind_three;
    private static TextView ind_four;
    private static TextView ind_five;
    private static TextView ind_six;
    private Context mContext;
    public static boolean IsSendSMS = false;
    private static String SMS_Content = "";
    String returnValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        initApp();
    }

    private void initApp(){
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_send_message);
        Toast.makeText(getApplicationContext(), "Run", Toast.LENGTH_SHORT).show();

        smsText = mainBinding.txtSend;
        countBraille = mainBinding.countBraille;
        ind_one = mainBinding.indexOne;
        ind_two = mainBinding.indexTwo;
        ind_three = mainBinding.indexThree;
        ind_four = mainBinding.indexFour;
        ind_five = mainBinding.indexFive;
        ind_six = mainBinding.indexSix;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getApplicationContext(), smsText.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        smsText.addTextChangedListener(textWatcher);

        countBraille.setText(Integer.toString((Input.countCheck())));
        mDetector = new GestureDetectorCompat(this, this);
    }

    //점자를 초기화함
    public static void initBraille() {
        ind_one.setText("");
        ind_two.setText("");
        ind_three.setText("");
        ind_four.setText("");
        ind_five.setText("");
        ind_six.setText("");
    }

    private void initSTT(){
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        stt = SpeechRecognizer.createSpeechRecognizer(this);
        stt.setRecognitionListener(listener);
        stt.startListening(sttIntent);
    }

    //텍스트뷰에 텍스트를 추가함
    public static void addFirstText(char t) {
        smsText.setText(smsText.getText().toString() + t);
    }

    public static void addMiddleText(char t) {
        smsText.setText(smsText.getText().toString().substring(0, smsText.length() - 1) + t);
    }

    public static void addLastText(char t) {
        smsText.setText(smsText.getText().toString().substring(0, smsText.length() - 1) + t);
    }

    public static void addNumber(int n) {
        smsText.setText(smsText.getText().toString() + Integer.toString(n));
    }

    private void eraseText() {
        smsText.setText(smsText.getText().toString().substring(0, smsText.getText().toString().length()-2));
        initBraille();
        Input.init();
        countBraille.setText("0");
    }

    private void initText(){
        smsText.setText("");
        initBraille();
        Input.init();
        countBraille.setText("0");
    }

    private void init(){
        smsText.setText(smsText.getText().toString());
        initBraille();
        Input.init();
        countBraille.setText("0");
    }

    private void sendSMS(String s) {
        String smsNum = "01097908310";
        String smsText = s;

        if (smsNum.length() > 0 && smsText.length() > 0) {
            sendSMS(smsNum, smsText);
        } else {
            Toast.makeText(this, "모두 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS(String smsNumber, String smsText) {
        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, null, null);
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override public void onReadyForSpeech(Bundle params) {}
        @Override public void onBeginningOfSpeech() {}
        @Override public void onRmsChanged(float rmsdB) {}
        @Override public void onBufferReceived(byte[] buffer) {}
        @Override public void onEndOfSpeech() {}
        @Override public void onError(int error) {}
        @Override public void onPartialResults(Bundle partialResults) {}
        @Override public void onEvent(int eventType, Bundle params) {}
        @Override public void onResults(Bundle results) {
            String key= "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            smsText.setText(smsText.getText().toString() + rs[0]);
        }
    };

    @Override public boolean onTouchEvent(MotionEvent event) {this.mDetector.onTouchEvent(event);return super.onTouchEvent(event);}
    @Override public void onShowPress(MotionEvent e) {}
    @Override public boolean onSingleTapUp(MotionEvent e) {return false;}
    @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}
    @Override public void onLongPress(MotionEvent e) {}
    @Override public boolean onDown(MotionEvent e) {Toast.makeText(getApplicationContext(), "Touch", Toast.LENGTH_SHORT).show();return true;}
    @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(getApplicationContext(), "Fling", Toast.LENGTH_SHORT).show();
        if (Math.abs(e1.getX() - e2.getX()) < 250 && (e1.getY() - e2.getY() > 0)) {
            //위로 드래그
            Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
            switch (Input.countCheck()) {
                case 0:
                    ind_one.setBackgroundColor(Integer.parseInt("#222222"));
                    break;
                case 1:
                    ind_two.setBackgroundColor(Integer.parseInt("#222222"));
                    break;
                case 2:
                    ind_three.setBackgroundColor(Integer.parseInt("#222222"));
                    break;
                case 3:
                    ind_four.setBackgroundColor(Integer.parseInt("#222222"));
                    break;
                case 4:
                    ind_five.setBackgroundColor(Integer.parseInt("#222222"));
                    break;
                case 5:
                    ind_six.setBackgroundColor(Integer.parseInt("#222222"));
                    break;
            }
            Input.recGesture(true);
        } else if (Math.abs(e1.getX() - e2.getX()) < 250 && (e2.getY() - e1.getY() > 0)) {
            //아래로 드래그
            Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
            countBraille.setText(Integer.toString((Input.countCheck())));
            switch (Input.countCheck()) {
                case 0:
                    ind_one.setBackgroundColor(Integer.parseInt("#eeeeee"));
                    break;
                case 1:
                    ind_two.setBackgroundColor(Integer.parseInt("#eeeeee"));
                    break;
                case 2:
                    ind_three.setBackgroundColor(Integer.parseInt("#eeeeee"));
                    break;
                case 3:
                    ind_four.setBackgroundColor(Integer.parseInt("#eeeeee"));
                    break;
                case 4:
                    ind_five.setBackgroundColor(Integer.parseInt("#eeeeee"));
                    break;
                case 5:
                    ind_six.setBackgroundColor(Integer.parseInt("#eeeeee"));
                    break;
            }
            Input.recGesture(false);
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e1.getX() - e2.getX() > 0)) {
            //왼쪽 드래그
            Toast.makeText(getApplication(), " left : erase ", Toast.LENGTH_SHORT).show();
            eraseText();
        } else if (Math.abs(e1.getY() - e2.getY()) < 250 && (e2.getX() - e1.getX() > 0)) {
            //오른쪽 드래그
            Toast.makeText(getApplicationContext(), " right : send SMS ", Toast.LENGTH_SHORT).show();
            sendSMS(smsText.getText().toString());
        } else if ((e2.getX() - e1.getX() > 0) && (e2.getY() - e1.getY() > 0)) {
            //오른쪽 아래 대각선 드래그
            Toast.makeText(this, "right/down : stt-on ", Toast.LENGTH_SHORT).show();
            init();
            initSTT();
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            Toast.makeText(this, "left/up : erase-all ", Toast.LENGTH_SHORT).show();
            initText();
        } else {
            Toast.makeText(getApplication(), "nothing on gesture", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
