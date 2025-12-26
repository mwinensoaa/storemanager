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
public class Supplier {
    @Id @GeneratedValue
    @Column(name = "supplier_id") private int supplierId;
    @Column(name = "supplier_name") private String supplierName;
    @Column(name = "supplier_tel") private String supplierTelephone;


    public Supplier(String supplierName, String supplierTelephone){
        setSupplierName(supplierName);
        setSupplierTelephone(supplierTelephone);
    }



}
