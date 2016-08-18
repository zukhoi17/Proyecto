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
import Entity.PermisoEntity;
import Entity.RolEntity;
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
public class PermisoEntityJpaController implements Serializable {

    public PermisoEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PermisoEntity permisoEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (permisoEntity.getRolEntityCollection() == null) {
            permisoEntity.setRolEntityCollection(new ArrayList<RolEntity>());
        }
        if (permisoEntity.getPermisoEntityCollection() == null) {
            permisoEntity.setPermisoEntityCollection(new ArrayList<PermisoEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PermisoEntity permisosidMod = permisoEntity.getPermisosidMod();
            if (permisosidMod != null) {
                permisosidMod = em.getReference(permisosidMod.getClass(), permisosidMod.getIdMod());
                permisoEntity.setPermisosidMod(permisosidMod);
            }
            Collection<RolEntity> attachedRolEntityCollection = new ArrayList<RolEntity>();
            for (RolEntity rolEntityCollectionRolEntityToAttach : permisoEntity.getRolEntityCollection()) {
                rolEntityCollectionRolEntityToAttach = em.getReference(rolEntityCollectionRolEntityToAttach.getClass(), rolEntityCollectionRolEntityToAttach.getIdRol());
                attachedRolEntityCollection.add(rolEntityCollectionRolEntityToAttach);
            }
            permisoEntity.setRolEntityCollection(attachedRolEntityCollection);
            Collection<PermisoEntity> attachedPermisoEntityCollection = new ArrayList<PermisoEntity>();
            for (PermisoEntity permisoEntityCollectionPermisoEntityToAttach : permisoEntity.getPermisoEntityCollection()) {
                permisoEntityCollectionPermisoEntityToAttach = em.getReference(permisoEntityCollectionPermisoEntityToAttach.getClass(), permisoEntityCollectionPermisoEntityToAttach.getIdMod());
                attachedPermisoEntityCollection.add(permisoEntityCollectionPermisoEntityToAttach);
            }
            permisoEntity.setPermisoEntityCollection(attachedPermisoEntityCollection);
            em.persist(permisoEntity);
            if (permisosidMod != null) {
                permisosidMod.getPermisoEntityCollection().add(permisoEntity);
                permisosidMod = em.merge(permisosidMod);
            }
            for (RolEntity rolEntityCollectionRolEntity : permisoEntity.getRolEntityCollection()) {
                rolEntityCollectionRolEntity.getPermisoEntityCollection().add(permisoEntity);
                rolEntityCollectionRolEntity = em.merge(rolEntityCollectionRolEntity);
            }
            for (PermisoEntity permisoEntityCollectionPermisoEntity : permisoEntity.getPermisoEntityCollection()) {
                PermisoEntity oldPermisosidModOfPermisoEntityCollectionPermisoEntity = permisoEntityCollectionPermisoEntity.getPermisosidMod();
                permisoEntityCollectionPermisoEntity.setPermisosidMod(permisoEntity);
                permisoEntityCollectionPermisoEntity = em.merge(permisoEntityCollectionPermisoEntity);
                if (oldPermisosidModOfPermisoEntityCollectionPermisoEntity != null) {
                    oldPermisosidModOfPermisoEntityCollectionPermisoEntity.getPermisoEntityCollection().remove(permisoEntityCollectionPermisoEntity);
                    oldPermisosidModOfPermisoEntityCollectionPermisoEntity = em.merge(oldPermisosidModOfPermisoEntityCollectionPermisoEntity);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPermisoEntity(permisoEntity.getIdMod()) != null) {
                throw new PreexistingEntityException("PermisoEntity " + permisoEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PermisoEntity permisoEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PermisoEntity persistentPermisoEntity = em.find(PermisoEntity.class, permisoEntity.getIdMod());
            PermisoEntity permisosidModOld = persistentPermisoEntity.getPermisosidMod();
            PermisoEntity permisosidModNew = permisoEntity.getPermisosidMod();
            Collection<RolEntity> rolEntityCollectionOld = persistentPermisoEntity.getRolEntityCollection();
            Collection<RolEntity> rolEntityCollectionNew = permisoEntity.getRolEntityCollection();
            Collection<PermisoEntity> permisoEntityCollectionOld = persistentPermisoEntity.getPermisoEntityCollection();
            Collection<PermisoEntity> permisoEntityCollectionNew = permisoEntity.getPermisoEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (PermisoEntity permisoEntityCollectionOldPermisoEntity : permisoEntityCollectionOld) {
                if (!permisoEntityCollectionNew.contains(permisoEntityCollectionOldPermisoEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PermisoEntity " + permisoEntityCollectionOldPermisoEntity + " since its permisosidMod field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (permisosidModNew != null) {
                permisosidModNew = em.getReference(permisosidModNew.getClass(), permisosidModNew.getIdMod());
                permisoEntity.setPermisosidMod(permisosidModNew);
            }
            Collection<RolEntity> attachedRolEntityCollectionNew = new ArrayList<RolEntity>();
            for (RolEntity rolEntityCollectionNewRolEntityToAttach : rolEntityCollectionNew) {
                rolEntityCollectionNewRolEntityToAttach = em.getReference(rolEntityCollectionNewRolEntityToAttach.getClass(), rolEntityCollectionNewRolEntityToAttach.getIdRol());
                attachedRolEntityCollectionNew.add(rolEntityCollectionNewRolEntityToAttach);
            }
            rolEntityCollectionNew = attachedRolEntityCollectionNew;
            permisoEntity.setRolEntityCollection(rolEntityCollectionNew);
            Collection<PermisoEntity> attachedPermisoEntityCollectionNew = new ArrayList<PermisoEntity>();
            for (PermisoEntity permisoEntityCollectionNewPermisoEntityToAttach : permisoEntityCollectionNew) {
                permisoEntityCollectionNewPermisoEntityToAttach = em.getReference(permisoEntityCollectionNewPermisoEntityToAttach.getClass(), permisoEntityCollectionNewPermisoEntityToAttach.getIdMod());
                attachedPermisoEntityCollectionNew.add(permisoEntityCollectionNewPermisoEntityToAttach);
            }
            permisoEntityCollectionNew = attachedPermisoEntityCollectionNew;
            permisoEntity.setPermisoEntityCollection(permisoEntityCollectionNew);
            permisoEntity = em.merge(permisoEntity);
            if (permisosidModOld != null && !permisosidModOld.equals(permisosidModNew)) {
                permisosidModOld.getPermisoEntityCollection().remove(permisoEntity);
                permisosidModOld = em.merge(permisosidModOld);
            }
            if (permisosidModNew != null && !permisosidModNew.equals(permisosidModOld)) {
                permisosidModNew.getPermisoEntityCollection().add(permisoEntity);
                permisosidModNew = em.merge(permisosidModNew);
            }
            for (RolEntity rolEntityCollectionOldRolEntity : rolEntityCollectionOld) {
                if (!rolEntityCollectionNew.contains(rolEntityCollectionOldRolEntity)) {
                    rolEntityCollectionOldRolEntity.getPermisoEntityCollection().remove(permisoEntity);
                    rolEntityCollectionOldRolEntity = em.merge(rolEntityCollectionOldRolEntity);
                }
            }
            for (RolEntity rolEntityCollectionNewRolEntity : rolEntityCollectionNew) {
                if (!rolEntityCollectionOld.contains(rolEntityCollectionNewRolEntity)) {
                    rolEntityCollectionNewRolEntity.getPermisoEntityCollection().add(permisoEntity);
                    rolEntityCollectionNewRolEntity = em.merge(rolEntityCollectionNewRolEntity);
                }
            }
            for (PermisoEntity permisoEntityCollectionNewPermisoEntity : permisoEntityCollectionNew) {
                if (!permisoEntityCollectionOld.contains(permisoEntityCollectionNewPermisoEntity)) {
                    PermisoEntity oldPermisosidModOfPermisoEntityCollectionNewPermisoEntity = permisoEntityCollectionNewPermisoEntity.getPermisosidMod();
                    permisoEntityCollectionNewPermisoEntity.setPermisosidMod(permisoEntity);
                    permisoEntityCollectionNewPermisoEntity = em.merge(permisoEntityCollectionNewPermisoEntity);
                    if (oldPermisosidModOfPermisoEntityCollectionNewPermisoEntity != null && !oldPermisosidModOfPermisoEntityCollectionNewPermisoEntity.equals(permisoEntity)) {
                        oldPermisosidModOfPermisoEntityCollectionNewPermisoEntity.getPermisoEntityCollection().remove(permisoEntityCollectionNewPermisoEntity);
                        oldPermisosidModOfPermisoEntityCollectionNewPermisoEntity = em.merge(oldPermisosidModOfPermisoEntityCollectionNewPermisoEntity);
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
                Integer id = permisoEntity.getIdMod();
                if (findPermisoEntity(id) == null) {
                    throw new NonexistentEntityException("The permisoEntity with id " + id + " no longer exists.");
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
            PermisoEntity permisoEntity;
            try {
                permisoEntity = em.getReference(PermisoEntity.class, id);
                permisoEntity.getIdMod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisoEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PermisoEntity> permisoEntityCollectionOrphanCheck = permisoEntity.getPermisoEntityCollection();
            for (PermisoEntity permisoEntityCollectionOrphanCheckPermisoEntity : permisoEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PermisoEntity (" + permisoEntity + ") cannot be destroyed since the PermisoEntity " + permisoEntityCollectionOrphanCheckPermisoEntity + " in its permisoEntityCollection field has a non-nullable permisosidMod field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PermisoEntity permisosidMod = permisoEntity.getPermisosidMod();
            if (permisosidMod != null) {
                permisosidMod.getPermisoEntityCollection().remove(permisoEntity);
                permisosidMod = em.merge(permisosidMod);
            }
            Collection<RolEntity> rolEntityCollection = permisoEntity.getRolEntityCollection();
            for (RolEntity rolEntityCollectionRolEntity : rolEntityCollection) {
                rolEntityCollectionRolEntity.getPermisoEntityCollection().remove(permisoEntity);
                rolEntityCollectionRolEntity = em.merge(rolEntityCollectionRolEntity);
            }
            em.remove(permisoEntity);
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

    public List<PermisoEntity> findPermisoEntityEntities() {
        return findPermisoEntityEntities(true, -1, -1);
    }

    public List<PermisoEntity> findPermisoEntityEntities(int maxResults, int firstResult) {
        return findPermisoEntityEntities(false, maxResults, firstResult);
    }

    private List<PermisoEntity> findPermisoEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PermisoEntity.class));
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

    public PermisoEntity findPermisoEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PermisoEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PermisoEntity> rt = cq.from(PermisoEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
