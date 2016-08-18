/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.exceptions.IllegalOrphanException;
import Dao.exceptions.NonexistentEntityException;
import Dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.OrdenesproduccionEntity;
import Entity.ResiduoEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ponchis
 */
public class ResiduoEntityJpaController implements Serializable {

    public ResiduoEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ResiduoEntity residuoEntity) throws RollbackFailureException, Exception {
        if (residuoEntity.getOrdenesproduccionEntityCollection() == null) {
            residuoEntity.setOrdenesproduccionEntityCollection(new ArrayList<OrdenesproduccionEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<OrdenesproduccionEntity> attachedOrdenesproduccionEntityCollection = new ArrayList<OrdenesproduccionEntity>();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach : residuoEntity.getOrdenesproduccionEntityCollection()) {
                ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach = em.getReference(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getClass(), ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getIdOrdenProduccion());
                attachedOrdenesproduccionEntityCollection.add(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach);
            }
            residuoEntity.setOrdenesproduccionEntityCollection(attachedOrdenesproduccionEntityCollection);
            em.persist(residuoEntity);
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntity : residuoEntity.getOrdenesproduccionEntityCollection()) {
                ResiduoEntity oldResiduoOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity = ordenesproduccionEntityCollectionOrdenesproduccionEntity.getResiduo();
                ordenesproduccionEntityCollectionOrdenesproduccionEntity.setResiduo(residuoEntity);
                ordenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
                if (oldResiduoOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity != null) {
                    oldResiduoOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
                    oldResiduoOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(oldResiduoOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity);
                }
            }
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

    public void edit(ResiduoEntity residuoEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ResiduoEntity persistentResiduoEntity = em.find(ResiduoEntity.class, residuoEntity.getIdResiduo());
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOld = persistentResiduoEntity.getOrdenesproduccionEntityCollection();
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionNew = residuoEntity.getOrdenesproduccionEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOldOrdenesproduccionEntity : ordenesproduccionEntityCollectionOld) {
                if (!ordenesproduccionEntityCollectionNew.contains(ordenesproduccionEntityCollectionOldOrdenesproduccionEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdenesproduccionEntity " + ordenesproduccionEntityCollectionOldOrdenesproduccionEntity + " since its residuo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<OrdenesproduccionEntity> attachedOrdenesproduccionEntityCollectionNew = new ArrayList<OrdenesproduccionEntity>();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach : ordenesproduccionEntityCollectionNew) {
                ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach = em.getReference(ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach.getClass(), ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach.getIdOrdenProduccion());
                attachedOrdenesproduccionEntityCollectionNew.add(ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach);
            }
            ordenesproduccionEntityCollectionNew = attachedOrdenesproduccionEntityCollectionNew;
            residuoEntity.setOrdenesproduccionEntityCollection(ordenesproduccionEntityCollectionNew);
            residuoEntity = em.merge(residuoEntity);
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionNewOrdenesproduccionEntity : ordenesproduccionEntityCollectionNew) {
                if (!ordenesproduccionEntityCollectionOld.contains(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity)) {
                    ResiduoEntity oldResiduoOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity = ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.getResiduo();
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.setResiduo(residuoEntity);
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                    if (oldResiduoOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity != null && !oldResiduoOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity.equals(residuoEntity)) {
                        oldResiduoOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                        oldResiduoOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(oldResiduoOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = residuoEntity.getIdResiduo();
                if (findResiduoEntity(id) == null) {
                    throw new NonexistentEntityException("The residuoEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ResiduoEntity residuoEntity;
            try {
                residuoEntity = em.getReference(ResiduoEntity.class, id);
                residuoEntity.getIdResiduo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The residuoEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOrphanCheck = residuoEntity.getOrdenesproduccionEntityCollection();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrphanCheckOrdenesproduccionEntity : ordenesproduccionEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ResiduoEntity (" + residuoEntity + ") cannot be destroyed since the OrdenesproduccionEntity " + ordenesproduccionEntityCollectionOrphanCheckOrdenesproduccionEntity + " in its ordenesproduccionEntityCollection field has a non-nullable residuo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(residuoEntity);
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

    public List<ResiduoEntity> findResiduoEntityEntities() {
        return findResiduoEntityEntities(true, -1, -1);
    }

    public List<ResiduoEntity> findResiduoEntityEntities(int maxResults, int firstResult) {
        return findResiduoEntityEntities(false, maxResults, firstResult);
    }

    private List<ResiduoEntity> findResiduoEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ResiduoEntity.class));
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

    public ResiduoEntity findResiduoEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ResiduoEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getResiduoEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ResiduoEntity> rt = cq.from(ResiduoEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
