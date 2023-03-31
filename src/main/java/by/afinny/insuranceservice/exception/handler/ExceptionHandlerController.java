package by.afinny.insuranceservice.exception.handler;

import by.afinny.insuranceservice.exception.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;


@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    private final String INTERNAL_SERVER_ERROR = "Internal server error";

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> serverExceptionHandler(Exception e) {
        log.error(INTERNAL_SERVER_ERROR + e.getMessage());
        return createResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                createErrorDto(e, HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR));
    }


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> badRequestExceptionHandler(EntityNotFoundException e) {
        log.error("Bad request", e);
        return createResponseEntity(
                HttpStatus.BAD_REQUEST,
                createErrorDto(e, HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    private ResponseEntity<ErrorDto> createResponseEntity(HttpStatus status, ErrorDto errorDto) {
        return ResponseEntity.status(status)
                .header("Content-Type", "application/json")
                .body(errorDto);
    }

    private ErrorDto createErrorDto(Exception e, HttpStatus httpStatus, String errorMessage) {
        if(e.getStackTrace().length == 0){
            return ErrorDto.builder()
                    .errorCode(String.valueOf(httpStatus))
                    .errorMessage(errorMessage)
                    .build();
        }
        StackTraceElement element = e.getStackTrace()[2];
        String[] splitArray = element.getClassName().split("\\.");
        int arrayLength = splitArray.length;

        return ErrorDto.builder()
                .errorCode(String.valueOf(httpStatus))
                .errorMessage(errorMessage)
                .errorClass(splitArray[arrayLength - 1])
                .errorMethod(element.getMethodName())
                .build();
    }
}