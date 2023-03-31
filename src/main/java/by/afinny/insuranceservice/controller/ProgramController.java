package by.afinny.insuranceservice.controller;

import by.afinny.insuranceservice.dto.ProgramDto;
import by.afinny.insuranceservice.service.ProgramService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/insurance-program")
public class ProgramController {

    private final ProgramService programService;

    @GetMapping("/new-medicine/programs")
    public ResponseEntity<List<ProgramDto>> getMedicinePrograms(@RequestParam Integer pageNumber,
                                                                @RequestParam Integer pageSize,
                                                                @RequestParam Boolean emergencyHospitalization,
                                                                @RequestParam Boolean dentalService,
                                                                @RequestParam Boolean telemedicine,
                                                                @RequestParam Boolean emergencyMedicalCare,
                                                                @RequestParam Boolean callingDoctor,
                                                                @RequestParam Boolean outpatientService) {
        List<ProgramDto> medicineProgramDtos = programService.getMedicinePrograms(pageNumber,
                pageSize,
                emergencyHospitalization,
                dentalService,
                telemedicine,
                emergencyMedicalCare,
                callingDoctor,
                outpatientService);
        return ResponseEntity.ok(medicineProgramDtos);
    }
}
