package com.mwinensoaa.storemanager.database;

public class CrudSql {


    public static final String RETRIEVE_SETTINGS  = "FROM Settings";
    public static final String RETRIEVE_ALL_CASHIER  = "FROM Cashier";
    public static final String RETRIEVE_TAXES  = "FROM Tax";
    public static final String RETRIEVE_ALL_DISCOUNTS = "FROM Discount";
    public static final String RETRIEVE_ALL_RESTOCK = "FROM Restock";
    public static final String RETRIEVE_ALL_RESTOCK_BY_YEARS = "SELECT r FROM Restock r WHERE r.restockDate LIKE ?1";
    public static final String RETRIEVE_ALL_RESTOCK_BY_MONTH = "SELECT r FROM Restock r WHERE r.restockDate LIKE ?1";
    public static final String RETRIEVE_LAST_ID  = "FROM ProductId";
    public static final String RETRIEVE_SALE_SESSION_BY_LOGIN_TIME = "SELECT s FROM SaleSession s WHERE s.loginTime = ?1";
    public static final String RETRIEVE_SALE_SESSION_BY_CASHIER_NAME = "SELECT s FROM SaleSession s WHERE s.cashierName = ?1";
    public static final String IS_CASHIER  = "SELECT c FROM Cashier c WHERE c.cashierId = ?1 AND c.cashierPassword = ?2";
    public static final String RETRIEVE_PRODUCT_BY_ID = "SELECT p FROM Product p WHERE p.productId = ?1";
    public static final String RETRIEVE_PRODUCT_BY_NAME = "SELECT p FROM Product p WHERE p.productName = ?1";
    public static final String RETRIEVE_ALL_PRODUCT = "SELECT DISTINCT p FROM Product p";
    public static final String RETRIEVE_PRODUCT_COUNT = "SELECT COUNT(p) FROM Product p";
    public static final String RETRIEVE_PRODUCT_STOCK_COUNT = "SELECT COUNT(p) FROM Product p WHERE p.quantityInStock <= ?1";
    public static final String RETRIEVE_ALL_PAYMENT = "SELECT p FROM Payment p WHERE p.paymentId = ?1";
    public static final String RETRIEVE_PAYMENT_BY_CASHIER = "SELECT * FROM Payment p WHERE cashierId = ?1";
    public static final String RETRIEVE_PAYMENT_BY_DATE = "SELECT * FROM Payment p WHERE paymentDate = ?1";
    public static final String DELETE_PAYMENT = "DELETE p FROM Payment p WHERE p.paymentId = ?1";
    public static final String RETRIEVE_CASHIERS = "SELECT NEW com.mwinensoaa.storemanager.entities.Cashier(c.cashierId, c.cashierName, c.cashierPassword, c.cashierType) FROM Cashier c";
    public static final String RETRIEVE_LANGUAGE = "SELECT NEW com.mwinensoaa.storemanager.entities.Settings(s.locale,s.theme) FROM Settings s";
    public static final String RETRIEVE_SALES = "FROM Sale";
    public static final String RETRIEVE_SALES_BY_DATE = "FROM Sale s WHERE s.saleDate = ?1";



}
