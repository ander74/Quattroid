/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid2.Data.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "ServiciosAuxiliares")
data class ServicioAuxiliarModel(

    // Propiedades
    var id: Int = 0,
    @ColumnInfo(name = "Linea")
    var linea: String = "",
    @ColumnInfo(name = "Servicio")
    var servicio: String = "",
    @ColumnInfo(name = "Turno")
    var turno: Int = 0,
    @ColumnInfo(name = "LineaAuxiliar")
    var lineaAuxiliar: String = "",
    @ColumnInfo(name = "ServicioAuxiliar")
    var servicioAuxiliar: String = "",
    @ColumnInfo(name = "TurnoAuxiliar")
    var turnoAuxiliar: Int = 0,
    @ColumnInfo(name = "Inicio")
    var inicio: String = "",
    @ColumnInfo(name = "LugarInicio")
    var lugarInicio: String = "",
    @ColumnInfo(name = "Final")
    var final: String = "",
    @ColumnInfo(name = "LugarFinal")
    var lugarFinal: String = "",

    @Ignore
    var isSeleccionado: Boolean = false
) {
}