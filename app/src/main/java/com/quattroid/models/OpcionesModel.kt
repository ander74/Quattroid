/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.models

import android.database.Cursor

class OpcionesModel {

    constructor() {

    }


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
    }

    var id = 0
    var primerMes = 10
    var primerAño = 2014
    var acumuladasAnteriores = 0.0
    var relevoFijo = 0
    var modoBasico = false
    var rellenarSemana = false
    var jornadaMedia = 7.75
    var jornadaMinima = 7.0
    var limiteEntreServicios = 60
    var diaCierreMes = 1
    var jornadaAnual = 1592
    var regularJornadaAnual = true
    var regularBisiestos = true
    var inicioNocturnas = 1320
    var finalNocturnas = 390
    var limiteDesayuno = 270
    var limiteComida1 = 930
    var limiteComida2 = 810
    var limiteCena = 30
    var inferirTurnos = false
    var diaBaseTurnos = 3
    var mesBaseTurnos = 1
    var añoBaseTurnos = 2021
    var pdfHorizontal = false
    var pdfIncluirServicios = false
    var pdfIncluirNotas = false
    var pdfAgruparNotas = false
    var verMesActual = true
    var iniciarCalendario = false
    var sumarTomaDeje = false
    var activarTecladoNumerico = false
    var guardarSiempre = false


}