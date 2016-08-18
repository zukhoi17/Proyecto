/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.exceptions.IllegalOrphanException;
import Dao.exceptions.NonexistentEntityException;
import Dao.exceptions.RollbackFailureException;
import Entity.DefectuosoEntity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.OrdenesproduccionEntity;
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
public class DefectuosoEntityJpaController implements Serializable {

    public DefectuosoEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DefectuosoEntity defectuosoEntity) throws RollbackFailureException, Exception {
        if (defectuosoEntity.getOrdenesproduccionEntityCollection() == null) {
            defectuosoEntity.setOrdenesproduccionEntityCollection(new ArrayList<OrdenesproduccionEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<OrdenesproduccionEntity> attachedOrdenesproduccionEntityCollection = new ArrayList<OrdenesproduccionEntity>();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach : defectuosoEntity.getOrdenesproduccionEntityCollection()) {
                ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach = em.getReference(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getClass(), ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getIdOrdenProduccion());
                attachedOrdenesproduccionEntityCollection.add(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach);
            }
            defectuosoEntity.setOrdenesproduccionEntityCollection(attachedOrdenesproduccionEntityCollection);
            em.persist(defectuosoEntity);
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntity : defectuosoEntity.getOrdenesproduccionEntityCollection()) {
                DefectuosoEntity oldDefectuososOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity = ordenesproduccionEntityCollectionOrdenesproduccionEntity.getDefectuosos();
                ordenesproduccionEntityCollectionOrdenesproduccionEntity.setDefectuosos(defectuosoEntity);
                ordenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
                if (oldDefectuososOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity != null) {
                    oldDefectuososOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
                    oldDefectuososOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(oldDefectuososOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity);
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

    public void edit(DefectuosoEntity defectuosoEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DefectuosoEntity persistentDefectuosoEntity = em.find(DefectuosoEntity.class, defectuosoEntity.getIdDefectuoso());
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOld = persistentDefectuosoEntity.getOrdenesproduccionEntityCollection();
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionNew = defectuosoEntity.getOrdenesproduccionEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOldOrdenesproduccionEntity : ordenesproduccionEntityCollectionOld) {
                if (!ordenesproduccionEntityCollectionNew.contains(ordenesproduccionEntityCollectionOldOrdenesproduccionEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdenesproduccionEntity " + ordenesproduccionEntityCollectionOldOrdenesproduccionEntity + " since its defectuosos field is not nullable.");
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
            defectuosoEntity.setOrdenesproduccionEntityCollection(ordenesproduccionEntityCollectionNew);
            defectuosoEntity = em.merge(defectuosoEntity);
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionNewOrdenesproduccionEntity : ordenesproduccionEntityCollectionNew) {
                if (!ordenesproduccionEntityCollectionOld.contains(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity)) {
                    DefectuosoEntity oldDefectuososOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity = ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.getDefectuosos();
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.setDefectuosos(defectuosoEntity);
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                    if (oldDefectuososOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity != null && !oldDefectuososOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity.equals(defectuosoEntity)) {
                        oldDefectuososOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                        oldDefectuososOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(oldDefectuososOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity);
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
                Integer id = defectuosoEntity.getIdDefectuoso();
                if (findDefectuosoEntity(id) == null) {
                    throw new NonexistentEntityException("The defectuosoEntity with id " + id + " no longer exists.");
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
            DefectuosoEntity defectuosoEntity;
            try {
                defectuosoEntity = em.getReference(DefectuosoEntity.class, id);
                defectuosoEntity.getIdDefectuoso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The defectuosoEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOrphanCheck = defectuosoEntity.getOrdenesproduccionEntityCollection();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrphanCheckOrdenesproduccionEntity : ordenesproduccionEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DefectuosoEntity (" + defectuosoEntity + ") cannot be destroyed since the OrdenesproduccionEntity " + ordenesproduccionEntityCollectionOrphanCheckOrdenesproduccionEntity + " in its ordenesproduccionEntityCollection field has a non-nullable defectuosos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(defectuosoEntity);
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

    public List<DefectuosoEntity> findDefectuosoEntityEntities() {
        return findDefectuosoEntityEntities(true, -1, -1);
    }

    public List<DefectuosoEntity> findDefectuosoEntityEntities(int maxResults, int firstResult) {
        return findDefectuosoEntityEntities(false, maxResults, firstResult);
    }

    private List<DefectuosoEntity> findDefectuosoEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DefectuosoEntity.class));
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

    public DefectuosoEntity findDefectuosoEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DefectuosoEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getDefectuosoEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DefectuosoEntity> rt = cq.from(DefectuosoEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
