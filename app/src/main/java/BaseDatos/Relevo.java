
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

public class Relevo {

    private int Matricula = 0;
    private String Nombre = "";
    private String Apellidos = "";
    private String Telefono = "";
    private int Calificacion = 0;
    private int Deuda = 0;
    private String Notas = "";


    //region GETTERS

    public int getMatricula() {
        return Matricula;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public String getTelefono() {
        return Telefono;
    }

    public int getCalificacion() {
        return Calificacion;
    }

    public int getDeuda() {
        return Deuda;
    }

    public String getNotas() {
        return Notas;
    }


    //endregion

    //region SETTERS

    public void setMatricula(int matricula) {
        Matricula = matricula;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public void setCalificacion(int calificacion) {
        Calificacion = calificacion;
    }

    public void setDeuda(int deuda) {
        Deuda = deuda;
    }

    public void setNotas(String notas) {
        Notas = notas;
    }


    //endregion


}
