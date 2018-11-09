
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

public class EstadoDia {

    private double Trabajadas;
    private double Acumuladas;
    private double Nocturnas;
    private boolean Desayuno;
    private boolean Comida;
    private boolean Cena;


    //region GETTERS

    public double getTrabajadas() {
        return Trabajadas;
    }

    public double getAcumuladas() {
        return Acumuladas;
    }

    public double getNocturnas() {
        return Nocturnas;
    }

    public boolean isDesayuno() {
        return Desayuno;
    }

    public boolean isComida() {
        return Comida;
    }

    public boolean isCena() {
        return Cena;
    }


    //endregion


    //region SETTERS

    public void setTrabajadas(double trabajadas) {
        Trabajadas = trabajadas;
    }

    public void setAcumuladas(double acumuladas) {
        Acumuladas = acumuladas;
    }

    public void setNocturnas(double nocturnas) {
        Nocturnas = nocturnas;
    }

    public void setDesayuno(boolean desayuno) {
        Desayuno = desayuno;
    }

    public void setComida(boolean comida) {
        Comida = comida;
    }

    public void setCena(boolean cena) {
        Cena = cena;
    }


    //endregion


}
