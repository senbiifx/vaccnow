package com.assessment.vaccination.domain;

import com.assessment.vaccination.domain.entity.TimeSlot;
import com.assessment.vaccination.domain.entity.VaccinationSchedule;
import com.assessment.vaccination.domain.repository.TimeSlotRepository;
import com.assessment.vaccination.domain.repository.VaccinationScheduleRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VaccinationServiceTest {

    @InjectMocks
    private VaccinationService vaccinationService;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private VaccinationScheduleRepository vaccinationScheduleRepository;

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
}