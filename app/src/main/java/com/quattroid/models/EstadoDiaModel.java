
/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.models;

public class EstadoDiaModel {

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
