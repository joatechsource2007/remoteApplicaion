package com.remote.restservice.delivery.chargemanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChargeDeliveryService {

    private final ChargeDeliveryRepository repository;

    public List<ChargeDelivery> getAll() {
        return repository.findAll();
    }

    public Optional<ChargeDelivery> getById(Long id) {
        return repository.findById(id);
    }

    public int create(ChargeDelivery item) {
        return repository.insert(item);
    }

    public int updateStatus(ChargeDelivery item) {
        return repository.updateStatus(item.getId(), item.getStatus());
    }

    public int delete(Long id) {
        return repository.delete(id);
    }
}
