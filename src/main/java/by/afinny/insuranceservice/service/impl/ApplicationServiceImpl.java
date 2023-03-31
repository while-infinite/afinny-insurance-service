package by.afinny.insuranceservice.service.impl;

import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.dto.ResponseApplicationInsuranceTypeDto;
import by.afinny.insuranceservice.dto.ResponseNewMedicinePolicyDto;
import by.afinny.insuranceservice.dto.ResponsePaymentDetailsDto;
import by.afinny.insuranceservice.dto.ResponseRejectionLetterDto;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewMedicineEvent;
import by.afinny.insuranceservice.dto.kafka.ProducerNewMedicineEvent;
import by.afinny.insuranceservice.entity.Agreement;
import by.afinny.insuranceservice.dto.ResponseTravelPolicyDto;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.Insurant;
import by.afinny.insuranceservice.entity.Insured;
import by.afinny.insuranceservice.entity.MedicalInsurance;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.TravelInsurance;
import by.afinny.insuranceservice.mapper.ApplicationMapper;
import by.afinny.insuranceservice.repository.AgreementRepository;
import by.afinny.insuranceservice.repository.ApplicationRepository;
import by.afinny.insuranceservice.repository.MedicalInsuranceRepository;
import by.afinny.insuranceservice.repository.PersonRepository;
import by.afinny.insuranceservice.repository.TravelInsuranceRepository;
import by.afinny.insuranceservice.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationRepository applicationRepository;
    private final MedicalInsuranceRepository medicalInsuranceRepository;
    private final TravelInsuranceRepository travelInsuranceRepository;
    private final PersonRepository personRepository;
    private final ApplicationMapper applicationMapper;
    private final AgreementRepository agreementRepository;

    @Transactional
    @Override
    public List<ResponseApplicationInsuranceTypeDto> getInsuranceTypes() {
        log.info("getInsuranceTypes() invoked");
        List<Application> allByInsuranceType = applicationRepository.findAll();
        return applicationMapper.toResponseTypes(allByInsuranceType);
    }

    @Transactional
    @Override
    public ResponseRejectionLetterDto getRejectionLetter(UUID clientId, UUID applicationId) {
        log.info("getRejectionLetter() invoked");
        return applicationMapper.toResponseLetter(applicationRepository.
                findApplicationByClientIdAndApplicationId(clientId.toString(), applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application with ID " + applicationId +
                        " for client id " + clientId + " wasn't found")));
    }

    @Override
    public ResponsePaymentDetailsDto getPaymentDetails(UUID clientId, UUID applicationId) {
        log.info("getPaymentDetails() invoked");
        return applicationMapper.toResponsePaymentDetails(applicationRepository.
                findApplicationByClientIdAndApplicationId(clientId.toString(), applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application with ID " + applicationId +
                        " for client id " + clientId + " wasn't found")));
    }

    @Transactional
    @Override
    public ResponseNewMedicinePolicyDto registerNewMedicinePolicy(RequestMedicinePolicyDto requestMedicinePolicyDto) {
        log.info("registerNewMedicinePolicy() method is invoked");
        Application application = applicationMapper.toApplication(requestMedicinePolicyDto);
        Person person = applicationMapper.toPerson(requestMedicinePolicyDto);
        if (application.getPeople() == null) {
            application.setPeople(List.of(person));
        } else {
            application.getPeople().add(person);
        }
        application.setInsuranceStatus(InsuranceStatus.PENDING);
        application.setRegistrationDate(LocalDate.now());
        applicationRepository.save(application);
        log.debug("Saved application");

        Insured insured = applicationMapper.toInsured(requestMedicinePolicyDto);
        insured.setId(person.getId());
        person.setInsured(insured);
        Insurant insurant = applicationMapper.toInsurant(requestMedicinePolicyDto);
        insurant.setId(person.getId());
        person.setInsurant(insurant);
        personRepository.save(person);
        log.debug("Saved person");
        log.debug("Saved insured by cascade");
        log.debug("Saved insurant by cascade");


        MedicalInsurance medicalInsurance = applicationMapper.toMedicalInsurance(requestMedicinePolicyDto);
        medicalInsurance.setId(application.getId());
        medicalInsuranceRepository.save(medicalInsurance);
        log.debug("Saved medical insurance");
        ResponseNewMedicinePolicyDto responseNewMedicinePolicyDto = applicationMapper.toResponseNewMedicinePolicyDto(application, person, insurant, insured);
        sendToKafka(application.getId(), responseNewMedicinePolicyDto);
        return responseNewMedicinePolicyDto;
    }

    @Transactional
    @Override
    public void saveNewMedicinePolicy(ConsumerNewMedicineEvent consumerNewMedicineEvent) {
        log.info("");
        Agreement agreement = applicationMapper.toAgreement(consumerNewMedicineEvent);
        if (consumerNewMedicineEvent.getInsuranceStatus().equals(String.valueOf(InsuranceStatus.APPROVED))) {
            agreement.setIsActive(true);
        } else {
            agreement.setIsActive(false);
        }
        agreement.setAgreementDate(LocalDate.now());
        agreementRepository.save(agreement);
        Application application = applicationRepository.findById(consumerNewMedicineEvent.getApplicationId())
                .orElseThrow(() -> new EntityNotFoundException("Application with id " + consumerNewMedicineEvent.getApplicationId() + " was not found"));
        application.setInsuranceStatus(InsuranceStatus.valueOf(consumerNewMedicineEvent.getInsuranceStatus()));
        application.setStartDate(consumerNewMedicineEvent.getStartDate());
        applicationRepository.save(application);
    }


    private void sendToKafka(UUID id, ResponseNewMedicinePolicyDto responseNewMedicinePolicyDto) {
        log.info("sendToKafka() method invoke");
        ProducerNewMedicineEvent event = applicationMapper.toProducerNewMedicineEvent(id, responseNewMedicinePolicyDto);
        eventPublisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void deleteApplication(UUID clientId, UUID applicationId) {
        log.info("deleteApplication() invoked");
        if (applicationRepository.findApplicationByClientIdAndApplicationId(clientId.toString(), applicationId).isPresent()) {
            applicationRepository.deleteById(applicationId);
        } else {
            throw new EntityNotFoundException("Application with ID " + applicationId +
                    " for client id " + clientId + " wasn't found");
        }
    }

    @Transactional
    @Override
    public ResponseTravelPolicyDto createNewTravelPolicy(RequestTravelPolicyDto travelPolicyDto) {
        log.info("createNewTravelPolicy() method is invoked");
        Application application = applicationMapper.toApplication(travelPolicyDto);
        Person person = applicationMapper.toPerson(travelPolicyDto);
        if (application.getPeople() == null) {
            application.setPeople(List.of(person));
        } else {
            application.getPeople().add(person);
        }
        applicationRepository.save(application);
        log.debug("Saved application");

        Insured insured = applicationMapper.toInsured(travelPolicyDto);
        insured.setId(person.getId());
        person.setInsured(insured);
        Insurant insurant = applicationMapper.toInsurant(travelPolicyDto);
        insurant.setId(person.getId());
        person.setInsurant(insurant);
        personRepository.save(person);
        log.debug("Saved person");
        log.debug("Saved insured by cascade");
        log.debug("Saved insurant by cascade");

        TravelInsurance travelInsurance = applicationMapper.toTravelInsurance(travelPolicyDto);
        travelInsurance.setId(application.getId());
        travelInsuranceRepository.save(travelInsurance);
        log.debug("Saved travel insurance");
        sendToKafka(travelPolicyDto);

        return applicationMapper.toResponseTravelPolicyDto(application, insured, insurant, person, travelInsurance);
    }

    private void sendToKafka(RequestTravelPolicyDto requestTravelPolicyDto) {
        log.info("sendToKafka() method invoke");
        eventPublisher.publishEvent(requestTravelPolicyDto);
    }
}