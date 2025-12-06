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
import com.quattroid2.enums.CalificacionRelevo
import com.quattroid2.enums.DiaSemana
import com.quattroid2.enums.TipoIncidencia

@Entity(tableName = "Calendario")
data class DiaModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "Dia")
    var dia: Int = 0,
    @ColumnInfo(name = "Mes")
    var mes: Int = 0,
    @ColumnInfo(name = "Año")
    var año: Int = 0,
    @ColumnInfo(name = "DiaSemana")
    var diaSemana: DiaSemana = DiaSemana.Ninguno,
    @ColumnInfo(name = "EsFranqueo")
    var esFranqueo: Boolean = false,
    @ColumnInfo(name = "EsFestivo")
    var esFestivo: Boolean = false,
    @ColumnInfo(name = "HorasHuelga")
    var horasHuelga: Double = 0.0,
    @ColumnInfo(name = "HuelgaParcial")
    var huelgaParcial: Boolean = false,
    @ColumnInfo(name = "CodigoIncidencia")
    var codigoIncidencia: Int = 0,
    @ColumnInfo(name = "TextoIncidencia")
    var textoIncidencia: String? = "",
    @ColumnInfo(name = "TipoIncidencia")
    var tipoIncidencia: TipoIncidencia = TipoIncidencia.Ninguno,
    @ColumnInfo(name = "Servicio")
    var servicio: String? = "",
    @ColumnInfo(name = "Turno")
    var turno: Int = 0,
    @ColumnInfo(name = "Linea")
    var linea: String? = "",
    @ColumnInfo(name = "TextoLinea")
    var textoLinea: String? = "",
    @ColumnInfo(name = "Inicio")
    var inicio: String? = "",
    @ColumnInfo(name = "LugarInicio")
    var lugarInicio: String? = "",
    @ColumnInfo(name = "Final")
    var final: String? = "",
    @ColumnInfo(name = "LugarFinal")
    var lugarFinal: String? = "",
    @ColumnInfo(name = "Acumuladas")
    var acumuladas: Double = 0.0,
    @ColumnInfo(name = "Nocturnas")
    var nocturnas: Double = 0.0,
    @ColumnInfo(name = "Trabajadas")
    var trabajadas: Double = 0.0,
    @ColumnInfo(name = "Desayuno")
    var desayuno: Boolean = false,
    @ColumnInfo(name = "Comida")
    var comida: Boolean = false,
    @ColumnInfo(name = "Cena")
    var cena: Boolean = false,
    @ColumnInfo(name = "TomaDeje")
    var tomaDeje: String? = "",
    @ColumnInfo(name = "TomaDejeDecimal")
    var tomaDejeDecimal: Double = 0.0,
    @ColumnInfo(name = "Euros")
    var euros: Double = 0.0,
    @ColumnInfo(name = "Matricula")
    var matricula: Int = 0,
    @ColumnInfo(name = "Apellidos")
    var apellidos: String? = "",
    @ColumnInfo(name = "Calificacion")
    var calificacion: CalificacionRelevo = CalificacionRelevo.Normal,
    @ColumnInfo(name = "MatriculaSusti")
    var matriculaSusti: Int = 0,
    @ColumnInfo(name = "ApellidosSusti")
    var apellidosSusti: String? = "",
    @ColumnInfo(name = "Bus")
    var bus: String? = "",
    @ColumnInfo(name = "Notas")
    var notas: String? = "",
    var servicios: MutableList<ServicioDiaModel> = mutableListOf(),
    @Ignore
    var isSeleccionado: Boolean = false
) {
}
