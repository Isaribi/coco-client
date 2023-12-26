package com.ndurska.coco_client.calendar.reminders;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ndurska.coco_client.R;

public class RemindersActivity extends AppCompatActivity implements SendRemindersFragment.SendRemindersListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SendRemindersFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onBtnSaveClicked() {
        finish();
    }
}