
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

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class Fondos {

    /**
     * Devuelve un fondo con una forma en su estado normal y otra al pulsarlo.
     *
     * @param forma Forma que se usa en su estado normal
     * @param formaOnClick Forma que se usa al pulsarlo.
     * @return Devuelve un Drawable con el fondo que cambia al ser pulsado.
     */
    public static Drawable Fondo(Drawable forma, Drawable formaOnClick){

        // Creamos el StateListDrawable a devolver.
        StateListDrawable sld = new StateListDrawable();

        // Añadimos las formas al StateListDrawable.
        sld.addState(new int[]{android.R.attr.state_enabled, -android.R.attr.state_pressed}, forma);
        sld.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, formaOnClick);

        // Devolvemos el fondo.
        return  sld;
    }


}
