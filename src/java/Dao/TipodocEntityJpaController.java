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
import Entity.TipodocEntity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.UsuarioEntity;
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
public class TipodocEntityJpaController implements Serializable {

    public TipodocEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipodocEntity tipodocEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipodocEntity.getUsuarioEntityCollection() == null) {
            tipodocEntity.setUsuarioEntityCollection(new ArrayList<UsuarioEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<UsuarioEntity> attachedUsuarioEntityCollection = new ArrayList<UsuarioEntity>();
            for (UsuarioEntity usuarioEntityCollectionUsuarioEntityToAttach : tipodocEntity.getUsuarioEntityCollection()) {
                usuarioEntityCollectionUsuarioEntityToAttach = em.getReference(usuarioEntityCollectionUsuarioEntityToAttach.getClass(), usuarioEntityCollectionUsuarioEntityToAttach.getNroDoc());
                attachedUsuarioEntityCollection.add(usuarioEntityCollectionUsuarioEntityToAttach);
            }
            tipodocEntity.setUsuarioEntityCollection(attachedUsuarioEntityCollection);
            em.persist(tipodocEntity);
            for (UsuarioEntity usuarioEntityCollectionUsuarioEntity : tipodocEntity.getUsuarioEntityCollection()) {
                TipodocEntity oldIdTipoDocOfUsuarioEntityCollectionUsuarioEntity = usuarioEntityCollectionUsuarioEntity.getIdTipoDoc();
                usuarioEntityCollectionUsuarioEntity.setIdTipoDoc(tipodocEntity);
                usuarioEntityCollectionUsuarioEntity = em.merge(usuarioEntityCollectionUsuarioEntity);
                if (oldIdTipoDocOfUsuarioEntityCollectionUsuarioEntity != null) {
                    oldIdTipoDocOfUsuarioEntityCollectionUsuarioEntity.getUsuarioEntityCollection().remove(usuarioEntityCollectionUsuarioEntity);
                    oldIdTipoDocOfUsuarioEntityCollectionUsuarioEntity = em.merge(oldIdTipoDocOfUsuarioEntityCollectionUsuarioEntity);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipodocEntity(tipodocEntity.getIdTipoDoc()) != null) {
                throw new PreexistingEntityException("TipodocEntity " + tipodocEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipodocEntity tipodocEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipodocEntity persistentTipodocEntity = em.find(TipodocEntity.class, tipodocEntity.getIdTipoDoc());
            Collection<UsuarioEntity> usuarioEntityCollectionOld = persistentTipodocEntity.getUsuarioEntityCollection();
            Collection<UsuarioEntity> usuarioEntityCollectionNew = tipodocEntity.getUsuarioEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (UsuarioEntity usuarioEntityCollectionOldUsuarioEntity : usuarioEntityCollectionOld) {
                if (!usuarioEntityCollectionNew.contains(usuarioEntityCollectionOldUsuarioEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioEntity " + usuarioEntityCollectionOldUsuarioEntity + " since its idTipoDoc field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UsuarioEntity> attachedUsuarioEntityCollectionNew = new ArrayList<UsuarioEntity>();
            for (UsuarioEntity usuarioEntityCollectionNewUsuarioEntityToAttach : usuarioEntityCollectionNew) {
                usuarioEntityCollectionNewUsuarioEntityToAttach = em.getReference(usuarioEntityCollectionNewUsuarioEntityToAttach.getClass(), usuarioEntityCollectionNewUsuarioEntityToAttach.getNroDoc());
                attachedUsuarioEntityCollectionNew.add(usuarioEntityCollectionNewUsuarioEntityToAttach);
            }
            usuarioEntityCollectionNew = attachedUsuarioEntityCollectionNew;
            tipodocEntity.setUsuarioEntityCollection(usuarioEntityCollectionNew);
            tipodocEntity = em.merge(tipodocEntity);
            for (UsuarioEntity usuarioEntityCollectionNewUsuarioEntity : usuarioEntityCollectionNew) {
                if (!usuarioEntityCollectionOld.contains(usuarioEntityCollectionNewUsuarioEntity)) {
                    TipodocEntity oldIdTipoDocOfUsuarioEntityCollectionNewUsuarioEntity = usuarioEntityCollectionNewUsuarioEntity.getIdTipoDoc();
                    usuarioEntityCollectionNewUsuarioEntity.setIdTipoDoc(tipodocEntity);
                    usuarioEntityCollectionNewUsuarioEntity = em.merge(usuarioEntityCollectionNewUsuarioEntity);
                    if (oldIdTipoDocOfUsuarioEntityCollectionNewUsuarioEntity != null && !oldIdTipoDocOfUsuarioEntityCollectionNewUsuarioEntity.equals(tipodocEntity)) {
                        oldIdTipoDocOfUsuarioEntityCollectionNewUsuarioEntity.getUsuarioEntityCollection().remove(usuarioEntityCollectionNewUsuarioEntity);
                        oldIdTipoDocOfUsuarioEntityCollectionNewUsuarioEntity = em.merge(oldIdTipoDocOfUsuarioEntityCollectionNewUsuarioEntity);
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
                String id = tipodocEntity.getIdTipoDoc();
                if (findTipodocEntity(id) == null) {
                    throw new NonexistentEntityException("The tipodocEntity with id " + id + " no longer exists.");
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
            TipodocEntity tipodocEntity;
            try {
                tipodocEntity = em.getReference(TipodocEntity.class, id);
                tipodocEntity.getIdTipoDoc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipodocEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsuarioEntity> usuarioEntityCollectionOrphanCheck = tipodocEntity.getUsuarioEntityCollection();
            for (UsuarioEntity usuarioEntityCollectionOrphanCheckUsuarioEntity : usuarioEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipodocEntity (" + tipodocEntity + ") cannot be destroyed since the UsuarioEntity " + usuarioEntityCollectionOrphanCheckUsuarioEntity + " in its usuarioEntityCollection field has a non-nullable idTipoDoc field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipodocEntity);
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

    public List<TipodocEntity> findTipodocEntityEntities() {
        return findTipodocEntityEntities(true, -1, -1);
    }

    public List<TipodocEntity> findTipodocEntityEntities(int maxResults, int firstResult) {
        return findTipodocEntityEntities(false, maxResults, firstResult);
    }

    private List<TipodocEntity> findTipodocEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipodocEntity.class));
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

    public TipodocEntity findTipodocEntity(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipodocEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipodocEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipodocEntity> rt = cq.from(TipodocEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
