package com.ndurska.coco_client.calendar.appointment;

import com.ndurska.coco_client.calendar.unavailable_period.UnavailablePeriod;
import com.ndurska.coco_client.database.DogDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AppointmentCell is a representation of a single 30 minute cell of appointment calendar. Similar to a ViewHolder.
 * AppointmentCell objects of the same day share static lists of clients and appointments and set their behaviour and appearance accordingly.
 */

public class AppointmentCell {
    private final List<AppointmentHeader> appointmentHeaders;
    private final List<Integer> IDs;
    private int numberOfAppointments;
    private boolean unavailable;
    private boolean firstCellOfAppointment;
    private boolean lastCellOfAppointment;
    private boolean middleCellOfAppointment;
    private final LocalTime time;
    private final LocalDate date;
    private static ArrayList<Appointment> appointments;
    private static ArrayList<DogDto> dogs;
    private static ArrayList<UnavailablePeriod> unavailablePeriods;

    public AppointmentCell(LocalDate date, LocalTime time) {
        this.time = time;
        this.date = date;
        appointmentHeaders = new ArrayList<>();
        IDs = new ArrayList<>();
        setThisDayAppointments();

    }

    private void setThisDayAppointments() {
        int timeInMinutes = time.getHour() * 60 + time.getMinute();
        unavailable = checkIfUnavailable(timeInMinutes);
        for (Appointment appointment : appointments) {
            checkIfFirstCellOfAppointment(appointment);
            checkIfLastOrMiddleCellOfAppointment(appointment, timeInMinutes);

        }
    }

    private void checkIfFirstCellOfAppointment(Appointment appointment) {
        if (Objects.equals(appointment.getTime(), time)) {
            for (DogDto dog : dogs)
                if (dog.getId() == appointment.getClientID()) {
                    appointmentHeaders.add(new AppointmentHeader(dog, appointment.getNotes()));
                    IDs.add(appointment.getID());
                    firstCellOfAppointment = true;
                }
            numberOfAppointments++;
        }
    }

    private void checkIfLastOrMiddleCellOfAppointment(Appointment appointment, int timeInMinutes) {
        for (DogDto dog : dogs)
            if (dog.getId() == appointment.getClientID()) {
                int appStartInMinutes = appointment.getTime().getHour() * 60 + appointment.getTime().getMinute();
                int appEndInMinutes = appStartInMinutes + dog.getExpectedAppointmentDuration();
                if (timeInMinutes > appStartInMinutes && timeInMinutes < appEndInMinutes) {
                    numberOfAppointments++;
                    //check if this is a last time cell of any appointment with some time flexibility (so 40 minutes of appointment will use two cells)
                    if (timeInMinutes >= appEndInMinutes - 30 && timeInMinutes <= appEndInMinutes + 30)
                        lastCellOfAppointment = true;
                    else if (!firstCellOfAppointment)//if not first and not last it has to be middle one
                        middleCellOfAppointment = true;
                }
            }
    }


    private boolean checkIfUnavailable(int timeInMinutes) {
        for (UnavailablePeriod unavailablePeriod : unavailablePeriods) {
            int upStartInMinutes = unavailablePeriod.getStart().getHour() * 60 + unavailablePeriod.getStart().getMinute();
            int upEndInMinutes = unavailablePeriod.getEnd().getHour() * 60 + unavailablePeriod.getEnd().getMinute();
            if (timeInMinutes >= upStartInMinutes && timeInMinutes < upEndInMinutes)
                return true;
        }
        return false;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public static void setAppointments(ArrayList<Appointment> appointments) {
        AppointmentCell.appointments = appointments;
    }

    public static void setDogs(ArrayList<DogDto> dogs) {
        AppointmentCell.dogs = dogs;
    }

    public static void setUnavailablePeriods(ArrayList<UnavailablePeriod> unavailablePeriods) {
        AppointmentCell.unavailablePeriods = unavailablePeriods;
    }

    public AppointmentHeader getAppointmentHeader(int i) {
        return appointmentHeaders.get(i);
    }

    public int getAppointmentID(int i) {
        return IDs.get(i);
    }

    public int getNumberOfHeaders() {
        return appointmentHeaders.size();
    }

    public int getNumberOfAppointments() {
        return numberOfAppointments;
    }

    public boolean isFirstCellOfAppointment() {
        return firstCellOfAppointment;
    }

    public boolean isLastCellOfAppointment() {
        return lastCellOfAppointment;
    }

    public boolean isMiddleCellOfAppointment() {
        return middleCellOfAppointment;
    }

    public boolean isUnavailable() {
        return unavailable;
    }
}
