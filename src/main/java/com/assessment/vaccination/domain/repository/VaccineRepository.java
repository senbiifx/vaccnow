package com.assessment.vaccination.domain.repository;

import com.assessment.vaccination.domain.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, String> {
}
