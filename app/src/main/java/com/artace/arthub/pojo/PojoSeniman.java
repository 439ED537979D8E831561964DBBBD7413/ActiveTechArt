package com.artace.arthub.pojo;


public class PojoSeniman {

    private int id_seniman,id_jenis_seniman,id_user,id_event;
    private String nama,jenis_kelamin,portfolio,no_hp,umur,foto,keahlian_spesifik,format_solo_grup;

    private String status,namaevent;
    private int id_tawaran_tampil;

    public PojoSeniman() {
    }

    public PojoSeniman(int id_seniman, int id_jenis_seniman, String nama, String foto, String keahlian_spesifik, String status, int id_tawaran_tampil, String namaevent) {
        this.id_seniman = id_seniman;
        this.id_jenis_seniman = id_jenis_seniman;
        this.nama = nama;
        this.foto = foto;
        this.keahlian_spesifik = keahlian_spesifik;
        this.status = status;
        this.id_tawaran_tampil = id_tawaran_tampil;
        this.namaevent = namaevent;
    }

    public PojoSeniman(int id_seniman, int id_event, String nama, String jenis_kelamin, String portfolio, String no_hp, String umur, String foto, String keahlian_spesifik, String format_solo_grup, String status) {
        this.id_seniman = id_seniman;
        this.id_event = id_event;
        this.nama = nama;
        this.jenis_kelamin = jenis_kelamin;
        this.portfolio = portfolio;
        this.no_hp = no_hp;
        this.umur = umur;
        this.foto = foto;
        this.keahlian_spesifik = keahlian_spesifik;
        this.format_solo_grup = format_solo_grup;
        this.status = status;
    }

    public PojoSeniman(int id_seniman, int id_jenis_seniman, int id_user, String nama, String jenis_kelamin, String portfolio, String no_hp, String umur, String foto, String keahlian_spesifik, String format_solo_grup) {
        this.id_seniman = id_seniman;
        this.id_jenis_seniman = id_jenis_seniman;
        this.id_user = id_user;
        this.nama = nama;
        this.jenis_kelamin = jenis_kelamin;
        this.portfolio = portfolio;
        this.no_hp = no_hp;
        this.umur = umur;
        this.foto = foto;
        this.keahlian_spesifik = keahlian_spesifik;
        this.format_solo_grup = format_solo_grup;
    }

    public PojoSeniman(int id_seniman, int id_user, String nama, String foto, String keahlian_spesifik) {
        this.id_seniman = id_seniman;
        this.id_user = id_user;
        this.nama = nama;
        this.foto = foto;
        this.keahlian_spesifik = keahlian_spesifik;
    }

    public int getId_tawaran_tampil() {
        return id_tawaran_tampil;
    }

    public String getNamaevent() {
        return namaevent;
    }

    public void setId_tawaran_tampil(int id_tawaran_tampil) {
        this.id_tawaran_tampil = id_tawaran_tampil;
    }

    public int getId_seniman() {
        return id_seniman;
    }

    public void setId_seniman(int id_seniman) {
        this.id_seniman = id_seniman;
    }

    public int getId_jenis_seniman() {
        return id_jenis_seniman;
    }

    public void setId_jenis_seniman(int id_jenis_seniman) {
        this.id_jenis_seniman = id_jenis_seniman;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKeahlian_spesifik() {
        return keahlian_spesifik;
    }

    public void setKeahlian_spesifik(String keahlian_spesifik) {
        this.keahlian_spesifik = keahlian_spesifik;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormat_solo_grup() {
        return format_solo_grup;
    }

    public void setFormat_solo_grup(String format_solo_grup) {
        this.format_solo_grup = format_solo_grup;
    }
}
