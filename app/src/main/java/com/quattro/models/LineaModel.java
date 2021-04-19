package com.quattro.models;


import android.database.Cursor;

import java.util.ArrayList;

import BaseDatos.Linea;

public class LineaModel {


    //region CONSTRUCTORES

    public LineaModel() { }

    public LineaModel(Cursor cursor){
        Linea = cursor.getString(cursor.getColumnIndex("Linea"));
        Texto = cursor.getString(cursor.getColumnIndex("Texto"));
    }

    public LineaModel(LineaModel linea){
        FromModel(linea);
        Nuevo = true;
    }

    //endregion


    //region CAMPOS PÚBLICOS

    public Boolean Nuevo = false;

    public Boolean Modificado = false;

    //endregion


    //region MÉTODOS PÚBLICOS

    public BaseDatos.Linea ToLinea(){
        Linea linea = new Linea();
        linea.setLinea(Linea);
        linea.setTexto(Texto);
        return linea;
    }

    public void FromModel(LineaModel linea){
        Linea = linea.getLinea();
        Texto = linea.getTexto();
    }


    public Boolean EsIgual(LineaModel otra){
        return Linea.equals(otra.getLinea());
    }

    //endregion


    //region PROPIEDADES

    // LÍNEA
    private String Linea = "";
    public String getLinea() { return Linea; }
    public void setLinea(String linea) {
        if (!linea.equals(Linea)) {
            Linea = linea;
            Modificado = true;
        }
    }


    // TEXTO
    private String Texto = "";
    public String getTexto() { return Texto; }
    public void setTexto(String texto) {
        if (!texto.equals(Texto)) {
            Texto = texto;
            Modificado = true;
        }
    }


    // SERVICIOS
    private ArrayList<ServicioModel> Servicios;
    public ArrayList<ServicioModel> getServicios() { return Servicios; }
    public void setServicios(ArrayList<ServicioModel> servicios) {
        Servicios = servicios;
        Modificado = true;
    }


    //endregion

}
