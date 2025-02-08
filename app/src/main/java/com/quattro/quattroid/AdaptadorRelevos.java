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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import BaseDatos.BaseDatos;
import Objetos.Colores;

public class AdaptadorRelevos extends CursorAdapter {

    LayoutInflater inflater = null;

    public AdaptadorRelevos(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_relevo2, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Instancias de los elementos del item.
        LinearLayout item = view.findViewById(R.id.item);
        TextView matricula = view.findViewById(R.id.tv_matricula);
        TextView nombre = view.findViewById(R.id.tv_nombre);
        TextView apellidos = view.findViewById(R.id.tv_apellidos);
        TextView deuda = view.findViewById(R.id.tv_deuda);
        ImageView calificacion = view.findViewById(R.id.im_calificacion);

        // Instanciamos la base de datos
        BaseDatos datos = new BaseDatos(context);

        // Borrado de los textos anteriores.
        matricula.setText("");
        nombre.setText("");
        apellidos.setText("");
        deuda.setText("");

        // Extraemos los datos del cursor.
        int c = cursor.getInt(cursor.getColumnIndexOrThrow("Calificacion"));
        int m = cursor.getInt(cursor.getColumnIndexOrThrow("Matricula"));
        String n = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
        String a = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"));
        String t = cursor.getString(cursor.getColumnIndexOrThrow("Telefono"));

        // Extraemos la deuda de la base de datos
        int d = datos.deudaRelevo(m);

        // Color del fondo
        if ((cursor.getPosition() + 1) % 2 == 0) {
            item.setBackground(context.getResources().getDrawable(R.drawable.fondo_relevo_par));
        } else {
            item.setBackground(context.getResources().getDrawable(R.drawable.fondo_relevo_impar));
        }

        // Mostramos la calificacion
        switch (c){
            case 1:
                calificacion.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_buen_relevo));
                calificacion.setVisibility(View.VISIBLE);
                break;
            case 2:
                calificacion.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_mal_relevo));
                calificacion.setVisibility(View.VISIBLE);
                break;
            default:
                calificacion.setVisibility(View.GONE);
        }

        // Escribimos los datos.
        matricula.setText((m == 0) ? "" : String.valueOf(m));
        nombre.setText(n);
        apellidos.setText(a);

        // Escribimos la deuda
        String s = "";
        switch (d){
            case 0:
                s = "";
                break;
            case 1:
                s = "Me debe un día.";
                deuda.setTextColor(Colores.VERDE_OSCURO);
                break;
            case -1:
                s = "Le debo un día.";
                deuda.setTextColor(Colores.ROJO_OSCURO);
                break;
            default:
                if (d > 1){
                    s = "Me debe " + String.valueOf(d) + " días.";
                    deuda.setTextColor(Colores.VERDE_OSCURO);
                }
                if (d < -1){
                    d = d * -1;
                    s = "Le debo " + String.valueOf(d) + " días.";
                    deuda.setTextColor(Colores.ROJO_OSCURO);
                }
        }
        deuda.setText(s);
    }


}
