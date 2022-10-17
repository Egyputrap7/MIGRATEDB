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
public class DataobatJpaController implements Serializable {

    public DataobatJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("migratedb_migratedb_jar_0.0.1-SNAPSHOTPU");

    public DataobatJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dataobat dataobat) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Transaksi transaksiOrphanCheck = dataobat.getTransaksi();
        if (transaksiOrphanCheck != null) {
            Dataobat oldDataobatOfTransaksi = transaksiOrphanCheck.getDataobat();
            if (oldDataobatOfTransaksi != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Transaksi " + transaksiOrphanCheck + " already has an item of type Dataobat whose transaksi column cannot be null. Please make another selection for the transaksi field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier supplier = dataobat.getSupplier();
            if (supplier != null) {
                supplier = em.getReference(supplier.getClass(), supplier.getIdPerusahaan());
                dataobat.setSupplier(supplier);
            }
            Transaksi transaksi = dataobat.getTransaksi();
            if (transaksi != null) {
                transaksi = em.getReference(transaksi.getClass(), transaksi.getKodeTransaksi());
                dataobat.setTransaksi(transaksi);
            }
            em.persist(dataobat);
            if (supplier != null) {
                Dataobat oldDataobatOfSupplier = supplier.getDataobat();
                if (oldDataobatOfSupplier != null) {
                    oldDataobatOfSupplier.setSupplier(null);
                    oldDataobatOfSupplier = em.merge(oldDataobatOfSupplier);
                }
                supplier.setDataobat(dataobat);
                supplier = em.merge(supplier);
            }
            if (transaksi != null) {
                transaksi.setDataobat(dataobat);
                transaksi = em.merge(transaksi);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDataobat(dataobat.getKodeObat()) != null) {
                throw new PreexistingEntityException("Dataobat " + dataobat + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dataobat dataobat) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dataobat persistentDataobat = em.find(Dataobat.class, dataobat.getKodeObat());
            Supplier supplierOld = persistentDataobat.getSupplier();
            Supplier supplierNew = dataobat.getSupplier();
            Transaksi transaksiOld = persistentDataobat.getTransaksi();
            Transaksi transaksiNew = dataobat.getTransaksi();
            List<String> illegalOrphanMessages = null;
            if (supplierOld != null && !supplierOld.equals(supplierNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Supplier " + supplierOld + " since its dataobat field is not nullable.");
            }
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                Dataobat oldDataobatOfTransaksi = transaksiNew.getDataobat();
                if (oldDataobatOfTransaksi != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Transaksi " + transaksiNew + " already has an item of type Dataobat whose transaksi column cannot be null. Please make another selection for the transaksi field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (supplierNew != null) {
                supplierNew = em.getReference(supplierNew.getClass(), supplierNew.getIdPerusahaan());
                dataobat.setSupplier(supplierNew);
            }
            if (transaksiNew != null) {
                transaksiNew = em.getReference(transaksiNew.getClass(), transaksiNew.getKodeTransaksi());
                dataobat.setTransaksi(transaksiNew);
            }
            dataobat = em.merge(dataobat);
            if (supplierNew != null && !supplierNew.equals(supplierOld)) {
                Dataobat oldDataobatOfSupplier = supplierNew.getDataobat();
                if (oldDataobatOfSupplier != null) {
                    oldDataobatOfSupplier.setSupplier(null);
                    oldDataobatOfSupplier = em.merge(oldDataobatOfSupplier);
                }
                supplierNew.setDataobat(dataobat);
                supplierNew = em.merge(supplierNew);
            }
            if (transaksiOld != null && !transaksiOld.equals(transaksiNew)) {
                transaksiOld.setDataobat(null);
                transaksiOld = em.merge(transaksiOld);
            }
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                transaksiNew.setDataobat(dataobat);
                transaksiNew = em.merge(transaksiNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = dataobat.getKodeObat();
                if (findDataobat(id) == null) {
                    throw new NonexistentEntityException("The dataobat with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dataobat dataobat;
            try {
                dataobat = em.getReference(Dataobat.class, id);
                dataobat.getKodeObat();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dataobat with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Supplier supplierOrphanCheck = dataobat.getSupplier();
            if (supplierOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dataobat (" + dataobat + ") cannot be destroyed since the Supplier " + supplierOrphanCheck + " in its supplier field has a non-nullable dataobat field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Transaksi transaksi = dataobat.getTransaksi();
            if (transaksi != null) {
                transaksi.setDataobat(null);
                transaksi = em.merge(transaksi);
            }
            em.remove(dataobat);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dataobat> findDataobatEntities() {
        return findDataobatEntities(true, -1, -1);
    }

    public List<Dataobat> findDataobatEntities(int maxResults, int firstResult) {
        return findDataobatEntities(false, maxResults, firstResult);
    }

    private List<Dataobat> findDataobatEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dataobat.class));
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

    public Dataobat findDataobat(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dataobat.class, id);
        } finally {
            em.close();
        }
    }

    public int getDataobatCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dataobat> rt = cq.from(Dataobat.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
