package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.PaymentFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class PaymentRepo implements PaymentFacade {

    private static PaymentRepo instance;
    public static PaymentRepo getInstance() {
        if (instance == null) {
            instance = new PaymentRepo();
        }
        return instance;
    }


    @Override
    public void doPayment(Transaction transaction) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(transaction);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
    }



    @Override
    public List<Transaction> getAllPayments(){
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Transaction> transactions = session.createQuery(CrudSql.RETRIEVE_ALL_PAYMENT, Transaction.class).list();
            session.getTransaction().commit();
            return transactions;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> getPaymentsByCashier(String cashierId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Transaction> transactions = session.createQuery(CrudSql.RETRIEVE_PAYMENT_BY_CASHIER, Transaction.class)
                    .setParameter(1, cashierId).getResultList();
            session.getTransaction().commit();
            return transactions;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> getPaymentsByDate(String paymentDate) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Transaction> transactions = session.createQuery(CrudSql.RETRIEVE_PAYMENT_BY_DATE, Transaction.class)
                    .setParameter(1, paymentDate).getResultList();
            session.getTransaction().commit();
            return transactions;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return null;
    }

    @Override
    public void deletePayment(String paymentId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.createQuery(CrudSql.DELETE_PAYMENT, Transaction.class).setParameter(1, paymentId).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
    }
}
