package com.ehr.doctor.feign;


import com.ehr.doctor.dto.medicalrecords.DiagnosisDto;
import com.ehr.doctor.dto.medicalrecords.FamilyHistoryDto;
import com.ehr.doctor.dto.medicalrecords.PrescriptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "patientmedicalrecordsservice")
public interface PatientMedicalRecordsServiceInterface {
    //get methods for medical records
    @GetMapping("/diagnosis/patient/{patientId}")
    List<DiagnosisDto> getDiagnosesByPatientId(@PathVariable Long patientId);

    @GetMapping("/prescription/patient/{patientId}")
    List<PrescriptionDto> getPrescriptionsByPatientId(@PathVariable Long patientId);

    @GetMapping("/familyHistory/patient/{patientId}")
    List<FamilyHistoryDto> getFamilyHistoryByPatientId(@PathVariable Long patientId);

//set methods for medical records
    @PostMapping("/diagnosis")
    public ResponseEntity<DiagnosisDto> addDiagnosis(@RequestBody DiagnosisDto diagnosisDto);


    @PostMapping("/familyHistory")
    public ResponseEntity<FamilyHistoryDto> addDiagnosis(@RequestBody FamilyHistoryDto prescriptionDto);

    @PostMapping("/prescription")
    public ResponseEntity<PrescriptionDto> addDiagnosis(@RequestBody PrescriptionDto prescriptionDto);
}
