package com.assessment.vaccination;

import com.assessment.common.DateConstants;
import com.assessment.common.Response;
import com.assessment.common.ResponseStatus;
import com.assessment.vaccination.domain.*;
import com.assessment.vaccination.domain.entity.*;
import com.assessment.vaccination.domain.model.VaccinationScheduleReport;
import com.assessment.vaccination.dto.*;
import io.swagger.annotations.*;
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

@Api(tags = {"Vaccination APIs"})
@RestController
@RequestMapping("/api/vaccination")
@AllArgsConstructor
public class VaccinationController {

    private ModelMapper modelMapper;
    private final VaccinationService vaccinationService;


    @ApiOperation(value = "Get all branches.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Response with list of branches"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Something went wrong on server's side")
    })
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

    @ApiOperation(value = "Get all vaccines available in a branch.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Response with list of vaccines"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Something went wrong on server's side")
    })
    @GetMapping("/branches/{branchCode}/vaccines")
    public ResponseEntity<Response<List<VaccineDto>>> getVaccinesByBranchCode(
                                            @ApiParam(name = "branchCode", value = "Branch code from the Branch List API")
                                            @Pattern(regexp = "\\w{4}")
                                            @PathVariable String branchCode) {
        List<Vaccine> vaccines = vaccinationService.getVaccinesByBranchCode(branchCode);
        List<VaccineDto> vaccineDtos = modelMapper.map(vaccines, new TypeToken<List<VaccineDto>>() {}.getType());
        return ResponseEntity.ok(
                Response.<List<VaccineDto>>builder()
                        .status(ResponseStatus.SUCCESS)
                        .data(vaccineDtos)
                        .build());
    }

    @ApiOperation(value = "Get all timeslots available in a branch.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Response with list of available timeslots"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Something went wrong on server's side")
    })
    @GetMapping("/branches/{branchCode}/timeslots/{date}")
    public ResponseEntity<Response<List<TimeSlotDto>>> getAvailableTimeSlots(
                                                @ApiParam(name = "branchCode", value = "Branch code from the Branch List API")
                                                @Pattern(regexp = "\\w{4}")
                                                @PathVariable String branchCode,
                                                @ApiParam(name = "date", value = "Date in yyyy-MM-dd format")
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

    @ApiOperation(value = "Get all Payment Methods.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Response with list of payment methods."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Something went wrong on server's side")
    })
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


    @ApiOperation(value = "Schedule a vaccination appointment and sends a confirmation email.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Response with the schedule information."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Something went wrong on server's side")
    })
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

    @ApiOperation(value = "Generate a PDF report of schedules.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Response with base64 of pdf report."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Something went wrong on server's side")
    })
    @GetMapping("/schedules/report")
    public ResponseEntity<Response<VaccinationScheduleReport>> getSchedules(
            @ApiParam(name = "fromDate", value = "Date in yyyy-MM-dd format")
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DateConstants.DATE_PATTERN) LocalDate fromDate,

            @ApiParam(name = "fromDate", value = "Date in yyyy-MM-dd format")
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DateConstants.DATE_PATTERN) LocalDate toDate,

            @ApiParam(name = "branchCode", value = "branchCode from Branch List API")
            @RequestParam(required = false) String branchCode,

            @ApiParam(name = "applied")
            @RequestParam(required = false) Boolean applied,

            @ApiParam(name = "confirmed")
            @RequestParam(required = false) Boolean confirmed) {
        return ResponseEntity.ok(
                Response.<VaccinationScheduleReport>builder()
                        .data(vaccinationService.getScheduleReport(fromDate, toDate, branchCode, applied, confirmed))
                        .build());
    }

    @ApiOperation(value = "Confirm a schedule.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Something went wrong on server's side")
    })
    @GetMapping("/schedules/{scheduleCode}/confirmations")
    public ResponseEntity<Response<?>> confirmSchedule(
                                @ApiParam(name = "scheduleCode", value = "vaccinationScheduleCode generated during schedule appointment.")
                                @PathVariable String scheduleCode) {
        vaccinationService.confirmSchedule(scheduleCode);
        return ResponseEntity.ok(
                Response.builder()
                        .status(ResponseStatus.SUCCESS)
                        .build());
    }

    @ApiOperation(value = "Mark vaccination as applied.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Something went wrong on server's side")
    })
    @PostMapping("/schedules/{scheduleCode}/applications")
    public ResponseEntity<Response<?>> markVaccinationAsApplied(
                        @ApiParam(name = "scheduleCode", value = "vaccinationScheduleCode generated during schedule appointment.")
                        @PathVariable String scheduleCode) {
        vaccinationService.markVaccinationAsApplied(scheduleCode);
        return ResponseEntity.ok(
                Response.builder()
                        .status(ResponseStatus.SUCCESS)
                        .build());
    }
}
