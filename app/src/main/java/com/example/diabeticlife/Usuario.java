package com.example.diabeticlife;

public class Usuario {

    String nombre;
    String apellidos;
    String telefono;
    String mail;

    String fechaAlta;


    public Usuario(String nombre, String apellidos, String telefono, String mail, String fechaAlta) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.mail = mail;
        this.fechaAlta = fechaAlta;
    }

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", telefono='" + telefono + '\'' +
                ", mail='" + mail + '\'' +
                ", fechaAlta='" + fechaAlta + '\'' +
                '}';
    }
}
