package com.ehr.doctor.dao;


import com.ehr.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserid(Long userid);

    Optional<Doctor> getDoctorById(Long id);
}
