package com.example.web_inventory.entities;

import java.io.Serializable;

import com.example.web_inventory.enums.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "suppliers")
@Entity
@Data
public class SupplierEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 355)
    private String companyName;

    @Column(nullable = false, length = 14)
    private String cnpj;

    @Column(nullable = false)
    private Category category;

}