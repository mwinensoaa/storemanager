package com.mwinensoaa.storemanager.dao;


import com.mwinensoaa.storemanager.entities.Transaction;

import java.util.List;

public interface TransactionFacade {

    void createTransaction(Transaction saleSession);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionByMonth(String month);
    List<Transaction> getTransactionByYear(String year);
    Transaction getTransaction(String loginTime);
    List<Transaction> getCashierSessions(String cashierId);
}
