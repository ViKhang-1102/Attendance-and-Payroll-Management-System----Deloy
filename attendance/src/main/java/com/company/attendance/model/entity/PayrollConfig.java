
package com.company.attendance.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payroll_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal overtimeMultiplier; // e.g. 1.5 for 1.5x overtime pay

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal insuranceRate; // e.g. 10.5 for 10.5% insurance

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate; // e.g. 20 for 20% tax

    @Column(nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
