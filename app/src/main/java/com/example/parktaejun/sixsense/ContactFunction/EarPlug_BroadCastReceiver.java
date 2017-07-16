package com.example.parktaejun.sixsense.ContactFunction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by parktaejun on 2017. 7. 17..
 */

public class EarPlug_BroadCastReceiver extends BroadcastReceiver{
    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Log.d("111", "Headset is unplugged");
                    break;
                case 1:
                    Log.d("111", "Headset is plugged");
                    break;
                default:
                    Log.d("111", "I have no idea what the headset state is");
            }
        }
    }
}
