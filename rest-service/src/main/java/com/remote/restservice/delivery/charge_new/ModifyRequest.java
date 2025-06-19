package com.remote.restservice.delivery.charge_new;

import lombok.Data;

@Data
public class ModifyRequest {
    private String pageID;
    private String tableName;
    private String operation;
    private String pkValue;
    private String newData;
    private String changedBy;
    private String userNo;
    private String appCert;
}
