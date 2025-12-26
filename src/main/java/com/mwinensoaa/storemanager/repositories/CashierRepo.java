package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.CashierFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Cashier;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class CashierRepo implements CashierFacade {

    private static CashierRepo instance;
    public static CashierRepo getInstance() {
        if (instance == null) {
            instance = new CashierRepo();
        }
        return instance;
    }


    @Override
    public void createCashier(Cashier cashier) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(cashier);
            session.getTransaction().commit();
        } catch (HibernateException ignore) {

        }
    }

    @Override
    public List<Cashier> getCashiers() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Cashier> cashier = session.createQuery(CrudSql.RETRIEVE_CASHIERS, Cashier.class).list();
            session.getTransaction().commit();
            return cashier;
        } catch (HibernateException ignore) {

        }
        return List.of();
    }


    @Override
    public Cashier getCashier(String cashierId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
           Cashier cashier = session.get(Cashier.class, cashierId);
            session.getTransaction().commit();
            return cashier;
        } catch (HibernateException ignore) {

        }
        return null;
    }


    @Override
    public void deleteCashier(String cashierId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Cashier cashier = session.get(Cashier.class, cashierId);
            session.remove(cashier);
            session.getTransaction().commit();
        } catch (HibernateException ignore) {
        }
    }

    @Override
    public boolean cashierExists(String cashierId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Cashier cashier = session.get(Cashier.class, cashierId);
            session.getTransaction().commit();
            return cashier != null;
        } catch (HibernateException ignore) {
        }
        return false;
    }
}
