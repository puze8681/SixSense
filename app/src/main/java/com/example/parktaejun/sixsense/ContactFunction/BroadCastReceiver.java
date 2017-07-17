package com.example.parktaejun.sixsense.ContactFunction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.parktaejun.sixsense.PhoneBookActivity;
import com.example.parktaejun.sixsense.PushActivity;
import com.example.parktaejun.sixsense.SMSContentActivity;

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
            Toast.makeText(context,
                    str, Toast.LENGTH_LONG).show();
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
        }
    }
}