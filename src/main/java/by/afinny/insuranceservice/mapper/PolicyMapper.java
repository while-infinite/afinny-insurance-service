package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.PolicyInfoDto;
import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.RequestNewRealEstatePolicy;
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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PolicyMapper {

    PolicyInfoDto toPolicyInfoDto(Application application, Agreement agreement,
                                  OSAGO osago, Insurant insurant, SumAssignment sumAssignment);

    Application toApplication(ConsumerNewPolicyEvent event);

    Application toApplication(ConsumerNewRealEstatePolicy event);

    Person toPerson(RequestNewPolicy requestNewPolicy);


    Person toPersonFromEvent(ConsumerNewRealEstatePolicy event);

    Person toPersonFromEvent(ConsumerNewPolicyEvent event);

    @Mapping(source = "person.id", target = "id")
    Insurant toInsurant(ConsumerNewPolicyEvent event, Person person);

    @Mapping(source = "person.id", target = "id")
    Insurant toInsurant(ConsumerNewRealEstatePolicy consumerNewRealEstatePolicy, Person person);

    @Mapping(source = "application.id", target = "id")
    OSAGO toOSAGO(ConsumerNewPolicyEvent event, Application application, CarInsurance carInsurance);

    @Mapping(source = "person.id", target = "id")
    @Mapping(source = "event.drivingExperience", target = "drivingExperience")
    Insured toInsured(ConsumerNewPolicyEvent event, Person person);

    @Mapping(source = "application.id", target = "id")
    CarInsurance toCarInsurance(Application application, Base base);

    @Mapping(source = "application.id", target = "id")
    PropertyInsurance toPropertyInsurance (Application application,SumAssignment sumAssignment);

    ProducerNewPolicyEvent toProducerNewPolicyEvent(String id, RequestNewPolicy requestNewPolicy);

    ProducerNewRealEstatePolicy toProducerNewRealEstatePolicy(RequestNewRealEstatePolicy requestNewRealEstatePolicy);

    @Mapping(source = "application.id",target = "id")
    @Mapping(source = "consumerNewRealEstatePolicy.startDate",target = "agreementDate")
    Agreement toAgreement (ConsumerNewRealEstatePolicy consumerNewRealEstatePolicy, Application application);
}