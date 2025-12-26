package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.TransactionFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class TransactionRepo implements TransactionFacade {

    private static TransactionRepo instance;
    public static TransactionRepo getInstance() {
        if (instance == null) {
            instance = new TransactionRepo();
        }
        return instance;
    }

    @Override
    public void createTransaction(Transaction saleSession) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(saleSession);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
    }


    @Override
    public List<Transaction> getAllTransactions() {

        return List.of();
    }

    @Override
    public List<Transaction> getTransactionByMonth(String month) {

        return List.of();
    }

    @Override
    public List<Transaction> getTransactionByYear(String year) {

        return List.of();
    }


    @Override
    public Transaction getTransaction(String loginTime) {
        Transaction transaction = null;
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            transaction = session.createQuery(CrudSql.RETRIEVE_SALE_SESSION_BY_LOGIN_TIME, Transaction.class)
                    .setParameter(1, loginTime).getResultList().get(0);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return transaction;
    }

    @Override
    public List<Transaction> getCashierSessions(String cashierName) {
        List<Transaction> transactions = null;
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            transactions = session.createQuery(CrudSql.RETRIEVE_SALE_SESSION_BY_CASHIER_NAME, Transaction.class)
                    .setParameter(1, cashierName).getResultList();
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return transactions;
    }


}
