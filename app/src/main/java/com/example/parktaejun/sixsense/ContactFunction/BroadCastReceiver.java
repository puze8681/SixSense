package com.example.parktaejun.sixsense.ContactFunction;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.parktaejun.sixsense.PhoneBook.PhoneBookData;
import com.example.parktaejun.sixsense.PhoneBookActivity;
import com.example.parktaejun.sixsense.PushActivity;
import com.example.parktaejun.sixsense.SMSContentActivity;
import com.example.parktaejun.sixsense.SendMessageActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class BroadCastReceiver extends BroadcastReceiver {

    PhoneBookData phoneBookData;
    private static ArrayList<PhoneBookData> PBitems = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
        if (bundle != null) { // 수신된 내용이 있으면

            PBitems = PhoneBookActivity.PBitems;

            // SMS 메시지를 파싱합니다.
            bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            // SMS 수신 시간 확인
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            String time = curDate.toString();
            Log.d("문자 발신 시간", curDate.toString());

            // SMS 발신 번호 확인
            String number = smsMessage[0].getOriginatingAddress();
            Log.d("문자 발신 번호", number);


            // SMS 발신자 이름 확인
            String person = "";
            for(int i = 0; i < PBitems.size(); i++){
                if(PBitems.get(i).getPhoneNum().equals(number)){
                    person = PBitems.get(i).getDisplayName();
                    break;
                }else{
                    person = "모르는 번호 입니다";
                }
            }

            // SMS 메시지 확인
            String message = smsMessage[0].getMessageBody().toString();
            Log.d("문자 수신 내용", message);

            Toast.makeText(context, "시간 : " + time + ", 발신자 : " + number + ", 내용 : " + message , Toast.LENGTH_LONG).show();

            Intent pushIntent = new Intent(context, PushActivity.class);
            pushIntent.putExtra("time", time);
            pushIntent.putExtra("number", number);
            pushIntent.putExtra("person", person);
            pushIntent.putExtra("body", message);
            context.startActivity(pushIntent);

        } else if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    PhoneBookActivity.setEarPlugMode(false);
                    SMSContentActivity.setEarPlugMode(false);
                    PushActivity.setEarPlugMode(false);
                    Log.d("111", "Headset is unplugged");
                    break;
                case 1:
                    PhoneBookActivity.setEarPlugMode(true);
                    SMSContentActivity.setEarPlugMode(true);
                    PushActivity.setEarPlugMode(false);
                    Log.d("111", "Headset is plugged");
                    break;
                default:
                    Log.d("111", "I have no idea what the headset state is");
            }
        } else if (action.equals("SMS_SENT_ACTION")) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // 전송 성공
                    Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    // 전송 실패
                    Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    // 서비스 지역 아님
                    Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    // 무선 꺼짐
                    Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    // PDU 실패
                    Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (action.equals("SMS_DELIVERED_ACTION")) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // 도착 완료
                    Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                    Intent scActivity = new Intent(context, SMSContentActivity.class);
                    context.startActivity(scActivity);
                    break;
                case Activity.RESULT_CANCELED:
                    // 도착 안됨
                    Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}