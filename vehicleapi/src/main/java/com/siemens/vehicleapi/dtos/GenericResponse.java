package com.siemens.vehicleapi.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponse<T> {
    private T data;
    private String message;

    public GenericResponse(T data) {
        this.data = data;
    }
    public  GenericResponse(String message) {
        this.message = message;
    }
}
