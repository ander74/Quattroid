/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor
import com.quattroid.data.enums.TipoIncidencia

class IncidenciaModel : BaseModel {


    var codigo: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var incidencia: String = ""
        set(value) {
            field = value
            modified = true
        }
    var tipo: TipoIncidencia = TipoIncidencia.Ninguno
        set(value) {
            field = value
            modified = true
        }

    constructor() {}

    constructor(cursor: Cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
        codigo = cursor.getInt(cursor.getColumnIndexOrThrow("Codigo"))
        incidencia = cursor.getString(cursor.getColumnIndexOrThrow("Incidencia"))
        tipo = TipoIncidencia.entries[cursor.getInt(cursor.getColumnIndexOrThrow("Tipo"))]
        modified = false;
    }

    fun clone(): IncidenciaModel {
        val nuevoModel = IncidenciaModel()
        nuevoModel.fromModel(this)
        return nuevoModel
    }

    fun fromModel(other: IncidenciaModel) {
        val wasModified = modified
        this.id = other.id
        this.codigo = other.codigo
        this.incidencia = other.incidencia
        this.tipo = other.tipo
        modified = wasModified
    }

    fun getContentValues(): ContentValues {
        val valores = ContentValues()
        valores.put("Codigo", codigo)
        valores.put("Incidencia", incidencia)
        valores.put("Tipo", tipo.ordinal)
        return valores
    }

}