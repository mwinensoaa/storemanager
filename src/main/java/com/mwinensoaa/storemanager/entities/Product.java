package com.mwinensoaa.storemanager.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id private String productId;
    @Column private String productName;
    @Column private int packSize;
    @Column private double unitPrice;
    @Column private int quantityInStock;
    @Column private double packPrice;
    @Column private String datePlacedInStock;

    public Product(String productName){
        setProductName(productName);
    }
}
