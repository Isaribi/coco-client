package com.ndurska.coco_client.summary;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.CalendarUtils;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.calendar.appointment.web.AppointmentsRequestDispatcher;

import java.util.List;

/**
 * A fragment containing recycler view of appointments for selected day allowing the user to enter payment and absence information for each client.
 */
public class AppointmentSummaryFragment extends DialogFragment implements AppointmentSummaryAdapter.SummaryListener {

    private AppointmentSummaryAdapter adapter;
    private TextView tvDate, tvTotal;
    private Button btnPrevDay, btnNextDay, btnSave;
    private AppointmentSummaryListener listener;
    private RecyclerView rvAppointmentList;
    private AppointmentsRequestDispatcher appointmentsRequestDispatcher;
    private DailySummaryActivity activity;

    interface AppointmentSummaryListener {
        void onBtnSaveClicked();
    }

    public static AppointmentSummaryFragment newInstance() {
        AppointmentSummaryFragment fragment = new AppointmentSummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AppointmentSummaryFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (DailySummaryActivity) context;
        if (context instanceof AppointmentSummaryListener) {
            listener = (AppointmentSummaryListener) context;
        } else
            throw new RuntimeException(context + " " + R.string.must_implement + "Appointment Summary Listener");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentsRequestDispatcher = new AppointmentsRequestDispatcher(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        executorService.execute(() -> {
            initViews(view);
            updateTextAndAdapter();
            setListeners();

            super.onViewCreated(view, savedInstanceState);
        });
    }

    private void initViews(@NonNull View view) {
        rvAppointmentList = view.findViewById(R.id.rvSummary);
        rvAppointmentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvDate = view.findViewById(R.id.tvRemindersDate);
        btnPrevDay = view.findViewById(R.id.btnPrevDay);
        btnNextDay = view.findViewById(R.id.btnNextDay);
        btnSave = view.findViewById(R.id.btnSummarySave);
        tvTotal = view.findViewById(R.id.tvTotal);
    }

    private void updateTextAndAdapter() {
        List<AppointmentDto> appointments = appointmentsRequestDispatcher.getAppointmentsForTheDay(CalendarUtils.selectedDate);
        adapter = new AppointmentSummaryAdapter(getContext(), appointments, this);
        activity.runOnUiThread(() -> {
            rvAppointmentList.setAdapter(adapter);
            tvTotal.setText(String.valueOf(adapter.getAppointmentsTotal()));
            tvDate.setText(CalendarUtils.selectedDate.toString());
        });
    }

    private void setListeners() {

        btnPrevDay.setOnClickListener(view1 -> executorService.execute(() -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
            updateTextAndAdapter();
        }));
        btnNextDay.setOnClickListener(view1 -> executorService.execute(() -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
            updateTextAndAdapter();
        }));
        btnSave.setOnClickListener(view1 -> {
            List<AppointmentDto> appointmentList = adapter.getAppointments();
            executorService.execute(()->{
                for (AppointmentDto app : appointmentList) {
                    appointmentsRequestDispatcher.editAppointment(app);
                }
                dismiss();
            });
            listener.onBtnSaveClicked();
        });
    }

    public void updateTotal(int newTotal) {
        tvTotal.setText(String.valueOf(newTotal));
    }
}