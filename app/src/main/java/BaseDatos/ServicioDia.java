
/*
 * Copyright 2015 - Quattroid 1.0
 *
 * Creado por A. Herrero en Enero de 2015
 * http://sites.google.com/site/qtroid
 * acumulador.admin@gmail.com
 *
 * Este programa es software libre; usted puede redistruirlo y/o modificarlo bajo los términos
 * de la Licencia Pública General GNU, tal y como está publicada por la Free Software Foundation;
 * ya sea la versión 2 de la Licencia, o (a su elección) cualquier versión posterior.
 *
 * Este programa se distribuye con la intención de ser útil, pero SIN NINGUNA GARANTÍA;
 * incluso sin la garantía implícita de USABILIDAD O UTILIDAD PARA UN FIN PARTICULAR.
 * Vea la Licencia Pública General GNU en "assets/Licencia" para más detalles.
 */
package BaseDatos;

import android.database.Cursor;

public class ServicioDia {

    private int Id;
    private int DiaId;
    private int Dia;
    private int Mes;
    private int Año;
    private String Servicio;
    private int Turno;
    private String Linea;
    private String Inicio;
    private String Final;

    private String LugarInicio = "";
    private String LugarFinal = "";

    private boolean Seleccionado = false;


    public ServicioDia() {
    }


    public ServicioDia(Cursor cursor) {
        Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        DiaId = cursor.getInt(cursor.getColumnIndexOrThrow("DiaId"));
        Dia = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"));
        Mes = cursor.getInt(cursor.getColumnIndexOrThrow("Mes"));
        Año = cursor.getInt(cursor.getColumnIndexOrThrow("Año"));
        Servicio = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"));
        Turno = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"));
        Linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"));
        Inicio = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"));
        Final = cursor.getString(cursor.getColumnIndexOrThrow("Final"));
        LugarInicio = cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio"));
        LugarFinal = cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal"));
    }


    //region GETTERS

    public int getId() {
        return Id;
    }

    public int getDiaId() {
        return DiaId;
    }

    public int getDia() {
        return Dia;
    }

    public int getMes() {
        return Mes;
    }

    public int getAño() {
        return Año;
    }

    public String getServicio() {
        return Servicio;
    }

    public int getTurno() {
        return Turno;
    }

    public String getLinea() {
        return Linea;
    }

    public String getInicio() {
        return Inicio;
    }

    public String getFinal() {
        return Final;
    }

    public String getLugarInicio() {
        return LugarInicio;
    }

    public String getLugarFinal() {
        return LugarFinal;
    }

    public boolean isSeleccionado() {
        return Seleccionado;
    }


    //endregion

    //region SETTERS

    public void setId(int id) {
        Id = id;
    }
    
    public void setDiaId(int id) {
        DiaId = id;
    }

    public void setDia(int dia) {
        Dia = dia;
    }

    public void setMes(int mes) {
        Mes = mes;
    }

    public void setAño(int año) {
        Año = año;
    }

    public void setServicio(String servicio) {
        Servicio = servicio;
    }

    public void setTurno(int turno) {
        Turno = turno;
    }

    public void setLinea(String linea) {
        Linea = linea;
    }

    public void setInicio(String inicio) {
        Inicio = inicio;
    }

    public void setFinal(String aFinal) {
        Final = aFinal;
    }

    public void setLugarInicio(String lugarInicio) {
        LugarInicio = lugarInicio;
    }

    public void setLugarFinal(String lugarFinal) {
        LugarFinal = lugarFinal;
    }

    public void setSeleccionado(boolean seleccionado) {
        Seleccionado = seleccionado;
    }


    //endregion


}
