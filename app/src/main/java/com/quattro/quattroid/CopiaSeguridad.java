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
    //SharedPreferences opciones = null;
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
        datos = new BaseDatos(this);

        // Ocultar la confirmación
        confirmacion.setVisibility(View.GONE);

        // Buscar el archivo de copia de seguridad.
        String estadoSD = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(estadoSD)) {
            File archivoCopia = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Quattroid/backup.db");
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
                if (datos.hacerCopiaSeguridad()){
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



}
