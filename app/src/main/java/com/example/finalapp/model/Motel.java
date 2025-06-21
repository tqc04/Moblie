package com.example.finalapp.model;

import java.io.Serializable;

public class Motel implements Serializable {
    private String id;
    private String img;
    private String tieude;
    private String mota;
    private String address;
    private String title;
    private String tinh;
    private String huyen;
    private String phone;
    private String price;
    private String dientich;

    public Motel() {
    }

// Constructor mặc định cần thiết cho Firebase


    // Constructor đầy đủ
    public Motel(String img, String tieude, String mota, String address, String title,
                String tinh, String huyen, String phone, String price, String dientich) {
        this.img = img;
        this.tieude = tieude;
        this.mota = mota;
        this.address = address;
        this.title = title;
        this.tinh = tinh;
        this.huyen = huyen;
        this.phone = phone;
        this.price = price;
        this.dientich = dientich;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters
    public String getImg() {
        return img;
    }

    public String getTieude() {
        return tieude;
    }

    public String getMota() {
        return mota;
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }

    public String getTinh() {
        return tinh;
    }

    public String getHuyen() {
        return huyen;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return price;
    }

    public String getDientich() {
        return dientich;
    }

    // Setters
    public void setImg(String img) {
        this.img = img;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTinh(String tinh) {
        this.tinh = tinh;
    }

    public void setHuyen(String huyen) {
        this.huyen = huyen;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDientich(String dientich) {
        this.dientich = dientich;
    }
}

