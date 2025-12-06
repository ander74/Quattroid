/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quattroid.Activities.R;

import java.util.ArrayList;

import BaseDatos.BaseDatos;
import BaseDatos.HoraAjena;
import Objetos.Colores;
import Objetos.Hora;

public class AdaptadorHorasAjenas extends ArrayAdapter<HoraAjena> {

    // Variables
    private Context context;
    private ArrayList<HoraAjena> listaAjenas;


    public AdaptadorHorasAjenas(@NonNull Context context, ArrayList<HoraAjena> lista) {
        super(context, 0, lista);
        this.context = context;
        this.listaAjenas = lista;
    }

    // Métodos públicos
    public int getCount() {
        return listaAjenas.size();
    }

    public HoraAjena getItem(int position) {
        return listaAjenas.get(position);
    }

    public long getItemId(HoraAjena item) {
        return listaAjenas.indexOf(item);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        final AdaptadorHorasAjenas.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_horaajena, null);
            holder = new AdaptadorHorasAjenas.ViewHolder();
            holder.Dia = view.findViewById(R.id.tv_dia);
            holder.Horas = view.findViewById(R.id.tv_horas);
            holder.Motivo = view.findViewById(R.id.tv_motivo);
            holder.Item = view.findViewById(R.id.item);
            view.setTag(holder);
        } else {
            holder = (AdaptadorHorasAjenas.ViewHolder) view.getTag();
        }

        // Instanciamos la base de datos
        BaseDatos datos = new BaseDatos(context);

        // Extraemos el relevo de la lista
        HoraAjena horaAjena = listaAjenas.get(position);

        // Borrado de los textos anteriores.
        holder.Dia.setText("");
        holder.Horas.setText("");
        holder.Motivo.setText("");

        String fechaActual = ((horaAjena.getDia() > 9) ? String.valueOf(horaAjena.getDia()) : "0" + String.valueOf(horaAjena.getDia()))
                + " de " + Hora.MESES_MIN[horaAjena.getMes()] + " de " + String.valueOf(horaAjena.getAño());
        holder.Dia.setText(fechaActual);

        // Color del fondo
        if (horaAjena.isSeleccionada()) {
            holder.Dia.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
        } else {
            holder.Dia.setBackground(context.getResources().getDrawable(R.drawable.fondo_ajenas_titulo));
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_ajenas_contenido));
        }

        // Escribimos los datos.

        // Color del texto
        if (horaAjena.getHoras() < 0) {
            holder.Horas.setTextColor(Colores.ROJO);
        } else if (horaAjena.getHoras() > 0) {
            holder.Horas.setTextColor(Colores.VERDE_OSCURO);
        } else {
            holder.Horas.setTextColor(Colores.AZUL_OSCURO);
        }

        // Escribimos los datos.
        holder.Horas.setText(Hora.textoDecimal(horaAjena.getHoras()));
        holder.Motivo.setText(horaAjena.getMotivo());


        return view;
    }


    public static class ViewHolder {
        LinearLayout Item;
        TextView Dia;
        TextView Horas;
        TextView Motivo;
    }


}
