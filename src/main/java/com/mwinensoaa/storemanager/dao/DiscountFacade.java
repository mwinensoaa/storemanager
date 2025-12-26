package com.mwinensoaa.storemanager.dao;

import com.mwinensoaa.storemanager.entities.Discount;

import java.util.List;

public interface DiscountFacade {

    void createDiscount(Discount discount);
    void deleteDiscount(String discount);
    Discount getDiscount(String discount);
    List<Discount> getAllDiscounts();
    boolean isDiscountExists(String discount);

}
