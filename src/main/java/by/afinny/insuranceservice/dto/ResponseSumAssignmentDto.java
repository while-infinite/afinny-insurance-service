package by.afinny.insuranceservice.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseSumAssignmentDto {
    private String id;
    private String name;
    private String minSum;
    private String maxSum;
    private String defaultSum;
}
