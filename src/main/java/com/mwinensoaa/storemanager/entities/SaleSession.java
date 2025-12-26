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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SaleSession {
    @Id private String loginTime;
    @Column private String cashierId;
    @Column private String cashierName;
    @Column private String logoutTime;
}
