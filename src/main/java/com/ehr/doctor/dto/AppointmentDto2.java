package com.ehr.doctor.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentDto2 {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String appointmentTime;
    private PatientDto patient;
    private String status;
    private VisitInfoDto visitInfo;
}
