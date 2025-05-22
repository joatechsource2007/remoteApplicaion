package com.remote.restservice.delivery.chargemanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charge-delivery")
@RequiredArgsConstructor
public class ChargeDeliveryController {

    private final ChargeDeliveryService service;

    @GetMapping
    public List<ChargeDelivery> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ChargeDelivery getById(@PathVariable Long id) {
        return service.getById(id).orElse(null);
    }

    @PostMapping
    public String create(@RequestBody ChargeDelivery item) {
        int result = service.create(item);
        return result > 0 ? "created" : "fail";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody ChargeDelivery item) {
        item.setId(id);
        int result = service.updateStatus(item);
        return result > 0 ? "updated" : "fail";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        int result = service.delete(id);
        return result > 0 ? "deleted" : "fail";
    }
}
