
/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.helpers;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class Fondos {

    /**
     * Devuelve un fondo con una forma en su estado normal y otra al pulsarlo.
     *
     * @param forma        Forma que se usa en su estado normal
     * @param formaOnClick Forma que se usa al pulsarlo.
     * @return Devuelve un Drawable con el fondo que cambia al ser pulsado.
     */
    public static Drawable Fondo(Drawable forma, Drawable formaOnClick) {

        // Creamos el StateListDrawable a devolver.
        StateListDrawable sld = new StateListDrawable();

        // Añadimos las formas al StateListDrawable.
        sld.addState(new int[]{android.R.attr.state_enabled, -android.R.attr.state_pressed}, forma);
        sld.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, formaOnClick);

        // Devolvemos el fondo.
        return sld;
    }


}
