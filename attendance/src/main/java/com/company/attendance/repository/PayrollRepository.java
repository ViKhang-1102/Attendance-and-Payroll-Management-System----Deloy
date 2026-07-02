
package com.company.attendance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.company.attendance.model.entity.Payroll;
import com.company.attendance.model.entity.User;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, UUID> {

    List<Payroll> findByUser(User user);

    List<Payroll> findByUserId(UUID userId);

    Optional<Payroll> findByUserIdAndMonthNumAndYearNum(UUID userId, Integer monthNum, Integer yearNum);

    List<Payroll> findByMonthNumAndYearNum(Integer monthNum, Integer yearNum);

    @Query("SELECT p FROM Payroll p WHERE p.user.id = :userId AND p.yearNum = :yearNum")
    List<Payroll> findByUserIdAndYearNum(@Param("userId") UUID userId, @Param("yearNum") Integer yearNum);

    @Query("SELECT p FROM Payroll p WHERE p.yearNum = :yearNum")
    List<Payroll> findByYearNum(@Param("yearNum") Integer yearNum);
}
