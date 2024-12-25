package com.ehr.doctor.feign;

import com.ehr.doctor.dto.AppointmentDto;
import com.ehr.doctor.dto.VisitInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@FeignClient("appointmentservice")
public interface DoctorAppointmentServiceInterface {

    @GetMapping("/doctorappointment/appointments")
    public ResponseEntity<Object> getAppointmentsOfDoctor(
            @RequestParam Long doctorId,
            @RequestParam LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime);

    @PostMapping("/appointments/visit-info")
    public ResponseEntity<VisitInfoDto> addVisitInfo(@RequestBody VisitInfoDto visitInfoDto);


    @GetMapping("/appointments/previousvisitinfo/{patientid}")
    public ResponseEntity<List<AppointmentDto>> getPreviousVisitInfo(@PathVariable Long patientid);
}
