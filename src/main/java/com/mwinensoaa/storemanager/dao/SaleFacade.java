package com.mwinensoaa.storemanager.dao;

import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.Sale;

import java.util.List;

public interface SaleFacade {
    void createSale(Sale cashier);
    List<Sale> getSales();
    List<Sale> getSales(String date);
    Sale getSale(String saleId);
    void deleteSale(int saleId);
    boolean saleExists(String saleId);
}
