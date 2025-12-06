/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid2.Data.Models

import com.quattroid2.enums.CalificacionRelevo

class RelevoModel {

    // Propiedades
    var id: Int = 0
    var matricula: Int = 0
    var nombre: String = ""
    var apellidos: String = ""
    var telefono: String = ""
    var calificacion: CalificacionRelevo = CalificacionRelevo.Normal
    var deuda: Int = 0
    var notas: String = ""

    var isSeleccionado: Boolean = false

}