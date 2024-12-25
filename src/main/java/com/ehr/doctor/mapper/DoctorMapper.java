package com.ehr.doctor.mapper;


import com.ehr.doctor.dto.DoctorDto;
import com.ehr.doctor.dto.DoctorAvailabilityDto;
import com.ehr.doctor.dto.DoctorPersonalDetailsDto;
import com.ehr.doctor.model.Doctor;
import com.ehr.doctor.model.DoctorAvailability;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // This tells Spring to automatically discover and inject this mapper
public interface DoctorMapper {
    // Mapping between DoctorDto and Doctor
    Doctor toEntity(DoctorDto doctorDto);

    DoctorDto toDto(Doctor doctor);

    Doctor toEntity(DoctorPersonalDetailsDto doctorPersonalDetailsDto);

    DoctorPersonalDetailsDto toPersonalDto(Doctor doctor);

    // Mapping between DoctorAvailabilityDto and DoctorAvailability
    DoctorAvailability toEntity(DoctorAvailabilityDto doctorAvailabilityDto);

    DoctorAvailabilityDto toDto(DoctorAvailability doctorAvailability);
}
