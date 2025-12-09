package com.siemens.vehicleapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.siemens.vehicleapi.models.Vehicle;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VehicleService {
    Vehicle addVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    Vehicle getVehicleById(String vehicleId);
    Vehicle updateVehicle(String registrationNo, String color);
    boolean deleteVehicle(String vehicleId);
    CompletableFuture<SendResult<String, Object>> publishVehicleInfo(Vehicle vehicle) throws JsonProcessingException;
}
