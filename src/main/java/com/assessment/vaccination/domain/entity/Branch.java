package com.assessment.vaccination.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Branch {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer branchId;

    @Id
    @Column
    private String branchCode;

    @Column
    private String description;

    @OneToMany
    @JoinTable(name = "branch_vaccine",
            joinColumns = @JoinColumn(name = "branch_code"),
            inverseJoinColumns = @JoinColumn(name="vaccine_code"))
    private List<Vaccine> vaccines = new ArrayList<>();
}
