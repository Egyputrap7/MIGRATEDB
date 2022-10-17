/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedb.migratedb;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import migratedb.migratedb.exceptions.IllegalOrphanException;
import migratedb.migratedb.exceptions.NonexistentEntityException;
import migratedb.migratedb.exceptions.PreexistingEntityException;

/**
 *
 * @author asus
 */
public class SupplierJpaController implements Serializable {

    public SupplierJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("migratedb_migratedb_jar_0.0.1-SNAPSHOTPU");

    public SupplierJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Supplier supplier) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Dataobat dataobatOrphanCheck = supplier.getDataobat();
        if (dataobatOrphanCheck != null) {
            Supplier oldSupplierOfDataobat = dataobatOrphanCheck.getSupplier();
            if (oldSupplierOfDataobat != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Dataobat " + dataobatOrphanCheck + " already has an item of type Supplier whose dataobat column cannot be null. Please make another selection for the dataobat field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dataobat dataobat = supplier.getDataobat();
            if (dataobat != null) {
                dataobat = em.getReference(dataobat.getClass(), dataobat.getKodeObat());
                supplier.setDataobat(dataobat);
            }
            em.persist(supplier);
            if (dataobat != null) {
                dataobat.setSupplier(supplier);
                dataobat = em.merge(dataobat);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSupplier(supplier.getIdPerusahaan()) != null) {
                throw new PreexistingEntityException("Supplier " + supplier + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Supplier supplier) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier persistentSupplier = em.find(Supplier.class, supplier.getIdPerusahaan());
            Dataobat dataobatOld = persistentSupplier.getDataobat();
            Dataobat dataobatNew = supplier.getDataobat();
            List<String> illegalOrphanMessages = null;
            if (dataobatNew != null && !dataobatNew.equals(dataobatOld)) {
                Supplier oldSupplierOfDataobat = dataobatNew.getSupplier();
                if (oldSupplierOfDataobat != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Dataobat " + dataobatNew + " already has an item of type Supplier whose dataobat column cannot be null. Please make another selection for the dataobat field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (dataobatNew != null) {
                dataobatNew = em.getReference(dataobatNew.getClass(), dataobatNew.getKodeObat());
                supplier.setDataobat(dataobatNew);
            }
            supplier = em.merge(supplier);
            if (dataobatOld != null && !dataobatOld.equals(dataobatNew)) {
                dataobatOld.setSupplier(null);
                dataobatOld = em.merge(dataobatOld);
            }
            if (dataobatNew != null && !dataobatNew.equals(dataobatOld)) {
                dataobatNew.setSupplier(supplier);
                dataobatNew = em.merge(dataobatNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = supplier.getIdPerusahaan();
                if (findSupplier(id) == null) {
                    throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier supplier;
            try {
                supplier = em.getReference(Supplier.class, id);
                supplier.getIdPerusahaan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.", enfe);
            }
            Dataobat dataobat = supplier.getDataobat();
            if (dataobat != null) {
                dataobat.setSupplier(null);
                dataobat = em.merge(dataobat);
            }
            em.remove(supplier);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Supplier> findSupplierEntities() {
        return findSupplierEntities(true, -1, -1);
    }

    public List<Supplier> findSupplierEntities(int maxResults, int firstResult) {
        return findSupplierEntities(false, maxResults, firstResult);
    }

    private List<Supplier> findSupplierEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Supplier.class));
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

    public Supplier findSupplier(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Supplier.class, id);
        } finally {
            em.close();
        }
    }

    public int getSupplierCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Supplier> rt = cq.from(Supplier.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
