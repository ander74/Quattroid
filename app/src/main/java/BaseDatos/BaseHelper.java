/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package BaseDatos;

import static BaseDatos.DatabaseConstants.BASE_NAME;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_CALENDARIO_V2;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_CALENDARIO_V3;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_CALENDARIO_V4;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_OPCIONES_V6;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_SERVICIOS_AUXILIARES_V2;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_SERVICIOS_AUXILIARES_V7;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_SERVICIOS_DIA_V2;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_SERVICIOS_DIA_V7;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_SERVICIOS_V2;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_SERVICIOS_V3;
import static BaseDatos.DatabaseConstants.COPIAR_TABLA_SERVICIOS_V7;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_CALENDARIO;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_HORAS_AJENAS;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_INCIDENCIAS;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_LINEAS;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_OPCIONES;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_RELEVOS;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_SERVICIOS;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_SERVICIOS_AUXILIARES;
import static BaseDatos.DatabaseConstants.CREAR_TABLA_SERVICIOS_DIA;
import static BaseDatos.DatabaseConstants.TABLA_CALENDARIO;
import static BaseDatos.DatabaseConstants.TABLA_LINEAS;
import static BaseDatos.DatabaseConstants.TABLA_OPCIONES;
import static BaseDatos.DatabaseConstants.TABLA_SERVICIOS;
import static BaseDatos.DatabaseConstants.TABLA_SERVICIOS_AUXILIARES;
import static BaseDatos.DatabaseConstants.TABLA_SERVICIOS_DIA;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.quattroid.helpers.FileHelper;
import com.quattroid.models.LineaModel;
import com.quattroid.models.ServicioAuxiliarModel;
import com.quattroid.models.ServicioModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;


public class BaseHelper extends SQLiteOpenHelper {

    Context context = null;
    SharedPreferences opc = null;

    private static BaseHelper baseHelper;

    /**
     * Devuelve una instancia única de la clase BaseHelper.
     */
    public static BaseHelper getInstance(Context context, int version) {
        if (baseHelper == null) {
            baseHelper = new BaseHelper(context, version);
        }
        return baseHelper;
    }

    // Constructor privado para el patrón singleton
    private BaseHelper(Context context, int version) {
        super(context, BASE_NAME, null, version);
        this.context = context;
        opc = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREAR LAS TABLAS EN LA BASE DE DATOS
        db.execSQL(CREAR_TABLA_CALENDARIO);
        db.execSQL(CREAR_TABLA_SERVICIOS_DIA);
        db.execSQL(CREAR_TABLA_HORAS_AJENAS);
        db.execSQL(CREAR_TABLA_INCIDENCIAS);
        db.execSQL(CREAR_TABLA_RELEVOS);
        db.execSQL(CREAR_TABLA_LINEAS);
        db.execSQL(CREAR_TABLA_SERVICIOS);
        db.execSQL(CREAR_TABLA_SERVICIOS_AUXILIARES);
        db.execSQL(CREAR_TABLA_OPCIONES);
        crearIncidencias(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Evaluamos cual es la siguiente versión a la que teníamos.
        int nuevaVersion = oldVersion + 1;
        // Ejecutamos la actualización, partiendo de la versión siguiente a la que tenemos.
        // Omitiendo los Breaks, pasamos de una versión 1, por ejemplo, a una 4, incrementalmente.
        switch (nuevaVersion) {
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
                //break;
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
                //break;
            case 4:
                // Cambiamos el nombre de las tablas afectadas
                db.execSQL("ALTER TABLE Calendario RENAME TO CalendarioOld;");
                // Creamos de nuevo las tablas afectadas con la nueva estructura.
                db.execSQL(CREAR_TABLA_CALENDARIO);
                // Copiamos los datos de las tablas antiguas a las nuevas
                db.execSQL(COPIAR_TABLA_CALENDARIO_V4);
                // Borramos las tablas antiguas
                db.execSQL("DROP TABLE CalendarioOld");
                //break;
            case 5:
                // Creamos la tabla de las opciones.
                db.execSQL(CREAR_TABLA_OPCIONES);
                // Copiamos las opciones de las preferencias a la nueva tabla.
                ContentValues valores = new ContentValues();
                valores.put("PrimerMes", (opc.getInt("PrimerMes", 10)));
                valores.put("PrimerAño", (opc.getInt("PrimerAño", 2014)));
                valores.put("AcumuladasAnteriores", (Double.longBitsToDouble(opc.getLong("AcumuladasAnteriores", 0))));
                valores.put("RelevoFijo", (opc.getInt("RelevoFijo", 0)));
                valores.put("ModoBasico", (opc.getBoolean("ModoBasico", false)));
                valores.put("RellenarSemana", (opc.getBoolean("RellenarSemana", false)));
                valores.put("JorMedia", (Double.longBitsToDouble(opc.getLong("JorMedia", 0))));
                valores.put("JorMinima", (Double.longBitsToDouble(opc.getLong("JorMinima", 0))));
                valores.put("LimiteEntreServicios", (opc.getInt("LimiteEntreServicios", 60)));
                valores.put("JornadaAnual", (opc.getInt("JornadaAnual", 1592)));
                valores.put("RegularJornadaAnual", (opc.getBoolean("RegularJornadaAnual", true)));
                valores.put("RegularBisiestos", (opc.getBoolean("RegularBisiestos", true)));
                valores.put("InicioNocturnas", (opc.getInt("InicioNocturnas", 1320)));
                valores.put("FinalNocturnas", (opc.getInt("FinalNocturnas", 390)));
                valores.put("LimiteDesayuno", (opc.getInt("LimiteDesayuno", 270)));
                valores.put("LimiteComida1", (opc.getInt("LimiteComida1", 930)));
                valores.put("LimiteComida2", (opc.getInt("LimiteComida2", 810)));
                valores.put("LimiteCena", (opc.getInt("LimiteCena", 30)));
                valores.put("InferirTurnos", (opc.getBoolean("InferirTurnos", false)));
                valores.put("DiaBaseTurnos", (opc.getInt("DiaBaseTurnos", 3)));
                valores.put("MesBaseTurnos", (opc.getInt("MesBaseTurnos", 1)));
                valores.put("AñoBaseTurnos", (opc.getInt("AñoBaseTurnos", 2021)));
                valores.put("PdfHorizontal", (opc.getBoolean("PdfHorizontal", false)));
                valores.put("PdfIncluirServicios", (opc.getBoolean("PdfIncluirServicios", false)));
                valores.put("PdfIncluirNotas", (opc.getBoolean("PdfIncluirNotas", false)));
                valores.put("PdfAgruparNotas", (opc.getBoolean("PdfAgruparNotas", false)));
                valores.put("VerMesActual", (opc.getBoolean("VerMesActual", true)));
                valores.put("IniciarCalendario", (opc.getBoolean("IniciarCalendario", false)));
                valores.put("SumarTomaDeje", (opc.getBoolean("SumarTomaDeje", false)));
                valores.put("ActivarTecladoNumerico", (opc.getBoolean("ActivarTecladoNumerico", false)));
                db.insert(TABLA_OPCIONES, null, valores);
                //break;
            case 6:
                // Cambiamos el nombre de las tablas afectadas
                db.execSQL("ALTER TABLE Opciones RENAME TO OpcionesOld;");
                // Creamos de nuevo las tablas afectadas con la nueva estructura.
                db.execSQL(CREAR_TABLA_OPCIONES);
                // Copiamos los datos de las tablas antiguas a las nuevas
                db.execSQL(COPIAR_TABLA_OPCIONES_V6);
                // Borramos las tablas antiguas
                db.execSQL("DROP TABLE OpcionesOld");
                break;
            case 7:
                // Cambiamos el nombre de las tablas afectadas
                db.execSQL("ALTER TABLE ServiciosCalendario RENAME TO ServiciosCalendarioOLD;");
                db.execSQL("ALTER TABLE Servicios RENAME TO ServiciosOLD;");
                db.execSQL("ALTER TABLE ServiciosAuxiliares RENAME TO ServiciosAuxiliaresOLD;");
                // Creamos de nuevo las tablas afectadas con la nueva estructura.
                db.execSQL(CREAR_TABLA_SERVICIOS_DIA);
                db.execSQL(CREAR_TABLA_SERVICIOS);
                db.execSQL(CREAR_TABLA_SERVICIOS_AUXILIARES);
                // Copiamos los datos de las tablas antiguas a las nuevas
                db.execSQL(COPIAR_TABLA_SERVICIOS_DIA_V7);
                db.execSQL(COPIAR_TABLA_SERVICIOS_V7);
                db.execSQL(COPIAR_TABLA_SERVICIOS_AUXILIARES_V7);
                // Borramos las tablas antiguas
                db.execSQL("DROP TABLE ServiciosCalendarioOLD");
                db.execSQL("DROP TABLE ServiciosOLD");
                db.execSQL("DROP TABLE ServiciosAuxiliaresOLD");
                AñadirIdsRelacionados(db);
                break;

        }
    }

    // CREAR INCIDENCIAS PROTEGIDAS (0 AL 16)
    private void crearIncidencias(SQLiteDatabase db) {
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
    boolean hacerCopia() {
        // Evaluamos si se puede escribir en la tarjeta de memoria, sino salimos
        String estadoMemoria = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(estadoMemoria)) {
            return false;
        }
        // Hacemos la copia
        String pathDestino = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Quattroid/backup.db";
        FileHelper.exportDatabaseToPath(context, pathDestino);
        // Cerramos la base de datos para que se guarden las operaciones pendientes.
        close();
        return true;
    }

    // RESTAURAR COPIA DE SEGURIDAD
    boolean restaurarCopia() {
        // Evaluamos si se puede escribir en la tarjeta de memoria, sino salimos
        String estadoMemoria = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(estadoMemoria)) {
            return false;
        }

        // Definimos el path de la copia de seguridad
        String origen = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/Quattroid/backup.db").getAbsolutePath();
        // Evaluamos si existe una copia de seguridad
        if (!new File(origen).exists()) return false;
        close();

        String pathDestino = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Quattroid/backup.db";
        FileHelper.importDatabaseFromPath(context, pathDestino);

        String destino = context.getDatabasePath(BASE_NAME).getPath();
        new File(destino).setLastModified(new Date().getTime());
        // Reabrimos la base de datos para que se establezcan las caches y se marque como creada.
        getWritableDatabase().close();
        return true;
    }

    // RESTAURAR COPIA DE SEGURIDAD
    boolean restaurarCopia(Uri uriOrigen) {

        // Definimos el path de la copia de seguridad
        String origen = uriOrigen.getPath();

        // Evaluamos si existe una copia de seguridad
        if (!new File(origen).exists()) return false;

        // Definimos el path de la base de datos.
        String destino = context.getDatabasePath(BASE_NAME).getPath();

        // Cerramos la base de datos para que este liberado el archivo
        close();

        // Copiamos los archivos
        try {
            FileInputStream archivoOrigen = new FileInputStream(origen);
            FileOutputStream archivoDestino = new FileOutputStream(destino);
            copiarArchivo(archivoOrigen, archivoDestino);
            //noinspection ResultOfMethodCallIgnored
            new File(destino).setLastModified(new Date().getTime());
        } catch (IOException e) {
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


    static void copiarArchivo(String origen, String destino) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Path origenPath = Paths.get(destino);
            Path destinoPath = Paths.get(destino);
            Files.deleteIfExists(destinoPath);
            Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void AñadirIdsRelacionados(SQLiteDatabase db) {
        AñadirIdsRelacionadosServiciosDia(db);
        AñadirIdsRelacionadosServicios(db);
        AñadirIdsRelacionadosServiciosAuxiliares(db);
    }

    private void AñadirIdsRelacionadosServiciosDia(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLA_SERVICIOS_DIA, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ServicioDia servDia = new ServicioDia(cursor);
                String[] args = {String.valueOf(servDia.getDia()), String.valueOf(servDia.getMes()), String.valueOf(servDia.getAño())};
                String where = "Dia=? AND Mes=? AND Año=?";
                Cursor cursor2 = db.query(TABLA_CALENDARIO, null, where, args, null, null, null);
                if (cursor2.moveToFirst()) {
                    DatosDia dia = new DatosDia(cursor2);
                    servDia.setDiaId(dia.getId());
                    String where2 = "_id=?";
                    String[] args2 = new String[]{String.valueOf(servDia.getId())};
                    db.delete(TABLA_SERVICIOS_DIA, where2, args2);
                    ContentValues valores = new ContentValues();
                    valores.put("DiaId", servDia.getDiaId());
                    valores.put("Dia", servDia.getDia());
                    valores.put("Mes", servDia.getMes());
                    valores.put("Año", servDia.getAño());
                    valores.put("Linea", servDia.getLinea());
                    valores.put("Servicio", servDia.getServicio());
                    valores.put("Turno", servDia.getTurno());
                    valores.put("Inicio", servDia.getInicio());
                    valores.put("LugarInicio", servDia.getLugarInicio());
                    valores.put("Final", servDia.getFinal());
                    valores.put("LugarFinal", servDia.getLugarFinal());
                    db.insert(TABLA_SERVICIOS_DIA, null, valores);
                }
            } while (cursor.moveToNext());
        }
    }

    private void AñadirIdsRelacionadosServicios(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLA_SERVICIOS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ServicioModel serv = new ServicioModel(cursor);
                String[] args = {String.valueOf(serv.getLinea())};
                String where = "Linea=?";
                Cursor cursor2 = db.query(TABLA_LINEAS, null, where, args, null, null, null);
                if (cursor2.moveToFirst()) {
                    LineaModel linea = new LineaModel(cursor2);
                    serv.setLineaId(linea.getId());
                    String where2 = "_id=?";
                    String[] args2 = new String[]{String.valueOf(serv.getId())};
                    db.delete(TABLA_SERVICIOS, where2, args2);
                    ContentValues valores = new ContentValues();
                    valores.put("LineaId", serv.getLineaId());
                    valores.put("Linea", serv.getLinea());
                    valores.put("Servicio", serv.getServicio());
                    valores.put("Turno", serv.getTurno());
                    valores.put("Inicio", serv.getInicio());
                    valores.put("LugarInicio", serv.getLugarInicio());
                    valores.put("Final", serv.getFinal());
                    valores.put("LugarFinal", serv.getLugarFinal());
                    valores.put("TomaDeje", serv.getTomaDeje());
                    valores.put("Euros", serv.getEuros());
                    db.insert(TABLA_SERVICIOS, null, valores);
                }
            } while (cursor.moveToNext());
        }
    }

    private void AñadirIdsRelacionadosServiciosAuxiliares(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLA_SERVICIOS_AUXILIARES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ServicioAuxiliarModel serv = new ServicioAuxiliarModel(cursor);
                String[] args = {String.valueOf(serv.getLinea()), String.valueOf(serv.getServicio()), String.valueOf(serv.getTurno())};
                String where = "Linea=? AND Servicio=? AND Turno=?";
                Cursor cursor2 = db.query(TABLA_SERVICIOS, null, where, args, null, null, null);
                if (cursor2.moveToFirst()) {
                    ServicioModel servi = new ServicioModel(cursor2);
                    serv.setServicioId(servi.getId());
                    String where2 = "_id=?";
                    String[] args2 = new String[]{String.valueOf(serv.getId())};
                    db.delete(TABLA_SERVICIOS_AUXILIARES, where2, args2);
                    ContentValues valores = new ContentValues();
                    valores.put("LineaId", serv.getServicioId());
                    valores.put("Linea", serv.getLinea());
                    valores.put("Servicio", serv.getServicio());
                    valores.put("Turno", serv.getTurno());
                    valores.put("LineaAuxiliar", serv.getLineaAuxiliar());
                    valores.put("ServicioAuxiliar", serv.getServicioAuxiliar());
                    valores.put("TurnoAuxiliar", serv.getTurnoAuxiliar());
                    valores.put("Inicio", serv.getInicio());
                    valores.put("LugarInicio", serv.getLugarInicio());
                    valores.put("Final", serv.getFinal());
                    valores.put("LugarFinal", serv.getLugarFinal());
                    db.insert(TABLA_SERVICIOS_AUXILIARES, null, valores);
                }
            } while (cursor.moveToNext());
        }
    }


}

