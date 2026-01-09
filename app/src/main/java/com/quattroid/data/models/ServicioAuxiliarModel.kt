/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor


class ServicioAuxiliarModel : BaseModel {


    var servicioId: Int = 0
        set(value) {
            field = value
            modified = true
        }

    var Servicioid: Int = 0
        set(value) {
            field = value
            modified = true
        }


    var linea: String = ""
        set(value) {
            field = value
            modified = true
        }

    var servicio: String = ""
        set(value) {
            field = value
            modified = true
        }

    var turno: Int = 0
        set(value) {
            field = value
            modified = true
        }

    var lineaAuxiliar: String = ""
        set(value) {
            field = value
            modified = true
        }

    var servicioAuxiliar: String = ""
        set(value) {
            field = value
            modified = true
        }

    var turnoAuxiliar: Int = 0
        set(value) {
            field = value
            modified = true
        }

    var inicio: String = ""
        set(value) {
            field = value
            modified = true
        }

    var lugarInicio: String = ""
        set(value) {
            field = value
            modified = true
        }

    var final: String = ""
        set(value) {
            field = value
            modified = true
        }

    var lugarFinal: String = ""
        set(value) {
            field = value
            modified = true
        }

    constructor() {}

    constructor(cursor: Cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
        servicioId = cursor.getInt(cursor.getColumnIndexOrThrow("ServicioId"))
        linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"))
        servicio = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"))
        turno = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"))
        lineaAuxiliar = cursor.getString(cursor.getColumnIndexOrThrow("LineaAuxiliar"))
        servicioAuxiliar = cursor.getString(cursor.getColumnIndexOrThrow("ServicioAuxiliar"))
        turnoAuxiliar = cursor.getInt(cursor.getColumnIndexOrThrow("TurnoAuxiliar"))
        inicio = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"))
        lugarInicio = cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio"))
        final = cursor.getString(cursor.getColumnIndexOrThrow("Final"))
        lugarFinal = cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal"))
        modified = false
    }

    fun clone(): ServicioAuxiliarModel {
        val nuevoServicioAuxiliar = ServicioAuxiliarModel()
        nuevoServicioAuxiliar.fromModel(this)
        return nuevoServicioAuxiliar
    }

    fun fromModel(other: ServicioAuxiliarModel) {
        val wasModified = modified
        id = other.id
        servicioId = other.servicioId
        linea = other.linea
        servicio = other.servicio
        turno = other.turno
        lineaAuxiliar = other.lineaAuxiliar
        servicioAuxiliar = other.servicioAuxiliar
        turnoAuxiliar = other.turnoAuxiliar
        inicio = other.inicio
        lugarInicio = other.lugarInicio
        final = other.final
        lugarFinal = other.lugarFinal
        modified = wasModified
    }


    fun getContentValues(): ContentValues {
        val valores = ContentValues()
        valores.put("ServicioId", servicioId)
        valores.put("Linea", linea)
        valores.put("Servicio", servicio)
        valores.put("Turno", turno)
        valores.put("LineaAuxiliar", lineaAuxiliar)
        valores.put("ServicioAuxiliar", servicioAuxiliar)
        valores.put("TurnoAuxiliar", turnoAuxiliar)
        valores.put("Inicio", inicio)
        valores.put("LugarInicio", lugarInicio)
        valores.put("Final", final)
        valores.put("LugarFinal", lugarFinal)
        return valores
    }

}