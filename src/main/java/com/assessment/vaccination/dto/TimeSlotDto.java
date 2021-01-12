package com.assessment.vaccination.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class TimeSlotDto {
    private Integer timeSlotId;
    private String fromTime;
    private String toTime;
}
