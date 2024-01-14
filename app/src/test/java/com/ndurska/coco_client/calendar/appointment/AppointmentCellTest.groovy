//package com.ndurska.coco_client.calendar.appointment
//
//import com.ndurska.coco_client.calendar.appointment.AppointmentCell
//import com.ndurska.coco_client.calendar.appointment.dto.AppointmentDto
//import com.ndurska.coco_client.calendar.unavailable_period.UnavailablePeriodDto
//import com.ndurska.coco_client.database.dto.DogDto
//import spock.lang.Specification
//
//import java.time.LocalDate
//import java.time.LocalTime
//
//class AppointmentCellTest extends Specification {
//
//    def "checkIfFirstCellOfAppointment"() {
//        given:
//        def dog = new DogDto(expectedAppointmentDuration: 90)
//
//        def appointmentDtos = [
//                new AppointmentDto(time: LocalTime.of(9, 0), dogDto: dog)
//        ]
//        def dogs = [dog]
//        def unavailablePeriodDtos = [
//                new UnavailablePeriodDto(timeStart: LocalTime.of(14, 0), timeEnd: LocalTime.of(16, 0))
//        ]
//        AppointmentCell.setAppointments(appointmentDtos)
//        AppointmentCell.setDogs(dogs)
//        AppointmentCell.setUnavailablePeriods(unavailablePeriodDtos)
//
//        when:
//        def appointmentCell10_00AM = new AppointmentCell(LocalDate.now(), LocalTime.of(10, 0))
//        def appointmentCell10_30AM = new AppointmentCell(LocalDate.now(), LocalTime.of(10, 30))
//        def appointmentCell11_00AM = new AppointmentCell(LocalDate.now(), LocalTime.of(11, 0))
//        def appointmentCell15_00AM = new AppointmentCell(LocalDate.now(), LocalTime.of(15, 0))
//
//        then: "assert first cell of appointment set properly"
//        assert appointmentCell10_00AM.firstCellOfAppointment
//        assert !appointmentCell10_30AM.firstCellOfAppointment
//        assert !appointmentCell11_00AM.firstCellOfAppointment
//        assert !appointmentCell15_00AM.firstCellOfAppointment
//
//        then: "assert middle cell of appointment set properly"
//        assert !appointmentCell10_00AM.middleCellOfAppointment
//        assert appointmentCell10_30AM.middleCellOfAppointment
//        assert !appointmentCell11_00AM.middleCellOfAppointment
//        assert !appointmentCell15_00AM.middleCellOfAppointment
//
//        then: "assert last cell of appointment set properly"
//        assert !appointmentCell10_00AM.lastCellOfAppointment
//        assert !appointmentCell10_30AM.lastCellOfAppointment
//        assert appointmentCell11_00AM.lastCellOfAppointment
//        assert !appointmentCell15_00AM.lastCellOfAppointment
//
//        then: "assert unavailable cell set properly"
//        assert !appointmentCell10_00AM.unavailable
//        assert !appointmentCell10_30AM.unavailable
//        assert !appointmentCell11_00AM.unavailable
//        assert appointmentCell15_00AM.unavailable
//    }
//}