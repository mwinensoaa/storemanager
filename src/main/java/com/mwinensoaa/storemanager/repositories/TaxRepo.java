package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.TaxFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Tax;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class TaxRepo implements TaxFacade {

    private static TaxRepo instance;
    public static TaxRepo getInstance() {
        if (instance == null) {
            instance = new TaxRepo();
        }
        return instance;
    }


    @Override
    public void createTax(Tax tax) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(tax);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
    }

    @Override
    public List<Tax> getAllTaxes() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Tax> taxes = session.createQuery(CrudSql.RETRIEVE_TAXES, Tax.class).list();
            session.getTransaction().commit();
            if(!taxes.isEmpty()){
                return  taxes;
            }
        } catch (HibernateException ignore) {}

        return List.of();
    }

    @Override
    public Tax getTax(String taxTitle) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Tax tax = session.get(Tax.class, taxTitle);
            session.getTransaction().commit();
            if(tax != null){
                return tax;
            }
        } catch (HibernateException ignore) {

        }
        return null;
    }

    @Override
    public void deleteTax(String taxTitle) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Tax tax = session.get(Tax.class, taxTitle);
            session.remove(tax);
            session.getTransaction().commit();

        } catch (HibernateException ignore) {

        }
    }

    @Override
    public boolean taxTitleExist(String taxTitle) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Tax tax = session.get(Tax.class, taxTitle);
            session.getTransaction().commit();
            return tax != null;
        } catch (HibernateException ignore) {

        }
        return false;
    }
}
