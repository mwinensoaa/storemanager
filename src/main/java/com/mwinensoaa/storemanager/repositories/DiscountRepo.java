package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.DiscountFacade;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.entities.Discount;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class DiscountRepo implements DiscountFacade {

    private static DiscountRepo instance;

    public static DiscountRepo getInstance() {
        if (instance == null) {
            instance = new DiscountRepo();
        }
        return instance;
    }


    @Override
    public void createDiscount(Discount discount) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(discount);
            session.getTransaction().commit();
        } catch (HibernateException ignored) {

        }
    }

    @Override
    public void deleteDiscount(String productTitle) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Discount discount = session.get(Discount.class, productTitle);
            session.remove(discount);
            session.getTransaction().commit();
        } catch (HibernateException ignored) {

        }
    }

    @Override
    public Discount getDiscount(String productTitle) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Discount discount = session.get(Discount.class, productTitle);
            session.getTransaction().commit();
            return discount;
        } catch (HibernateException ignored) {

        }
        return new Discount();
    }

    @Override
    public List<Discount> getAllDiscounts() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Discount> discounts = session.createQuery(CrudSql.RETRIEVE_ALL_DISCOUNTS, Discount.class).list();
            session.getTransaction().commit();
            return discounts;
        } catch (HibernateException ignored) {

        }
        return List.of();
    }

    @Override
    public boolean isDiscountExists(String discountTitle) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Discount discount = session.get(Discount.class, discountTitle);
            session.getTransaction().commit();
            return discount != null;
        } catch (HibernateException ignored) {

        }

        return false;
    }

}
