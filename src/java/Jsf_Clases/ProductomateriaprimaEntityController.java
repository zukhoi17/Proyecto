package Jsf_Clases;

import Entity.ProductomateriaprimaEntity;
import Jsf_Clases.util.JsfUtil;
import Jsf_Clases.util.PaginationHelper;
import SessionBeans.ProductomateriaprimaEntityFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("productomateriaprimaEntityController")
@SessionScoped
public class ProductomateriaprimaEntityController implements Serializable {

    private ProductomateriaprimaEntity current;
    private DataModel items = null;
    @EJB
    private SessionBeans.ProductomateriaprimaEntityFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ProductomateriaprimaEntityController() {
    }

    public ProductomateriaprimaEntity getSelected() {
        if (current == null) {
            current = new ProductomateriaprimaEntity();
            current.setProductomateriaprimaEntityPK(new Entity.ProductomateriaprimaEntityPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProductomateriaprimaEntityFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (ProductomateriaprimaEntity) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ProductomateriaprimaEntity();
        current.setProductomateriaprimaEntityPK(new Entity.ProductomateriaprimaEntityPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getProductomateriaprimaEntityPK().setProductoId(current.getProductoEntity().getIdProducto());
            current.getProductomateriaprimaEntityPK().setMateriaPrimaId(current.getMateriaprimaEntity().getIdMateriaPrima());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductomateriaprimaEntityCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (ProductomateriaprimaEntity) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getProductomateriaprimaEntityPK().setProductoId(current.getProductoEntity().getIdProducto());
            current.getProductomateriaprimaEntityPK().setMateriaPrimaId(current.getMateriaprimaEntity().getIdMateriaPrima());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductomateriaprimaEntityUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (ProductomateriaprimaEntity) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductomateriaprimaEntityDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public ProductomateriaprimaEntity getProductomateriaprimaEntity(Entity.ProductomateriaprimaEntityPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = ProductomateriaprimaEntity.class)
    public static class ProductomateriaprimaEntityControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductomateriaprimaEntityController controller = (ProductomateriaprimaEntityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productomateriaprimaEntityController");
            return controller.getProductomateriaprimaEntity(getKey(value));
        }

        Entity.ProductomateriaprimaEntityPK getKey(String value) {
            Entity.ProductomateriaprimaEntityPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new Entity.ProductomateriaprimaEntityPK();
            key.setProductoId(Integer.parseInt(values[0]));
            key.setMateriaPrimaId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(Entity.ProductomateriaprimaEntityPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getProductoId());
            sb.append(SEPARATOR);
            sb.append(value.getMateriaPrimaId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ProductomateriaprimaEntity) {
                ProductomateriaprimaEntity o = (ProductomateriaprimaEntity) object;
                return getStringKey(o.getProductomateriaprimaEntityPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ProductomateriaprimaEntity.class.getName());
            }
        }

    }

}
