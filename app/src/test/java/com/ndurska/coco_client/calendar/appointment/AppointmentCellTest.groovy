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
//    def "should recogize all cells of an appointment properly"() {
//        given:
//        def dog = new DogDto(expectedAppointmentDuration: 90)
//        def appointmentDtos = [
//                new AppointmentDto(time: LocalTime.of(9, 0), dogDto: dog)
//        ]
//        def dogs = [dog]
//
//        AppointmentCell.setAppointments(appointmentDtos)
//        AppointmentCell.setDogs(dogs)
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
//        then: "assert appointments are properly counted"
//        assert appointmentCell10_00AM.numberOfAppointments == 1
//        assert appointmentCell10_30AM.numberOfAppointments == 1
//        assert appointmentCell11_00AM.numberOfAppointments == 1
//        assert appointmentCell15_00AM.numberOfAppointments == 0
//    }
//}