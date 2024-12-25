package com.ehr.doctor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String availableDays; // e.g., "Mon-Fri"
    private String availableFrom; // e.g., "09:00"
    private String availableUntil; // e.g., "17:00"
    @OneToOne(mappedBy = "availability")
    private Doctor doctor;

}

