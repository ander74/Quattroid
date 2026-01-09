/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor

class HoraAjenaModel : BaseModel {


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
    var motivo: String = ""
        set(value) {
            field = value
            modified = true
        }
    var horas: Double = 0.0
        set(value) {
            field = value
            modified = true
        }

    constructor() {}

    constructor(cursor: Cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
        dia = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"))
        mes = cursor.getInt(cursor.getColumnIndexOrThrow("Mes"))
        año = cursor.getInt(cursor.getColumnIndexOrThrow("Año"))
        motivo = cursor.getString(cursor.getColumnIndexOrThrow("Motivo"))
        horas = cursor.getDouble(cursor.getColumnIndexOrThrow("Horas"))
        modified = false;
    }

    fun clone(): HoraAjenaModel {
        val nuevoModel = HoraAjenaModel()
        nuevoModel.fromModel(this)
        return nuevoModel
    }

    fun fromModel(other: HoraAjenaModel) {
        val wasModified = modified
        this.id = other.id
        this.dia = other.dia
        this.mes = other.mes
        this.año = other.año
        this.motivo = other.motivo
        this.horas = other.horas
        modified = wasModified
    }

    fun getContentValues(): ContentValues {
        val valores = ContentValues()
        valores.put("Dia", dia)
        valores.put("Mes", mes)
        valores.put("Año", año)
        valores.put("Motivo", motivo)
        valores.put("Horas", horas)
        return valores
    }

}