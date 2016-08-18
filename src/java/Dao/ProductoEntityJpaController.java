/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.exceptions.IllegalOrphanException;
import Dao.exceptions.NonexistentEntityException;
import Dao.exceptions.PreexistingEntityException;
import Dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.OrdenesproduccionEntity;
import java.util.ArrayList;
import java.util.Collection;
import Entity.MaquinaEntity;
import Entity.ProductoEntity;
import Entity.ProductomateriaprimaEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ponchis
 */
public class ProductoEntityJpaController implements Serializable {

    public ProductoEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductoEntity productoEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (productoEntity.getOrdenesproduccionEntityCollection() == null) {
            productoEntity.setOrdenesproduccionEntityCollection(new ArrayList<OrdenesproduccionEntity>());
        }
        if (productoEntity.getMaquinaEntityCollection() == null) {
            productoEntity.setMaquinaEntityCollection(new ArrayList<MaquinaEntity>());
        }
        if (productoEntity.getProductomateriaprimaEntityCollection() == null) {
            productoEntity.setProductomateriaprimaEntityCollection(new ArrayList<ProductomateriaprimaEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<OrdenesproduccionEntity> attachedOrdenesproduccionEntityCollection = new ArrayList<OrdenesproduccionEntity>();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach : productoEntity.getOrdenesproduccionEntityCollection()) {
                ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach = em.getReference(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getClass(), ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getIdOrdenProduccion());
                attachedOrdenesproduccionEntityCollection.add(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach);
            }
            productoEntity.setOrdenesproduccionEntityCollection(attachedOrdenesproduccionEntityCollection);
            Collection<MaquinaEntity> attachedMaquinaEntityCollection = new ArrayList<MaquinaEntity>();
            for (MaquinaEntity maquinaEntityCollectionMaquinaEntityToAttach : productoEntity.getMaquinaEntityCollection()) {
                maquinaEntityCollectionMaquinaEntityToAttach = em.getReference(maquinaEntityCollectionMaquinaEntityToAttach.getClass(), maquinaEntityCollectionMaquinaEntityToAttach.getIdMaquina());
                attachedMaquinaEntityCollection.add(maquinaEntityCollectionMaquinaEntityToAttach);
            }
            productoEntity.setMaquinaEntityCollection(attachedMaquinaEntityCollection);
            Collection<ProductomateriaprimaEntity> attachedProductomateriaprimaEntityCollection = new ArrayList<ProductomateriaprimaEntity>();
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach : productoEntity.getProductomateriaprimaEntityCollection()) {
                productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach = em.getReference(productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach.getClass(), productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach.getProductomateriaprimaEntityPK());
                attachedProductomateriaprimaEntityCollection.add(productomateriaprimaEntityCollectionProductomateriaprimaEntityToAttach);
            }
            productoEntity.setProductomateriaprimaEntityCollection(attachedProductomateriaprimaEntityCollection);
            em.persist(productoEntity);
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntity : productoEntity.getOrdenesproduccionEntityCollection()) {
                ordenesproduccionEntityCollectionOrdenesproduccionEntity.getProductoEntityCollection().add(productoEntity);
                ordenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
            }
            for (MaquinaEntity maquinaEntityCollectionMaquinaEntity : productoEntity.getMaquinaEntityCollection()) {
                maquinaEntityCollectionMaquinaEntity.getProductoEntityCollection().add(productoEntity);
                maquinaEntityCollectionMaquinaEntity = em.merge(maquinaEntityCollectionMaquinaEntity);
            }
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionProductomateriaprimaEntity : productoEntity.getProductomateriaprimaEntityCollection()) {
                ProductoEntity oldProductoEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity = productomateriaprimaEntityCollectionProductomateriaprimaEntity.getProductoEntity();
                productomateriaprimaEntityCollectionProductomateriaprimaEntity.setProductoEntity(productoEntity);
                productomateriaprimaEntityCollectionProductomateriaprimaEntity = em.merge(productomateriaprimaEntityCollectionProductomateriaprimaEntity);
                if (oldProductoEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity != null) {
                    oldProductoEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity.getProductomateriaprimaEntityCollection().remove(productomateriaprimaEntityCollectionProductomateriaprimaEntity);
                    oldProductoEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity = em.merge(oldProductoEntityOfProductomateriaprimaEntityCollectionProductomateriaprimaEntity);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProductoEntity(productoEntity.getIdProducto()) != null) {
                throw new PreexistingEntityException("ProductoEntity " + productoEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductoEntity productoEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProductoEntity persistentProductoEntity = em.find(ProductoEntity.class, productoEntity.getIdProducto());
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOld = persistentProductoEntity.getOrdenesproduccionEntityCollection();
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionNew = productoEntity.getOrdenesproduccionEntityCollection();
            Collection<MaquinaEntity> maquinaEntityCollectionOld = persistentProductoEntity.getMaquinaEntityCollection();
            Collection<MaquinaEntity> maquinaEntityCollectionNew = productoEntity.getMaquinaEntityCollection();
            Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollectionOld = persistentProductoEntity.getProductomateriaprimaEntityCollection();
            Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollectionNew = productoEntity.getProductomateriaprimaEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionOldProductomateriaprimaEntity : productomateriaprimaEntityCollectionOld) {
                if (!productomateriaprimaEntityCollectionNew.contains(productomateriaprimaEntityCollectionOldProductomateriaprimaEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductomateriaprimaEntity " + productomateriaprimaEntityCollectionOldProductomateriaprimaEntity + " since its productoEntity field is not nullable.");
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
            productoEntity.setOrdenesproduccionEntityCollection(ordenesproduccionEntityCollectionNew);
            Collection<MaquinaEntity> attachedMaquinaEntityCollectionNew = new ArrayList<MaquinaEntity>();
            for (MaquinaEntity maquinaEntityCollectionNewMaquinaEntityToAttach : maquinaEntityCollectionNew) {
                maquinaEntityCollectionNewMaquinaEntityToAttach = em.getReference(maquinaEntityCollectionNewMaquinaEntityToAttach.getClass(), maquinaEntityCollectionNewMaquinaEntityToAttach.getIdMaquina());
                attachedMaquinaEntityCollectionNew.add(maquinaEntityCollectionNewMaquinaEntityToAttach);
            }
            maquinaEntityCollectionNew = attachedMaquinaEntityCollectionNew;
            productoEntity.setMaquinaEntityCollection(maquinaEntityCollectionNew);
            Collection<ProductomateriaprimaEntity> attachedProductomateriaprimaEntityCollectionNew = new ArrayList<ProductomateriaprimaEntity>();
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach : productomateriaprimaEntityCollectionNew) {
                productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach = em.getReference(productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach.getClass(), productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach.getProductomateriaprimaEntityPK());
                attachedProductomateriaprimaEntityCollectionNew.add(productomateriaprimaEntityCollectionNewProductomateriaprimaEntityToAttach);
            }
            productomateriaprimaEntityCollectionNew = attachedProductomateriaprimaEntityCollectionNew;
            productoEntity.setProductomateriaprimaEntityCollection(productomateriaprimaEntityCollectionNew);
            productoEntity = em.merge(productoEntity);
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOldOrdenesproduccionEntity : ordenesproduccionEntityCollectionOld) {
                if (!ordenesproduccionEntityCollectionNew.contains(ordenesproduccionEntityCollectionOldOrdenesproduccionEntity)) {
                    ordenesproduccionEntityCollectionOldOrdenesproduccionEntity.getProductoEntityCollection().remove(productoEntity);
                    ordenesproduccionEntityCollectionOldOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionOldOrdenesproduccionEntity);
                }
            }
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionNewOrdenesproduccionEntity : ordenesproduccionEntityCollectionNew) {
                if (!ordenesproduccionEntityCollectionOld.contains(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity)) {
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.getProductoEntityCollection().add(productoEntity);
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                }
            }
            for (MaquinaEntity maquinaEntityCollectionOldMaquinaEntity : maquinaEntityCollectionOld) {
                if (!maquinaEntityCollectionNew.contains(maquinaEntityCollectionOldMaquinaEntity)) {
                    maquinaEntityCollectionOldMaquinaEntity.getProductoEntityCollection().remove(productoEntity);
                    maquinaEntityCollectionOldMaquinaEntity = em.merge(maquinaEntityCollectionOldMaquinaEntity);
                }
            }
            for (MaquinaEntity maquinaEntityCollectionNewMaquinaEntity : maquinaEntityCollectionNew) {
                if (!maquinaEntityCollectionOld.contains(maquinaEntityCollectionNewMaquinaEntity)) {
                    maquinaEntityCollectionNewMaquinaEntity.getProductoEntityCollection().add(productoEntity);
                    maquinaEntityCollectionNewMaquinaEntity = em.merge(maquinaEntityCollectionNewMaquinaEntity);
                }
            }
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionNewProductomateriaprimaEntity : productomateriaprimaEntityCollectionNew) {
                if (!productomateriaprimaEntityCollectionOld.contains(productomateriaprimaEntityCollectionNewProductomateriaprimaEntity)) {
                    ProductoEntity oldProductoEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity = productomateriaprimaEntityCollectionNewProductomateriaprimaEntity.getProductoEntity();
                    productomateriaprimaEntityCollectionNewProductomateriaprimaEntity.setProductoEntity(productoEntity);
                    productomateriaprimaEntityCollectionNewProductomateriaprimaEntity = em.merge(productomateriaprimaEntityCollectionNewProductomateriaprimaEntity);
                    if (oldProductoEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity != null && !oldProductoEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity.equals(productoEntity)) {
                        oldProductoEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity.getProductomateriaprimaEntityCollection().remove(productomateriaprimaEntityCollectionNewProductomateriaprimaEntity);
                        oldProductoEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity = em.merge(oldProductoEntityOfProductomateriaprimaEntityCollectionNewProductomateriaprimaEntity);
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
                Integer id = productoEntity.getIdProducto();
                if (findProductoEntity(id) == null) {
                    throw new NonexistentEntityException("The productoEntity with id " + id + " no longer exists.");
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
            ProductoEntity productoEntity;
            try {
                productoEntity = em.getReference(ProductoEntity.class, id);
                productoEntity.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollectionOrphanCheck = productoEntity.getProductomateriaprimaEntityCollection();
            for (ProductomateriaprimaEntity productomateriaprimaEntityCollectionOrphanCheckProductomateriaprimaEntity : productomateriaprimaEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProductoEntity (" + productoEntity + ") cannot be destroyed since the ProductomateriaprimaEntity " + productomateriaprimaEntityCollectionOrphanCheckProductomateriaprimaEntity + " in its productomateriaprimaEntityCollection field has a non-nullable productoEntity field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection = productoEntity.getOrdenesproduccionEntityCollection();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntity : ordenesproduccionEntityCollection) {
                ordenesproduccionEntityCollectionOrdenesproduccionEntity.getProductoEntityCollection().remove(productoEntity);
                ordenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
            }
            Collection<MaquinaEntity> maquinaEntityCollection = productoEntity.getMaquinaEntityCollection();
            for (MaquinaEntity maquinaEntityCollectionMaquinaEntity : maquinaEntityCollection) {
                maquinaEntityCollectionMaquinaEntity.getProductoEntityCollection().remove(productoEntity);
                maquinaEntityCollectionMaquinaEntity = em.merge(maquinaEntityCollectionMaquinaEntity);
            }
            em.remove(productoEntity);
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

    public List<ProductoEntity> findProductoEntityEntities() {
        return findProductoEntityEntities(true, -1, -1);
    }

    public List<ProductoEntity> findProductoEntityEntities(int maxResults, int firstResult) {
        return findProductoEntityEntities(false, maxResults, firstResult);
    }

    private List<ProductoEntity> findProductoEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductoEntity.class));
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

    public ProductoEntity findProductoEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductoEntity> rt = cq.from(ProductoEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
