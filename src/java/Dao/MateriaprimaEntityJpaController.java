/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.exceptions.IllegalOrphanException;
import Dao.exceptions.NonexistentEntityException;
import Dao.exceptions.RollbackFailureException;
import Entity.MateriaprimaEntity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.ProductomateriaprimaEntity;
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
public class MateriaprimaEntityJpaController implements Serializable {

    public MateriaprimaEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MateriaprimaEntity materiaprimaEntity) throws RollbackFailureException, Exception {
        if (materiaprimaEntity.getProductomateriaprimaEntityCollection() == null) {
            materiaprimaEntity.setProductomateriaprimaEntityCollection(new ArrayList<ProductomateriaprimaEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<ProductomateriaprimaEntity> attachedProductomateriaprimaEntityCollection = new ArrayList<ProductomateriaprimaEntity>();
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach : materiaprimaEntity.getProductomateriaprimaEntityCollection()) {
                productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach = em.getReference(productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach.getClass(), productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach.getProductomateriaprimaEntityPK());
                attachedProductomateriaprimaEntityCollection.add(productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach);
            }
            materiaprimaEntity.setProductomateriaprimaEntityCollection(attachedProductomateriaprimaEntityCollection);
            em.persist(materiaprimaEntity);
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionProductomateriaprimaEntity : materiaprimaEntity.getProductomateriaprimaEntityCollection()) {
                MateriaprimaEntity oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity = productomateriaprimaEntityCollectionProductomateriaprimaEntity.getMateriaprimaEntity();
                productomateriaprimaEntityCollectionProductomateriaprimaEntity.setMateriaprimaEntity(materiaprimaEntity);
                productomateriaprimaEntityCollectionProductomateriaprimaEntity = em.merge(productomateriaprimaEntityCollectionProductomateriaprimaEntity);
                if (oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity != null) {
                    oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity.getProductomateriaprimaEntityCollection().remove(productomateriaprimaEntityCollectionProductomateriaprimaEntity);
                    oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity = em.merge(oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity);
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

    public void edit(MateriaprimaEntity materiaprimaEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MateriaprimaEntity persistentMateriaprimaEntity = em.find(MateriaprimaEntity.class, materiaprimaEntity.getIdMateriaPrima());
            Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollectionOld = persistentMateriaprimaEntity.getProductomateriaprimaEntityCollection();
            Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollectionNew = materiaprimaEntity.getProductomateriaprimaEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionOldProductomateriaprimaEntity : productomateriaprimaEntityCollectionOld) {
                if (!productomateriaprimaEntityCollectionNew.contains(productomateriaprimaEntityCollectionOldProductomateriaprimaEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductomateriaprimaEntity " + productomateriaprimaEntityCollectionOldProductomateriaprimaEntity + " since its materiaprimaEntity field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ProductomateriaprimaEntity> attachedProductomateriaprimaEntityCollectionNew = new ArrayList<ProductomateriaprimaEntity>();
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach : productomateriaprimaEntityCollectionNew) {
                productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach = em.getReference(productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach.getClass(), productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach.getProductomateriaprimaEntityPK());
                attachedProductomateriaprimaEntityCollectionNew.add(productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach);
            }
            productomateriaprimaEntityCollectionNew = attachedProductomateriaprimaEntityCollectionNew;
            materiaprimaEntity.setProductomateriaprimaEntityCollection(productomateriaprimaEntityCollectionNew);
            materiaprimaEntity = em.merge(materiaprimaEntity);
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionNewProductomateriaprimaEntity : productomateriaprimaEntityCollectionNew) {
                if (!productomateriaprimaEntityCollectionOld.contains(productomateriaprimaEntityCollectionNewProductomateriaprimaEntity)) {
                    MateriaprimaEntity oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity = productomateriaprimaEntityCollectionNewProductomateriaprimaEntity.getMateriaprimaEntity();
                    productomateriaprimaEntityCollectionNewProductomateriaprimaEntity.setMateriaprimaEntity(materiaprimaEntity);
                    productomateriaprimaEntityCollectionNewProductomateriaprimaEntity = em.merge(productomateriaprimaEntityCollectionNewProductomateriaprimaEntity);
                    if (oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity != null && !oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity.equals(materiaprimaEntity)) {
                        oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity.getProductomateriaprimaEntityCollection().remove(productomateriaprimaEntityCollectionNewProductomateriaprimaEntity);
                        oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity = em.merge(oldMateriaprimaEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity);
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
                Integer id = materiaprimaEntity.getIdMateriaPrima();
                if (findMateriaprimaEntity(id) == null) {
                    throw new NonexistentEntityException("The materiaprimaEntity with id " + id + " no longer exists.");
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
            MateriaprimaEntity materiaprimaEntity;
            try {
                materiaprimaEntity = em.getReference(MateriaprimaEntity.class, id);
                materiaprimaEntity.getIdMateriaPrima();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiaprimaEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollectionOrphanCheck = materiaprimaEntity.getProductomateriaprimaEntityCollection();
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionOrphanCheckProductomateriaprimaEntity : productomateriaprimaEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MateriaprimaEntity (" + materiaprimaEntity + ") cannot be destroyed since the ProductomateriaprimaEntity " + productomateriaprimaEntityCollectionOrphanCheckProductomateriaprimaEntity + " in its productomateriaprimaEntityCollection field has a non-nullable materiaprimaEntity field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(materiaprimaEntity);
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

    public List<MateriaprimaEntity> findMateriaprimaEntityEntities() {
        return findMateriaprimaEntityEntities(true, -1, -1);
    }

    public List<MateriaprimaEntity> findMateriaprimaEntityEntities(int maxResults, int firstResult) {
        return findMateriaprimaEntityEntities(false, maxResults, firstResult);
    }

    private List<MateriaprimaEntity> findMateriaprimaEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MateriaprimaEntity.class));
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

    public MateriaprimaEntity findMateriaprimaEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MateriaprimaEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaprimaEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MateriaprimaEntity> rt = cq.from(MateriaprimaEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
