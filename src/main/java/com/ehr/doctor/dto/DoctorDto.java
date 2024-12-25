package com.ehr.doctor.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DoctorDto {
    private Long id;
    private Long userid;
    @NotBlank
    private String specialization;
    @NotBlank
    private String phone;
    private DoctorAvailabilityDto availability;
}
