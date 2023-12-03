package com.ndurska.coco_client.database;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;

import java.time.LocalDate;
import java.util.List;

public class ShowAppointmentsAdapter extends RecyclerView.Adapter<AppointmentVH> {
    List<AppointmentDto> appointments;
    Context context;

    public ShowAppointmentsAdapter(Context context, List<AppointmentDto> appointments) {
        this.appointments = appointments;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);

        return new AppointmentVH(view).linkAdapter(this);

    }


    @Override
    public void onBindViewHolder(@NonNull AppointmentVH holder, int position) {
        try {
            AppointmentDto appointment = appointments.get(position);
            holder.setAppointment(appointment);
            holder.tvTime.setText(appointment.getTime().toString());
            holder.cbAbsent.setChecked(!appointment.getAttendance());
            holder.tvDate.setText(appointment.getDate().toString());
            holder.tvNote.setText(appointment.getNotes());
            if (appointment.getAmountPaid() != 0)
                holder.tvPayment.setText(String.valueOf(appointment.getAmountPaid()));
            else
                holder.tvPayment.setText("");
            if (appointment.getDate().isBefore(LocalDate.now())) {
                Drawable drawableWrap = DrawableCompat.wrap(holder.itemView.getBackground()).mutate();
                DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.very_light_gray));
            }

        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


}


class AppointmentVH extends RecyclerView.ViewHolder {

    AppointmentDto appointment;
    TextView tvDate;
    CheckBox cbAbsent;
    TextView tvPayment;
    TextView tvTime;
    TextView tvNote;

    public AppointmentVH(@NonNull View itemView) {
        super(itemView);
        cbAbsent = itemView.findViewById(R.id.cbAppointmentAbsent);
        tvPayment = itemView.findViewById(R.id.tvAppointmentPayment);
        tvTime = itemView.findViewById(R.id.tvAppointmentTime);
        tvDate = itemView.findViewById(R.id.tvAppointmentDate);
        tvNote = itemView.findViewById(R.id.tvAppointmentNote);

    }

    public AppointmentVH linkAdapter(ShowAppointmentsAdapter adapter) {
        return this;

    }

    public void setAppointment(AppointmentDto appointment) {
        this.appointment = appointment;
    }


}
