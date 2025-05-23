package com.remote.restservice.delivery.vehicle;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Vehicle {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int id;
    private String carNumber;
    private String driverName;
    private String workTime;
    private String address;
    private String status;
    private int supplyCount;
    private int supplyAmount;
    private int totalQty;
}
