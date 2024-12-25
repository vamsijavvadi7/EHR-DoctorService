package com.ehr.doctor.feign;

import com.ehr.doctor.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("patient")
public interface PatientServiceInterface {
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable Long id);
}
