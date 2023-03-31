package by.afinny.insuranceservice.controller;

import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.ResponseNewMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.dto.ResponseTravelPolicyDto;
import by.afinny.insuranceservice.service.ApplicationService;
import by.afinny.insuranceservice.service.InsurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/insurance-order")
public class InsuranceOrderController {

    private final InsurantService insurantService;
    private final ApplicationService applicationService;

    @PostMapping("/new-cars")
    public ResponseEntity<RequestNewPolicy> createNewPolicy(@RequestBody RequestNewPolicy requestNewPolicy) {
        insurantService.createNewPolicy(requestNewPolicy);
        return ResponseEntity.ok(requestNewPolicy);
    }

    @PostMapping("/new-medicine")
    public ResponseEntity<ResponseNewMedicinePolicyDto> registerNewMedicinePolicy(@RequestBody RequestMedicinePolicyDto requestMedicinePolicyDto) {
        return ResponseEntity.ok().body(applicationService.registerNewMedicinePolicy(requestMedicinePolicyDto));
    }

    @PostMapping("/new-property")
    public ResponseEntity<ConsumerNewRealEstatePolicy> createNewRealEstatePolicy (@RequestBody RequestNewRealEstatePolicy requestNewRealEstatePolicy){
        insurantService.createNewRealEstatePolicy(requestNewRealEstatePolicy);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/new-travel-program")
    public ResponseEntity<ResponseTravelPolicyDto> createNewTravelPolicy(@RequestBody RequestTravelPolicyDto requestTravelPolicyDto) {
        ResponseTravelPolicyDto responseTravelPolicyDto = applicationService.createNewTravelPolicy(requestTravelPolicyDto);
        return ResponseEntity.ok(responseTravelPolicyDto);
    }
}
