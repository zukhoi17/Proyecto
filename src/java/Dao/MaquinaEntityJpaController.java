/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.exceptions.IllegalOrphanException;
import Dao.exceptions.NonexistentEntityException;
import Dao.exceptions.RollbackFailureException;
import Entity.MaquinaEntity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.ProductoEntity;
import java.util.ArrayList;
import java.util.Collection;
import Entity.OrdenesproduccionEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ponchis
 */
public class MaquinaEntityJpaController implements Serializable {

    public MaquinaEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MaquinaEntity maquinaEntity) throws RollbackFailureException, Exception {
        if (maquinaEntity.getProductoEntityCollection() == null) {
            maquinaEntity.setProductoEntityCollection(new ArrayList<ProductoEntity>());
        }
        if (maquinaEntity.getOrdenesproduccionEntityCollection() == null) {
            maquinaEntity.setOrdenesproduccionEntityCollection(new ArrayList<OrdenesproduccionEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<ProductoEntity> attachedProductoEntityCollection = new ArrayList<ProductoEntity>();
            for (ProductoEntity productoEntityCollectionProductoEntityToAttach : maquinaEntity.getProductoEntityCollection()) {
                productoEntityCollectionProductoEntityToAttach = em.getReference(productoEntityCollectionProductoEntityToAttach.getClass(), productoEntityCollectionProductoEntityToAttach.getIdProducto());
                attachedProductoEntityCollection.add(productoEntityCollectionProductoEntityToAttach);
            }
            maquinaEntity.setProductoEntityCollection(attachedProductoEntityCollection);
            Collection<OrdenesproduccionEntity> attachedOrdenesproduccionEntityCollection = new ArrayList<OrdenesproduccionEntity>();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach : maquinaEntity.getOrdenesproduccionEntityCollection()) {
                ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach = em.getReference(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getClass(), ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getIdOrdenProduccion());
                attachedOrdenesproduccionEntityCollection.add(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach);
            }
            maquinaEntity.setOrdenesproduccionEntityCollection(attachedOrdenesproduccionEntityCollection);
            em.persist(maquinaEntity);
            for (ProductoEntity productoEntityCollectionProductoEntity : maquinaEntity.getProductoEntityCollection()) {
                productoEntityCollectionProductoEntity.getMaquinaEntityCollection().add(maquinaEntity);
                productoEntityCollectionProductoEntity = em.merge(productoEntityCollectionProductoEntity);
            }
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntity : maquinaEntity.getOrdenesproduccionEntityCollection()) {
                MaquinaEntity oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity = ordenesproduccionEntityCollectionOrdenesproduccionEntity.getMaquinasidMaquina();
                ordenesproduccionEntityCollectionOrdenesproduccionEntity.setMaquinasidMaquina(maquinaEntity);
                ordenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
                if (oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity != null) {
                    oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
                    oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity);
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

    public void edit(MaquinaEntity maquinaEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MaquinaEntity persistentMaquinaEntity = em.find(MaquinaEntity.class, maquinaEntity.getIdMaquina());
            Collection<ProductoEntity> productoEntityCollectionOld = persistentMaquinaEntity.getProductoEntityCollection();
            Collection<ProductoEntity> productoEntityCollectionNew = maquinaEntity.getProductoEntityCollection();
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOld = persistentMaquinaEntity.getOrdenesproduccionEntityCollection();
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionNew = maquinaEntity.getOrdenesproduccionEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOldOrdenesproduccionEntity : ordenesproduccionEntityCollectionOld) {
                if (!ordenesproduccionEntityCollectionNew.contains(ordenesproduccionEntityCollectionOldOrdenesproduccionEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdenesproduccionEntity " + ordenesproduccionEntityCollectionOldOrdenesproduccionEntity + " since its maquinasidMaquina field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ProductoEntity> attachedProductoEntityCollectionNew = new ArrayList<ProductoEntity>();
            for (ProductoEntity productoEntityCollectionNewProductoEntityToAttach : productoEntityCollectionNew) {
                productoEntityCollectionNewProductoEntityToAttach = em.getReference(productoEntityCollectionNewProductoEntityToAttach.getClass(), productoEntityCollectionNewProductoEntityToAttach.getIdProducto());
                attachedProductoEntityCollectionNew.add(productoEntityCollectionNewProductoEntityToAttach);
            }
            productoEntityCollectionNew = attachedProductoEntityCollectionNew;
            maquinaEntity.setProductoEntityCollection(productoEntityCollectionNew);
            Collection<OrdenesproduccionEntity> attachedOrdenesproduccionEntityCollectionNew = new ArrayList<OrdenesproduccionEntity>();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach : ordenesproduccionEntityCollectionNew) {
                ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach = em.getReference(ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach.getClass(), ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach.getIdOrdenProduccion());
                attachedOrdenesproduccionEntityCollectionNew.add(ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach);
            }
            ordenesproduccionEntityCollectionNew = attachedOrdenesproduccionEntityCollectionNew;
            maquinaEntity.setOrdenesproduccionEntityCollection(ordenesproduccionEntityCollectionNew);
            maquinaEntity = em.merge(maquinaEntity);
            for (ProductoEntity productoEntityCollectionOldProductoEntity : productoEntityCollectionOld) {
                if (!productoEntityCollectionNew.contains(productoEntityCollectionOldProductoEntity)) {
                    productoEntityCollectionOldProductoEntity.getMaquinaEntityCollection().remove(maquinaEntity);
                    productoEntityCollectionOldProductoEntity = em.merge(productoEntityCollectionOldProductoEntity);
                }
            }
            for (ProductoEntity productoEntityCollectionNewProductoEntity : productoEntityCollectionNew) {
                if (!productoEntityCollectionOld.contains(productoEntityCollectionNewProductoEntity)) {
                    productoEntityCollectionNewProductoEntity.getMaquinaEntityCollection().add(maquinaEntity);
                    productoEntityCollectionNewProductoEntity = em.merge(productoEntityCollectionNewProductoEntity);
                }
            }
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionNewOrdenesproduccionEntity : ordenesproduccionEntityCollectionNew) {
                if (!ordenesproduccionEntityCollectionOld.contains(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity)) {
                    MaquinaEntity oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity = ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.getMaquinasidMaquina();
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.setMaquinasidMaquina(maquinaEntity);
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                    if (oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity != null && !oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity.equals(maquinaEntity)) {
                        oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                        oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(oldMaquinasidMaquinaOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity);
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
                Integer id = maquinaEntity.getIdMaquina();
                if (findMaquinaEntity(id) == null) {
                    throw new NonexistentEntityException("The maquinaEntity with id " + id + " no longer exists.");
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
            MaquinaEntity maquinaEntity;
            try {
                maquinaEntity = em.getReference(MaquinaEntity.class, id);
                maquinaEntity.getIdMaquina();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The maquinaEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOrphanCheck = maquinaEntity.getOrdenesproduccionEntityCollection();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrphanCheckOrdenesproduccionEntity : ordenesproduccionEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MaquinaEntity (" + maquinaEntity + ") cannot be destroyed since the OrdenesproduccionEntity " + ordenesproduccionEntityCollectionOrphanCheckOrdenesproduccionEntity + " in its ordenesproduccionEntityCollection field has a non-nullable maquinasidMaquina field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ProductoEntity> productoEntityCollection = maquinaEntity.getProductoEntityCollection();
            for (ProductoEntity productoEntityCollectionProductoEntity : productoEntityCollection) {
                productoEntityCollectionProductoEntity.getMaquinaEntityCollection().remove(maquinaEntity);
                productoEntityCollectionProductoEntity = em.merge(productoEntityCollectionProductoEntity);
            }
            em.remove(maquinaEntity);
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

    public List<MaquinaEntity> findMaquinaEntityEntities() {
        return findMaquinaEntityEntities(true, -1, -1);
    }

    public List<MaquinaEntity> findMaquinaEntityEntities(int maxResults, int firstResult) {
        return findMaquinaEntityEntities(false, maxResults, firstResult);
    }

    private List<MaquinaEntity> findMaquinaEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MaquinaEntity.class));
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

    public MaquinaEntity findMaquinaEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MaquinaEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getMaquinaEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MaquinaEntity> rt = cq.from(MaquinaEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
