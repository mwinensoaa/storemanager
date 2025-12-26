package com.mwinensoaa.storemanager.dao;

import com.mwinensoaa.storemanager.entities.Restock;

import java.util.List;

public interface RestockFacade {

    void createRestock(Restock restock);
    List<Restock> getAllRestock();
    List<Restock> getRestockByYear(String year);
    List<Restock> getRestockByMonth(String month);
}
