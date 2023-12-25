package com.ndurska.coco_client.summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.database.dto.DogDto;
import com.ndurska.coco_client.shared.TextWatcherAdapter;

import java.util.List;

public class AppointmentSummaryAdapter extends RecyclerView.Adapter<AppointmentSummaryVH> {

    private final List<AppointmentDto> appointments;
    private final Context context;
    private final SummaryListener listener;

    interface SummaryListener {
        void updateTotal(int newTotal);
    }


    public AppointmentSummaryAdapter(Context context, List<AppointmentDto> appointments, AppointmentSummaryFragment fragment) {
        this.appointments = appointments;
        this.context = context;
        listener = fragment;
    }

    @NonNull
    @Override
    public AppointmentSummaryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_summary_item, parent, false);

        return new AppointmentSummaryVH(view).linkAdapter(this);

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentSummaryVH holder, int position) {

        try {
            AppointmentDto appointment = appointments.get(position);
            DogDto dog = appointment.getDogDto();
            String dogName = dog.getName() + " " + dog.getPseudonym() + " " + dog.getBreed();
            holder.setAppointment(appointment);
            holder.linkListener(listener);
            holder.tvDogName.setText(dogName);
            holder.tvTime.setText(appointment.getTime().toString());
            holder.cbAbsent.setChecked(!appointment.getAttendance());
            if (appointment.getAmountPaid() != 0)
                holder.etPayment.setText(String.valueOf(appointment.getAmountPaid()));

        } catch (NullPointerException e) {
            holder.tvDogName.setText(R.string.null_);
            Toast.makeText(context, context.getString(R.string.null_dog_exception) + e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            holder.tvDogName.setText(R.string.error);
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public List<AppointmentDto> getAppointments() {
        return appointments;
    }

    public int getAppointmentsTotal() {
        //todo stream ffs
        int total = 0;
        for (AppointmentDto appointment : appointments)
            total += appointment.getAmountPaid();
        return total;
    }

}

class AppointmentSummaryVH extends RecyclerView.ViewHolder {

    TextView tvDogName;
    CheckBox cbAbsent;
    TextView etPayment;
    TextView tvTime;
    AppointmentDto appointment;
    AppointmentSummaryAdapter.SummaryListener listener;

    private AppointmentSummaryAdapter adapter;

    public AppointmentSummaryVH(@NonNull View itemView) {
        super(itemView);

        findViews(itemView);
        setListeners();

    }

    private void setListeners() {
        cbAbsent.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                appointment.setAttendance(false);
                appointment.setAmountPaid(0);
                etPayment.setText("");
                etPayment.setEnabled(false);
                listener.updateTotal(adapter.getAppointmentsTotal());
            } else {
                appointment.setAttendance(true);
                etPayment.setEnabled(true);
            }
        });

        etPayment.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPayment.getText().length() > 0)
                    appointment.setAmountPaid(Integer.parseInt(etPayment.getText().toString()));
                else
                    appointment.setAmountPaid(0);
                listener.updateTotal(adapter.getAppointmentsTotal());

            }
        });
    }

    private void findViews(@NonNull View itemView) {
        tvDogName = itemView.findViewById(R.id.tvSummaryClientName);
        cbAbsent = itemView.findViewById(R.id.cbAbsent);
        etPayment = itemView.findViewById(R.id.etPayment);
        tvTime = itemView.findViewById(R.id.tvSummaryTime);
    }

    public AppointmentSummaryVH linkAdapter(AppointmentSummaryAdapter adapter) {
        this.adapter = adapter;
        return this;

    }

    public void linkListener(AppointmentSummaryAdapter.SummaryListener listener) {
        this.listener = listener;
    }

    public void setAppointment(AppointmentDto appointment) {
        this.appointment = appointment;
    }


}
