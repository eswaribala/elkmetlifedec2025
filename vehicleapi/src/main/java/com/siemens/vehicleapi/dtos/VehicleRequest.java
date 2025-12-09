package com.siemens.vehicleapi.dtos;

import com.siemens.vehicleapi.models.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleRequest {

    @NotEmpty(message = "Registration No must not be empty")
    @NotBlank(message = "Registration No must not be blank")
    @Pattern(regexp = "^[A-Z0-9-]{1,25}$", message = "Registration No must be alphanumeric and can include hyphens, max length 25")
    private String registrationNo;
    @NotEmpty(message = "Maker must not be empty")
    @NotBlank(message = "Maker must not be blank")
    @Pattern(regexp = "^[A-Za-z ]{1,25}$", message = "Maker must contain only letters and spaces, max length 25")
    private String maker;

    private LocalDate dateOfRegistration;
    @NotEmpty(message = "Chassis No must not be empty")
    @NotBlank(message = "Chassis No must not be blank")
    @Pattern(regexp = "^[A-Z0-9]{1,25}$", message = "Chassis No must be alphanumeric, max length 25")
    private String chassisNo;
    @NotEmpty(message = "Engine No must not be empty")
    @NotBlank(message = "Engine No must not be blank")
    @Pattern(regexp = "^[A-Z0-9]{1,25}$", message = "Engine No must be alphanumeric, max length 25")

    private String engineNo;
    @NotEmpty(message = "Chassis No must not be empty")
    @NotBlank(message = "Chassis No must not be blank")
    @Pattern(regexp = "^[A-Za-z ]{1,25}$", message = "Color must contain only letters and spaces, max length 25")

    private String color;
    //@NotEmpty(message = "Fuel Type must not be empty")
    //@NotBlank(message = "Fuel Type must not be blank")
    private FuelType fuelType;
}
