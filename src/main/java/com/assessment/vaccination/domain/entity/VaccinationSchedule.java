package com.assessment.vaccination.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VaccinationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vaccinationScheduleId;

    @Column
    private String vaccinationScheduleCode;

    @Column
    private LocalDate scheduleDate;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;


    @ManyToOne
    @JoinColumn(name = "branch_code")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "vaccine_code")
    private Vaccine vaccine;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column
    private boolean confirmed;

    @Column
    private boolean applied;
}
