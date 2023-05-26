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

package com.quattro.quattroid;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import Objetos.Colores;
import Objetos.Hora;

public class AdaptadorHorasAjenas extends CursorAdapter {

    LayoutInflater inflater = null;

    public AdaptadorHorasAjenas(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_horaajena, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Instancias de los elementos del item.
        TextView dia = view.findViewById(R.id.tv_dia);
        TextView horas = view.findViewById(R.id.tv_horas);
        TextView motivo = view.findViewById(R.id.tv_motivo);

        // Borrado de los textos anteriores.
        dia.setText("");
        horas.setText("");
        motivo.setText("");

        // Extraemos los datos del cursor.
        int d = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"));
        int m = cursor.getInt(cursor.getColumnIndexOrThrow("Mes"));
        int a = cursor.getInt(cursor.getColumnIndexOrThrow("Año"));
        double h = cursor.getDouble(cursor.getColumnIndexOrThrow("Horas"));
        String mot = cursor.getString(cursor.getColumnIndexOrThrow("Motivo"));

        // Ponemos el fondo del día fijo
        dia.setBackgroundColor(Colores.VIOLETA);

        // Poner la fecha
        String fechaActual = (d > 9) ? String.valueOf(d) : "0" + String.valueOf(d);
        fechaActual += " de " + Hora.MESES_MIN[m] + " de " + String.valueOf(a);
        dia.setText(fechaActual);

        // Color del texto
        if (h < 0){
            horas.setTextColor(Colores.ROJO);
        } else if (h > 0){
            horas.setTextColor(Colores.VERDE_OSCURO);
        } else {
            horas.setTextColor(Colores.AZUL_OSCURO);
        }

        // Escribimos los datos.
        horas.setText(Hora.textoDecimal(h));
        motivo.setText(mot);

    }


}
