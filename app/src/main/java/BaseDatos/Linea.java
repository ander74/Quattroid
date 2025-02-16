
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

public class Linea {

    private int Id = 0;
    private String Linea = "";
    private String Texto = "";
    private boolean Seleccionado = false;


    //region CONSTRUCTOR

    public Linea(){}


    public Linea(Cursor cursor){
        Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        Linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"));
        Texto = cursor.getString(cursor.getColumnIndexOrThrow("Texto"));
    }


    //endregion


    //region GETTERS

    public int getId() {
        return Id;
    }

    public String getLinea() {
        return Linea;
    }

    public String getTexto() {
        return Texto;
    }

    public boolean isSeleccionado() {
        return Seleccionado;
    }


    //endregion

    //region SETTERS


    public void setId(int id) {
        Id = id;
    }

    public void setLinea(String linea) {
        Linea = linea;
    }

    public void setTexto(String texto) {
        Texto = texto;
    }

    public void setSeleccionado(boolean seleccionado) {
        Seleccionado = seleccionado;
    }


    //endregion


}
