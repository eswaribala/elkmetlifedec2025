package com.siemens.vehicleapi.repositories;

import com.siemens.vehicleapi.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepo extends JpaRepository<Vehicle, String> {
}
