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
import Entity.TipodocEntity;
import Entity.RolEntity;
import java.util.ArrayList;
import java.util.Collection;
import Entity.OrdenesproduccionEntity;
import Entity.UsuarioEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ponchis
 */
public class UsuarioEntityJpaController implements Serializable {

    public UsuarioEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioEntity usuarioEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuarioEntity.getRolEntityCollection() == null) {
            usuarioEntity.setRolEntityCollection(new ArrayList<RolEntity>());
        }
        if (usuarioEntity.getOrdenesproduccionEntityCollection() == null) {
            usuarioEntity.setOrdenesproduccionEntityCollection(new ArrayList<OrdenesproduccionEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipodocEntity idTipoDoc = usuarioEntity.getIdTipoDoc();
            if (idTipoDoc != null) {
                idTipoDoc = em.getReference(idTipoDoc.getClass(), idTipoDoc.getIdTipoDoc());
                usuarioEntity.setIdTipoDoc(idTipoDoc);
            }
            Collection<RolEntity> attachedRolEntityCollection = new ArrayList<RolEntity>();
            for (RolEntity rolEntityCollectionRolEntityToAttach : usuarioEntity.getRolEntityCollection()) {
                rolEntityCollectionRolEntityToAttach = em.getReference(rolEntityCollectionRolEntityToAttach.getClass(), rolEntityCollectionRolEntityToAttach.getIdRol());
                attachedRolEntityCollection.add(rolEntityCollectionRolEntityToAttach);
            }
            usuarioEntity.setRolEntityCollection(attachedRolEntityCollection);
            Collection<OrdenesproduccionEntity> attachedOrdenesproduccionEntityCollection = new ArrayList<OrdenesproduccionEntity>();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach : usuarioEntity.getOrdenesproduccionEntityCollection()) {
                ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach = em.getReference(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getClass(), ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach.getIdOrdenProduccion());
                attachedOrdenesproduccionEntityCollection.add(ordenesproduccionEntityCollectionOrdenesproduccionEntityToAttach);
            }
            usuarioEntity.setOrdenesproduccionEntityCollection(attachedOrdenesproduccionEntityCollection);
            em.persist(usuarioEntity);
            if (idTipoDoc != null) {
                idTipoDoc.getUsuarioEntityCollection().add(usuarioEntity);
                idTipoDoc = em.merge(idTipoDoc);
            }
            for (RolEntity rolEntityCollectionRolEntity : usuarioEntity.getRolEntityCollection()) {
                rolEntityCollectionRolEntity.getUsuarioEntityCollection().add(usuarioEntity);
                rolEntityCollectionRolEntity = em.merge(rolEntityCollectionRolEntity);
            }
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrdenesproduccionEntity : usuarioEntity.getOrdenesproduccionEntityCollection()) {
                UsuarioEntity oldUsuarioProduccionOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity = ordenesproduccionEntityCollectionOrdenesproduccionEntity.getUsuarioProduccion();
                ordenesproduccionEntityCollectionOrdenesproduccionEntity.setUsuarioProduccion(usuarioEntity);
                ordenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
                if (oldUsuarioProduccionOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity != null) {
                    oldUsuarioProduccionOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntityCollectionOrdenesproduccionEntity);
                    oldUsuarioProduccionOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity = em.merge(oldUsuarioProduccionOfOrdenesproduccionEntityCollectionOrdenesproduccionEntity);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuarioEntity(usuarioEntity.getNroDoc()) != null) {
                throw new PreexistingEntityException("UsuarioEntity " + usuarioEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioEntity usuarioEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UsuarioEntity persistentUsuarioEntity = em.find(UsuarioEntity.class, usuarioEntity.getNroDoc());
            TipodocEntity idTipoDocOld = persistentUsuarioEntity.getIdTipoDoc();
            TipodocEntity idTipoDocNew = usuarioEntity.getIdTipoDoc();
            Collection<RolEntity> rolEntityCollectionOld = persistentUsuarioEntity.getRolEntityCollection();
            Collection<RolEntity> rolEntityCollectionNew = usuarioEntity.getRolEntityCollection();
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOld = persistentUsuarioEntity.getOrdenesproduccionEntityCollection();
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionNew = usuarioEntity.getOrdenesproduccionEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOldOrdenesproduccionEntity : ordenesproduccionEntityCollectionOld) {
                if (!ordenesproduccionEntityCollectionNew.contains(ordenesproduccionEntityCollectionOldOrdenesproduccionEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdenesproduccionEntity " + ordenesproduccionEntityCollectionOldOrdenesproduccionEntity + " since its usuarioProduccion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTipoDocNew != null) {
                idTipoDocNew = em.getReference(idTipoDocNew.getClass(), idTipoDocNew.getIdTipoDoc());
                usuarioEntity.setIdTipoDoc(idTipoDocNew);
            }
            Collection<RolEntity> attachedRolEntityCollectionNew = new ArrayList<RolEntity>();
            for (RolEntity rolEntityCollectionNewRolEntityToAttach : rolEntityCollectionNew) {
                rolEntityCollectionNewRolEntityToAttach = em.getReference(rolEntityCollectionNewRolEntityToAttach.getClass(), rolEntityCollectionNewRolEntityToAttach.getIdRol());
                attachedRolEntityCollectionNew.add(rolEntityCollectionNewRolEntityToAttach);
            }
            rolEntityCollectionNew = attachedRolEntityCollectionNew;
            usuarioEntity.setRolEntityCollection(rolEntityCollectionNew);
            Collection<OrdenesproduccionEntity> attachedOrdenesproduccionEntityCollectionNew = new ArrayList<OrdenesproduccionEntity>();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach : ordenesproduccionEntityCollectionNew) {
                ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach = em.getReference(ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach.getClass(), ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach.getIdOrdenProduccion());
                attachedOrdenesproduccionEntityCollectionNew.add(ordenesproduccionEntityCollectionNewOrdenesproduccionEntityToAttach);
            }
            ordenesproduccionEntityCollectionNew = attachedOrdenesproduccionEntityCollectionNew;
            usuarioEntity.setOrdenesproduccionEntityCollection(ordenesproduccionEntityCollectionNew);
            usuarioEntity = em.merge(usuarioEntity);
            if (idTipoDocOld != null && !idTipoDocOld.equals(idTipoDocNew)) {
                idTipoDocOld.getUsuarioEntityCollection().remove(usuarioEntity);
                idTipoDocOld = em.merge(idTipoDocOld);
            }
            if (idTipoDocNew != null && !idTipoDocNew.equals(idTipoDocOld)) {
                idTipoDocNew.getUsuarioEntityCollection().add(usuarioEntity);
                idTipoDocNew = em.merge(idTipoDocNew);
            }
            for (RolEntity rolEntityCollectionOldRolEntity : rolEntityCollectionOld) {
                if (!rolEntityCollectionNew.contains(rolEntityCollectionOldRolEntity)) {
                    rolEntityCollectionOldRolEntity.getUsuarioEntityCollection().remove(usuarioEntity);
                    rolEntityCollectionOldRolEntity = em.merge(rolEntityCollectionOldRolEntity);
                }
            }
            for (RolEntity rolEntityCollectionNewRolEntity : rolEntityCollectionNew) {
                if (!rolEntityCollectionOld.contains(rolEntityCollectionNewRolEntity)) {
                    rolEntityCollectionNewRolEntity.getUsuarioEntityCollection().add(usuarioEntity);
                    rolEntityCollectionNewRolEntity = em.merge(rolEntityCollectionNewRolEntity);
                }
            }
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionNewOrdenesproduccionEntity : ordenesproduccionEntityCollectionNew) {
                if (!ordenesproduccionEntityCollectionOld.contains(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity)) {
                    UsuarioEntity oldUsuarioProduccionOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity = ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.getUsuarioProduccion();
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity.setUsuarioProduccion(usuarioEntity);
                    ordenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                    if (oldUsuarioProduccionOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity != null && !oldUsuarioProduccionOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity.equals(usuarioEntity)) {
                        oldUsuarioProduccionOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity.getOrdenesproduccionEntityCollection().remove(ordenesproduccionEntityCollectionNewOrdenesproduccionEntity);
                        oldUsuarioProduccionOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity = em.merge(oldUsuarioProduccionOfOrdenesproduccionEntityCollectionNewOrdenesproduccionEntity);
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
                String id = usuarioEntity.getNroDoc();
                if (findUsuarioEntity(id) == null) {
                    throw new NonexistentEntityException("The usuarioEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UsuarioEntity usuarioEntity;
            try {
                usuarioEntity = em.getReference(UsuarioEntity.class, id);
                usuarioEntity.getNroDoc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollectionOrphanCheck = usuarioEntity.getOrdenesproduccionEntityCollection();
            for (OrdenesproduccionEntity ordenesproduccionEntityCollectionOrphanCheckOrdenesproduccionEntity : ordenesproduccionEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsuarioEntity (" + usuarioEntity + ") cannot be destroyed since the OrdenesproduccionEntity " + ordenesproduccionEntityCollectionOrphanCheckOrdenesproduccionEntity + " in its ordenesproduccionEntityCollection field has a non-nullable usuarioProduccion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipodocEntity idTipoDoc = usuarioEntity.getIdTipoDoc();
            if (idTipoDoc != null) {
                idTipoDoc.getUsuarioEntityCollection().remove(usuarioEntity);
                idTipoDoc = em.merge(idTipoDoc);
            }
            Collection<RolEntity> rolEntityCollection = usuarioEntity.getRolEntityCollection();
            for (RolEntity rolEntityCollectionRolEntity : rolEntityCollection) {
                rolEntityCollectionRolEntity.getUsuarioEntityCollection().remove(usuarioEntity);
                rolEntityCollectionRolEntity = em.merge(rolEntityCollectionRolEntity);
            }
            em.remove(usuarioEntity);
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

    public List<UsuarioEntity> findUsuarioEntityEntities() {
        return findUsuarioEntityEntities(true, -1, -1);
    }

    public List<UsuarioEntity> findUsuarioEntityEntities(int maxResults, int firstResult) {
        return findUsuarioEntityEntities(false, maxResults, firstResult);
    }

    private List<UsuarioEntity> findUsuarioEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioEntity.class));
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

    public UsuarioEntity findUsuarioEntity(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioEntity> rt = cq.from(UsuarioEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
