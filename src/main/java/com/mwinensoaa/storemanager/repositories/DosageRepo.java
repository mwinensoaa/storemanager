package com.mwinensoaa.storemanager.repositories;


import com.mwinensoaa.storemanager.dao.DosageFacade;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Dosage;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class DosageRepo implements DosageFacade {

    private static DosageRepo instance;
    public static DosageRepo getInstance() {
        if (instance == null) {
            instance = new DosageRepo();
        }
        return instance;
    }

    @Override
    public void createDosage(Dosage dosage) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(dosage);
            session.getTransaction().commit();
        } catch (HibernateException ignore) {

        }
    }

    @Override
    public Dosage getDosage(String productId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Dosage dosage = session.get(Dosage.class, productId);
            session.getTransaction().commit();
            return dosage;
        } catch (HibernateException ignore) {

        }
        return null;
    }

    @Override
    public void deleteDosage(String productId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Dosage dosage = session.get(Dosage.class, productId);
            session.remove(dosage);
            session.getTransaction().commit();
        } catch (HibernateException ignore) {
        }
    }


}
