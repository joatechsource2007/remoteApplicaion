package com.remote.restservice.delivery.charge_new;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ChargeDelivery {
    private Long id;
    private LocalTime deliveryTime;
    private String status;
    private String address;
    private String customerName;
    private Integer remainPercent;
    private Integer weightKg;
    private String dealInfo;
    private String memo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 👈 이 부분 추가
    private LocalDateTime createdAt;
}
