package com.assessment.vaccination.domain.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeSlotId;
    @Column
    private String fromTime;
    @Column
    private String toTime;
}
