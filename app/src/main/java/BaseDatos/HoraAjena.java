
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

public class HoraAjena {

    private int Id = 0;
    private int Dia;
    private int Mes;
    private int Año;
    private double Horas;
    private String Motivo;
    private boolean Seleccionada = false;


    public HoraAjena() {
    }

    public HoraAjena(Cursor cursor) {
        Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        Dia = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"));
        Mes = cursor.getInt(cursor.getColumnIndexOrThrow("Mes"));
        Año = cursor.getInt(cursor.getColumnIndexOrThrow("Año"));
        Horas = cursor.getDouble(cursor.getColumnIndexOrThrow("Horas"));
        Motivo = cursor.getString(cursor.getColumnIndexOrThrow("Motivo"));
    }


    //region GETTERS

    public int getId() {
        return Id;
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

    public double getHoras() {
        return Horas;
    }

    public String getMotivo() {
        return Motivo;
    }

    public boolean isSeleccionada() {
        return Seleccionada;
    }


    //endregion

    //region SETTERS

    public void setId(int id) {
        Id = id;
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

    public void setHoras(double horas) {
        Horas = horas;
    }

    public void setMotivo(String motivo) {
        Motivo = motivo;
    }

    public void setSeleccionada(boolean seleccionada) {
        Seleccionada = seleccionada;
    }


    //endregion

}
