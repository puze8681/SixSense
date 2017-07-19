package com.example.parktaejun.sixsense.SMSFunction;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parktaejun.sixsense.Font.Font;
import com.example.parktaejun.sixsense.GestureListener.GestureListenerReceiveMessageActivity;
import com.example.parktaejun.sixsense.IO.Input;
import com.example.parktaejun.sixsense.R;
import com.example.parktaejun.sixsense.databinding.ActivitySendMessageBinding;

import java.util.ArrayList;

public class SendMessageActivity extends Activity {

    static Context context;
    ActivitySendMessageBinding mainBinding;
    private static String number;
    public static TextView smsText;
    public static TextView countBraille;
    static Intent sttIntent;
    static SpeechRecognizer stt;
    private static TextView ind_one;
    private static TextView ind_two;
    private static TextView ind_three;
    private static TextView ind_four;
    private static TextView ind_five;
    private static TextView ind_six;

    LinearLayout gestureOverlay;
    GestureListenerReceiveMessageActivity gestureListener;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        initApp();
    }

    private void initApp(){
        Font.setGlobalFont(this, getWindow().getDecorView());

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

        Intent getIntent = new Intent();
        String number = getIntent.getExtras().getString("number");
        setNumber(number);

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
        initGesture();
        initSTT();
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

    private void initGesture() { //activity gesture recognition
        gestureOverlay = (LinearLayout) findViewById(R.id.activity_send_message);
        gestureListener = new GestureListenerReceiveMessageActivity(context, gestureDetector);
        if (gestureOverlay == null) {
            Toast.makeText(context, "gestureOverlay object is null bro", Toast.LENGTH_SHORT).show();
        }
        gestureOverlay.setOnTouchListener(gestureListener);
    }


    public static void initSTT(){
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        stt = SpeechRecognizer.createSpeechRecognizer(context);
        stt.setRecognitionListener(listener);
        stt.startListening(sttIntent);
    }

    private void setNumber(String n){
        number = n;
    }

    private static String getNumber(){
        return number;
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

    public static void eraseText() {
        smsText.setText(smsText.getText().toString().substring(0, smsText.getText().toString().length()-2));
        initBraille();
        Input.init();
        countBraille.setText("0");
    }

    public static void initText(){
        smsText.setText("");
        initBraille();
        Input.init();
        countBraille.setText("0");
    }

    public static void init(){
        smsText.setText(smsText.getText().toString());
        initBraille();
        Input.init();
        countBraille.setText("0");
    }

    public static void sendSMS(String s) {
        String smsNum = getNumber();
        String smsText = s;

        if (smsNum.length() > 0 && smsText.length() > 0) {
            sendSMS(smsNum, smsText);
        } else {
            Toast.makeText(context, "모두 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendSMS(String smsNumber, String smsText) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }

    public static void setBraiile(int n, char b){
        countBraille.setText(Integer.toString(n+1));
        switch (b){
            case 'u':
                switch (n){
                    case 0:ind_one.setBackgroundColor(Integer.parseInt("#222222"));
                        break;
                    case 1:ind_two.setBackgroundColor(Integer.parseInt("#222222"));
                        break;
                    case 2:ind_three.setBackgroundColor(Integer.parseInt("#222222"));
                        break;
                    case 3:ind_four.setBackgroundColor(Integer.parseInt("#222222"));
                        break;
                    case 4:ind_five.setBackgroundColor(Integer.parseInt("#222222"));
                        break;
                    case 5:ind_six.setBackgroundColor(Integer.parseInt("#222222"));
                        break;
                    default:
                        break;
                }
                break;
            case 'd':
                switch (n){
                    case 1:ind_one.setBackgroundColor(Integer.parseInt("#eeeeee"));
                        break;
                    case 2:ind_two.setBackgroundColor(Integer.parseInt("#eeeeee"));
                        break;
                    case 3:ind_three.setBackgroundColor(Integer.parseInt("#eeeeee"));
                        break;
                    case 4:ind_four.setBackgroundColor(Integer.parseInt("#eeeeee"));
                        break;
                    case 5:ind_five.setBackgroundColor(Integer.parseInt("#eeeeee"));
                        break;
                    case 6:ind_six.setBackgroundColor(Integer.parseInt("#eeeeee"));
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private static RecognitionListener listener = new RecognitionListener() {
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
}
