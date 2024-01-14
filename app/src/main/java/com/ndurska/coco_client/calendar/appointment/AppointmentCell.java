package com.ndurska.coco_client.calendar.appointment;

import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.calendar.unavailable_period.UnavailablePeriodDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AppointmentCell is a representation of a single 30 minute cell of appointment calendar. Similar to a ViewHolder.
 * AppointmentCell objects of the same day share static lists of appointments and set their behaviour and appearance accordingly.
 */

public class AppointmentCell {
    private final List<AppointmentHeader> appointmentHeaders;
    private final List<Long> IDs;
    private int numberOfAppointments;
    private boolean unavailable;
    private boolean firstCellOfAppointment;
    private boolean lastCellOfAppointment;
    private boolean middleCellOfAppointment;
    private final LocalTime time;
    private final LocalDate date;
    private static List<AppointmentDto> appointments;
    private static List<UnavailablePeriodDto> unavailablePeriods;

    public AppointmentCell(LocalDate date, LocalTime time) {
        this.time = time;
        this.date = date;
        appointmentHeaders = new ArrayList<>();
        IDs = new ArrayList<>();
        setThisDayAppointments();
    }

    private void setThisDayAppointments() {
        unavailable = checkIfUnavailable();
        for (AppointmentDto appointment : appointments) {
            checkIfFirstCellOfAppointment(appointment);
            checkIfLastCellOfAppointment(appointment);
            checkIfMiddleCellOfAppointment(appointment);
        }
    }

    private void checkIfFirstCellOfAppointment(AppointmentDto appointment) {
        if (Objects.equals(appointment.getTime(), time)) {
            appointmentHeaders.add(new AppointmentHeader(appointment.getDogDto(), appointment.getNotes()));
            IDs.add(appointment.getId());
            firstCellOfAppointment = true;
            numberOfAppointments++;
        }
    }

    private void checkIfLastCellOfAppointment(AppointmentDto appointment) {
        LocalTime appStart = appointment.getTime();
        LocalTime appEnd = appointment.getTime().plusMinutes(appointment.getDogDto().getExpectedAppointmentDuration());
        if (time.isAfter(appStart) && time.isBefore(appEnd)) {
                if (!time.isBefore(appEnd.minusMinutes(30)) && !time.isAfter(appEnd.plusMinutes(30))) {
                lastCellOfAppointment = true;
                numberOfAppointments++;
            }
        }
    }

    private void checkIfMiddleCellOfAppointment(AppointmentDto appointment) {
        LocalTime appStart = appointment.getTime();
        LocalTime appEnd = appointment.getTime().plusMinutes(appointment.getDogDto().getExpectedAppointmentDuration());
        if (time.isAfter(appStart) && time.isBefore(appEnd)) {
            if (!firstCellOfAppointment && !lastCellOfAppointment) {
                middleCellOfAppointment = true;
                numberOfAppointments++;
            }
        }
    }

    private boolean checkIfUnavailable() {
        for (UnavailablePeriodDto unavailablePeriodDto : unavailablePeriods) {
            if (time.isAfter(unavailablePeriodDto.getTimeStart()) && time.isBefore(unavailablePeriodDto.getTimeEnd())) {
                return true;
            }
        }
        return false;
    }

    //boilerplate code

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public static void setAppointments(List<AppointmentDto> appointmentDtos) {
        AppointmentCell.appointments = appointmentDtos;
    }

    public static void setUnavailablePeriods(List<UnavailablePeriodDto> unavailablePeriodDtos) {
        AppointmentCell.unavailablePeriods = unavailablePeriodDtos;
    }

    public AppointmentHeader getAppointmentHeader(int i) {
        return appointmentHeaders.get(i);
    }

    public long getAppointmentID(int i) {
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
