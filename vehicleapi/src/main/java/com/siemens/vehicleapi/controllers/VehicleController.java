package com.siemens.vehicleapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.siemens.vehicleapi.dtos.GenericResponse;
import com.siemens.vehicleapi.dtos.VehicleRequest;
import com.siemens.vehicleapi.models.Vehicle;
import com.siemens.vehicleapi.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/v1.0")
    public ResponseEntity<GenericResponse> addVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {

        Vehicle vehicle = Vehicle.builder()
                .maker(vehicleRequest.getMaker())
                .registrationNo(vehicleRequest.getRegistrationNo())
                .dateOfRegistration(vehicleRequest.getDateOfRegistration())
                .chassisNo(vehicleRequest.getChassisNo())
                .engineNo(vehicleRequest.getEngineNo())
                .color(vehicleRequest.getColor())
                .fuelType(vehicleRequest.getFuelType())
                .build();
        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);
        if(savedVehicle == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Vehicle could not be added"));
        else
           return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(savedVehicle));
    }

    @GetMapping("/v1.0")
    public ResponseEntity<GenericResponse> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(vehicles));
    }

    @GetMapping("/v1.0/{registrationNo}")
    public ResponseEntity<GenericResponse> getVehicleByRegistrationNo(@PathVariable("registrationNo") String registrationNo) {
        Vehicle vehicle = vehicleService.getVehicleById(registrationNo);
        if(vehicle == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Vehicle not found"));
        else
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(vehicle));

    }

    @PatchMapping("/v1.0/{registrationNo}")
    public ResponseEntity<GenericResponse> updateVehicle(@PathVariable("registrationNo") String registrationNo, @RequestParam("color") String color){

        Vehicle updatedVehicle = vehicleService.updateVehicle(registrationNo, color);
        if(updatedVehicle == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Vehicle could not be updated"));
        else
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse(updatedVehicle));
    }
    @DeleteMapping("/v1.0/{registrationNo}")
    public ResponseEntity<GenericResponse> deleteVehicle(@PathVariable("registrationNo") String registrationNo) {
        if (vehicleService.deleteVehicle(registrationNo))
           return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse("Vehicle deleted successfully"));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Vehicle could not be deleted"));
    }

    @GetMapping("/v1.0/publish/{registrationNo}")

    public CompletableFuture<ResponseEntity<String>> publishUserData(@PathVariable("registrationNo") String registrationNo) throws JsonProcessingException {

        Vehicle vehicle= vehicleService.getVehicleById(registrationNo);
        if(vehicle!=null){
            return vehicleService.publishVehicleInfo(vehicle)
                    .thenApply(result->ResponseEntity.status(HttpStatus.OK)
                            .body(result.getRecordMetadata().topic()+","+result.getRecordMetadata().partition()+","+result.getRecordMetadata().offset()))
                    .exceptionally(ex-> {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
                    });

        }
        return null;


    }


}
