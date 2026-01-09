/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.helpers;

public class Converters {

    /**
     * Devuelve el número pasado como cadena en un Integer, si pertenece a un mes. Si no, devuelve null.
     */
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

    /**
     * Devuelve el número pasado como cadena en un integer. Si no es un número válido devuelve null.
     */
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
