package com.mwinensoaa.storemanager.dao;

import com.mwinensoaa.storemanager.entities.Cashier;

import java.util.List;

public interface CashierFacade {
    void createCashier(Cashier cashier);
    List<Cashier> getCashiers();
    Cashier getCashier(String cashierId);
    void deleteCashier(String cashierId);
    boolean cashierExists(String cashierId);

}
