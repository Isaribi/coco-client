package com.ndurska.coco_client.summary;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ndurska.coco_client.R;

public class DailySummaryActivity extends AppCompatActivity implements AppointmentSummaryFragment.AppointmentSummaryListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_summary);
        if (savedInstanceState == null) {
            executorService.execute(()-> getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AppointmentSummaryFragment.newInstance())
                    .commit());
        }
    }

    @Override
    public void onBtnSaveClicked() {
        finish();
    }
}