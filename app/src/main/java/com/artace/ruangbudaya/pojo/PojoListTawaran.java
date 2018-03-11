package com.artace.ruangbudaya.pojo;

import com.artace.ruangbudaya.connection.DatabaseConnection;

/**
 * Created by USER on 11/9/2017.
 */

public class PojoListTawaran {

    private int id_tawaran_tampil, id_acara, id_kelompok_seniman,id_penyelenggara_acara;
    private String harga,status,nama,tanggal,lokasi,keterangan,foto,eo,nama_eo;

    public PojoListTawaran() {
    }

    public PojoListTawaran(int id_tawaran_tampil, int id_acara, int id_kelompok_seniman, int id_penyelenggara_acara, String harga, String status, String nama, String tanggal, String lokasi, String keterangan, String foto, String eo, String nama_eo) {
        this.id_tawaran_tampil = id_tawaran_tampil;
        this.id_acara = id_acara;
        this.id_kelompok_seniman = id_kelompok_seniman;
        this.id_penyelenggara_acara = id_penyelenggara_acara;
        this.harga = harga;
        this.status = status;
        this.nama = nama;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.keterangan = keterangan;
        this.foto = foto;
        this.eo = eo;
        this.nama_eo = nama_eo;
    }

    public int getId_tawaran_tampil() {
        return id_tawaran_tampil;
    }

    public void setId_tawaran_tampil(int id_tawaran_tampil) {
        this.id_tawaran_tampil = id_tawaran_tampil;
    }

    public int getId_acara() {
        return id_acara;
    }

    public void setId_acara(int id_acara) {
        this.id_acara = id_acara;
    }

    public int getId_kelompok_seniman() {
        return id_kelompok_seniman;
    }

    public void setId_kelompok_seniman(int id_kelompok_seniman) {
        this.id_kelompok_seniman = id_kelompok_seniman;
    }

    public int getId_penyelenggara_acara() {
        return id_penyelenggara_acara;
    }

    public void setId_penyelenggara_acara(int id_penyelenggara_acara) {
        this.id_penyelenggara_acara = id_penyelenggara_acara;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getFoto() {
        return DatabaseConnection.BASE_URL+foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEo() {
        return eo;
    }

    public void setEo(String eo) {
        this.eo = eo;
    }

    public String getNama_eo() {
        return nama_eo;
    }

    public void setNama_eo(String nama_eo) {
        this.nama_eo = nama_eo;
    }
}