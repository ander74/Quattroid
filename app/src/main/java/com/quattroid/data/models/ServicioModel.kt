/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor

class ServicioModel : BaseModel {


    var lineaId: Int = 0
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

    var tomaDeje: String = ""
        set(value) {
            field = value
            modified = true
        }

    var euros: Double = 0.0
        set(value) {
            field = value
            modified = true
        }

    var servicios: ArrayList<ServicioAuxiliarModel> = ArrayList()

    constructor() {}

    constructor(cursor: Cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
        lineaId = cursor.getInt(cursor.getColumnIndexOrThrow("LineaId"))
        linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"))
        servicio = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"))
        turno = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"))
        inicio = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"))
        lugarInicio = cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio"))
        final = cursor.getString(cursor.getColumnIndexOrThrow("Final"))
        lugarFinal = cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal"))
        tomaDeje = cursor.getString(cursor.getColumnIndexOrThrow("TomaDeje"))
        euros = cursor.getDouble(cursor.getColumnIndexOrThrow("Euros"))
        modified = false;
    }


    fun clone(): ServicioModel {
        val nuevoServicio = ServicioModel()
        nuevoServicio.fromModel(this)
        return nuevoServicio
    };

    fun fromModel(other: ServicioModel) {
        val wasModified = modified
        id = other.id
        lineaId = other.lineaId
        linea = other.linea
        servicio = other.servicio
        turno = other.turno
        inicio = other.inicio
        lugarInicio = other.lugarInicio
        final = other.final
        lugarFinal = other.lugarFinal
        tomaDeje = other.tomaDeje
        euros = other.euros
        servicios = ArrayList(other.servicios.toList())
        modified = wasModified
    }


    fun getContentValues(): ContentValues {
        val valores = ContentValues()
        valores.put("LineaId", lineaId)
        valores.put("Linea", linea)
        valores.put("Servicio", servicio)
        valores.put("Turno", turno)
        valores.put("Inicio", inicio)
        valores.put("LugarInicio", lugarInicio)
        valores.put("Final", final)
        valores.put("LugarFinal", lugarFinal)
        valores.put("TomaDeje", tomaDeje)
        valores.put("Euros", euros)
        return valores
    }
}