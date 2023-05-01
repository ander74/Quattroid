package com.quattro.models;

import android.database.Cursor;

import java.util.ArrayList;

import BaseDatos.Servicio;

public class ServicioModel {


    //region CONSTRUCTORES
    public ServicioModel() { }

    public ServicioModel(Cursor cursor){
        Linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"));
        Servicio = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"));
        Turno = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"));
        TomaDeje = cursor.getString(cursor.getColumnIndexOrThrow("TomaDeje"));
        Euros = cursor.getDouble(cursor.getColumnIndexOrThrow("Euros"));
        Inicio = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"));
        LugarInicio = cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio"));
        Final = cursor.getString(cursor.getColumnIndexOrThrow("Final"));
        LugarFinal = cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal"));
    }

    public ServicioModel (ServicioModel servicio){
        FromModel(servicio);
        Nuevo = true;
    }

    //endregion


    //region CAMPOS PÚBLICOS

    public Boolean Nuevo = false;

    public Boolean Modificado = false;

    //endregion


    //region MÉTODOS PÚBLICOS

    public Servicio ToServicio(){
        Servicio servicio = new Servicio();
        servicio.setLinea(Linea);
        servicio.setServicio(Servicio);
        servicio.setTurno(Turno);
        servicio.setInicio(Inicio);
        servicio.setFinal(Final);
        servicio.setLugarInicio(LugarInicio);
        servicio.setLugarFinal(LugarFinal);
        servicio.setTomaDeje(TomaDeje);
        servicio.setEuros(Euros);
        return servicio;
    }

    public void FromModel(ServicioModel servicio){
        Linea = servicio.getLinea();
        Servicio = servicio.getServicio();
        Turno = servicio.getTurno();
        TomaDeje = servicio.getTomaDeje();
        Euros = servicio.getEuros();
        Inicio = servicio.getInicio();
        LugarInicio = servicio.getLugarInicio();
        Final = servicio.getFinal();
        LugarFinal = servicio.getLugarFinal();
    }

    public Boolean EsIgual(ServicioModel servicio){
        return Linea.equals(servicio.getLinea()) && Servicio.equals(servicio.getServicio()) && Turno == servicio.getTurno();
    }

    //endregion


    //region PROPIEDADES

    // LINEA
    private String Linea = "";
    public String getLinea() {
        return Linea;
    }
    public void setLinea(String linea) {
        if(!linea.equals(Linea)) {
            Linea = linea;
            Modificado = true;
        }
    }


    // SERVICIO
    private String Servicio = "";
    public String getServicio() {
        return Servicio;
    }
    public void setServicio(String servicio) {
        if (!servicio.equals(Servicio)) {
        Servicio = servicio;
        Modificado = true;
    }
    }


    // TURNO
    private int Turno = 0;
    public int getTurno() {
        return Turno;
    }
    public void setTurno(int turno) {
        if (turno != Turno) {
            Turno = turno;
            Modificado = true;
        }
    }


    // INICIO
    private String Inicio = "";
    public String getInicio() {
        return Inicio;
    }
    public void setInicio(String inicio) {
        if (!inicio.equals(Inicio)) {
        Inicio = inicio;
        Modificado = true;
        }
    }


    // FINAL
    private String Final = "";
    public String getFinal() {
        return Final;
    }
    public void setFinal(String aFinal) {
        if (!aFinal.equals(Final)) {
        Final = aFinal;
        Modificado = true;
        }
    }


    // LUGAR INICIO
    private String LugarInicio = "";
    public String getLugarInicio() {
        return LugarInicio;
    }
    public void setLugarInicio(String lugarInicio) {
        if (!lugarInicio.equals(LugarInicio)) {
        LugarInicio = lugarInicio;
        Modificado = true;
        }
    }


    // LUGAR FINAL
    private String LugarFinal = "";
    public String getLugarFinal() {
        return LugarFinal;
    }
    public void setLugarFinal(String lugarFinal) {
        if (!lugarFinal.equals(LugarFinal)) {
        LugarFinal = lugarFinal;
        Modificado = true;
        }
    }


    // TOMA DEJE
    private String TomaDeje = "";
    public String getTomaDeje() {
        return TomaDeje;
    }
    public void setTomaDeje(String tomaDeje) {
        if (!tomaDeje.equals(TomaDeje)) {
        TomaDeje = tomaDeje;
        Modificado = true;
        }
    }


    // EUROS
    private double Euros = 0d;
    public double getEuros() { return Euros; }
    public void setEuros(double euros) {
        if (euros != Euros) {
        Euros = euros;
        Modificado = true;
        }
    }


    // SERVICIOS AUXILIARES
    private ArrayList<ServicioAuxiliarModel> ServiciosAuxiliares;
    public ArrayList<ServicioAuxiliarModel> getServiciosAuxiliares() { return ServiciosAuxiliares; }
    public void setServiciosAuxiliares(ArrayList<ServicioAuxiliarModel> servicios) {
        ServiciosAuxiliares = servicios;
        Modificado = true;
    }

    //endregion

}
