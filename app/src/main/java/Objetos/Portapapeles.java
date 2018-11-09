
/*
 * Copyright 2015 - Quattroid 1.0
 *
 * Creado por A. Herrero en Enero de 2015
 * http://sites.google.com/site/qtroid
 * acumulador.admin@gmail.com
 *
 * Este programa es software libre; usted puede redistruirlo y/o modificarlo bajo los términos
 * de la Licencia Pública General GNU, tal y como está publicada por la Free Software Foundation;
 * ya sea la versión 2 de la Licencia, o (a su elección) cualquier versión posterior.
 *
 * Este programa se distribuye con la intención de ser útil, pero SIN NINGUNA GARANTÍA;
 * incluso sin la garantía implícita de USABILIDAD O UTILIDAD PARA UN FIN PARTICULAR.
 * Vea la Licencia Pública General GNU en "assets/Licencia" para más detalles.
 */
package Objetos;

import BaseDatos.DatosDia;

public class Portapapeles {

    private static DatosDia diaGuardado = null;

    public static void copiar(DatosDia datosDia){
        diaGuardado = new DatosDia();
        diaGuardado.setDia(datosDia.getDia());
        diaGuardado.setMes(datosDia.getMes());
        diaGuardado.setAño(datosDia.getAño());
        diaGuardado.setDiaSemana(datosDia.getDiaSemana());
        diaGuardado.setCodigoIncidencia(datosDia.getCodigoIncidencia());
        diaGuardado.setTextoIncidencia(datosDia.getTextoIncidencia());
        diaGuardado.setTipoIncidencia(datosDia.getTipoIncidencia());
        diaGuardado.setServicio(datosDia.getServicio());
        diaGuardado.setTurno(datosDia.getTurno());
        diaGuardado.setLinea(datosDia.getLinea());
        diaGuardado.setTextoLinea(datosDia.getTextoLinea());
        diaGuardado.setInicio(datosDia.getInicio());
        diaGuardado.setFinal(datosDia.getFinal());
        diaGuardado.setAcumuladas(datosDia.getAcumuladas());
        diaGuardado.setNocturnas(datosDia.getNocturnas());
        diaGuardado.setTrabajadas(datosDia.getTrabajadas());
        diaGuardado.setDesayuno(datosDia.isDesayuno());
        diaGuardado.setComida(datosDia.isComida());
        diaGuardado.setCena(datosDia.isCena());
        diaGuardado.setMatricula(datosDia.getMatricula());
        diaGuardado.setApellidos(datosDia.getApellidos());
        diaGuardado.setCalificacion(datosDia.getCalificacion());
        diaGuardado.setMatriculaSusti(datosDia.getMatriculaSusti());
        diaGuardado.setApellidosSusti(datosDia.getApellidosSusti());
        diaGuardado.setBus(datosDia.getBus());
        diaGuardado.setNotas(datosDia.getNotas());
    }

    public static DatosDia pegar(DatosDia datosDia){
        if (diaGuardado == null) return datosDia;
        datosDia.setDia(diaGuardado.getDia());
        datosDia.setMes(diaGuardado.getMes());
        datosDia.setAño(diaGuardado.getAño());
        datosDia.setDiaSemana(diaGuardado.getDiaSemana());
        datosDia.setCodigoIncidencia(diaGuardado.getCodigoIncidencia());
        datosDia.setTextoIncidencia(diaGuardado.getTextoIncidencia());
        datosDia.setTipoIncidencia(diaGuardado.getTipoIncidencia());
        datosDia.setServicio(diaGuardado.getServicio());
        datosDia.setTurno(diaGuardado.getTurno());
        datosDia.setLinea(diaGuardado.getLinea());
        datosDia.setTextoLinea(diaGuardado.getTextoLinea());
        datosDia.setInicio(diaGuardado.getInicio());
        datosDia.setFinal(diaGuardado.getFinal());
        datosDia.setAcumuladas(diaGuardado.getAcumuladas());
        datosDia.setNocturnas(diaGuardado.getNocturnas());
        datosDia.setTrabajadas(diaGuardado.getTrabajadas());
        datosDia.setDesayuno(diaGuardado.isDesayuno());
        datosDia.setComida(diaGuardado.isComida());
        datosDia.setCena(diaGuardado.isCena());
        datosDia.setMatricula(diaGuardado.getMatricula());
        datosDia.setApellidos(diaGuardado.getApellidos());
        datosDia.setCalificacion(diaGuardado.getCalificacion());
        datosDia.setMatriculaSusti(diaGuardado.getMatriculaSusti());
        datosDia.setApellidosSusti(diaGuardado.getApellidosSusti());
        datosDia.setBus(diaGuardado.getBus());
        datosDia.setNotas(diaGuardado.getNotas());
        return datosDia;
    }

}
