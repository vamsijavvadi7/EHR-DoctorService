package com.ehr.doctor.service;

import com.ehr.doctor.dto.*;
import com.ehr.doctor.feign.DoctorAppointmentServiceInterface;
import com.ehr.doctor.feign.UserServiceInterface;
import com.ehr.doctor.mapper.DoctorMapper;
import com.ehr.doctor.model.Doctor;
import com.ehr.doctor.dao.DoctorRepository;
import com.ehr.doctor.model.DoctorAvailability;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    DoctorAppointmentServiceInterface doctorAppointmentServiceInterface;

    @Autowired
    UserServiceInterface userServiceInterface;



    @Transactional
    public ResponseEntity<?> createDoctor(DoctorPersonalDetailsDto doctorPersonalDetailsDto) {

            UserDto userDto=new UserDto();
            userDto.setEmail(doctorPersonalDetailsDto.getEmail());
            userDto.setFirstName(doctorPersonalDetailsDto.getFirstName());
            userDto.setLastName(doctorPersonalDetailsDto.getLastName());
            userDto.setIsActive(doctorPersonalDetailsDto.getIsActive());
            userDto.setPassword(doctorPersonalDetailsDto.getPassword());
            userDto.setRole("doctor");

            ResponseEntity<UserDto> user=userServiceInterface.createUser(userDto);

            if(user.getStatusCode()!=HttpStatus.CREATED){
                return user;
            }

            UserDto userDto1 = user.getBody();
            Doctor doctor=new Doctor();

            doctor.setUserid(userDto1.getId());

            doctor.setSpecialization(doctorPersonalDetailsDto.getSpecialization());

            doctor.setPhone(doctorPersonalDetailsDto.getPhone());
            DoctorAvailability doctorAvailability=new DoctorAvailability();

        doctorAvailability.setAvailableDays(doctorPersonalDetailsDto.getAvailability().getAvailableDays());
        doctorAvailability.setAvailableUntil(doctorPersonalDetailsDto.getAvailability().getAvailableUntil());
        doctorAvailability.setAvailableFrom(doctorPersonalDetailsDto.getAvailability().getAvailableFrom());
        doctor.setAvailability(doctorAvailability);
            try {
                Doctor doctor1=doctorRepository.save(doctor);
                doctorPersonalDetailsDto.setId(doctor1.getId());
                doctorPersonalDetailsDto.setUserid(doctor1.getUserid());
            }catch (Exception e) {
                return new ResponseEntity<>("Database Error while updating doctor",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(doctorPersonalDetailsDto, HttpStatus.CREATED);
    }

//    public ResponseEntity<?> createDoctor(DoctorDto doctorDto) {
//
//        // Map DoctorDto to Doctor entity
//        Doctor doctor = doctorMapper.toEntity(doctorDto);
//
//        // Save the doctor entity
//        doctorRepository.save(doctor);
//
//        // Map saved Doctor entity back to DTO for response
//        DoctorDto savedDoctorDto = doctorMapper.toDto(doctor);
//        return new ResponseEntity<>(savedDoctorDto, HttpStatus.CREATED);
//    }

    public ResponseEntity<?> getDoctor(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);

        // If doctor is present, map it to a DoctorDto and return it as a ResponseEntity
        if (doctor.isPresent()) {
             DoctorPersonalDetailsDto doctorPersonalDetailsDto = doctorMapper.toPersonalDto(doctor.get()); // Get the doctor and convert to DTO
            ResponseEntity<UserDto> userResponse= userServiceInterface.getUserById(doctorPersonalDetailsDto.getUserid());
            if(userResponse.getStatusCode()==HttpStatus.OK){
                UserDto userDto= userResponse.getBody();
                if(userDto!=null) {
                    doctorPersonalDetailsDto.setEmail(userDto.getEmail());
                    doctorPersonalDetailsDto.setFirstName(userDto.getFirstName());
                    doctorPersonalDetailsDto.setLastName(userDto.getLastName());
                    doctorPersonalDetailsDto.setIsActive(userDto.getIsActive());
                }
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(doctorPersonalDetailsDto); // Return the DTO in response
        }

        // If doctor is not found, return a 404 Not Found response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
    }


    @Transactional
    public ResponseEntity<?> updateDoctor(DoctorPersonalDetailsDto doctorPersonalDetailsDto) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorPersonalDetailsDto.getId());

        if (doctorOptional.isPresent()) {
            UserDto userDto=new UserDto();
            userDto.setId(doctorPersonalDetailsDto.getUserid());
            userDto.setEmail(doctorPersonalDetailsDto.getEmail());
            userDto.setFirstName(doctorPersonalDetailsDto.getFirstName());
            userDto.setLastName(doctorPersonalDetailsDto.getLastName());

           ResponseEntity<Object> user=userServiceInterface.updateUser(userDto);

           if(user.getStatusCode()!=HttpStatus.OK){
               return user;
           }

           Doctor doctor=doctorOptional.get();
           if(!doctorPersonalDetailsDto.getSpecialization().isEmpty()) {
               doctor.setSpecialization(doctorPersonalDetailsDto.getSpecialization());
           }
           if (!doctorPersonalDetailsDto.getPhone().isEmpty()) {
               doctor.setPhone(doctorPersonalDetailsDto.getPhone());
           }
           if (doctorPersonalDetailsDto.getAvailability()!=null) {
               doctor.getAvailability().setAvailableDays(doctorPersonalDetailsDto.getAvailability().getAvailableDays());
               doctor.getAvailability().setAvailableUntil(doctorPersonalDetailsDto.getAvailability().getAvailableUntil());
               doctor.getAvailability().setAvailableFrom(doctorPersonalDetailsDto.getAvailability().getAvailableFrom());
           }

            try {
                doctorRepository.save(doctor);
            }catch (Exception e) {
                return new ResponseEntity<>("Database Error while updating doctor",HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(doctorPersonalDetailsDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteDoctor(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        Map<String, String> response = new HashMap<>();

        if (doctor.isPresent()) {

            ResponseEntity<?> user =userServiceInterface.deleteUser(doctor.get().getUserid());

                response.put("message", Objects.requireNonNull(user.getBody()).toString());
                response.put("status", "error");
                return ResponseEntity.status(user.getStatusCode()).body(response);
        }
        response.put("message","Doctor Not Found, consider deleted successfully" );
        response.put("status", "success");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    public ResponseEntity<?> getAllDoctors() {
        // Fetch all doctors from the database
        List<Doctor> doctors = doctorRepository.findAll();

        // Map each doctor to a DTO and enrich with user details
        List<DoctorPersonalDetailsDto> doctorDtos = doctors.stream()
                .map(doctor -> {
                    // Map doctor entity to DTO
                    DoctorPersonalDetailsDto doctorPersonalDetailsDto = doctorMapper.toPersonalDto(doctor);

                    // Fetch additional user details using the user ID
                    ResponseEntity<UserDto> userResponse = userServiceInterface.getUserById(doctorPersonalDetailsDto.getUserid());
                    if (userResponse.getStatusCode() == HttpStatus.OK) {
                        UserDto userDto = userResponse.getBody();
                        if (userDto != null) {
                            // Enrich the doctor DTO with user details
                            doctorPersonalDetailsDto.setEmail(userDto.getEmail());
                            doctorPersonalDetailsDto.setFirstName(userDto.getFirstName());
                            doctorPersonalDetailsDto.setLastName(userDto.getLastName());
                            doctorPersonalDetailsDto.setIsActive(userDto.getIsActive());
                        }
                    }
                    return doctorPersonalDetailsDto;
                })
                .collect(Collectors.toList());

        // Return the enriched list of doctors
        return new ResponseEntity<>(doctorDtos, HttpStatus.OK);
    }


    public ResponseEntity<?> getAvailableDoctorsByDate(LocalDate dateparam) {
        String day = dateparam.getDayOfWeek().name().toLowerCase();
        day = day.substring(0, 1).toUpperCase() + day.substring(1);
        String finalDay = day;
//        List<String> days = new ArrayList<>(Arrays.asList("Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));

        List<DoctorPersonalAndAppointmentDetails> availableDoctors = doctorRepository.findAll().stream()
                .map(doctor -> {
                    // Fetch the doctor's availability
                    DoctorAvailability availability = doctor.getAvailability();

                    // Check if the doctor is available on the specified day
                    boolean isAvailableDay = availability != null &&
                            availability.getAvailableDays().contains(finalDay);
                    // If doctor is available, fetch their appointments
                    if (isAvailableDay) {
                        // Map to the DoctorPersonalAndAppointmentDetails DTO
                        DoctorPersonalAndAppointmentDetails doctorDetails = new DoctorPersonalAndAppointmentDetails();
                        doctorDetails.setId(doctor.getId());
                        doctorDetails.setUserid(doctor.getUserid());
                        doctorDetails.setAvailability(doctorMapper.toDto(doctor.getAvailability()));
                        ResponseEntity<UserDto> userResponse= userServiceInterface.getUserById(doctor.getUserid());

                        if(userResponse.getStatusCode()==HttpStatus.OK){
                            UserDto userDto= userResponse.getBody();
                            if(userDto!=null) {
                                doctorDetails.setFirstName(userDto.getFirstName());
                                doctorDetails.setLastName(userDto.getLastName());
                            }
                        }else{
                            doctorDetails.setFirstName("N/A");
                            doctorDetails.setLastName("N/A");
                        }
                        LocalDate today = LocalDate.now();
                        LocalTime startOfDay = LocalTime.from(today.atStartOfDay());
                        LocalTime endOfDay = LocalTime.from(today.atTime(LocalTime.MAX));
                        // Fetch appointments for the doctor on the specified date
                        ResponseEntity<Object> appointmentResponse = doctorAppointmentServiceInterface.getAppointmentsOfDoctor(
                                doctor.getId(), dateparam, startOfDay, endOfDay); // Assuming dateparam is a LocalDate
                        List<AppointmentDto> appointmentDtos = (List<AppointmentDto>) appointmentResponse.getBody();

                        if (appointmentResponse.getStatusCode() == HttpStatus.OK) {

//                            if (appointmentDtos != null) {
//                                List<AppointmentInfo> appointmentInfos = appointmentDtos.stream().map(appointmentDto -> {
//                                    AppointmentInfo appointmentInfo = new AppointmentInfo();
//                                    appointmentInfo.setAppointmentTime(appointmentDto.getAppointmentTime());
//                                    appointmentInfo.setStatus(appointmentDto.getStatus());
//                                    return appointmentInfo;
//                                }).collect(Collectors.toList());
//                                doctorDetails.setAppointment(appointmentInfos);
//                            } else{
//                                doctorDetails.setAppointment(new ArrayList<AppointmentInfo>());
//                            }

                     doctorDetails.setAppointment(appointmentDtos);

                        }
                        return doctorDetails;
                    }
                    return null; // Return null if doctor is not available or if appointments are not found
                })
                .filter(Objects::nonNull) // Filter out null values where doctor was not available
                .collect(Collectors.toList());


        return new ResponseEntity<>(availableDoctors, HttpStatus.OK);
    }

    public ResponseEntity<?> getAvailableDoctors(DoctorAvailabilityDto availabilityDto) {
        LocalTime fromTime = LocalTime.parse(availabilityDto.getAvailableFrom());
        LocalTime untilTime = LocalTime.parse(availabilityDto.getAvailableUntil());

        List<DoctorDto> availableDoctors = doctorRepository.findAll().stream()
                .filter(doctor -> {

                    DoctorAvailability availability = doctor.getAvailability();

                    // Check if the doctor is available on the specified days
                    boolean isAvailableDay = availability != null &&
                            availability.getAvailableDays().contains(availabilityDto.getAvailableDays());

                    // Check if the doctor is available within the specified time range
                    boolean isAvailableTime = availability != null &&
                            LocalTime.parse(availability.getAvailableFrom()).compareTo(fromTime) <= 0 &&
                            LocalTime.parse(availability.getAvailableUntil()).compareTo(untilTime) >= 0;

                    return isAvailableDay && isAvailableTime;
                })
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(availableDoctors, HttpStatus.OK);
    }

    public ResponseEntity<?> setDoctorAvailability(Long doctorId, DoctorAvailabilityDto availabilityDto) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);


        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            DoctorAvailability availability = doctorMapper.toEntity(availabilityDto);
            availability.setDoctor(doctor); // Ensure bidirectional relationship is set

            doctor.setAvailability(availability);
            doctorRepository.save(doctor);
            DoctorDto updatedDoctorDto = doctorMapper.toDto(doctor);
            return ResponseEntity.status(HttpStatus.OK).body(updatedDoctorDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found with ID: " + doctorId);
        }
    }

    public ResponseEntity<?> getDoctorAppointments( Long doctorId,
                                                  LocalDate date,
                                                   LocalTime startTime,
                                                    LocalTime endTime) {
    return doctorAppointmentServiceInterface.getAppointmentsOfDoctor(doctorId,date,startTime,endTime);
    }


    public ResponseEntity<?> getDoctorByUserid(Long userid) {

        Optional<Doctor> doctor = doctorRepository.findByUserid(userid);

        // If doctor is present, map it to a DoctorDto and return it as a ResponseEntity
        if (doctor.isPresent()) {
            DoctorPersonalDetailsDto doctorPersonalDetailsDto = doctorMapper.toPersonalDto(doctor.get()); // Get the doctor and convert to DTO
            ResponseEntity<UserDto> userResponse= userServiceInterface.getUserById(doctorPersonalDetailsDto.getUserid());
            if(userResponse.getStatusCode()==HttpStatus.OK){
                UserDto userDto= userResponse.getBody();
                if(userDto!=null) {
                    doctorPersonalDetailsDto.setEmail(userDto.getEmail());
                    doctorPersonalDetailsDto.setFirstName(userDto.getFirstName());
                    doctorPersonalDetailsDto.setLastName(userDto.getLastName());
                }
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(doctorPersonalDetailsDto); // Return the DTO in response
        }

        // If doctor is not found, return a 404 Not Found response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
    }
}
