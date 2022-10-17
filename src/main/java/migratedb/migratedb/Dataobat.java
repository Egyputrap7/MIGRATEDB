/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedb.migratedb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "dataobat")
@NamedQueries({
    @NamedQuery(name = "Dataobat.findAll", query = "SELECT d FROM Dataobat d"),
    @NamedQuery(name = "Dataobat.findByKodeObat", query = "SELECT d FROM Dataobat d WHERE d.kodeObat = :kodeObat"),
    @NamedQuery(name = "Dataobat.findByNamaObat", query = "SELECT d FROM Dataobat d WHERE d.namaObat = :namaObat"),
    @NamedQuery(name = "Dataobat.findByJenisObat", query = "SELECT d FROM Dataobat d WHERE d.jenisObat = :jenisObat"),
    @NamedQuery(name = "Dataobat.findByStock", query = "SELECT d FROM Dataobat d WHERE d.stock = :stock"),
    @NamedQuery(name = "Dataobat.findByHarga", query = "SELECT d FROM Dataobat d WHERE d.harga = :harga")})
public class Dataobat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kode_obat")
    private String kodeObat;
    @Basic(optional = false)
    @Column(name = "nama_obat")
    private String namaObat;
    @Basic(optional = false)
    @Column(name = "jenis_obat")
    private String jenisObat;
    @Basic(optional = false)
    @Column(name = "stock")
    private String stock;
    @Basic(optional = false)
    @Column(name = "harga")
    private String harga;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dataobat")
    private Supplier supplier;
    @JoinColumn(name = "kode_obat", referencedColumnName = "kode_transaksi", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transaksi transaksi;

    public Dataobat() {
    }

    public Dataobat(String kodeObat) {
        this.kodeObat = kodeObat;
    }

    public Dataobat(String kodeObat, String namaObat, String jenisObat, String stock, String harga) {
        this.kodeObat = kodeObat;
        this.namaObat = namaObat;
        this.jenisObat = jenisObat;
        this.stock = stock;
        this.harga = harga;
    }

    public String getKodeObat() {
        return kodeObat;
    }

    public void setKodeObat(String kodeObat) {
        this.kodeObat = kodeObat;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getJenisObat() {
        return jenisObat;
    }

    public void setJenisObat(String jenisObat) {
        this.jenisObat = jenisObat;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeObat != null ? kodeObat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dataobat)) {
            return false;
        }
        Dataobat other = (Dataobat) object;
        if ((this.kodeObat == null && other.kodeObat != null) || (this.kodeObat != null && !this.kodeObat.equals(other.kodeObat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.migratedb.Dataobat[ kodeObat=" + kodeObat + " ]";
    }
    
}
