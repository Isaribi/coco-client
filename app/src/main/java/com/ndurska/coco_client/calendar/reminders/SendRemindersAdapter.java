package com.ndurska.coco_client.calendar.reminders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurska.coco_client.R;
import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.database.dto.DogDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SendRemindersAdapter extends RecyclerView.Adapter<SendRemindersVH> {

    private final Context context;
    private ArrayList<String> phoneNumbers;
    private final Map<String, String> remindersList;
    private final List<AppointmentDto> appointments;
    private List<DogDto> dogs;
    private final List<Boolean> positionsOfDisplayedNumbers;
    private final Boolean[][] positionsOfCheckedBoxesOnRecyclerView;


    public SendRemindersAdapter(Context context, List<AppointmentDto> appointments) {
        this.context = context;
        this.appointments = appointments;
        String className = "com.ndurska.coco_client.database.dto.DogDto";
        try {
            Class.forName(className); // Attempt to load the class
            System.out.println("Class is available: " + className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class is not available: " + className);
            e.printStackTrace();
        }
List<DogDto> dogs =  new ArrayList<>();
        for (AppointmentDto appointmentDto : appointments) {
            dogs.add(appointmentDto.getDogDto());
        }
        positionsOfDisplayedNumbers = new ArrayList<>();
        phoneNumbers = new ArrayList<>();
        remindersList = new LinkedHashMap<>();
        positionsOfCheckedBoxesOnRecyclerView = new Boolean[appointments.size()][2];
        for (Boolean[] booleans : positionsOfCheckedBoxesOnRecyclerView)
            Arrays.fill(booleans, false);


        for (DogDto dog : dogs) {
            if (phoneNumbers.contains(dog.getPhoneNumber1()) && phoneNumbers.contains(dog.getPhoneNumber2())) {
                positionsOfDisplayedNumbers.add(false);
            } else {
                positionsOfDisplayedNumbers.add(true);
                phoneNumbers.add(dog.getPhoneNumber1());
                phoneNumbers.add(dog.getPhoneNumber2());
            }
        }
    }

    @NonNull
    @Override
    public SendRemindersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_reminder_item, parent, false);

        return new SendRemindersVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull SendRemindersVH holder, int position) {

        try {
            holder.setIsRecyclable(false);

            AppointmentDto appointment = appointments.get(position);
            DogDto dto = appointment.getDogDto();
            holder.setAppointmentDto(appointment);

            String clientName = dto.getName() + " " + dto.getPseudonym() + " " + dto.getBreed();
            holder.tvDogName.setText(clientName);

            customizePhoneNumberCheckBox(dto, holder, 1);
            customizePhoneNumberCheckBox(dto, holder, 2);

            String textMessage = context.getString(R.string.text_reminder1) + " " + appointment.getTime() + context.getString(R.string.text_reminder2);
            holder.etTextMessage.setText(textMessage);
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

    public Map<String, String> getRemindersList() {
        return remindersList;
    }

    private void customizePhoneNumberCheckBox(DogDto dog, @NonNull SendRemindersVH holder, int boxNumber) {
        String phoneNumber;
        String phoneLabel;
        CheckBox checkBox;
        TextView textView;
        EditText editText = holder.etTextMessage;
        int position = holder.getAdapterPosition();

        if (boxNumber == 1) {
            phoneNumber = dog.getPhoneNumber1();
            phoneLabel = dog.getPhoneNumberLabel1();
            checkBox = holder.cbPhoneNumber1;
            textView = holder.tvPhoneLabel1;
        } else if (boxNumber == 2) {
            phoneNumber = dog.getPhoneNumber2();
            phoneLabel = dog.getPhoneNumberLabel2();
            checkBox = holder.cbPhoneNumber2;
            textView = holder.tvPhoneLabel2;
        } else
            return;

        if (positionsOfCheckedBoxesOnRecyclerView[position][boxNumber - 1]) {
            checkBox.setChecked(true);
            editText.setEnabled(false);
        }

        if (!(phoneNumber == null || phoneNumber.length() == 0)) {
            if (positionsOfDisplayedNumbers.get(position)) {
                checkBox.setText(phoneNumber);

                checkBox.setOnClickListener(view -> {
                    if (checkBox.isChecked()) {
                        positionsOfCheckedBoxesOnRecyclerView[position][boxNumber - 1] = true;
                        String textMessageReminder = editText.getText().toString();
                        remindersList.put(phoneNumber, textMessageReminder);
                        editText.setEnabled(false);
                    } else {
                        positionsOfCheckedBoxesOnRecyclerView[position][boxNumber - 1] = false;
                        remindersList.remove(phoneNumber);
                        editText.setEnabled(true);
                    }
                });
            } else {
                checkBox.setText(R.string.repeatedNumber);
                checkBox.setClickable(false);

                Drawable drawableWrap = DrawableCompat.wrap(holder.itemView.getBackground()).mutate();
                DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.opaque_light_gray));
                holder.etTextMessage.setBackgroundColor(ContextCompat.getColor(context, R.color.very_light_gray));
            }
            textView.setText(phoneLabel);
        } else {
            checkBox.setText("");
            checkBox.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.GONE);
        }
    }
}

class SendRemindersVH extends RecyclerView.ViewHolder {

    TextView tvDogName;
    AppointmentDto appointment;
    CheckBox cbPhoneNumber1;
    CheckBox cbPhoneNumber2;
    EditText etTextMessage;
    TextView tvPhoneLabel1;
    TextView tvPhoneLabel2;

    private SendRemindersAdapter adapter;

    public SendRemindersVH(@NonNull View itemView) {
        super(itemView);
        tvDogName = itemView.findViewById(R.id.tvReminderClientName);

        cbPhoneNumber1 = itemView.findViewById(R.id.cbReminderPhoneNumber1);
        cbPhoneNumber2 = itemView.findViewById(R.id.cbReminderPhoneNumber2);
        etTextMessage = itemView.findViewById(R.id.etTextMessage);
        tvPhoneLabel1 = itemView.findViewById(R.id.tvReminderPhoneLabel1);
        tvPhoneLabel2 = itemView.findViewById(R.id.tvReminderPhoneLabel2);
    }

    public SendRemindersVH linkAdapter(SendRemindersAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public void setAppointmentDto(AppointmentDto appointment) {
        this.appointment = appointment;
    }

}
