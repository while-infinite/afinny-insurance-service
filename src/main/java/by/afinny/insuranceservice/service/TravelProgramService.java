package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.ResponseFinalPriceDto;

import java.time.LocalDate;

public interface TravelProgramService {

    ResponseFinalPriceDto getFinalPrice (Boolean isWithSportType,
                                         Integer insuredNumber,
                                         Integer basicPrice,
                                         LocalDate startDate,
                                         LocalDate lastDate,
                                         Boolean isWithPCR);
}
