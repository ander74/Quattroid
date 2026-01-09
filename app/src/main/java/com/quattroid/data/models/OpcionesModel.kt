/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.data.models

import android.content.ContentValues
import android.database.Cursor

class OpcionesModel : BaseModel {


    var primerMes: Int = 10
        set(value) {
            field = value
            modified = true
        }
    var primerAño: Int = 2014
        set(value) {
            field = value
            modified = true
        }
    var acumuladasAnteriores: Double = 0.0
        set(value) {
            field = value
            modified = true
        }
    var relevoFijo: Int = 0
        set(value) {
            field = value
            modified = true
        }
    var modoBasico: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var rellenarSemana: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var jornadaMedia: Double = 7.75
        set(value) {
            field = value
            modified = true
        }
    var jornadaMinima: Double = 7.0
        set(value) {
            field = value
            modified = true
        }
    var limiteEntreServicios: Int = 60
        set(value) {
            field = value
            modified = true
        }
    var diaCierreMes: Int = 1
        set(value) {
            field = value
            modified = true
        }
    var jornadaAnual: Int = 1592
        set(value) {
            field = value
            modified = true
        }
    var regularJornadaAnual: Boolean = true
        set(value) {
            field = value
            modified = true
        }
    var regularBisiestos: Boolean = true
        set(value) {
            field = value
            modified = true
        }
    var inicioNocturnas: Int = 1320
        set(value) {
            field = value
            modified = true
        }
    var finalNocturnas: Int = 390
        set(value) {
            field = value
            modified = true
        }
    var limiteDesayuno: Int = 270
        set(value) {
            field = value
            modified = true
        }
    var limiteComida1: Int = 930
        set(value) {
            field = value
            modified = true
        }
    var limiteComida2: Int = 810
        set(value) {
            field = value
            modified = true
        }
    var limiteCena: Int = 30
        set(value) {
            field = value
            modified = true
        }
    var inferirTurnos: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var diaBaseTurnos: Int = 3
        set(value) {
            field = value
            modified = true
        }
    var mesBaseTurnos: Int = 1
        set(value) {
            field = value
            modified = true
        }
    var añoBaseTurnos: Int = 2021
        set(value) {
            field = value
            modified = true
        }
    var pdfHorizontal: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var pdfIncluirServicios: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var pdfIncluirNotas: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var pdfAgruparNotas: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var verMesActual: Boolean = true
        set(value) {
            field = value
            modified = true
        }
    var iniciarCalendario: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var sumarTomaDeje: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var activarTecladoNumerico: Boolean = false
        set(value) {
            field = value
            modified = true
        }
    var guardarSiempre: Boolean = false
        set(value) {
            field = value
            modified = true
        }


    constructor() {}


    constructor(cursor: Cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
        primerMes = cursor.getInt(cursor.getColumnIndexOrThrow("PrimerMes"))
        primerAño = cursor.getInt(cursor.getColumnIndexOrThrow("PrimerAño"))
        acumuladasAnteriores = cursor.getDouble(cursor.getColumnIndexOrThrow("AcumuladasAnteriores"))
        relevoFijo = cursor.getInt(cursor.getColumnIndexOrThrow("RelevoFijo"))
        modoBasico = cursor.getInt(cursor.getColumnIndexOrThrow("ModoBasico")) > 0
        rellenarSemana = cursor.getInt(cursor.getColumnIndexOrThrow("RellenarSemana")) > 0
        jornadaMedia = cursor.getDouble(cursor.getColumnIndexOrThrow("JorMedia"))
        jornadaMinima = cursor.getDouble(cursor.getColumnIndexOrThrow("JorMinima"))
        limiteEntreServicios = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteEntreServicios"))
        diaCierreMes = cursor.getInt(cursor.getColumnIndexOrThrow("DiaCierreMes"))
        jornadaAnual = cursor.getInt(cursor.getColumnIndexOrThrow("JornadaAnual"))
        regularJornadaAnual = cursor.getInt(cursor.getColumnIndexOrThrow("RegularJornadaAnual")) > 0
        regularBisiestos = cursor.getInt(cursor.getColumnIndexOrThrow("RegularBisiestos")) > 0
        inicioNocturnas = cursor.getInt(cursor.getColumnIndexOrThrow("InicioNocturnas"))
        finalNocturnas = cursor.getInt(cursor.getColumnIndexOrThrow("FinalNocturnas"))
        limiteDesayuno = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteDesayuno"))
        limiteComida1 = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteComida1"))
        limiteComida2 = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteComida2"))
        limiteCena = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteCena"))
        inferirTurnos = cursor.getInt(cursor.getColumnIndexOrThrow("InferirTurnos")) > 0
        diaBaseTurnos = cursor.getInt(cursor.getColumnIndexOrThrow("DiaBaseTurnos"))
        mesBaseTurnos = cursor.getInt(cursor.getColumnIndexOrThrow("MesBaseTurnos"))
        añoBaseTurnos = cursor.getInt(cursor.getColumnIndexOrThrow("AñoBaseTurnos"))
        pdfHorizontal = cursor.getInt(cursor.getColumnIndexOrThrow("PdfHorizontal")) > 0
        pdfIncluirServicios = cursor.getInt(cursor.getColumnIndexOrThrow("PdfIncluirServicios")) > 0
        pdfIncluirNotas = cursor.getInt(cursor.getColumnIndexOrThrow("PdfIncluirNotas")) > 0
        pdfAgruparNotas = cursor.getInt(cursor.getColumnIndexOrThrow("PdfAgruparNotas")) > 0
        verMesActual = cursor.getInt(cursor.getColumnIndexOrThrow("VerMesActual")) > 0
        iniciarCalendario = cursor.getInt(cursor.getColumnIndexOrThrow("IniciarCalendario")) > 0
        sumarTomaDeje = cursor.getInt(cursor.getColumnIndexOrThrow("SumarTomaDeje")) > 0
        activarTecladoNumerico = cursor.getInt(cursor.getColumnIndexOrThrow("ActivarTecladoNumerico")) > 0
        guardarSiempre = cursor.getInt(cursor.getColumnIndexOrThrow("GuardarSiempre")) > 0
        modified = false;
    }

    fun clone(): OpcionesModel {
        val nuevoModel = OpcionesModel()
        nuevoModel.fromModel(this)
        return nuevoModel
    }

    fun fromModel(other: OpcionesModel) {
        val wasModified = modified
        primerMes = other.primerMes
        primerAño = other.primerAño
        acumuladasAnteriores = other.acumuladasAnteriores
        relevoFijo = other.relevoFijo
        modoBasico = other.modoBasico
        rellenarSemana = other.rellenarSemana
        jornadaMedia = other.jornadaMedia
        jornadaMinima = other.jornadaMinima
        limiteEntreServicios = other.limiteEntreServicios
        diaCierreMes = other.diaCierreMes
        jornadaAnual = other.jornadaAnual
        regularJornadaAnual = other.regularJornadaAnual
        regularBisiestos = other.regularBisiestos
        inicioNocturnas = other.inicioNocturnas
        finalNocturnas = other.finalNocturnas
        limiteDesayuno = other.limiteDesayuno
        limiteComida1 = other.limiteComida1
        limiteComida2 = other.limiteComida2
        limiteCena = other.limiteCena
        inferirTurnos = other.inferirTurnos
        diaBaseTurnos = other.diaBaseTurnos
        mesBaseTurnos = other.mesBaseTurnos
        añoBaseTurnos = other.añoBaseTurnos
        pdfHorizontal = other.pdfHorizontal
        pdfIncluirServicios = other.pdfIncluirServicios
        pdfIncluirNotas = other.pdfIncluirNotas
        pdfAgruparNotas = other.pdfAgruparNotas
        verMesActual = other.verMesActual
        iniciarCalendario = other.iniciarCalendario
        sumarTomaDeje = other.sumarTomaDeje
        activarTecladoNumerico = other.activarTecladoNumerico
        guardarSiempre = other.guardarSiempre
        modified = wasModified
    }

    fun getContentValues(): ContentValues {
        val valores = ContentValues()
        valores.put("PrimerMes", primerMes)
        valores.put("PrimerAño", primerAño)
        valores.put("AcumuladasAnteriores", acumuladasAnteriores)
        valores.put("RelevoFijo", relevoFijo)
        valores.put("ModoBasico", modoBasico)
        valores.put("RellenarSemana", rellenarSemana)
        valores.put("JorMedia", jornadaMedia)
        valores.put("JorMinima", jornadaMinima)
        valores.put("LimiteEntreServicios", limiteEntreServicios)
        valores.put("DiaCierreMes", diaCierreMes)
        valores.put("JornadaAnual", jornadaAnual)
        valores.put("RegularJornadaAnual", regularJornadaAnual)
        valores.put("RegularBisiestos", regularBisiestos)
        valores.put("InicioNocturnas", inicioNocturnas)
        valores.put("FinalNocturnas", finalNocturnas)
        valores.put("LimiteDesayuno", limiteDesayuno)
        valores.put("LimiteComida1", limiteComida1)
        valores.put("LimiteComida2", limiteComida2)
        valores.put("LimiteCena", limiteCena)
        valores.put("InferirTurnos", inferirTurnos)
        valores.put("DiaBaseTurnos", diaBaseTurnos)
        valores.put("MesBaseTurnos", mesBaseTurnos)
        valores.put("AñoBaseTurnos", añoBaseTurnos)
        valores.put("PdfHorizontal", pdfHorizontal)
        valores.put("PdfIncluirServicios", pdfIncluirServicios)
        valores.put("PdfIncluirNotas", pdfIncluirNotas)
        valores.put("PdfAgruparNotas", pdfAgruparNotas)
        valores.put("VerMesActual", verMesActual)
        valores.put("IniciarCalendario", iniciarCalendario)
        valores.put("SumarTomaDeje", sumarTomaDeje)
        valores.put("ActivarTecladoNumerico", activarTecladoNumerico)
        valores.put("GuardarSiempre", guardarSiempre)
        return valores
    }

}