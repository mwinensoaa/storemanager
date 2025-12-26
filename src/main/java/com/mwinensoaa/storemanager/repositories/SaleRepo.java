package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.SaleFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.Sale;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class SaleRepo implements SaleFacade {

    private static SaleRepo instance;
    public static SaleRepo getInstance() {
        if (instance == null) {
            instance = new SaleRepo();
        }
        return instance;
    }


    @Override
    public void createSale(Sale sale) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(sale);
            session.getTransaction().commit();
        } catch (HibernateException ignore) {
        }
    }

    @Override
    public List<Sale> getSales() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Sale> sales = session.createQuery(CrudSql.RETRIEVE_SALES, Sale.class).list();
            session.getTransaction().commit();
            return sales;
        } catch (HibernateException ignore) {

        }
        return List.of();
    }

    @Override
    public List<Sale> getSales(String date) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Sale> sales = session.createQuery(CrudSql.RETRIEVE_SALES_BY_DATE, Sale.class)
                    .setParameter(1, date)
                    .list();
            session.getTransaction().commit();
            return sales;
        } catch (HibernateException ignore) {

        }
        return List.of();
    }


    @Override
    public Sale getSale(String saleId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
           Sale sale = session.get(Sale.class, saleId);
            session.getTransaction().commit();
            return sale;
        } catch (HibernateException ignore) {
        }
        return null;
    }


    @Override
    public void deleteSale(int saleId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Sale sale = session.get(Sale.class, saleId);
            session.remove(sale);
            session.getTransaction().commit();
        } catch (HibernateException ignore) {
        }
    }

    @Override
    public boolean saleExists(String saleId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Sale sale = session.get(Sale.class, saleId);
            session.getTransaction().commit();
            return sale != null;
        } catch (HibernateException ignore) {
        }
        return false;
    }


}
