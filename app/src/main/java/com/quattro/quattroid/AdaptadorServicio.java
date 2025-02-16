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

import com.quattro.models.ServicioModel;

import java.util.ArrayList;

public class AdaptadorServicio extends ArrayAdapter<ServicioModel> {

    // Variables
    private Context context;
    private ArrayList<ServicioModel> listaServicios;


    public AdaptadorServicio(@NonNull Context context, ArrayList<ServicioModel> lista) {
        super(context, 0, lista);
        this.context = context;
        this.listaServicios = lista;
    }

    // Métodos públicos
    public int getCount() {
        return listaServicios.size();
    }
    public ServicioModel getItem(int position) {
        return listaServicios.get(position);
    }
    public long getItemId(ServicioModel item) {
        return listaServicios.indexOf(item);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        final AdaptadorServicio.ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_servicio, null);
            holder = new AdaptadorServicio.ViewHolder();
            holder.Cabecera = view.findViewById(R.id.ly_cabecera);
            holder.Item = view.findViewById(R.id.ly_item);
            holder.Servicio = view.findViewById(R.id.tv_servicio);
            holder.Turno = view.findViewById(R.id.tv_turno);
            holder.Inicio = view.findViewById(R.id.tv_inicio);
            holder.Fin = view.findViewById(R.id.tv_final);
            view.setTag(holder);
        } else {
            holder = (AdaptadorServicio.ViewHolder) view.getTag();
        }

        // Extraemos el servicio de la lista
        ServicioModel servicio = listaServicios.get(position);

        // Borrado de los textos anteriores.
        holder.Servicio.setText("");
        holder.Turno.setText("");
        holder.Inicio.setText("");
        holder.Fin.setText("");

        // Determinamos si se muestra la cabecera
        if (position == 0){
            holder.Cabecera.setVisibility(View.VISIBLE);
        } else {
            holder.Cabecera.setVisibility(View.GONE);
        }

        // Color del fondo
        if(servicio.isSeleccionado()){
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
        } else if ((position + 1) % 2 == 0) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_servicio_p));
        } else {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_servicio_i));
        }

        // Escribimos los datos.
        holder.Servicio.setText(servicio.getServicio());
        holder.Turno.setText(String.valueOf(servicio.getTurno()));
        holder.Inicio.setText(servicio.getInicio());
        holder.Fin.setText(servicio.getFinal());

        return view;
    }


    public static class ViewHolder{
        LinearLayout Item;
        TextView Servicio;
        TextView Turno;
        TextView Inicio;
        TextView Fin;
        LinearLayout Cabecera;
    }



}
