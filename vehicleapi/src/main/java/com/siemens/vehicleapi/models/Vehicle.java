package com.siemens.vehicleapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.EnableMBeanExport;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="vehicles")
public class Vehicle {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reg_no", nullable = false, length = 25)
    private String registrationNo;
    @Column(name="maker", nullable = false, length = 25)
    private String maker;
    @Column(name="reg_date")
    private LocalDate dateOfRegistration;
    @Column(name="chassis_no", nullable = false, length = 25)
    private String chassisNo;
    @Column(name="engine_no", nullable = false, length = 25)
    private String engineNo;
    @Column(name="color", nullable = false, length = 25)
    private String color;
    @Enumerated(EnumType.STRING)
    @Column(name="fuel_type", nullable = false, length = 10)
    private FuelType fuelType;
}
