package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.SettingsFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Settings;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class SettingsRepo implements SettingsFacade {

    private static SettingsRepo instance;
    public static SettingsRepo getInstance() {
        if (instance == null) {
            instance = new SettingsRepo();
        }
        return instance;
    }


    @Override
    public  void createSettings(Settings settings) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(settings);
            session.getTransaction().commit();
        } catch (HibernateException ignore) {

        }
    }


    @Override
    public Settings getSettings() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Settings> settings = session.createQuery(CrudSql.RETRIEVE_SETTINGS, Settings.class).list();
            session.getTransaction().commit();
            if(!settings.isEmpty()){
                return settings.get(0);
            }
        }catch(HibernateException ignore) {
            ignore.printStackTrace();
        }
        return null;
    }



    @Override
    public void deleteSettings(String companyName) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Settings settings = session.get(Settings.class, companyName);
            session.remove(settings);
            session.getTransaction().commit();
        }catch(HibernateException ignore) {
        }
    }

    @Override
    public Settings getDefaultLanguage() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Settings> settings = session.createQuery(CrudSql.RETRIEVE_LANGUAGE, Settings.class).list();
            session.getTransaction().commit();
            if(!settings.isEmpty()){
                return settings.get(0);
            }
        }catch(HibernateException ignore) {
        }
        return new Settings();
    }


}
