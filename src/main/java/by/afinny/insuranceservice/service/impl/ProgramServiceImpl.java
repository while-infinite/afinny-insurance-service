package by.afinny.insuranceservice.service.impl;

import by.afinny.insuranceservice.dto.ProgramDto;
import by.afinny.insuranceservice.entity.Program;
import by.afinny.insuranceservice.mapper.ProgramMapper;
import by.afinny.insuranceservice.repository.ProgramRepository;
import by.afinny.insuranceservice.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;

    @Override
    public List<ProgramDto> getMedicinePrograms(Integer pageNumber,
                                                Integer pageSize,
                                                Boolean emergencyHospitalization,
                                                Boolean dentalService,
                                                Boolean telemedicine,
                                                Boolean emergencyMedicalCare,
                                                Boolean callingDoctor,
                                                Boolean outpatientService) {
        log.info("getMedicinePrograms() method invoke");
        Specification<Program> specification = programRepository.getProgramSpecification(emergencyHospitalization,
                dentalService,
                telemedicine,
                emergencyMedicalCare,
                callingDoctor,
                outpatientService);
        Page<Program> programs = programRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return programMapper.toProgramDtoList(programs.stream().collect(Collectors.toList()));
    }
}
