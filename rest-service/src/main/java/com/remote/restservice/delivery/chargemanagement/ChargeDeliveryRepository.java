package com.remote.restservice.delivery.chargemanagement;

import com.remote.restservice.delivery.charge_new.ChargeDelivery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChargeDeliveryRepository {

    private final JdbcTemplate jdbc;

    private final RowMapper<ChargeDelivery> mapper = (rs, rowNum) -> {
        ChargeDelivery item = new ChargeDelivery();
        item.setId(rs.getLong("Id"));
        item.setDeliveryTime(rs.getTime("DeliveryTime").toLocalTime());
        item.setStatus(rs.getString("Status"));
        item.setAddress(rs.getString("Address"));
        item.setCustomerName(rs.getString("CustomerName"));
        item.setRemainPercent(rs.getInt("RemainPercent"));
        item.setWeightKg(rs.getInt("WeightKg"));
        item.setDealInfo(rs.getString("DealInfo"));
        item.setMemo(rs.getString("Memo"));
        item.setCreatedAt(rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime() : null);
        return item;
    };

    public List<ChargeDelivery> findAll() {
        return jdbc.query("SELECT * FROM test_DeliveryClient", mapper);
    }

    public Optional<ChargeDelivery> findById(Long id) {
        return jdbc.query("SELECT * FROM test_DeliveryClient WHERE Id = ?", mapper, id)
                   .stream().findFirst();
    }

    public int insert(ChargeDelivery item) {
        return jdbc.update("""
            INSERT INTO test_DeliveryClient 
            (DeliveryTime, Status, Address, CustomerName, RemainPercent, WeightKg, DealInfo, Memo, CreatedAt)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """,
            item.getDeliveryTime(),
            item.getStatus(),
            item.getAddress(),
            item.getCustomerName(),
            item.getRemainPercent(),
            item.getWeightKg(),
            item.getDealInfo(),
            item.getMemo(),
            Timestamp.valueOf(item.getCreatedAt()));
    }

    public int update(ChargeDelivery item) {
      try{
          return jdbc.update("""
            UPDATE test_DeliveryClient 
            SET DeliveryTime = ?, Status = ?, Address = ?, CustomerName = ?, RemainPercent = ?, WeightKg = ?, DealInfo = ?, Memo = ? 
            WHERE Id = ?
        """,
                  item.getDeliveryTime(),
                  item.getStatus(),
                  item.getAddress(),
                  item.getCustomerName(),
                  item.getRemainPercent(),
                  item.getWeightKg(),
                  item.getDealInfo(),
                  item.getMemo(),
                  item.getId());
      } catch (Exception e) {
          System.out.println(e.getMessage());
          System.out.println(e.getMessage());
          System.out.println(e.getMessage());
          System.out.println(e.getMessage());
          throw new RuntimeException(e);
      }
    }

    public int updateStatus(Long id, String status) {
        try {
            return jdbc.update("""
            UPDATE test_DeliveryClient 
            SET Status = ? 
            WHERE Id = ?
        """, status, id);
        } catch (Exception e) {
            System.out.println("상태 업데이트 중 오류 발생: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



    public int delete(Long id) {
        return jdbc.update("DELETE FROM test_DeliveryClient WHERE Id = ?", id);
    }
}
