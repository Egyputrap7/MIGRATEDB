/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedb.migratedb;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "transaksi")
@NamedQueries({
    @NamedQuery(name = "Transaksi.findAll", query = "SELECT t FROM Transaksi t"),
    @NamedQuery(name = "Transaksi.findByKodeTransaksi", query = "SELECT t FROM Transaksi t WHERE t.kodeTransaksi = :kodeTransaksi"),
    @NamedQuery(name = "Transaksi.findByNamaObat", query = "SELECT t FROM Transaksi t WHERE t.namaObat = :namaObat"),
    @NamedQuery(name = "Transaksi.findByQtyTerjual", query = "SELECT t FROM Transaksi t WHERE t.qtyTerjual = :qtyTerjual"),
    @NamedQuery(name = "Transaksi.findByHargaTotal", query = "SELECT t FROM Transaksi t WHERE t.hargaTotal = :hargaTotal"),
    @NamedQuery(name = "Transaksi.findByTanggal", query = "SELECT t FROM Transaksi t WHERE t.tanggal = :tanggal")})
public class Transaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kode_transaksi")
    private String kodeTransaksi;
    @Basic(optional = false)
    @Column(name = "nama_obat")
    private String namaObat;
    @Basic(optional = false)
    @Column(name = "qty_terjual")
    private String qtyTerjual;
    @Basic(optional = false)
    @Column(name = "harga_total")
    private String hargaTotal;
    @Basic(optional = false)
    @Column(name = "tanggal")
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @JoinColumn(name = "kode_transaksi", referencedColumnName = "id_karywan", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Karyawan karyawan;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transaksi")
    private Dataobat dataobat;

    public Transaksi() {
    }

    public Transaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public Transaksi(String kodeTransaksi, String namaObat, String qtyTerjual, String hargaTotal, Date tanggal) {
        this.kodeTransaksi = kodeTransaksi;
        this.namaObat = namaObat;
        this.qtyTerjual = qtyTerjual;
        this.hargaTotal = hargaTotal;
        this.tanggal = tanggal;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getQtyTerjual() {
        return qtyTerjual;
    }

    public void setQtyTerjual(String qtyTerjual) {
        this.qtyTerjual = qtyTerjual;
    }

    public String getHargaTotal() {
        return hargaTotal;
    }

    public void setHargaTotal(String hargaTotal) {
        this.hargaTotal = hargaTotal;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Karyawan getKaryawan() {
        return karyawan;
    }

    public void setKaryawan(Karyawan karyawan) {
        this.karyawan = karyawan;
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
        hash += (kodeTransaksi != null ? kodeTransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaksi)) {
            return false;
        }
        Transaksi other = (Transaksi) object;
        if ((this.kodeTransaksi == null && other.kodeTransaksi != null) || (this.kodeTransaksi != null && !this.kodeTransaksi.equals(other.kodeTransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.migratedb.Transaksi[ kodeTransaksi=" + kodeTransaksi + " ]";
    }
    
}
