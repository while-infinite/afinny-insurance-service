package by.afinny.insuranceservice.service.impl;

import by.afinny.insuranceservice.dto.ResponseFinalPriceDto;
import by.afinny.insuranceservice.service.TravelProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class TravelProgramServiceImpl implements TravelProgramService {

    @Override
    public ResponseFinalPriceDto getFinalPrice(Boolean isWithSportType,
                                               Integer insuredNumber,
                                               Integer basicPrice,
                                               LocalDate startDate,
                                               LocalDate lastDate,
                                               Boolean isWithPCR) {
        log.info("getFinalPrice() method invoke");
        int isWithSportTypeCoefficient = isWithSportType ? 2 : 1;
        int countOfDays = (int) ChronoUnit.DAYS.between(startDate, lastDate);
        int isWithPCRCoefficient = isWithPCR ? 1990 : 0;
        int finalPrice = basicPrice * insuredNumber * isWithSportTypeCoefficient * countOfDays + isWithPCRCoefficient;
        return new ResponseFinalPriceDto(finalPrice);
    }

}
