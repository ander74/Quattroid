/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quattroid.Activities.R;

import java.util.ArrayList;

import BaseDatos.Estadistica;
import Objetos.Calculos;

public class AdaptadorEstadisticas extends ArrayAdapter<Estadistica> {

    ViewHolder viewHolder;
    private final int pad4 = Calculos.ConvierteDpEnPx(4, getContext());
    private final int pad16 = Calculos.ConvierteDpEnPx(16, getContext());

    private static class ViewHolder {
        private TextView estadistica;
        private TextView valor;
        private LinearLayout fila;
    }

    public AdaptadorEstadisticas(Context context, ArrayList<Estadistica> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Estadistica e = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_estadistica, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.estadistica = convertView.findViewById(R.id.tv_estadistica);
            viewHolder.valor = convertView.findViewById(R.id.tv_valor);
            viewHolder.fila = convertView.findViewById(R.id.ly_fila);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Ponemos a cero los campos
        viewHolder.estadistica.setText("");
        viewHolder.valor.setText("");

        // Si es una cabecera, la mostramos, sino mostramos la incidencia
        if (e.Tipo == 0) {
            // Establecemos el estilo de la estadistica.
            if ((e.Contador) % 2 == 0) {
                viewHolder.fila.setBackground(getContext().getResources().getDrawable(R.drawable.fondo_estadisticas_p));
            } else {
                viewHolder.fila.setBackground(getContext().getResources().getDrawable(R.drawable.fondo_estadisticas_i));
            }
            // Rellenamos los valores
            viewHolder.estadistica.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            viewHolder.estadistica.setTypeface(null, Typeface.NORMAL);
            viewHolder.estadistica.setText(e.Texto);
            viewHolder.fila.setPadding(pad16, pad4, pad4, pad4);
            viewHolder.valor.setText(e.Valor);
        } else {
            viewHolder.estadistica.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            viewHolder.estadistica.setTypeface(null, Typeface.BOLD);
            viewHolder.fila.setBackground(getContext().getResources().getDrawable(R.drawable.fondo_transparente));
            viewHolder.fila.setPadding(pad4, pad16, 0, 0);
            viewHolder.estadistica.setText(e.Texto);
        }

        return convertView;
    }
}
