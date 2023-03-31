package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.ProgramDto;

import java.util.List;

public interface ProgramService {

    List<ProgramDto> getMedicinePrograms(Integer pageNumber,
                                         Integer pageSize,
                                         Boolean emergencyHospitalization,
                                         Boolean dentalService,
                                         Boolean telemedicine,
                                         Boolean emergencyMedicalCare,
                                         Boolean callingDoctor,
                                         Boolean outpatientService);
}
