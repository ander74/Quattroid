/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.helpers;

/**
 * QuattroNet - ander74 - 2016.
 */

public class NombresHelper {

    public static final String[] MESES = new String[]{"Desconocido", "Enero", "Febrero", "Marzo",
            "Abril", "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    public static final String[] DIAS_SEMANA = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves",
            "Viernes", "Sábado", "Domingo"};


    /**
     * Devuelve el nombre del mes, cuyo índice se ha pasado.
     *
     * @param iMes       Índice del mes (1-Enero ... 12-Diciembre)
     * @param Mays       True si queremos el nombre en mayúsculas.
     *                   False, la primera mayúscula y el resto en minúsculas.
     * @param TresLetras True si queremos la abreviatura de tres letras.
     *                   False si queremos el nombre completo.
     * @return El nombre del mes indicado, con el formato indicado.
     */
    public static String Mes(int iMes, boolean Mays, boolean TresLetras) {

        // Si el mes pasado está fuera de límite, se pone a cero.
        if (iMes < 1 || iMes > 12) iMes = 0;
        // Establecemos el nombre del mes en mayúsculas o minúsculas, según se haya indicado.
        String sMes = Mays ? MESES[iMes].toUpperCase() : MESES[iMes];
        // Ajustamos a tres letras si se ha indicado.
        sMes = TresLetras ? sMes.substring(0, 2) : sMes;
        // Devolvemos el nombre del mes.
        return sMes;
    }

    /**
     * Devuelve el nombre del mes, cuyo índice se ha pasado.
     *
     * @param iMes Índice del mes (1-Enero ... 12-Diciembre)
     * @param Mays True si queremos el nombre en mayúsculas.
     *             False, la primera mayúscula y el resto en minúsculas.
     * @return El nombre del mes indicado, con el formato indicado.
     */
    public static String Mes(int iMes, boolean Mays) {
        return Mes(iMes, Mays, false);
    }

    /**
     * Devuelve el nombre del mes, cuyo índice se ha pasado.
     *
     * @param iMes Índice del mes (1-Enero ... 12-Diciembre)
     * @return El nombre del mes indicado, con el formato indicado.
     */
    public static String Mes(int iMes) {
        return Mes(iMes, false, false);
    }


    /**
     * Devuelve el nombre del día de la semana, cuyo índice se ha pasado.
     *
     * @param iDia       Índice del día de la semana (1-Lunes .. 7-Domingo)
     *                   El índice cero se interpreta como Domingo.
     * @param Mays       True si queremos el nombre en mayúsculas.
     *                   False, la primera mayúscula y el resto en minúsculas.
     * @param TresLetras True si queremos la abreviatura de tres letras.
     *                   False si queremos el nombre completo.
     * @return El nombre del día de la semana indicado, con el formato indicado.
     */
    public static String DiaSemana(int iDia, boolean Mays, boolean TresLetras) {

        // Si el mes pasado está fuera de límite, se pone a cero.
        if (iDia < 1 || iDia > 7) iDia = 0;
        // Establecemos el nombre del mes en mayúsculas o minúsculas, según se haya indicado.
        String sDia = Mays ? DIAS_SEMANA[iDia].toUpperCase() : DIAS_SEMANA[iDia];
        // Ajustamos a tres letras si se ha indicado.
        sDia = TresLetras ? sDia.substring(0, 2) : sDia;
        // Devolvemos el nombre del mes.
        return sDia;
    }

    /**
     * Devuelve el nombre del día de la semana, cuyo índice se ha pasado.
     *
     * @param iDia Índice del día de la semana (1-Lunes .. 7-Domingo)
     *             El índice cero se interpreta como Domingo.
     * @param Mays True si queremos el nombre en mayúsculas.
     *             False, la primera mayúscula y el resto en minúsculas.
     * @return El nombre del día de la semana indicado, con el formato indicado.
     */
    public static String DiaSemana(int iDia, boolean Mays) {
        return DiaSemana(iDia, Mays, false);
    }

    /**
     * Devuelve el nombre del día de la semana, cuyo índice se ha pasado.
     *
     * @param iDia Índice del día de la semana (1-Lunes .. 7-Domingo)
     *             El índice cero se interpreta como Domingo.
     * @return El nombre del día de la semana indicado, con el formato indicado.
     */
    public static String DiaSemana(int iDia) {
        return DiaSemana(iDia, false, false);
    }


}
