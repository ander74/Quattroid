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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Objetos.Hora;

public class BaseDatos {
	

    //region CONSTANTES

    // INFORMA SI HAY CAMBIOS
    public static boolean hayCambios = false;

    // TIPOS DE ORDEN
    public static final int SIN_ORDEN = 0;
    public static final int RELEVOS_POR_MATRICULA = 1;
    public static final int RELEVOS_POR_NOMRE = 2;
    public static final int RELEVOS_POR_APELLIDOS = 3;


    // NOMBRE DE LA BASE DE DATOS
    private static final String BASE_NAME = "Quattro";

    // NOMBRES DE LAS TABLAS
    private static final String TABLA_CALENDARIO = "Calendario";
    private static final String TABLA_SERVICIOS_DIA = "ServiciosCalendario";
    private static final String TABLA_HORAS_AJENAS = "HorasAjenas";
    private static final String TABLA_INCIDENCIAS = "Incidencias";
    private static final String TABLA_TIPOS_INCIDENCIA = "TiposIncidencia";
    private static final String TABLA_RELEVOS = "Relevos";
    private static final String TABLA_LINEAS = "Lineas";
    private static final String TABLA_SERVICIOS = "Servicios";
    private static final String TABLA_SERVICIOS_AUXILIARES = "ServiciosAuxiliares";

    // COMANDOS DE CREACION DE LAS TABLAS

    private static final String CREAR_TABLA_CALENDARIO = "CREATE TABLE Calendario " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Dia INTEGER DEFAULT 0, " +
            "Mes INTEGER DEFAULT 0, " +
            "Año INTEGER DEFAULT 0, " +
            "DiaSemana INTEGER DEFAULT 0, " +
            "EsFranqueo INTEGER DEFAULT 0, " +
            "EsFestivo INTEGER DEFAULT 0, " +
            "CodigoIncidencia INTEGER DEFAULT 0, " +
            "TextoIncidencia TEXT DEFAULT '', " +
            "TipoIncidencia INTEGER DEFAULT 0, " +
            "HuelgaParcial INTEGER DEFAULT 0, " +
            "HorasHuelga REAL DEFAULT 0, " +
            "Servicio TEXT DEFAULT '', " +
            "Turno INTEGER DEFAULT 0, " +
            "Linea TEXT DEFAULT '', " +
            "TextoLinea TEXT DEFAULT '', " +
            "Inicio TEXT DEFAULT '', " +
            "LugarInicio TEXT DEFAULT '', " +
            "Final TEXT DEFAULT '', " +
            "LugarFinal TEXT DEFAULT '', " +
            "Acumuladas REAL DEFAULT 0, " +
            "Nocturnas REAL DEFAULT 0, " +
            "Trabajadas REAL DEFAULT 0, " +
            "Desayuno INTEGER DEFAULT 0, " +
            "Comida INTEGER DEFAULT 0, " +
            "Cena INTEGER DEFAULT 0, " +
            "TomaDeje TEXT DEFAULT '', " +
            "TomaDejeDecimal REAL DEFAULT 0, " +
            "Euros REAL DEFAULT 0, " +
            "Matricula INTEGER DEFAULT 0, " +
            "Apellidos TEXT DEFAULT '', " +
            "Calificacion INTEGER DEFAULT 0, " +
            "MatriculaSusti INTEGER DEFAULT 0, " +
            "ApellidosSusti TEXT DEFAULT '', " +
            "Bus TEXT DEFAULT '', " +
            "Notas TEXT DEFAULT '')";

    private static final String CREAR_TABLA_SERVICIOS_DIA = "CREATE TABLE ServiciosCalendario " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Dia INTEGER DEFAULT 0, " +
            "Mes INTEGER DEFAULT 0, " +
            "Año INTEGER DEFAULT 0, " +
            "Servicio TEXT DEFAULT '', " +
            "Turno INTEGER DEFAULT 0, " +
            "Linea TEXT DEFAULT '', " +
            "Inicio TEXT DEFAULT '', " +
            "LugarInicio TEXT DEFAULT '', " +
            "Final TEXT DEFAULT '', " +
            "LugarFinal TEXT DEFAULT '')";

    private static final String CREAR_TABLA_HORAS_AJENAS = "CREATE TABLE HorasAjenas " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Dia INTEGER DEFAULT 0, " +
            "Mes INTEGER DEFAULT 0, " +
            "Año INTEGER DEFAULT 0, " +
            "Horas REAL DEFAULT 0, " +
            "Motivo TEXT DEFAULT '')";

    private static final String CREAR_TABLA_INCIDENCIAS = "CREATE TABLE Incidencias " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Codigo INTEGER UNIQUE, " +
            "Incidencia TEXT DEFAULT '', " +
            "Tipo INTEGER DEFAULT 0)";

    private static final String CREAR_TABLA_RELEVOS = "CREATE TABLE Relevos " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Matricula INTEGER UNIQUE, " +
            "Nombre TEXT DEFAULT '', " +
            "Apellidos TEXT DEFAULT '', " +
            "Telefono TEXT DEFAULT '', " +
            "Calificacion INTEGER DEFAULT 0, " +
            "Deuda INTEGER DEFAULT 0, " +
            "Notas TEXT DEFAULT '')";

    private static final String CREAR_TABLA_LINEAS = "CREATE TABLE Lineas " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Linea TEXT DEFAULT '', " +
            "Texto TEXT DEFAULT '')";

    private static final String CREAR_TABLA_SERVICIOS = "CREATE TABLE Servicios " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Linea TEXT DEFAULT '', " +
            "Servicio TEXT DEFAULT '', " +
            "Turno INTEGER DEFAULT 0, " +
            "TomaDeje TEXT DEFAULT '', " +
            "TomaDejeDecimal REAL DEFAULT 0, " +
            "Euros REAL DEFAULT 0, " +
            "Inicio TEXT DEFAULT '', " +
            "LugarInicio TEXT DEFAULT '', " +
            "Final TEXT DEFAULT '', " +
            "LugarFinal TEXT DEFAULT '')";

    private static final String CREAR_TABLA_SERVICIOS_AUXILIARES = "CREATE TABLE ServiciosAuxiliares " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Linea TEXT DEFAULT '', " +
            "Servicio TEXT DEFAULT '', " +
            "Turno INTEGER DEFAULT 0, " +
            "LineaAuxiliar TEXT DEFAULT '', " +
            "ServicioAuxiliar TEXT DEFAULT '', " +
            "TurnoAuxiliar INTEGER DEFAULT 0, " +
            "Inicio TEXT DEFAULT '', " +
            "LugarInicio TEXT DEFAULT '', " +
            "Final TEXT DEFAULT '', " +
            "LugarFinal TEXT DEFAULT '')";

    //endregion


    //region  CADENAS DE ACTUALIZACION DE BASE DE DATOS

    // INSERTAR REGISTROS EN SERVICIOS PARA LA VERSION 2
    private static final String COPIAR_TABLA_CALENDARIO_V2 = "INSERT INTO Calendario " +
            "(Dia, Mes, Año, DiaSemana, EsFranqueo, EsFestivo, CodigoIncidencia, TextoIncidencia, " +
            "TipoIncidencia, Servicio, Turno, Linea, TextoLinea, Inicio, Final, " +
            "Acumuladas, Nocturnas, Trabajadas, Desayuno, Comida, Cena, " +
            "Matricula, Apellidos, Calificacion, MatriculaSusti, ApellidosSusti, " +
            "Bus, Notas) " +
            "SELECT " +
            "Dia, Mes, Año, DiaSemana, EsFranqueo, EsFestivo, CodigoIncidencia, TextoIncidencia, " +
            "TipoIncidencia, Servicio, Turno, Linea, TextoLinea, Inicio, Final, " +
            "Acumuladas, Nocturnas, Trabajadas, Desayuno, Comida, Cena, " +
            "Matricula, Apellidos, Calificacion, MatriculaSusti, ApellidosSusti, " +
            "Bus, Notas " +
            "FROM CalendarioOld;";

    private static final String COPIAR_TABLA_SERVICIOS_DIA_V2 = "INSERT INTO ServiciosCalendario " +
            "(Dia, Mes, Año, Servicio, Turno, Linea, Inicio, Final) " +
            "SELECT " +
            "Dia, Mes, Año, Servicio, Turno, Linea, Inicio, Final " +
            "FROM ServiciosCalendarioOld;";

    private static final String COPIAR_TABLA_SERVICIOS_V2 = "INSERT INTO Servicios " +
            "(Linea, Servicio, Turno, Inicio, Final) " +
            "SELECT " +
            "Linea, Servicio, Turno, Inicio, Final " +
            "FROM ServiciosOld;";

    private static final String COPIAR_TABLA_SERVICIOS_AUXILIARES_V2 = "INSERT INTO ServiciosAuxiliares " +
            "(Linea, Servicio, Turno, LineaAuxiliar, ServicioAuxiliar, TurnoAuxiliar, Inicio, Final) " +
            "SELECT " +
            "Linea, Servicio, Turno, LineaAuxiliar, ServicioAuxiliar, TurnoAuxiliar, Inicio, Final " +
            "FROM ServiciosAuxiliaresOld;";


    // INSERTAR REGISTROS EN SERVICIOS PARA LA VERSION 3
   private static final String COPIAR_TABLA_CALENDARIO_V3 = "INSERT INTO Calendario " +
            "(Dia, Mes, Año, DiaSemana, EsFranqueo, EsFestivo, CodigoIncidencia, TextoIncidencia, " +
            "TipoIncidencia, Servicio, Turno, Linea, TextoLinea, Inicio, LugarInicio, Final, LugarFinal, " +
            "Acumuladas, Nocturnas, Trabajadas, Desayuno, Comida, Cena, " +
            "Matricula, Apellidos, Calificacion, MatriculaSusti, ApellidosSusti, " +
            "Bus, Notas) " +
            "SELECT " +
            "Dia, Mes, Año, DiaSemana, EsFranqueo, EsFestivo, CodigoIncidencia, TextoIncidencia, " +
            "TipoIncidencia, Servicio, Turno, Linea, TextoLinea, Inicio, LugarInicio, Final, LugarFinal, " +
            "Acumuladas, Nocturnas, Trabajadas, Desayuno, Comida, Cena, " +
            "Matricula, Apellidos, Calificacion, MatriculaSusti, ApellidosSusti, " +
            "Bus, Notas " +
            "FROM CalendarioOld;";

    private static final String COPIAR_TABLA_SERVICIOS_V3 = "INSERT INTO Servicios " +
            "(Linea, Servicio, Turno, Inicio, LugarInicio, Final, LugarFinal) " +
            "SELECT " +
            "Linea, Servicio, Turno, Inicio, LugarInicio, Final, LugarFinal " +
            "FROM ServiciosOld;";

    // INSERTAR REGISTROS EN SERVICIOS PARA LA VERSION 4
    private static final String COPIAR_TABLA_CALENDARIO_V4 = "INSERT INTO Calendario " +
            "(Dia, Mes, Año, DiaSemana, EsFranqueo, EsFestivo, CodigoIncidencia, TextoIncidencia, " +
            "TipoIncidencia, Servicio, Turno, Linea, TextoLinea, Inicio, " +
            "LugarInicio, Final, LugarFinal, " +
            "Acumuladas, Nocturnas, Trabajadas, Desayuno, Comida, Cena, " +
            "Tomadeje, TomaDejeDecimal, Euros, " +
            "Matricula, Apellidos, Calificacion, MatriculaSusti, ApellidosSusti, " +
            "Bus, Notas) " +
            "SELECT " +
            "Dia, Mes, Año, DiaSemana, EsFranqueo, EsFestivo, CodigoIncidencia, TextoIncidencia, " +
            "TipoIncidencia, Servicio, Turno, Linea, TextoLinea, Inicio, " +
            "LugarInicio, Final, LugarFinal, " +
            "Acumuladas, Nocturnas, Trabajadas, Desayuno, Comida, Cena, " +
            "Tomadeje, TomaDejeDecimal, Euros, " +
            "Matricula, Apellidos, Calificacion, MatriculaSusti, ApellidosSusti, " +
            "Bus, Notas " +
            "FROM CalendarioOld;";

    //endregion


    //region  VARIABLES, CONSTRUCTOR Y METODOS SECUNDARIOS

    // VARIABLES DE LA CLASE
    private BaseHelper baseHelper = null;
    private static SQLiteDatabase baseDatos = null;


    // CONSTRUCTOR DE LA CLASE
    public BaseDatos(Context context){
        baseHelper = new BaseHelper(context);
        baseDatos = baseHelper.getWritableDatabase();
    }

    // CERRAR BASE DATOS
    public void close(){
        baseHelper.close();
    }

    //endregion


    //region METODOS DEL CALENDARIO

    /**
     *
     * @param mes Mes a mostrar.
     * @param año Año a mostrar.
     * @return Cursor con los días del mes indicado.
     */
    public Cursor cursorMes(int mes, int año){
        String[] args = {String.valueOf(mes), String.valueOf(año)};
        String where = "Mes=? AND Año=?";
        String orderby = "Dia ASC";
        return baseDatos.query(TABLA_CALENDARIO, null, where, args, null, null, orderby);
    }

    public ArrayList<DatosDia> datosMes(int mes, int año){
        String[] args = {String.valueOf(mes), String.valueOf(año)};
        String where = "Mes=? AND Año=?";
        String orderby = "Dia ASC";
        Cursor cursor = baseDatos.query(TABLA_CALENDARIO, null, where, args, null, null, orderby);
        ArrayList<DatosDia> lista = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                DatosDia dia = new DatosDia(cursor);
                lista.add(dia);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    /**
     *
     * @param dia Dia a devolver.
     * @param mes Mes del día a devolver.
     * @param año Año del día a devolver.
     * @return Devuelve un objeto ServicioDia con los datos del día indicado.
     */
    public DatosDia servicioDia(int dia, int mes, int año){
        String[] args = {String.valueOf(dia), String.valueOf(mes), String.valueOf(año)};
        String where = "Dia=? AND Mes=? AND Año=?";
        String orderby = "Dia ASC";
        Cursor c = baseDatos.query(TABLA_CALENDARIO, null, where, args, null, null, orderby);

        DatosDia datosDia = new DatosDia();
        if (c.getCount() == 0) return datosDia;
        c.moveToFirst();
        datosDia.setDia(c.getInt(c.getColumnIndex("Dia")));
        datosDia.setMes(c.getInt(c.getColumnIndex("Mes")));
        datosDia.setAño(c.getInt(c.getColumnIndex("Año")));
        datosDia.setDiaSemana(c.getInt(c.getColumnIndex("DiaSemana")));
        datosDia.setEsFranqueo(c.getInt(c.getColumnIndex("EsFranqueo")) != 0);
        datosDia.setEsFestivo(c.getInt(c.getColumnIndex("EsFestivo")) != 0);
        datosDia.setCodigoIncidencia(c.getInt(c.getColumnIndex("CodigoIncidencia")));
        datosDia.setTextoIncidencia(c.getString(c.getColumnIndex("TextoIncidencia")));
        datosDia.setTipoIncidencia(c.getInt(c.getColumnIndex("TipoIncidencia")));
        datosDia.setHuelgaParcial(c.getInt(c.getColumnIndex("HuelgaParcial")) != 0);
        datosDia.setHorasHuelga(c.getDouble(c.getColumnIndex("HorasHuelga")));
        datosDia.setServicio(c.getString(c.getColumnIndex("Servicio")));
        datosDia.setTurno(c.getInt(c.getColumnIndex("Turno")));
        datosDia.setLinea(c.getString(c.getColumnIndex("Linea")));
        datosDia.setTextoLinea(c.getString(c.getColumnIndex("TextoLinea")));
        datosDia.setInicio(c.getString(c.getColumnIndex("Inicio")));
        datosDia.setLugarInicio(c.getString(c.getColumnIndex("LugarInicio")));
        datosDia.setFinal(c.getString(c.getColumnIndex("Final")));
        datosDia.setLugarFinal(c.getString(c.getColumnIndex("LugarFinal")));
        datosDia.setTrabajadas(c.getDouble(c.getColumnIndex("Trabajadas")));
        datosDia.setAcumuladas(c.getDouble(c.getColumnIndex("Acumuladas")));
        datosDia.setNocturnas(c.getDouble(c.getColumnIndex("Nocturnas")));
        datosDia.setDesayuno(c.getInt(c.getColumnIndex("Desayuno")) != 0);
        datosDia.setComida(c.getInt(c.getColumnIndex("Comida")) != 0);
        datosDia.setCena(c.getInt(c.getColumnIndex("Cena")) != 0);
        datosDia.setTomaDeje(c.getString(c.getColumnIndex("TomaDeje")));
        datosDia.setTomaDejeDecimal(c.getDouble(c.getColumnIndex("TomaDejeDecimal")));
        datosDia.setEuros(c.getDouble(c.getColumnIndex("Euros")));
        datosDia.setMatricula(c.getInt(c.getColumnIndex("Matricula")));
        datosDia.setApellidos(c.getString(c.getColumnIndex("Apellidos")));
        datosDia.setCalificacion(c.getInt(c.getColumnIndex("Calificacion")));
        datosDia.setMatriculaSusti(c.getInt(c.getColumnIndex("MatriculaSusti")));
        datosDia.setApellidosSusti(c.getString(c.getColumnIndex("ApellidosSusti")));
        datosDia.setBus(c.getString(c.getColumnIndex("Bus")));
        datosDia.setNotas(c.getString(c.getColumnIndex("Notas")));

        c.close();
        return datosDia;
    }

    /**
     *
     * @param datosDia Objeto ServicioDia con los datos del día a guardar.
     * @return true si se ha guardado correctamente.
     */
    public boolean guardaDia(DatosDia datosDia) {
        hayCambios = true;
        // Llenamos los valores que se guardarán.
        ContentValues valores = new ContentValues();
        valores.put("Dia", datosDia.getDia());
        valores.put("Mes", datosDia.getMes());
        valores.put("Año", datosDia.getAño());
        valores.put("DiaSemana", datosDia.getDiaSemana());
        valores.put("EsFranqueo", (datosDia.isEsFranqueo()) ? 1 : 0);
        valores.put("EsFestivo", (datosDia.isEsFestivo()) ? 1 : 0);
        valores.put("CodigoIncidencia", datosDia.getCodigoIncidencia());
        valores.put("TextoIncidencia", datosDia.getTextoIncidencia());
        valores.put("TipoIncidencia", datosDia.getTipoIncidencia());
        valores.put("HuelgaParcial", (datosDia.isHuelgaParcial()) ? 1 : 0);
        valores.put("HorasHuelga", datosDia.getHorasHuelga());
        valores.put("Servicio", datosDia.getServicio());
        valores.put("Turno", datosDia.getTurno());
        valores.put("Linea", datosDia.getLinea());
        valores.put("TextoLinea", datosDia.getTextoLinea());
        valores.put("Inicio", datosDia.getInicio());
        valores.put("LugarInicio", datosDia.getLugarInicio());
        valores.put("Final", datosDia.getFinal());
        valores.put("LugarFinal", datosDia.getLugarFinal());
        valores.put("Acumuladas", datosDia.getAcumuladas());
        valores.put("Nocturnas", datosDia.getNocturnas());
        valores.put("Trabajadas", datosDia.getTrabajadas());
        valores.put("Desayuno", (datosDia.isDesayuno()) ? 1 : 0);
        valores.put("Comida", (datosDia.isComida()) ? 1 : 0);
        valores.put("Cena", (datosDia.isCena()) ? 1 : 0);
        valores.put("TomaDeje", datosDia.getTomaDeje());
        valores.put("TomaDejeDecimal", datosDia.getTomaDejeDecimal());
        valores.put("Euros", datosDia.getEuros());
        valores.put("Matricula", datosDia.getMatricula());
        valores.put("Apellidos", datosDia.getApellidos());
        valores.put("Calificacion", datosDia.getCalificacion());
        valores.put("MatriculaSusti", datosDia.getMatriculaSusti());
        valores.put("ApellidosSusti", datosDia.getApellidosSusti());
        valores.put("Bus", datosDia.getBus());
        valores.put("Notas", datosDia.getNotas());

        // Creamos la cadena where
        String where = "Dia=? AND Mes=? AND Año=?";
        // Creamos los parametros del where
        String[] params = new String[]{String.valueOf(datosDia.getDia()),
                String.valueOf(datosDia.getMes()),
                String.valueOf(datosDia.getAño())};
        // Actualizamos el registro.
        return baseDatos.update(TABLA_CALENDARIO, valores, where, params) > 0;
    }

    /**
     *
     * @param mes Mes a crear.
     * @param año Año del mes a crear.
     */
    public void crearMes(int mes, int año){
        hayCambios = true;
        Calendar fecha = Calendar.getInstance();
        fecha.set(año, mes-1, 1); // Enero = 0; Febrero = 2...
        int DSemana;
        int diasMes = fecha.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int m=1; m <= diasMes; m++){
            fecha.set(año, mes-1, m);
            DSemana = fecha.get(Calendar.DAY_OF_WEEK);
            ContentValues valores = new ContentValues();
            valores.put("Dia", m);
            valores.put("Mes", mes);
            valores.put("Año", año);
            valores.put("DiaSemana", DSemana);
            if (baseDatos.insert(TABLA_CALENDARIO, null, valores) == -1) return;
        }
    }

    public void actualizarCalificacion(int matricula, int calificacion){
        hayCambios = true;
        if (matricula < 1) return;
        if (calificacion < 0 || calificacion > 2) calificacion = 0;
        ContentValues valores = new ContentValues();
        valores.put("Calificacion", calificacion);
        String where = "Matricula=" + String.valueOf(matricula);
	    baseDatos.update(TABLA_CALENDARIO, valores, where, null);
    }

    public void actualizarRelevos(int matricula, String apellidos){
        hayCambios = true;
        if (matricula < 1) return;
        ContentValues valores = new ContentValues();
        valores.put("Apellidos", apellidos);
        String where = "Matricula=" + String.valueOf(matricula);
	    baseDatos.update(TABLA_CALENDARIO, valores, where, null);
    }

    public Cursor cursorBusqueda(String where){
        String orderby = "Año ASC, Mes ASC, Dia ASC";
        return baseDatos.query(TABLA_CALENDARIO, null, where, null, null, null, orderby);
    }

    //endregion


    //region  METODOS DE INCIDENCIAS

    /**
     *
     * @return Cursor con todas las incidencias ordenadas por código.
     */
    public Cursor cursorIncidencias (){
        String orderby = "Codigo ASC";
        return baseDatos.query(TABLA_INCIDENCIAS, null, null, null, null, null, orderby);
    }

    /**
     *
     * @param codigo Codigo de la incidencia a devolver.
     * @return Incidencia que representa el código
     */
    public Incidencia getIncidencia(int codigo){
        String where = "Codigo=" + String.valueOf(codigo);
        Cursor c = baseDatos.query(TABLA_INCIDENCIAS, null, where, null, null, null, null);
        Incidencia i = new Incidencia();
        if (c.getCount() == 0) return i;
        c.moveToFirst();
        i.setCodigo(c.getInt(c.getColumnIndex("Codigo")));
        i.setTexto(c.getString(c.getColumnIndex("Incidencia")));
        i.setTipo(c.getInt(c.getColumnIndex("Tipo")));

        c.close();
        return i;
    }

    /**
     *
     * @return Cursor con todas las incidencias insertadas por el usuario.
     */
    public Cursor cursorIncidenciasEditables(){
        String where = "Codigo > 16";
        String orderby = "Codigo ASC";
        return baseDatos.query(TABLA_INCIDENCIAS, null, where, null, null, null, orderby);
    }

    /**
     *
     * @return Devuelve el código de la siguiente nueva incidencia disponible.
     */
    public int codigoNuevaIncidencia(){
        Cursor c = cursorIncidencias();
        c.moveToLast();
        int ultima = c.getInt(c.getColumnIndex("Codigo"));
        
        return ultima + 1;
    }

    /**
     *
     * @param i Incidencia que se va a guardar o actualizar.
     */
    public void setIncidencia(Incidencia i) {
        hayCambios = true;
        ContentValues valores = new ContentValues();
        valores.put("Codigo", i.getCodigo());
        valores.put("Incidencia", i.getTexto());
        valores.put("Tipo", i.getTipo());
        baseDatos.insertWithOnConflict(TABLA_INCIDENCIAS, null, valores, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     *
     * @param codigo Codigo de la incidencia que va a ser borrada.
     */
    public void borraIncidencia(int codigo){
        hayCambios = true;
        String where = "Codigo=" + String.valueOf(codigo);
        baseDatos.delete(TABLA_INCIDENCIAS, where, null);
    }

    public void modificarIncidencias(int codigo, String texto){
        if (codigo < 1 || codigo > 16) return;
        hayCambios = true;
        ContentValues valores = new ContentValues();
        valores.put("TextoIncidencia", texto);
        String where = "CodigoIncidencia=" + String.valueOf(codigo);
        baseDatos.update(TABLA_CALENDARIO, valores, where, null);
    }

    public void actualizarTiposIncidencias(){
        Incidencia i = getIncidencia(8);
        if (i.getTipo() != 6) {
            i.setTipo(6);
            setIncidencia(i);
        }
        i = getIncidencia(15);
        if (i.getTipo() != 1) {
            i.setTipo(1);
            setIncidencia(i);
        }
        i = getIncidencia(6);
        if (i.getTexto().equals("Enfermo")){
            i.setTexto("Enfermo/a");
            setIncidencia(i);
        }
        i = getIncidencia(7);
        if (i.getTexto().equals("Accidentado")){
            i.setTexto("Accidentado/a");
            setIncidencia(i);
        }
    }

    //endregion


    //region METODOS ESTADISTICAS

    /**
     *
     * @param mes Ultimo mes que se sumará a las acumuladas.
     * @param año Año del último mes que se sumará a las acumuladas.
     * @return Devuelve las horas acumuladas hasta el final del mes indicado.
     */
    public double acumuladasHastaMes(int mes, int año){
        double acum = 0d;
        String consulta = "SELECT SUM(Acumuladas)" +
                " AS sumaAcumuladas FROM Calendario WHERE Año<" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) acum += c.getDouble(c.getColumnIndex("sumaAcumuladas"));
        consulta = "SELECT SUM(Acumuladas) AS sumaAcumuladas FROM Calendario WHERE Mes<=" +
                String.valueOf(mes) + " AND Año=" + String.valueOf(año);
        c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) acum += c.getDouble(c.getColumnIndex("sumaAcumuladas"));
        c.close();
        return acum;
    }

    /**
     *
     * @param mes Mes del que se quieren tener las acumuladas.
     * @param año Año del mes del que se quieren tener las acumuladas.
     * @return Devuelve las horas acumuladas del mes indicado.
     */
    public double acumuladasMes(int mes, int año){
        double acum = 0d;
        String consulta = "SELECT SUM(Acumuladas)" +
                " AS sumaAcumuladas FROM Calendario WHERE Mes=" + String.valueOf(mes) +
                " AND Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) acum += c.getDouble(c.getColumnIndex("sumaAcumuladas"));
        c.close();
        return acum;
    }

    public double acumuladasAño(int año){
        double acum = 0d;
        String consulta = "SELECT SUM(Acumuladas)" +
                " AS sumaAcumuladas FROM Calendario WHERE Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) acum += c.getDouble(c.getColumnIndex("sumaAcumuladas"));
        c.close();
        return acum;
    }

    public double acumuladasFecha(String where){
        double acum = 0d;
        String consulta = "SELECT SUM(Acumuladas) AS Suma FROM Calendario WHERE ";
        consulta = consulta + where;
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) acum += c.getDouble(c.getColumnIndex("Suma"));
        c.close();
        return acum;
    }

    /**
     *
     * @param mes Mes del que se quieren tener las nocturnas.
     * @param año Año del mes del que se quieren tener las nocturnas.
     * @return Devuelve las horas nocturnas del mes indicado.
     */
    public double nocturnasMes(int mes, int año){
        double noct = 0d;
        String consulta = "SELECT SUM(Nocturnas)" +
                " AS sumaNocturnas FROM Calendario WHERE Mes=" + String.valueOf(mes) +
                " AND Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) noct += c.getDouble(c.getColumnIndex("sumaNocturnas"));
        c.close();
        return noct;
    }

    /**
     *
     * @param mes Mes del que se quieren tener las horas trabajadas.
     * @param año Año del mes del que se quieren tener las horas trabajadas.
     * @return Devuelve las horas trabajadas del mes indicado.
     */
    public double trabajadasMes(int mes, int año){
        double trab = 0d;
        String consulta = "SELECT SUM(Trabajadas)" +
                " AS sumaTrabajadas FROM Calendario WHERE Mes=" + String.valueOf(mes) +
                " AND Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) trab += c.getDouble(c.getColumnIndex("sumaTrabajadas"));
        c.close();
        return trab;
    }

    public double trabajadasAño(int año){
        double trab = 0d;
        String consulta = "SELECT SUM(Trabajadas)" +
                " AS sumaTrabajadas FROM Calendario WHERE Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) trab += c.getDouble(c.getColumnIndex("sumaTrabajadas"));
        c.close();
        return trab;
    }

    public double trabajadasFecha(String where){
        double trab = 0d;
        String consulta = "SELECT SUM(Trabajadas) AS Suma FROM Calendario WHERE ";
        consulta = consulta + where;
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) trab += c.getDouble(c.getColumnIndex("Suma"));
        c.close();
        return trab;
    }

    /**
     *
     * NUEVO VERSIÓN 1.5
     *
     * DIAS TRABAJADOS A EFECTOS DE CONVENIO.
     *
     * Días con tipo incidencia 1-Trabajo, 3-FOD, 6-Jornada Media.
     *
     */
    public int diasTrabajadosConvenio(int mes, int año){
        int trab = 0;
        String consulta = "SELECT * FROM Calendario WHERE Mes=" + mes + " AND año=" + año +
        " AND (TipoIncidencia=1 OR TipoIncidencia=3 OR TipoIncidencia=6)";
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) trab = c.getCount();
        c.close();
        return trab;
    }

    public int diasTrabajadosConvenio(int año){
        int trab = 0;
        String consulta = "SELECT * FROM Calendario WHERE Año=" + año +
                " AND (TipoIncidencia=1 OR TipoIncidencia=3 OR TipoIncidencia=6)";
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) trab = c.getCount();
        c.close();
        return trab;
    }

    public int diasEnfermoComputables(int año){
        int diasEnfermo = 0;
        String consulta = "SELECT * FROM Calendario WHERE Año=" + año +
                " AND (CodigoIncidencia=6 OR CodigoIncidencia=7) AND EsFranqueo=0";
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) diasEnfermo = c.getCount();
        c.close();
        return diasEnfermo;
    }

    // DIETAS DEPRECATED
    public int DesayunosMes(int mes, int año){
        int trab = 0;
        String consulta = "SELECT SUM(Desayuno)" +
                " AS sumaDesayuno FROM Calendario WHERE Mes=" + String.valueOf(mes) +
                " AND Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) trab += c.getInt(c.getColumnIndex("sumaDesayuno"));
        c.close();
        return trab;
    }

    public int ComidasMes(int mes, int año){
        int trab = 0;
        String consulta = "SELECT SUM(Comida)" +
                " AS sumaComida FROM Calendario WHERE Mes=" + String.valueOf(mes) +
                " AND Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) trab += c.getInt(c.getColumnIndex("sumaComida"));
        c.close();
        return trab;
    }

    public int CenasMes(int mes, int año){
        int trab = 0;
        String consulta = "SELECT SUM(Cena)" +
                " AS sumaCena FROM Calendario WHERE Mes=" + String.valueOf(mes) +
                " AND Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) trab += c.getInt(c.getColumnIndex("sumaCena"));
        c.close();
        return trab;
    }

    // TOMA Y DEJE Y EUROS
    public double tomaDejeHastaMes(int mes, int año){
        double tomadeje = 0d;
        String consulta = "SELECT SUM(TomaDejeDecimal)" +
                " AS sumaTomaDeje FROM Calendario WHERE Año<" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) tomadeje += c.getDouble(c.getColumnIndex("sumaTomaDeje"));
        consulta = "SELECT SUM(TomaDejeDecimal) AS sumaTomaDeje FROM Calendario WHERE Mes<=" +
                String.valueOf(mes) + " AND Año=" + String.valueOf(año);
        c.close();
        c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) tomadeje += c.getDouble(c.getColumnIndex("sumaTomaDeje"));
        c.close();
        return tomadeje;
    }

    public double tomaDejeMes(int mes, int año){
        double tomadeje = 0d;
        String consulta = "SELECT SUM(TomaDejeDecimal)" +
                " AS sumaTomaDeje FROM Calendario WHERE Mes=" + String.valueOf(mes) +
                " AND Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) tomadeje += c.getDouble(c.getColumnIndex("sumaTomaDeje"));
        c.close();
        return tomadeje;
    }

    public double eurosMes(int mes, int año){
        double euros = 0d;
        String consulta = "SELECT SUM(Euros)" +
                " AS sumaEuros FROM Calendario WHERE Mes=" + String.valueOf(mes) +
                " AND Año=" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) euros += c.getDouble(c.getColumnIndex("sumaEuros"));
        c.close();
        return euros;
    }
	
    public ArrayList<Estadistica> Estadisticas(String whereFecha, double JMedia){
        // ArrayList que se va a devolver.
        ArrayList<Estadistica> lista = new ArrayList<>();
        //Objeto que se va a almacenar.
        Estadistica e = null;
        // Cursor que se va a usar para recuperar las estadisticas.
        Cursor c = null;
        // Variables generales
        whereFecha = "WHERE " + whereFecha;
        String select, whereFiltro, consulta;
        int contador = 0;

        //region HORAS
        e = new Estadistica();
        e.Texto = "Horas";
        e.Tipo = 1;
        contador = 1;
        lista.add(e);
        /* Trabajadas TOTALES*/
        select = "SELECT SUM(Trabajadas) AS Suma FROM Calendario ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Horas Trabajadas Totales";
        e.Valor = "0,00";
        e.Contador = contador;
        contador++;
        if (c.moveToFirst()) {
            e.Valor = Hora.textoDecimal(c.getDouble(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();

        /*
         *  INICIO TRABAJADAS CONVENIO (VERSIÓN 1.5)
         */

        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND (TipoIncidencia=1 OR TipoIncidencia=3 OR TipoIncidencia=6)";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        double trabajadas = 0;
        if (c.moveToFirst()) {
            trabajadas = c.getCount() * JMedia;
        }
        c.close();
        whereFiltro = " AND ((CodigoIncidencia=6 OR CodigoIncidencia=7) AND EsFranqueo=0)";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) {
            trabajadas += c.getCount() * JMedia;
        }
        c.close();
        e = new Estadistica();
        e.Texto = "Horas Trabajadas Convenio";
        e.Valor = "0,00";
        e.Contador = contador;
        contador++;
        e.Valor = Hora.textoDecimal(trabajadas);
        lista.add(e);
        c.close();


        /*
         * FINAL TRABAJADAS CONVENIO (VERSIÓN 1.5)
         */

        /* Acumuladas */
        select = "SELECT SUM(Acumuladas) AS Suma FROM Calendario ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Horas Acumuladas";
        e.Valor = "0,00";
        e.Contador = contador;
        contador++;
        if (c.moveToFirst()) {
            e.Valor = Hora.textoDecimal(c.getDouble(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();
        /* Toma y Deje */
        select = "SELECT SUM(TomaDejeDecimal) AS Suma FROM Calendario ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Horas de Toma y Deje";
        e.Valor = "0,00";
        e.Contador = contador;
        contador++;
        if (c.moveToFirst()) {
            e.Valor = Hora.textoDecimal(c.getDouble(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();
        /* Nocturnas */
        select = "SELECT SUM(Nocturnas) AS Suma FROM Calendario ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Horas Nocturnas";
        e.Valor = "0,00";
        e.Contador = contador;
        contador++;
        if (c.moveToFirst()) {
            e.Valor = Hora.textoDecimal(c.getDouble(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();
        /* Ajenas */
        select = "SELECT SUM(Horas) AS Suma FROM HorasAjenas ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Horas Ajenas";
        e.Valor = "0,00";
        e.Contador = contador;
        //contador++;
        if (c.moveToFirst()) {
            e.Valor = Hora.textoDecimal(c.getDouble(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();

        //endregion

        //region DIETAS
        e = new Estadistica();
        e.Texto = "Dietas";
        e.Tipo = 1;
        contador = 1;
        lista.add(e);

        /* Desayuno */
        select = "SELECT SUM(Desayuno) AS Suma FROM Calendario ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Dietas de Desayuno";
        e.Valor = "0";
        e.Contador = contador;
        contador++;
        if (c.moveToFirst()) {
            e.Valor = String.valueOf(c.getInt(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();
        /* Comida */
        select = "SELECT SUM(Comida) AS Suma FROM Calendario ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Dietas de Comida";
        e.Valor = "0";
        e.Contador = contador;
        contador++;
        if (c.moveToFirst()) {
            e.Valor = String.valueOf(c.getInt(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();
        /* Cena */
        select = "SELECT SUM(Cena) AS Suma FROM Calendario ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Dietas de Cena";
        e.Valor = "0";
        e.Contador = contador;
        //contador++;
        if (c.moveToFirst()) {
            e.Valor = String.valueOf(c.getInt(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();

        //endregion

        //region SABADOS
        e = new Estadistica();
        e.Texto = "Sábados";
        e.Tipo = 1;
        contador = 1;
        lista.add(e);

        /* Sabados Trabajados */
        int plusSabado = 0;
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND (TipoIncidencia BETWEEN 1 AND 2) AND DiaSemana=7";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Sábados Trabajados";
        e.Valor = String.valueOf(c.getCount());
        plusSabado += c.getCount();
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Sabados con permiso*/
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND (CodigoIncidencia=8) AND DiaSemana=7";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Permisos en Sábado";
        e.Valor = String.valueOf(c.getCount());
        plusSabado += c.getCount();
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Total Plus Sábados */
        e = new Estadistica();
        e.Texto = "Total Plus Sábado";
        e.Valor = String.valueOf(plusSabado);
        e.Contador = contador;
        //contador++;
        lista.add(e);

        //endregion

        //region DOMINGOS Y FESTIVOS
        e = new Estadistica();
        e.Texto = "Domingos y Festivos";
        e.Tipo = 1;
        contador = 1;
        lista.add(e);

        /* Domingos Trabajados */
        int plusFestivo = 0;
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND (TipoIncidencia BETWEEN 1 AND 2) AND DiaSemana=1";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Domingos Trabajados";
        e.Valor = String.valueOf(c.getCount());
        plusFestivo += c.getCount();
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Domingo con permiso*/
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND (CodigoIncidencia=8) AND DiaSemana=1";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Permisos en Domingo";
        e.Valor = String.valueOf(c.getCount());
        plusFestivo += c.getCount();
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Total Festivos */
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND EsFestivo=1";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Total Festivos";
        e.Valor = String.valueOf(c.getCount());
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Festivos Trabajados */
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND (TipoIncidencia BETWEEN 1 AND 2) AND EsFestivo=1";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Festivos Trabajados";
        e.Valor = String.valueOf(c.getCount());
        plusFestivo += c.getCount();
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Total Plus Festivo */
        e = new Estadistica();
        e.Texto = "Total Plus Festivo";
        e.Valor = String.valueOf(plusFestivo);
        e.Contador = contador;
        //contador++;
        lista.add(e);

        //endregion

        //region FRANQUEOS
        String texto = getIncidencia(2).getTexto();
        e = new Estadistica();
        e.Texto = texto + "s";
        e.Tipo = 1;
        contador = 1;
        lista.add(e);

        /* Franqueos en Calendario */
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND EsFranqueo=1";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = texto + "s en Calendario";
        e.Valor = String.valueOf(c.getCount());
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Dias de Franqueo */
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND CodigoIncidencia=2";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Días de " + texto;
        e.Valor = String.valueOf(c.getCount());
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Hacemos día en franqueo */
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND EsFranqueo=1 AND CodigoIncidencia=12";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Hacemos día en " + texto;
        e.Valor = String.valueOf(c.getCount());
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Franqueos Trabajados */
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND (TipoIncidencia BETWEEN 1 AND 2) AND EsFranqueo=1";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = texto + "s Trabajados";
        e.Valor = String.valueOf(c.getCount());
        e.Contador = contador;
        //contador++;
        lista.add(e);
        c.close();


        //endregion

        //region HUELGAS
        e = new Estadistica();
        e.Texto = "Huelgas";
        e.Tipo = 1;
        contador = 1;
        lista.add(e);
        /* Días de huelga completa */
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND CodigoIncidencia=15 AND HuelgaParcial=0 ";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Días de huelga";
        e.Valor = String.valueOf(c.getCount());
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Días de huelga parcial */
        select = "SELECT * FROM Calendario ";
        whereFiltro = " AND CodigoIncidencia=15 AND HuelgaParcial=1 ";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Días de huelga parcial";
        e.Valor = String.valueOf(c.getCount());
        e.Contador = contador;
        contador++;
        lista.add(e);
        c.close();
        /* Total horas en huelga parcial */
        select = "SELECT SUM(HorasHuelga) AS Suma FROM Calendario ";
        whereFiltro = " AND CodigoIncidencia=15 AND HuelgaParcial=1";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Total Horas en huelga parcial";
        e.Valor = "0,00";
        e.Contador = contador;
        //contador++;
        if (c.moveToFirst()) {
            e.Valor = Hora.textoDecimal(c.getDouble(c.getColumnIndex("Suma")));
        }
        lista.add(e);
        c.close();

        //endregion

        //region  RESUMEN POR INCIDENCIA
        e = new Estadistica();
        e.Texto = "Resumen por Incidencia";
        e.Tipo = 1;
        contador = 1;
        lista.add(e);

        /* Resumen */
        int cont = 0;
        int sinIncidencia = 0;
        Cursor i = cursorIncidencias();

        while (i.moveToNext()){
            select = "SELECT * FROM Calendario ";
            whereFiltro = " AND CodigoIncidencia=" + String.valueOf(i.getInt(i.getColumnIndex("Codigo")));
            consulta = select + whereFecha + whereFiltro;
            c = baseDatos.rawQuery(consulta, null);
            if (c.getCount() > 0){
                String textoIncidencia = i.getString(i.getColumnIndex("Incidencia"));
                if (textoIncidencia.equals("Repetir Día Anterior")){
                    sinIncidencia = c.getCount();
                } else {
                    e = new Estadistica();
                    e.Texto = i.getString(i.getColumnIndex("Incidencia"));
                    e.Valor = String.valueOf(c.getCount());
                    e.Contador = contador;
                    contador++;
                    lista.add(e);
                }
                cont += c.getCount();
            }
            c.close();
        }
        e = new Estadistica();
        e.Texto = "Total días con incidencia";
        e.Valor = String.valueOf(cont-sinIncidencia);
        e.Contador = contador;
        lista.add(e);
        contador++;
        e = new Estadistica();
        e.Texto = "Total días sin incidencia";
        e.Valor = String.valueOf(sinIncidencia);
        e.Contador = contador;
        lista.add(e);
        contador++;
        e = new Estadistica();
        e.Texto = "Total días";
        e.Valor = String.valueOf(cont);
        e.Contador = contador;
        lista.add(e);
        i.close();

        //endregion

        //region RESUMEN

        e = new Estadistica();
        e.Texto = "Resumen";
        e.Tipo = 1;
        contador = 1;
        lista.add(e);

        /* Euros por Servicio */
        select = "SELECT SUM(Euros) AS Suma FROM Calendario ";
        whereFiltro = "";
        consulta = select + whereFecha + whereFiltro;
        c = baseDatos.rawQuery(consulta, null);
        e = new Estadistica();
        e.Texto = "Euros por Servicio";
        e.Valor = "0,00 €";
        e.Contador = contador;
        //contador++;
        if (c.moveToFirst()) {
            e.Valor = Hora.textoDecimal(c.getDouble(c.getColumnIndex("Suma"))) + " €";
        }
        lista.add(e);


        //endregion

        c.close();
        return lista;
    }

    //endregion


    //region  METODOS SERVICIOS DIA

    /**
     *
     * @param dia Dia del que se quieren extraer los servicios.
     * @param mes Mes del día del que se quieren extraer los servicios.
     * @param año Año del día del que se quieren extraer los servicios.
     * @return Devuelve un cursor con los servicios del día indicado.
     */
    public Cursor cursorServiciosDia(int dia, int mes, int año){
        String[] args = {String.valueOf(dia), String.valueOf(mes), String.valueOf(año)};
        String where = "Dia=? AND Mes=? AND Año=?";
        String orderby = "_id ASC";
        return baseDatos.query(TABLA_SERVICIOS_DIA, null, where, args, null, null, orderby);
    }

    /**
     *
     * @param dia Dia del que se quieren vaciar los servicios.
     * @param mes Mes del día del que se quieren vaciar los servicios.
     * @param año Año del día del que se quieren vaciar los servicios.
     */
    public void vaciarServiciosDia(int dia, int mes, int año){
        hayCambios = true;
        String[] args = {String.valueOf(dia), String.valueOf(mes), String.valueOf(año)};
        String where = "Dia=? AND Mes=? AND Año=?";
	    baseDatos.delete(TABLA_SERVICIOS_DIA, where, args);
    }

    /**
     *
     * @param servicioDia ServicioCalendario que se guardará.
     */
    public void guardaServicioDia(ServicioDia servicioDia){
        hayCambios = true;
        ContentValues valores = new ContentValues();
        valores.put("Dia", servicioDia.getDia());
        valores.put("Mes", servicioDia.getMes());
        valores.put("Año", servicioDia.getAño());
        valores.put("Linea", servicioDia.getLinea());
        valores.put("Servicio", servicioDia.getServicio());
        valores.put("Turno", servicioDia.getTurno());
        valores.put("Inicio", servicioDia.getInicio());
        valores.put("LugarInicio", servicioDia.getLugarInicio());
        valores.put("Final", servicioDia.getFinal());
        valores.put("LugarFinal", servicioDia.getLugarFinal());
	    baseDatos.insert(TABLA_SERVICIOS_DIA, null, valores);
	
    }

    /**
     *
     * @param servicioDia ServicioCalendario que se borrará.
     */
    public void borraServicioDia(ServicioDia servicioDia){
        hayCambios = true;
        String where = "Dia=? AND Mes=? AND Año=? AND Servicio=? AND Turno=? AND Linea=?";
        String[] args = new String[]{String.valueOf(servicioDia.getDia()),
                String.valueOf(servicioDia.getMes()),
                String.valueOf(servicioDia.getAño()),
                servicioDia.getServicio(),
                String.valueOf(servicioDia.getTurno()),
                servicioDia.getLinea()};
	    baseDatos.delete(TABLA_SERVICIOS_DIA, where, args);
    }

    //endregion


    //region  METODOS HORAS AJENAS

    public Cursor cursorAjenas(){
        String orderBy = "Año ASC, Mes ASC, Dia ASC";
        return baseDatos.query(TABLA_HORAS_AJENAS, null, null, null, null, null, orderBy);
    }

    /**
     *
     * @param mes Mes hasta donde se devolverán las horas ajenas.
     * @param año Año del mes donde se devolverán las horas ajenas.
     * @return Horas ajenas hasta el mes indicado.
     */
    public double ajenasHastaMes(int mes, int año){
        double ajenas = 0d;
        String consulta = "SELECT SUM(Horas)" +
                " AS sumaAjenas FROM HorasAjenas WHERE Año<" + String.valueOf(año);
        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) ajenas += c.getDouble(c.getColumnIndex("sumaAjenas"));
        consulta = "SELECT SUM(Horas) AS sumaAjenas FROM HorasAjenas WHERE Mes<=" +
                String.valueOf(mes) + " AND Año=" + String.valueOf(año);
        c = baseDatos.rawQuery(consulta, null);
        if (c.moveToFirst()) ajenas += c.getDouble(c.getColumnIndex("sumaAjenas"));

        c.close();
        return ajenas;
    }

    public void setAjena(HoraAjena horaAjena){
        hayCambios = true;
        if (horaAjena.getDia() == 0 || horaAjena.getMes() == 0 || horaAjena.getAño() == 0) return;
        ContentValues valores = new ContentValues();
        valores.put("Dia", horaAjena.getDia());
        valores.put("Mes", horaAjena.getMes());
        valores.put("Año", horaAjena.getAño());
        valores.put("Horas", horaAjena.getHoras());
        valores.put("Motivo", horaAjena.getMotivo());
	    baseDatos.insert(TABLA_HORAS_AJENAS, null, valores);
    }

    public boolean borrarAjena(int id){
        if (id == -1) return false;
        hayCambios = true;
        String where = "_id=" + String.valueOf(id);
        return (baseDatos.delete(TABLA_HORAS_AJENAS, where, null) > 0);
    }

    public void setAjenaBisiestos(double horas, int año){

        HoraAjena hora = new HoraAjena();
        hora.setAño(año);
        hora.setHoras(horas);
        hora.setMotivo("Regulación año bisiesto.");
        hora.setDia(29);
        hora.setMes(2);

        String consulta = "SELECT * FROM HorasAjenas WHERE Año=" + String.valueOf(año) +
                " AND Motivo='Regulación año bisiesto.'";

        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.getCount() > 0){
            String[] args = {String.valueOf(año), "Regulación año bisiesto."};
            String where = "Año=? AND Motivo=?";
            baseDatos.delete(TABLA_HORAS_AJENAS, where, args);
        }
        c.close();

        if (horas != 0)
        {
	        setAjena(hora);
        }
	
    }

    public void setAjenaFinAño(double horas, int año){

        HoraAjena hora = new HoraAjena();
        hora.setAño(año);
        hora.setHoras(horas);
        hora.setMotivo("Regulación fin de año.");
        hora.setDia(31);
        hora.setMes(12);

        String consulta = "SELECT * FROM HorasAjenas WHERE Año=" + String.valueOf(año) +
                " AND Motivo='Regulación fin de año.'";

        Cursor c = baseDatos.rawQuery(consulta, null);
        if (c.getCount() > 0){
            String[] args = {String.valueOf(año), "Regulación fin de año."};
            String where = "Año=? AND Motivo=?";
            baseDatos.delete(TABLA_HORAS_AJENAS, where, args);
        }
        c.close();

        if (horas != 0) setAjena(hora);
    }

    //endregion


    //region  METODOS LINEAS

    public Cursor cursorLineas(){
        String orderBy = "Linea ASC";
        return baseDatos.query(TABLA_LINEAS, null, null, null, null, null, orderBy);
    }

    public Linea getLinea(String linea){
        Linea l = new Linea();
        String where = "Linea='" + linea.trim() + "'";
        Cursor c = baseDatos.query(TABLA_LINEAS, null, where, null, null, null, null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        l.setLinea(c.getString(c.getColumnIndex("Linea")));
        l.setTexto(c.getString(c.getColumnIndex("Texto")));

        c.close();
        return l;
    }

    public Linea getLinea(int id){
        Linea l = new Linea();
        String where = "_id=" + String.valueOf(id);
        Cursor c = baseDatos.query(TABLA_LINEAS, null, where, null, null, null, null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        l.setLinea(c.getString(c.getColumnIndex("Linea")));
        l.setTexto(c.getString(c.getColumnIndex("Texto")));

        c.close();
        return l;
    }

    public boolean setLinea(Linea linea){
        if (linea == null) return false;
        hayCambios = true;
        Linea l = getLinea(linea.getLinea());
        if (l != null) borrarLinea(l.getLinea());
        ContentValues valores = new ContentValues();
        valores.put("Linea", linea.getLinea());
        valores.put("Texto", linea.getTexto());
        return (baseDatos.insert(TABLA_LINEAS, null, valores) > -1);
    }

    public void borrarLinea(String linea){
        if (linea == null) return;
        hayCambios = true;
        String[] args = new String[]{linea};
        String where = "Linea=?";
	    baseDatos.delete(TABLA_LINEAS, where, args);
    }

    //endregion


    //region  METODOS SERVICIOS

    public Cursor cursorServiciosLinea(String linea){
        String where = "Linea='" + linea + "'";
        String orderBy = "Servicio ASC, Turno ASC";
        return baseDatos.query(TABLA_SERVICIOS, null, where, null, null, null, orderBy);
    }

    public Servicio getServicio(int id ){
        String[] args = new String[]{String.valueOf(id)};
        String where = "_id=?";
        Cursor c = baseDatos.query(TABLA_SERVICIOS, null, where, args, null, null, null);
        if (c.getCount() == 0) return null;
        Servicio s = new Servicio();
        c.moveToFirst();
        s.setLinea(c.getString(c.getColumnIndex("Linea")));
        s.setServicio(c.getString(c.getColumnIndex("Servicio")));
        s.setTurno(c.getInt(c.getColumnIndex("Turno")));
        s.setTomaDeje(c.getString(c.getColumnIndex("TomaDeje")));
        s.setEuros(c.getDouble(c.getColumnIndex("Euros")));
        s.setInicio(c.getString(c.getColumnIndex("Inicio")));
        s.setLugarInicio(c.getString(c.getColumnIndex("LugarInicio")));
        s.setFinal(c.getString(c.getColumnIndex("Final")));
        s.setLugarFinal(c.getString(c.getColumnIndex("LugarFinal")));

        c.close();
        return s;
    }

    public Servicio getServicio(String linea, String servicio, int turno){
        String[] args = new String[]{linea, servicio, String.valueOf(turno)};
        String where = "Linea=? AND Servicio=? AND Turno=?";
        Cursor c = baseDatos.query(TABLA_SERVICIOS, null, where, args, null, null, null);
        if (c.getCount() == 0) return null;
        Servicio s = new Servicio();
        c.moveToFirst();
        s.setLinea(c.getString(c.getColumnIndex("Linea")));
        s.setServicio(c.getString(c.getColumnIndex("Servicio")));
        s.setTurno(c.getInt(c.getColumnIndex("Turno")));
        s.setTomaDeje(c.getString(c.getColumnIndex("TomaDeje")));
        s.setEuros(c.getDouble(c.getColumnIndex("Euros")));
        s.setInicio(c.getString(c.getColumnIndex("Inicio")));
        s.setLugarInicio(c.getString(c.getColumnIndex("LugarInicio")));
        s.setFinal(c.getString(c.getColumnIndex("Final")));
        s.setLugarFinal(c.getString(c.getColumnIndex("LugarFinal")));

        c.close();
        return s;
    }

    public void setServicio(int id, Servicio servicio){
        if (servicio == null) return;
        hayCambios = true;
        if (id != -1) borrarServicio(id);
        ContentValues valores = new ContentValues();
        valores.put("Linea", servicio.getLinea());
        valores.put("Servicio", servicio.getServicio());
        valores.put("Turno", servicio.getTurno());
        valores.put("Inicio", servicio.getInicio());
        valores.put("TomaDeje", servicio.getTomaDeje());
        valores.put("Euros", servicio.getEuros());
        valores.put("LugarInicio", servicio.getLugarInicio());
        valores.put("Final", servicio.getFinal());
        valores.put("LugarFinal", servicio.getLugarFinal());
	    baseDatos.insert(TABLA_SERVICIOS, null, valores);
    }

    public void borrarServicio(int id){
        hayCambios = true;
        String[] args = new String[]{String.valueOf(id)};
        String where = "_id=?";
	    baseDatos.delete(TABLA_SERVICIOS, where, args);
    }

    //endregion


    //region  METODOS RELEVOS

    public Cursor cursorRelevos(int orden){

        String orderBy;
        switch (orden){
            case RELEVOS_POR_MATRICULA:
                orderBy = "Matricula ASC";
                break;
            case RELEVOS_POR_NOMRE:
                orderBy = "Nombre ASC";
                break;
            case RELEVOS_POR_APELLIDOS:
                orderBy = "Apellidos ASC";
                break;
            default:
                orderBy = "Matricula ASC";
        }
        return baseDatos.query(TABLA_RELEVOS, null, null, null, null, null, orderBy);
    }

    public Relevo getRelevo(int matricula){
        if (matricula == 0) return null;
        Relevo r = new Relevo();
        String where = "Matricula=" + String.valueOf(matricula);
        Cursor c = baseDatos.query(TABLA_RELEVOS, null, where, null, null, null, null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        r.setMatricula(c.getInt(c.getColumnIndex("Matricula")));
        r.setNombre(c.getString(c.getColumnIndex("Nombre")));
        r.setApellidos(c.getString(c.getColumnIndex("Apellidos")));
        r.setTelefono(c.getString(c.getColumnIndex("Telefono")));
        r.setCalificacion(c.getInt(c.getColumnIndex("Calificacion")));
        r.setDeuda(c.getInt(c.getColumnIndex("Deuda")));
        r.setNotas(c.getString(c.getColumnIndex("Notas")));

        c.close();
        return r;
    }

    public boolean setRelevo(Relevo relevo){
        if (relevo == null) return false;
        if (relevo.getMatricula() == 0) return false;
        hayCambios = true;
        ContentValues valores = new ContentValues();
        valores.put("Matricula", relevo.getMatricula());
        valores.put("Nombre", relevo.getNombre());
        valores.put("Apellidos", relevo.getApellidos());
        valores.put("Telefono", relevo.getTelefono());
        valores.put("Calificacion", relevo.getCalificacion());
        valores.put("Deuda", relevo.getDeuda());
        valores.put("Notas", relevo.getNotas());
        return (baseDatos.insertWithOnConflict(TABLA_RELEVOS, null,
                                               valores, SQLiteDatabase.CONFLICT_REPLACE) > -1);
    }

    public void borrarRelevo(int matricula){
        if (matricula < 1) return;
        hayCambios = true;
        String where = "Matricula=" + String.valueOf(matricula);
	    baseDatos.delete(TABLA_RELEVOS, where, null);
    }

    public int calificacionRelevo(int matricula){
        if (matricula == 0) return 0;
        Relevo r = getRelevo(matricula);
        if (r == null) return 0;
        return r.getCalificacion();
    }

    public int deudaRelevo(int matricula){
        if (matricula == 0) return 0;
        Relevo r = getRelevo(matricula);
        if (r == null) return 0;
        int deuda = r.getDeuda();
        String where11 = "CodigoIncidencia=11 AND MatriculaSusti=" + String.valueOf(matricula); // Nos hacen
        String where12 = "CodigoIncidencia=12 AND MatriculaSusti=" + String.valueOf(matricula); // Hacemos
        Cursor cursor = baseDatos.query(TABLA_CALENDARIO, null, where11, null, null, null, null);
        deuda -= cursor.getCount();
        cursor = baseDatos.query(TABLA_CALENDARIO, null, where12, null, null, null, null);
        deuda += cursor.getCount();
        cursor.close();
        return deuda;
    }

    //endregion


    //region METODOS SERVICIOS AUXILIARES

    public Cursor cursorServiciosAuxiliares(String linea, String servicio, int turno){
        String[] args = new String[]{linea, servicio, String.valueOf(turno)};
        String where = "Linea=? AND Servicio=? AND Turno=?";
        String orderBy = "ServicioAuxiliar ASC, TurnoAuxiliar ASC";
        return baseDatos.query(TABLA_SERVICIOS_AUXILIARES, null, where, args, null, null, orderBy);
    }

    public void setServicioAuxiliar(ServicioAuxiliar servicioAuxiliar){
        hayCambios = true;
        ContentValues valores = new ContentValues();
        valores.put("Linea", servicioAuxiliar.getLinea());
        valores.put("Servicio", servicioAuxiliar.getServicio());
        valores.put("Turno", servicioAuxiliar.getTurno());
        valores.put("LineaAuxiliar", servicioAuxiliar.getLineaAuxiliar());
        valores.put("ServicioAuxiliar", servicioAuxiliar.getServicioAuxiliar());
        valores.put("TurnoAuxiliar", servicioAuxiliar.getTurnoAuxiliar());
        valores.put("Inicio", servicioAuxiliar.getInicio());
        valores.put("LugarInicio", servicioAuxiliar.getLugarInicio());
        valores.put("Final", servicioAuxiliar.getFinal());
        valores.put("LugarFinal", servicioAuxiliar.getLugarFinal());
	    baseDatos.insert(TABLA_SERVICIOS_AUXILIARES, null, valores);
	
    }

    public void borraServicioAuxiliar(int id){
        hayCambios = true;
        String where = "_id=?";
        String[] args = new String[]{String.valueOf(id)};
	    baseDatos.delete(TABLA_SERVICIOS_AUXILIARES, where, args);
    }

    public void vaciarServiciosAuxiliares(String linea, String servicio, int turno){
        hayCambios = true;
        String[] args = new String[]{linea, servicio, String.valueOf(turno)};
        String where = "Linea=? AND Servicio=? AND Turno=?";
	    baseDatos.delete(TABLA_SERVICIOS_AUXILIARES, where, args);
	
    }

    //endregion


    //region  METODOS DE COPIA DE SEGURIDAD

    public boolean hacerCopiaSeguridad(){
        return baseHelper.hacerCopia();
    }

    public boolean restaurarCopiaSeguridad(){
        hayCambios = true;
        return baseHelper.restaurarCopia();
    }

    //endregion


    //region   CLASE HELPER PARA LAS BASES DE DATOS. VERSION = 3

    private static class BaseHelper extends SQLiteOpenHelper {

        Context context = null;

        // Versión de la Base de Datos = 4
        BaseHelper(Context context){
            super(context, BASE_NAME, null, 4);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            hayCambios = true;
            // CREAR LAS TABLAS EN LA BASE DE DATOS
            db.execSQL(CREAR_TABLA_CALENDARIO);
            db.execSQL(CREAR_TABLA_SERVICIOS_DIA);
            db.execSQL(CREAR_TABLA_HORAS_AJENAS);
            db.execSQL(CREAR_TABLA_INCIDENCIAS);
            db.execSQL(CREAR_TABLA_RELEVOS);
            db.execSQL(CREAR_TABLA_LINEAS);
            db.execSQL(CREAR_TABLA_SERVICIOS);
            db.execSQL(CREAR_TABLA_SERVICIOS_AUXILIARES);
            crearIncidencias(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            hayCambios = true;
            // Evaluamos cual es la siguiente versión a la que teníamos.
            int nuevaVersion = oldVersion + 1;
            // Ejecutamos la actualización, partiendo de la versión siguiente a la que tenemos.
            // Omitiendo los Breaks, pasamos de una versión 1, por ejemplo, a una 4, incrementalmente.
            switch (nuevaVersion){
                case 2:
                    // Cambiamos el nombre de las tablas afectadas
                    db.execSQL("ALTER TABLE Calendario RENAME TO CalendarioOld;");
                    db.execSQL("ALTER TABLE ServiciosCalendario RENAME TO ServiciosCalendarioOld;");
                    db.execSQL("ALTER TABLE Servicios RENAME TO ServiciosOld;");
                    db.execSQL("ALTER TABLE ServiciosAuxiliares RENAME TO ServiciosAuxiliaresOld;");
                    // Creamos de nuevo las tablas afectadas con la nueva estructura.
                    db.execSQL(CREAR_TABLA_CALENDARIO);
                    db.execSQL(CREAR_TABLA_SERVICIOS_DIA);
                    db.execSQL(CREAR_TABLA_SERVICIOS);
                    db.execSQL(CREAR_TABLA_SERVICIOS_AUXILIARES);
                    // Copiamos los datos de las tablas antiguas a las nuevas
                    db.execSQL(COPIAR_TABLA_CALENDARIO_V2);
                    db.execSQL(COPIAR_TABLA_SERVICIOS_DIA_V2);
                    db.execSQL(COPIAR_TABLA_SERVICIOS_V2);
                    db.execSQL(COPIAR_TABLA_SERVICIOS_AUXILIARES_V2);
                    // Borramos las tablas antiguas
                    db.execSQL("DROP TABLE CalendarioOld");
                    db.execSQL("DROP TABLE ServiciosCalendarioOld");
                    db.execSQL("DROP TABLE ServiciosOld");
                    db.execSQL("DROP TABLE ServiciosAuxiliaresOld");
                    break;
                case 3:
                    // Cambiamos el nombre de las tablas afectadas
                    db.execSQL("ALTER TABLE Calendario RENAME TO CalendarioOld;");
                    db.execSQL("ALTER TABLE Servicios RENAME TO ServiciosOld;");
                    // Creamos de nuevo las tablas afectadas con la nueva estructura.
                    db.execSQL(CREAR_TABLA_CALENDARIO);
                    db.execSQL(CREAR_TABLA_SERVICIOS);
                    // Copiamos los datos de las tablas antiguas a las nuevas
                    db.execSQL(COPIAR_TABLA_CALENDARIO_V3);
                    db.execSQL(COPIAR_TABLA_SERVICIOS_V3);
                    // Borramos las tablas antiguas
                    db.execSQL("DROP TABLE CalendarioOld");
                    db.execSQL("DROP TABLE ServiciosOld");
                    break;
                case 4:
                    // Cambiamos el nombre de las tablas afectadas
                    db.execSQL("ALTER TABLE Calendario RENAME TO CalendarioOld;");
                    // Creamos de nuevo las tablas afectadas con la nueva estructura.
                    db.execSQL(CREAR_TABLA_CALENDARIO);
                    // Copiamos los datos de las tablas antiguas a las nuevas
                    db.execSQL(COPIAR_TABLA_CALENDARIO_V4);
                    // Borramos las tablas antiguas
                    db.execSQL("DROP TABLE CalendarioOld");
                    break;
            }
        }

        // CREAR INCIDENCIAS PROTEGIDAS (0 AL 16)
        private void crearIncidencias(SQLiteDatabase db){
            hayCambios = true;
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (0, 'Repetir Día Anterior', 0)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (1, 'Trabajo', 1)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (2, 'Franqueo', 4)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (3, 'Vacaciones', 4)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (4, 'F.O.D.', 3)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (5, 'Franqueo a Trabajar', 2)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (6, 'Enfermo/a', 4)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (7, 'Accidentado/a', 4)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (8, 'Permiso', 6)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (9, 'F.N.R. Año Actual', 4)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (10, 'F.N.R. Año Anterior', 4)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (11, 'Nos hacen el día', 1)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (12, 'Hacemos el día', 5)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (13, 'Sanción', 4)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (14, 'En otro destino', 4)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (15, 'Huelga', 5)");
            db.execSQL("INSERT INTO Incidencias (Codigo, Incidencia, Tipo)" +
                    "VALUES (16, 'Día por H. Acumuladas', 3)");
        }

        // HACER COPIA DE SEGURIDAD
        boolean hacerCopia(){

            // Evaluamos si se puede escribir en la tarjeta de memoria, sino salimos
            String estadoMemoria = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(estadoMemoria)) {
                return false;
            }

            // Definimos el path de la base de datos.
            String origen = context.getDatabasePath(BASE_NAME).getPath();

            // Definimos el path de destino y lo creamos si no existe.
            String destino = Environment.getExternalStorageDirectory().getPath();
            destino = destino + "/Quattroid";
            File d = new File(destino);
            if (!d.exists()){
	            //noinspection ResultOfMethodCallIgnored
                d.mkdir();
            }

            // Creamos el path del archivo de destino
            destino = destino + "/backup.db";

            // Cerramos la base de datos para que se guarden las operaciones pendientes.
            close();

            // Copiamos el archivo de base de datos en el archivo de destino.
            try{
                FileInputStream archivoDatos = new FileInputStream(origen);
                FileOutputStream archivoDestino = new FileOutputStream(destino);
                copiarArchivo(archivoDatos, archivoDestino);
            } catch (IOException e){
                return false;
            }
            return true;
        }

        // RESTAURAR COPIA DE SEGURIDAD
        boolean restaurarCopia(){

            // Evaluamos si se puede escribir en la tarjeta de memoria, sino salimos
            String estadoMemoria = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(estadoMemoria)) {
                return false;
            }
            hayCambios = true;

            // Definimos el path de la copia de seguridad
            String origen = Environment.getExternalStorageDirectory().getPath();
            origen = origen + "/Quattroid/backup.db";

            // Evaluamos si existe una copia de seguridad
            if (!new File(origen).exists()) return false;

            // Definimos el path de la base de datos.
            String destino = context.getDatabasePath(BASE_NAME).getPath();

            // Cerramos la base de datos para que este liberado el archivo
            close();

            // Copiamos los archivos
            try{
                FileInputStream archivoOrigen = new FileInputStream(origen);
                FileOutputStream archivoDestino = new FileOutputStream(destino);
                copiarArchivo(archivoOrigen, archivoDestino);
	            //noinspection ResultOfMethodCallIgnored
                new File(destino).setLastModified(new Date().getTime());
            } catch (IOException e){
                return false;
            }

            // Reabrimos la base de datos para que se establezcan las caches y se marque como creada.
            getWritableDatabase().close();

            return true;
        }

        // COPIAR UN ARCHIVO
        static void copiarArchivo(FileInputStream origen, FileOutputStream destino) throws IOException {
            FileChannel fromChannel = null;
            FileChannel toChannel = null;
            try {
                fromChannel = origen.getChannel();
                toChannel = destino.getChannel();
                fromChannel.transferTo(0, fromChannel.size(), toChannel);
            } finally {
                try {
                    if (fromChannel != null) {
                        fromChannel.close();
                    }
                } finally {
                    if (toChannel != null) {
                        toChannel.close();
                    }
                }
            }
        }


    }

    //endregion

}
