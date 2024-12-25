package com.ehr.doctor.controller;

import com.ehr.doctor.dto.AppointmentDto;
import com.ehr.doctor.dto.PatientDto;
import com.ehr.doctor.dto.VisitInfoDto;
import com.ehr.doctor.service.DoctorViewPatientDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ehr.doctor.dto.medicalrecords.DiagnosisDto;
import com.ehr.doctor.dto.medicalrecords.FamilyHistoryDto;
import com.ehr.doctor.dto.medicalrecords.PrescriptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/doctorviewpatientdetails")
public class DoctorViewPatientDetailsController {

    @Autowired
    private DoctorViewPatientDetailsService doctorViewPatientDetailsService;
    @GetMapping("/personaldetails/{patientId}")
    public ResponseEntity<PatientDto> getPatientDetails(@PathVariable Long patientId) {
        return doctorViewPatientDetailsService.getPatientDetails(patientId);
    }

    // GET methods for patient's medical records
    @GetMapping("/diagnosis/{patientId}")
    public ResponseEntity<List<DiagnosisDto>> getPatientDiagnosis(@PathVariable Long patientId) {
        return doctorViewPatientDetailsService.getPatientDiagnosis(patientId);
    }

    @GetMapping("/prescription/{patientId}")
    public ResponseEntity<List<PrescriptionDto>> getPatientPrescriptions(@PathVariable Long patientId) {
        return doctorViewPatientDetailsService.getPatientPrescriptions(patientId);
    }

    @GetMapping("/familyHistory/{patientId}")
    public ResponseEntity<List<FamilyHistoryDto>> getPatientFamilyHistory(@PathVariable Long patientId) {
        return doctorViewPatientDetailsService.getPatientFamilyHistory(patientId);
    }

    // POST methods for setting patient's medical records
    @PostMapping("/diagnosis")
    public ResponseEntity<DiagnosisDto> addDiagnosis(@RequestBody DiagnosisDto diagnosisDto) {
        return doctorViewPatientDetailsService.addDiagnosis(diagnosisDto);
    }

    @PostMapping("/familyHistory")
    public ResponseEntity<FamilyHistoryDto> addFamilyHistory(@RequestBody FamilyHistoryDto familyHistoryDto) {
        return doctorViewPatientDetailsService.addFamilyHistory(familyHistoryDto);
    }

    @PostMapping("/prescription")
    public ResponseEntity<PrescriptionDto> addPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        return doctorViewPatientDetailsService.addPrescription(prescriptionDto);
    }
    @PostMapping("/visitinfo")
    public ResponseEntity<VisitInfoDto> addVisitInfo(@RequestBody VisitInfoDto visitInfoDto) {
        return doctorViewPatientDetailsService.addVisitInfo(visitInfoDto);
    }

    @GetMapping("/previousvisitinfo/{patientid}")
    public ResponseEntity<List<AppointmentDto>> getPreviousVisitInfo(@PathVariable Long patientid){
        return doctorViewPatientDetailsService.getPreviousVisitInfo(patientid);
    }
}

