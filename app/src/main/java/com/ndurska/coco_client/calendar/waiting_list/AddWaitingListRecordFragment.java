package com.ndurska.coco_client.calendar.waiting_list;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.CalendarActivity;
import com.ndurska.coco_client.calendar.CalendarUtils;
import com.ndurska.coco_client.database.DogDto;
import com.ndurska.coco_client.shared.ChooseDogAdapter;
import com.ndurska.coco_client.shared.DogFilter;
import com.ndurska.coco_client.shared.RequestDispatcher;
import com.ndurska.coco_client.shared.TextWatcherAdapter;

import java.time.LocalDate;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddWaitingListRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddWaitingListRecordFragment extends DialogFragment {

    private LocalDate dateStart;
    private LocalDate dateEnd;

    private TextView tvDateStart;
    private TextView tvDateEnd;
    private TextView etNotes;
    private TextView etClientSearch;
    private RecyclerView rvClientList;
    private Button btnSave;
    private ImageButton ibDateStart;
    private ImageButton ibDateEnd;

    private List<DogDto> dogs;
    private RequestDispatcher requestDispatcher;
    private NewWaitingListRecordListener listener;

    public interface NewWaitingListRecordListener {
        void onNewRecordAdded();
    }


    public AddWaitingListRecordFragment() {
        // Required empty public constructor
    }


    public static AddWaitingListRecordFragment newInstance() {
        return new AddWaitingListRecordFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NewWaitingListRecordListener) {
            listener = (NewWaitingListRecordListener) context;
        } else
            throw new RuntimeException(context + getString(R.string.must_implement) + " CreateAppointmentFragment.EditAppointmentListener");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestDispatcher = new RequestDispatcher(getActivity());
        dateStart = LocalDate.now();
        dateEnd = LocalDate.now().plusMonths(1);
        //todo thread
        //clients = requestDispatcher.getClients();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_waiting_list_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        initViews();
        setListeners();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setListeners() {
        setSearchBarListener();
        setDatePickersListeners();
        setSaveButtonListener();
    }

    private void findViews(@NonNull View view) {
        tvDateStart = view.findViewById(R.id.tvAddWaitingListRecordDateStart);
        tvDateEnd = view.findViewById(R.id.tvAddWaitingListRecordDateEnd);
        etNotes = view.findViewById(R.id.etWaitingListRecordNotes);
        etClientSearch = view.findViewById(R.id.etClientSearchWaiting);
        rvClientList = view.findViewById(R.id.rvClientListWaiting);
        btnSave = view.findViewById(R.id.btnAddWaitingListRecord);
        ibDateStart = view.findViewById(R.id.ibWaitingDateStart);
        ibDateEnd = view.findViewById(R.id.ibWaitingDateEnd);
    }

    private void initViews() {
        rvClientList.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvDateStart.setText(CalendarUtils.dayMonthFromDate(dateStart));
        tvDateEnd.setText(CalendarUtils.dayMonthFromDate(dateEnd));

    }

    private void setSearchBarListener() {
        DogFilter filter = new DogFilter(dogs);
        etClientSearch.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void afterTextChanged(Editable editable) {
                List<DogDto> searchResults = filter.dogsContaining(String.valueOf(editable));
                ChooseDogAdapter adapter = new ChooseDogAdapter(searchResults, getContext());
                rvClientList.setAdapter(adapter);
            }
        });
    }

    private void setDatePickersListeners() {

        int currentYear = dateStart.getYear();
        int currentMonth = dateStart.getMonthValue() - 1;
        int nextMonth = dateEnd.getMonthValue() - 1;
        int currentDay = dateStart.getDayOfMonth();

        ibDateStart.setOnClickListener(view -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (datePicker, year1, month1, day1) -> {
                        dateStart = LocalDate.of(year1, month1 + 1, day1);
                        tvDateStart.setText(CalendarUtils.dayMonthFromDate(dateStart));
                    },
                    currentYear,
                    currentMonth,
                    currentDay
            );
            datePickerDialog.show();
        });
        ibDateEnd.setOnClickListener(view -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (datePicker, year1, month1, day1) -> {
                        dateEnd = LocalDate.of(year1, month1 + 1, day1);
                        tvDateEnd.setText(CalendarUtils.dayMonthFromDate(dateEnd));
                    },
                    currentYear,
                    nextMonth,
                    currentDay
            );
            datePickerDialog.show();
        });
    }

    private void setSaveButtonListener() {
        btnSave.setOnClickListener(view -> {
                    CalendarActivity activity = (CalendarActivity) getActivity();
                    if ((activity != null ? activity.getActiveDog() : null) != null) {
                        if (validateDates()) {
                            DogDto client = activity.getActiveDog();
                            WaitingListRecord waitingListRecord = new WaitingListRecord(
                                    client.getId(),
                                    dateStart,
                                    dateEnd,
                                    etNotes.getText().toString()
                            );
                            //todo thread
//                            if (requestDispatcher.addRecordToWaitingList(waitingListRecord)) {
//                                dismiss();
//                                listener.onNewRecordAdded();
//                                Toast.makeText(activity, R.string.dog_added_to_waiting_list + "", Toast.LENGTH_LONG).show();
//                            } else
//                                Toast.makeText(activity, R.string.adding_dog_to_waiting_list_failed + "", Toast.LENGTH_LONG).show();
                        }
                    } else
                        Toast.makeText(activity, R.string.choose_dog_warning + "", Toast.LENGTH_LONG).show();

                }
        );
    }

    private boolean validateDates() {
        if (dateEnd.isBefore(dateStart)) {
            tvDateEnd.setError(R.string.lock_date_end_before_start_warning + "");
            return false;
        }
        return true;
    }
}