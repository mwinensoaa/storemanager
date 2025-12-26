package com.mwinensoaa.storemanager.dao;


import com.mwinensoaa.storemanager.entities.Dosage;

public interface DosageFacade {
    void createDosage(Dosage dosage);
    Dosage getDosage(String productId);
    void deleteDosage(String productId);
}
