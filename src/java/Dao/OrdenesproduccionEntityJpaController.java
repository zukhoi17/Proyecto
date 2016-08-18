/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.exceptions.NonexistentEntityException;
import Dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.DefectuosoEntity;
import Entity.ResiduoEntity;
import Entity.UsuarioEntity;
import Entity.MaquinaEntity;
import Entity.OrdenesproduccionEntity;
import Entity.ProductoEntity;
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
public class OrdenesproduccionEntityJpaController implements Serializable {

    public OrdenesproduccionEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OrdenesproduccionEntity ordenesproduccionEntity) throws RollbackFailureException, Exception {
        if (ordenesproduccionEntity.getProductoEntityCollection() == null) {
            ordenesproduccionEntity.setProductoEntityCollection(new ArrayList<ProductoEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DefectuosoEntity defectuosos = ordenesproduccionEntity.getDefectuosos();
            if (defectuosos != null) {
                defectuosos = em.getReference(defectuosos.getClass(), defectuosos.getIdDefectuoso());
                ordenesproduccionEntity.setDefectuosos(defectuosos);
            }
            ResiduoEntity residuo = ordenesproduccionEntity.getResiduo();
            if (residuo != null) {
                residuo = em.getReference(residuo.getClass(), residuo.getIdResiduo());
                ordenesproduccionEntity.setResiduo(residuo);
            }
            UsuarioEntity usuarioProduccion = ordenesproduccionEntity.getUsuarioProduccion();
            if (usuarioProduccion != null) {
                usuarioProduccion = em.getReference(usuarioProduccion.getClass(), usuarioProduccion.getNroDoc());
                ordenesproduccionEntity.setUsuarioProduccion(usuarioProduccion);
            }
            MaquinaEntity maquinasidMaquina = ordenesproduccionEntity.getMaquinasidMaquina();
            if (maquinasidMaquina != null) {
                maquinasidMaquina = em.getReference(maquinasidMaquina.getClass(), maquinasidMaquina.getIdMaquina());
                ordenesproduccionEntity.setMaquinasidMaquina(maquinasidMaquina);
            }
            Collection<ProductoEntity> attachedProductoEntityCollection = new ArrayList<ProductoEntity>();
            for (ProductoEntity productoEntityCollectionProductoEntityToAttach : ordenesproduccionEntity.getProductoEntityCollection()) {
                productoEntityCollectionProductoEntityToAttach = em.getReference(productoEntityCollectionProductoEntityToAttach.getClass(), productoEntityCollectionProductoEntityToAttach.getIdProducto());
                attachedProductoEntityCollection.add(productoEntityCollectionProductoEntityToAttach);
            }
            ordenesproduccionEntity.setProductoEntityCollection(attachedProductoEntityCollection);
            em.persist(ordenesproduccionEntity);
            if (defectuosos != null) {
                defectuosos.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                defectuosos = em.merge(defectuosos);
            }
            if (residuo != null) {
                residuo.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                residuo = em.merge(residuo);
            }
            if (usuarioProduccion != null) {
                usuarioProduccion.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                usuarioProduccion = em.merge(usuarioProduccion);
            }
            if (maquinasidMaquina != null) {
                maquinasidMaquina.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                maquinasidMaquina = em.merge(maquinasidMaquina);
            }
            for (ProductoEntity productoEntityCollectionProductoEntity : ordenesproduccionEntity.getProductoEntityCollection()) {
                productoEntityCollectionProductoEntity.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                productoEntityCollectionProductoEntity = em.merge(productoEntityCollectionProductoEntity);
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

    public void edit(OrdenesproduccionEntity ordenesproduccionEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            OrdenesproduccionEntity persistentOrdenesproduccionEntity = em.find(OrdenesproduccionEntity.class, ordenesproduccionEntity.getIdOrdenProduccion());
            DefectuosoEntity defectuososOld = persistentOrdenesproduccionEntity.getDefectuosos();
            DefectuosoEntity defectuososNew = ordenesproduccionEntity.getDefectuosos();
            ResiduoEntity residuoOld = persistentOrdenesproduccionEntity.getResiduo();
            ResiduoEntity residuoNew = ordenesproduccionEntity.getResiduo();
            UsuarioEntity usuarioProduccionOld = persistentOrdenesproduccionEntity.getUsuarioProduccion();
            UsuarioEntity usuarioProduccionNew = ordenesproduccionEntity.getUsuarioProduccion();
            MaquinaEntity maquinasidMaquinaOld = persistentOrdenesproduccionEntity.getMaquinasidMaquina();
            MaquinaEntity maquinasidMaquinaNew = ordenesproduccionEntity.getMaquinasidMaquina();
            Collection<ProductoEntity> productoEntityCollectionOld = persistentOrdenesproduccionEntity.getProductoEntityCollection();
            Collection<ProductoEntity> productoEntityCollectionNew = ordenesproduccionEntity.getProductoEntityCollection();
            if (defectuososNew != null) {
                defectuososNew = em.getReference(defectuososNew.getClass(), defectuososNew.getIdDefectuoso());
                ordenesproduccionEntity.setDefectuosos(defectuososNew);
            }
            if (residuoNew != null) {
                residuoNew = em.getReference(residuoNew.getClass(), residuoNew.getIdResiduo());
                ordenesproduccionEntity.setResiduo(residuoNew);
            }
            if (usuarioProduccionNew != null) {
                usuarioProduccionNew = em.getReference(usuarioProduccionNew.getClass(), usuarioProduccionNew.getNroDoc());
                ordenesproduccionEntity.setUsuarioProduccion(usuarioProduccionNew);
            }
            if (maquinasidMaquinaNew != null) {
                maquinasidMaquinaNew = em.getReference(maquinasidMaquinaNew.getClass(), maquinasidMaquinaNew.getIdMaquina());
                ordenesproduccionEntity.setMaquinasidMaquina(maquinasidMaquinaNew);
            }
            Collection<ProductoEntity> attachedProductoEntityCollectionNew = new ArrayList<ProductoEntity>();
            for (ProductoEntity productoEntityCollectionNewProductoEntityToAttach : productoEntityCollectionNew) {
                productoEntityCollectionNewProductoEntityToAttach = em.getReference(productoEntityCollectionNewProductoEntityToAttach.getClass(), productoEntityCollectionNewProductoEntityToAttach.getIdProducto());
                attachedProductoEntityCollectionNew.add(productoEntityCollectionNewProductoEntityToAttach);
            }
            productoEntityCollectionNew = attachedProductoEntityCollectionNew;
            ordenesproduccionEntity.setProductoEntityCollection(productoEntityCollectionNew);
            ordenesproduccionEntity = em.merge(ordenesproduccionEntity);
            if (defectuososOld != null && !defectuososOld.equals(defectuososNew)) {
                defectuososOld.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                defectuososOld = em.merge(defectuososOld);
            }
            if (defectuososNew != null && !defectuososNew.equals(defectuososOld)) {
                defectuososNew.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                defectuososNew = em.merge(defectuososNew);
            }
            if (residuoOld != null && !residuoOld.equals(residuoNew)) {
                residuoOld.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                residuoOld = em.merge(residuoOld);
            }
            if (residuoNew != null && !residuoNew.equals(residuoOld)) {
                residuoNew.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                residuoNew = em.merge(residuoNew);
            }
            if (usuarioProduccionOld != null && !usuarioProduccionOld.equals(usuarioProduccionNew)) {
                usuarioProduccionOld.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                usuarioProduccionOld = em.merge(usuarioProduccionOld);
            }
            if (usuarioProduccionNew != null && !usuarioProduccionNew.equals(usuarioProduccionOld)) {
                usuarioProduccionNew.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                usuarioProduccionNew = em.merge(usuarioProduccionNew);
            }
            if (maquinasidMaquinaOld != null && !maquinasidMaquinaOld.equals(maquinasidMaquinaNew)) {
                maquinasidMaquinaOld.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                maquinasidMaquinaOld = em.merge(maquinasidMaquinaOld);
            }
            if (maquinasidMaquinaNew != null && !maquinasidMaquinaNew.equals(maquinasidMaquinaOld)) {
                maquinasidMaquinaNew.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                maquinasidMaquinaNew = em.merge(maquinasidMaquinaNew);
            }
            for (ProductoEntity productoEntityCollectionOldProductoEntity : productoEntityCollectionOld) {
                if (!productoEntityCollectionNew.contains(productoEntityCollectionOldProductoEntity)) {
                    productoEntityCollectionOldProductoEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                    productoEntityCollectionOldProductoEntity = em.merge(productoEntityCollectionOldProductoEntity);
                }
            }
            for (ProductoEntity productoEntityCollectionNewProductoEntity : productoEntityCollectionNew) {
                if (!productoEntityCollectionOld.contains(productoEntityCollectionNewProductoEntity)) {
                    productoEntityCollectionNewProductoEntity.getOrdenesproduccionEntityCollection().add(ordenesproduccionEntity);
                    productoEntityCollectionNewProductoEntity = em.merge(productoEntityCollectionNewProductoEntity);
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
                Integer id = ordenesproduccionEntity.getIdOrdenProduccion();
                if (findOrdenesproduccionEntity(id) == null) {
                    throw new NonexistentEntityException("The ordenesproduccionEntity with id " + id + " no longer exists.");
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
            OrdenesproduccionEntity ordenesproduccionEntity;
            try {
                ordenesproduccionEntity = em.getReference(OrdenesproduccionEntity.class, id);
                ordenesproduccionEntity.getIdOrdenProduccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordenesproduccionEntity with id " + id + " no longer exists.", enfe);
            }
            DefectuosoEntity defectuosos = ordenesproduccionEntity.getDefectuosos();
            if (defectuosos != null) {
                defectuosos.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                defectuosos = em.merge(defectuosos);
            }
            ResiduoEntity residuo = ordenesproduccionEntity.getResiduo();
            if (residuo != null) {
                residuo.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                residuo = em.merge(residuo);
            }
            UsuarioEntity usuarioProduccion = ordenesproduccionEntity.getUsuarioProduccion();
            if (usuarioProduccion != null) {
                usuarioProduccion.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                usuarioProduccion = em.merge(usuarioProduccion);
            }
            MaquinaEntity maquinasidMaquina = ordenesproduccionEntity.getMaquinasidMaquina();
            if (maquinasidMaquina != null) {
                maquinasidMaquina.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                maquinasidMaquina = em.merge(maquinasidMaquina);
            }
            Collection<ProductoEntity> productoEntityCollection = ordenesproduccionEntity.getProductoEntityCollection();
            for (ProductoEntity productoEntityCollectionProductoEntity : productoEntityCollection) {
                productoEntityCollectionProductoEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntity);
                productoEntityCollectionProductoEntity = em.merge(productoEntityCollectionProductoEntity);
            }
            em.remove(ordenesproduccionEntity);
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

    public List<OrdenesproduccionEntity> findOrdenesproduccionEntityEntities() {
        return findOrdenesproduccionEntityEntities(true, -1, -1);
    }

    public List<OrdenesproduccionEntity> findOrdenesproduccionEntityEntities(int maxResults, int firstResult) {
        return findOrdenesproduccionEntityEntities(false, maxResults, firstResult);
    }

    private List<OrdenesproduccionEntity> findOrdenesproduccionEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrdenesproduccionEntity.class));
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

    public OrdenesproduccionEntity findOrdenesproduccionEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrdenesproduccionEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenesproduccionEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrdenesproduccionEntity> rt = cq.from(OrdenesproduccionEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
