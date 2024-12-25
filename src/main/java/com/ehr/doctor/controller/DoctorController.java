package com.ehr.doctor.controller;

import com.ehr.doctor.dto.DoctorAvailabilityDto;
import com.ehr.doctor.dto.DoctorDto;
import com.ehr.doctor.dto.DoctorPersonalDetailsDto;
import com.ehr.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/create")
    public ResponseEntity<?> createDoctor(@RequestBody DoctorPersonalDetailsDto doctorPersonalDetailsDto) {
        return doctorService.createDoctor(doctorPersonalDetailsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }


    @GetMapping("/userid/{userid}")
    public ResponseEntity<?> getDoctorByUserid(@PathVariable Long userid) {
        return doctorService.getDoctorByUserid(userid);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorPersonalDetailsDto doctorPersonalDetailsDto) {
        return doctorService.updateDoctor(doctorPersonalDetailsDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        return doctorService.deleteDoctor(id);
    }

    @GetMapping("/allDoctors")
    public ResponseEntity<?> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PostMapping("/availableDoctors")
    public ResponseEntity<?> getAvailableDoctors(@RequestBody DoctorAvailabilityDto availabilityDto) {
        return doctorService.getAvailableDoctors(availabilityDto);
    }

    @GetMapping("/availableDoctorsByDate")
    public ResponseEntity<?> getAvailableDoctorsByDate(@RequestParam @DateTimeFormat(pattern = "MM-dd-yy") LocalDate dateParam) {
        return doctorService.getAvailableDoctorsByDate(dateParam);
    }

    @PostMapping("/setAvailability/{id}")
    public ResponseEntity<?> setDoctorAvailability(@PathVariable Long id,
                                                   @RequestBody DoctorAvailabilityDto availabilityDto) {
        return doctorService.setDoctorAvailability(id, availabilityDto);
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getDoctorAppointments( @RequestParam Long doctorId,
                                                    @RequestParam LocalDate date,
                                                    @RequestParam LocalTime startTime,
                                                    @RequestParam LocalTime endTime) {
            // Validate the input parameters
            if (startTime.isAfter(endTime)) {
                return ResponseEntity.badRequest().body("Start time cannot be after end time.");
            }


            try {
                // Call service method to fetch appointments
                return doctorService.getDoctorAppointments(doctorId, date, startTime, endTime);
            } catch (IllegalArgumentException e) {
                // Handle invalid arguments (if any)
                return ResponseEntity.badRequest().body("Invalid input parameters: " + e.getMessage());
            } catch (Exception e) {
                // Handle unexpected errors (e.g., database issues)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching appointments: " + e.getMessage());
            }
        }

    }

