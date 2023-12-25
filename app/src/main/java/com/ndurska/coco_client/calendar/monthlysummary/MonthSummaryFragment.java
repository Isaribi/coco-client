package com.ndurska.coco_client.calendar.monthlysummary;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.CalendarUtils;

import java.io.Serializable;
import java.time.LocalDate;


public class MonthSummaryFragment extends DialogFragment {


    private static final String ARG_MONTH_SUMMARY_DTO = "monthSummaryDTO";

    private MonthSummaryDTO monthSummaryDTO;

    private TextView tvDate, tvNumberOfAppointments, tvNumberOfAbsentClients,
            tvNumberOfFirstTimeClients, tvAmountPaid,
            tvPredictedAmount, tvSum;
    private Button btnPreviousMonth, btnNextMonth;


    private LocalDate date;

    public MonthSummaryFragment() {
        // Required empty public constructor
    }


    public static MonthSummaryFragment newInstance(MonthSummaryDTO monthlySummary) {
        MonthSummaryFragment fragment = new MonthSummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MONTH_SUMMARY_DTO, (Serializable) monthlySummary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            monthSummaryDTO = (MonthSummaryDTO) getArguments().getSerializable(ARG_MONTH_SUMMARY_DTO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finanses, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        displayData();
        setListeners();
    }

    private void setListeners() {
        btnPreviousMonth.setOnClickListener(view -> {
                    date = date.minusMonths(1);
                    executorService.execute(() -> {
                       // monthSummaryDTO = monthSummaryService.getSummaryForMonth(date);
                        displayData();
                    });
                }
        );
        btnNextMonth.setOnClickListener(view -> {
                    date = date.plusMonths(1);
                    executorService.execute(() -> {
                        //todo add this
                       // monthSummaryDTO = monthSummaryService.getSummaryForMonth(date);
                        displayData();
                    });
                }
        );
    }

    private void initViews(View view) {
        tvDate = view.findViewById(R.id.tvDate);
        tvNumberOfAppointments = view.findViewById(R.id.tvNumberOfAppointments);
        tvNumberOfAbsentClients = view.findViewById(R.id.tvNumberOfAbsentClients);
        tvNumberOfFirstTimeClients = view.findViewById(R.id.tvNumberOfFirstTimeClients);
        tvAmountPaid = view.findViewById(R.id.tvAmountPaid);
        tvPredictedAmount = view.findViewById(R.id.tvPredictedAmount);
        tvSum = view.findViewById(R.id.tvSum);
        btnNextMonth = view.findViewById(R.id.btnNextMonth);
        btnPreviousMonth = view.findViewById(R.id.btnPreviousMonth);
    }

    private void displayData() {
        tvDate.setText(CalendarUtils.monthYearFromDate(monthSummaryDTO.getDate()));
        tvNumberOfAppointments.setText(String.valueOf(monthSummaryDTO.getNumberOfAppointments()));
        tvNumberOfAbsentClients.setText(String.valueOf(monthSummaryDTO.getNumberOfAbsentClients()));
        tvNumberOfFirstTimeClients.setText(String.valueOf(monthSummaryDTO.getNumberOfClientsWithNoLastPayment()));
        tvAmountPaid.setText(String.valueOf(monthSummaryDTO.getAmountPaidThisMonth()));
        tvPredictedAmount.setText(String.valueOf(monthSummaryDTO.getAmountPredicted()));
        tvSum.setText(String.valueOf(monthSummaryDTO.getAmountPaidThisMonth() + monthSummaryDTO.getAmountPredicted()));
    }
}