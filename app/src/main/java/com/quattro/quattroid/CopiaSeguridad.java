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
package com.quattro.quattroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Map;

import BaseDatos.BaseDatos;

public class CopiaSeguridad extends Activity {

    // CONSTANTES
    private final int MODO_NORMAL = 0;
    private final int MODO_AVISO = 1;

    // VARIABLES
    BaseDatos datos = null;
    SharedPreferences opciones = null;
    int modo = MODO_NORMAL;

    // ELEMENTOS DEL VIEW
    LinearLayout copia = null;
    LinearLayout confirmacion = null;
    TextView fecha = null;

    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.guardar);
        getActionBar().setTitle("Copia Seguridad");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_copiaseguridad);

        // Inicializar los elementos del view.
        copia = (LinearLayout) findViewById(R.id.ly_copia);
        confirmacion = (LinearLayout) findViewById(R.id.ly_confirmacion);
        fecha = (TextView) findViewById(R.id.tv_fecha);

        // Inicializamos la base de datos y las opciones
        opciones = PreferenceManager.getDefaultSharedPreferences(this);
        datos = new BaseDatos(this);

        // Ocultar la confirmación
        confirmacion.setVisibility(View.GONE);

        // Buscar el archivo de copia de seguridad.
        String estadoSD = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(estadoSD)) {
            File archivoCopia = new File(Environment.getExternalStorageDirectory().getPath() + "/Quattroid/backup.db");
            //File archivoCopia = new File(getApplicationContext().getExternalFilesDir("CopiaSeg").getPath() + "/opciones.bak");
            if (archivoCopia.exists()){
                // Extraer la fecha del archivo
                Date f = new Date(archivoCopia.lastModified());
                String ff = String.format("%td - %tB - %tY", f, f, f);
                fecha.setText("Fecha : " + ff);
            } else {
                fecha.setText("Copia de seguridad no encontrada.");
            }
        } else {
            fecha.setText("No hay memoria externa");
        }

    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            switch (modo){
                case MODO_NORMAL:
                    finish();
                    break;
                case MODO_AVISO:
                    confirmacion.setVisibility(View.GONE);
                    copia.setVisibility(View.VISIBLE);
                    modo = MODO_NORMAL;
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL PULSAR UN BOTON
    public void botonPulsado(View v){
        switch (v.getId()){
            case R.id.bt_hacerCopia:
                if (datos.hacerCopiaSeguridad() && hacerCopiaOpciones()){
                    fecha.setText("Copia creada correctamente.");
                } else {
                    fecha.setText("No se pudo hacer la copia.");
                }
                break;
            case R.id.bt_restaurarCopia:
                copia.setVisibility(View.GONE);
                confirmacion.setVisibility(View.VISIBLE);
                modo = MODO_AVISO;
                break;
            case R.id.bt_aceptar:
                if (datos.restaurarCopiaSeguridad()){
                    // Restaurar las opciones de los ajustes.
                    restaurarCopiaOpciones();
                    // Intentar apagar la activity del calendario.
                    try{
                        Calendario.activityCalendario.finish();
                    } catch (NullPointerException e){
                        // Error en la llamada al Calendario.
                    }
                    finish();
                } else {
                    confirmacion.setVisibility(View.GONE);
                    copia.setVisibility(View.VISIBLE);
                    modo = MODO_NORMAL;
                    fecha.setText("No se pudo restaurar la copia.");
                }
                break;
            case R.id.bt_cancelar:
                confirmacion.setVisibility(View.GONE);
                copia.setVisibility(View.VISIBLE);
                modo = MODO_NORMAL;
                break;
        }
    }

    // HACER COPIA DE LAS OPCIONES
    public boolean hacerCopiaOpciones(){

        // Evaluamos si se puede escribir en la tarjeta de memoria, sino salimos
        String estadoMemoria = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(estadoMemoria)) {
            return false;
        }

        // Definimos el path de destino y lo creamos si no existe.
        String destino = Environment.getExternalStorageDirectory().getPath();
        destino = destino + "/Quattroid";
        //String destino = getApplicationContext().getExternalFilesDir("CopiaSeg").getPath();

        File d = new File(destino);
        if (!d.exists()){
            d.mkdir();
        }
        // Creamos el path del archivo de destino
        destino = destino + "/opciones.bak";
        d = new File(destino);

        boolean res = false;
        ObjectOutputStream output = null;

        try{
            output = new ObjectOutputStream(new FileOutputStream(d));
            output.writeObject(opciones.getAll());
            res = true;
        } catch (IOException e){
            // Fallo por error de ficheros.
        } finally {
            // Cerramos los streams.
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                // Falla el cerrado de los streams
            }
        }
        return res;
    }

    // RESTAURAR COPIA DE LAS OPCIONES
    public boolean restaurarCopiaOpciones(){

        // Evaluamos si se puede escribir en la tarjeta de memoria, sino salimos
        String estadoMemoria = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(estadoMemoria)) {
            return false;
        }

        // Definimos el path de destino y lo creamos si no existe.
        String destino = Environment.getExternalStorageDirectory().getPath();
        destino = destino + "/Quattroid/opciones.bak";
        //String destino = getApplicationContext().getExternalFilesDir("CopiaSeg").getPath();
        //destino = destino + "/opciones.bak";
        File d = new File(destino);
        if (!d.exists()){
            return false;
        }

        // Guardamos las opciones que no se deben perder.
        Boolean logueado = opciones.getBoolean("Logueado", false);
        String token = opciones.getString("Token", null);
        Boolean primerAccesoDropbox = opciones.getBoolean("PrimerAccesoDropBox", true);
        Boolean primerAccesoLogin = opciones.getBoolean("PrimerAccesoLogin", true);

        boolean res = false;
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(d));
            SharedPreferences.Editor prefEdit = opciones.edit();
            prefEdit.clear();
            Map<String, ?> entries = (Map<String, ?>) input.readObject();
            for (Map.Entry<String, ?> entry : entries.entrySet()) {
                Object v = entry.getValue();
                String key = entry.getKey();

                if (v instanceof Boolean)
                    prefEdit.putBoolean(key, (Boolean) v);
                else if (v instanceof Float)
                    prefEdit.putFloat(key, (Float) v);
                else if (v instanceof Integer)
                    prefEdit.putInt(key, (Integer) v);
                else if (v instanceof Long)
                    prefEdit.putLong(key, (Long) v);
                else if (v instanceof String)
                    prefEdit.putString(key, ((String) v));
            }
            prefEdit.apply();
            res = true;
        } catch (ClassNotFoundException | IOException e) {
            // Fallo en el fichero no encontrado.
        } finally {
            // Cerramos el stream.
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                // Fallo en el cerrado del stream
            }
        }

        // Desactivamos la sincronización automática.
        opciones.edit().putBoolean("SincronizarDropBox", false).apply();

        // Restauramos las opciones que no se deben perder.
        opciones.edit().putBoolean("Logueado", logueado).apply();
        opciones.edit().putString("Token", token).apply();
        opciones.edit().putBoolean("PrimerAccesoDropBox", primerAccesoDropbox).apply();
        opciones.edit().putBoolean("PrimerAccesoLogin", primerAccesoLogin).apply();

        return res;
    }


}
