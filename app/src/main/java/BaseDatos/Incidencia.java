
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

public class Incidencia {

    private int Id = 0;
    private int Codigo;
    private String Texto;
    private int Tipo;
    private boolean Seleccionada = false;


    public Incidencia() {
    }


    public Incidencia(Cursor cursor) {
        Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        Codigo = cursor.getInt(cursor.getColumnIndexOrThrow("Codigo"));
        Texto = cursor.getString(cursor.getColumnIndexOrThrow("Incidencia"));
        Tipo = cursor.getInt(cursor.getColumnIndexOrThrow("Tipo"));
    }


    //region GETTERS

    public int getId() {
        return Id;
    }

    public int getCodigo() {
        return Codigo;
    }

    public String getTexto() {
        return Texto;
    }

    public int getTipo() {
        return Tipo;
    }

    public boolean isSeleccionada() {
        return Seleccionada;
    }


    //endregion

    //region SETTERS

    public void setId(int id) {
        Id = id;
    }

    public void setCodigo(int codigo) {
        Codigo = codigo;
    }

    public void setTexto(String texto) {
        Texto = texto;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    public void setSeleccionada(boolean seleccionada) {
        Seleccionada = seleccionada;
    }


    //endregion


}
