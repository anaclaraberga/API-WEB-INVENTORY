package com.example.web_inventory.dtos.request;

import java.math.BigInteger;

import com.example.web_inventory.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
    
    private String description;
    private BigInteger price;
    private String category;
    private String status;

    public Status getStatus() {

        return Status.valueOf(this.status);
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
