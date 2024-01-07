package com.ndurska.coco_client.database;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.calendar.appointment.web.AppointmentsRequestDispatcher;
import com.ndurska.coco_client.database.dto.DogDto;

import java.util.List;


public class ShowAppointments extends DialogFragment {
    ShowAppointmentsAdapter adapter;
    TextView tvClientName;
    private List<AppointmentDto> appointments;
    RecyclerView rvAppointments;
    DogDto dogDto;
    private AppointmentsRequestDispatcher appointmentsRequestDispatcher;

    public ShowAppointments() {
        // Required empty public constructor
    }


    public static ShowAppointments newInstance(DogDto dogDto) {
        ShowAppointments fragment = new ShowAppointments();
        Bundle args = new Bundle();
        args.putSerializable("dogDto", dogDto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appointmentsRequestDispatcher = new AppointmentsRequestDispatcher(getContext());
            dogDto = (DogDto) getArguments().getSerializable("dogDto");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvAppointments = view.findViewById(R.id.rvShowAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvClientName = view.findViewById(R.id.tvName);
        executorService.execute(() -> {
            appointments = appointmentsRequestDispatcher.getAppointmentsForDog(dogDto.getId());
            adapter = new ShowAppointmentsAdapter(getContext(), appointments);
            getActivity().runOnUiThread(() -> rvAppointments.setAdapter(adapter));
            tvClientName.setText(dogDto.getName());
            super.onViewCreated(view, savedInstanceState);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_appointments, container, false);
    }
}