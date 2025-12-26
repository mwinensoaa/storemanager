package com.mwinensoaa.storemanager.dao;

import com.mwinensoaa.storemanager.entities.Transaction;

import java.util.List;

public interface PaymentFacade {
    void doPayment(Transaction transaction);

    List<Transaction> getAllPayments();

    List<Transaction> getPaymentsByCashier(String cashierId);
    List<Transaction> getPaymentsByDate(String date);
    void deletePayment(String paymentId);
}
