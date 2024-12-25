package com.ehr.doctor.service;

import com.ehr.doctor.dto.AppointmentDto;
import com.ehr.doctor.dto.PatientDto;
import com.ehr.doctor.dto.UserDto;
import com.ehr.doctor.dto.VisitInfoDto;
import com.ehr.doctor.feign.DoctorAppointmentServiceInterface;
import com.ehr.doctor.feign.PatientServiceInterface;
import com.ehr.doctor.feign.UserServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.ehr.doctor.dto.medicalrecords.DiagnosisDto;
import com.ehr.doctor.dto.medicalrecords.FamilyHistoryDto;
import com.ehr.doctor.dto.medicalrecords.PrescriptionDto;
import com.ehr.doctor.feign.PatientMedicalRecordsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Optional;

@Service
public class DoctorViewPatientDetailsService {

    @Autowired
    private PatientMedicalRecordsServiceInterface patientMedicalRecordsServiceInterface;
    @Autowired
    DoctorAppointmentServiceInterface doctorAppointmentServiceInterface;

    @Autowired
    private PatientServiceInterface patientServiceInterface;

    @Autowired
    private UserServiceInterface userServiceInterface;

    // Get patient diagnoses
    public ResponseEntity<List<DiagnosisDto>> getPatientDiagnosis(Long patientId) {
        List<DiagnosisDto> diagnoses = patientMedicalRecordsServiceInterface.getDiagnosesByPatientId(patientId);
        return ResponseEntity.ok(diagnoses);
    }

    // Get patient prescriptions
    public ResponseEntity<List<PrescriptionDto>> getPatientPrescriptions(Long patientId) {
        List<PrescriptionDto> prescriptions = patientMedicalRecordsServiceInterface.getPrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    // Get patient family history
    public ResponseEntity<List<FamilyHistoryDto>> getPatientFamilyHistory(Long patientId) {
        List<FamilyHistoryDto> familyHistory = patientMedicalRecordsServiceInterface.getFamilyHistoryByPatientId(patientId);
        return ResponseEntity.ok(familyHistory);
    }

    // Add a new diagnosis
    public ResponseEntity<DiagnosisDto> addDiagnosis(DiagnosisDto diagnosisDto) {
        return patientMedicalRecordsServiceInterface.addDiagnosis(diagnosisDto);
    }

    // Add a new family history record
    public ResponseEntity<FamilyHistoryDto> addFamilyHistory(FamilyHistoryDto familyHistoryDto) {
        return patientMedicalRecordsServiceInterface.addDiagnosis(familyHistoryDto);
    }

    // Add a new prescription
    public ResponseEntity<PrescriptionDto> addPrescription(PrescriptionDto prescriptionDto) {
        return patientMedicalRecordsServiceInterface.addDiagnosis(prescriptionDto);
    }

    public ResponseEntity<PatientDto> getPatientDetails(Long patientId) {
        // Get the patient response from the service layer
        ResponseEntity<PatientDto> patientResponse = patientServiceInterface.getPatient(patientId);

        // Check if the response is OK (200)
        if (patientResponse.getStatusCode() == HttpStatus.OK) {
            // If the patient is found, return the PatientDto
            PatientDto patientDto = patientResponse.getBody();

            ResponseEntity<UserDto> userResponse=userServiceInterface.getUserById(patientDto.getUserid());

            if(userResponse.getStatusCode()==HttpStatus.OK){
                UserDto userDto= userResponse.getBody();
                if(userDto!=null) {
                    patientDto.setEmail(userDto.getEmail());
                    patientDto.setFirstName(userDto.getFirstName());
                    patientDto.setLastName(userDto.getLastName());
                }
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Perform any additional operations on patientDto if needed
            return ResponseEntity.ok(patientDto);
        } else {
            // If the patient was not found, return a 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<VisitInfoDto> addVisitInfo(VisitInfoDto visitInfoDto) {
        return doctorAppointmentServiceInterface.addVisitInfo(visitInfoDto);
    }

    public ResponseEntity<List<AppointmentDto>> getPreviousVisitInfo(Long patientid) {
        return doctorAppointmentServiceInterface.getPreviousVisitInfo(patientid);
    }
}
