package com.example.diabeticlife;

public class Dosis {

    public String unidades;
    public String tipo;
    public String fecha;

    public String lugar;

    public String observaciones;


    public Dosis(String unidades, String tipo, String fecha, String lugar, String observaciones) {
        this.unidades = unidades;
        this.tipo = tipo;
        this.fecha = fecha;
        this.lugar = lugar;
        this.observaciones = observaciones;
    }

    public Dosis() {

    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return
                "Unidades: " + unidades +
                        "  | Tipo: " + tipo +

                        "  | Lugar: " + lugar;
    }


    /*
    Posible actualización añadir otro usuario a la misma cuenta,
    una madre puede llevar ambos registros de sus 2 hijos.
     */
}
