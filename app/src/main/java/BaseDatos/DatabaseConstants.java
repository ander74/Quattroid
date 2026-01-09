/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package BaseDatos;

public class DatabaseConstants {


    //region NOMBRES DE TABLAS

    // Nombre y versión de la base de datos
    public static final String BASE_NAME = "Quattro";
    public static final int DB_VERSION = 7;

    // Nombres de las tablas
    public static final String TABLA_CALENDARIO = "Calendario";
    public static final String TABLA_SERVICIOS_DIA = "ServiciosCalendario";
    public static final String TABLA_HORAS_AJENAS = "HorasAjenas";
    public static final String TABLA_INCIDENCIAS = "Incidencias";
    public static final String TABLA_TIPOS_INCIDENCIA = "TiposIncidencia";
    public static final String TABLA_RELEVOS = "Relevos";
    public static final String TABLA_LINEAS = "Lineas";
    public static final String TABLA_SERVICIOS = "Servicios";
    public static final String TABLA_SERVICIOS_AUXILIARES = "ServiciosAuxiliares";
    public static final String TABLA_OPCIONES = "Opciones";

    //endregion

    //region CONSULTAS DE CREACION DE LAS TABLAS

    public static final String CREAR_TABLA_CALENDARIO = "CREATE TABLE Calendario " +
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

    public static final String CREAR_TABLA_SERVICIOS_DIA = "CREATE TABLE ServiciosCalendario " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "DiaId INTEGER DEFAULT 0, " +
            "Dia INTEGER DEFAULT 0, " +
            "Mes INTEGER DEFAULT 0, " +
            "Año INTEGER DEFAULT 0, " +
            "Servicio TEXT DEFAULT '', " +
            "Turno INTEGER DEFAULT 0, " +
            "Linea TEXT DEFAULT '', " +
            "Inicio TEXT DEFAULT '', " +
            "LugarInicio TEXT DEFAULT '', " +
            "Final TEXT DEFAULT '', " +
            "LugarFinal TEXT DEFAULT '', " +
            "FOREIGN KEY (DiaId) " +
            "   REFERENCES Calendario(_id) " +
            "      ON UPDATE CASCADE " +
            "      ON DELETE CASCADE " +
            ")";

    public static final String CREAR_TABLA_HORAS_AJENAS = "CREATE TABLE HorasAjenas " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Dia INTEGER DEFAULT 0, " +
            "Mes INTEGER DEFAULT 0, " +
            "Año INTEGER DEFAULT 0, " +
            "Horas REAL DEFAULT 0, " +
            "Motivo TEXT DEFAULT '')";

    public static final String CREAR_TABLA_INCIDENCIAS = "CREATE TABLE Incidencias " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Codigo INTEGER UNIQUE, " +
            "Incidencia TEXT DEFAULT '', " +
            "Tipo INTEGER DEFAULT 0)";

    public static final String CREAR_TABLA_RELEVOS = "CREATE TABLE Relevos " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Matricula INTEGER UNIQUE, " +
            "Nombre TEXT DEFAULT '', " +
            "Apellidos TEXT DEFAULT '', " +
            "Telefono TEXT DEFAULT '', " +
            "Calificacion INTEGER DEFAULT 0, " +
            "Deuda INTEGER DEFAULT 0, " +
            "Notas TEXT DEFAULT '')";

    public static final String CREAR_TABLA_LINEAS = "CREATE TABLE Lineas " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Linea TEXT DEFAULT '', " +
            "Texto TEXT DEFAULT '')";

    public static final String CREAR_TABLA_SERVICIOS = "CREATE TABLE Servicios " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "LineaId INTEGER DEFAULT 0, " +
            "Linea TEXT DEFAULT '', " +
            "Servicio TEXT DEFAULT '', " +
            "Turno INTEGER DEFAULT 0, " +
            "TomaDeje TEXT DEFAULT '', " +
            "TomaDejeDecimal REAL DEFAULT 0, " +
            "Euros REAL DEFAULT 0, " +
            "Inicio TEXT DEFAULT '', " +
            "LugarInicio TEXT DEFAULT '', " +
            "Final TEXT DEFAULT '', " +
            "LugarFinal TEXT DEFAULT '', " +
            "FOREIGN KEY (LineaId) " +
            "   REFERENCES Lineas(_id) " +
            "      ON UPDATE CASCADE " +
            "      ON DELETE CASCADE " +
            ")";

    public static final String CREAR_TABLA_SERVICIOS_AUXILIARES = "CREATE TABLE ServiciosAuxiliares " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ServicioId INTEGER DEFAULT 0, " +
            "Linea TEXT DEFAULT '', " +
            "Servicio TEXT DEFAULT '', " +
            "Turno INTEGER DEFAULT 0, " +
            "LineaAuxiliar TEXT DEFAULT '', " +
            "ServicioAuxiliar TEXT DEFAULT '', " +
            "TurnoAuxiliar INTEGER DEFAULT 0, " +
            "Inicio TEXT DEFAULT '', " +
            "LugarInicio TEXT DEFAULT '', " +
            "Final TEXT DEFAULT '', " +
            "LugarFinal TEXT DEFAULT '', " +
            "FOREIGN KEY (ServicioId) " +
            "   REFERENCES Servicios(_id) " +
            "      ON UPDATE CASCADE " +
            "      ON DELETE CASCADE " +
            ")";


    public static final String CREAR_TABLA_OPCIONES = "CREATE TABLE Opciones " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "PrimerMes INTEGER DEFAULT 1, " + // Primer mes mostrado
            "PrimerAño INTEGER DEFAULT 2016, " + // Primer año mostrado
            "AcumuladasAnteriores REAL DEFAULT 0, " + // Horas anteriores
            "RelevoFijo INTEGER DEFAULT 0, " + // Relevo fijo
            "ModoBasico INTEGER DEFAULT 0, " + // Mostrar día básico
            "RellenarSemana INTEGER DEFAULT 0, " + // Autorellenar semana
            "JorMedia REAL DEFAULT 0, " + // Jornada media
            "JorMinima REAL DEFAULT 0, " + // Jornada mínima
            "LimiteEntreServicios INTEGER DEFAULT 0, " + // Límite entre servicios
            "DiaCierreMes INTEGER DEFAULT 1, " + // Límite entre servicios
            "JornadaAnual INTEGER DEFAULT 0, " + // Jornada anual
            "RegularJornadaAnual INTEGER DEFAULT 0, " + // Regular Jornada anual
            "RegularBisiestos INTEGER DEFAULT 0, " + // Regular años bisiestos
            "InicioNocturnas INTEGER DEFAULT 0, " + // Inicio nocturnas
            "FinalNocturnas INTEGER DEFAULT 0, " + // Final nocturnas
            "LimiteDesayuno INTEGER DEFAULT 0, " + // Limite dieta desayuno
            "LimiteComida1 INTEGER DEFAULT 0, " + // Límite dieta comida turno 1
            "LimiteComida2 INTEGER DEFAULT 0, " + // Límite dieta comida turno 2
            "LimiteCena INTEGER DEFAULT 0, " + // Límite dieta cena
            "InferirTurnos INTEGER DEFAULT 0, " + // Inferir turnos
            "DiaBaseTurnos INTEGER DEFAULT 0, " + // Dia Inferir turnos
            "MesBaseTurnos INTEGER DEFAULT 0, " + // Mes inferir turnos
            "AñoBaseTurnos INTEGER DEFAULT 0, " + // Año inferir turnos
            "PdfHorizontal INTEGER DEFAULT 0, " + // PDF en horizontal
            "PdfIncluirServicios INTEGER DEFAULT 0, " + // Incluir servicios
            "PdfIncluirNotas INTEGER DEFAULT 0, " + // Incluir notas
            "PdfAgruparNotas INTEGER DEFAULT 0, " + // Agrupar notas
            "VerMesActual INTEGER DEFAULT 0, " + // Iniciar mes actual
            "IniciarCalendario INTEGER DEFAULT 0, " + // Iniciar en el calendario
            "SumarTomaDeje INTEGER DEFAULT 0, " + // Acumular toma y deje
            "ActivarTecladoNumerico INTEGER DEFAULT 0, " + // Activar teclado numérico
            "GuardarSiempre INTEGER DEFAULT 0)"; // Guardar siempre, aunque le demos al boton atras.


    //endregion


    //region  CADENAS DE ACTUALIZACION DE BASE DE DATOS

    // VERSION 2
    public static final String COPIAR_TABLA_CALENDARIO_V2 = "INSERT INTO Calendario " +
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

    public static final String COPIAR_TABLA_SERVICIOS_DIA_V2 = "INSERT INTO ServiciosCalendario " +
            "(Dia, Mes, Año, Servicio, Turno, Linea, Inicio, Final) " +
            "SELECT " +
            "Dia, Mes, Año, Servicio, Turno, Linea, Inicio, Final " +
            "FROM ServiciosCalendarioOld;";

    public static final String COPIAR_TABLA_SERVICIOS_V2 = "INSERT INTO Servicios " +
            "(Linea, Servicio, Turno, Inicio, Final) " +
            "SELECT " +
            "Linea, Servicio, Turno, Inicio, Final " +
            "FROM ServiciosOld;";

    public static final String COPIAR_TABLA_SERVICIOS_AUXILIARES_V2 = "INSERT INTO ServiciosAuxiliares " +
            "(Linea, Servicio, Turno, LineaAuxiliar, ServicioAuxiliar, TurnoAuxiliar, Inicio, Final) " +
            "SELECT " +
            "Linea, Servicio, Turno, LineaAuxiliar, ServicioAuxiliar, TurnoAuxiliar, Inicio, Final " +
            "FROM ServiciosAuxiliaresOld;";


    // VERSION 3
    public static final String COPIAR_TABLA_CALENDARIO_V3 = "INSERT INTO Calendario " +
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

    public static final String COPIAR_TABLA_SERVICIOS_V3 = "INSERT INTO Servicios " +
            "(Linea, Servicio, Turno, Inicio, LugarInicio, Final, LugarFinal) " +
            "SELECT " +
            "Linea, Servicio, Turno, Inicio, LugarInicio, Final, LugarFinal " +
            "FROM ServiciosOld;";

    // VERSION 4
    public static final String COPIAR_TABLA_CALENDARIO_V4 = "INSERT INTO Calendario " +
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

    // VERSIÓN 6
    public static final String COPIAR_TABLA_OPCIONES_V6 = "INSERT INTO Opciones " +
            "(PrimerMes, PrimerAño, AcumuladasAnteriores, RelevoFijo, ModoBasico, RellenarSemana, JorMedia, " +
            "JorMinima, LimiteEntreServicios, JornadaAnual, RegularJornadaAnual, RegularBisiestos, " +
            "InicioNocturnas, FinalNocturnas, LimiteDesayuno, LimiteComida1, LimiteComida2, LimiteCena, " +
            "InferirTurnos, DiaBaseTurnos, MesBaseTurnos, AñoBaseTurnos, PdfHorizontal, PdfIncluirServicios, " +
            "PdfIncluirNotas, PdfAgruparNotas, VerMesActual, IniciarCalendario, SumarTomaDeje, " +
            "ActivarTecladoNumerico, GuardarSiempre) " +
            "SELECT " +
            "PrimerMes, PrimerAño, AcumuladasAnteriores, RelevoFijo, ModoBasico, RellenarSemana, JorMedia, " +
            "JorMinima, LimiteEntreServicios, JornadaAnual, RegularJornadaAnual, RegularBisiestos, " +
            "InicioNocturnas, FinalNocturnas, LimiteDesayuno, LimiteComida1, LimiteComida2, LimiteCena, " +
            "InferirTurnos, DiaBaseTurnos, MesBaseTurnos, AñoBaseTurnos, PdfHorizontal, PdfIncluirServicios, " +
            "PdfIncluirNotas, PdfAgruparNotas, VerMesActual, IniciarCalendario, SumarTomaDeje, " +
            "ActivarTecladoNumerico, GuardarSiempre " +
            "FROM OpcionesOld";

    // VERSIÓN 7
    // Además hay que procesar todos los servicios día para añadir el DiaId, todos los servicios para añadir los LineaId,
    // los servicios auxiliares para añadir los ServicioId.

    public static final String COPIAR_TABLA_SERVICIOS_DIA_V7 = "INSERT INTO ServiciosCalendario " +
            "(Dia, Mes, Año, Linea, Servicio, Turno, Inicio, LugarInicio, Final, LugarFinal) " +
            "SELECT " +
            "Dia, Mes, Año, Linea, Servicio, Turno, Inicio, LugarInicio, Final, LugarFinal " +
            "FROM ServiciosCalendarioOld;";

    public static final String COPIAR_TABLA_SERVICIOS_V7 = "INSERT INTO Servicios " +
            "(Linea, Servicio, Turno, Inicio, LugarInicio, Final, LugarFinal, TomaDeje, Euros) " +
            "SELECT " +
            "Linea, Servicio, Turno, Inicio, LugarInicio, Final, LugarFinal, TomaDeje, Euros " +
            "FROM ServiciosOld;";

    public static final String COPIAR_TABLA_SERVICIOS_AUXILIARES_V7 = "INSERT INTO ServiciosAuxiliares " +
            "(Linea, Servicio, Turno, LineaAuxiliar, ServicioAuxiliar, TurnoAuxiliar, Inicio, LugarInicio, Final, LugarFinal) " +
            "SELECT " +
            "Linea, Servicio, Turno, LineaAuxiliar, ServicioAuxiliar, TurnoAuxiliar, Inicio, LugarInicio, Final, LugarFinal " +
            "FROM ServiciosAuxiliaresOld;";

    //endregion


}
