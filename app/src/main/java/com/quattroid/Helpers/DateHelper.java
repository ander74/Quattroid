/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.Helpers;

import org.joda.time.LocalDate;

public class DateHelper {

    /* Devuelve true si la fecha es válida. */
    public static Boolean isDateValid(int year, int month, int day) {
        LocalDate fecha = new LocalDate(year, month, 1);
        int diasMes = fecha.dayOfMonth().getMaximumValue();
        if (day > diasMes) return false;
        return true;
    }

}
