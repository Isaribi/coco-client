package com.ndurska.coco_client.calendar.appointment;

import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto;
import com.ndurska.coco_client.calendar.unavailable_period.UnavailablePeriodDto;
import com.ndurska.coco_client.database.dto.DogDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AppointmentCell is a representation of a single 30 minute cell of appointment calendar. Similar to a ViewHolder.
 * AppointmentCell objects of the same day share static lists of clients and appointmentDtos and set their behaviour and appearance accordingly.
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
    private static List<AppointmentDto> appointmentDtos;
    private static List<UnavailablePeriodDto> unavailablePeriodDtos;

    public AppointmentCell(LocalDate date, LocalTime time) {
        this.time = time;
        this.date = date;
        appointmentHeaders = new ArrayList<>();
        IDs = new ArrayList<>();
        setThisDayAppointments();
    }

    private void setThisDayAppointments() {
        unavailable = checkIfUnavailable();
        for (AppointmentDto appointmentDto : appointmentDtos) {
            checkIfFirstCellOfAppointment(appointmentDto);
            checkIfLastOrMiddleCellOfAppointment(appointmentDto);
        }
    }

    private void checkIfFirstCellOfAppointment(AppointmentDto appointmentDto) {
        if (Objects.equals(appointmentDto.getTime(), time)) {

            appointmentHeaders.add(new AppointmentHeader(appointmentDto.getDogDto(), appointmentDto.getNotes()));
            IDs.add(appointmentDto.getId());
            firstCellOfAppointment = true;
            numberOfAppointments++;
        }


    }

    private void checkIfLastOrMiddleCellOfAppointment(AppointmentDto appointmentDto) {
        LocalTime appStart = appointmentDto.getTime();
        LocalTime appEnd = appointmentDto.getTime().plusMinutes(appointmentDto.getDogDto().getExpectedAppointmentDuration());
        if (time.isAfter(appStart) && time.isBefore(appEnd)) {
            numberOfAppointments++;
            //check if this is a last time cell of any appointmentDto with some time flexibility (so 40 minutes of appointmentDto will use two cells)
            //if (timeInMinutes >= appEndInMinutes - 30 && timeInMinutes <= appEndInMinutes + 30) {
            if (!time.isBefore(appEnd.minusMinutes(30)) && !time.isAfter(appEnd.plusMinutes(30))) {
                lastCellOfAppointment = true;
            } else if (!firstCellOfAppointment) {//if not first and not last it has to be middle one
                middleCellOfAppointment = true;
            }
        }
    }

    private int getTimeInMinutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }


    private boolean checkIfUnavailable() {
        int timeInMinutes = getTimeInMinutes(time);
        for (UnavailablePeriodDto unavailablePeriodDto : unavailablePeriodDtos) {
            int upStartInMinutes = getTimeInMinutes(unavailablePeriodDto.getTimeStart());
            int upEndInMinutes = getTimeInMinutes(unavailablePeriodDto.getTimeEnd());
            if (timeInMinutes >= upStartInMinutes && timeInMinutes < upEndInMinutes) {
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
        AppointmentCell.appointmentDtos = appointmentDtos;
    }

    public static void setUnavailablePeriods(List<UnavailablePeriodDto> unavailablePeriodDtos) {
        AppointmentCell.unavailablePeriodDtos = unavailablePeriodDtos;
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
