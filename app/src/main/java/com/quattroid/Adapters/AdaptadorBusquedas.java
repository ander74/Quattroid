/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.Adapters;

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

import com.quattroid.Activities.R;

import Objetos.Colores;
import Objetos.Hora;

public class AdaptadorBusquedas extends CursorAdapter {

    // CONSTANTES
    private static final int DESCONOCIDO = 0;
    private static final int CABECERA = 1;
    private static final int CUERPO = 2;

    // VARIABLES
    LayoutInflater inflater = null;
    private int[] estadoFila;

    public AdaptadorBusquedas(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        estadoFila = c == null ? null : new int[c.getCount()];
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        estadoFila = cursor == null ? null : new int[cursor.getCount()];
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_busquedas_2, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Instancias de los elementos del item.
        LinearLayout Item = view.findViewById(R.id.item);
        RelativeLayout TextoResto = view.findViewById(R.id.resto);
        LinearLayout EsFranqueo = view.findViewById(R.id.esFranqueo);
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
        TextView CabeceraMes = view.findViewById(R.id.cabeceraMes);


        // Ocultar todas las imágenes.
        EsHuelga.setVisibility(View.GONE);
        HayCompañero.setVisibility(View.GONE);
        Notas.setVisibility(View.GONE);
        Desayuno.setVisibility(View.GONE);
        Comida.setVisibility(View.GONE);
        Cena.setVisibility(View.GONE);


        // Decide si se necesita una cabecera o no.
        boolean esCabecera = false;
        final int pos = cursor.getPosition();
        String fechaActual = Hora.MESES_MIN[cursor.getInt(cursor.getColumnIndexOrThrow("Mes"))] + " - " +
                cursor.getInt(cursor.getColumnIndexOrThrow("Año"));
        String fechaAnterior = "";

        switch (estadoFila[pos]) {
            case CABECERA:
                esCabecera = true;
                break;
            case CUERPO:
                esCabecera = false;
                break;
            case DESCONOCIDO:
                if (pos == 0) {
                    esCabecera = true;
                } else {
                    cursor.moveToPosition(pos - 1);
                    fechaAnterior = Hora.MESES_MIN[cursor.getInt(cursor.getColumnIndexOrThrow("Mes"))] + " - " +
                            cursor.getInt(cursor.getColumnIndexOrThrow("Año"));
                    if (!fechaActual.equals(fechaAnterior)) {
                        esCabecera = true;
                    }
                    cursor.moveToPosition(pos);
                }
                estadoFila[pos] = esCabecera ? CABECERA : CUERPO;
                break;
        }
        // Muestra la cabecera si se necesita
        if (esCabecera) {
            CabeceraMes.setText(fechaActual);
            CabeceraMes.setVisibility(View.VISIBLE);
        } else {
            CabeceraMes.setVisibility(View.GONE);
        }

        // Establece los fondos.
        Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_blanco_r));
        //Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_blanco_r));
        if ((cursor.getPosition() + 1) % 2 == 0) {
            Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_par));
            EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_par));
        } else {
            Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_impar));
            EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_impar));
        }

        // Variables a usar.
        String s = "";
        int i = 0;

        // Rellenamos el dia
        int Dsemana = cursor.getInt(cursor.getColumnIndexOrThrow("DiaSemana"));
        i = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"));
        s = (i > 9) ? String.valueOf(i) : "0" + String.valueOf(i);
        Dia.setText(s);
        DiaSemana.setText(Hora.DIAS_SEMANA_ABREV[Dsemana]);

        // Color si es un Franqueo o un Festivo
        Dia.setTextColor(Colores.NEGRO);
        DiaSemana.setTextColor(Colores.NEGRO);

        if (cursor.getInt(cursor.getColumnIndexOrThrow("EsFranqueo")) == 1) {
            EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_franqueo));
        }
        if (cursor.getInt(cursor.getColumnIndexOrThrow("EsFestivo")) == 1) {
            EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_festivo));
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
        switch (tipo) {
            case 1:
            case 2:
            case 5:
                String ser = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"));
                String lin = cursor.getString(cursor.getColumnIndexOrThrow("Linea"));
                String tex = cursor.getString(cursor.getColumnIndexOrThrow("TextoLinea"));
                if (ser == null) ser = "";
                if (lin == null) lin = "";
                if (tex == null) tex = "";
                i = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"));
                if (!ser.trim().equals("") && !lin.trim().equals("") && !tex.trim().equals("") && i != 0) {
                    s = ser + "/" + String.valueOf(i) + "-" + lin + ": " + tex;
                } else {
                    if (i != 0 && !ini.trim().equals("") && !fin.trim().equals("")) {
                        s = cursor.getString(cursor.getColumnIndexOrThrow("TextoIncidencia")) +
                                " " + String.valueOf(i);
                    } else {
                        s = "";
                    }
                }

                Servicio.setText(s);
                break;
            case 3:
            case 4:
            case 6:
                String inc = cursor.getString(cursor.getColumnIndexOrThrow("TextoIncidencia"));
                i = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"));
                if (i != 0) inc = inc + " " + i;
                Servicio.setText(inc);
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
        Horario.setTextColor(Colores.GRIS_OSCURO);
        if (!ini.trim().equals("") && !fin.trim().equals("")) {
            s = ini + " - " + fin;
        } else {
            s = "";
        }
        Horario.setText(s);

        // Rellenamos las horas acumuladas y nocturnas, si la incidencia es del tipo 1, 2 o 3
        Nocturnas.setText("");
        Acumuladas.setText("");
        Guion.setVisibility(View.GONE);
        if (!s.equals("")) {
            if (tipo > 0 && tipo < 4 && cursor.getInt(cursor.getColumnIndexOrThrow("Turno")) != 0) {
                Nocturnas.setText(Hora.textoDecimal(cursor.getDouble(cursor.getColumnIndexOrThrow("Nocturnas"))));
                double acum = cursor.getDouble(cursor.getColumnIndexOrThrow("Acumuladas"));
                if (acum < 0) {
                    Acumuladas.setTextColor(Colores.ROJO);
                } else {
                    Acumuladas.setTextColor(Colores.VERDE_OSCURO);
                }
                Acumuladas.setText(Hora.textoDecimal(acum));
                Guion.setVisibility(View.VISIBLE);
            } else {
                Nocturnas.setText("");
                Acumuladas.setText("");
                Guion.setVisibility(View.GONE);
            }
        } else {
            if (tipo == 3) {
                Nocturnas.setText(Hora.textoDecimal(cursor.getDouble(cursor.getColumnIndexOrThrow("Nocturnas"))));
                double acum = cursor.getDouble(cursor.getColumnIndexOrThrow("Acumuladas"));
                if (acum < 0) {
                    Acumuladas.setTextColor(Colores.ROJO);
                } else {
                    Acumuladas.setTextColor(Colores.VERDE_OSCURO);
                }
                Acumuladas.setText(Hora.textoDecimal(acum));
                Guion.setVisibility(View.VISIBLE);
            }
        }

        // Activar icono si hay un compañero que nos hace o hacemos el dia.
        i = cursor.getInt(cursor.getColumnIndexOrThrow("CodigoIncidencia"));
        switch (i) {
            case 11:
                HayCompañero.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_usuario_rojo));
                HayCompañero.setVisibility(View.VISIBLE);
                break;
            case 12:
                HayCompañero.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_usuario_verde));
                HayCompañero.setVisibility(View.VISIBLE);
                break;
            case 15:
                EsHuelga.setVisibility(View.VISIBLE);
                break;
            default:
                HayCompañero.setVisibility(View.GONE);
                EsHuelga.setVisibility(View.GONE);
        }

        // Activar icono de las dietas.
        i = cursor.getInt(cursor.getColumnIndexOrThrow("Desayuno"));
        if (i > 0) Desayuno.setVisibility(View.VISIBLE);
        i = cursor.getInt(cursor.getColumnIndexOrThrow("Comida"));
        if (i > 0) Comida.setVisibility(View.VISIBLE);
        i = cursor.getInt(cursor.getColumnIndexOrThrow("Cena"));
        if (i > 0) Cena.setVisibility(View.VISIBLE);

        // Activar el icono de la calificación del relevo
        i = cursor.getInt(cursor.getColumnIndexOrThrow("Calificacion"));
        switch (i) {
            case 1:
                Calificacion.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_buen_relevo));
                Calificacion.setVisibility(View.VISIBLE);
                break;
            case 2:
                Calificacion.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_mal_relevo));
                Calificacion.setVisibility(View.VISIBLE);
                break;
            default:
                Calificacion.setVisibility(View.GONE);
        }
    }

}
