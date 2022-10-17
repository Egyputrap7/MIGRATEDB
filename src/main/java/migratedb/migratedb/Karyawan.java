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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "karyawan")
@NamedQueries({
    @NamedQuery(name = "Karyawan.findAll", query = "SELECT k FROM Karyawan k"),
    @NamedQuery(name = "Karyawan.findByIdKarywan", query = "SELECT k FROM Karyawan k WHERE k.idKarywan = :idKarywan"),
    @NamedQuery(name = "Karyawan.findByNamaKaryawan", query = "SELECT k FROM Karyawan k WHERE k.namaKaryawan = :namaKaryawan"),
    @NamedQuery(name = "Karyawan.findByAlamat", query = "SELECT k FROM Karyawan k WHERE k.alamat = :alamat"),
    @NamedQuery(name = "Karyawan.findByNoHp", query = "SELECT k FROM Karyawan k WHERE k.noHp = :noHp")})
public class Karyawan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_karywan")
    private String idKarywan;
    @Basic(optional = false)
    @Column(name = "nama_karyawan")
    private String namaKaryawan;
    @Basic(optional = false)
    @Column(name = "alamat")
    private String alamat;
    @Basic(optional = false)
    @Column(name = "no_hp")
    private String noHp;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "karyawan")
    private Transaksi transaksi;

    public Karyawan() {
    }

    public Karyawan(String idKarywan) {
        this.idKarywan = idKarywan;
    }

    public Karyawan(String idKarywan, String namaKaryawan, String alamat, String noHp) {
        this.idKarywan = idKarywan;
        this.namaKaryawan = namaKaryawan;
        this.alamat = alamat;
        this.noHp = noHp;
    }

    public String getIdKarywan() {
        return idKarywan;
    }

    public void setIdKarywan(String idKarywan) {
        this.idKarywan = idKarywan;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
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
        hash += (idKarywan != null ? idKarywan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Karyawan)) {
            return false;
        }
        Karyawan other = (Karyawan) object;
        if ((this.idKarywan == null && other.idKarywan != null) || (this.idKarywan != null && !this.idKarywan.equals(other.idKarywan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.migratedb.Karyawan[ idKarywan=" + idKarywan + " ]";
    }
    
}
