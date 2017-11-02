package com.artace.arthub.pojo;

/**
 * Created by herlambang on 11/2/17.
 */

public class PojoEvent {

    private int id_event, id_event_organizer;
    private String nama, tanggal, lokasi, keterangan,foto,eo;

    public PojoEvent() {
    }

    public PojoEvent(int id_event, int id_event_organizer, String nama, String tanggal, String lokasi, String keterangan, String foto, String eo) {
        this.id_event = id_event;
        this.id_event_organizer = id_event_organizer;
        this.nama = nama;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.keterangan = keterangan;
        this.foto = foto;
        this.eo = eo;
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

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public int getId_event_organizer() {
        return id_event_organizer;
    }

    public void setId_event_organizer(int id_event_organizer) {
        this.id_event_organizer = id_event_organizer;
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
