package by.afinny.insuranceservice.integration.controller;

import by.afinny.insuranceservice.config.annotation.TestWithPostgresContainer;
import by.afinny.insuranceservice.dto.PolicyInfoDto;
import by.afinny.insuranceservice.dto.ResponseApplicationInsuranceTypeDto;
import by.afinny.insuranceservice.dto.ResponsePaymentDetailsDto;
import by.afinny.insuranceservice.dto.ResponseRejectionLetterDto;
import by.afinny.insuranceservice.dto.ResponseUserPolicyDto;
import by.afinny.insuranceservice.entity.Agreement;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.Base;
import by.afinny.insuranceservice.entity.CarInsurance;
import by.afinny.insuranceservice.entity.Factor;
import by.afinny.insuranceservice.entity.Insurant;
import by.afinny.insuranceservice.entity.OSAGO;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.PropertyInsurance;
import by.afinny.insuranceservice.entity.SumAssignment;
import by.afinny.insuranceservice.entity.constant.BaseRate;
import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.FactorName;
import by.afinny.insuranceservice.entity.constant.FactorRate;
import by.afinny.insuranceservice.entity.constant.InsuranceCountry;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.mapper.ApplicationMapper;
import by.afinny.insuranceservice.mapper.PolicyMapper;
import by.afinny.insuranceservice.mapper.UserPolicyMapper;
import by.afinny.insuranceservice.repository.AgreementRepository;
import by.afinny.insuranceservice.repository.ApplicationRepository;
import by.afinny.insuranceservice.repository.BaseRepository;
import by.afinny.insuranceservice.repository.CarInsuranceRepository;
import by.afinny.insuranceservice.repository.FactorRepository;
import by.afinny.insuranceservice.repository.InsurantRepository;
import by.afinny.insuranceservice.repository.OSAGORepository;
import by.afinny.insuranceservice.repository.PersonRepository;
import by.afinny.insuranceservice.repository.PropertyInsuranceRepository;
import by.afinny.insuranceservice.repository.SumAssignmentRepository;
import by.afinny.insuranceservice.service.ApplicationService;
import by.afinny.insuranceservice.service.InsurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestWithPostgresContainer
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Integration test for InsuranceController")
public class InsuranceControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InsurantRepository insurantRepository;

    @Autowired
    private UserPolicyMapper userPolicyMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private CarInsuranceRepository carInsuranceRepository;

    @Autowired
    private OSAGORepository osagoRepository;

    @Autowired
    private BaseRepository baseRepository;

    @Autowired
    private FactorRepository factorRepository;
    @Autowired
    private SumAssignmentRepository sumAssignmentRepository;
    @Autowired
    PropertyInsuranceRepository propertyInsuranceRepository;
    @Autowired
    PolicyMapper policyMapper;

   @Autowired
    private InsurantService insurantService;

   @Autowired
    private ApplicationService applicationService;


    private Person person;
    private Insurant insurant;
    private Application application;
    private String CLIENT_ID = "8fae9356-91bb-11ed-a1eb-0242ac120002";
    private UUID APPLICATION_ID;
    private Agreement agreement;
    private OSAGO osago;
    private SumAssignment sumAssignment;
    private Base base;
    private Factor factor;
    private CarInsurance carInsurance;


    @BeforeEach
    void save() {
        person = Person.builder()
                .firstName("FirstName")
                .middleName("MiddleName")
                .lastName("LastName")
                .build();
        insurant = Insurant.builder()
                .documentType(DocumentType.INN)
                .documentNumber("123456")
                .email("mail@mail.ru")
                .phoneNumber("79992095590")
                .clientId(CLIENT_ID)
                .build();
        application = Application.builder()
                .insuranceSum(BigDecimal.valueOf(300000).movePointLeft(2))
                .registrationDate(LocalDate.now().minusDays(2))
                .periodOfInsurance(Period.TWELVE_MONTHS)
                .paymentCycle(Period.TWELVE_MONTHS)
                .policySum(BigDecimal.valueOf(200000).movePointLeft(2))
                .insuranceStatus(InsuranceStatus.PENDING)
                .region("region")
                .district("distr")
                .city("city")
                .street("str")
                .houseNumber("2")
                .flatNumber("2")
                .startDate(LocalDate.now())
                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                .failureDiagnosisReport("fail")
                .insuranceCountry(InsuranceCountry.UK)
                .lastDate(LocalDate.now().plusDays(100))
                .build();
        agreement = Agreement.builder()
                .number("222222")
                .agreementDate(LocalDate.now().minusDays(2))
                .isActive(true)
                .build();
        osago = OSAGO.builder()
                .capacityGroup(CapacityGroup.OVER_120_TO_150_INCLUSIVE)
                .categoryGroup(CategoryGroup.PASSENGER_AUTOMOBILE)
                .isWithInsuredAccident(false)
                .model("model")
                .carNumber("1234567")
                .build();
        sumAssignment = SumAssignment.builder()
                .id(1)
                .isFlat(true)
                .name(ItemOfExpense.FLAT_WALLS)
                .generalSum(BigDecimal.valueOf(2000).movePointLeft(2))
                .sum(BigDecimal.valueOf(3000).movePointLeft(2))
                .minSum(BigDecimal.valueOf(1000).movePointLeft(2))
                .maxSum(BigDecimal.valueOf(4000).movePointLeft(2))
                .defaultSum(BigDecimal.valueOf(2000).movePointLeft(2))
                .build();
        factor = Factor.builder()
                .name(FactorName.HARD)
                .rate(FactorRate.K2)
                .build();
        base = Base.builder()
                .name(CategoryGroup.PASSENGER_AUTOMOBILE)
                .rate(BaseRate.RUB_5000)
                .factor(factor)
                .build();
        carInsurance = CarInsurance.builder()
                .base(base)
                .build();


        application.setPeople(List.of(person));
        person.setApplications(List.of(application));
        application = applicationRepository.save(application);
        APPLICATION_ID = application.getId();
        agreement.setId(APPLICATION_ID);
        agreement.setApplication(application);
        agreement = agreementRepository.save(agreement);
        application.setAgreement(agreement);
        insurant.setPerson(person);
        insurant.setId(person.getId());
        insurant = insurantRepository.save(insurant);
        factorRepository.save(factor);
        baseRepository.save(base);
        carInsurance.setId(application.getId());
        carInsurance.setApplication(application);
        carInsurance = carInsuranceRepository.save(carInsurance);
        osago.setId(APPLICATION_ID);
        osago.setCarInsurance(carInsurance);
        osago = osagoRepository.save(osago);
        sumAssignment = sumAssignmentRepository.save(sumAssignment);
        PropertyInsurance propertyInsurance = new PropertyInsurance(APPLICATION_ID, application, sumAssignment);
        propertyInsuranceRepository.save(propertyInsurance);
        application.setCarInsurance(carInsurance);
        application.setAgreement(agreement);
        applicationRepository.save(application);


    }

    @Test
    @DisplayName("ifFindAllUserPoliciesClientId_success_thenReturnAllPolicies")
    void findAllUserPoliciesClientId_thenReturnAllPolicies() throws Exception {
        //ARRANGE
        List<ResponseUserPolicyDto> responseUserPolicyDto = userPolicyMapper.applicationsToUserPolicyDto(List.of(application));

        //ACT
        MvcResult result = mockMvc.perform(get("/auth/insurance")
                        .param("clientId", "8fae9356-91bb-11ed-a1eb-0242ac120002"))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseUserPolicyDto), result.getResponse().getContentAsString());
    }



    @Test
    @DisplayName("ifGetInsuranceTypes_success_thenReturnResponseApplicationInsuranceType")
    void findAllInsuranceTypes_success_returnAllInsuranceTypes() throws Exception {

        //ARRANGE
        List<Application> applications = applicationRepository.findAll();
        List<ResponseApplicationInsuranceTypeDto> responseApplicationInsuranceTypeDto = applicationMapper.toResponseTypes(applications);

        //ACT

        MvcResult result = mockMvc.perform(get("/auth/insurance/types"))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseApplicationInsuranceTypeDto), result.getResponse().getContentAsString());
    }



    @Test
    @DisplayName("ifFindRejectionLetter_success_thenReturnResponseRejectionLetter")
    void findRejectionLetter_success_returnResponseRejectionLetter() throws Exception {
        //ARRANGE
        Application application = applicationRepository.findApplicationByClientIdAndApplicationId(CLIENT_ID, APPLICATION_ID).get();
        ResponseRejectionLetterDto responseRejectionLetterDto = applicationMapper.toResponseLetter(application);


        //ACT
        MvcResult result = mockMvc.perform(
                        get("/auth/insurance/" + APPLICATION_ID + "/report")
                                .param("clientId", CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseRejectionLetterDto), result.getResponse().getContentAsString());
    }


    @Test
    @DisplayName("ifGetPolicyInformation_success_thenReturnPolicyInfoDto")
    void findPolicyInformation_success_returnPolicyInfo() throws Exception {
        //ARRANGE
        PolicyInfoDto policyInfoDto = policyMapper.toPolicyInfoDto(application, agreement, osago, insurant, sumAssignment);


        //ACT
        MvcResult result = mockMvc.perform(get("/auth/insurance/" + APPLICATION_ID)
                        .param("clientId", CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(policyInfoDto), result.getResponse().getContentAsString());
    }


    @Test
    @DisplayName("ifGetPaymentDetails_success_thenReturnResponsePaymentDetail")
    void getPaymentDetails_success_thenReturnResponsePaymentDetailsDto() throws Exception {
        //ARRANGE
        ResponsePaymentDetailsDto responsePaymentDetailsDto = applicationMapper.toResponsePaymentDetails(application);

        //ACT
        MvcResult result = mockMvc.perform(get("/auth/insurance/" + APPLICATION_ID + "/payment-details")
                        .param("clientId", CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responsePaymentDetailsDto), result.getResponse().getContentAsString());
    }


    @Test
    @DisplayName("ifCancelPolicyApplication_success_thenApplicationEqualsNull")
    void cancelPolicyApplication_success_thenApplicationNull() throws Exception {

        mockMvc.perform(delete("/auth/insurance/"+APPLICATION_ID+"/revocation")
                .param("clientId", CLIENT_ID))
                .andExpect(status().isNoContent());

        Assertions.assertThat(
                        applicationRepository.findById(APPLICATION_ID))
                .isEmpty();
    }


    private String asJsonString(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).usingDefaultComparator().isEqualTo(expectedBody);
    }
}



