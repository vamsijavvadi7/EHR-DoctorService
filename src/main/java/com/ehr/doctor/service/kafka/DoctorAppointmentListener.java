package com.ehr.doctor.service.kafka;

import com.ehr.doctor.dao.DoctorRepository;
import com.ehr.doctor.dto.AppointmentDto;
import com.ehr.doctor.dto.AppointmentDto2;
import com.ehr.doctor.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorAppointmentListener {


    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private DoctorRepository doctorRepository;

    public DoctorAppointmentListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "appointments", groupId = "doctor-group")
    public void listen(AppointmentDto2 appointment) {
        try {
            Optional<Doctor> doctor= doctorRepository.getDoctorById(appointment.getDoctorId());
            doctor.ifPresent(value -> messagingTemplate.convertAndSendToUser(value.getUserid().toString(), "/doctor/appointments", appointment));
        } catch(Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
    }

}
