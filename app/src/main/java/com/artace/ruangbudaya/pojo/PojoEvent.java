package com.artace.ruangbudaya.pojo;

/**
 * Created by herlambang on 11/2/17.
 */

public class PojoEvent {

    private int id_acara, id_penyelenggara_acara;
    private String nama, tanggal, lokasi, keterangan,foto,eo;
    private double latitude,longitude;

    public PojoEvent() {
    }

    public PojoEvent(int id_acara, int id_penyelenggara_acara, String nama, String tanggal, String lokasi, String keterangan, String foto, String eo) {
        this.id_acara = id_acara;
        this.id_penyelenggara_acara = id_penyelenggara_acara;
        this.nama = nama;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.keterangan = keterangan;
        this.foto = foto;
        this.eo = eo;
    }

    public PojoEvent(int id_acara, String nama, String tanggal, String foto) {
        this.id_acara = id_acara;
        this.nama = nama;
        this.tanggal = tanggal;
        this.foto = foto;
    }

    public PojoEvent(int id_acara, int id_penyelenggara_acara, String nama, String tanggal, String keterangan, String foto, String eo) {
        this.id_acara = id_acara;
        this.id_penyelenggara_acara = id_penyelenggara_acara;
        this.nama = nama;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.foto = foto;
        this.eo = eo;
    }

    public PojoEvent(int id_acara, int id_penyelenggara_acara, String nama, String tanggal, String keterangan, String foto, String eo, double latitude, double longitude) {
        this.id_acara = id_acara;
        this.id_penyelenggara_acara = id_penyelenggara_acara;
        this.nama = nama;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.foto = foto;
        this.eo = eo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEo() {
        return eo;
    }

    public void setEo(String eo) {
        this.eo = eo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId_acara() {
        return id_acara;
    }

    public void setId_acara(int id_acara) {
        this.id_acara = id_acara;
    }

    public int getId_penyelenggara_acara() {
        return id_penyelenggara_acara;
    }

    public void setId_penyelenggara_acara(int id_penyelenggara_acara) {
        this.id_penyelenggara_acara = id_penyelenggara_acara;
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


}
