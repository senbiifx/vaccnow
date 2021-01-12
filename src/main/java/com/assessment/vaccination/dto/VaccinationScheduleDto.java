package com.assessment.vaccination.dto;

import com.assessment.vaccination.dto.BranchDto;
import com.assessment.vaccination.dto.CustomerDto;
import com.assessment.vaccination.dto.PaymentMethodDto;
import com.assessment.vaccination.dto.VaccineDto;
import lombok.Data;

@Data
public class VaccinationScheduleDto {
    private Integer vaccinationScheduleId;
    private String vaccinationScheduleCode;
    private BranchDto branch;
    private VaccineDto vaccine;
    private CustomerDto customer;
    private PaymentMethodDto paymentMethod;
    private boolean confirmed;
    private boolean applied;
}
