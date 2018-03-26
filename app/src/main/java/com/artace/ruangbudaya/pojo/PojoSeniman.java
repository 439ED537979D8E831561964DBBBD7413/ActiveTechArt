package com.artace.ruangbudaya.pojo;


public class PojoSeniman {

    private int id_kelompok_seniman,id_bidang_seni,id_user,id_acara;
    private String nama,portfolio,no_hp,foto,namaeo,tanggalevent;

    private String status,namaevent;
    private int id_tawaran_tampil,status_kehadiran;

    public PojoSeniman() {
    }

    public PojoSeniman(int id_kelompok_seniman, int id_bidang_seni, String nama, String foto, String status, int id_tawaran_tampil, String namaevent) {
        this.id_kelompok_seniman = id_kelompok_seniman;
        this.id_bidang_seni = id_bidang_seni;
        this.nama = nama;
        this.foto = foto;
        this.status = status;
        this.id_tawaran_tampil = id_tawaran_tampil;
        this.namaevent = namaevent;
    }

    public PojoSeniman(int id_kelompok_seniman, int id_bidang_seni, String nama, String foto, String status, int id_tawaran_tampil, int status_kehadiran, String namaevent, String tanggalevent, String namaeo) {
        this.id_kelompok_seniman = id_kelompok_seniman;
        this.id_bidang_seni = id_bidang_seni;
        this.nama = nama;
        this.foto = foto;
        this.status = status;
        this.id_tawaran_tampil = id_tawaran_tampil;
        this.status_kehadiran = status_kehadiran;
        this.namaevent = namaevent;
        this.namaeo = namaeo;
        this.tanggalevent = tanggalevent;
    }

    public PojoSeniman(int id_kelompok_seniman, int id_bidang_seni, String nama, String foto, String status, int id_tawaran_tampil, int status_kehadiran, String namaevent) {
        this.id_kelompok_seniman = id_kelompok_seniman;
        this.id_bidang_seni = id_bidang_seni;
        this.nama = nama;
        this.foto = foto;
        this.status = status;
        this.id_tawaran_tampil = id_tawaran_tampil;
        this.status_kehadiran = status_kehadiran;
        this.namaevent = namaevent;
    }

    public PojoSeniman(int id_kelompok_seniman, int id_acara, String nama, String portfolio, String no_hp, String foto, String status) {
        this.id_kelompok_seniman = id_kelompok_seniman;
        this.id_acara = id_acara;
        this.nama = nama;
        this.portfolio = portfolio;
        this.no_hp = no_hp;
        this.foto = foto;
        this.status = status;
    }

    public PojoSeniman(int id_kelompok_seniman, int id_bidang_seni, int id_user, String nama, String portfolio, String no_hp, String foto) {
        this.id_kelompok_seniman = id_kelompok_seniman;
        this.id_bidang_seni = id_bidang_seni;
        this.id_user = id_user;
        this.nama = nama;
        this.portfolio = portfolio;
        this.no_hp = no_hp;
        this.foto = foto;
    }

    public PojoSeniman(int id_kelompok_seniman, int id_user, String nama, String foto) {
        this.id_kelompok_seniman = id_kelompok_seniman;
        this.id_user = id_user;
        this.nama = nama;
        this.foto = foto;
    }

    public int getStatus_kehadiran() {
        return status_kehadiran;
    }

    public void setStatus_kehadiran(int status_kehadiran) {
        this.status_kehadiran = status_kehadiran;
    }

    public String getNamaeo() {
        return namaeo;
    }

    public String getTanggalevent() {
        return tanggalevent;
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

    public int getId_kelompok_seniman() {
        return id_kelompok_seniman;
    }

    public void setId_kelompok_seniman(int id_kelompok_seniman) {
        this.id_kelompok_seniman = id_kelompok_seniman;
    }

    public int getId_bidang_seni() {
        return id_bidang_seni;
    }

    public void setId_bidang_seni(int id_bidang_seni) {
        this.id_bidang_seni = id_bidang_seni;
    }

    public int getId_acara() {
        return id_acara;
    }

    public void setId_acara(int id_acara) {
        this.id_acara = id_acara;
    }

    public void setNamaevent(String namaevent) {
        this.namaevent = namaevent;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getid_acara() {
        return id_acara;
    }

    public void setid_acara(int id_acara) {
        this.id_acara = id_acara;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
