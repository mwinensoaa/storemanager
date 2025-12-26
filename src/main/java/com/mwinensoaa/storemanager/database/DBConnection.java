package com.mwinensoaa.storemanager.database;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class DBConnection {
    private static final SessionFactory SESSIONFACTORY;
    private static   Session SESSION;
    static {
        try {
            SESSIONFACTORY = new Configuration().configure(DBConnection.class.getResource("/hibernate/hibernate.xml")).buildSessionFactory();
        } catch (HibernateException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getOpenedSession(){
        if(SESSIONFACTORY.isClosed() || !SESSIONFACTORY.isClosed()){
            SESSION = SESSIONFACTORY.openSession();
        }
        return SESSION;
    }

    public static void shutdownDatabase(){
        if(SESSIONFACTORY.isOpen()){
            SESSIONFACTORY.close();
        }
    }

}
