/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import Entity.DefectuosoEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ponchis
 */
@Stateless
public class DefectuosoEntityFacade extends AbstractFacade<DefectuosoEntity> {

    @PersistenceContext(unitName = "CodelcaJsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DefectuosoEntityFacade() {
        super(DefectuosoEntity.class);
    }
    
}
