package com.ehr.doctor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AppointmentInfo {
    private LocalDateTime appointmentTime;
    private String status;
}
