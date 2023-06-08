package com.example.diabeticlife;

public class DosisDiaria {
    double dosisDiariaFija;

    double dosisDiariaMostrar;

    String fechaDiaDiaria;
    String userId;


    public DosisDiaria(double dosisDiariaFija, double dosisDiariaMostrar, String fechaDiaDiaria, String userId) {
        this.dosisDiariaFija = dosisDiariaFija;
        this.dosisDiariaMostrar = dosisDiariaMostrar;
        this.fechaDiaDiaria = fechaDiaDiaria;
        this.userId = userId;
    }

    public DosisDiaria() {

    }

    public double getDosisDiariaFija() {
        return dosisDiariaFija;
    }

    public void setDosisDiariaFija(double dosisDiariaFija) {
        this.dosisDiariaFija = dosisDiariaFija;
    }

    public double getDosisDiariaMostrar() {
        return dosisDiariaMostrar;
    }

    public void setDosisDiariaMostrar(double dosisDiariaMostrar) {
        this.dosisDiariaMostrar = dosisDiariaMostrar;
    }

    public String getFechaDiaDiaria() {
        return fechaDiaDiaria;
    }

    public void setFechaDiaDiaria(String fechaDiaDiaria) {
        this.fechaDiaDiaria = fechaDiaDiaria;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return
                "DOSIS DIARIA RESTANTE:" + dosisDiariaMostrar;
    }
}
