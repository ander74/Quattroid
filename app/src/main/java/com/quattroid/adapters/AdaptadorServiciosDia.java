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

package com.quattroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quattroid.activities.R;

import java.util.ArrayList;

import BaseDatos.ServicioDia;

public class AdaptadorServiciosDia extends ArrayAdapter<ServicioDia> {

    // Variables
    private Context context;
    private ArrayList<ServicioDia> listaServicios;


    public AdaptadorServiciosDia(@NonNull Context context, ArrayList<ServicioDia> lista) {
        super(context, 0, lista);
        this.context = context;
        this.listaServicios = lista;
    }

    // Métodos públicos
    public int getCount() {
        return listaServicios.size();
    }

    public ServicioDia getItem(int position) {
        return listaServicios.get(position);
    }

    public long getItemId(ServicioDia item) {
        return listaServicios.indexOf(item);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        final AdaptadorServiciosDia.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_serviciodia, null);
            holder = new AdaptadorServiciosDia.ViewHolder();
            holder.Cabecera = view.findViewById(R.id.ly_cabecera);
            holder.Item = view.findViewById(R.id.ly_item);
            holder.Servicio = view.findViewById(R.id.tv_servicio);
            holder.Turno = view.findViewById(R.id.tv_turno);
            holder.Linea = view.findViewById(R.id.tv_linea);
            holder.Inicio = view.findViewById(R.id.tv_inicio);
            holder.Fin = view.findViewById(R.id.tv_final);
            view.setTag(holder);
        } else {
            holder = (AdaptadorServiciosDia.ViewHolder) view.getTag();
        }

        // Extraemos el servicio de la lista
        ServicioDia servicio = listaServicios.get(position);

        // Borrado de los textos anteriores.
        holder.Servicio.setText("");
        holder.Turno.setText("");
        holder.Linea.setText("");
        holder.Inicio.setText("");
        holder.Fin.setText("");

        // Determinamos si se muestra la cabecera
        if (position == 0) {
            holder.Cabecera.setVisibility(View.VISIBLE);
        } else {
            holder.Cabecera.setVisibility(View.GONE);
        }

        // Color del fondo
        if (servicio.isSeleccionado()) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
        } else if ((position + 1) % 2 == 0) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_relevo_par));
        } else {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_relevo_impar));
        }

        // Escribimos los datos.
        holder.Servicio.setText(servicio.getServicio());
        holder.Turno.setText(String.valueOf(servicio.getTurno()));
        holder.Linea.setText(String.valueOf(servicio.getLinea()));
        holder.Inicio.setText(servicio.getInicio());
        holder.Fin.setText(servicio.getFinal());

        return view;
    }


    public static class ViewHolder {
        LinearLayout Item;
        TextView Servicio;
        TextView Turno;
        TextView Linea;
        TextView Inicio;
        TextView Fin;
        LinearLayout Cabecera;
    }


}
