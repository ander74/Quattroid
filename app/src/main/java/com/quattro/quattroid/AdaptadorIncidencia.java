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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdaptadorIncidencia extends CursorAdapter {

    LayoutInflater inflater = null;

    public AdaptadorIncidencia(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_incidencia, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Instancias de los elementos del item.
        TextView codigo = (TextView) view.findViewById(R.id.codigo);
        TextView incidencia = (TextView) view.findViewById(R.id.incidencia);
        LinearLayout item = (LinearLayout) view.findViewById(R.id.itemIncidencia);

        // Borrado de los textos anteriores.
        codigo.setText("");
        incidencia.setText("");

        // Extraemos los datos del cursor.
        int cod = cursor.getInt(cursor.getColumnIndex("Codigo"));
        String inc = cursor.getString(cursor.getColumnIndex("Incidencia"));

        // Color del fondo
        if (cod % 2 == 0) {
            item.setBackground(context.getResources().getDrawable(R.drawable.fondo_incidencias_p));
        } else {
            item.setBackground(context.getResources().getDrawable(R.drawable.fondo_incidencias_i));
        }

        // Escribimos los datos.
        codigo.setText((cod > 9) ? String.valueOf(cod) : "0" + String.valueOf(cod));
        incidencia.setText(inc);

    }
}
