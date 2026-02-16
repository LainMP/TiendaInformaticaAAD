package dao;

import entity.Fabricante;
import entity.Producto;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class FabricanteDAO {

    public void guardar(Fabricante fabricante) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(fabricante);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public int updateFab (int id, String newName) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Fabricante fab = session.find(Fabricante.class, id);
            if (fab != null) {
                fab.setNombre(newName);
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

    public int deleteFab (int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Fabricante fab = session.find(Fabricante.class, id);
            if (fab != null) {
                session.remove(fab);
                tx.commit();
                return 1;
            } else  {
                return 0;
            }
        }  catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            return -1;
        }
    }

    public List<Producto> getProductsByFab(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT p FROM Producto p WHERE LOWER(p.fabricante.nombre) = :name", Producto.class)
                    .setParameter("name", name.toLowerCase())
                    .getResultList();
        }
    }

    public List<Fabricante> searchByProduct(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT f FROM Fabricante f JOIN f.listaProductos p WHERE LOWER(p.nombre) = :name",
                        Fabricante.class)
                    .setParameter("name", name.toLowerCase())
                    .getResultList();
        }
    }

    public List<Fabricante> searchByName(String  name) throws Exception {
        Fabricante fabricante = new Fabricante();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            List<Fabricante> listaF = session.createQuery(
                    "SELECT f FROM Fabricante f WHERE LOWER(f.nombre) = :name",
                        Fabricante.class)
                        .setParameter("name", name.toLowerCase())
                        .getResultList();
            tx.commit();
            return  listaF;
        }
    }

    public List<Fabricante> showAllFabs() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT f FROM Fabricante f",
                            Fabricante.class)
                            .getResultList();
        }
    }

    //Añadido para recuperar el fabricante de la bd y verificar su existencia antes de eliminarlo.
    public Fabricante findById(int id) {
        Transaction tx = null;
        Fabricante fabricante = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            fabricante = (Fabricante) session.find(Fabricante.class, id);
            if (fabricante != null) {
                Hibernate.initialize(fabricante.getListaProductos());
            }
            tx.commit();
            return fabricante;
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            return null;
        }
    }

}
