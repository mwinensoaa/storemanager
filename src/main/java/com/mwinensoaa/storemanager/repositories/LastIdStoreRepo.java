package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.LastIdStoreFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.LastIdStore;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class LastIdStoreRepo implements LastIdStoreFacade {

    private static LastIdStoreRepo instance;
    public static LastIdStoreRepo getInstance() {
        if (instance == null) {
            instance = new LastIdStoreRepo();
        }
        return instance;
    }

    @Override
    public void updateLastId(LastIdStore lastIdStore) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(lastIdStore);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
    }

    @Override
    public LastIdStore getIds() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
           List<LastIdStore> lastIdStore = session.createQuery(CrudSql.RETRIEVE_LAST_ID, LastIdStore.class).list();
            session.getTransaction().commit();
            if(!lastIdStore.isEmpty()){
                return lastIdStore.get(0);
            }
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return null;
    }

    @Override
    public LastIdStore getId(String key) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            LastIdStore lastIdStore = session.get(LastIdStore.class,key);
            session.getTransaction().commit();
            return lastIdStore;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return null;
    }


}
