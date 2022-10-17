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
public class KaryawanJpaController implements Serializable {

    public KaryawanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("migratedb_migratedb_jar_0.0.1-SNAPSHOTPU");

    public KaryawanJpaController() {
    }

    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Karyawan karyawan) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi transaksi = karyawan.getTransaksi();
            if (transaksi != null) {
                transaksi = em.getReference(transaksi.getClass(), transaksi.getKodeTransaksi());
                karyawan.setTransaksi(transaksi);
            }
            em.persist(karyawan);
            if (transaksi != null) {
                Karyawan oldKaryawanOfTransaksi = transaksi.getKaryawan();
                if (oldKaryawanOfTransaksi != null) {
                    oldKaryawanOfTransaksi.setTransaksi(null);
                    oldKaryawanOfTransaksi = em.merge(oldKaryawanOfTransaksi);
                }
                transaksi.setKaryawan(karyawan);
                transaksi = em.merge(transaksi);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKaryawan(karyawan.getIdKarywan()) != null) {
                throw new PreexistingEntityException("Karyawan " + karyawan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Karyawan karyawan) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Karyawan persistentKaryawan = em.find(Karyawan.class, karyawan.getIdKarywan());
            Transaksi transaksiOld = persistentKaryawan.getTransaksi();
            Transaksi transaksiNew = karyawan.getTransaksi();
            List<String> illegalOrphanMessages = null;
            if (transaksiOld != null && !transaksiOld.equals(transaksiNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Transaksi " + transaksiOld + " since its karyawan field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (transaksiNew != null) {
                transaksiNew = em.getReference(transaksiNew.getClass(), transaksiNew.getKodeTransaksi());
                karyawan.setTransaksi(transaksiNew);
            }
            karyawan = em.merge(karyawan);
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                Karyawan oldKaryawanOfTransaksi = transaksiNew.getKaryawan();
                if (oldKaryawanOfTransaksi != null) {
                    oldKaryawanOfTransaksi.setTransaksi(null);
                    oldKaryawanOfTransaksi = em.merge(oldKaryawanOfTransaksi);
                }
                transaksiNew.setKaryawan(karyawan);
                transaksiNew = em.merge(transaksiNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = karyawan.getIdKarywan();
                if (findKaryawan(id) == null) {
                    throw new NonexistentEntityException("The karyawan with id " + id + " no longer exists.");
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
            Karyawan karyawan;
            try {
                karyawan = em.getReference(Karyawan.class, id);
                karyawan.getIdKarywan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The karyawan with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Transaksi transaksiOrphanCheck = karyawan.getTransaksi();
            if (transaksiOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Karyawan (" + karyawan + ") cannot be destroyed since the Transaksi " + transaksiOrphanCheck + " in its transaksi field has a non-nullable karyawan field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(karyawan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Karyawan> findKaryawanEntities() {
        return findKaryawanEntities(true, -1, -1);
    }

    public List<Karyawan> findKaryawanEntities(int maxResults, int firstResult) {
        return findKaryawanEntities(false, maxResults, firstResult);
    }

    private List<Karyawan> findKaryawanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Karyawan.class));
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

    public Karyawan findKaryawan(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Karyawan.class, id);
        } finally {
            em.close();
        }
    }

    public int getKaryawanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Karyawan> rt = cq.from(Karyawan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
