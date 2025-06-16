//package com.remote.restservice.delivery.charge_new;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class ChargeManagementService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public List<Map<String, Object>> getChargeList() {
//        String procedure = "{CALL dbo.wsp_TANK_UB_DELIVERY_USERNO(?, ?, ?, ?, ?, ?)}";
//        return jdbcTemplate.queryForList(
//                procedure,
//                "0024",
//                "0000000004",
//                "20250611",
//                "001",
//                "7",
//                ""
//        );
//    }
//
//    public List<Map<String, Object>> getCompletedDeliveries(
//            String divType,
//            String cMngNo,
//            String userNo,
//            String date,
//            String carCode,
//            String swCode,
//            String findStr
//    ) {
//        String procedure = "{CALL dbo.wsp_TANK_UB_DELIVERY_USERNO(?, ?, ?, ?, ?, ?, ?)}";
//        return jdbcTemplate.queryForList(
//                procedure,
//                divType,
//                cMngNo,
//                userNo,
//                date,
//                carCode,
//                swCode,
//                findStr
//        );
//    }
//}
