package com.example.finalapp.model;

public class QuanHuyen {
    private int id;
    private int idTinh;
    private String ten;
    private boolean select;

    // Add no-argument constructor for Firebase
    public QuanHuyen() {
    }

    public QuanHuyen(int id, int idTinh, String ten, boolean select) {
        this.id = id;
        this.idTinh = idTinh;
        this.ten = ten;
        this.select = select;
    }



    public boolean isSelect() {

        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public QuanHuyen(int id, int idTinh, String ten) {
        this.id = id;
        this.idTinh = idTinh;
        this.ten = ten;
        this.select = false;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTinh() {
        return idTinh;
    }

    public void setIdTinh(int idTinh) {
        this.idTinh = idTinh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
    @Override
    public String toString() {
        return  this.ten;
    }
}

