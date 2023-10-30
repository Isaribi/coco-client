package com.ndurska.coco_client.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.Appointment;
import com.ndurska.coco_client.calendar.appointment.AppointmentAdapter;
import com.ndurska.coco_client.calendar.appointment.AppointmentCell;
import com.ndurska.coco_client.calendar.unavailable_period.UnavailablePeriod;
import com.ndurska.coco_client.database.DogDto;
import com.ndurska.coco_client.shared.RequestDispatcher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;


public class CalendarDay extends Fragment {

    LocalDate date;
    TextView tvDay;
    LinearLayout llLabels;
    List<Appointment> appointments;
    List<DogDto> dogs;
    List<UnavailablePeriod> unavailablePeriods;
    private AppointmentAdapterListener listener;

    public interface AppointmentAdapterListener {
        boolean onDayLabelLongClicked(View view, LocalDate date);
    }

    public CalendarDay() {
        // Required empty public constructor
    }

    public LocalDate getDate() {
        return date;
    }

    public static CalendarDay newInstance(LocalDate date) {
        CalendarDay fragment = new CalendarDay();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            date = (LocalDate) getArguments().getSerializable("date");
        }
        findAppointmentsForTheDay();
    }

    private void findAppointmentsForTheDay() {
        RequestDispatcher dbHelper = new RequestDispatcher(getActivity());
        try {
            //todo thread
            //appointments = dbHelper.getAppointmentsForTheDay(date);
            //unavailablePeriods = dbHelper.getUnavailablePeriodsForTheDay(date);
            appointments = new ArrayList<>();
            unavailablePeriods = new ArrayList<>();
            List<Integer> IDs = new ArrayList<>();
            for (Appointment appointment : appointments)
                IDs.add(appointment.getClientID());
            if (IDs.size() > 0){}
              //  dogs = dbHelper.getClientsByIDs(IDs);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "CalendarDay.OnCreate(): " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (AppointmentAdapterListener) getContext();
        tvDay = view.findViewById(R.id.header);
        String day = CalendarUtils.dayMonthFromDate(date) + " - " + date.getDayOfWeek().getDisplayName(TextStyle.FULL, CalendarUtils.locale);
        tvDay.setText(day);
        tvDay.setOnLongClickListener(view1 -> listener.onDayLabelLongClicked(tvDay,date));

    }

    private ArrayList<AppointmentCell> getAppointmentCellList() {
        ArrayList<AppointmentCell> list = new ArrayList<>();
        AppointmentCell.setAppointments((ArrayList<Appointment>) appointments);
        AppointmentCell.setDogs((ArrayList<DogDto>) dogs);
        AppointmentCell.setUnavailablePeriods((ArrayList<UnavailablePeriod>) unavailablePeriods);

        for (int hour = 9; hour <= 19; hour++) {
            for (int minutes = 0; minutes <= 30; minutes += 30) {
                LocalTime time = LocalTime.of(hour, minutes);
                AppointmentCell cell = new AppointmentCell(date, time);

                list.add(cell);
            }
        }
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_day, container, false);
        llLabels = view.findViewById(R.id.llLabels);
        setAdapters();
        return view;
    }

    private void setAdapters() {
        setCellAdapter();
    }

    private void setCellAdapter() {
        ArrayList<AppointmentCell> listOfCells = getAppointmentCellList();
        AppointmentAdapter adapter = new AppointmentAdapter(this.requireContext(), listOfCells, appointments);
        final int adapterCount = adapter.getCount();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;

        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            item.setLayoutParams(params);
            item.setPadding(5, 0, 0, 0);
            llLabels.addView(item);
        }
    }
}