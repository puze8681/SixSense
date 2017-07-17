package com.example.parktaejun.sixsense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PushActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        Intent getIntent = new Intent();
        String time = getIntent.getExtras().getString("time");
        String number = getIntent.getExtras().getString("name");
        String person = getIntent.getExtras().getString("person");
        String body = getIntent.getExtras().getString("body");

    }
}
