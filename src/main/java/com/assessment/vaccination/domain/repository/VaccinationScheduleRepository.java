package com.assessment.vaccination.domain.repository;

import com.assessment.vaccination.domain.entity.Branch;
import com.assessment.vaccination.domain.entity.TimeSlot;
import com.assessment.vaccination.domain.entity.VaccinationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VaccinationScheduleRepository extends JpaRepository<VaccinationSchedule, Integer> {

    @Query("Select v from  VaccinationSchedule v WHERE v.timeSlot = :timeSlot and v.scheduleDate = :scheduleDate and v.branch = :branch")
    Optional<VaccinationSchedule> findSchedule(@Param("timeSlot") TimeSlot timeSlot,
                                               @Param("scheduleDate") LocalDate scheduleDate,
                                               @Param("branch") Branch branch);


    @Query("Select v from  VaccinationSchedule v WHERE  v.scheduleDate = :scheduleDate and v.branch.branchCode = :branchCode")
    List<VaccinationSchedule> findByScheduleDateAndBranchCode(@Param("scheduleDate") LocalDate scheduleDate,
                                                              @Param("branchCode") String branchCode);

    @Query("Select v from  VaccinationSchedule v WHERE " +
            "((:fromDate is null) or (v.scheduleDate >= :fromDate)) AND " +
            "((:toDate is null) or (v.scheduleDate <= :toDate)) AND " +
            "((:branchCode is null) or (v.branch.branchCode = :branchCode)) AND " +
            "((:applied is null) or (v.applied = :applied)) AND " +
            "((:confirmed is null) or (v.confirmed = :confirmed))")
    List<VaccinationSchedule> findSchedule(@Param("fromDate") LocalDate fromDate,
                                           @Param("toDate") LocalDate toDate,
                                           @Param("branchCode") String branchCode,
                                           @Param("applied") Boolean applied,
                                           @Param("confirmed") Boolean confirmed);

    Optional<VaccinationSchedule> findByVaccinationScheduleCode(String scheduleCode);
}
