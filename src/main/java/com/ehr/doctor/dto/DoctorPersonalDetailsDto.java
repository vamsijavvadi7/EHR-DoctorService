package com.ehr.doctor.dto;



import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DoctorPersonalDetailsDto {
    private Long id;
    private Long userid;
    private String email;
    private String firstName;
    private Boolean isActive;
    private String lastName;
    private String specialization;
    private String phone;
    private String password;
    private DoctorAvailabilityDto availability;
}
