/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor
import com.quattroid.data.enums.CalificacionRelevo

class RelevoModel : BaseModel {


    var matricula: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var nombre: String = ""
        set(value) {
            field = value
            modified = true
        }
    var apellidos: String = ""
        set(value) {
            field = value
            modified = true
        }
    var telefono: String = ""
        set(value) {
            field = value
            modified = true
        }
    var calificacion: CalificacionRelevo = CalificacionRelevo.Normal
        set(value) {
            field = value
            modified = true
        }
    var deuda: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var notas: String = ""
        set(value) {
            field = value
            modified = true
        }

    constructor() {}

    constructor(cursor: Cursor) {
        matricula = cursor.getInt(cursor.getColumnIndexOrThrow("Matricula"))
        nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
        apellidos = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"))
        telefono = cursor.getString(cursor.getColumnIndexOrThrow("Telefono"))
        calificacion = CalificacionRelevo.entries[cursor.getInt(cursor.getColumnIndexOrThrow("Calificacion"))]
        deuda = cursor.getInt(cursor.getColumnIndexOrThrow("Deuda"))
        notas = cursor.getString(cursor.getColumnIndexOrThrow("Notas"))
        modified = false;
    }

    fun clone(): RelevoModel {
        val nuevoModel = RelevoModel()
        nuevoModel.fromModel(this)
        return nuevoModel
    }

    fun fromModel(other: RelevoModel) {
        val wasModified = modified
        this.matricula = other.matricula
        this.apellidos = other.apellidos
        this.nombre = other.nombre
        this.telefono = other.telefono
        this.calificacion = other.calificacion
        this.deuda = other.deuda
        this.notas = other.notas
        modified = wasModified
    }

    fun getContentValues(): ContentValues {
        val valores = ContentValues()
        valores.put("Matricula", matricula)
        valores.put("Apellidos", apellidos)
        valores.put("Nombre", nombre)
        valores.put("Telefono", telefono)
        valores.put("Calificacion", calificacion.ordinal)
        valores.put("Deuda", deuda)
        valores.put("Notas", notas)
        return valores
    }

}