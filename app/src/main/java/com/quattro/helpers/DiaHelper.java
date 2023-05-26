package com.quattro.helpers;

import android.database.Cursor;

import BaseDatos.BaseDatos;
import BaseDatos.DatosDia;
import Objetos.Calculos;
import Objetos.EstadoDia;
import Objetos.Hora;
import Objetos.HorasServicio;

public class DiaHelper {

    public static void CalcularHorasDia(DatosDia datosDia, Cursor cursor, BaseDatos datos){
        // Determinamos cuantos servicios hay en total
        int num = cursor.getCount() + 1;
        HorasServicio[] servs = new HorasServicio[num];
        servs[0] = new HorasServicio();
        servs[0].Inicio = Hora.horaToInt(datosDia.getInicio());
        servs[0].Final = Hora.horaToInt(datosDia.getFinal());
        for (int m=1; m <num; m++){
            cursor.moveToPosition(m-1);
            servs[m] = new HorasServicio();
            servs[m].Inicio = Hora.horaToInt(cursor.getString(cursor.getColumnIndexOrThrow("Inicio")));
            servs[m].Final = Hora.horaToInt(cursor.getString(cursor.getColumnIndexOrThrow("Final")));
        }
        EstadoDia resultado = Calculos.TiempoTrabajado(servs, datos.opciones, datosDia.getTurno(), datosDia.getTipoIncidencia());
        if (resultado == null){
            datosDia.setTrabajadas(0d);
            datosDia.setAcumuladas(0d);
            datosDia.setNocturnas(0d);
            datosDia.setDesayuno(false);
            datosDia.setComida(false);
            datosDia.setCena(false);
        } else {
            datosDia.setTrabajadas(resultado.getTrabajadas());
            datosDia.setAcumuladas(resultado.getAcumuladas());
            datosDia.setNocturnas(resultado.getNocturnas());
            datosDia.setDesayuno(resultado.isDesayuno());
            datosDia.setComida(resultado.isComida());
            datosDia.setCena(resultado.isCena());
        }
        // Si la incidencia es Huelga, calculamos los datos de la huelga
        if (datosDia.getCodigoIncidencia() == 15){
            // Recuperamos los datos del convenio que necesitamos.
            double Jornada = datos.opciones.getJornadaMedia(); //Double.longBitsToDouble(jMedia);
            double JornadaMinima = datos.opciones.getJornadaMinima(); //Double.longBitsToDouble(jMinima);
            // Si la huelga es parcial...
            if (datosDia.isHuelgaParcial()){
                datosDia.setTrabajadas(Jornada);
                //TODO Comprobar que las horas trabajadas son la jornada media y no la jornada mínima.
                double acum = ((Jornada - JornadaMinima) * datosDia.getHorasHuelga())/Jornada;
                datosDia.setAcumuladas(-acum);
                // Si la huelga es completa...
            } else {
                datosDia.setTrabajadas(Jornada);
                //TODO Comprobar que las horas trabajadas son la jornada media y no la jornada mínima.
                datosDia.setAcumuladas(JornadaMinima - Jornada);
                datosDia.setNocturnas(0d);
            }
        }
    }


}
