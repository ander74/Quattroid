
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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class Forma {

    private GradientDrawable forma = null;

    /**
     *  Este método devuelve un fondo rectangular en el que se puede elegir el color del fondo,
     *  el grosor y color del borde y el redondeo de las esquinas.
     *
     * @param context Contexto donde se va a usar el fondo.
     * @param colorFondo Color que se usará para el fondo.
     * @param radio Radio de las esquinas en dp. Cero si no se quieren esquinas redondeadas.
     * @param colorBorde Color que se usará para el borde.
     * @param borde Grosor del borde en dp. Cero si no se quiere borde.
     * @return Drawable con el fondo definido.
     */
    public Forma(Context context, int colorFondo, int radio, int borde, int colorBorde){

        // Creamos la forma que vamos a devolver, insertada en un StateListDrawable
        forma = new GradientDrawable();
        forma.setShape(GradientDrawable.RECTANGLE);

        // Ponemos el color deseado a la forma.
        forma.setColor(colorFondo);

        // Si borde es mayor que cero, añadimos un borde del grosor pasado en dp.
        if (borde > 0) forma.setStroke(Calculos.ConvierteDpEnPx(borde, context), colorBorde);

        // Si redondeado es mayor que cero, redondeamos las esquinas con el radio pasado en dp.
        if (radio > 0) forma.setCornerRadius(Calculos.ConvierteDpEnPx(radio, context));

    }

    /**
     *  Este método devuelve un fondo rectangular en el que se puede elegir el color del fondo,
     *  el grosor del borde y el redondeo de las esquinas. El borde será de color negro.
     *
     * @param context Contexto donde se va a usar el fondo.
     * @param colorFondo Color que se usará para el fondo.
     * @param radio Radio de las esquinas en dp. Cero si no se quieren esquinas redondeadas.
     * @param borde Grosor del borde en dp. Cero si no se quiere borde.
     * @return Drawable con el fondo definido.
     */
    public Forma(Context context, int colorFondo, int radio, int borde){

        // Creamos la forma que vamos a devolver, insertada en un StateListDrawable
        forma = new GradientDrawable();
        forma.setShape(GradientDrawable.RECTANGLE);

        // Ponemos el color deseado a la forma.
        forma.setColor(colorFondo);

        // Si borde es mayor que cero, añadimos un borde del grosor pasado en dp.
        if (borde > 0) forma.setStroke(Calculos.ConvierteDpEnPx(borde, context), 0X000000);

        // Si redondeado es mayor que cero, redondeamos las esquinas con el radio pasado en dp.
        if (radio > 0) forma.setCornerRadius(Calculos.ConvierteDpEnPx(radio, context));

    }

    /**
     *  Este método devuelve un fondo rectangular sin borde, en el que se puede elegir el color
     *  del fondo y el redondeo de las esquinas.
     *
     * @param context Contexto donde se va a usar el fondo.
     * @param colorFondo Color que se usará para el fondo.
     * @param radio Radio de las esquinas en dp. Cero si no se quieren esquinas redondeadas.
     * @return Drawable con el fondo definido.
     */
    public Forma(Context context, int colorFondo, int radio){

        // Creamos la forma que vamos a devolver, insertada en un StateListDrawable
        forma = new GradientDrawable();
        forma.setShape(GradientDrawable.RECTANGLE);

        // Ponemos el color deseado a la forma.
        forma.setColor(colorFondo);

        // Si redondeado es mayor que cero, redondeamos las esquinas con el radio pasado en dp.
        if (radio > 0) forma.setCornerRadius(Calculos.ConvierteDpEnPx(radio, context));

    }

    /**
     *  Este método devuelve un fondo rectangular sin borde del color indicado.
     *
     * @param context Contexto donde se va a usar el fondo.
     * @param colorFondo Color que se usará para el fondo.
     * @return Drawable con el fondo definido.
     */
    public Forma(Context context, int colorFondo){

        // Creamos la forma que vamos a devolver, insertada en un StateListDrawable
        forma = new GradientDrawable();
        forma.setShape(GradientDrawable.RECTANGLE);
        // Ponemos el color deseado a la forma.
        forma.setColor(colorFondo);

    }

    /**
     *
     * @return Devuelve la forma creada
     */
    public Drawable getForma(){
        return forma;
    }


}
