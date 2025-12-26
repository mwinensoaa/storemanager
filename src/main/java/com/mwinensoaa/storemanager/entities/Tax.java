package com.mwinensoaa.storemanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tax")
@NoArgsConstructor
public class Tax {

    @Column @Id  private String taxTitle;
    @Column private double percentage;

    public Tax(String taxTitle, double percentage){
        setTaxTitle(taxTitle);
        setPercentage(percentage);
    }


}
