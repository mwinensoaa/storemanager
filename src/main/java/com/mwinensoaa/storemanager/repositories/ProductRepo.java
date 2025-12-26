package com.mwinensoaa.storemanager.repositories;

import com.mwinensoaa.storemanager.dao.ProductFacade;
import com.mwinensoaa.storemanager.database.DBConnection;
import com.mwinensoaa.storemanager.database.CrudSql;
import com.mwinensoaa.storemanager.entities.Product;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class ProductRepo implements ProductFacade {

    private static ProductRepo instance;

    public static ProductRepo getInstance() {
        if (instance == null) {
            instance = new ProductRepo();
        }
        return instance;
    }


    @Override
    public void createProduct(Product product) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            session.merge(product);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }

    }

    @Override
    public List<Product> getAllProducts() {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Product> products = session.createQuery(CrudSql.RETRIEVE_ALL_PRODUCT, Product.class).list();
            session.getTransaction().commit();
            if(!products.isEmpty())
                return products;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Product getProduct(String productId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
          List<Product> products = session.createQuery(CrudSql.RETRIEVE_PRODUCT_BY_ID, Product.class)
                  .setParameter(1, productId).list();
            session.getTransaction().commit();
            if(!products.isEmpty()){
                return products.get(0);
            }
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return null;
    }


    @Override
    public void deleteProduct(String productId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, productId);
            session.remove(product);
            session.getTransaction().commit();
        } catch (HibernateException ignored) {

        }
    }

    @Override
    public void minusProduct(String productName, int numberBought) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Product product = session.createQuery(CrudSql.RETRIEVE_PRODUCT_BY_NAME,Product.class)
                    .setParameter(1, productName).getSingleResult();
            product.setQuantityInStock(product.getQuantityInStock() - numberBought);
            session.merge(product);
            session.getTransaction().commit();
        } catch (HibernateException ignore) {

        }
    }

    @Override
    public boolean productCodeExists(String productId) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, productId);
            session.getTransaction().commit();
            return product != null;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean productNameExist(String productDescription) {
        try (Session session = DBConnection.getOpenedSession()) {
            session.beginTransaction();
            List<Product> products = session.createQuery(CrudSql.RETRIEVE_PRODUCT_BY_NAME,Product.class)
                    .setParameter(1, productDescription).list();
            session.getTransaction().commit();
            return !products.isEmpty();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        }
        return false;
    }


}
