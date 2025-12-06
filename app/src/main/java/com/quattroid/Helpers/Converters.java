/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.Helpers;

public class Converters {


    public static Integer getMesOrNull(String mesEnNumero) {
        Integer mes;
        try {
            mes = Integer.valueOf(mesEnNumero);
            if (mes < 1 || mes > 12) return null;
        } catch (NumberFormatException e) {
            mes = null;
        }
        return mes;
    }

    public static Integer getEnteroOrNull(String numeroEntero) {
        Integer entero;
        try {
            entero = Integer.valueOf(numeroEntero);
        } catch (NumberFormatException e) {
            entero = null;
        }
        return entero;
    }


}
