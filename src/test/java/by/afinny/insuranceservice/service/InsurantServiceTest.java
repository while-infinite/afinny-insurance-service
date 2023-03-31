package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.ResponseUserPolicyDto;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.Insurant;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.mapper.UserPolicyMapper;
import by.afinny.insuranceservice.repository.InsurantRepository;
import by.afinny.insuranceservice.repository.PersonRepository;
import by.afinny.insuranceservice.service.impl.InsurantServiceImpl;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
public class InsurantServiceTest {

    @Mock
    private InsurantRepository insurantRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private UserPolicyMapper userPolicyMapper;
    @InjectMocks
    private InsurantServiceImpl insurantService;


    private static Insurant insurant;
    private static Person person;
    private static List<ResponseUserPolicyDto> userPoliciesDto;
    private static final String CLIENT_ID = "cc6588da-ffaf-4c00-a3bd-2e0c6d83655d";

    @BeforeAll
    static void setUp() {

        person = Person.builder()
                .id(UUID.fromString("c252ff7e-e6ab-4cf1-ad11-fb2b07abc1bb"))
                .firstName("name")
                .middleName("middle")
                .lastName("last")
                .applications(List.of(Application.builder()
                                .insuranceSum(BigDecimal.valueOf(300000.00))
                                .registrationDate(LocalDate.of(2022, 10, 15))
                                .periodOfInsurance(Period.THREE_MONTHS)
                                .paymentCycle(Period.ONE_MONTH)
                                .policySum(BigDecimal.valueOf(10000.00))
                                .insuranceStatus(InsuranceStatus.PENDING)
                                .region("region")
                                .district("district")
                                .city("city")
                                .street("street")
                                .houseNumber("11")
                                .flatNumber("33")
                                .startDate(LocalDate.now())
                                .insuranceType(InsuranceType.MEDICAL_INSURANCE)
                                .build(),
                        Application.builder()
                                .insuranceSum(BigDecimal.valueOf(250000.00))
                                .registrationDate(LocalDate.of(2022, 5, 10))
                                .periodOfInsurance(Period.TWELVE_MONTHS)
                                .paymentCycle(Period.SIX_MONTHS)
                                .policySum(BigDecimal.valueOf(9000.00))
                                .insuranceStatus(InsuranceStatus.APPROVED)
                                .region("region1")
                                .district("district1")
                                .city("city1")
                                .street("street1")
                                .houseNumber("111")
                                .flatNumber("333")
                                .startDate(LocalDate.now())
                                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                                .build()))
                .build();

        insurant = Insurant.builder()
                .id(person.getId())
                .documentNumber("1234567890")
                .documentType(DocumentType.INN)
                .email("123@mail.ru")
                .phoneNumber("89119119911")
                .clientId(CLIENT_ID)
                .build();

        userPoliciesDto = List.of(ResponseUserPolicyDto.builder()
                        .insuranceType(InsuranceType.MEDICAL_INSURANCE)
                        .registrationDate(LocalDate.of(2022, 10, 15))
                        .insuranceStatus(InsuranceStatus.PENDING)
                        .build(),
                ResponseUserPolicyDto.builder()
                        .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                        .registrationDate(LocalDate.of(2022, 5, 10))
                        .insuranceStatus(InsuranceStatus.APPROVED)
                        .build());
    }


    @Test
    @DisplayName("Return user policies when client id was found.")
    void getAllUserPolicies_shouldReturnListResponseUserPolicyDto() {
        //ARRANGE
        when(insurantRepository.findInsurantByClientId(CLIENT_ID))
                .thenReturn(Optional.of(insurant));
        when(personRepository.findById(insurant.getId()))
                .thenReturn(Optional.of(person));
        when(userPolicyMapper.applicationsToUserPolicyDto(person.getApplications()))
                .thenReturn(userPoliciesDto);
        //ACT
        List<ResponseUserPolicyDto> userPolicyDtoList = insurantService.getAllUserPolicies(CLIENT_ID);
        //VERIFY
        assertThat(userPolicyDtoList).isEqualTo(userPoliciesDto);
    }

    @Test
    @DisplayName("If insurant with clientId wasn't found then throw EntityNotFoundException")
    void getAllUserPolicies_ifClientIdNotFound_thenThrow() {
        //ARRANGE
        when(insurantRepository.findInsurantByClientId(CLIENT_ID)).thenReturn(Optional.empty());
        //ACT
        ThrowableAssert.ThrowingCallable createOrderMethodInvocation = () -> insurantService.getAllUserPolicies(CLIENT_ID);
        //VERIFY
        assertThatThrownBy(createOrderMethodInvocation).isInstanceOf(EntityNotFoundException.class);
    }


}
