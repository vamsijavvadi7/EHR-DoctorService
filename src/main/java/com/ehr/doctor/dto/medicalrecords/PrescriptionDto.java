package com.ehr.doctor.dto.medicalrecords;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PrescriptionDto {
    private Long id;
    @NotNull(message = "Patient ID is required.")
    private Long patientId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String instructions;
}
