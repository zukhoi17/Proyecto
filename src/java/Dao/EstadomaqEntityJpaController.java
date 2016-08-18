/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.exceptions.NonexistentEntityException;
import Dao.exceptions.PreexistingEntityException;
import Dao.exceptions.RollbackFailureException;
import Entity.EstadomaqEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ponchis
 */
public class EstadomaqEntityJpaController implements Serializable {

    public EstadomaqEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadomaqEntity estadomaqEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(estadomaqEntity);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstadomaqEntity(estadomaqEntity.getIdEstado()) != null) {
                throw new PreexistingEntityException("EstadomaqEntity " + estadomaqEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadomaqEntity estadomaqEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            estadomaqEntity = em.merge(estadomaqEntity);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadomaqEntity.getIdEstado();
                if (findEstadomaqEntity(id) == null) {
                    throw new NonexistentEntityException("The estadomaqEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EstadomaqEntity estadomaqEntity;
            try {
                estadomaqEntity = em.getReference(EstadomaqEntity.class, id);
                estadomaqEntity.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadomaqEntity with id " + id + " no longer exists.", enfe);
            }
            em.remove(estadomaqEntity);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadomaqEntity> findEstadomaqEntityEntities() {
        return findEstadomaqEntityEntities(true, -1, -1);
    }

    public List<EstadomaqEntity> findEstadomaqEntityEntities(int maxResults, int firstResult) {
        return findEstadomaqEntityEntities(false, maxResults, firstResult);
    }

    private List<EstadomaqEntity> findEstadomaqEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadomaqEntity.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public EstadomaqEntity findEstadomaqEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadomaqEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadomaqEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadomaqEntity> rt = cq.from(EstadomaqEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
