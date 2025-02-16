package com.quattro.quattroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import BaseDatos.DatosDia;
import Objetos.Colores;
import Objetos.Hora;

public class AdaptadorDiaCalendario extends ArrayAdapter <DatosDia> {

    private Context context;
    private ArrayList<DatosDia> listaDias;


    public AdaptadorDiaCalendario (@NonNull Context _context, ArrayList<DatosDia> lista) {
        super(_context, 0 , lista);
        this.context = _context;
        this.listaDias = lista;
    }

    public int getCount() {
        return listaDias.size();
    }

    public DatosDia getItem(int position) {
        return listaDias.get(position);
    }
    public long getItemId(DatosDia item) {
        return listaDias.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_calendario3, null);
            holder = new ViewHolder();

            holder.Item = view.findViewById(R.id.item);
            holder.EsFranqueo = view.findViewById(R.id.esFranqueo);
            holder.TextoResto = view.findViewById(R.id.resto);
            holder.TextoDia = view.findViewById(R.id.textoDia);
            holder.Dia = view.findViewById(R.id.dia);
            holder.DiaSemana = view.findViewById(R.id.diaSemana);
            holder.EsHuelga = view.findViewById(R.id.esHuelga);
            holder.HayCompañero = view.findViewById(R.id.hayCompañero);
            holder.Notas = view.findViewById(R.id.notas);
            holder.Servicio = view.findViewById(R.id.servicio);
            holder.Desayuno = view.findViewById(R.id.desayuno);
            holder.Comida = view.findViewById(R.id.comida);
            holder.Cena = view.findViewById(R.id.cena);
            holder.Relevo = view.findViewById(R.id.relevo);
            holder.Calificacion = view.findViewById(R.id.calificacion);
            holder.Horario = view.findViewById(R.id.horario);
            holder.Nocturnas = view.findViewById(R.id.nocturnas);
            holder.Guion = view.findViewById(R.id.guion);
            holder.Acumuladas = view.findViewById(R.id.acumuladas);
            holder.SeparadorUltimoElemento = view.findViewById(R.id.separadorUltimoElemento);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DatosDia dia = listaDias.get(position);

        // Ocultar todas las imágenes.
        holder.EsHuelga.setVisibility(View.GONE);
        holder.HayCompañero.setVisibility(View.GONE);
        holder.Notas.setVisibility(View.GONE);
        holder.Desayuno.setVisibility(View.GONE);
        holder.Comida.setVisibility(View.GONE);
        holder.Cena.setVisibility(View.GONE);

        if(dia.isSeleccionado()){
           holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
            holder.EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_seleccionado));
        } else if (dia.getDia() % 2 == 0) {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_par));
            holder.EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_par));
        } else {
            holder.Item.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_impar));
            holder.EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_calendario_impar));
        }

        // Variables a usar.
        String s = "";
        int i = 0;

        // Rellenamos el dia
        int Dsemana = dia.getDiaSemana();
        i = dia.getDia();
        s = (i > 9) ? String.valueOf(i) : "0" + String.valueOf(i);
        holder.Dia.setText(s);
        holder.DiaSemana.setText(Hora.DIAS_SEMANA_ABREV[Dsemana]);

        // Color si es un Franqueo o un Festivo
        holder.Dia.setTextColor(Colores.NEGRO);
        holder.DiaSemana.setTextColor(Colores.NEGRO);

        if (dia.isEsFranqueo()) {
            holder.EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_franqueo));
        }
        if (dia.isEsFestivo()) {
            holder.EsFranqueo.setBackground(context.getResources().getDrawable(R.drawable.fondo_festivo));
        }
        if (Dsemana == 1 || dia.isEsFestivo()) {
            holder.Dia.setTextColor(Colores.ROJO);
            holder.DiaSemana.setTextColor(Colores.ROJO);
        }

        // Rellenamos el servicio
        holder.Servicio.setText("");
        holder.Servicio.setTextColor(Colores.NEGRO);
        int tipo = dia.getTipoIncidencia();
        String ini = dia.getInicio();
        String fin = dia.getFinal();
        i = dia.getTurno();
        switch (tipo) {
            case 1:case 2:case 5:
                String ser = dia.getServicio();
                String lin = dia.getLinea();
                String tex = dia.getTextoLinea();
                if (ser == null) ser = "";
                if (lin == null) lin = "";
                if (tex == null) tex = "";
                if (!ser.trim().equals("") && !lin.trim().equals("") && !tex.trim().equals("") && i != 0) {
                    s = ser + "/" + i + "-" + lin + ": " + tex;
                } else {
                        s = dia.getTextoIncidencia();
                        if (i != 0) s = s + " " + i;
                }

                holder.Servicio.setText(s);
                break;
            case 3:case 4:case 6:
                String inc = dia.getTextoIncidencia();
                if (i != 0) inc = inc + " " + i;
                holder.Servicio.setText(inc);
                break;
            default:
                if (i != 0) holder.Servicio.setText("Turno " + i);
                break;

        }

        // Rellenamos el relevo
        holder.Relevo.setText("");
        int mat = dia.getMatricula();
        String ape = dia.getApellidos();
        if (mat != 0 && ape != null) {
            s = String.valueOf(mat) + ": " + ape;
        } else {
            s = "";
        }
        holder.Relevo.setText(s);

        // Rellenamos el horario
        holder.Horario.setText("");
        holder.Horario.setTextColor(Colores.GRIS_OSCURO);
        if (!ini.trim().equals("") && !fin.trim().equals("")) {
            s = ini + " - " + fin;
        } else {
            s = "";
        }
        holder.Horario.setText(s);

        // Rellenamos las horas acumuladas y nocturnas, si la incidencia es del tipo 1, 2 o 3
        holder.Nocturnas.setText("");
        holder.Acumuladas.setText("");
        double hacum = dia.getAcumuladas();
        double hnoct = dia.getNocturnas();
        holder.Guion.setVisibility(View.GONE);
        if (!s.equals("")) {
            if (tipo > 0 && tipo < 4 && dia.getTurno() != 0) {
                holder.Nocturnas.setText(Hora.textoDecimal(hnoct));
                if (hacum > -0.01) {
                    holder.Acumuladas.setTextColor(Colores.VERDE_OSCURO);
                } else {
                    holder.Acumuladas.setTextColor(Colores.ROJO);
                }
                holder.Acumuladas.setText(Hora.textoDecimal(hacum));
                holder.Guion.setVisibility(View.VISIBLE);
            } else {
                holder.Nocturnas.setText("");
                holder.Acumuladas.setText("");
                holder.Guion.setVisibility(View.GONE);
            }
        } else {
            if (tipo == 3 || hacum != 0 || hnoct != 0){
                holder.Nocturnas.setText(Hora.textoDecimal(hnoct));
                if (hacum < 0) {
                    holder.Acumuladas.setTextColor(Colores.ROJO);
                } else {
                    holder.Acumuladas.setTextColor(Colores.VERDE_OSCURO);
                }
                holder.Acumuladas.setText(Hora.textoDecimal(hacum));
                holder.Guion.setVisibility(View.VISIBLE);
            }
        }

        // Activar icono si hay un compañero que nos hace o hacemos el dia o si hay huelga.
        i = dia.getCodigoIncidencia();
        switch (i){
            case 11:
                holder.HayCompañero.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_usuario_rojo));
                holder.HayCompañero.setVisibility(View.VISIBLE);
                break;
            case 12:
                holder.HayCompañero.setImageDrawable(context.getResources().getDrawable(R.drawable.icono_usuario_verde));
                holder.HayCompañero.setVisibility(View.VISIBLE);
                break;
            case 15:
                holder.EsHuelga.setVisibility(View.VISIBLE);
                break;
            default:
                holder.HayCompañero.setVisibility(View.GONE);
                holder.EsHuelga.setVisibility(View.GONE);
        }

        // Activar icono si las notas tienen algo escrito
        if (!dia.getNotas().trim().equals("")) holder.Notas.setVisibility(View.VISIBLE);

        // Activar icono de las dietas.
        if (dia.isDesayuno()) holder.Desayuno.setVisibility(View.VISIBLE);
        if (dia.isComida()) holder.Comida.setVisibility(View.VISIBLE);
        if (dia.isCena()) holder.Cena.setVisibility(View.VISIBLE);

        // Activar el icono de la calificación del relevo
        i = dia.getCalificacion();
        switch (i){
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

        return view;

    }


    public static class ViewHolder{
        LinearLayout Item;
        LinearLayout EsFranqueo;
        RelativeLayout TextoResto;
        RelativeLayout TextoDia;
        TextView Dia;
        TextView DiaSemana;
        ImageView EsHuelga;
        ImageView HayCompañero;
        ImageView Notas;
        TextView Servicio;
        ImageView Desayuno;
        ImageView Comida;
        ImageView Cena;
        TextView Relevo;
        ImageView Calificacion;
        TextView Horario;
        TextView Nocturnas;
        TextView Guion;
        TextView Acumuladas;
        LinearLayout SeparadorUltimoElemento;
    }


}
