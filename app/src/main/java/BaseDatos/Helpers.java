/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package BaseDatos;

import android.content.Context;
import android.database.Cursor;

import com.quattroid.helpers.CalculosHelper;
import com.quattroid.helpers.DiaHelper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Helpers {

    /// Establece la incidencia en el día o la elimina si se pasa null como incidencia.
    public static void SetIncidenciaEnDia(DatosDia dia, @Nullable Incidencia incidencia) {
        if (incidencia == null) {
            dia.setCodigoIncidencia(0);
            dia.setTextoIncidencia("");
            dia.setTipoIncidencia(0);
        } else {
            dia.setCodigoIncidencia(incidencia.getCodigo());
            dia.setTextoIncidencia(incidencia.getTexto());
            dia.setTipoIncidencia(incidencia.getTipo());
        }
    }


    /// Establece los sábados y domingos como franqueos.
    public static boolean SetFindesComoFranqueos(int año, Context context) {
        BaseDatos datos = new BaseDatos(context);
        Incidencia incidenciaFranqueo = datos.getIncidencia(2);
        for (int mes = 1; mes <= 12; mes++) {
            ArrayList<DatosDia> datosDia = CargarDiasMesConTurno(mes, año, datos);
            datosDia.stream().filter(dia -> dia.getDiaSemana() == 7 || dia.getDiaSemana() == 1).forEach(dia -> {
                if (dia.getCodigoIncidencia() == 0) {
                    dia.setEsFranqueo(true);
                    SetIncidenciaEnDia(dia, incidenciaFranqueo);
                    datos.guardaDia(dia);
                }
            });
        }
        return true;
    }


    /// Pega el día que hay en el portapapeles en todos los días del año a partir del día seleccionado, respetando el turno.
    public static boolean PegarDiaEnAño(DateTime fecha, Context context) {
        if (DiaHelper.DiaEnPortapapeles == null) return false;
        BaseDatos datos = new BaseDatos(context);
        for (int mes = 1; mes <= 12; mes++) {
            if (mes < fecha.getMonthOfYear()) continue;
            ArrayList<DatosDia> datosDia = CargarDiasMesConTurno(mes, fecha.getYear(), datos);
            datosDia.stream().filter(dia -> dia.getTurno() == DiaHelper.DiaEnPortapapeles.getTurno() && dia.getCodigoIncidencia() == 0).forEach(dia -> {
                DateTime fechaDia = new DateTime(dia.getAño(), dia.getMes(), dia.getDia(), 0, 0);
                if (!fechaDia.isBefore(fecha)) {
                    PegarDelPortapapeles(dia, datos);
                    datos.guardaDia(dia);
                }
            });
        }
        return true;
    }


    /// Vacía todos los días del mes pasado en la fecha.
    public static void VaciarDiasMes(DateTime fecha, Context context) {
        BaseDatos datos = new BaseDatos(context);
        ArrayList<DatosDia> datosDia = datos.datosMes(fecha.getMonthOfYear(), fecha.getYear());
        datosDia.forEach(dia -> {
            VaciarDia(dia, datos);
        });
    }


    /// Maarca todos los días que se tenga trabajo en un mes con la incidencia enfermo.
    public static void MarcarEnfermoMes(DateTime fecha, Context context) {
        BaseDatos datos = new BaseDatos(context);
        Incidencia incidenciaEnfermo = datos.getIncidencia(6);
        ArrayList<DatosDia> datosDia = CargarDiasMesConTurno(fecha.getMonthOfYear(), fecha.getYear(), datos);
        datosDia.stream().filter(dia -> dia.getCodigoIncidencia() == 1).forEach(dia -> {
            String notas = dia.getNotas();
            int turno = dia.getTurno();
            VaciarDia(dia, datos);
            dia.setNotas(notas);
            dia.setTurno(turno);
            SetIncidenciaEnDia(dia, incidenciaEnfermo);
            datos.guardaDia(dia);
        });

    }


    /// Devuelve todos los días del mes indicado, con los turnos inferidos si está la opción activada.
    public static ArrayList<DatosDia> CargarDiasMesConTurno(int mes, int año, BaseDatos datos) {
        ArrayList<DatosDia> listaDias = datos.datosMes(mes, año);
        if (listaDias.isEmpty()) {
            datos.crearMes(mes, año);
            listaDias = datos.datosMes(mes, año);
        }
        if (datos.opciones.isInferirTurnos()) {
            int diaTurno = datos.opciones.getDiaBaseTurnos();
            int mesTurno = datos.opciones.getMesBaseTurnos();
            int añoTurno = datos.opciones.getAñoBaseTurnos();
            LocalDate fechaReferencia = new LocalDate(añoTurno, mesTurno, diaTurno);
            listaDias.stream().filter(d -> d.getCodigoIncidencia() == 0).forEach(d -> {
                LocalDate fechaDia = new LocalDate(d.getAño(), d.getMes(), d.getDia());
                d.setTurno(CalculosHelper.InferirTurno(fechaDia, fechaReferencia, 1));
                datos.guardaDia(d);
            });
        }
        return listaDias;
    }


    /// Pega el día que está en el portapapeles al día que se pasa.
    public static void PegarDelPortapapeles(DatosDia dia, BaseDatos datos) {
        dia.setCodigoIncidencia(DiaHelper.DiaEnPortapapeles.getCodigoIncidencia());
        dia.setTextoIncidencia(DiaHelper.DiaEnPortapapeles.getTextoIncidencia());
        dia.setTipoIncidencia(DiaHelper.DiaEnPortapapeles.getTipoIncidencia());
        dia.setLinea(DiaHelper.DiaEnPortapapeles.getLinea());
        dia.setServicio(DiaHelper.DiaEnPortapapeles.getServicio());
        dia.setTurno(DiaHelper.DiaEnPortapapeles.getTurno());
        dia.setTextoLinea(DiaHelper.DiaEnPortapapeles.getTextoLinea());
        dia.setInicio(DiaHelper.DiaEnPortapapeles.getInicio());
        dia.setLugarInicio(DiaHelper.DiaEnPortapapeles.getLugarInicio());
        dia.setFinal(DiaHelper.DiaEnPortapapeles.getFinal());
        dia.setLugarFinal(DiaHelper.DiaEnPortapapeles.getLugarFinal());
        dia.setBus(DiaHelper.DiaEnPortapapeles.getBus());
        dia.setTomaDeje(DiaHelper.DiaEnPortapapeles.getTomaDeje());
        dia.setTomaDejeDecimal(DiaHelper.DiaEnPortapapeles.getTomaDejeDecimal());
        dia.setEuros(DiaHelper.DiaEnPortapapeles.getEuros());
        dia.setAcumuladas(DiaHelper.DiaEnPortapapeles.getAcumuladas());
        dia.setNocturnas(DiaHelper.DiaEnPortapapeles.getNocturnas());
        dia.setTrabajadas(DiaHelper.DiaEnPortapapeles.getTrabajadas());
        dia.setMatricula(DiaHelper.DiaEnPortapapeles.getMatricula());
        dia.setApellidos(DiaHelper.DiaEnPortapapeles.getApellidos());
        dia.setMatriculaSusti(DiaHelper.DiaEnPortapapeles.getMatriculaSusti());
        dia.setApellidosSusti(DiaHelper.DiaEnPortapapeles.getApellidosSusti());
        dia.setCalificacion(DiaHelper.DiaEnPortapapeles.getCalificacion());
        dia.setNotas(DiaHelper.DiaEnPortapapeles.getNotas());
        datos.guardaDia(dia);
        // Copiamos los servicios del día
        datos.vaciarServiciosDia(dia.getDia(), dia.getMes(), dia.getAño());
        Cursor cursor = datos.cursorServiciosDia(DiaHelper.DiaPortapapeles, DiaHelper.MesPortapapeles, DiaHelper.AñoPortapapeles);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ServicioDia servicioDia = new ServicioDia();
                servicioDia.setDia(dia.getDia());
                servicioDia.setMes(dia.getMes());
                servicioDia.setAño(dia.getAño());
                servicioDia.setLinea(cursor.getString(cursor.getColumnIndexOrThrow("Linea")));
                servicioDia.setServicio(cursor.getString(cursor.getColumnIndexOrThrow("Servicio")));
                servicioDia.setTurno(cursor.getInt(cursor.getColumnIndexOrThrow("Turno")));
                servicioDia.setInicio(cursor.getString(cursor.getColumnIndexOrThrow("Inicio")));
                servicioDia.setLugarInicio(cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio")));
                servicioDia.setFinal(cursor.getString(cursor.getColumnIndexOrThrow("Final")));
                servicioDia.setLugarFinal(cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal")));
                datos.guardaServicioDia(servicioDia);
            }
        }
        cursor.close();
    }


    /// Vacía los datos de un día, incluido los servicios auxiliares del día.
    public static void VaciarDia(DatosDia dia, BaseDatos datos) {
        dia.setCodigoIncidencia(0);
        dia.setTextoIncidencia("");
        dia.setTipoIncidencia(0);
        dia.setEsFranqueo(false);
        dia.setEsFestivo(false);
        dia.setLinea("");
        dia.setServicio("");
        dia.setTurno(0);
        dia.setTextoLinea("");
        dia.setInicio("");
        dia.setLugarInicio("");
        dia.setFinal("");
        dia.setLugarFinal("");
        dia.setBus("");
        dia.setTomaDeje("");
        dia.setTomaDejeDecimal(0d);
        dia.setEuros(0d);
        dia.setAcumuladas(0d);
        dia.setNocturnas(0d);
        dia.setTrabajadas(0d);
        dia.setDesayuno(false);
        dia.setComida(false);
        dia.setCena(false);
        dia.setMatricula(0);
        dia.setApellidos("");
        dia.setMatriculaSusti(0);
        dia.setApellidosSusti("");
        dia.setCalificacion(0);
        dia.setNotas("");
        if (datos.guardaDia(dia)) {
            datos.vaciarServiciosDia(dia.getDia(), dia.getMes(), dia.getAño());
        }
    }


    /// Marca el día de la fecha pasada como festivo poniendo la incidencia si no hay otra puesta.
    public static void MarcarFestivo(DateTime fecha, Context context) {
        BaseDatos datos = new BaseDatos(context);
        Incidencia incidenciaFestivo = datos.getIncidencia(9);
        DatosDia dia = datos.getDia(fecha.getYear(), fecha.getMonthOfYear(), fecha.getDayOfMonth());
        if (dia == null || dia.getDiaSemana() == 1) return;
        dia.setEsFestivo(true);
        if (dia.getCodigoIncidencia() == 0 || dia.getCodigoIncidencia() == 2) {
            SetIncidenciaEnDia(dia, incidenciaFestivo);
            datos.guardaDia(dia);
        }
    }

}
