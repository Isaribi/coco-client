package com.ndurska.coco_client.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.AppointmentAdapter;
import com.ndurska.coco_client.calendar.appointment.CreateAppointmentFragment;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.calendar.appointment.web.AppointmentsRequestDispatcher;
import com.ndurska.coco_client.calendar.waiting_list.AddWaitingListRecordFragment;
import com.ndurska.coco_client.calendar.waiting_list.WaitingListFragment;
import com.ndurska.coco_client.calendar.waiting_list.WaitingListRecord;
import com.ndurska.coco_client.database.DatabaseActivity;
import com.ndurska.coco_client.database.dto.DogDto;
import com.ndurska.coco_client.database.web.DogsRequestDispatcher;
import com.ndurska.coco_client.shared.ChooseDogAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalendarActivity extends AppCompatActivity implements ChooseDogAdapter.ChooseDogAdapterListener,
        AppointmentAdapter.AppointmentAdapterListener,
        CreateAppointmentFragment.EditAppointmentListener,
        AddWaitingListRecordFragment.NewWaitingListRecordListener,
        CalendarDay.AppointmentAdapterListener {

    TextView tvMonthYear, tvWaitingListPreviewForDisplayedWeek;
    DogDto activeClient;

    ConstraintLayout monday;
    ConstraintLayout tuesday;
    ConstraintLayout wednesday;
    ConstraintLayout thursday;
    ConstraintLayout friday;
    ConstraintLayout saturday;

    Button nextWeek, nextMonth, prevWeek, prevMonth, btnDeleteAppointment, btnAddAppointment, btnNavigateToClient, btnMoveAppointmentUp, btnMoveAppointmentDown;
    ImageButton ibWaitingListForDisplayedWeek;

    CreateAppointmentFragment createAppointmentFragment;
    WaitingListFragment waitingListFragment;
    //MonthSummaryFragment monthSummaryFragment;

    List<WaitingListRecord> waitingListForShownWeek;
    DogsRequestDispatcher dogsRequestDispatcher;
    AppointmentsRequestDispatcher appointmentsRequestDispatcher;

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private float x1;
    private float y1;
    static final int MIN_SWIPE_DISTANCE = 100;
    private Button btnTimeBack;
    private Button btnLockDate;
    private Button btnTimeForward;
    private TextView etLockEndTime;

    public static ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);
        dogsRequestDispatcher = new DogsRequestDispatcher();
        appointmentsRequestDispatcher = new AppointmentsRequestDispatcher();
        executorService = Executors.newFixedThreadPool(10);
        findViews();
        initToolbar();
        setListeners();
        showCurrentWeek();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.calendar).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent switchActivityIntent;
        if (item.getItemId() == R.id.dog_database) {
            switchActivityIntent = new Intent(this, DatabaseActivity.class);
            startActivity(switchActivityIntent);
            return true;

//        }else if (item.getItemId() == R.id.summary) {
//            CalendarUtils.selectedDate = LocalDate.now();
//            switchActivityIntent = new Intent(this, DailySummaryActivity.class);
//            startActivity(switchActivityIntent);
//            return true;
//        } else if (item.getItemId() == R.id.send_text_reminders) {
//            CalendarUtils.selectedDate = LocalDate.now();
//            switchActivityIntent = new Intent(this, RemindersActivity.class);
//            startActivity(switchActivityIntent);
//            return true;
//        } else if (item.getItemId() == R.id.waiting_list) {
//            FragmentManager fm = getSupportFragmentManager();
//            waitingListFragment = WaitingListFragment.newInstance();
//            waitingListFragment.show(fm, "fragment_waiting_list");
//            return true;
//        } else if (item.getItemId() == R.id.month_summary) {
//            FragmentManager fm = getSupportFragmentManager();
//            monthSummaryFragment = MonthSummaryFragment.newInstance(CalendarUtils.selectedDate);
//            monthSummaryFragment.show(fm, "finances_fragment");
//            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void setWeekView() {
        tvMonthYear.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));

        ArrayList<LocalDate> days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);
        try {
            executorService.execute(() -> {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.clMonday, getCalendarDay(days, 0));
                ft.replace(R.id.clTuesday, getCalendarDay(days, 1));
                ft.replace(R.id.clWednesday, getCalendarDay(days, 2));
                ft.replace(R.id.clThursday, getCalendarDay(days, 3));
                ft.replace(R.id.clFriday, getCalendarDay(days, 4));
                ft.replace(R.id.clSaturday, getCalendarDay(days, 5));
                ft.commit();
            });


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), " " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRecyclerItemClicked(DogDto dog) {
        this.activeClient = dog;
        if (createAppointmentFragment != null && createAppointmentFragment.isVisible()) {
            createAppointmentFragment.displayPhoneNumbers();
        }
    }

    public DogDto getActiveDog() {
        return activeClient;
    }

    @Override
    public void onLabelItemClicked(LocalDate date, LocalTime time) {

        FragmentManager fm = getSupportFragmentManager();
        activeClient = null;
        createAppointmentFragment = CreateAppointmentFragment.newInstance(new AppointmentDto(date, time));
        createAppointmentFragment.show(fm, "fragment_edit_appointment");
    }

    @Override
    public void onLabelItemLongClicked(View view, LocalDate date, LocalTime time) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupAppointmentOptions = inflater.inflate(R.layout.popup_lock_period, null);
        PopupWindow popupWindow = new PopupWindow(popupAppointmentOptions, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);

        showPopupNextToView(view, popupWindow);
        findTimeLockingViews(popupAppointmentOptions);

        etLockEndTime.setText(time.plusMinutes(30).toString());
        validateTimeButtons(time, time.plusMinutes(30));

        btnTimeBack.setOnClickListener(view1 -> displayEarlierTime(time, etLockEndTime));
        btnTimeForward.setOnClickListener(view1 -> displayLaterTime(time, etLockEndTime));
        btnLockDate.setOnClickListener(view1 -> lockDate(popupWindow, etLockEndTime));
    }


    @NonNull
    private CalendarDay getCalendarDay(ArrayList<LocalDate> days, int index) {
        return CalendarDay.newInstance(
                days.get(index),
                appointmentsRequestDispatcher.getAppointmentsForTheDay(days.get(index))
        );
    }

    @Override
    public void onUnavailableItemClicked(View view, LocalDate date, LocalTime time) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupAppointmentOptions = inflater.inflate(R.layout.popup_delete, null);
        PopupWindow popupWindow = new PopupWindow(popupAppointmentOptions, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);

        showPopupNextToView(view, popupWindow);

        btnDeleteAppointment = popupAppointmentOptions.findViewById(R.id.btnDelete);

        btnDeleteAppointment.setOnClickListener(view1 -> {
            DogsRequestDispatcher dbHelper = new DogsRequestDispatcher();
            //todo thread
            //dbHelper.deleteUnavailablePeriod(date);
            popupWindow.dismiss();
            setWeekView();
        });
    }

    @Override
    public void onClientNameClicked(TextView tv, LocalDate date, LocalTime time, long appointmentID, DogDto dto) {
        PopupWindow popupWindow = createAddOrDeleteAppointmentPopup(tv);

        btnDeleteAppointment.setOnClickListener(view -> {
            //todo thread
            executorService.execute(() -> {
                appointmentsRequestDispatcher.deleteAppointment(appointmentID);
                runOnUiThread(() -> {
                    popupWindow.dismiss();
                    setWeekView();
                });
            });
        });

        btnAddAppointment.setOnClickListener(view -> {
            onLabelItemClicked(date, time);
            popupWindow.dismiss();
        });

        btnNavigateToClient.setOnClickListener(view -> {
            Intent switchActivityIntent = new Intent(this, DatabaseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("dto", dto);
            bundle.putInt("id", dto.getId());
            switchActivityIntent.putExtras(bundle);
            startActivity(switchActivityIntent);
            popupWindow.dismiss();
        });

        btnMoveAppointmentUp.setOnClickListener(view -> {
            //todo thread
            executorService.execute(() -> {
                AppointmentDto appointment = appointmentsRequestDispatcher.getAppointment(appointmentID);
                LocalTime appointmentTime = appointment.getTime();
                if (appointmentTime.isAfter(LocalTime.of(9, 0))) {
                    appointment.setTime(appointment.getTime().minusMinutes(30));
                    appointmentsRequestDispatcher.editAppointment(appointment);
                    runOnUiThread(this::setWeekView);
                }
                runOnUiThread(popupWindow::dismiss);

            });
        });

        btnMoveAppointmentDown.setOnClickListener(view -> {
            //todo thread
            executorService.execute(() -> {
                AppointmentDto appointment = appointmentsRequestDispatcher.getAppointment(appointmentID);
                LocalTime appointmentTime = appointment.getTime();
                DogDto dog = appointment.getDogDto();
                LocalTime appointmentEnd = appointmentTime.plusMinutes(dog.getExpectedAppointmentDuration());
                if (appointmentEnd.isBefore(LocalTime.of(20, 0))) {
                    appointment.setTime(appointment.getTime().plusMinutes(30));
                    appointmentsRequestDispatcher.editAppointment(appointment);
                    runOnUiThread(this::setWeekView);
                }
                runOnUiThread(popupWindow::dismiss);
            });
        });
    }


    public void onNotesButtonClicked(Button btn, String note) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupAppointmentOptions = inflater.inflate(R.layout.popup_notes, null);
        PopupWindow popupWindow = new PopupWindow(popupAppointmentOptions, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
        TextView tvNote = popupAppointmentOptions.findViewById(R.id.tvNote);
        tvNote.setText(note);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(tvNote, TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        showPopupNextToView(btn, popupWindow);
    }

    @Override
    public void refreshWeekView() {

        setWeekView();
        setWaitingListForShownWeek();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        //this method allows motion events to have priority over calendar click listeners "underneath"
        onTouchEvent(motionEvent);
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if (deltaX > MIN_SWIPE_DISTANCE)
                    previousWeek();
                if (deltaX < -MIN_SWIPE_DISTANCE)
                    nextWeek();
                if (deltaY > MIN_SWIPE_DISTANCE)
                    previousMonth();
                if (deltaY < -MIN_SWIPE_DISTANCE)
                    nextMonth();
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void onNewRecordAdded() {
        setWaitingListForShownWeek();
        waitingListFragment.onNewRecordAdded();
    }

    @Override
    public boolean onDayLabelLongClicked(View view, LocalDate date) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupAppointmentOptions = inflater.inflate(R.layout.popup_lock_day, null);
        PopupWindow popupWindow = new PopupWindow(popupAppointmentOptions, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
        Button btnLock = popupAppointmentOptions.findViewById(R.id.btnLockDate);
        btnLock.setOnClickListener(view1 -> {
            //todo thread
            //dogsRequestDispatcher.addUnavailablePeriod(date,LocalTime.of(8,0),LocalTime.of(20,00));
            refreshWeekView();
            popupWindow.dismiss();
        });
        showPopupNextToView(view, popupWindow);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            DogDto client = (DogDto) data.getSerializableExtra("client");
            createAppointmentFragment.newClientCreated(client);
        }
    }

    private void findViews() {
        tvMonthYear = findViewById(R.id.tvMonthYear);
        monday = findViewById(R.id.clMonday);
        tuesday = findViewById(R.id.clTuesday);
        wednesday = findViewById(R.id.clWednesday);
        thursday = findViewById(R.id.clThursday);
        friday = findViewById(R.id.clFriday);
        saturday = findViewById(R.id.clSaturday);
        nextMonth = findViewById(R.id.btnNextMonth);
        nextWeek = findViewById(R.id.btnNextWeek);
        prevMonth = findViewById(R.id.btnPrevMonth);
        prevWeek = findViewById(R.id.btnPrevWeek);
        ibWaitingListForDisplayedWeek = findViewById(R.id.ibWaitingListForShownWeek);
        tvWaitingListPreviewForDisplayedWeek = findViewById(R.id.tvWaitingListPreviewForShownWeek);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setListeners() {
        nextMonth.setOnClickListener(view -> nextMonth());
        nextWeek.setOnClickListener(view -> nextWeek());
        prevMonth.setOnClickListener(view -> previousMonth());
        prevWeek.setOnClickListener(view -> previousWeek());
        tvMonthYear.setOnClickListener(view -> showCurrentWeek());
        ibWaitingListForDisplayedWeek.setOnClickListener(view -> showWaitingListForDisplayedWeek());
    }

    private void showCurrentWeek() {
        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();
        setWaitingListForShownWeek();
    }

    private void previousWeek() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
        setWaitingListForShownWeek();
    }

    private void nextWeek() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
        setWaitingListForShownWeek();
    }

    private void previousMonth() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setWeekView();
        setWaitingListForShownWeek();
    }

    private void nextMonth() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setWeekView();
        setWaitingListForShownWeek();
    }

    private void showWaitingListForDisplayedWeek() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupWaitingList = inflater.inflate(R.layout.popup_waiting_list_for_chosen_week, null);
        PopupWindow popupWindow = new PopupWindow(popupWaitingList, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
        //todo thread
        // List<WaitingListRecord> waitingListForTheWeek = dogsRequestDispatcher.getWaitingListForTheWeek(CalendarUtils.selectedDate);
//        int[] location = new int[2];
//        ibWaitingListForDisplayedWeek.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//        ibWaitingListForDisplayedWeek.measure(0, 0);
//        popupWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity.NO_GRAVITY, x + ibWaitingListForDisplayedWeek.getMeasuredWidth() / 2, y);
//
//        RecyclerView rvWaitingListForShownWeek = popupWaitingList.findViewById(R.id.rvWaitingListForTheWeek);
//        rvWaitingListForShownWeek.setLayoutManager(new LinearLayoutManager(this));
//        rvWaitingListForShownWeek.setAdapter(new ShowWaitingListAdapter(this, waitingListForTheWeek));
    }

    private void setWaitingListForShownWeek() {

        //todo thread
//        waitingListForShownWeek = dogsRequestDispatcher.getWaitingListForTheWeek(CalendarUtils.selectedDate);
//        if (waitingListForShownWeek.isEmpty()) {
//            ibWaitingListForDisplayedWeek.setVisibility(View.GONE);
//            tvWaitingListPreviewForDisplayedWeek.setVisibility(View.GONE);
//        } else {
//            ibWaitingListForDisplayedWeek.setVisibility(View.VISIBLE);
//            tvWaitingListPreviewForDisplayedWeek.setVisibility(View.VISIBLE);
//            int firstClientOnWaitingListId = waitingListForShownWeek.get(0).getClientID();
//            DogDto firstClientOnWaitingList = dogsRequestDispatcher.getDog(firstClientOnWaitingListId);
//            int numberOfClientsOnWaitingList = waitingListForShownWeek.size();
//            String waitingListPreview = firstClientOnWaitingList.getFullName();
//            if (numberOfClientsOnWaitingList > 1) {
//                waitingListPreview += " ...+" + (numberOfClientsOnWaitingList - 1);
//            }
//            tvWaitingListPreviewForDisplayedWeek.setText(waitingListPreview);
//        }
    }

    private void showPopupNextToView(View view, PopupWindow popupWindow) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        view.measure(0, 0);
        popupWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity.NO_GRAVITY, x + view.getMeasuredWidth() / 2, y);
    }

    private void findTimeLockingViews(View popupAppointmentOptions) {
        btnLockDate = popupAppointmentOptions.findViewById(R.id.btnLockDate);
        btnTimeBack = popupAppointmentOptions.findViewById(R.id.btnTimeBack);
        btnTimeForward = popupAppointmentOptions.findViewById(R.id.btnTimeForward);
        etLockEndTime = popupAppointmentOptions.findViewById(R.id.etLockEndTime);
    }

    private void validateTimeButtons(LocalTime time, LocalTime lockEndTime) {
        if (!lockEndTime.isAfter(time.plusMinutes(30))) {
            btnTimeBack.setClickable(false);
            btnTimeBack.setAlpha(.5f);
        } else {
            btnTimeBack.setClickable(true);
            btnTimeBack.setAlpha(1f);
        }
        if (lockEndTime.isAfter(LocalTime.of(19, 30))) {
            btnTimeForward.setClickable(false);
            btnTimeForward.setAlpha(.5f);
        } else {
            btnTimeForward.setClickable(true);
            btnTimeForward.setAlpha(1f);
        }
    }

    private void displayEarlierTime(LocalTime time, TextView etLockEndTime) {
        LocalTime lockEndTime = LocalTime.parse(etLockEndTime.getText(), timeFormatter);
        lockEndTime = lockEndTime.minusMinutes(30);
        etLockEndTime.setText(lockEndTime.toString());
        validateTimeButtons(time, lockEndTime);
    }

    private void displayLaterTime(LocalTime time, TextView etLockEndTime) {
        LocalTime lockEndTime = LocalTime.parse(etLockEndTime.getText(), timeFormatter);
        lockEndTime = lockEndTime.plusMinutes(30);
        etLockEndTime.setText(lockEndTime.toString());
        validateTimeButtons(time, lockEndTime);
    }

    private void lockDate(PopupWindow popupWindow, TextView etLockEndTime) {
        LocalTime lockEndTime = LocalTime.parse(etLockEndTime.getText(), timeFormatter);
        DogsRequestDispatcher dogsRequestDispatcher = new DogsRequestDispatcher();
        //todo thread
        //dogsRequestDispatcher.addUnavailablePeriod(date, time, lockEndTime);
        popupWindow.dismiss();
        refreshWeekView();
    }

    private PopupWindow createAddOrDeleteAppointmentPopup(View view) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupAppointmentOptions = inflater.inflate(R.layout.popup_appointment_options, null);
        PopupWindow popupWindow = new PopupWindow(popupAppointmentOptions, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
        showPopupNextToView(view, popupWindow);
        btnAddAppointment = popupAppointmentOptions.findViewById(R.id.btnAddAppointment);
        btnDeleteAppointment = popupAppointmentOptions.findViewById(R.id.btnDeleteAppointment);
        btnNavigateToClient = popupAppointmentOptions.findViewById(R.id.btnNavigateToClient);
        btnMoveAppointmentUp = popupAppointmentOptions.findViewById(R.id.btnMoveAppointmentUp);
        btnMoveAppointmentDown = popupAppointmentOptions.findViewById(R.id.btnMoveAppointmentDown);
        return popupWindow;
    }
}
