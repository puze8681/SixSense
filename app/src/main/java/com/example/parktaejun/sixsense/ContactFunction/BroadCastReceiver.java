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

import com.example.parktaejun.sixsense.PhoneBookActivity;
import com.example.parktaejun.sixsense.PushActivity;
import com.example.parktaejun.sixsense.SMSContentActivity;
import com.example.parktaejun.sixsense.SendMessageActivity;

public class BroadCastReceiver extends BroadcastReceiver {
    public static String smsText;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
        String str = ""; // 출력할 문자열 저장
        if (bundle != null) { // 수신된 내용이 있으면
            // 실제 메세지는 Object타입의 배열에 PDU 형식으로 저장됨
            Object [] pdus = (Object[])bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage
                        .createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getOriginatingAddress()
                        + "에게 문자왔음, " +
                        msgs[i].getMessageBody().toString()
                        +"\n";
            }
            smsText = str;
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            Intent pushIntent = new Intent(context, PushActivity.class);
            context.startActivity(pushIntent);
        }
        if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    PhoneBookActivity.setEarPlugMode(false);
                    SMSContentActivity.setEarPlugMode(false);
                    Log.d("111", "Headset is unplugged");
                    break;
                case 1:
                    PhoneBookActivity.setEarPlugMode(true);
                    SMSContentActivity.setEarPlugMode(true);
                    Log.d("111", "Headset is plugged");
                    break;
                default:
                    Log.d("111", "I have no idea what the headset state is");
            }
        }else if(action.equals("SMS_SENT_ACTION")){
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
        }else if(action.equals("SMS_DELIVERED_ACTION")){
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