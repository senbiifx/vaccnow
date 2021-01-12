package com.assessment.vaccination.domain.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentMethodId;

    @Column
    private String description;
}
