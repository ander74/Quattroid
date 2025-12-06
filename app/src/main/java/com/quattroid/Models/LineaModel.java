package com.quattroid.Models;


import android.database.Cursor;

import java.util.ArrayList;

import BaseDatos.Linea;

public class LineaModel {


    //region CONSTRUCTORES

    public LineaModel() {
    }

    public LineaModel(Cursor cursor) {
        Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        Linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"));
        Texto = cursor.getString(cursor.getColumnIndexOrThrow("Texto"));
    }

    public LineaModel(LineaModel linea) {
        FromModel(linea);
        Nuevo = true;
    }

    //endregion


    //region CAMPOS PÚBLICOS

    public Boolean Nuevo = false;

    public Boolean Modificado = false;

    //endregion


    //region MÉTODOS PÚBLICOS

    public BaseDatos.Linea ToLinea() {
        Linea linea = new Linea();
        linea.setId(Id);
        linea.setLinea(Linea);
        linea.setTexto(Texto);
        return linea;
    }

    public void FromModel(LineaModel linea) {
        Id = linea.getId();
        Linea = linea.getLinea();
        Texto = linea.getTexto();
    }


    public Boolean EsIgual(LineaModel otra) {
        return Linea.equals(otra.getLinea());
    }

    //endregion


    //region PROPIEDADES

    // ID
    private int Id = 0;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        if (Id != id) {
            Id = id;
            Modificado = true;
        }
    }

    // LÍNEA
    private String Linea = "";

    public String getLinea() {
        return Linea;
    }

    public void setLinea(String linea) {
        if (!linea.equals(Linea)) {
            Linea = linea;
            Modificado = true;
        }
    }


    // TEXTO
    private String Texto = "";

    public String getTexto() {
        return Texto;
    }

    public void setTexto(String texto) {
        if (!texto.equals(Texto)) {
            Texto = texto;
            Modificado = true;
        }
    }

    // SELECCIONADA
    private boolean Seleccionada = false;

    public boolean isSeleccionada() {
        return Seleccionada;
    }

    public void setSeleccionada(boolean seleccionada) {
        if (seleccionada != Seleccionada) {
            Seleccionada = seleccionada;
            Modificado = true;
        }
    }


    // SERVICIOS
    private ArrayList<ServicioModel> Servicios;

    public ArrayList<ServicioModel> getServicios() {
        return Servicios;
    }

    public void setServicios(ArrayList<ServicioModel> servicios) {
        Servicios = servicios;
        Modificado = true;
    }


    //endregion

}
