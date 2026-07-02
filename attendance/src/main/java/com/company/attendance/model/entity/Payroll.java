
package com.company.attendance.model.entity;

import com.company.attendance.model.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payroll")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "month_num", nullable = false)
    private Integer monthNum;

    @Column(name = "year_num", nullable = false)
    private Integer yearNum;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal baseSalary;

    @Column(precision = 7, scale = 2)
    private BigDecimal totalHours;

    @Column(precision = 7, scale = 2)
    private BigDecimal overtimeHours;

    @Column(precision = 15, scale = 2)
    private BigDecimal overtimePay;

    @Column(precision = 15, scale = 2)
    private BigDecimal allowances;

    @Column(precision = 15, scale = 2)
    private BigDecimal deductions;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal netSalary;

    @Column(precision = 15, scale = 2)
    private BigDecimal insurance;

    @Column(precision = 15, scale = 2)
    private BigDecimal tax;

    private Integer workingDays;

    private Integer absentDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollStatus status;

    private Boolean isPaid;

    private LocalDateTime paidAt;

    @Column(length = 500)
    private String notes;
}
