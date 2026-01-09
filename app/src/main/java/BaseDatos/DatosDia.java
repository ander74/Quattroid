
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

import android.database.Cursor;

public class DatosDia {


    //region VARIABLES PRIVADAS

    private int Id = 0;
    private int Dia = 0;
    private int Mes = 0;
    private int Año = 0;
    private int DiaSemana = 0;
    private boolean EsFranqueo = false;
    private boolean EsFestivo = false;
    private int CodigoIncidencia = 0;
    private String TextoIncidencia = "";
    private int TipoIncidencia = 0;
    private String Servicio = "";
    private int Turno = 0;
    private String Linea = "";
    private String TextoLinea = "";
    private String Inicio = "";
    private String Final = "";
    private double Acumuladas = 0d;
    private double Nocturnas = 0d;
    private double Trabajadas = 0d;
    private boolean Desayuno = false;
    private boolean Comida = false;
    private boolean Cena = false;
    private int Matricula = 0;
    private String Apellidos = "";
    private int Calificacion = 0;
    private int MatriculaSusti = 0;
    private String ApellidosSusti = "";
    private String Bus = "";
    private String Notas = "";

    private String LugarInicio = "";
    private String LugarFinal = "";

    private String TomaDeje = "";
    private double TomaDejeDecimal = 0d;
    private double Euros = 0d;

    private double HorasHuelga = 0d;
    private boolean HuelgaParcial = false;

    private boolean Seleccionado = false;
    //endregion


    //region CONSTRUCTOR

    public DatosDia() {
    }

    DatosDia(Cursor cursor) {

        Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        Dia = cursor.getInt(cursor.getColumnIndexOrThrow("Dia"));
        Mes = cursor.getInt(cursor.getColumnIndexOrThrow("Mes"));
        Año = cursor.getInt(cursor.getColumnIndexOrThrow("Año"));
        DiaSemana = cursor.getInt(cursor.getColumnIndexOrThrow("DiaSemana"));
        EsFranqueo = cursor.getInt(cursor.getColumnIndexOrThrow("EsFranqueo")) != 0;
        EsFestivo = cursor.getInt(cursor.getColumnIndexOrThrow("EsFestivo")) != 0;
        CodigoIncidencia = cursor.getInt(cursor.getColumnIndexOrThrow("CodigoIncidencia"));
        TextoIncidencia = cursor.getString(cursor.getColumnIndexOrThrow("TextoIncidencia"));
        TipoIncidencia = cursor.getInt(cursor.getColumnIndexOrThrow("TipoIncidencia"));
        Servicio = cursor.getString(cursor.getColumnIndexOrThrow("Servicio"));
        Turno = cursor.getInt(cursor.getColumnIndexOrThrow("Turno"));
        Linea = cursor.getString(cursor.getColumnIndexOrThrow("Linea"));
        TextoLinea = cursor.getString(cursor.getColumnIndexOrThrow("TextoLinea"));
        Inicio = cursor.getString(cursor.getColumnIndexOrThrow("Inicio"));
        Final = cursor.getString(cursor.getColumnIndexOrThrow("Final"));
        Acumuladas = cursor.getDouble(cursor.getColumnIndexOrThrow("Acumuladas"));
        Nocturnas = cursor.getDouble(cursor.getColumnIndexOrThrow("Nocturnas"));
        Trabajadas = cursor.getDouble(cursor.getColumnIndexOrThrow("Trabajadas"));
        Desayuno = cursor.getInt(cursor.getColumnIndexOrThrow("Desayuno")) != 0;
        Comida = cursor.getInt(cursor.getColumnIndexOrThrow("Comida")) != 0;
        Cena = cursor.getInt(cursor.getColumnIndexOrThrow("Cena")) != 0;
        Matricula = cursor.getInt(cursor.getColumnIndexOrThrow("Matricula"));
        Apellidos = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos"));
        Calificacion = cursor.getInt(cursor.getColumnIndexOrThrow("Calificacion"));
        MatriculaSusti = cursor.getInt(cursor.getColumnIndexOrThrow("MatriculaSusti"));
        ApellidosSusti = cursor.getString(cursor.getColumnIndexOrThrow("ApellidosSusti"));
        Bus = cursor.getString(cursor.getColumnIndexOrThrow("Bus"));
        Notas = cursor.getString(cursor.getColumnIndexOrThrow("Notas"));
        LugarInicio = cursor.getString(cursor.getColumnIndexOrThrow("LugarInicio"));
        LugarFinal = cursor.getString(cursor.getColumnIndexOrThrow("LugarFinal"));
        TomaDeje = cursor.getString(cursor.getColumnIndexOrThrow("TomaDeje"));
        TomaDejeDecimal = cursor.getDouble(cursor.getColumnIndexOrThrow("TomaDejeDecimal"));
        Euros = cursor.getDouble(cursor.getColumnIndexOrThrow("Euros"));
        HorasHuelga = cursor.getDouble(cursor.getColumnIndexOrThrow("HorasHuelga"));
        HuelgaParcial = cursor.getInt(cursor.getColumnIndexOrThrow("HuelgaParcial")) != 0;
    }


    //endregion


    //region GETTERS

    public int getId() {
        return Id;
    }

    public int getDia() {
        return Dia;
    }

    public int getMes() {
        return Mes;
    }

    public int getAño() {
        return Año;
    }

    public int getDiaSemana() {
        return DiaSemana;
    }

    public boolean isEsFranqueo() {
        return EsFranqueo;
    }

    public boolean isEsFestivo() {
        return EsFestivo;
    }

    public int getCodigoIncidencia() {
        return CodigoIncidencia;
    }

    public String getTextoIncidencia() {
        return TextoIncidencia;
    }

    public int getTipoIncidencia() {
        return TipoIncidencia;
    }

    public String getServicio() {
        return Servicio;
    }

    public int getTurno() {
        return Turno;
    }

    public String getLinea() {
        return Linea;
    }

    public String getTextoLinea() {
        return TextoLinea;
    }

    public String getInicio() {
        return Inicio;
    }

    public String getFinal() {
        return Final;
    }

    public double getAcumuladas() {
        return Acumuladas;
    }

    public double getNocturnas() {
        return Nocturnas;
    }

    public double getTrabajadas() {
        return Trabajadas;
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

    public int getMatricula() {
        return Matricula;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public int getCalificacion() {
        return Calificacion;
    }

    public int getMatriculaSusti() {
        return MatriculaSusti;
    }

    public String getApellidosSusti() {
        return ApellidosSusti;
    }

    public String getBus() {
        return Bus;
    }

    public String getNotas() {
        return Notas;
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

    public double getTomaDejeDecimal() {
        return TomaDejeDecimal;
    }

    public double getEuros() {
        return Euros;
    }

    public double getHorasHuelga() {
        return HorasHuelga;
    }

    public boolean isHuelgaParcial() {
        return HuelgaParcial;
    }

    public boolean isSeleccionado() {
        return Seleccionado;
    }

    //endregion


    //region SETTERS

    public void setId(int id) {
        Id = id;
    }

    public void setDia(int dia) {
        Dia = dia;
    }

    public void setMes(int mes) {
        Mes = mes;
    }

    public void setAño(int año) {
        Año = año;
    }

    public void setDiaSemana(int diaSemana) {
        DiaSemana = diaSemana;
    }

    public void setEsFranqueo(boolean esFranqueo) {
        EsFranqueo = esFranqueo;
    }

    public void setEsFestivo(boolean esFestivo) {
        EsFestivo = esFestivo;
    }

    public void setCodigoIncidencia(int codigoIncidencia) {
        CodigoIncidencia = codigoIncidencia;
    }

    public void setTextoIncidencia(String textoIncidencia) {
        TextoIncidencia = textoIncidencia;
    }

    public void setTipoIncidencia(int tipoIncidencia) {
        TipoIncidencia = tipoIncidencia;
    }

    public void setServicio(String servicio) {
        Servicio = servicio;
    }

    public void setTurno(int turno) {
        Turno = turno;
    }

    public void setLinea(String linea) {
        Linea = linea;
    }

    public void setTextoLinea(String textoLinea) {
        TextoLinea = textoLinea;
    }

    public void setInicio(String inicio) {
        Inicio = inicio;
    }

    public void setFinal(String aFinal) {
        Final = aFinal;
    }

    public void setAcumuladas(double acumuladas) {
        Acumuladas = acumuladas;
    }

    public void setNocturnas(double nocturnas) {
        Nocturnas = nocturnas;
    }

    public void setTrabajadas(double trabajadas) {
        Trabajadas = trabajadas;
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

    public void setMatricula(int matricula) {
        Matricula = matricula;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public void setCalificacion(int calificacion) {
        Calificacion = calificacion;
    }

    public void setMatriculaSusti(int matriculaSusti) {
        MatriculaSusti = matriculaSusti;
    }

    public void setApellidosSusti(String apellidosSusti) {
        ApellidosSusti = apellidosSusti;
    }

    public void setBus(String bus) {
        Bus = bus;
    }

    public void setNotas(String notas) {
        Notas = notas;
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

    public void setTomaDejeDecimal(double tomaDejeDecimal) {
        TomaDejeDecimal = tomaDejeDecimal;
    }

    public void setEuros(double euros) {
        Euros = euros;
    }

    public void setHorasHuelga(double horasHuelga) {
        HorasHuelga = horasHuelga;
    }

    public void setHuelgaParcial(boolean huelgaParcial) {
        HuelgaParcial = huelgaParcial;
    }

    public void setSeleccionado(boolean seleccionado) {
        Seleccionado = seleccionado;
    }

    //endregion


    //region MÉTODOS

    public void copiarDe(DatosDia datosDia) {
        Dia = datosDia.getDia();
        Mes = datosDia.getMes();
        Año = datosDia.getAño();
        DiaSemana = datosDia.getDiaSemana();
        EsFranqueo = datosDia.isEsFranqueo();
        EsFestivo = datosDia.isEsFestivo();
        CodigoIncidencia = datosDia.getCodigoIncidencia();
        TextoIncidencia = datosDia.getTextoIncidencia();
        TipoIncidencia = datosDia.getTipoIncidencia();
        Servicio = datosDia.getServicio();
        Turno = datosDia.getTurno();
        Linea = datosDia.getLinea();
        TextoLinea = datosDia.getTextoLinea();
        Inicio = datosDia.getInicio();
        Final = datosDia.getFinal();
        Acumuladas = datosDia.getAcumuladas();
        Nocturnas = datosDia.getNocturnas();
        Trabajadas = datosDia.getTrabajadas();
        Desayuno = datosDia.isDesayuno();
        Comida = datosDia.isComida();
        Cena = datosDia.isCena();
        Matricula = datosDia.getMatricula();
        Apellidos = datosDia.getApellidos();
        Calificacion = datosDia.getCalificacion();
        MatriculaSusti = datosDia.getMatriculaSusti();
        ApellidosSusti = datosDia.getApellidosSusti();
        Bus = datosDia.getBus();
        Notas = datosDia.getNotas();
        LugarInicio = datosDia.getLugarInicio();
        LugarFinal = datosDia.getLugarFinal();
        TomaDeje = datosDia.getTomaDeje();
        TomaDejeDecimal = datosDia.getTomaDejeDecimal();
        Euros = datosDia.getEuros();
        HorasHuelga = datosDia.getHorasHuelga();
        HuelgaParcial = datosDia.isHuelgaParcial();
    }


    /***
     * Devuelve true si el día tiene la línea, servicio, turno, inicio y final con datos.
     */
    public boolean isServicioCompleto() {
        return !(getLinea().trim().equals("")) &&
                !(getServicio().trim().equals("")) &&
                !(getTurno() == 0) &&
                !(getInicio().trim().equals("")) &&
                !(getFinal().trim().equals(""));
    }


    //endregion

}
