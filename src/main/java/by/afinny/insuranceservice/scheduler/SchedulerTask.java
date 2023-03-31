package by.afinny.insuranceservice.scheduler;

import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static by.afinny.insuranceservice.entity.constant.InsuranceStatus.CANCELLED;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerTask {
    private final ApplicationRepository applicationRepository;

    @Scheduled(cron = "${interval-in-cron}")
    @Transactional
    public void checkInsuranceStatusAfterThreeDays() {
        List<Application> applicationList = applicationRepository.findAllApplicationByInsuranceStatus(InsuranceStatus.PENDING);
        applicationList.stream()
                .filter(application -> application.getRegistrationDate().isBefore(LocalDate.now().minusDays(3)))
                .peek(application -> application.setInsuranceStatus(CANCELLED))
                .collect(Collectors.toList());
        applicationRepository.saveAll(applicationList);
    }
}