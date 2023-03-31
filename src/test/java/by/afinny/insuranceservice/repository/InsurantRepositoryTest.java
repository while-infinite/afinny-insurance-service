package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.Insurant;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"/schema-h2.sql"}
)
@ActiveProfiles("test")
class InsurantRepositoryTest {

    @Autowired
    private InsurantRepository insurantRepository;

    @Autowired
    private PersonRepository personRepository;

    private Insurant insurant;
    private Person person;

    @BeforeEach
    void setUp() {
        person = Person.builder()
                .firstName("name")
                .middleName("middle")
                .lastName("last")
                .build();

        insurant = Insurant.builder()
                .documentNumber("1234567890")
                .documentType(DocumentType.INN)
                .email("123@mail.ru")
                .phoneNumber("89119119911")
                .clientId("cc6588da-ffaf-4c00-a3bd-2e0c6d83655d")
                .build();
    }

    @AfterEach
    void cleanUp() {
        insurantRepository.deleteAllInBatch();
        personRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("If insurant with this clientId exists then return the insurant")
    void findInsurantByClientId_thenReturnInsurant() {
        //ARRANGE
        UUID personid = personRepository.save(person).getId();
        insurant.setId(personid);
        insurantRepository.save(insurant);
        //ACT
        Insurant foundInsurant = insurantRepository.findInsurantByClientId(insurant.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Insurant with id " + insurant.getClientId() + " wasn't found"));
        //VERIFY
        verifyInsurant(insurant, foundInsurant);
    }

    @Test
    @DisplayName("If insurant with this clientId doesn't exist then return empty")
    void findInsurantByClientId_ifInsurantNotExists_thenReturnEmpty() {
        //ACT
        Optional<Insurant> insur = insurantRepository.findInsurantByClientId(insurant.getClientId());
        //VERIFY
        assertThat(insur.isEmpty()).isTrue();
    }

    private void verifyInsurant(Insurant expected, Insurant actual) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId())
                    .isEqualTo(expected.getId());
            softAssertions.assertThat(actual.getDocumentNumber())
                    .isEqualTo(expected.getDocumentNumber());
            softAssertions.assertThat(actual.getDocumentType())
                    .isEqualTo(expected.getDocumentType());
            softAssertions.assertThat(actual.getEmail())
                    .isEqualTo(expected.getEmail());
            softAssertions.assertThat(actual.getPhoneNumber())
                    .isEqualTo(expected.getPhoneNumber());
            softAssertions.assertThat(actual.getClientId())
                    .isEqualTo(expected.getClientId());
        });
    }

}