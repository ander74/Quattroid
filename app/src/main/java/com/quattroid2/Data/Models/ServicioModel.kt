/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid2.Data.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "Servicios")
data class ServicioModel(

    // Propiedades
    var id: Int = 0,
    @ColumnInfo(name = "Linea")
    var linea: String = "",
    @ColumnInfo(name = "Servicio")
    var servicio: String = "",
    @ColumnInfo(name = "Turno")
    var turno: Int = 0,
    @ColumnInfo(name = "Inicio")
    var inicio: String = "",
    @ColumnInfo(name = "LugarInicio")
    var lugarInicio: String = "",
    @ColumnInfo(name = "Final")
    var final: String = "",
    @ColumnInfo(name = "LugarFinal")
    var lugarFinal: String = "",
    @ColumnInfo(name = "TomaDeje")
    var tomaDeje: String = "",
    @ColumnInfo(name = "Euros")
    var euros: Double = 0.0,

    var servicios: MutableList<ServicioAuxiliarModel> = mutableListOf(),

    @Ignore
    var isSeleccionado: Boolean = false
) {
}