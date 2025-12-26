package com.mwinensoaa.storemanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "sales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private String productName;
    private int quantity;
    private double totalPrice;
    private String saleDate;
    private String cashierName;


    public Sale(String productName, int quantity, double totalPrice, String cashierName) {
        DateTimeFormatter receiptFormat =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");
        setSaleDate(LocalDateTime.now().format(receiptFormat));
        setProductName(productName);
        setQuantity(quantity);
        setTotalPrice(totalPrice);
        setCashierName(cashierName);
    }

}
