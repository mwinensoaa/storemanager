package com.mwinensoaa.storemanager.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.h2.engine.Setting;

@Getter
@Setter
@Entity
@Table(name = "setting")
@NoArgsConstructor
public class Settings {
    @Id
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "country")
    private String country;
    @Column(name = "region")
    private String region;
    @Column(name = "currency")
    private String currency;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "language")
    private int  locale;
    @Column(name = "theme")
    private int theme;



    public Settings(String companyName, String country, String currency, String telephone){
        setCompanyName(companyName);
        setCountry(country);
        setCurrency(currency);
        setTelephone(telephone);
    }


    public Settings(int locale, int theme){
        setLocale(locale);
        setTheme(theme);
    }


}
