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
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdaptadorLinea extends CursorAdapter {

    LayoutInflater inflater = null;

    public AdaptadorLinea(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_linea2, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Instancias de los elementos del item.
        LinearLayout item = view.findViewById(R.id.item);
        TextView linea = view.findViewById(R.id.tv_linea);
        TextView texto = view.findViewById(R.id.tv_texto);

        // Borrado de los textos anteriores.
        linea.setText("");
        texto.setText("");

        // Color del fondo
        if ((cursor.getPosition() + 1) % 2 == 0) {
            item.setBackground(context.getResources().getDrawable(R.drawable.fondo_linea_par));
        } else {
            item.setBackground(context.getResources().getDrawable(R.drawable.fondo_linea_impar));
        }

        // Escribimos los datos.
        linea.setText(cursor.getString(cursor.getColumnIndexOrThrow("Linea")));
        texto.setText(cursor.getString(cursor.getColumnIndexOrThrow("Texto")));

    }
}
