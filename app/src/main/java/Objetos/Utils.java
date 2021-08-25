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


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Map;

public class Utils {

    // EVALUAR SI HAY INTERNET
    @NonNull
    public static Boolean hayInternet(Context contexto){

        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    // EVALUAR SI HAY WIFI
    @NonNull
    public static Boolean hayWifi(Context contexto){

        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnected();

    }


    // HACER COPIA DE LAS OPCIONES
    public static boolean guardarOpcionesTemp(SharedPreferences Opciones){

        // Evaluamos si se puede escribir en la tarjeta de memoria, sino salimos
        String estadoMemoria = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(estadoMemoria)) {
            return false;
        }

        // Definimos el path de destino y lo creamos si no existe.
        String destino = Environment.getExternalStorageDirectory().getPath();
        destino = destino + "/Quattroid";
        File d = new File(destino);
        if (!d.exists()){
            d.mkdir();
        }
        // Creamos el path del archivo de destino
        destino = destino + "/opciones.tmp";
        d = new File(destino);

        boolean res = false;
        ObjectOutputStream output = null;

        try{
            output = new ObjectOutputStream(new FileOutputStream(d));
            output.writeObject(Opciones.getAll());
            res = true;
        } catch (FileNotFoundException e){
            return false;
        } catch (IOException e){
            return false;
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
    public static boolean restaurarOpcionesTemp(SharedPreferences Opciones){

        // Evaluamos si se puede escribir en la tarjeta de memoria, sino salimos
        String estadoMemoria = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(estadoMemoria)) {
            return false;
        }

        // Definimos si la sincronización con dropbox está activada.
        boolean sincronizarDropbox = Opciones.getBoolean("SincronizarDropBox", false);

        // Definimos el path de destino y lo creamos si no existe.
        String destino = Environment.getExternalStorageDirectory().getPath();
        destino = destino + "/Quattroid/opciones.tmp";
        File d = new File(destino);
        if (!d.exists()){
            return false;
        }

        boolean res = false;
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(d));
            SharedPreferences.Editor prefEdit = Opciones.edit();
            prefEdit.clear();
            Map<String, ?> entries = (Map<String, ?>) input.readObject();
            for (Map.Entry<String, ?> entry : entries.entrySet()) {
                Object v = entry.getValue();
                String key = entry.getKey();
                //if (key.equals("RefreshToken")) continue;
                //if (key.equals("ValidezToken")) continue;
                //if (key.equals("DropboxCredential")) continue;
                //if (key.equals("PrimerAccesoDropbox")) continue;

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
            // Restauramos el estado de la sincronización con dropbox antes de la restauración.
            prefEdit.putBoolean("SincronizarDropBox", sincronizarDropbox);
            prefEdit.commit();
            res = true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }finally {
            // Cerramos el stream.
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                // Fallo en el cerrado del stream
            }
        }
        return res;
    }


    // Devuelve si es un año bisiesto
    public static boolean esAñoBisiesto(int año){
        Calendar fecha = Calendar.getInstance();
        fecha.set(año, 0, 1);
        return fecha.getActualMaximum(Calendar.DAY_OF_YEAR) == 366;
    }


    // Guarda un archivo de texto en la carpeta Quattroid
    public static boolean guardarLineasJson(String nombre, String texto){
        // Definimos el path de destino y lo creamos si no existe.
        String destino = Environment.getExternalStorageDirectory().getPath();
        destino = destino + "/Quattroid/Lineas";
        File d = new File(destino);
        if (!d.exists()){
            if (!d.mkdir()) return false;
        }
        // Creamos el path del archivo de destino
        destino = destino + "/" + nombre;
        d = new File(destino);

        boolean res = false;
        OutputStreamWriter output = null;

        try{
            output = new OutputStreamWriter(new FileOutputStream(d));
            output.write(texto);
            res = true;
        } catch (IOException e){
            res = false;
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



}
