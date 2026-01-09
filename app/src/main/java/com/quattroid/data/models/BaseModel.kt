/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

/**
 * Clase base para los modelos de datos con una propiedad id.
 * Contiene también las propiedades isModified e isSelected para definir si el modelo ha sido modificado o seleccionado.
 */
abstract class BaseModel {

    protected var modified: Boolean = false;

    /**
     * Id del modelo.
     */
    var id: Int = 0
        set(value) {
            field = value
            modified = true
        }

    /**
     * Indica si el modelo ha sido modificado.
     */
    val isModified: Boolean get() = modified

    /**
     * Indica si el modelo ha sido seleccionado.
     */
    var isSelected: Boolean = false


}