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
public class Transaction {

    @Id private String paymentId;
    @Column private String productId;
    @Column private int quantity;
    @Column private Double amount;
    @Column private String Discount;
    @Column private String transDate;
    @Column private String cashierId;



}
