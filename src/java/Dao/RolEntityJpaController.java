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
import Entity.PermisoEntity;
import Entity.RolEntity;
import java.util.ArrayList;
import java.util.Collection;
import Entity.UsuarioEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ponchis
 */
public class RolEntityJpaController implements Serializable {

    public RolEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RolEntity rolEntity) throws RollbackFailureException, Exception {
        if (rolEntity.getPermisoEntityCollection() == null) {
            rolEntity.setPermisoEntityCollection(new ArrayList<PermisoEntity>());
        }
        if (rolEntity.getUsuarioEntityCollection() == null) {
            rolEntity.setUsuarioEntityCollection(new ArrayList<UsuarioEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PermisoEntity> attachedPermisoEntityCollection = new ArrayList<PermisoEntity>();
            for (PermisoEntity permisoEntityCollectionPermisoEntityToAttach : rolEntity.getPermisoEntityCollection()) {
                permisoEntityCollectionPermisoEntityToAttach = em.getReference(permisoEntityCollectionPermisoEntityToAttach.getClass(), permisoEntityCollectionPermisoEntityToAttach.getIdMod());
                attachedPermisoEntityCollection.add(permisoEntityCollectionPermisoEntityToAttach);
            }
            rolEntity.setPermisoEntityCollection(attachedPermisoEntityCollection);
            Collection<UsuarioEntity> attachedUsuarioEntityCollection = new ArrayList<UsuarioEntity>();
            for (UsuarioEntity usuarioEntityCollectionUsuarioEntityToAttach : rolEntity.getUsuarioEntityCollection()) {
                usuarioEntityCollectionUsuarioEntityToAttach = em.getReference(usuarioEntityCollectionUsuarioEntityToAttach.getClass(), usuarioEntityCollectionUsuarioEntityToAttach.getNroDoc());
                attachedUsuarioEntityCollection.add(usuarioEntityCollectionUsuarioEntityToAttach);
            }
            rolEntity.setUsuarioEntityCollection(attachedUsuarioEntityCollection);
            em.persist(rolEntity);
            for (PermisoEntity permisoEntityCollectionPermisoEntity : rolEntity.getPermisoEntityCollection()) {
                permisoEntityCollectionPermisoEntity.getRolEntityCollection().add(rolEntity);
                permisoEntityCollectionPermisoEntity = em.merge(permisoEntityCollectionPermisoEntity);
            }
            for (UsuarioEntity usuarioEntityCollectionUsuarioEntity : rolEntity.getUsuarioEntityCollection()) {
                usuarioEntityCollectionUsuarioEntity.getRolEntityCollection().add(rolEntity);
                usuarioEntityCollectionUsuarioEntity = em.merge(usuarioEntityCollectionUsuarioEntity);
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

    public void edit(RolEntity rolEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RolEntity persistentRolEntity = em.find(RolEntity.class, rolEntity.getIdRol());
            Collection<PermisoEntity> permisoEntityCollectionOld = persistentRolEntity.getPermisoEntityCollection();
            Collection<PermisoEntity> permisoEntityCollectionNew = rolEntity.getPermisoEntityCollection();
            Collection<UsuarioEntity> usuarioEntityCollectionOld = persistentRolEntity.getUsuarioEntityCollection();
            Collection<UsuarioEntity> usuarioEntityCollectionNew = rolEntity.getUsuarioEntityCollection();
            Collection<PermisoEntity> attachedPermisoEntityCollectionNew = new ArrayList<PermisoEntity>();
            for (PermisoEntity permisoEntityCollectionNewPermisoEntityToAttach : permisoEntityCollectionNew) {
                permisoEntityCollectionNewPermisoEntityToAttach = em.getReference(permisoEntityCollectionNewPermisoEntityToAttach.getClass(), permisoEntityCollectionNewPermisoEntityToAttach.getIdMod());
                attachedPermisoEntityCollectionNew.add(permisoEntityCollectionNewPermisoEntityToAttach);
            }
            permisoEntityCollectionNew = attachedPermisoEntityCollectionNew;
            rolEntity.setPermisoEntityCollection(permisoEntityCollectionNew);
            Collection<UsuarioEntity> attachedUsuarioEntityCollectionNew = new ArrayList<UsuarioEntity>();
            for (UsuarioEntity usuarioEntityCollectionNewUsuarioEntityToAttach : usuarioEntityCollectionNew) {
                usuarioEntityCollectionNewUsuarioEntityToAttach = em.getReference(usuarioEntityCollectionNewUsuarioEntityToAttach.getClass(), usuarioEntityCollectionNewUsuarioEntityToAttach.getNroDoc());
                attachedUsuarioEntityCollectionNew.add(usuarioEntityCollectionNewUsuarioEntityToAttach);
            }
            usuarioEntityCollectionNew = attachedUsuarioEntityCollectionNew;
            rolEntity.setUsuarioEntityCollection(usuarioEntityCollectionNew);
            rolEntity = em.merge(rolEntity);
            for (PermisoEntity permisoEntityCollectionOldPermisoEntity : permisoEntityCollectionOld) {
                if (!permisoEntityCollectionNew.contains(permisoEntityCollectionOldPermisoEntity)) {
                    permisoEntityCollectionOldPermisoEntity.getRolEntityCollection().remove(rolEntity);
                    permisoEntityCollectionOldPermisoEntity = em.merge(permisoEntityCollectionOldPermisoEntity);
                }
            }
            for (PermisoEntity permisoEntityCollectionNewPermisoEntity : permisoEntityCollectionNew) {
                if (!permisoEntityCollectionOld.contains(permisoEntityCollectionNewPermisoEntity)) {
                    permisoEntityCollectionNewPermisoEntity.getRolEntityCollection().add(rolEntity);
                    permisoEntityCollectionNewPermisoEntity = em.merge(permisoEntityCollectionNewPermisoEntity);
                }
            }
            for (UsuarioEntity usuarioEntityCollectionOldUsuarioEntity : usuarioEntityCollectionOld) {
                if (!usuarioEntityCollectionNew.contains(usuarioEntityCollectionOldUsuarioEntity)) {
                    usuarioEntityCollectionOldUsuarioEntity.getRolEntityCollection().remove(rolEntity);
                    usuarioEntityCollectionOldUsuarioEntity = em.merge(usuarioEntityCollectionOldUsuarioEntity);
                }
            }
            for (UsuarioEntity usuarioEntityCollectionNewUsuarioEntity : usuarioEntityCollectionNew) {
                if (!usuarioEntityCollectionOld.contains(usuarioEntityCollectionNewUsuarioEntity)) {
                    usuarioEntityCollectionNewUsuarioEntity.getRolEntityCollection().add(rolEntity);
                    usuarioEntityCollectionNewUsuarioEntity = em.merge(usuarioEntityCollectionNewUsuarioEntity);
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
                Integer id = rolEntity.getIdRol();
                if (findRolEntity(id) == null) {
                    throw new NonexistentEntityException("The rolEntity with id " + id + " no longer exists.");
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
            RolEntity rolEntity;
            try {
                rolEntity = em.getReference(RolEntity.class, id);
                rolEntity.getIdRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rolEntity with id " + id + " no longer exists.", enfe);
            }
            Collection<PermisoEntity> permisoEntityCollection = rolEntity.getPermisoEntityCollection();
            for (PermisoEntity permisoEntityCollectionPermisoEntity : permisoEntityCollection) {
                permisoEntityCollectionPermisoEntity.getRolEntityCollection().remove(rolEntity);
                permisoEntityCollectionPermisoEntity = em.merge(permisoEntityCollectionPermisoEntity);
            }
            Collection<UsuarioEntity> usuarioEntityCollection = rolEntity.getUsuarioEntityCollection();
            for (UsuarioEntity usuarioEntityCollectionUsuarioEntity : usuarioEntityCollection) {
                usuarioEntityCollectionUsuarioEntity.getRolEntityCollection().remove(rolEntity);
                usuarioEntityCollectionUsuarioEntity = em.merge(usuarioEntityCollectionUsuarioEntity);
            }
            em.remove(rolEntity);
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

    public List<RolEntity> findRolEntityEntities() {
        return findRolEntityEntities(true, -1, -1);
    }

    public List<RolEntity> findRolEntityEntities(int maxResults, int firstResult) {
        return findRolEntityEntities(false, maxResults, firstResult);
    }

    private List<RolEntity> findRolEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RolEntity.class));
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

    public RolEntity findRolEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RolEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RolEntity> rt = cq.from(RolEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
