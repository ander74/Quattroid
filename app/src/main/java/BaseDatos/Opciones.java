package BaseDatos;

import android.database.Cursor;

public class Opciones {


    //region CONSTRUCTORES

    public Opciones() {
    }

    public Opciones(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        primerMes = cursor.getInt(cursor.getColumnIndexOrThrow("PrimerMes"));
        primerAño = cursor.getInt(cursor.getColumnIndexOrThrow("PrimerAño"));
        acumuladasAnteriores = cursor.getDouble(cursor.getColumnIndexOrThrow("AcumuladasAnteriores"));
        relevoFijo = cursor.getInt(cursor.getColumnIndexOrThrow("RelevoFijo"));
        modoBasico = cursor.getInt(cursor.getColumnIndexOrThrow("ModoBasico")) > 0;
        rellenarSemana = cursor.getInt(cursor.getColumnIndexOrThrow("RellenarSemana")) > 0;
        jornadaMedia = cursor.getDouble(cursor.getColumnIndexOrThrow("JorMedia"));
        jornadaMinima = cursor.getDouble(cursor.getColumnIndexOrThrow("JorMinima"));
        limiteEntreServicios = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteEntreServicios"));
        jornadaAnual = cursor.getInt(cursor.getColumnIndexOrThrow("JornadaAnual"));
        regularJornadaAnual = cursor.getInt(cursor.getColumnIndexOrThrow("RegularJornadaAnual")) > 0;
        regularBisiestos = cursor.getInt(cursor.getColumnIndexOrThrow("RegularBisiestos")) > 0;
        inicioNocturnas = cursor.getInt(cursor.getColumnIndexOrThrow("InicioNocturnas"));
        finalNocturnas = cursor.getInt(cursor.getColumnIndexOrThrow("FinalNocturnas"));
        limiteDesayuno = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteDesayuno"));
        limiteComida1 = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteComida1"));
        limiteComida2 = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteComida2"));
        limiteCena = cursor.getInt(cursor.getColumnIndexOrThrow("LimiteCena"));
        inferirTurnos = cursor.getInt(cursor.getColumnIndexOrThrow("InferirTurnos")) > 0;
        diaBaseTurnos = cursor.getInt(cursor.getColumnIndexOrThrow("DiaBaseTurnos"));
        mesBaseTurnos = cursor.getInt(cursor.getColumnIndexOrThrow("MesBaseTurnos"));
        añoBaseTurnos = cursor.getInt(cursor.getColumnIndexOrThrow("AñoBaseTurnos"));
        pdfHorizontal = cursor.getInt(cursor.getColumnIndexOrThrow("PdfHorizontal")) > 0;
        pdfIncluirServicios = cursor.getInt(cursor.getColumnIndexOrThrow("PdfIncluirServicios")) > 0;
        pdfIncluirNotas = cursor.getInt(cursor.getColumnIndexOrThrow("PdfIncluirNotas")) > 0;
        pdfAgruparNotas = cursor.getInt(cursor.getColumnIndexOrThrow("PdfAgruparNotas")) > 0;
        verMesActual = cursor.getInt(cursor.getColumnIndexOrThrow("VerMesActual")) > 0;
        iniciarCalendario = cursor.getInt(cursor.getColumnIndexOrThrow("IniciarCalendario")) > 0;
        sumarTomaDeje = cursor.getInt(cursor.getColumnIndexOrThrow("SumarTomaDeje")) > 0;
        activarTecladoNumerico = cursor.getInt(cursor.getColumnIndexOrThrow("ActivarTecladoNumerico")) > 0;
        guardarSiempre = cursor.getInt(cursor.getColumnIndexOrThrow("GuardarSiempre")) > 0;
    }

    //endregion


    //region CAMPOS PRIVADOS

    private int id;
    private int primerMes = 10;
    private int primerAño = 2014;
    private double acumuladasAnteriores = 0;
    private int relevoFijo = 0;
    private boolean modoBasico = false;
    private boolean rellenarSemana = false;
    private double jornadaMedia = 0;
    private double jornadaMinima = 0;
    private int limiteEntreServicios = 60;
    private int jornadaAnual = 1592;
    private boolean regularJornadaAnual = true;
    private boolean regularBisiestos = true;
    private int inicioNocturnas = 1320;
    private int finalNocturnas = 390;
    private int limiteDesayuno = 270;
    private int limiteComida1 = 930;
    private int limiteComida2 = 810;
    private int limiteCena = 30;
    private boolean inferirTurnos = false;
    private int diaBaseTurnos = 3;
    private int mesBaseTurnos = 1;
    private int añoBaseTurnos = 2021;
    private boolean pdfHorizontal = false;
    private boolean pdfIncluirServicios = false;
    private boolean pdfIncluirNotas = false;
    private boolean pdfAgruparNotas = false;
    private boolean verMesActual = true;
    private boolean iniciarCalendario = false;
    private boolean sumarTomaDeje = false;
    private boolean activarTecladoNumerico = false;
    private boolean guardarSiempre = false;

    //endregion


    //region GETTERS

    public int getId() {
        return id;
    }

    public int getPrimerMes() {
        return primerMes;
    }

    public int getPrimerAño() {
        return primerAño;
    }

    public double getAcumuladasAnteriores() {
        return acumuladasAnteriores;
    }

    public int getRelevoFijo() {
        return relevoFijo;
    }

    public boolean isModoBasico() {
        return modoBasico;
    }

    public boolean isRellenarSemana() {
        return rellenarSemana;
    }

    public double getJornadaMedia() {
        return jornadaMedia;
    }

    public double getJornadaMinima() {
        return jornadaMinima;
    }

    public int getLimiteEntreServicios() {
        return limiteEntreServicios;
    }

    public int getJornadaAnual() {
        return jornadaAnual;
    }

    public boolean isRegularJornadaAnual() {
        return regularJornadaAnual;
    }

    public boolean isRegularBisiestos() {
        return regularBisiestos;
    }

    public int getInicioNocturnas() {
        return inicioNocturnas;
    }

    public int getFinalNocturnas() {
        return finalNocturnas;
    }

    public int getLimiteDesayuno() {
        return limiteDesayuno;
    }

    public int getLimiteComida1() {
        return limiteComida1;
    }

    public int getLimiteComida2() {
        return limiteComida2;
    }

    public int getLimiteCena() {
        return limiteCena;
    }

    public boolean isInferirTurnos() {
        return inferirTurnos;
    }

    public int getDiaBaseTurnos() {
        return diaBaseTurnos;
    }

    public int getMesBaseTurnos() {
        return mesBaseTurnos;
    }

    public int getAñoBaseTurnos() {
        return añoBaseTurnos;
    }

    public boolean isPdfHorizontal() {
        return pdfHorizontal;
    }

    public boolean isPdfIncluirServicios() {
        return pdfIncluirServicios;
    }

    public boolean isPdfIncluirNotas() {
        return pdfIncluirNotas;
    }

    public boolean isPdfAgruparNotas() {
        return pdfAgruparNotas;
    }

    public boolean isVerMesActual() {
        return verMesActual;
    }

    public boolean isIniciarCalendario() {
        return iniciarCalendario;
    }

    public boolean isSumarTomaDeje() {
        return sumarTomaDeje;
    }

    public boolean isActivarTecladoNumerico() {
        return activarTecladoNumerico;
    }

    public boolean isGuardarSiempre() {
        return guardarSiempre;
    }


    //endregion


    //region SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setPrimerMes(int primerMes) {
        this.primerMes = primerMes;
    }

    public void setPrimerAño(int primerAño) {
        this.primerAño = primerAño;
    }

    public void setAcumuladasAnteriores(double acumuladasAnteriores) {
        this.acumuladasAnteriores = acumuladasAnteriores;
    }

    public void setRelevoFijo(int relevoFijo) {
        this.relevoFijo = relevoFijo;
    }

    public void setModoBasico(boolean modoBasico) {
        this.modoBasico = modoBasico;
    }

    public void setRellenarSemana(boolean rellenarSemana) {
        this.rellenarSemana = rellenarSemana;
    }

    public void setJornadaMedia(double jornadaMedia) {
        this.jornadaMedia = jornadaMedia;
    }

    public void setJornadaMinima(double jornadaMinima) {
        this.jornadaMinima = jornadaMinima;
    }

    public void setLimiteEntreServicios(int limiteEntreServicios) {
        this.limiteEntreServicios = limiteEntreServicios;
    }

    public void setJornadaAnual(int jornadaAnual) {
        this.jornadaAnual = jornadaAnual;
    }

    public void setRegularJornadaAnual(boolean regularJornadaAnual) {
        this.regularJornadaAnual = regularJornadaAnual;
    }

    public void setRegularBisiestos(boolean regularBisiestos) {
        this.regularBisiestos = regularBisiestos;
    }

    public void setInicioNocturnas(int inicioNocturnas) {
        this.inicioNocturnas = inicioNocturnas;
    }

    public void setFinalNocturnas(int finalNocturnas) {
        this.finalNocturnas = finalNocturnas;
    }

    public void setLimiteDesayuno(int limiteDesayuno) {
        this.limiteDesayuno = limiteDesayuno;
    }

    public void setLimiteComida1(int limiteComida1) {
        this.limiteComida1 = limiteComida1;
    }

    public void setLimiteComida2(int limiteComida2) {
        this.limiteComida2 = limiteComida2;
    }

    public void setLimiteCena(int limiteCena) {
        this.limiteCena = limiteCena;
    }

    public void setInferirTurnos(boolean inferirTurnos) {
        this.inferirTurnos = inferirTurnos;
    }

    public void setDiaBaseTurnos(int diaBaseTurnos) {
        this.diaBaseTurnos = diaBaseTurnos;
    }

    public void setMesBaseTurnos(int mesBaseTurnos) {
        this.mesBaseTurnos = mesBaseTurnos;
    }

    public void setAñoBaseTurnos(int añoBaseTurnos) {
        this.añoBaseTurnos = añoBaseTurnos;
    }

    public void setPdfHorizontal(boolean pdfHorizontal) {
        this.pdfHorizontal = pdfHorizontal;
    }

    public void setPdfIncluirServicios(boolean pdfIncluirServicios) {
        this.pdfIncluirServicios = pdfIncluirServicios;
    }

    public void setPdfIncluirNotas(boolean pdfIncluirNotas) {
        this.pdfIncluirNotas = pdfIncluirNotas;
    }

    public void setPdfAgruparNotas(boolean pdfAgruparNotas) {
        this.pdfAgruparNotas = pdfAgruparNotas;
    }

    public void setVerMesActual(boolean verMesActual) {
        this.verMesActual = verMesActual;
    }

    public void setIniciarCalendario(boolean iniciarCalendario) {
        this.iniciarCalendario = iniciarCalendario;
    }

    public void setSumarTomaDeje(boolean sumarTomaDeje) {
        this.sumarTomaDeje = sumarTomaDeje;
    }

    public void setActivarTecladoNumerico(boolean activarTecladoNumerico) {
        this.activarTecladoNumerico = activarTecladoNumerico;
    }

    public void setGuardarSiempre(boolean guardarSiempre) {
        this.guardarSiempre = guardarSiempre;
    }


    //endregion


}
