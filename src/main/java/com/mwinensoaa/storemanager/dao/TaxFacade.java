package com.mwinensoaa.storemanager.dao;


import com.mwinensoaa.storemanager.entities.Tax;

import java.util.List;

public interface TaxFacade {

    void createTax(Tax tax);
    List<Tax> getAllTaxes();
     Tax getTax(String taxTitle);
     void deleteTax(String taxTitle);
     boolean taxTitleExist(String taxTitle);

}
