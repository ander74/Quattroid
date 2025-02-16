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

import com.quattro.models.LineaModel;

import java.util.ArrayList;

import BaseDatos.BaseDatos;

public class AdaptadorLinea extends ArrayAdapter<LineaModel> {

    // Variables
    private Context context;
    private ArrayList<LineaModel> listaLineas;


    public AdaptadorLinea(@NonNull Context context, ArrayList<LineaModel> lista) {
        super(context, 0, lista);
        this.context = context;
        this.listaLineas = lista;
    }

    // Métodos públicos
    public int getCount() {
        return listaLineas.size();
    }
    public LineaModel getItem(int position) {
        return listaLineas.get(position);
    }
    public long getItemId(LineaModel item) {
        return listaLineas.indexOf(item);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        final AdaptadorLinea.ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_linea2, null);
            holder = new AdaptadorLinea.ViewHolder();
            holder.Item = view.findViewById(R.id.item);
            holder.Linea = view.findViewById(R.id.tv_linea);
            holder.Texto = view.findViewById(R.id.tv_texto);
            view.setTag(holder);
        } else {
            holder = (AdaptadorLinea.ViewHolder) view.getTag();
        }

        // Instanciamos la base de datos
        BaseDatos datos = new BaseDatos(context);

        // Extraemos el relevo de la lista
        LineaModel linea = listaLineas.get(position);

        // Borrado de los textos anteriores.
        holder.Linea.setText("");
        holder.Texto.setText("");

        // Color del fondo
        if(linea.isSeleccionada()){
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
        } else if ((position + 1) % 2 == 0) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_linea_par));
        } else {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_linea_impar));
        }

        // Escribimos los datos.
        holder.Linea.setText(linea.getLinea());
        holder.Texto.setText(linea.getTexto());

        return view;
    }



    public static class ViewHolder{
        LinearLayout Item;
        TextView Linea;
        TextView Texto;
    }

}
