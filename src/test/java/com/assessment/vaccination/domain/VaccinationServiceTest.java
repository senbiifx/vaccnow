package com.assessment.vaccination.domain;

import com.assessment.common.PreconditionException;
import com.assessment.config.AppProperties;
import com.assessment.vaccination.domain.entity.*;
import com.assessment.vaccination.domain.model.VaccinationScheduleReport;
import com.assessment.vaccination.domain.repository.*;
import com.assessment.vaccination.dto.VaccinationScheduleRequestDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VaccinationServiceTest {

    @InjectMocks
    private VaccinationService vaccinationService;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private VaccinationScheduleRepository vaccinationScheduleRepository;

    @Mock
    private ReportGenerator reportGenerator;

    @Mock
    private AppProperties appProperties;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JavaMailSender mailSender;

    @Test
    public void getAvailableTimeSlots(){
        when(timeSlotRepository.findAll()).thenReturn(Arrays.asList(new TimeSlot(), new TimeSlot()));
        when(vaccinationScheduleRepository.findByScheduleDateAndBranchCode(any(), anyString())).thenReturn(Collections.emptyList());
        List<TimeSlot> timeSlots = vaccinationService.getAvailableTimeSlots("BURJ", LocalDate.now());

        assertNotNull(timeSlots);
        assertEquals(2, timeSlots.size());
    }

    @Test
    public void getAvailableTimeSlots_excludeOccupiedTimeSlots(){
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setTimeSlotId(1);
        TimeSlot timeSlot2 = new TimeSlot();
        timeSlot2.setTimeSlotId(2);
        when(timeSlotRepository.findAll()).thenReturn(Arrays.asList(timeSlot1, timeSlot2));

        VaccinationSchedule occupiedSchedule = new VaccinationSchedule();
        occupiedSchedule.setTimeSlot(timeSlot1);
        when(vaccinationScheduleRepository.findByScheduleDateAndBranchCode(any(), anyString())).thenReturn(Arrays.asList(occupiedSchedule));

        List<TimeSlot> timeSlots = vaccinationService.getAvailableTimeSlots("BURJ", LocalDate.now());

        assertNotNull(timeSlots);
        assertEquals(1, timeSlots.size());
        assertEquals(2, (int)timeSlots.get(0).getTimeSlotId());
    }

    @Test
    public void getScheduleReport(){
        when(vaccinationScheduleRepository.findSchedule(any(), any(), anyString(), any(), any()))
                .thenReturn(Collections.emptyList());

        when(appProperties.getScheduleReportTemplate()).thenReturn("/schedule");

        when(reportGenerator.createPdf(anyString(),any())).thenReturn(new byte[]{0});

        VaccinationScheduleReport report = vaccinationService.getScheduleReport(LocalDate.now(), LocalDate.now(), "BURH", false, false);

        assertNotNull(report);
        assertNotNull(report.getBase64());
    }

    @Test(expected = PreconditionException.class)
    public void scheduleVaccination_invalidBranch(){
       when(branchRepository.findByBranchCode(anyString())).thenReturn(Optional.empty());

        VaccinationScheduleRequestDto scheduleRequest = new VaccinationScheduleRequestDto();
        scheduleRequest.setBranchCode("B");
       vaccinationService.scheduleVaccination(scheduleRequest);
    }

    @Test(expected = PreconditionException.class)
    public void scheduleVaccination_invalidVaccine(){
        when(branchRepository.findByBranchCode(anyString())).thenReturn(Optional.of(new Branch()));

        when(vaccineRepository.findById(anyString())).thenReturn(Optional.empty());

        VaccinationScheduleRequestDto scheduleRequest = new VaccinationScheduleRequestDto();
        scheduleRequest.setBranchCode("B");
        scheduleRequest.setVaccineCode("A");
        vaccinationService.scheduleVaccination(scheduleRequest);
    }

    @Test(expected = PreconditionException.class)
    public void scheduleVaccination_invalidPaymentMethod(){
        when(branchRepository.findByBranchCode(anyString())).thenReturn(Optional.of(new Branch()));

        when(vaccineRepository.findById(anyString())).thenReturn(Optional.of(new Vaccine()));

        when(paymentMethodRepository.findById(any())).thenReturn(Optional.empty());

        VaccinationScheduleRequestDto scheduleRequest = new VaccinationScheduleRequestDto();
        scheduleRequest.setBranchCode("B");
        scheduleRequest.setVaccineCode("A");
        scheduleRequest.setPaymentMethodId(2);
        vaccinationService.scheduleVaccination(scheduleRequest);
    }

    @Test(expected = PreconditionException.class)
    public void scheduleVaccination_invalidTimeslot(){
        when(branchRepository.findByBranchCode(anyString())).thenReturn(Optional.of(new Branch()));

        when(vaccineRepository.findById(anyString())).thenReturn(Optional.of(new Vaccine()));

        when(paymentMethodRepository.findById(any())).thenReturn(Optional.of(new PaymentMethod()));

        when(timeSlotRepository.findById(any())).thenReturn(Optional.empty());

        VaccinationScheduleRequestDto scheduleRequest = new VaccinationScheduleRequestDto();
        scheduleRequest.setBranchCode("B");
        scheduleRequest.setVaccineCode("A");
        scheduleRequest.setPaymentMethodId(2);
        scheduleRequest.setTimeSlotId(2);
        vaccinationService.scheduleVaccination(scheduleRequest);
    }


    @Test(expected = PreconditionException.class)
    public void scheduleVaccination_timeslotNotAvailable(){
        when(branchRepository.findByBranchCode(anyString())).thenReturn(Optional.of(new Branch()));

        when(vaccineRepository.findById(anyString())).thenReturn(Optional.of(new Vaccine()));

        when(paymentMethodRepository.findById(any())).thenReturn(Optional.of(new PaymentMethod()));

        when(timeSlotRepository.findById(any())).thenReturn(Optional.of(new TimeSlot()));

        when(vaccinationScheduleRepository.findSchedule(any(), any(), any()))
                .thenReturn(Optional.of(new VaccinationSchedule()));

        VaccinationScheduleRequestDto scheduleRequest = new VaccinationScheduleRequestDto();
        scheduleRequest.setBranchCode("B");
        scheduleRequest.setVaccineCode("A");
        scheduleRequest.setPaymentMethodId(2);
        scheduleRequest.setTimeSlotId(2);
        scheduleRequest.setScheduleDate(LocalDate.now());
        vaccinationService.scheduleVaccination(scheduleRequest);
    }

    @Test
    public void scheduleVaccination_customerInformationIsSaved(){
        when(branchRepository.findByBranchCode(anyString())).thenReturn(Optional.of(new Branch()));

        when(vaccineRepository.findById(anyString())).thenReturn(Optional.of(new Vaccine()));

        when(paymentMethodRepository.findById(any())).thenReturn(Optional.of(new PaymentMethod()));

        when(timeSlotRepository.findById(any())).thenReturn(Optional.of(new TimeSlot()));

        when(vaccinationScheduleRepository.findSchedule(any(), any(), any()))
                .thenReturn(Optional.empty());

        VaccinationSchedule sched = new VaccinationSchedule();
        sched.setBranch(new Branch());
        sched.setVaccine(new Vaccine());
        sched.setPaymentMethod(new PaymentMethod());
        sched.setTimeSlot(new TimeSlot());
        sched.setScheduleDate(LocalDate.now());
        sched.setCustomer(new Customer("", "", ""));
        when(vaccinationScheduleRepository.save(any())).thenReturn(sched);


        when(appProperties.getEmailSubject()).thenReturn("");
        when(appProperties.getEmailContent()).thenReturn("");

        VaccinationScheduleRequestDto scheduleRequest = new VaccinationScheduleRequestDto();
        scheduleRequest.setBranchCode("B");
        scheduleRequest.setVaccineCode("A");
        scheduleRequest.setPaymentMethodId(2);
        scheduleRequest.setTimeSlotId(2);
        scheduleRequest.setScheduleDate(LocalDate.now());
        scheduleRequest.setCustomerName("sdasd");
        scheduleRequest.setCustomerNationalNumber("sdasd");
        scheduleRequest.setEmail("sdasd");
        vaccinationService.scheduleVaccination(scheduleRequest);

        verify(customerRepository, times(1)).save(any());
    }

    @Test
    public void scheduleVaccination_emailSent(){
        when(branchRepository.findByBranchCode(anyString())).thenReturn(Optional.of(new Branch()));

        when(vaccineRepository.findById(anyString())).thenReturn(Optional.of(new Vaccine()));

        when(paymentMethodRepository.findById(any())).thenReturn(Optional.of(new PaymentMethod()));

        when(timeSlotRepository.findById(any())).thenReturn(Optional.of(new TimeSlot()));

        when(vaccinationScheduleRepository.findSchedule(any(), any(), any()))
                .thenReturn(Optional.empty());

        VaccinationSchedule sched = new VaccinationSchedule();
        sched.setBranch(new Branch());
        sched.setVaccine(new Vaccine());
        sched.setPaymentMethod(new PaymentMethod());
        sched.setTimeSlot(new TimeSlot());
        sched.setScheduleDate(LocalDate.now());
        sched.setCustomer(new Customer("", "", ""));
        when(vaccinationScheduleRepository.save(any())).thenReturn(sched);


        when(appProperties.getEmailSubject()).thenReturn("");
        when(appProperties.getEmailContent()).thenReturn("");

        VaccinationScheduleRequestDto scheduleRequest = new VaccinationScheduleRequestDto();
        scheduleRequest.setBranchCode("B");
        scheduleRequest.setVaccineCode("A");
        scheduleRequest.setPaymentMethodId(2);
        scheduleRequest.setTimeSlotId(2);
        scheduleRequest.setScheduleDate(LocalDate.now());
        scheduleRequest.setCustomerName("sdasd");
        scheduleRequest.setCustomerNationalNumber("sdasd");
        scheduleRequest.setEmail("sdasd");
        vaccinationService.scheduleVaccination(scheduleRequest);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}