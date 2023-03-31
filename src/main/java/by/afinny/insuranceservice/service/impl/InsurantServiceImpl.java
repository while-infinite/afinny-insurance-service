package by.afinny.insuranceservice.service.impl;

import by.afinny.insuranceservice.dto.PolicyInfoDto;
import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.RequestNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.ResponseUserPolicyDto;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.kafka.ProducerNewPolicyEvent;
import by.afinny.insuranceservice.dto.kafka.ProducerNewRealEstatePolicy;
import by.afinny.insuranceservice.entity.Agreement;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.Base;
import by.afinny.insuranceservice.entity.CarInsurance;
import by.afinny.insuranceservice.entity.Factor;
import by.afinny.insuranceservice.entity.Insurant;
import by.afinny.insuranceservice.entity.Insured;
import by.afinny.insuranceservice.entity.OSAGO;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.PropertyInsurance;
import by.afinny.insuranceservice.entity.SumAssignment;
import by.afinny.insuranceservice.entity.constant.BaseRate;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.FactorName;
import by.afinny.insuranceservice.entity.constant.FactorRate;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.mapper.PolicyMapper;
import by.afinny.insuranceservice.mapper.SumAssignmentMapper;
import by.afinny.insuranceservice.mapper.UserPolicyMapper;
import by.afinny.insuranceservice.repository.AgreementRepository;
import by.afinny.insuranceservice.repository.ApplicationRepository;
import by.afinny.insuranceservice.repository.BaseRepository;
import by.afinny.insuranceservice.repository.CarInsuranceRepository;
import by.afinny.insuranceservice.repository.FactorRepository;
import by.afinny.insuranceservice.repository.InsurantRepository;
import by.afinny.insuranceservice.repository.InsuredRepository;
import by.afinny.insuranceservice.repository.OSAGORepository;
import by.afinny.insuranceservice.repository.PersonRepository;
import by.afinny.insuranceservice.repository.PropertyInsuranceRepository;
import by.afinny.insuranceservice.repository.SumAssignmentRepository;
import by.afinny.insuranceservice.service.InsurantService;
import by.afinny.insuranceservice.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsurantServiceImpl implements InsurantService {

    private final InsurantRepository insurantRepository;
    private final PersonRepository personRepository;
    private final ApplicationRepository applicationRepository;
    private final UserPolicyMapper userPolicyMapper;
    private final PolicyMapper policyMapper;
    private final OSAGORepository osagoRepository;
    private final InsuredRepository insuredRepository;
    private final FactorRepository factorRepository;
    private final CarInsuranceRepository carInsuranceRepository;
    private final BaseRepository baseRepository;
    private final ApplicationEventPublisher eventPublisher;

    private final SumAssignmentMapper sumAssignmentMapper;
    private final SumAssignmentRepository sumAssignmentRepository;
    private final PropertyInsuranceRepository propertyInsuranceRepository;
    private final AgreementRepository agreementRepository;
    private final KafkaService kafkaService;


    @Override
    public List<ResponseUserPolicyDto> getAllUserPolicies(String clientId) {
        log.info("getAllUserPolicies() method invoke");
        Insurant insurant = insurantRepository.findInsurantByClientId(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Insurant with clientId " + clientId + " was not found"));

        Person person = personRepository.findById(insurant.getId())
                .orElseThrow(() -> new EntityNotFoundException("Person with personId " + insurant.getId() + " was not found"));

        List<Application> applications = person.getApplications();

        return userPolicyMapper.applicationsToUserPolicyDto(applications);
    }

    @Override
    public PolicyInfoDto getPolicyInformation(UUID clientId, UUID applicationId) {
        log.info("getPolicyInformation() method invoke");
        Application application = applicationRepository.findApplicationByClientIdAndApplicationId(clientId.toString(), applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Policy with application ID " + applicationId +
                        " for client id " + clientId + " wasn't found"));
        Agreement agreement = application.getAgreement();
        CarInsurance carInsurance = application.getCarInsurance();
        OSAGO osago = carInsurance.getOsago();
        PropertyInsurance propertyInsurance = application.getPropertyInsurance();
        SumAssignment sumAssignment = propertyInsurance.getSumAssignment();
        List<Person> people = application.getPeople();
        Person person = people.get(0);
        Insurant insurant = person.getInsurant();
        return policyMapper.toPolicyInfoDto(application, agreement, osago, insurant, sumAssignment);
    }

    @Override
    public void createNewPolicy(RequestNewPolicy requestNewPolicy) {
        log.info("createNewPolicy() method invoke");
        kafkaService.sendToKafka(requestNewPolicy.getClientId(), requestNewPolicy);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveNewPolicy(ConsumerNewPolicyEvent event) {
        log.info("saveNewPolicy() invoke");
        Person person = policyMapper.toPersonFromEvent(event);
        Application application = policyMapper.toApplication(event);
        application.setPeople(List.of(person));
        applicationRepository.save(application);
        personRepository.save(person);
        Insurant insurant = policyMapper.toInsurant(event, person);
        Insured insured = policyMapper.toInsured(event, person);
        insurantRepository.save(insurant);
        insuredRepository.save(insured);
        Factor factor = getFactor(event);
        Base base = getBase(event);
        factorRepository.save(factor);
        baseRepository.save(base);
        CarInsurance carInsurance = policyMapper.toCarInsurance(application, base);
        carInsuranceRepository.save(carInsurance);
        OSAGO osago = policyMapper.toOSAGO(event, application, carInsurance);
        osagoRepository.save(osago);
    }

    @Override
    public void createNewRealEstatePolicy(RequestNewRealEstatePolicy requestNewRealEstatePolicy) {
        log.info("createNewRealEstatePolicy() method invoke");
        kafkaService.sendToKafka(requestNewRealEstatePolicy);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveNewRealEstatePolicy(ConsumerNewRealEstatePolicy consumerNewRealEstatePolicy) {
        log.info("saveNewRealEstatePolicy() method invoke");
        Application application = policyMapper.toApplication(consumerNewRealEstatePolicy);
        Person person = policyMapper.toPersonFromEvent(consumerNewRealEstatePolicy);
        personRepository.save(person);
        log.debug("Person saved");
        application.setPeople(List.of(person));
        applicationRepository.save(application);
        log.debug("Application saved");
        Insurant insurant = policyMapper.toInsurant(consumerNewRealEstatePolicy,person);
        SumAssignment sumAssignment = sumAssignmentMapper.toSumAssignmentFromEvent(consumerNewRealEstatePolicy);
        insurantRepository.save(insurant);
        log.debug("Insurant saved");
        sumAssignmentRepository.save(sumAssignment);
        log.debug("SumAssignment saved");
        PropertyInsurance propertyInsurance = policyMapper.toPropertyInsurance(application,sumAssignment);
        propertyInsuranceRepository.save(propertyInsurance);
        Agreement agreement = policyMapper.toAgreement(consumerNewRealEstatePolicy,application);
        if (consumerNewRealEstatePolicy.getInsuranceStatus().equals(String.valueOf(InsuranceStatus.APPROVED))){
            agreement.setIsActive(true);
        }else {
            agreement.setIsActive(false);
        }
        agreementRepository.save(agreement);
    }

    private Factor getFactor(ConsumerNewPolicyEvent event) {
        log.info("getFactor() method invoke");
        FactorRate factorRate = getFactorRate(event.getFactorName());
        return new Factor(FactorName.valueOf(event.getFactorName()), factorRate);
    }

    private Base getBase(ConsumerNewPolicyEvent event) {
        log.info("getBase() method invoke");
        BaseRate baseRate = getBaseRate(event.getCategoryGroup());
        Factor factor = getFactor(event);
        return new Base(CategoryGroup.valueOf(event.getCategoryGroup()), baseRate, factor);
    }


    private FactorRate getFactorRate(String factorName) {
        log.info("getFactorRate() method invoke");
        FactorRate factorRate = null;
        switch (factorName) {
            case "HARD":
                factorRate = FactorRate.K1;
                break;
            case "MIDDLE":
                factorRate = FactorRate.K2;
                break;
            case "LIGHT":
                factorRate = FactorRate.K3;
                break;
        }
        return factorRate;
    }

    private BaseRate getBaseRate(String category) {
        log.info("getBaseRate() method invoke");
        BaseRate baseRate = null;
        switch (category) {
            case "PASSENGER_AUTOMOBILE":
                baseRate = BaseRate.RUB_5000;
                break;
            case "TRUCK":
                baseRate = BaseRate.RUB_7000;
        }
        return baseRate;
    }


}