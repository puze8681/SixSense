package com.example.parktaejun.sixsense;


import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parktaejun.sixsense.databinding.ActivityMainBinding;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener { //

    ActivityMainBinding mainBinding;

    private GestureDetectorCompat mDetector;
    private EditText txtTest;
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
    private static TextView text;
    public static Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toast.makeText(getApplicationContext(),"Run",Toast.LENGTH_SHORT).show();

        txtTest = mainBinding.txtTest;
        txtResult = mainBinding.txtResult;
        countBraille = mainBinding.countBraille;
        ind_one = mainBinding.indexOne;
        ind_two = mainBinding.indexTwo;
        ind_three = mainBinding.indexThree;
        ind_four = mainBinding.indexFour;
        ind_five = mainBinding.indexFive;
        ind_six = mainBinding.indexSix;
        text = mainBinding.showText;
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Toast.makeText(getApplicationContext(),txtTest.getText(),Toast.LENGTH_SHORT).show();
                String returnValue = "";
                if(Hangul.IsHangul(txtTest.getText().toString())) {
                    for (int i = 0; i < txtTest.getText().length(); i++) {
                        String alphabet = Hangul.HangulAlphabet(Hangul.split(txtTest.getText().charAt(i)));
                        returnValue += alphabet;
                    }
                    txtResult.setText(returnValue);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        txtTest.addTextChangedListener(textWatcher);
        mainBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrate.makeVibe();
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
        if(Math.abs(e1.getX() - e2.getX()) < 250) {
            if (e1.getY() - e2.getY() > 0) {
                Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
                countBraille.setText(Integer.toString((Braille.countCheck())));
                dragUpDown = true;
                switch (Braille.countCheck()){
                    case 1:
                        ind_one.setText("UP");
                        break;
                    case 2:
                        ind_two.setText("UP");
                        break;
                    case 3:
                        ind_three.setText("UP");
                        break;
                    case 4:
                        ind_four.setText("UP");
                        break;
                    case 5:
                        ind_five.setText("UP");
                        break;
                    case 6:
                        ind_six.setText("UP");
                        break;
                }
                Braille.recGesture(dragUpDown);

            } else if (e2.getY() - e1.getY() > 0) {
                Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
                countBraille.setText(Integer.toString((Braille.countCheck())));
                dragUpDown = false;
                switch (Braille.countCheck()){
                    case 1:
                        ind_one.setText("DOWN");
                        break;
                    case 2:
                        ind_two.setText("DOWN");
                        break;
                    case 3:
                        ind_three.setText("DOWN");
                        break;
                    case 4:
                        ind_four.setText("DOWN");
                        break;
                    case 5:
                        ind_five.setText("DOWN");
                        break;
                    case 6:
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
    public static void addText(char t){
        text.setText(text.getText().toString()+t);
    }

    //점자를 초기화함
    public static void initBraille(){
        ind_one.setText("");
        ind_two.setText("");
        ind_three.setText("");
        ind_four.setText("");
        ind_five.setText("");
        ind_six.setText("");
    }
}