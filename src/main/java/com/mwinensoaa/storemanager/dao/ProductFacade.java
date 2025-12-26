package com.mwinensoaa.storemanager.dao;

import com.mwinensoaa.storemanager.entities.Product;

import java.util.List;

public interface ProductFacade {

    void createProduct(Product product);
    Product getProduct(String productId);
    List<Product> getAllProducts();
    void deleteProduct(String productId);
    void minusProduct(String productName, int numberBought);
    boolean productCodeExists(String productId);
    boolean productNameExist(String productDescription);

}
