package com.mwinensoaa.storemanager.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
public class Restock {

    @Column @Id @GeneratedValue private int restockId;
    @Column private String productId;
    @Column private String productName;
    @Column private String restockDate;
    @Column private int quantity;

}
