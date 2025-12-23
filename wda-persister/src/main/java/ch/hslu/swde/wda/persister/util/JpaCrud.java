package ch.hslu.swde.wda.persister.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse JpaCrud ist die generische Implementation der Create, Read, Update und Delete Funktionen.
 */

public class JpaCrud<T> {

    final private Class<T> typeParameterClass;

    public JpaCrud(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public T add(T type) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(type);
            em.getTransaction().commit();
            return type;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public T delete(int id) {
        EntityManager em = JpaUtil.createEntityManager();
        T type = em.find(typeParameterClass, id);
        if (type != null) {
            try {
                em.getTransaction().begin();
                em.remove(type);
                em.getTransaction().commit();
                return type;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e;
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }
        return null;
    }

    public T update(T type) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(type);
            em.getTransaction().commit();
            return type;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }


    public T find(int id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.find(typeParameterClass, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public List<T> all() {
        List<T> types;

        EntityManager em = JpaUtil.createEntityManager();

        try {
            TypedQuery<T> tQry = em.createQuery("SELECT e FROM " + typeParameterClass.getName() + " e ORDER BY e.id ASC", typeParameterClass);
            types = tQry.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        return types != null ? types : new ArrayList<T>();
    }
}
