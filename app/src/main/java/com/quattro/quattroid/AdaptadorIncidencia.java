/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattro.quattroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import BaseDatos.BaseDatos;
import BaseDatos.Incidencia;
import Objetos.Colores;

public class AdaptadorIncidencia extends ArrayAdapter<Incidencia> {

    // Variables
    private Context context;
    private ArrayList<Incidencia> listaIncidencias;


    public AdaptadorIncidencia(@NonNull Context context, ArrayList<Incidencia> lista) {
        super(context, 0, lista);
        this.context = context;
        this.listaIncidencias = lista;
    }

    // Métodos públicos
    public int getCount() {
        return listaIncidencias.size();
    }

    public Incidencia getItem(int position) {
        return listaIncidencias.get(position);
    }

    public long getItemId(Incidencia item) {
        return listaIncidencias.indexOf(item);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        final AdaptadorIncidencia.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_incidencia2, null);
            holder = new AdaptadorIncidencia.ViewHolder();
            holder.Item = view.findViewById(R.id.item);
            holder.Codigo = view.findViewById(R.id.codigo);
            holder.Incidencia = view.findViewById(R.id.incidencia);
            view.setTag(holder);
        } else {
            holder = (AdaptadorIncidencia.ViewHolder) view.getTag();
        }

        // Instanciamos la base de datos
        BaseDatos datos = new BaseDatos(context);

        // Extraemos el relevo de la lista
        Incidencia incidencia = listaIncidencias.get(position);

        // Borrado de los textos anteriores.
        holder.Codigo.setText("");
        holder.Incidencia.setText("");

        // Color del fondo
        if (incidencia.isSeleccionada()) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
        } else if ((position + 1) % 2 == 0) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_incidencia_par));
        } else {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_incidencia_impar));
        }

        // Si la incidencia es protegida, se pone el código en rojo.
        if (incidencia.getCodigo() > 16) {
            holder.Codigo.setTextColor(Colores.NEGRO);
        } else {
            holder.Codigo.setTextColor(Colores.ROJO_OSCURO);
        }

        // Escribimos los datos.
        holder.Codigo.setText((incidencia.getCodigo() > 9) ? String.valueOf(incidencia.getCodigo()) : "0" + String.valueOf(incidencia.getCodigo()));
        holder.Incidencia.setText(incidencia.getTexto());

        return view;
    }


    public static class ViewHolder {
        LinearLayout Item;
        TextView Codigo;
        TextView Incidencia;
    }


}
