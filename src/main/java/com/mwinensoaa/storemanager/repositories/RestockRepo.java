package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.RestockFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Restock;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class RestockRepo implements RestockFacade {

    private static RestockRepo instance;
    public static RestockRepo getInstance() {
        if (instance == null) {
            instance = new RestockRepo();
        }
        return instance;
    }


    @Override
    public void createRestock(Restock restock) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(restock);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
    }

    @Override
    public List<Restock> getAllRestock() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Restock> restocks = session.createQuery(CrudSql.RETRIEVE_ALL_RESTOCK, Restock.class).list();
            session.getTransaction().commit();
            return restocks;
        } catch (HibernateException ignored) {

        }
        return List.of();
    }

    @Override
    public List<Restock> getRestockByYear(String year) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Restock> restocks = session.createQuery(CrudSql.RETRIEVE_ALL_RESTOCK_BY_YEARS, Restock.class)
                    .setParameter(1, "%/"+year).list();
            session.getTransaction().commit();
            return restocks;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<Restock> getRestockByMonth(String month) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Restock> restocks = session.createQuery(CrudSql.RETRIEVE_ALL_RESTOCK_BY_MONTH, Restock.class)
                    .setParameter(1, "%/"+month+"/%").list();
            session.getTransaction().commit();
            return restocks;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return List.of();
    }



}
