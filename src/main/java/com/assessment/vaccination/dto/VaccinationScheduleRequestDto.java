package com.assessment.vaccination.dto;

import com.assessment.common.DateConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
public class VaccinationScheduleRequestDto {
    @NotNull
    private Integer timeSlotId;
    @NotNull
    private String vaccineCode;
    @NotNull
    private String customerName;
    @NotNull
    private String customerNationalNumber;
    @Email
    @NotNull
    private String email;
    @NotNull
    private Integer paymentMethodId;

    @JsonFormat(pattern = DateConstants.DATE_PATTERN, timezone = DateConstants.TIMEZONE)
    @NotNull
    private LocalDate scheduleDate;

    @NotNull
    private String branchCode;
}
