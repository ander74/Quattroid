
/*
 * Copyright 2015 - Quattroid 1.0
 *
 * Creado por A. Herrero en Enero de 2015
 * http://sites.google.com/site/qtroid
 * acumulador.admin@gmail.com
 *
 * Este programa es software libre; usted puede redistruirlo y/o modificarlo bajo los términos
 * de la Licencia Pública General GNU, tal y como está publicada por la Free Software Foundation;
 * ya sea la versión 2 de la Licencia, o (a su elección) cualquier versión posterior.
 *
 * Este programa se distribuye con la intención de ser útil, pero SIN NINGUNA GARANTÍA;
 * incluso sin la garantía implícita de USABILIDAD O UTILIDAD PARA UN FIN PARTICULAR.
 * Vea la Licencia Pública General GNU en "assets/Licencia" para más detalles.
 */
package Objetos;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Calculos {

    /**
     * Este método convierte dp en su equivalente en px, según la densidad del dispositivo.
     *
     * @param dp El valor en dp que se pretende convertir.
     * @param context Contexto donde se ejecutará el cálculo.
     * @return Un float que representa los px que corresponden al valor de dp pasado.
     */
    public static int ConvierteDpEnPx(int dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)(dp * (metrics.densityDpi / 160));
    }


    /**
     * Este método convierte px en su equivalente en dp, según la densidad del dispositivo.
     *
     * @param px EL valor en px que se pretende convertir.
     * @param context COntexto donde se ejecutará el cálculo.
     * @return Un float que representa los dp que corresponden al valor de px pasado.
     */
    public static int ConviertePxEnDp(int px, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)(px / (metrics.densityDpi / 160));
    }


    public static EstadoDia TiempoTrabajado(HorasServicio[] horas,
                                            Context context,
                                            int turno,
                                            int tipoIncidencia){

        // Esta versión del método de cálculo, realiza los cálculos con números decimales.
        // De esta manera, se elimina el desfase por redondeo que había anteriormente.

        // Creamos el objeto que contendrá las opciones del convenio
        SharedPreferences opciones = PreferenceManager.getDefaultSharedPreferences(context);

        // Obtenemos las variables del convenio desde las preferencias.
        long jMedia = opciones.getLong("JorMedia", Double.doubleToRawLongBits(7.75d));
        double Jornada = Double.longBitsToDouble(jMedia);
        long jMinima = opciones.getLong("JorMinima", Double.doubleToRawLongBits(7d));
        double JornadaMinima = Double.longBitsToDouble(jMinima);
        int LimiteEntreServicios = opciones.getInt("LimiteEntreServicios", 60);
        int InicioNocturnas = opciones.getInt("InicioNocturnas", 1320);
        int FinalNocturnas = opciones.getInt("FinalNocturnas", 390);
        int Desayuno = opciones.getInt("LimiteDesayuno", 270);
        int Comida1 = opciones.getInt("LimiteComida1", 930);
        int Comida2 = opciones.getInt("LimiteComida2", 810);
        int Cena = opciones.getInt("LimiteCena", 30);

        // NUEVO
        // Convertimos minutos en horas decimales.
        double limEntreServicios = Hora.horaToDecimal(LimiteEntreServicios);
        double iniNocturnas = Hora.horaToDecimal(InicioNocturnas);
        double finNocturnas = Hora.horaToDecimal(FinalNocturnas);
        double desayun = Hora.horaToDecimal(Desayuno);
        double com1 = Hora.horaToDecimal(Comida1);
        double com2 = Hora.horaToDecimal(Comida2);

        // Evaluamos la cena
        if (Cena < Desayuno) Cena += 1440;
        // NUEVO
        double cen = Hora.horaToDecimal(Cena);

        // Creamos las variables temporales del método.

        // Usadas para calcular los minutos totales a recorrer.
        int PrimerInicio = 0, UltimoFinal = 0;
        // DEPREC - Minutos totales a recorrer.
        int MinutosTotales;
        // Minuto en el que se encuentra el cursor del bucle.
        double MinutoActual;
        // Los minutos trabajados totales, así como el tiempo intermedio que cuenta
        // como trabajado. El intermedio parcial es de apoyo al cálculo.
        double MinutosTrabajados = 0, Intermedio = 0, IntermedioParcial = 0;
        // Los minutos en horario nocturno, así como el tiempo intermedio que cuenta
        // como trabajado y es nocturno. El intermedio parcial es de apoyo al cálculo.
        double MinutosNocturnos = 0, IntermedioNocturno = 0, IntermedioNocturnoParcial = 0;
        // Las dietas que corresponden al servicio.
        boolean DietaDesayuno = false, DietaComida = false, DietaCena = false;
        // Variables de apoyo para el cálculo.
        boolean EsTrabajado = false, EsNocturno = false;

        // Evaluamos los parámetros pasados para ver que sean correctos.
        if (horas.length == 0) return null;
        if (turno < 1 || turno > 2) return null;

        // Establecemos los valores PrimerInicio y UltimoFinal
        for (int X = 0; X < horas.length; X++ ){
            // Si las horas de inicio y final no son iguales y son diferentes a menos uno...
            if ((horas[X].Inicio != horas[X].Final) && (horas[X].Inicio != -1) && (horas[X].Final != -1)) {
                // Regulamos la hora final, si pasa de las 23:59
                if (horas[X].Inicio > horas[X].Final) horas[X].Final += 1440;
                // Si es la primera posición de la matriz...
                if (X == 0) {
                    // ... establecemos las horas
                    PrimerInicio = horas[X].Inicio;
                    UltimoFinal = horas[X].Final;
                } else {
                    // Evaluamos las horas y las sustituimos si es necesario.
                    if (PrimerInicio > horas[X].Inicio) PrimerInicio = horas[X].Inicio;
                    if (UltimoFinal < horas[X].Final) UltimoFinal = horas[X].Final;
                }
            }
        }

        // NUEVO
        // Convertimos primer inicio y ultimo final
        double priInicio = Hora.horaToDecimal(PrimerInicio);
        double ultFinal = Hora.horaToDecimal(UltimoFinal);

        // DEPREC - Asignamos los minutos totales.
        MinutosTotales = UltimoFinal - PrimerInicio;
        // NUEVO
        double minTotales = ultFinal - priInicio;

        // Comenzamos el primer bucle: Paso minuto a minuto por el tiempo total.
        Bucle1:
        for (double M = 0.01; M <= minTotales; M = M + 0.01){
            MinutoActual = priInicio + M;
            // Comenzamos el segundo bucle: Recorrido por las horas de los servicios.
            // En este caso, el bucle es un for each en lugar de un simple for.
            for (HorasServicio hora : horas) {
                // Sólo iteramos si el inicio y el final son diferentes y no son nulos.
                if ((hora.Inicio != hora.Final) && (hora.Inicio != -1) && hora.Final != -1 ){
                    // Si el minuto actual es un minuto trabajado:
                    // NUEVO
                    double horaInicio = Hora.horaToDecimal(hora.Inicio);
                    double horaFinal = Hora.horaToDecimal(hora.Final);
                    if (MinutoActual > horaInicio && MinutoActual <= horaFinal) {
                        MinutosTrabajados += 0.01;
                        // Evaluamos si hay alguna dieta
                        switch (turno) {
                            case (1):
                                if (MinutoActual < desayun) DietaDesayuno = true;
                                if (MinutoActual > com1) DietaComida = true;
                                break;
                            case (2):
                                if (MinutoActual <= com2) DietaComida = true;
                                if (MinutoActual > cen) DietaCena = true;
                                break;
                        }
                        // Evaluamos si es un minuto nocturno
                        if ((MinutoActual - 0.01 > 0 && MinutoActual - 0.01 < finNocturnas) ||
                                (MinutoActual > iniNocturnas && MinutoActual < (finNocturnas + 24))) {
                            // En caso de serlo, lo añadimos al total.
                            MinutosNocturnos += 0.01;
                            if (IntermedioNocturnoParcial > 0 &&
                                    IntermedioNocturnoParcial < limEntreServicios) {
                                IntermedioNocturno += IntermedioNocturnoParcial;
                            }
                            EsNocturno = true;
                        }
                        // Si el tiempo parcial no supera el límite, se suma al tiempo intermedio.
                        if (IntermedioParcial > 0 && IntermedioParcial < limEntreServicios) {
                            Intermedio += IntermedioParcial;
                        }
                        // Volvemos a poner el tiempo parcial a cero.
                        IntermedioParcial = 0;
                        IntermedioNocturnoParcial = 0;
                        // Marcamos el minuto como trabajado.
                        EsTrabajado = true;
                        continue Bucle1;
                    } else {
                        // Establecemos el minuto como no trabajado.
                        EsTrabajado = false;
                    }
                }
            }
            // Si el minuto no es trabajado, se añade al intermedio parcial.
            if (!EsTrabajado) IntermedioParcial += 0.01;
            // Si el minuto no es trabajado, pero si es nocturno, lo añadimos al intermedio
            // nocturno parcial.
            if (!EsTrabajado && EsNocturno)  IntermedioNocturnoParcial += 0.01;
        }
        // Sumamos los intermedios a los totales, una vez hemos salido de ambos bucles.
        MinutosTrabajados += Intermedio;
        MinutosNocturnos += IntermedioNocturno;

        // Inicializamos el resultado
        EstadoDia resultado = new EstadoDia();

        // Convertimos los resultados a decimales.
        double ht = MinutosTrabajados;
        double hn = MinutosNocturnos;

        // Cargamos los datos a devolver según el tipo de incidencia.
        switch (tipoIncidencia){
            case 1: // Trabajo
                resultado.setTrabajadas(ht);
                resultado.setNocturnas(hn);
                // Nueva incorporación
                if (ht < JornadaMinima) ht = JornadaMinima;
                resultado.setAcumuladas(ht - Jornada);
                resultado.setDesayuno(DietaDesayuno);
                resultado.setComida(DietaComida);
                resultado.setCena(DietaCena);
                break;
            case 2: // Franqueo a Trabajar
                resultado.setTrabajadas(ht);
                resultado.setNocturnas(hn);
                resultado.setAcumuladas(ht);
                resultado.setDesayuno(DietaDesayuno);
                resultado.setComida(DietaComida);
                resultado.setCena(DietaCena);
                break;
            case 3: // Fiesta por Otro Día
                resultado.setTrabajadas(0);
                resultado.setNocturnas(0);
                resultado.setAcumuladas(-Jornada);
                resultado.setDesayuno(false);
                resultado.setComida(false);
                resultado.setCena(false);
                break;
            case 6: // Jornada Media
                resultado.setTrabajadas(Jornada);
                resultado.setNocturnas(0);
                resultado.setAcumuladas(0);
                resultado.setDesayuno(false);
                resultado.setComida(false);
                resultado.setCena(false);
                break;
            case 7: // Huelga

            default:
                resultado.setTrabajadas(0);
                resultado.setNocturnas(0);
                resultado.setAcumuladas(0);
                resultado.setDesayuno(false);
                resultado.setComida(false);
                resultado.setCena(false);
        }

        // Devolvemos el resultado.
        return resultado;
    }

    public static void setAlturaLista(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
