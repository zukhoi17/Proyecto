/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import Entity.PermisoEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ponchis
 */
@Stateless
public class PermisoEntityFacade extends AbstractFacade<PermisoEntity> {

    @PersistenceContext(unitName = "CodelcaJsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisoEntityFacade() {
        super(PermisoEntity.class);
    }
    
}
