/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid2.Data.Models

import com.quattroid2.enums.TipoIncidencia

class IncidenciaModel {


    // Propiedades
    var id: Int = 0
    var codigo: Int = 0
    var texto: String = ""
    var tipo: TipoIncidencia = TipoIncidencia.Ninguno

    var isSeleccionado: Boolean = false
}