/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor

class ServicioDiaModel : BaseModel {


    var diaId: Int = 0
        set(value) {
            field = value
            modified = true
        }
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

    constructor() {}

    constructor(cursor: Cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
        diaId = cursor.getInt(cursor.getColumnIndexOrThrow("DiaId"))
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"))
        mes = cursor.getInt(cursor.getColumnIndexOrThrow("Mes"))
        año = cursor.getInt(cursor.getColumnIndexOrThrow("Año"))
        linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"))
        servicio = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"))
        turno = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"))
        inicio = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"))
        lugarInicio = cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio"))
        final = cursor.getString(cursor.getColumnIndexOrThrow("Final"))
        lugarFinal = cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal"))
        modified = false;
    }

    fun clone(): ServicioDiaModel {
        val nuevoModel = ServicioDiaModel()
        nuevoModel.fromModel(this)
        return nuevoModel
    }

    fun fromModel(other: ServicioDiaModel) {
        val wasModified = modified
        this.id = other.id
        this.diaId = other.diaId
        this.dia = other.dia
        this.mes = other.mes
        this.año = other.año
        this.linea = other.linea
        this.servicio = other.servicio
        this.turno = other.turno
        this.inicio = other.inicio
        this.lugarInicio = other.lugarInicio
        this.final = other.final
        this.lugarFinal = other.lugarFinal
        modified = wasModified
    }

    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put("DiaId", diaId)
        values.put("Dia", dia)
        values.put("Mes", mes)
        values.put("Año", año)
        values.put("Linea", linea)
        values.put("Servicio", servicio)
        values.put("Turno", turno)
        values.put("Inicio", inicio)
        values.put("LugarInicio", lugarInicio)
        values.put("Final", final)
        values.put("LugarFinal", lugarFinal)
        return values
    }


}