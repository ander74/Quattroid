/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor

class LineaModel : BaseModel {

    var linea: String = ""
        set(value) {
            field = value
            modified = true
        }

    var texto: String = ""
        set(value) {
            field = value
            modified = true
        }

    var servicios: ArrayList<ServicioModel> = ArrayList()

    constructor() {}

    constructor(cursor: Cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
        linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"))
        texto = cursor.getString(cursor.getColumnIndexOrThrow("Texto"))
        modified = false;

    }

    fun clone(): LineaModel {
        val nuevoModel = LineaModel()
        nuevoModel.fromModel(this)
        return nuevoModel
    }

    fun fromModel(other: LineaModel) {
        val wasModified = modified
        id = other.id
        linea = other.linea
        texto = other.texto
        servicios = ArrayList(other.servicios.toList())
        modified = wasModified
    }

    fun getContentValues(): ContentValues {
        val valores = ContentValues()
        valores.put("Linea", linea)
        valores.put("Texto", texto)
        return valores
    }
}