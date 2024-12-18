package com.example.web_inventory.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.web_inventory.dtos.request.OrderRequestDTO;
import com.example.web_inventory.dtos.response.OrderResponseDTO;
import com.example.web_inventory.entities.CustomerEntity;
import com.example.web_inventory.entities.OrderEntity;
import com.example.web_inventory.entities.OrderItemEntity;
import com.example.web_inventory.repositories.CustomerRepository;
import com.example.web_inventory.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderItemService itemService;

    @Autowired
    private CustomerRepository customerRepository;

    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        CustomerEntity customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException(
                        "Customer not found."));

        OrderEntity entity = new OrderEntity(dto);
        entity.setCustomerId(customer);

        OrderEntity created = repository.save(entity);
        BigDecimal total = BigDecimal.ZERO;

        for (var item : dto.getItems()) {
            item.setOrderId(created.getId());
            OrderItemEntity orderItem = itemService.createOrderItem(item);
            BigDecimal subtotal = orderItem
                    .getUnitPrice()
                    .multiply(new BigDecimal(orderItem.getQuantity()));

            total = total.add(subtotal);
        }

        created.setTotalValue(total);
        repository.save(created);

        return this.findOrderById(created.getId());
    }

    public OrderResponseDTO findOrderById(Long id) {
        OrderEntity order = this.repository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado! Id: " + id));

        OrderResponseDTO response = new OrderResponseDTO(order);
        response.setItems(itemService.findByOrderId(id));

        return response;
    }

    public List<OrderEntity> findByCustomerId(Long customerId) {
        CustomerEntity customerEntity = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Pedido não encontrado para o ID: " + customerId + OrderItemEntity.class.getName(),
                        customerId));

        return repository.findByCustomerId(customerEntity.getId());
    }

    public List<OrderEntity> getAllOrders() {
        return repository.findAll();
    }

    public ResponseEntity<OrderEntity> updateOrderById(OrderRequestDTO dto, Long id) {
        return repository.findById(id)
                .map(update -> {
                    update.setDateTimeAtCreation(LocalDateTime.now());
                    update.setStatus(dto.getStatus());
                    update.setTotalValue(dto.getTotalValue());

                    OrderEntity updated = repository.save(update);

                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteById(Long id) {
        return repository.findById(id)
                .map(delete -> {
                    repository.deleteById(id);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
