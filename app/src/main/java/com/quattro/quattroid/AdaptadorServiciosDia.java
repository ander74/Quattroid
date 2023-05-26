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

public class AdaptadorServiciosDia extends CursorAdapter {

    LayoutInflater inflater = null;

    public AdaptadorServiciosDia(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_serviciodia, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Instancias de los elementos del item.
        TextView servicio = view.findViewById(R.id.tv_servicio);
        TextView turno = view.findViewById(R.id.tv_turno);
        TextView linea = view.findViewById(R.id.tv_linea);
        TextView inicio = view.findViewById(R.id.tv_inicio);
        TextView fin = view.findViewById(R.id.tv_final);
        LinearLayout item = view.findViewById(R.id.ly_item);
        LinearLayout cabecera = view.findViewById(R.id.ly_cabecera);

        // Borrado de los textos anteriores.
        servicio.setText("");
        turno.setText("");
        linea.setText("");
        inicio.setText("");
        fin.setText("");

        // Mostrar cabecera si el cursor está en la posición 0
        if (cursor.getPosition() == 0){
            cabecera.setVisibility(View.VISIBLE);
        } else {
            cabecera.setVisibility(View.GONE);
        }

        // Color del fondo
        int cod = cursor.getPosition() + 1;
        if (cod % 2 == 0) {
            item.setBackground(context.getResources().getDrawable(R.drawable.fondo_serviciosdia_p));
        } else {
            item.setBackground(context.getResources().getDrawable(R.drawable.fondo_serviciosdia_i));
        }

        // Escribimos los datos.
        servicio.setText(cursor.getString(cursor.getColumnIndexOrThrow("Servicio")));
        turno.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("Turno"))));
        linea.setText(cursor.getString(cursor.getColumnIndexOrThrow("Linea")));
        inicio.setText(cursor.getString(cursor.getColumnIndexOrThrow("Inicio")));
        fin.setText(cursor.getString(cursor.getColumnIndexOrThrow("Final")));
    }
}
