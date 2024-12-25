package com.ehr.doctor.dto;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DoctorPersonalAndAppointmentDetails {
    private Long id;
    private Long userid;
    private String firstName;
    private String lastName;
    private List<AppointmentDto> appointment;
    private DoctorAvailabilityDto availability;
}

