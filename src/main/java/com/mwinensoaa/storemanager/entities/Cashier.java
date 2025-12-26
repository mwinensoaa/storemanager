package com.mwinensoaa.storemanager.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "cashier")
@AllArgsConstructor
@NoArgsConstructor
public class Cashier {
    @Column @Id private String cashierId;
    @Column private String cashierName;
    @Column private String cashierDob;
    @Column private String cashierEmploymentDate;
    @Column private String cashierPassword;
    @Column private String cashierTelephone;
    @Column private CashierType cashierType;



    public Cashier(String cashierId, String cashierName, String password, CashierType cashierType) {
        setCashierId(cashierId);
        setCashierType(cashierType);
        setCashierName(cashierName);
        setCashierPassword(password);
    }


}

