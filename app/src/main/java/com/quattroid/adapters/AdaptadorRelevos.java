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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quattroid.activities.R;
import com.quattroid.helpers.Colores;

import java.util.ArrayList;

import BaseDatos.BaseDatos;
import BaseDatos.Relevo;

public class AdaptadorRelevos extends ArrayAdapter<Relevo> {

    // Variables
    private Context context;
    private ArrayList<Relevo> listaRelevos;


    // Constructor
    public AdaptadorRelevos(@NonNull Context context, ArrayList<Relevo> lista) {
        super(context, 0, lista);
        this.context = context;
        this.listaRelevos = lista;
    }

    // Métodos públicos
    public int getCount() {
        return listaRelevos.size();
    }

    public Relevo getItem(int position) {
        return listaRelevos.get(position);
    }

    public long getItemId(Relevo item) {
        return listaRelevos.indexOf(item);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_relevo2, null);
            holder = new ViewHolder();
            holder.Item = view.findViewById(R.id.item);
            holder.Matricula = view.findViewById(R.id.tv_matricula);
            holder.Nombre = view.findViewById(R.id.tv_nombre);
            holder.Apellidos = view.findViewById(R.id.tv_apellidos);
            holder.Deuda = view.findViewById(R.id.tv_deuda);
            holder.Calificacion = view.findViewById(R.id.im_calificacion);
            holder.IconoLlamar = view.findViewById(R.id.iconoLlamar);
            view.setTag(holder);
        } else {
            holder = (AdaptadorRelevos.ViewHolder) view.getTag();
        }

        // Instanciamos la base de datos
        BaseDatos datos = new BaseDatos(context);

        // Extraemos el relevo de la lista
        Relevo relevo = listaRelevos.get(position);

        // Extraemos la deuda de la base de datos
        int deudaTotal = datos.deudaRelevo(relevo.getMatricula());

        // Borrado de los textos anteriores.
        holder.Matricula.setText("");
        holder.Nombre.setText("");
        holder.Apellidos.setText("");
        holder.Deuda.setText("");

        // Si hay teléfono, mostramos el icono de llamar.
        if (!relevo.getTelefono().isEmpty()) {
            holder.IconoLlamar.setVisibility(View.VISIBLE);
        } else {
            holder.IconoLlamar.setVisibility(View.GONE);
        }

        // Color del fondo
        if (relevo.isSeleccionado()) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
        } else if ((position + 1) % 2 == 0) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_relevo_par));
        } else {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_relevo_impar));
        }

        // Mostramos la calificacion
        switch (relevo.getCalificacion()) {
            case 1:
                holder.Calificacion.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_buen_relevo));
                holder.Calificacion.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.Calificacion.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_mal_relevo));
                holder.Calificacion.setVisibility(View.VISIBLE);
                break;
            default:
                holder.Calificacion.setVisibility(View.GONE);
        }

        // Escribimos los datos.
        holder.Matricula.setText((relevo.getMatricula() == 0) ? "" : String.valueOf(relevo.getMatricula()));
        holder.Nombre.setText(relevo.getNombre());
        holder.Apellidos.setText(relevo.getApellidos());

        // Escribimos la deuda
        String textoDeuda = "";
        switch (deudaTotal) {
            case 0:
                textoDeuda = "";
                break;
            case 1:
                textoDeuda = "Me debe un día.";
                holder.Deuda.setTextColor(Colores.VERDE_OSCURO);
                break;
            case -1:
                textoDeuda = "Le debo un día.";
                holder.Deuda.setTextColor(Colores.ROJO_OSCURO);
                break;
            default:
                if (deudaTotal > 1) {
                    textoDeuda = "Me debe " + String.valueOf(deudaTotal) + " días.";
                    holder.Deuda.setTextColor(Colores.VERDE_OSCURO);
                }
                if (deudaTotal < -1) {
                    deudaTotal = deudaTotal * -1;
                    textoDeuda = "Le debo " + String.valueOf(deudaTotal) + " días.";
                    holder.Deuda.setTextColor(Colores.ROJO_OSCURO);
                }
        }
        holder.Deuda.setText(textoDeuda);

        return view;
    }


    public static class ViewHolder {
        RelativeLayout Item;
        TextView Matricula;
        TextView Nombre;
        TextView Apellidos;
        TextView Deuda;
        ImageView Calificacion;
        ImageView IconoLlamar;
    }


}
