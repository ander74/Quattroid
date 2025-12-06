/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid2.Data.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "Lineas")
data class LineaModel(

    // Propiedades

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "Linea")
    var linea: String = "",
    @ColumnInfo(name = "Texto")
    var texto: String = "",

    var servicios: MutableList<ServicioModel> = mutableListOf(),
    @Ignore
    var isSeleccionada: Boolean = false,
) {
}