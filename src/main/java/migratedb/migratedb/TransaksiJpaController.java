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
public class TransaksiJpaController implements Serializable {

    public TransaksiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("migratedb_migratedb_jar_0.0.1-SNAPSHOTPU");

    public TransaksiJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaksi transaksi) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Karyawan karyawanOrphanCheck = transaksi.getKaryawan();
        if (karyawanOrphanCheck != null) {
            Transaksi oldTransaksiOfKaryawan = karyawanOrphanCheck.getTransaksi();
            if (oldTransaksiOfKaryawan != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Karyawan " + karyawanOrphanCheck + " already has an item of type Transaksi whose karyawan column cannot be null. Please make another selection for the karyawan field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Karyawan karyawan = transaksi.getKaryawan();
            if (karyawan != null) {
                karyawan = em.getReference(karyawan.getClass(), karyawan.getIdKarywan());
                transaksi.setKaryawan(karyawan);
            }
            Dataobat dataobat = transaksi.getDataobat();
            if (dataobat != null) {
                dataobat = em.getReference(dataobat.getClass(), dataobat.getKodeObat());
                transaksi.setDataobat(dataobat);
            }
            em.persist(transaksi);
            if (karyawan != null) {
                karyawan.setTransaksi(transaksi);
                karyawan = em.merge(karyawan);
            }
            if (dataobat != null) {
                Transaksi oldTransaksiOfDataobat = dataobat.getTransaksi();
                if (oldTransaksiOfDataobat != null) {
                    oldTransaksiOfDataobat.setDataobat(null);
                    oldTransaksiOfDataobat = em.merge(oldTransaksiOfDataobat);
                }
                dataobat.setTransaksi(transaksi);
                dataobat = em.merge(dataobat);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransaksi(transaksi.getKodeTransaksi()) != null) {
                throw new PreexistingEntityException("Transaksi " + transaksi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaksi transaksi) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi persistentTransaksi = em.find(Transaksi.class, transaksi.getKodeTransaksi());
            Karyawan karyawanOld = persistentTransaksi.getKaryawan();
            Karyawan karyawanNew = transaksi.getKaryawan();
            Dataobat dataobatOld = persistentTransaksi.getDataobat();
            Dataobat dataobatNew = transaksi.getDataobat();
            List<String> illegalOrphanMessages = null;
            if (karyawanNew != null && !karyawanNew.equals(karyawanOld)) {
                Transaksi oldTransaksiOfKaryawan = karyawanNew.getTransaksi();
                if (oldTransaksiOfKaryawan != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Karyawan " + karyawanNew + " already has an item of type Transaksi whose karyawan column cannot be null. Please make another selection for the karyawan field.");
                }
            }
            if (dataobatOld != null && !dataobatOld.equals(dataobatNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Dataobat " + dataobatOld + " since its transaksi field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (karyawanNew != null) {
                karyawanNew = em.getReference(karyawanNew.getClass(), karyawanNew.getIdKarywan());
                transaksi.setKaryawan(karyawanNew);
            }
            if (dataobatNew != null) {
                dataobatNew = em.getReference(dataobatNew.getClass(), dataobatNew.getKodeObat());
                transaksi.setDataobat(dataobatNew);
            }
            transaksi = em.merge(transaksi);
            if (karyawanOld != null && !karyawanOld.equals(karyawanNew)) {
                karyawanOld.setTransaksi(null);
                karyawanOld = em.merge(karyawanOld);
            }
            if (karyawanNew != null && !karyawanNew.equals(karyawanOld)) {
                karyawanNew.setTransaksi(transaksi);
                karyawanNew = em.merge(karyawanNew);
            }
            if (dataobatNew != null && !dataobatNew.equals(dataobatOld)) {
                Transaksi oldTransaksiOfDataobat = dataobatNew.getTransaksi();
                if (oldTransaksiOfDataobat != null) {
                    oldTransaksiOfDataobat.setDataobat(null);
                    oldTransaksiOfDataobat = em.merge(oldTransaksiOfDataobat);
                }
                dataobatNew.setTransaksi(transaksi);
                dataobatNew = em.merge(dataobatNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = transaksi.getKodeTransaksi();
                if (findTransaksi(id) == null) {
                    throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.");
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
            Transaksi transaksi;
            try {
                transaksi = em.getReference(Transaksi.class, id);
                transaksi.getKodeTransaksi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Dataobat dataobatOrphanCheck = transaksi.getDataobat();
            if (dataobatOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Transaksi (" + transaksi + ") cannot be destroyed since the Dataobat " + dataobatOrphanCheck + " in its dataobat field has a non-nullable transaksi field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Karyawan karyawan = transaksi.getKaryawan();
            if (karyawan != null) {
                karyawan.setTransaksi(null);
                karyawan = em.merge(karyawan);
            }
            em.remove(transaksi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaksi> findTransaksiEntities() {
        return findTransaksiEntities(true, -1, -1);
    }

    public List<Transaksi> findTransaksiEntities(int maxResults, int firstResult) {
        return findTransaksiEntities(false, maxResults, firstResult);
    }

    private List<Transaksi> findTransaksiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaksi.class));
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

    public Transaksi findTransaksi(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaksi.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaksiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaksi> rt = cq.from(Transaksi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
