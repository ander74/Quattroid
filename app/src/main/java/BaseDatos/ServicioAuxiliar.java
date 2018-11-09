
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
package BaseDatos;

public class ServicioAuxiliar {

    private String Linea;
    private String Servicio;
    private int Turno;
    private String LineaAuxiliar;
    private String ServicioAuxiliar;
    private int TurnoAuxiliar;
    private String Inicio;
    private String Final;

    private String LugarInicio = "";
    private String LugarFinal = "";

    //region GETTERS

    public String getLinea() {
        return Linea;
    }

    public String getServicio() {
        return Servicio;
    }

    public int getTurno() {
        return Turno;
    }

    public String getLineaAuxiliar() {
        return LineaAuxiliar;
    }

    public String getServicioAuxiliar() {
        return ServicioAuxiliar;
    }

    public int getTurnoAuxiliar() {
        return TurnoAuxiliar;
    }

    public String getInicio() {
        return Inicio;
    }

    public String getFinal() {
        return Final;
    }

    public String getLugarInicio() {
        return LugarInicio;
    }

    public String getLugarFinal() {
        return LugarFinal;
    }


    //endregion

    //region SETTERS

    public void setLinea(String linea) {
        Linea = linea;
    }

    public void setServicio(String servicio) {
        Servicio = servicio;
    }

    public void setTurno(int turno) {
        Turno = turno;
    }

    public void setLineaAuxiliar(String lineaAuxiliar) {
        LineaAuxiliar = lineaAuxiliar;
    }

    public void setServicioAuxiliar(String servicioAuxiliar) {
        ServicioAuxiliar = servicioAuxiliar;
    }

    public void setTurnoAuxiliar(int turnoAuxiliar) {
        TurnoAuxiliar = turnoAuxiliar;
    }

    public void setInicio(String inicio) {
        Inicio = inicio;
    }

    public void setFinal(String aFinal) {
        Final = aFinal;
    }

    public void setLugarInicio(String lugarInicio) {
        LugarInicio = lugarInicio;
    }

    public void setLugarFinal(String lugarFinal) {
        LugarFinal = lugarFinal;
    }


    //endregion


}
