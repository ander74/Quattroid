/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor
import com.quattroid.data.enums.CalificacionRelevo
import com.quattroid.data.enums.DiaSemana
import com.quattroid.data.enums.TipoIncidencia

class DiaModel : BaseModel {


    var dia: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var mes: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var año: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var diaSemana: DiaSemana = DiaSemana.Ninguno
        set(value) {
            field = value
            modified = true
        }
    var esFranqueo: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var esFestivo: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var horasHuelga: Double = 0.0
        set(value) {
            field = value
            modified = true
        }
    var huelgaParcial: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var codigoIncidencia: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var textoIncidencia: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var tipoIncidencia: TipoIncidencia = TipoIncidencia.Ninguno
        set(value) {
            field = value
            modified = true
        }
    var servicio: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var turno: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var linea: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var textoLinea: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var inicio: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var lugarInicio: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var final: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var lugarFinal: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var acumuladas: Double = 0.0
        set(value) {
            field = value
            modified = true
        }
    var nocturnas: Double = 0.0
        set(value) {
            field = value
            modified = true
        }
    var trabajadas: Double = 0.0
        set(value) {
            field = value
            modified = true
        }
    var desayuno: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var comida: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var cena: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var tomaDeje: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var tomaDejeDecimal: Double = 0.0
        set(value) {
            field = value
            modified = true
        }
    var euros: Double = 0.0
        set(value) {
            field = value
            modified = true
        }
    var matricula: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var apellidos: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var calificacion: CalificacionRelevo = CalificacionRelevo.Normal
        set(value) {
            field = value
            modified = true
        }
    var matriculaSusti: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var apellidosSusti: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var bus: String? = ""
        set(value) {
            field = value
            modified = true
        }
    var notas: String? = ""
        set(value) {
            field = value
            modified = true
        }

    var servicios: MutableList<ServicioDiaModel> = mutableListOf()


    constructor() {}

    constructor(cursor: Cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"))
        mes = cursor.getInt(cursor.getColumnIndexOrThrow("Mes"))
        año = cursor.getInt(cursor.getColumnIndexOrThrow("Año"))
        diaSemana = DiaSemana.entries[cursor.getInt(cursor.getColumnIndexOrThrow("DiaSemana"))]
        esFranqueo = cursor.getInt(cursor.getColumnIndexOrThrow("EsFranqueo")) == 1
        esFestivo = cursor.getInt(cursor.getColumnIndexOrThrow("EsFestivo")) == 1
        horasHuelga = cursor.getDouble(cursor.getColumnIndexOrThrow("HorasHuelga"))
        huelgaParcial = cursor.getInt(cursor.getColumnIndexOrThrow("HuelgaParcial")) == 1
        codigoIncidencia = cursor.getInt(cursor.getColumnIndexOrThrow("CodigoIncidencia"))
        textoIncidencia = cursor.getString(cursor.getColumnIndexOrThrow("TextoIncidencia"))
        tipoIncidencia = TipoIncidencia.entries[cursor.getInt(cursor.getColumnIndexOrThrow("TipoIncidencia"))]
        servicio = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"))
        turno = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"))
        linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"))
        textoLinea = cursor.getString(cursor.getColumnIndexOrThrow("TextoLinea"))
        inicio = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"))
        lugarInicio = cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio"))
        final = cursor.getString(cursor.getColumnIndexOrThrow("Final"))
        lugarFinal = cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal"))
        acumuladas = cursor.getDouble(cursor.getColumnIndexOrThrow("Acumuladas"))
        nocturnas = cursor.getDouble(cursor.getColumnIndexOrThrow("Nocturnas"))
        trabajadas = cursor.getDouble(cursor.getColumnIndexOrThrow("Trabajadas"))
        desayuno = cursor.getInt(cursor.getColumnIndexOrThrow("Desayuno")) == 1
        comida = cursor.getInt(cursor.getColumnIndexOrThrow("Comida")) == 1
        cena = cursor.getInt(cursor.getColumnIndexOrThrow("Cena")) == 1
        tomaDeje = cursor.getString(cursor.getColumnIndexOrThrow("TomaDeje"))
        tomaDejeDecimal = cursor.getDouble(cursor.getColumnIndexOrThrow("TomaDejeDecimal"))
        euros = cursor.getDouble(cursor.getColumnIndexOrThrow("Euros"))
        matricula = cursor.getInt(cursor.getColumnIndexOrThrow("Matricula"))
        apellidos = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"))
        calificacion = CalificacionRelevo.entries[cursor.getInt(cursor.getColumnIndexOrThrow("Calificacion"))]
        matriculaSusti = cursor.getInt(cursor.getColumnIndexOrThrow("MatriculaSusti"))
        apellidosSusti = cursor.getString(cursor.getColumnIndexOrThrow("ApellidosSusti"))
        bus = cursor.getString(cursor.getColumnIndexOrThrow("Bus"))
        notas = cursor.getString(cursor.getColumnIndexOrThrow("Notas"))
        modified = false;
    }

    fun clone(): DiaModel {
        val nuevoModel = DiaModel()
        nuevoModel.fromModel(this)
        return nuevoModel
    }

    fun fromModel(other: DiaModel) {
        val wasModified = modified
        this.dia = other.dia
        this.mes = other.mes
        this.año = other.año
        this.diaSemana = other.diaSemana
        this.esFranqueo = other.esFranqueo
        this.esFestivo = other.esFestivo
        this.horasHuelga = other.horasHuelga
        this.huelgaParcial = other.huelgaParcial
        this.codigoIncidencia = other.codigoIncidencia
        this.textoIncidencia = other.textoIncidencia
        this.tipoIncidencia = other.tipoIncidencia
        this.servicio = other.servicio
        this.turno = other.turno
        this.linea = other.linea
        this.textoLinea = other.textoLinea
        this.inicio = other.inicio
        this.lugarInicio = other.lugarInicio
        this.final = other.final
        this.lugarFinal = other.lugarFinal
        this.acumuladas = other.acumuladas
        this.nocturnas = other.nocturnas
        this.trabajadas = other.trabajadas
        this.desayuno = other.desayuno
        this.comida = other.comida
        this.cena = other.cena
        this.tomaDeje = other.tomaDeje
        this.tomaDejeDecimal = other.tomaDejeDecimal
        this.euros = other.euros
        this.matricula = other.matricula
        this.apellidos = other.apellidos
        this.calificacion = other.calificacion
        this.matriculaSusti = other.matriculaSusti
        this.apellidosSusti = other.apellidosSusti
        this.bus = other.bus
        this.notas = other.notas
        this.servicios = ArrayList(other.servicios.toList())
        modified = wasModified
    }

    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put("Dia", dia)
        values.put("Mes", mes)
        values.put("Año", año)
        values.put("DiaSemana", diaSemana.ordinal)
        values.put("EsFranqueo", esFranqueo)
        values.put("EsFestivo", esFestivo)
        values.put("HorasHuelga", horasHuelga)
        values.put("HuelgaParcial", huelgaParcial)
        values.put("CodigoIncidencia", codigoIncidencia)
        values.put("TextoIncidencia", textoIncidencia)
        values.put("TipoIncidencia", tipoIncidencia.ordinal)
        values.put("Servicio", servicio)
        values.put("Turno", turno)
        values.put("Linea", linea)
        values.put("TextoLinea", textoLinea)
        values.put("Inicio", inicio)
        values.put("LugarInicio", lugarInicio)
        values.put("Final", final)
        values.put("LugarFinal", lugarFinal)
        values.put("Acumuladas", acumuladas)
        values.put("Nocturnas", nocturnas)
        values.put("Trabajadas", trabajadas)
        values.put("Desayuno", desayuno)
        values.put("Comida", comida)
        values.put("Cena", cena)
        values.put("TomaDeje", tomaDeje)
        values.put("TomaDejeDecimal", tomaDejeDecimal)
        values.put("Euros", euros)
        values.put("Matricula", matricula)
        values.put("Apellidos", apellidos)
        values.put("Calificacion", calificacion.ordinal)
        values.put("MatriculaSusti", matriculaSusti)
        values.put("ApellidosSusti", apellidosSusti)
        values.put("Bus", bus)
        values.put("Notas", notas)
        return values
    }
}
