package com.ndurska.coco_client.calendar.appointment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.database.dto.DogDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<AppointmentCell> {
    private final AppointmentAdapterListener listener;
    private LinearLayout llAppointmentCell;
    private LayerDrawable background;
    private AppointmentCell appointmentCell;


    public interface AppointmentAdapterListener {
        void onLabelItemClicked(LocalDate date, LocalTime time);

        void onLabelItemLongClicked(View view, LocalDate date, LocalTime time);

        void onUnavailableItemClicked(View view, LocalDate date, LocalTime time);

        void onClientNameClicked(TextView tv, LocalDate date, LocalTime time, long appointmentID, DogDto client);

        void onNotesButtonClicked(Button btn, String note);
    }

    public AppointmentAdapter(@NonNull Context context, List<AppointmentCell> appointmentCells, List<AppointmentDto> appointmentDtos) {
        super(context, 0, appointmentCells);
        listener = (AppointmentAdapterListener) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        appointmentCell = getItem(position);
        LocalTime time = appointmentCell.getTime();
        LocalDate date = appointmentCell.getDate();

        convertView = findView(parent);
        displayAppointmentInfo(date, time);
        displayColor();
        displayBorder();
        setListeners(convertView, date, time);
        return convertView;

    }

    @NonNull
    private View findView(@NonNull ViewGroup parent) {
        @Nullable View convertView;
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.appointment_cell, parent, false);
        llAppointmentCell = convertView.findViewById(R.id.llAppointmentCell);
        return convertView;
    }

    private void displayAppointmentInfo(LocalDate date, LocalTime time) {
        for (int i = 0; i < appointmentCell.getNumberOfHeaders(); i++) {
            TextView tv = createTextViewWithAppointmentInfo(i);
            llAppointmentCell.addView(tv);
            setAppointmentCellListener(i, tv, date, time);
            if (appointmentCell.getAppointmentHeader(i).hasNote())
                addNoteButton(appointmentCell.getAppointmentHeader(i).getNote());
        }

    }

    private void setListeners(View view, LocalDate date, LocalTime time) {

        if (appointmentCell.isUnavailable()) {
            view.setOnClickListener(view1 -> listener.onUnavailableItemClicked(view, date, time));
        } else {
            view.setOnClickListener(view2 -> listener.onLabelItemClicked(date, time));
            view.setOnLongClickListener(view2 -> {
                listener.onLabelItemLongClicked(view, date, time);
                return true;
            });
        }

    }

    private TextView createTextViewWithAppointmentInfo(int i) {
        TextView tv = new TextView(getContext());
        tv.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
        tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 5));
        tv.setText(appointmentCell.getAppointmentHeader(i).getClientName());
        return tv;

    }

    private void setAppointmentCellListener(int i, TextView tv, LocalDate date, LocalTime time) {
        long appID = appointmentCell.getAppointmentID(i);
        DogDto client = appointmentCell.getAppointmentHeader(i).getDog();
        tv.setOnClickListener(view -> listener.onClientNameClicked(tv, date, time, appID, client));

    }

    private void displayBorder() {

        if (appointmentCell.isFirstCellOfAppointment()) {
            background.addLayer(getContext().getResources().getDrawable(R.drawable.border_top));
            llAppointmentCell.setBackground((Drawable) background);
        }
        if (appointmentCell.isLastCellOfAppointment()) {
            background.addLayer(getContext().getResources().getDrawable(R.drawable.border_bottom));
            llAppointmentCell.setBackground((Drawable) background);
        }
        if (appointmentCell.isMiddleCellOfAppointment()) {
            background.addLayer(getContext().getResources().getDrawable(R.drawable.border_middle));
            llAppointmentCell.setBackground((Drawable) background);
        }
        if (appointmentCell.isUnavailable()) {
            background.addLayer(getContext().getResources().getDrawable(R.drawable.crossed_cell));
            llAppointmentCell.setBackground((Drawable) background);
        }
    }

    private void displayColor() {
        background = (LayerDrawable) getContext().getResources().getDrawable(R.drawable.calendar_cell_layer_list);
        GradientDrawable color = (GradientDrawable) background.findDrawableByLayerId(R.id.color);
        if (appointmentCell.getNumberOfAppointments() == 1) {
            color.setColor(getContext().getResources().getColor(R.color.one_client_calendar));
        } else if (appointmentCell.getNumberOfAppointments() == 2) {
            color.setColor(getContext().getResources().getColor(R.color.two_clients_calendar));
        } else if (appointmentCell.getNumberOfAppointments() >= 3) {
            color.setColor(getContext().getResources().getColor(R.color.three_clients_calendar));
        } else {
            color.setColor(getContext().getResources().getColor(R.color.opaque));
        }

    }

    private void addNoteButton(String note) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
        Button notesButton = new Button(getContext());
        notesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_exclamation_mark, 0, 0, 0);
        notesButton.setLayoutParams(params);
        notesButton.setBackgroundResource(R.drawable.round_button);
        llAppointmentCell.addView(notesButton);
        notesButton.setOnClickListener(view ->
                listener.onNotesButtonClicked(notesButton, note)
        );
    }
}



