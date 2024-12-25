package com.ehr.doctor.service.kafka;

import com.ehr.doctor.dto.AppointmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class DoctorAppointmentListener {

    private final SimpMessagingTemplate messagingTemplate;

    public DoctorAppointmentListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "appointments", groupId = "doctor-group")
    public void listen(AppointmentDto appointment) {
        messagingTemplate.convertAndSendToUser("1","/doctor/appointments", appointment);
        System.out.println("Received appointment notification: " + appointment);
    }
}
