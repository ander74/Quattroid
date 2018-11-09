
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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hora {

    // CONSTANTES PUBLICAS

    public static final String[] MESES_MAY = new String[] {"DESCONOCIDO", "ENERO","FEBRERO","MARZO",
                                                    "ABRIL", "MAYO","JUNIO", "JULIO","AGOSTO",
                                                    "SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};

    public static final String[] DIAS_SEMANA_ABREV = {"DES", "DOM", "LUN", "MAR", "MIE",
                                                      "JUE", "VIE", "SAB", "DOM"};

    public static final String[] DIAS_SEMANA = {"Domingo", "Domingo", "Lunes", "Martes",
                                                "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    public static final String[] MESES_MIN = {"Desconocido", "Enero", "Febrero", "Marzo", "Abril",
                                              "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                                              "Octubre", "Noviembre", "Diciembre"};


    // Pasando una hora h, en cadena o en minutos, la devuelve formateada, siempre
    // que sea una hora válida o esté en los límites adecuados.
    // Si no es una hora válida, devuelve una cadena vacía.
    public static String horaToString(String h){
        // Creamos un filtro para evaluar la cadena pasada.
        Pattern p = Pattern.compile("[0-9]{1,2}:[0-9]{2}");
        // Reemplazamos tanto los puntos, como las comas, por el símbolo : y quitamos espacios.
        h = h.trim().replace(".", ":").replace(",", ":");
        Matcher mt = p.matcher(h);
        // Si no tiene formato de hora, devuelve una cadena vacía.
        if (!mt.matches()) return "";
        // Dividimos la cadena ya comprobada usando el símbolo : como separador.
        String[] datos = h.split(":");
        // Comprobar que no sean más de 59 minutos
        int mm = -1;
        try {
            mm = Integer.valueOf(datos[1]);
        } catch (NumberFormatException e){
            return "";
        }
        if (mm < 0 || mm > 59) return "";
        // Si la hora solo tiene un dígito, ponemos el cero delante.
        String hora =  datos[0].length()>1 ? datos[0] : "0" + datos[0];
        // Completamos la hora añadiendole los minutos.
        hora = hora + ":" + datos[1];
        // Devolvemos hora en una cadena.
        return hora;
    }

    // Sobrecarga del método anterior
    public static String horaToString(int h){
        if (h < 0 || h > 1439) return "";
        int mm = h % 60;
        int hh = (h - mm) / 60;
        String hora = hh > 9 ? String.valueOf(hh) : "0" + String.valueOf(hh);
        hora = hora + ":" + (mm > 9 ? String.valueOf(mm) : "0" + String.valueOf(mm));
        return hora;
    }

    // Pasando una hora h, devuelve un entero con el número de minutos que representa.
    // Si la hora no es válida, devuelve -1.
    public static int horaToInt(String h){
        // Si h es nulo, devolvemos -1
        if (h == null || h == "") return -1;
        // Creamos un filtro para evaluar la cadena pasada.
        Pattern p = Pattern.compile("[0-9]{1,2}:[0-9]{2}");
        // Reemplazamos tanto los puntos, como las comas, por el símbolo : y quitamos espacios.
        h = h.trim().replace(".", ":").replace(",", ":");
        Matcher mt = p.matcher(h);
        // Si no tiene formato de hora, devuelve -1.
        if (!mt.matches()) return -1;
        // Dividimos la cadena ya comprobada usando el símbolo : como separador.
        String[] numeros = h.split(":");
        // Si la parte de las horas es mayor que 23 o la parte de los minutos es
        // mayor de 59, devuelve -1.
        int hh = Integer.parseInt(numeros[0]);
        int mm = Integer.parseInt(numeros[1]);
        if (hh > 23 || mm > 59) return -1;
        // Devolvemos los minutos
        return (hh * 60) + mm;
    }

    // Pasando una hora h, en cadena o en minutos, devuelve un double con la hora en
    // formato decimal y redondeada a dos decimales.
    // En el caso de pasar una hora en cadena de texto, si no es válida devuelve -1.
    // En el caso de pasar una hora en minutos, no hay límites, admitiendo negativos.
    public static double horaToDecimal(String h){
        // Convertimos la hora a un entero con los minutos.
        int horaInt = horaToInt(h);
        // Si horaInt es -1 devolvemos -1.
        if (horaInt == -1) return -1;
        // Calculamos la hora y la devolvemos en decimal, redondeada.
        return redondeaDecimal((double) horaInt / 60);

    }

    // Sobrecarga del método anterior.
    public static double horaToDecimal(int i){
        // Devuelve la hora decimal redondeada.
        return redondeaDecimal((double) i / 60);
    }


    // Pasando una hora h en minutos, devuelve un entero con la parte que corresponde
    // a la hora, descartando la parte de los minutos.
    // Si h no está en los límites devuelve -1.
    public static int horas(int h){
        // Comprobamos que h está en los límites.
        if (h < 0 || h > 1439) return -1;
        // Obtenemos los minutos.
        int m = h % 60;
        // Devolvemos las horas, quitándole antes los minutos para que
        // la división de un número entero.
        return (h - m) / 60;
    }

    // Pasando una hora h en minutos, devuelve un entero con la parte que corresponde
    // a los minutos, descartando la parte de las horas.
    // Si h no está en los límites devuelve -1.
    public static int minutos(int h){
        // Comprobamos que h está en los límites.
        if (h < 0 || h > 1439) return -1;
        // Devolvemos los minutos.
        return h % 60;
    }

    // Devuelve el double pasado como decimal redondeado a dos decimales.
    public static double redondeaDecimal(double h){
        String s = String.format("%.2f", h).replace(",", ".");
        return Double.parseDouble(s);
    }

    // Devuelve el double pasado como decimal redondeado a dos decimales.
    public static double redondeaDecimal4(double h){
        String s = String.format("%.4f", h).replace(",", ".");
        return Double.parseDouble(s);
    }

    // Devuelve una cadena con el double pasado redondeado y formateado para mostrar.
    public static String textoDecimal(double h){
        String s = String.format("%.2f", h).replace(",", ".");
        s = s.replace(".", ",");
        if (s.equals("-0,00")) s = "0,00";
        return s;
    }

    public static String validaHoraDecimal(String s){
        // Creamos un filtro para evaluar la cadena pasada.
        Pattern p = Pattern.compile("-?[0-9]+.?[0-9]{0,2}");
        // Reemplazamos tanto los puntos, como las comas, por el símbolo : y quitamos espacios.
        s = s.trim().replace(",", ".");
        Matcher mt = p.matcher(s);
        // Si no tiene formato de hora, devuelve una cadena vacía.
        if (!mt.matches()) return "";
        return textoDecimal(Double.valueOf(s));
    }


    //region METODOS DE SERVICIO (SIN RELACION CON HORAS)

    public static String validarServicio(String s) {

        // Comprobamos si sólo tiene un digito al principio
        Pattern p = Pattern.compile("^[0-9]{1}\\D*");
        Matcher m = p.matcher(s);
        if (m.matches()) s = "0" + s;

        // Devolvemos el texto con las letras en mayusculas.
        return s.toUpperCase();
    }


    //endregion

}
