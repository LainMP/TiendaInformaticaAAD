package dao;

import entity.Fabricante;
import entity.Producto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class ProductoDAO {

    public void guardar(Producto producto) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(producto);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    //necesario para encontrar el producto por id y actualizarlo.
    public Producto findById(int id) {
        Transaction tx = null;
        Producto producto = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            producto = (Producto) session.find(Producto.class, id);
            tx.commit();
            return producto;
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            return null;
        }
    }

    public int updateProd(Producto prod) {//Se le pasa el objeto producto para que haga el merge en la base de datos en vez de los parametros sueltos
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            if (prod != null) {
                session.merge(prod);
                tx.commit();
                return 1;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            return -1;
        }
    }

    public int deleteProd(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Producto prod = session.find(Producto.class, id);
            if (prod != null) {
                session.remove(prod);
                tx.commit();
                return 1;
            }  else {
                return 0;
            }
        }  catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            return -1;
        }
    }

    public List<Producto> showAll() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT p FROM Producto p",
                    Producto.class)
                    .getResultList();
        }
    }

    public List<Producto> searchByName(String name) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            List<Producto> listaProd = session.createQuery(
                    "SELECT p FROM Producto p WHERE p.nombre = :name",
                    Producto.class)
                    .setParameter("name", name)
                    .getResultList();
            tx.commit();
            return listaProd;
        }
    }

}
