package com.ndurska.coco_client.calendar.reminders;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.CalendarUtils;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.calendar.appointment.web.AppointmentsRequestDispatcher;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendRemindersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendRemindersFragment extends DialogFragment {

    TextView tvDate;
    Button btnSend;
    SendRemindersAdapter adapter;
    RecyclerView rvReminderList;
    private List<AppointmentDto> appointments;
    SendRemindersListener listener;

    private AppointmentsRequestDispatcher appointmentsRequestDispatcher;


    public SendRemindersFragment() {
        // Required empty public constructor
    }

    public static SendRemindersFragment newInstance() {
        SendRemindersFragment fragment = new SendRemindersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentsRequestDispatcher = new AppointmentsRequestDispatcher();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SendRemindersListener) {
            listener = (SendRemindersListener) context;
        } else
            throw new RuntimeException(context + " " + R.string.must_implement + " SendRemindersListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tvDate = view.findViewById(R.id.tvRemindersDate);
        btnSend = view.findViewById(R.id.btnSend);
        rvReminderList = view.findViewById(R.id.rvReminders);
        rvReminderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        LocalDate date = CalendarUtils.selectedDate.plusDays(1);
        executorService.execute(() -> {
            appointments = appointmentsRequestDispatcher.getAppointmentsForTheDay(date);
            adapter = new SendRemindersAdapter(getActivity(), appointments);
            rvReminderList.setAdapter(adapter);

            tvDate.setText(CalendarUtils.dayMonthFromDate(date));

            setButtonListener();

            super.onViewCreated(view, savedInstanceState);
        });
    }

    private void setButtonListener() {
        btnSend.setOnClickListener(view1 -> {
            try {
                Map<String, String> remindersList = adapter.getRemindersList();
                for (Map.Entry<String, String> entry : remindersList.entrySet()) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(entry.getKey(), null, entry.getValue(), null, null);
                    Toast.makeText(getActivity(), R.string.message_sent, Toast.LENGTH_LONG).show();
                }
                listener.onBtnSaveClicked();
                dismiss();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }


    interface SendRemindersListener {
        void onBtnSaveClicked();
    }
}