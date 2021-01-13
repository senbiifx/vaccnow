package com.assessment.vaccination.domain.model;

import com.assessment.vaccination.domain.entity.VaccinationSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationScheduleReportModel {
    private String fromDate;
    private String toDate;
    private List<VaccinationSchedule> schedules;
}
