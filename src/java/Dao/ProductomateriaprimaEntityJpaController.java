/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.exceptions.NonexistentEntityException;
import Dao.exceptions.PreexistingEntityException;
import Dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.MateriaprimaEntity;
import Entity.ProductoEntity;
import Entity.ProductomateriaprimaEntity;
import Entity.ProductomateriaprimaEntityPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ponchis
 */
public class ProductomateriaprimaEntityJpaController implements Serializable {

    public ProductomateriaprimaEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductomateriaprimaEntity productomateriaprimaEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (productomateriaprimaEntity.getProductomateriaprimaEntityPK() == null) {
            productomateriaprimaEntity.setProductomateriaprimaEntityPK(new ProductomateriaprimaEntityPK());
        }
        productomateriaprimaEntity.getProductomateriaprimaEntityPK().setMateriaPrimaId(productomateriaprimaEntity.getMateriaprimaEntity().getIdMateriaPrima());
        productomateriaprimaEntity.getProductomateriaprimaEntityPK().setProductoId(productomateriaprimaEntity.getProductoEntity().getIdProducto());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MateriaprimaEntity materiaprimaEntity = productomateriaprimaEntity.getMateriaprimaEntity();
            if (materiaprimaEntity != null) {
                materiaprimaEntity = em.getReference(materiaprimaEntity.getClass(), materiaprimaEntity.getIdMateriaPrima());
                productomateriaprimaEntity.setMateriaprimaEntity(materiaprimaEntity);
            }
            ProductoEntity productoEntity = productomateriaprimaEntity.getProductoEntity();
            if (productoEntity != null) {
                productoEntity = em.getReference(productoEntity.getClass(), productoEntity.getIdProducto());
                productomateriaprimaEntity.setProductoEntity(productoEntity);
            }
            em.persist(productomateriaprimaEntity);
            if (materiaprimaEntity != null) {
                materiaprimaEntity.getProductomateriaprimaEntityCollection().add(productomateriaprimaEntity);
                materiaprimaEntity = em.merge(materiaprimaEntity);
            }
            if (productoEntity != null) {
                productoEntity.getProductomateriaprimaEntityCollection().add(productomateriaprimaEntity);
                productoEntity = em.merge(productoEntity);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProductomateriaprimaEntity(productomateriaprimaEntity.getProductomateriaprimaEntityPK()) != null) {
                throw new PreexistingEntityException("ProductomateriaprimaEntity " + productomateriaprimaEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductomateriaprimaEntity productomateriaprimaEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        productomateriaprimaEntity.getProductomateriaprimaEntityPK().setMateriaPrimaId(productomateriaprimaEntity.getMateriaprimaEntity().getIdMateriaPrima());
        productomateriaprimaEntity.getProductomateriaprimaEntityPK().setProductoId(productomateriaprimaEntity.getProductoEntity().getIdProducto());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProductomateriaprimaEntity persistentProductomateriaprimaEntity = em.find(ProductomateriaprimaEntity.class, productomateriaprimaEntity.getProductomateriaprimaEntityPK());
            MateriaprimaEntity materiaprimaEntityOld = persistentProductomateriaprimaEntity.getMateriaprimaEntity();
            MateriaprimaEntity materiaprimaEntityNew = productomateriaprimaEntity.getMateriaprimaEntity();
            ProductoEntity productoEntityOld = persistentProductomateriaprimaEntity.getProductoEntity();
            ProductoEntity productoEntityNew = productomateriaprimaEntity.getProductoEntity();
            if (materiaprimaEntityNew != null) {
                materiaprimaEntityNew = em.getReference(materiaprimaEntityNew.getClass(), materiaprimaEntityNew.getIdMateriaPrima());
                productomateriaprimaEntity.setMateriaprimaEntity(materiaprimaEntityNew);
            }
            if (productoEntityNew != null) {
                productoEntityNew = em.getReference(productoEntityNew.getClass(), productoEntityNew.getIdProducto());
                productomateriaprimaEntity.setProductoEntity(productoEntityNew);
            }
            productomateriaprimaEntity = em.merge(productomateriaprimaEntity);
            if (materiaprimaEntityOld != null && !materiaprimaEntityOld.equals(materiaprimaEntityNew)) {
                materiaprimaEntityOld.getProductomateriaprimaEntityCollection().remove(productomateriaprimaEntity);
                materiaprimaEntityOld = em.merge(materiaprimaEntityOld);
            }
            if (materiaprimaEntityNew != null && !materiaprimaEntityNew.equals(materiaprimaEntityOld)) {
                materiaprimaEntityNew.getProductomateriaprimaEntityCollection().add(productomateriaprimaEntity);
                materiaprimaEntityNew = em.merge(materiaprimaEntityNew);
            }
            if (productoEntityOld != null && !productoEntityOld.equals(productoEntityNew)) {
                productoEntityOld.getProductomateriaprimaEntityCollection().remove(productomateriaprimaEntity);
                productoEntityOld = em.merge(productoEntityOld);
            }
            if (productoEntityNew != null && !productoEntityNew.equals(productoEntityOld)) {
                productoEntityNew.getProductomateriaprimaEntityCollection().add(productomateriaprimaEntity);
                productoEntityNew = em.merge(productoEntityNew);
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
                ProductomateriaprimaEntityPK id = productomateriaprimaEntity.getProductomateriaprimaEntityPK();
                if (findProductomateriaprimaEntity(id) == null) {
                    throw new NonexistentEntityException("The productomateriaprimaEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductomateriaprimaEntityPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProductomateriaprimaEntity productomateriaprimaEntity;
            try {
                productomateriaprimaEntity = em.getReference(ProductomateriaprimaEntity.class, id);
                productomateriaprimaEntity.getProductomateriaprimaEntityPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productomateriaprimaEntity with id " + id + " no longer exists.", enfe);
            }
            MateriaprimaEntity materiaprimaEntity = productomateriaprimaEntity.getMateriaprimaEntity();
            if (materiaprimaEntity != null) {
                materiaprimaEntity.getProductomateriaprimaEntityCollection().remove(productomateriaprimaEntity);
                materiaprimaEntity = em.merge(materiaprimaEntity);
            }
            ProductoEntity productoEntity = productomateriaprimaEntity.getProductoEntity();
            if (productoEntity != null) {
                productoEntity.getProductomateriaprimaEntityCollection().remove(productomateriaprimaEntity);
                productoEntity = em.merge(productoEntity);
            }
            em.remove(productomateriaprimaEntity);
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

    public List<ProductomateriaprimaEntity> findProductomateriaprimaEntityEntities() {
        return findProductomateriaprimaEntityEntities(true, -1, -1);
    }

    public List<ProductomateriaprimaEntity> findProductomateriaprimaEntityEntities(int maxResults, int firstResult) {
        return findProductomateriaprimaEntityEntities(false, maxResults, firstResult);
    }

    private List<ProductomateriaprimaEntity> findProductomateriaprimaEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductomateriaprimaEntity.class));
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

    public ProductomateriaprimaEntity findProductomateriaprimaEntity(ProductomateriaprimaEntityPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductomateriaprimaEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductomateriaprimaEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductomateriaprimaEntity> rt = cq.from(ProductomateriaprimaEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
