/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.activityHelpers

import android.widget.Button
import androidx.core.graphics.drawable.DrawableCompat
import com.quattroid.helpers.Colores

class CommonHelper {


    companion object {
        fun activarBoton(boton: Button) {
            boton.isEnabled = true
            boton.setTextColor(-0xffff67)
            DrawableCompat.setTint(boton.compoundDrawables[1], -0xffff67)
        }


        fun desactivarBoton(boton: Button) {
            boton.isEnabled = false
            boton.setTextColor(Colores.GRIS_OSCURO)
            DrawableCompat.setTint(boton.compoundDrawables[1], Colores.GRIS_OSCURO)
        }

    }

}