package com.assessment.vaccination.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    @Column
    private String customerName;

    @Column
    private String customerNationalNumber;

    @Column
    private String email;

    public Customer(String customerName, String customerNationalNumber, String email){
        this.customerName = customerName;
        this.customerNationalNumber = customerNationalNumber;
        this.email = email;
    }
}
