package com.assessment.vaccination;

import com.assessment.common.DateConstants;
import com.assessment.common.Response;
import com.assessment.common.ResponseStatus;
import com.assessment.vaccination.domain.*;
import com.assessment.vaccination.domain.entity.*;
import com.assessment.vaccination.dto.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vaccination")
@AllArgsConstructor
public class VaccinationController {

    private ModelMapper modelMapper;
    private final VaccinationService vaccinationService;

    @GetMapping("/branches")
    public ResponseEntity<Response<List<BranchDto>>> getBranches() {
        List<Branch> branches = vaccinationService.getBranches();
        List<BranchDto> branchDtos = modelMapper.map(branches, new TypeToken<List<BranchDto>>() {}.getType());
        return ResponseEntity.ok(
                    Response.<List<BranchDto>>builder()
                            .status(ResponseStatus.SUCCESS)
                            .data(branchDtos)
                            .build());
    }

    @GetMapping("/branches/{branchCode}/vaccines")
    public ResponseEntity<Response<List<VaccineDto>>> getVaccinesByBranchCode(@Pattern(regexp = "\\w{4}")
                                                                              @PathVariable String branchCode) {
        List<Vaccine> vaccines = vaccinationService.getVaccinesByBranchCode(branchCode);
        List<VaccineDto> vaccineDtos = modelMapper.map(vaccines, new TypeToken<List<VaccineDto>>() {}.getType());
        return ResponseEntity.ok(
                Response.<List<VaccineDto>>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(vaccineDtos)
                        .build());
    }

    @GetMapping("/branches/{branchCode}/timeslots/{date}")
    public ResponseEntity<Response<List<TimeSlotDto>>> getAvailableTimeSlots(
                                                @Pattern(regexp = "\\w{4}")
                                                @PathVariable String branchCode,
                                                @PathVariable
                                                @DateTimeFormat(pattern = DateConstants.DATE_PATTERN) LocalDate date) {
        List<TimeSlot> timeSlots = vaccinationService.getAvailableTimeSlots(branchCode, date);
        List<TimeSlotDto> timeSlotDtos = modelMapper.map(timeSlots, new TypeToken<List<TimeSlotDto>>() {}.getType());
        return ResponseEntity.ok(
                Response.<List<TimeSlotDto>>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(timeSlotDtos)
                        .build());
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<Response<List<PaymentMethodDto>>> getPaymentMethods() {
        List<PaymentMethod> paymentMethods = vaccinationService.getPaymentMethods();
        List<PaymentMethodDto> paymentMethodDtos = modelMapper.map(paymentMethods,
                                                                new TypeToken<List<PaymentMethodDto>>() {}.getType());
        return ResponseEntity.ok(
                Response.<List<PaymentMethodDto>>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(paymentMethodDtos)
                        .build());
    }


    @PostMapping("/schedules")
    public ResponseEntity<Response<VaccinationScheduleDto>> scheduleVaccination(
                                            @RequestBody @Valid VaccinationScheduleRequestDto scheduleRequest) {
        VaccinationSchedule vaccinationSchedule =
                        vaccinationService.scheduleVaccination(scheduleRequest);
        VaccinationScheduleDto vaccinationScheduleDto =
                        modelMapper.map(vaccinationSchedule, VaccinationScheduleDto.class);
        return ResponseEntity.ok(
                Response.<VaccinationScheduleDto>builder()
                        .data(vaccinationScheduleDto)
                        .build());
    }

    @GetMapping("/schedules")
    public ResponseEntity<Response<List<VaccinationScheduleDto>>> getSchedules(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DateConstants.DATE_PATTERN) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DateConstants.DATE_PATTERN) LocalDate toDate,
            @RequestParam(required = false)
            String branchCode,
            @RequestParam(required = false) Boolean applied,
            @RequestParam(required = false) Boolean confirmed) {
        List<VaccinationSchedule> vaccinationSchedules =
                vaccinationService.getSchedules(fromDate, toDate, branchCode, applied, confirmed);
        List<VaccinationScheduleDto> vaccinationScheduleDto =
                modelMapper.map(vaccinationSchedules, new TypeToken<List<VaccinationScheduleDto>>() {}.getType());
        return ResponseEntity.ok(
                Response.<List<VaccinationScheduleDto>>builder()
                        .data(vaccinationScheduleDto)
                        .build());
    }


    @GetMapping("/schedules/{scheduleCode}/confirmations")
    public ResponseEntity<Response<?>> confirmSchedule(@PathVariable String scheduleCode) {
        vaccinationService.confirmSchedule(scheduleCode);
        return ResponseEntity.ok(
                Response.builder()
                        .status(ResponseStatus.SUCCESS)
                        .build());
    }

    @PostMapping("/schedules/{scheduleCode}/applications")
    public ResponseEntity<Response<?>> markVaccinationAsApplied(@PathVariable String scheduleCode) {
        vaccinationService.markVaccinationAsApplied(scheduleCode);
        return ResponseEntity.ok(
                Response.builder()
                        .status(ResponseStatus.SUCCESS)
                        .build());
    }
}
