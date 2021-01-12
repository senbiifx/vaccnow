package com.assessment.vaccination.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Vaccine {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vaccineId;

    @Id
    @Column
    private String vaccineCode;

    @Column
    private String description;

}
