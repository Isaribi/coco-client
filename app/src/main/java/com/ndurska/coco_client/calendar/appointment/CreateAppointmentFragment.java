package com.ndurska.coco_client.calendar.appointment;

import static com.ndurska.coco_client.calendar.CalendarActivity.executorService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.CalendarActivity;
import com.ndurska.coco_client.calendar.CalendarUtils;
import com.ndurska.coco_client.calendar.ChooseMultipleClientsAdapter;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.calendar.appointment.web.AppointmentsRequestDispatcher;
import com.ndurska.coco_client.calendar.waiting_list.WaitingListRecordDto;
import com.ndurska.coco_client.calendar.waiting_list.WaitingListRequestDispatcher;
import com.ndurska.coco_client.database.DatabaseActivity;
import com.ndurska.coco_client.database.dto.DogDto;
import com.ndurska.coco_client.database.web.DogsRequestDispatcher;
import com.ndurska.coco_client.shared.ChooseDogAdapter;
import com.ndurska.coco_client.shared.DogFilter;
import com.ndurska.coco_client.shared.TextWatcherAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CreateAppointmentFragment extends DialogFragment {

    public static final String APPOINTMENT = "appointmentDto";
    private final int LAUNCH_ACTIVITY_TO_CREATE_CLIENT = 1;

    private CalendarActivity activity;
    private DogDto dog;
    private AppointmentDto appointmentDto;
    private LocalDate date;
    private LocalTime time;
    private List<DogDto> dogs;
    private List<WaitingListRecordDto> waitingListForTheDay;
    private TextView etClientSearch, etAppointmentNotes;
    private TextView tvPhoneLabel1, tvPhoneLabel2;
    private CheckBox cbPhone1, cbPhone2;
    private RecyclerView rvClientList;
    private Button btnSave;
    private Button btnAddNewClient;


    private EditAppointmentListener listener;
    private DogsRequestDispatcher dogsRequestDispatcher;
    private AppointmentsRequestDispatcher appointmentsRequestDispatcher;
    private WaitingListRequestDispatcher waitingListRequestDispatcher;

    public void newClientCreated(DogDto dog) {
        dogs.add(dog);
        refreshClientSetResults(dog.clientFullName());
        etClientSearch.setText(dog.clientFullName());
    }

    public interface EditAppointmentListener {
        void refreshWeekView();
    }

    public CreateAppointmentFragment() {
        // Required empty public constructor
    }

    public static CreateAppointmentFragment newInstance(AppointmentDto appointmentDto) {
        CreateAppointmentFragment fragment = new CreateAppointmentFragment();
        Bundle args = new Bundle();
        args.putSerializable(APPOINTMENT, appointmentDto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditAppointmentListener) {
            listener = (EditAppointmentListener) context;
        } else
            throw new RuntimeException(context + getString(R.string.must_implement) + " CreateAppointmentFragment.EditAppointmentListener");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dogsRequestDispatcher = new DogsRequestDispatcher();
        appointmentsRequestDispatcher = new AppointmentsRequestDispatcher();
        waitingListRequestDispatcher = new WaitingListRequestDispatcher();
        executorService.execute(
                () -> {
                    dogs = dogsRequestDispatcher.getDogs();
                    waitingListForTheDay = waitingListRequestDispatcher.getWaitingListForChosenDay(date);
                });
        if (getArguments() != null) {
            appointmentDto = (AppointmentDto) getArguments().getSerializable(APPOINTMENT);
            date = appointmentDto.getDate();
            time = appointmentDto.getTime();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return inflater.inflate(R.layout.fragment_create_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setListeners();
    }

    private void setListeners() {

        etClientSearch.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void afterTextChanged(Editable editable) {
                if (dogs != null) {
                    refreshClientSetResults(String.valueOf(editable));
                }
            }

        });

        btnSave.setOnClickListener(view1 -> {
                    activity = (CalendarActivity) getActivity();
                    if (activityHasActiveClient()) {
                        dog = activity.getActiveDog();
                        boolean savingSuccess = createAppointment(time, dog, etAppointmentNotes.getText().toString());
                        if (savingSuccess) {
                            checkIfClientIsOnWaitingList(dog);
                            sendTextWithAppointmentDate();
                            listener.refreshWeekView();

                            executorService.execute(() -> {
                                ArrayList<DogDto> sameOwnerDogs = (ArrayList<DogDto>) dogsRequestDispatcher.getSameOwnerDogs(dog.getId());

                                if (!sameOwnerDogs.isEmpty()) {
                                    activity.runOnUiThread(() -> {
                                        displayPromptForMoreAppointments();
                                        ChooseMultipleClientsAdapter adapter = new ChooseMultipleClientsAdapter(sameOwnerDogs, getContext());
                                        rvClientList.setAdapter(adapter);
                                        switchThisListenerToSavingMultipleClients(adapter);
                                    });
                                } else
                                    dismiss();
                            });
                        } else
                            dismiss();
                    } else
                        Toast.makeText(getActivity(), R.string.choose_dog_warning, Toast.LENGTH_SHORT).show();
                }
        );

        btnAddNewClient.setOnClickListener(view -> {
            Intent switchActivityIntent;
            switchActivityIntent = new Intent(getActivity(), DatabaseActivity.class);
            startActivityForResult(switchActivityIntent, LAUNCH_ACTIVITY_TO_CREATE_CLIENT);
        });
    }

    private void switchThisListenerToSavingMultipleClients(ChooseMultipleClientsAdapter adapter) {
        btnSave.setOnClickListener(view2 -> { //change save button behaviour to add chosen clients to appointments with 30 min intervals
            ArrayList<DogDto> chosenClients = (ArrayList<DogDto>) adapter.getChosenDogs();
            createAppointmentsForChosenDogs(chosenClients);
            dismiss();
            listener.refreshWeekView();
        });
    }

    private void createAppointmentsForChosenDogs(ArrayList<DogDto> chosenDogs) {
        LocalTime nextTime = time;
        for (DogDto nextDog : chosenDogs) {
            nextTime = nextTime.plusMinutes(30);
            boolean savingSuccess = createAppointment(nextTime, nextDog, null);
            if (savingSuccess) {
                checkIfClientIsOnWaitingList(nextDog);
            }
        }
    }

    private void displayPromptForMoreAppointments() {
        etAppointmentNotes.setText(R.string.appointment_more_dogs_question);
        etAppointmentNotes.setTextAppearance(R.style.Theme_CocoClient);
        etAppointmentNotes.setEnabled(false);
    }

    private boolean createAppointment(LocalTime time, DogDto dog, String etAppointmentNotes) {
        LocalTime appointmentEnd = time.plusMinutes(dog.getExpectedAppointmentDuration());
        if (appointmentEnd.isBefore(LocalTime.of(20, 1))) {
            appointmentDto.setDate(date);
            appointmentDto.setTime(time);
            appointmentDto.setDogDto(dog);
            appointmentDto.setNotes(etAppointmentNotes);
            executorService.execute(() -> {
                appointmentsRequestDispatcher.addAppointment(appointmentDto);
            });
            return true;
        } else {
            Toast.makeText(activity, "Wizyta skończy się zbyt późno!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean activityHasActiveClient() {
        return (activity != null ? activity.getActiveDog() : null) != null;
    }

    private void checkIfClientIsOnWaitingList(DogDto dog) {
        for (WaitingListRecordDto record : waitingListForTheDay) {
            if (Objects.equals(record.getDogDto().getId(), dog.getId())) {
                showConfirmationDialog(record, dog);
            }
        }
    }

    private void showConfirmationDialog(WaitingListRecordDto waitingListRecordDto, DogDto dto) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_from_waiting_list)
                .setMessage(dto.clientFullName() + getString(R.string.is_on_waiting_list))

                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    //todo thread
                    executorService.execute(() -> {
                        waitingListRequestDispatcher.deleteWaitingListRecord(waitingListRecordDto.getID());
                        activity.runOnUiThread(() -> listener.refreshWeekView());
                    });
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void refreshClientSetResults(String searchedText) {
        DogFilter filter = new DogFilter(dogs);
        List<DogDto> searchResults = filter.dogsContaining(searchedText);
        ChooseDogAdapter adapter = new ChooseDogAdapter(searchResults, getContext());
        rvClientList.setAdapter(adapter);
    }

    private void initViews(@NonNull View view) {
        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvTime = view.findViewById(R.id.tvTime);
        etClientSearch = view.findViewById(R.id.etClientSearchAppointment);
        rvClientList = view.findViewById(R.id.rvClientListAppointment);
        rvClientList.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnSave = view.findViewById(R.id.btnSaveAppointment);
        btnAddNewClient = view.findViewById(R.id.btnNewClient);
        etAppointmentNotes = view.findViewById(R.id.etAppointmentNotes);
        tvPhoneLabel1 = view.findViewById(R.id.tvAppointmentPhoneLabel1);
        tvPhoneLabel2 = view.findViewById(R.id.tvAppointmentPhoneLabel2);
        cbPhone1 = view.findViewById(R.id.cbAppointmentPhoneNumber1);
        cbPhone2 = view.findViewById(R.id.cbAppointmentPhoneNumber2);
        String displayedDate = CalendarUtils.dayMonthFromDate(date) + " - " + date.getDayOfWeek().getDisplayName(TextStyle.FULL, CalendarUtils.locale);
        tvDate.setText(displayedDate);
        tvTime.setText(time.toString());
        tvPhoneLabel1.setVisibility(View.INVISIBLE);
        cbPhone1.setVisibility(View.INVISIBLE);
        tvPhoneLabel2.setVisibility(View.INVISIBLE);
        cbPhone2.setVisibility(View.INVISIBLE);
    }

    private void sendTextWithAppointmentDate() {
        SmsManager smsManager = SmsManager.getDefault();
        String textMessage = getString(R.string.text_with_appointment_details, date, time);
        if (cbPhone1.isChecked()) {
            smsManager.sendTextMessage(dog.getPhoneNumber1(), null, textMessage, null, null);
            Toast.makeText(getActivity(), "" + R.string.message_sent,
                    Toast.LENGTH_LONG).show();
        }
        if (cbPhone2.isChecked()) {
            smsManager.sendTextMessage(dog.getPhoneNumber2(), null, textMessage, null, null);
            Toast.makeText(getActivity(), "" + R.string.message_sent,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void displayPhoneNumbers() {
        cbPhone1.setChecked(false);
        cbPhone2.setChecked(false);
        activity = (CalendarActivity) getActivity();

        if (activityHasActiveClient()) {
            dog = activity.getActiveDog();
            if (dog.getPhoneNumber1() != null) {
                tvPhoneLabel1.setVisibility(View.VISIBLE);
                cbPhone1.setVisibility(View.VISIBLE);
                tvPhoneLabel1.setText(dog.getPhoneNumberLabel1());
                cbPhone1.setText(dog.getPhoneNumber1());
            } else {
                tvPhoneLabel1.setVisibility(View.GONE);
                cbPhone1.setVisibility(View.GONE);
            }
            if (dog.getPhoneNumber2() != null && dog.getPhoneNumber2().length() > 0) {
                tvPhoneLabel2.setVisibility(View.VISIBLE);
                cbPhone2.setVisibility(View.VISIBLE);
                tvPhoneLabel2.setText(dog.getPhoneNumberLabel2());
                cbPhone2.setText(dog.getPhoneNumber2());
            } else {
                tvPhoneLabel2.setVisibility(View.GONE);
                cbPhone2.setVisibility(View.GONE);
            }
        }
    }
}
