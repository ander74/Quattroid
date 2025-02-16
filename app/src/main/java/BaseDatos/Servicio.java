
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

public class Servicio {

    private int Id = 0;
    private String Linea = "";
    private String Servicio = "";
    private int Turno = 0;
    private String Inicio = "";
    private String Final = "";

    private String LugarInicio = "";
    private String LugarFinal = "";

    private String TomaDeje = "";
    private double Euros = 0d;

    private boolean Seleccionado = false;

    //region GETTERS

    public int getId() {
        return Id;
    }

    public String getLinea() {
        return Linea;
    }

    public String getServicio() {
        return Servicio;
    }

    public int getTurno() {
        return Turno;
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

    public String getTomaDeje() {
        return TomaDeje;
    }

    public double getEuros() {
        return Euros;
    }

    public boolean isSeleccionado(){
        return Seleccionado;
    }

    //endregion

    //region SETTERS

    public void setId(int id) {
        Id = id;
    }

    public void setLinea(String linea) {
        Linea = linea;
    }

    public void setServicio(String servicio) {
        Servicio = servicio;
    }

    public void setTurno(int turno) {
        Turno = turno;
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

    public void setTomaDeje(String tomaDeje) {
        TomaDeje = tomaDeje;
    }

    public void setEuros(double euros) {
        Euros = euros;
    }

    public void setSeleccionado(boolean seleccionado){
        Seleccionado = seleccionado;
    }

    //endregion


}
