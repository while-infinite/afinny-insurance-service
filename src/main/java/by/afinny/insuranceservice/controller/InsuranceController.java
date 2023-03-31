package by.afinny.insuranceservice.controller;

import by.afinny.insuranceservice.dto.PolicyInfoDto;
import by.afinny.insuranceservice.dto.ResponseApplicationInsuranceTypeDto;
import by.afinny.insuranceservice.dto.ResponsePaymentDetailsDto;
import by.afinny.insuranceservice.dto.ResponseRejectionLetterDto;
import by.afinny.insuranceservice.dto.ResponseUserPolicyDto;
import by.afinny.insuranceservice.service.ApplicationService;
import by.afinny.insuranceservice.service.InsurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/insurance")
public class InsuranceController {

    public static final String URL_INSURANCE = "/auth/insurance/";
    public static final String URL_APPLICATION_ID = "{application-id}";
    public static final String URL_TYPES = "/types";
    public static final String URL_REPORT = "/{application-id}/report";
    public static final String URL_REVOCATION = "/{application-id}/revocation";

    private final InsurantService insurantService;
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<ResponseUserPolicyDto>> getAllUserPolicies(@RequestParam String clientId) {
        List<ResponseUserPolicyDto> responseAllPolicies = insurantService.getAllUserPolicies(clientId);
        return ResponseEntity.ok(responseAllPolicies);
    }

    @GetMapping("/types")
    public ResponseEntity<List<ResponseApplicationInsuranceTypeDto>> getInsuranceTypes() {
        List<ResponseApplicationInsuranceTypeDto> insuranceTypes = applicationService.getInsuranceTypes();
        return ResponseEntity.ok(insuranceTypes);
    }

    @GetMapping("/{application-id}/report")
    public ResponseEntity<ResponseRejectionLetterDto> getRejectionLetter(@RequestParam UUID clientId,
                                                                         @PathVariable("application-id") UUID applicationId) {
        ResponseRejectionLetterDto response = applicationService.getRejectionLetter(clientId, applicationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<PolicyInfoDto> getPolicyInformation(@RequestParam UUID clientId,
                                                              @PathVariable UUID applicationId) {
        PolicyInfoDto policyInfoDto = insurantService.getPolicyInformation(clientId, applicationId);
        return ResponseEntity.ok(policyInfoDto);
    }

    @GetMapping("/{application-id}/payment-details")
    public ResponseEntity<ResponsePaymentDetailsDto> getPaymentDetails(@RequestParam UUID clientId,
                                                                       @PathVariable("application-id") UUID applicationId) {
        ResponsePaymentDetailsDto paymentDetailsDto = applicationService.getPaymentDetails(clientId, applicationId);
        return ResponseEntity.ok(paymentDetailsDto);
    }

    @DeleteMapping("/{application-id}/revocation")
    public ResponseEntity<Void> cancelPolicyApplication(@RequestParam UUID clientId,
                                                        @PathVariable("application-id") UUID applicationId) {
        applicationService.deleteApplication(clientId, applicationId);
        return ResponseEntity.noContent().build();
    }

}