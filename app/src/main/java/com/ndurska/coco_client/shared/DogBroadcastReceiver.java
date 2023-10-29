package com.ndurska.coco_client.shared;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DogBroadcastReceiver extends BroadcastReceiver {

        public DogBroadcastReceiver(){
            super();
        };

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("dogs");
            Toast.makeText(context,data,Toast.LENGTH_LONG).show();
        }
    }