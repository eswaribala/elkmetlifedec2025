package com.siemens.vehicleapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.siemens.vehicleapi.exceptions.VehicleNotFoundException;
import com.siemens.vehicleapi.models.Vehicle;
import com.siemens.vehicleapi.repositories.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @Value("${topicName}")
    private String topicName;

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        if((vehicle!=null) && (vehicle.getRegistrationNo()!=null)){
            return vehicleRepo.save(vehicle);
        }else {
            return null;
        }
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAll();
    }

    @Override
    public Vehicle getVehicleById(String vehicleId) {
        return vehicleRepo.findById(vehicleId).orElseThrow(()->
                new VehicleNotFoundException(vehicleId+"Not Found"));
    }

    @Override
    public Vehicle updateVehicle(String registrationNo, String color) {
        Vehicle vehicle = getVehicleById(registrationNo);
        if(vehicle!=null){
            vehicle.setColor(color);
            return vehicleRepo.save(vehicle);
        }else  {
            return null;
        }
    }

    @Override
    public boolean deleteVehicle(String vehicleId) {
        boolean result = false;
        Vehicle vehicle = getVehicleById(vehicleId);
        if(vehicle!=null) {
            vehicleRepo.delete(vehicle);
            result = true;
        }
        return result;
    }

    @Override
    public CompletableFuture<SendResult<String, Object>> publishVehicleInfo(Vehicle vehicle) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper .registerModule(new JavaTimeModule());
        // Optional: write dates as ISO-8601 (`"2025-11-27"`) instead of timestamps
        objectMapper .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String json = objectMapper.writeValueAsString(vehicle);
            // publish json...
            return kafkaTemplate.send(topicName,json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing vehicle", e);
        }



    }

}
