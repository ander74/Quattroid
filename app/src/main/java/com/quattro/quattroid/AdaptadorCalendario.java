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
import android.widget.RelativeLayout;
import android.widget.TextView;

import Objetos.Colores;
import Objetos.Hora;

public class AdaptadorCalendario extends CursorAdapter {

    // VARIABLES
    LayoutInflater inflater = null;


    public AdaptadorCalendario(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_calendario, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Instancias de los elementos del item.
        LinearLayout Item = view.findViewById(R.id.item);
        RelativeLayout TextoResto = view.findViewById(R.id.resto);
        RelativeLayout TextoDia = view.findViewById(R.id.textoDia);
        TextView Dia = view.findViewById(R.id.dia);
        TextView DiaSemana = view.findViewById(R.id.diaSemana);
        ImageView EsHuelga = view.findViewById(R.id.esHuelga);
        ImageView HayCompañero = view.findViewById(R.id.hayCompañero);
        ImageView Notas = view.findViewById(R.id.notas);
        TextView Servicio = view.findViewById(R.id.servicio);
        ImageView Desayuno = view.findViewById(R.id.desayuno);
        ImageView Comida = view.findViewById(R.id.comida);
        ImageView Cena = view.findViewById(R.id.cena);
        TextView Relevo = view.findViewById(R.id.relevo);
        ImageView Calificacion = view.findViewById(R.id.calificacion);
        TextView Horario = view.findViewById(R.id.horario);
        TextView Nocturnas = view.findViewById(R.id.nocturnas);
        TextView Guion = view.findViewById(R.id.guion);
        TextView Acumuladas = view.findViewById(R.id.acumuladas);
        LinearLayout SeparadorUltimoElemento = view.findViewById(R.id.separadorUltimoElemento);

        // Ocultar todas las imágenes.
        EsHuelga.setVisibility(View.GONE);
        HayCompañero.setVisibility(View.GONE);
        Notas.setVisibility(View.GONE);
        Desayuno.setVisibility(View.GONE);
        Comida.setVisibility(View.GONE);
        Cena.setVisibility(View.GONE);

        // Establece los fondos.
        Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_blanco_r));

        if (cursor.getInt(cursor.getColumnIndexOrThrow("Dia")) % 2 == 0) {
            TextoDia.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_pbr));
            TextoResto.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_pbr));
            Horario.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_p));
            Nocturnas.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_p));
            Guion.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_p));
            Acumuladas.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_p));
        } else {
            TextoDia.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_ibr));
            TextoResto.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_ibr));
            Horario.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_i));
            Nocturnas.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_i));
            Guion.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_i));
            Acumuladas.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_i));
        }

        // Variables a usar.
        String s = "";
        int dia = 0;
        int mes = 0;
        int año = 0;

        // Rellenamos el dia
        int Dsemana = cursor.getInt(cursor.getColumnIndexOrThrow("DiaSemana"));
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"));
        mes = cursor.getInt(cursor.getColumnIndexOrThrow("Mes"));
        año = cursor.getInt(cursor.getColumnIndexOrThrow("Año"));
        s = (dia > 9) ? String.valueOf(dia) : "0" + String.valueOf(dia);
        Dia.setText(s);
        DiaSemana.setText(Hora.DIAS_SEMANA_ABREV[Dsemana]);

        // Si es el último día del mes, se activa el separador.
//        DateTime fecha = new DateTime(año, mes, dia, 0, 0);
//        int diasMes = fecha.dayOfMonth().getMaximumValue();
//        if (dia == diasMes) {
//            SeparadorUltimoElemento.setVisibility(View.VISIBLE);
//        } else{
//            SeparadorUltimoElemento.setVisibility(View.GONE);
//        }

        // Color si es un Franqueo o un Festivo
        Dia.setTextColor(Colores.NEGRO);
        DiaSemana.setTextColor(Colores.NEGRO);

        if (cursor.getInt(cursor.getColumnIndexOrThrow("EsFranqueo")) == 1) {
            Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_azulclaro_r));
        }
        if (Dsemana == 1 || cursor.getInt(cursor.getColumnIndexOrThrow("EsFestivo")) == 1) {
            Dia.setTextColor(Colores.ROJO);
            DiaSemana.setTextColor(Colores.ROJO);
        }

        // Rellenamos el servicio
        Servicio.setText("");
        int tipo = cursor.getInt(cursor.getColumnIndexOrThrow("TipoIncidencia"));
        String ini = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"));
        String fin = cursor.getString(cursor.getColumnIndexOrThrow("Final"));
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"));
        switch (tipo) {
            case 1:case 2:case 5:
                String ser = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"));
                String lin = cursor.getString(cursor.getColumnIndexOrThrow("Linea"));
                String tex = cursor.getString(cursor.getColumnIndexOrThrow("TextoLinea"));
                if (ser == null) ser = "";
                if (lin == null) lin = "";
                if (tex == null) tex = "";
                if (!ser.trim().equals("") && !lin.trim().equals("") && !tex.trim().equals("") && dia != 0) {
                    s = ser + "/" + dia + "-" + lin + ": " + tex;
                } else {
                    if (dia != 0 && !ini.trim().equals("") && !fin.trim().equals("")) {
                        s = cursor.getString(cursor.getColumnIndexOrThrow("TextoIncidencia")) +
                                " " + dia;
                    } else {
                        s = "";
                        if (dia != 0) s = "Turno " + dia;
                    }
                }

                Servicio.setText(s);
                break;
            case 3:case 4:case 6:
                String inc = cursor.getString(cursor.getColumnIndexOrThrow("TextoIncidencia"));
                if (dia > 0){
                    Servicio.setText(inc + " " + dia);
                } else{
                    Servicio.setText(inc + " ");
                }
                break;
            default:
                if (dia > 0){
                    Servicio.setText("Turno " + dia);
                } else {
                    Servicio.setText("Sin turno");
                }
                break;
        }

        // Rellenamos el relevo
        Relevo.setText("");
        int mat = cursor.getInt(cursor.getColumnIndexOrThrow("Matricula"));
        String ape = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"));
        if (mat != 0 && ape != null) {
            s = String.valueOf(mat) + ": " + ape;
        } else {
            s = "";
        }
        Relevo.setText(s);

        // Rellenamos el horario
        Horario.setText("");
        if (!ini.trim().equals("") && !fin.trim().equals("")) {
            s = ini + " - " + fin;
        } else {
            s = "";
        }
        Horario.setText(s);

        // Rellenamos las horas acumuladas y nocturnas, si la incidencia es del tipo 1, 2 o 3
        Nocturnas.setText("");
        Acumuladas.setText("");
        double hacum = cursor.getDouble(cursor.getColumnIndexOrThrow("Acumuladas"));
        double hnoct = cursor.getDouble(cursor.getColumnIndexOrThrow("Nocturnas"));
        Guion.setVisibility(View.GONE);
        if (!s.equals("")) {
            if (tipo > 0 && tipo < 4 && cursor.getInt(cursor.getColumnIndexOrThrow("Turno")) != 0) {
                Nocturnas.setText(Hora.textoDecimal(hnoct));
                if (hacum > -0.01) {
                    Acumuladas.setTextColor(Colores.VERDE_OSCURO);
                } else {
                    Acumuladas.setTextColor(Colores.ROJO);
                }
                Acumuladas.setText(Hora.textoDecimal(hacum));
                Guion.setVisibility(View.VISIBLE);
            } else {
                Nocturnas.setText("");
                Acumuladas.setText("");
                Guion.setVisibility(View.GONE);
            }
        } else {
            if (tipo == 3 || hacum != 0 || hnoct != 0){
                Nocturnas.setText(Hora.textoDecimal(hnoct));
                if (hacum < 0) {
                    Acumuladas.setTextColor(Colores.ROJO);
                } else {
                    Acumuladas.setTextColor(Colores.VERDE_OSCURO);
                }
                Acumuladas.setText(Hora.textoDecimal(hacum));
                Guion.setVisibility(View.VISIBLE);
            }
        }

        // Activar icono si hay un compañero que nos hace o hacemos el dia o si hay huelga.
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("CodigoIncidencia"));
        switch (dia){
            case 11:
                HayCompañero.setImageDrawable(context.getResources().getDrawable(R.drawable.usuario_rojo));
                HayCompañero.setVisibility(View.VISIBLE);
                break;
            case 12:
                HayCompañero.setImageDrawable(context.getResources().getDrawable(R.drawable.usuario_verde));
                HayCompañero.setVisibility(View.VISIBLE);
                break;
            case 15:
                EsHuelga.setVisibility(View.VISIBLE);
                break;
            default:
                HayCompañero.setVisibility(View.GONE);
                EsHuelga.setVisibility(View.GONE);
        }

        // Activar icono si las notas tienen algo escrito
        if (!cursor.getString(cursor.getColumnIndexOrThrow("Notas")).trim().equals("")) Notas.setVisibility(View.VISIBLE);

        // Activar icono de las dietas.
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Desayuno"));
        if (dia > 0) Desayuno.setVisibility(View.VISIBLE);
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Comida"));
        if (dia > 0) Comida.setVisibility(View.VISIBLE);
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Cena"));
        if (dia > 0) Cena.setVisibility(View.VISIBLE);

        // Activar el icono de la calificación del relevo
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Calificacion"));
        switch (dia){
            case 1:
                Calificacion.setImageDrawable(context.getResources().getDrawable(R.drawable.buenrelevo));
                Calificacion.setVisibility(View.VISIBLE);
                break;
            case 2:
                Calificacion.setImageDrawable(context.getResources().getDrawable(R.drawable.malrelevo));
                Calificacion.setVisibility(View.VISIBLE);
                break;
            default:
                Calificacion.setVisibility(View.GONE);
        }
    }

}


