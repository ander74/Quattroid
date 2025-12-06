package com.quattroid.Helpers;

import java.util.ArrayList;

import BaseDatos.BaseDatos;
import BaseDatos.DatosDia;
import BaseDatos.ServicioDia;
import Objetos.Calculos;
import Objetos.EstadoDia;
import Objetos.Hora;
import Objetos.HorasServicio;

public class DiaHelper {


    /// Diá que se ha copiado en el portapapeles.
    public static DatosDia DiaEnPortapapeles = null;
    public static int DiaPortapapeles = 0;
    public static int MesPortapapeles = 0;
    public static int AñoPortapapeles = 0;


    public static void CalcularHorasDia(DatosDia datosDia, ArrayList<ServicioDia> servicios, BaseDatos datos) {
        // Determinamos cuantos servicios hay en total
        int num = servicios.size() + 1;
        HorasServicio[] servs = new HorasServicio[num];
        servs[0] = new HorasServicio();
        servs[0].Inicio = Hora.horaToInt(datosDia.getInicio());
        servs[0].Final = Hora.horaToInt(datosDia.getFinal());
        for (int m = 1; m < num; m++) {
            ServicioDia sss = servicios.get(m - 1);
            servs[m] = new HorasServicio();
            servs[m].Inicio = Hora.horaToInt(sss.getInicio());
            servs[m].Final = Hora.horaToInt(sss.getFinal());
        }
        EstadoDia resultado = Calculos.TiempoTrabajado(servs, datos.opciones, datosDia.getTurno(), datosDia.getTipoIncidencia());
        if (resultado == null) {
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
        if (datosDia.getCodigoIncidencia() == 15) {
            // Recuperamos los datos del convenio que necesitamos.
            double Jornada = datos.opciones.getJornadaMedia(); //Double.longBitsToDouble(jMedia);
            double JornadaMinima = datos.opciones.getJornadaMinima(); //Double.longBitsToDouble(jMinima);
            // Si la huelga es parcial...
            if (datosDia.isHuelgaParcial()) {
                datosDia.setTrabajadas(Jornada);
                double acum = ((Jornada - JornadaMinima) * datosDia.getHorasHuelga()) / Jornada;
                datosDia.setAcumuladas(-acum);
                // Si la huelga es completa...
            } else {
                datosDia.setTrabajadas(Jornada);
                datosDia.setAcumuladas(JornadaMinima - Jornada);
                datosDia.setNocturnas(0d);
            }
        }


    }

}
