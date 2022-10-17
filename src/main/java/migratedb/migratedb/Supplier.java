/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedb.migratedb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "supplier")
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findByIdPerusahaan", query = "SELECT s FROM Supplier s WHERE s.idPerusahaan = :idPerusahaan"),
    @NamedQuery(name = "Supplier.findByNamaPerusahaan", query = "SELECT s FROM Supplier s WHERE s.namaPerusahaan = :namaPerusahaan"),
    @NamedQuery(name = "Supplier.findByAsalKota", query = "SELECT s FROM Supplier s WHERE s.asalKota = :asalKota"),
    @NamedQuery(name = "Supplier.findByTanggal", query = "SELECT s FROM Supplier s WHERE s.tanggal = :tanggal"),
    @NamedQuery(name = "Supplier.findByNamaObat", query = "SELECT s FROM Supplier s WHERE s.namaObat = :namaObat"),
    @NamedQuery(name = "Supplier.findByKodeObat", query = "SELECT s FROM Supplier s WHERE s.kodeObat = :kodeObat"),
    @NamedQuery(name = "Supplier.findByJumlah", query = "SELECT s FROM Supplier s WHERE s.jumlah = :jumlah")})
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_perusahaan")
    private String idPerusahaan;
    @Basic(optional = false)
    @Column(name = "nama_perusahaan")
    private int namaPerusahaan;
    @Basic(optional = false)
    @Column(name = "asal_kota")
    private String asalKota;
    @Basic(optional = false)
    @Column(name = "tanggal")
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @Basic(optional = false)
    @Column(name = "nama_obat")
    private String namaObat;
    @Basic(optional = false)
    @Column(name = "kode_obat")
    private String kodeObat;
    @Basic(optional = false)
    @Column(name = "jumlah")
    private String jumlah;
    @JoinColumn(name = "id_perusahaan", referencedColumnName = "kode_obat", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Dataobat dataobat;

    public Supplier() {
    }

    public Supplier(String idPerusahaan) {
        this.idPerusahaan = idPerusahaan;
    }

    public Supplier(String idPerusahaan, int namaPerusahaan, String asalKota, Date tanggal, String namaObat, String kodeObat, String jumlah) {
        this.idPerusahaan = idPerusahaan;
        this.namaPerusahaan = namaPerusahaan;
        this.asalKota = asalKota;
        this.tanggal = tanggal;
        this.namaObat = namaObat;
        this.kodeObat = kodeObat;
        this.jumlah = jumlah;
    }

    public String getIdPerusahaan() {
        return idPerusahaan;
    }

    public void setIdPerusahaan(String idPerusahaan) {
        this.idPerusahaan = idPerusahaan;
    }

    public int getNamaPerusahaan() {
        return namaPerusahaan;
    }

    public void setNamaPerusahaan(int namaPerusahaan) {
        this.namaPerusahaan = namaPerusahaan;
    }

    public String getAsalKota() {
        return asalKota;
    }

    public void setAsalKota(String asalKota) {
        this.asalKota = asalKota;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getKodeObat() {
        return kodeObat;
    }

    public void setKodeObat(String kodeObat) {
        this.kodeObat = kodeObat;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public Dataobat getDataobat() {
        return dataobat;
    }

    public void setDataobat(Dataobat dataobat) {
        this.dataobat = dataobat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerusahaan != null ? idPerusahaan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.idPerusahaan == null && other.idPerusahaan != null) || (this.idPerusahaan != null && !this.idPerusahaan.equals(other.idPerusahaan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.migratedb.Supplier[ idPerusahaan=" + idPerusahaan + " ]";
    }
    
}
