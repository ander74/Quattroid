package com.quattro.models;

import android.database.Cursor;
import BaseDatos.ServicioAuxiliar;

public class ServicioAuxiliarModel {


    //region CONSTRUCTORES

    public ServicioAuxiliarModel() { }

    public ServicioAuxiliarModel(Cursor cursor){
        Linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"));
        Servicio = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"));
        Turno = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"));
        LineaAuxiliar = cursor.getString(cursor.getColumnIndexOrThrow("LineaAuxiliar"));
        ServicioAuxiliar = cursor.getString(cursor.getColumnIndexOrThrow("ServicioAuxiliar"));
        TurnoAuxiliar = cursor.getInt(cursor.getColumnIndexOrThrow("TurnoAuxiliar"));
        Inicio = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"));
        LugarInicio = cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio"));
        Final = cursor.getString(cursor.getColumnIndexOrThrow("Final"));
        LugarFinal = cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal"));
    }

    public ServicioAuxiliarModel (ServicioAuxiliarModel auxiliar){
        FromModel(auxiliar);
        Nuevo = true;
    }

    //endregion


    //region CAMPOS PÚBLICOS

    public Boolean Nuevo = false;

    public Boolean Modificado = false;

    //endregion


    //region MÉTODOS PÚBLICOS

    public ServicioAuxiliar ToServicioAuxiliar(){
        ServicioAuxiliar auxiliar = new ServicioAuxiliar();
        auxiliar.setLinea(Linea);
        auxiliar.setServicio(Servicio);
        auxiliar.setTurno(Turno);
        auxiliar.setLineaAuxiliar(LineaAuxiliar);
        auxiliar.setServicioAuxiliar(ServicioAuxiliar);
        auxiliar.setTurnoAuxiliar(TurnoAuxiliar);
        auxiliar.setInicio(Inicio);
        auxiliar.setFinal(Final);
        auxiliar.setLugarInicio(LugarInicio);
        auxiliar.setLugarFinal(LugarFinal);
        return auxiliar;
    }


    public void FromModel(ServicioAuxiliarModel servicio){
        Linea = servicio.getLinea();
        Servicio = servicio.getServicio();
        Turno = servicio.getTurno();
        LineaAuxiliar = servicio.getLineaAuxiliar();
        ServicioAuxiliar = servicio.getServicioAuxiliar();
        TurnoAuxiliar = servicio.getTurnoAuxiliar();
        Inicio = servicio.getInicio();
        LugarInicio = servicio.getLugarInicio();
        Final = servicio.getFinal();
        LugarFinal = servicio.getLugarFinal();
    }


    public Boolean EsIgual(ServicioAuxiliarModel auxiliar){
        return Linea.equals(auxiliar.getLinea()) &&
                Servicio.equals((auxiliar.getServicio())) &&
                Turno == auxiliar.getTurno() &&
                LineaAuxiliar.equals(auxiliar.getLineaAuxiliar()) &&
                ServicioAuxiliar.equals(auxiliar.getServicioAuxiliar()) &&
                TurnoAuxiliar == auxiliar.getTurnoAuxiliar();
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


    // LÍNEA AUXILIAR
    private String LineaAuxiliar;
    public String getLineaAuxiliar() {
        return LineaAuxiliar;
    }
    public void setLineaAuxiliar(String lineaAuxiliar) {
        if (!lineaAuxiliar.equals(LineaAuxiliar)) {
            LineaAuxiliar = lineaAuxiliar;
            Modificado = true;
        }
    }


    // SERVICIO AUXILIAR
    private String ServicioAuxiliar;
    public String getServicioAuxiliar() {
        return ServicioAuxiliar;
    }
    public void setServicioAuxiliar(String servicioAuxiliar) {
        if (!servicioAuxiliar.equals(ServicioAuxiliar)){
            ServicioAuxiliar = servicioAuxiliar;
            Modificado = true;
        }
    }


    // TURNO AUXILIAR
    private int TurnoAuxiliar;
    public int getTurnoAuxiliar() {
        return TurnoAuxiliar;
    }
    public void setTurnoAuxiliar(int turnoAuxiliar) {
        if (turnoAuxiliar != TurnoAuxiliar){
            TurnoAuxiliar = turnoAuxiliar;
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


    //endregion

}
