/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.repositories

import BaseDatos.BaseHelper
import BaseDatos.DatabaseConstants
import BaseDatos.DatabaseConstants.DB_VERSION
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import com.quattroid.data.models.LineaModel
import com.quattroid.data.models.ServicioAuxiliarModel
import com.quattroid.data.models.ServicioModel

class LineaRepository {

    //private val db: BaseDatos
    private val baseDatos: SQLiteDatabase;

    constructor(context: Context) {
        val baseHelper: BaseHelper = BaseHelper.getInstance(context, DB_VERSION)
        baseDatos = baseHelper.writableDatabase
    }

    fun getLineas(incluirServicios: Boolean): ArrayList<LineaModel> {
        val orderBy = "Linea ASC"
        val cursorLineas: Cursor = baseDatos.query(DatabaseConstants.TABLA_LINEAS, null, null, null, null, null, orderBy)
        val lineas = ArrayList<LineaModel>()
        if (cursorLineas.moveToFirst()) {
            do {
                val linea = LineaModel(cursorLineas)
                if (incluirServicios) linea.servicios = getServicios(linea.id)
                lineas.add(linea)
            } while (cursorLineas.moveToNext())
            cursorLineas.close()
        }
        return lineas
    }

    fun getServicios(lineaId: Int): ArrayList<ServicioModel> {
        val where = "LineaId=?"
        val args = arrayOf(lineaId.toString())
        val orderBy = "Servicio ASC, Turno ASC"
        val cursorServicios: Cursor = baseDatos.query(DatabaseConstants.TABLA_SERVICIOS, null, where, args, null, null, orderBy)
        val servicios = ArrayList<ServicioModel>()
        if (cursorServicios.moveToFirst()) {
            do {
                val servicio = ServicioModel(cursorServicios)
                servicio.servicios = getServiciosAuxiliares(servicio.id)
                servicios.add(servicio)
            } while (cursorServicios.moveToNext())
            cursorServicios.close()
        }
        return servicios
    }

    fun getServiciosAuxiliares(servicioId: Int): ArrayList<ServicioAuxiliarModel> {
        val where = "ServicioId=?"
        val args = arrayOf(servicioId.toString())
        val orderBy = "ServicioAuxiliar ASC, TurnoAuxiliar ASC"
        val cursorServiciosAuxiliares: Cursor = baseDatos.query(DatabaseConstants.TABLA_SERVICIOS_AUXILIARES, null, where, args, null, null, orderBy)
        val serviciosAuxiliares = ArrayList<ServicioAuxiliarModel>()
        if (cursorServiciosAuxiliares.moveToFirst()) {
            do {
                val servicioAuxiliar = ServicioAuxiliarModel(cursorServiciosAuxiliares)
                serviciosAuxiliares.add(servicioAuxiliar)
            } while (cursorServiciosAuxiliares.moveToNext())
            cursorServiciosAuxiliares.close()
        }
        return serviciosAuxiliares
    }

    fun insertOrUpdateLineas(lineas: List<LineaModel>) {
        for (linea: LineaModel in lineas) {
            if (linea.id == 0) insertLinea(linea)
            if (linea.id > 0) updateLinea(linea)
        }
    }

    fun insertLinea(linea: LineaModel) {
        val valores = linea.getContentValues()
        baseDatos.insert(DatabaseConstants.TABLA_LINEAS, null, valores)
        val lastId: Int = baseDatos.lastInsertRowId.toInt()
        if (linea.servicios.any()) {
            linea.servicios.forEach { it -> it.lineaId = lastId }
            insertServicios(linea.servicios)
        }
    }

    fun insertLineas(lineas: List<LineaModel>) {
        for (linea: LineaModel in lineas) {
            insertLinea(linea)
        }
    }

    fun updateLinea(linea: LineaModel) {
        val valores = linea.getContentValues()
        val where = "_id=?"
        val args = arrayOf(linea.id.toString())
        baseDatos.update(DatabaseConstants.TABLA_LINEAS, valores, where, args)
        val lastId: Int = baseDatos.lastInsertRowId.toInt()
        if (linea.servicios.any()) {
            linea.servicios.forEach { it -> it.lineaId = lastId }
            updateServicios(linea.servicios)
        }
    }

    fun updateLineas(lineas: List<LineaModel>) {
        for (linea: LineaModel in lineas) {
            updateLinea(linea)
        }
    }

    fun insertServicio(servicio: ServicioModel) {
        var valores = servicio.getContentValues()
        baseDatos.insert(DatabaseConstants.TABLA_SERVICIOS, null, valores)
        val lastId: Int = baseDatos.lastInsertRowId.toInt()
        if (servicio.servicios.any()) {
            servicio.servicios.forEach { it -> it.servicioId = lastId }
            insertServiciosAuxiliares(servicio.servicios)
        }
    }

    fun insertServicios(servicios: List<ServicioModel>) {
        for (servicio: ServicioModel in servicios) {
            insertServicio(servicio)
        }
    }

    fun updateServicio(servicio: ServicioModel) {
        var valores = servicio.getContentValues()
        val where = "_id=?"
        val args = arrayOf(servicio.id.toString())
        baseDatos.update(DatabaseConstants.TABLA_SERVICIOS, valores, where, args)
        val lastId: Int = baseDatos.lastInsertRowId.toInt()
        if (servicio.servicios.any()) {
            servicio.servicios.forEach { it -> it.servicioId = lastId }
            updateServiciosAuxiliares(servicio.servicios)
        }
    }

    fun updateServicios(servicios: List<ServicioModel>) {
        for (servicio: ServicioModel in servicios) {
            updateServicio(servicio)
        }
    }

    fun insertServicioAuxiliar(servicio: ServicioAuxiliarModel) {
        var valores = servicio.getContentValues()
        baseDatos.insert(DatabaseConstants.TABLA_SERVICIOS, null, valores)
    }

    fun insertServiciosAuxiliares(servicios: List<ServicioAuxiliarModel>) {
        for (servicio: ServicioAuxiliarModel in servicios) {
            insertServicioAuxiliar(servicio)
        }
    }

    fun updateServicioAuxiliar(servicio: ServicioAuxiliarModel) {
        var valores = servicio.getContentValues()
        val where = "_id=?"
        val args = arrayOf(servicio.id.toString())
        baseDatos.update(DatabaseConstants.TABLA_SERVICIOS, valores, where, args)
    }

    fun updateServiciosAuxiliares(servicios: List<ServicioAuxiliarModel>) {
        for (servicio: ServicioAuxiliarModel in servicios) {
            updateServicioAuxiliar(servicio)
        }
    }

    fun deleteLinea(id: Int) {
        val args: Array<String> = arrayOf(id.toString())
        val where = "_id=?"
        baseDatos.delete(DatabaseConstants.TABLA_LINEAS, where, args)
    }

    fun deleteServicio(id: Int) {
        val args: Array<String> = arrayOf(id.toString())
        val where = "_id=?"
        baseDatos.delete(DatabaseConstants.TABLA_SERVICIOS, where, args)
    }

    fun deleteServicioAuxiliar(id: Int) {
        val args: Array<String> = arrayOf(id.toString())
        val where = "_id=?"
        baseDatos.delete(DatabaseConstants.TABLA_SERVICIOS_AUXILIARES, where, args)
    }

    fun countLineas(): Int {
        val sql = "SELECT COUNT(*) FROM Lineas"
        val statement: SQLiteStatement = baseDatos.compileStatement(sql)
        val count: Int = statement.simpleQueryForLong().toInt()
        return count
    }

}