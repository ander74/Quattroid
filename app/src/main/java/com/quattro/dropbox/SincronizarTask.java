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

package com.quattro.dropbox;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;
import java.util.Date;

import BaseDatos.BaseDatos;

public class SincronizarTask extends AsyncTask<Void, Void, Soporte.Resultado>{

    private final String Token;
    private final Callback mCallback;
    private final SharedPreferences Opciones;

    public interface Callback{
        void onComplete();
        void onError(Soporte.Resultado resultado);
    }

    public SincronizarTask(String token, SharedPreferences opciones, Callback callback){
        Token = token;
        Opciones = opciones;
        mCallback = callback;
    }

    @Override
    protected Soporte.Resultado doInBackground(Void... params) {

        FileMetadata metadatos;
        File archivo = new File(Soporte.ARCHIVO_BASEDATOS);
        Date FechaLocal;
        Date FechaRemoto;
        Soporte.Resultado resultado;

        try {
            metadatos = (FileMetadata) DropBoxFactory.GetCliente(Token).files().getMetadata(Soporte.ARCHIVO_DATOS);
        } catch (DbxException dbe) {
            return Soporte.Resultado.ERROR_DROPBOX;
        }

        if (metadatos != null) {
            FechaRemoto = metadatos.getClientModified();
            FechaLocal = new Date(archivo.lastModified());
        } else {
            return Soporte.Resultado.ERROR_DROPBOX;
        }

        if (FechaLocal.equals(FechaRemoto)) {
            return Soporte.Resultado.OK;

        } else if (FechaLocal.before(FechaRemoto)) {
            resultado = Soporte.DescargarBaseDatos(Token);
            if (resultado == Soporte.Resultado.OK) {
                resultado = Soporte.DescargarOpciones(Token, Opciones);
            } else {
                return Soporte.Resultado.NULL;
            }
            return resultado;

        } else if (FechaLocal.after(FechaRemoto)) {
            resultado = Soporte.SubirBaseDatos(Token);
            if (resultado == Soporte.Resultado.OK) {
                resultado = Soporte.SubirOpciones(Token, Opciones);
            } else {
                return Soporte.Resultado.NULL;
            }
            return Soporte.Resultado.OK;
        }
        return Soporte.Resultado.NULL;
    }


    @Override
    protected void onPostExecute(Soporte.Resultado resultado) {
        super.onPostExecute(resultado);

        switch (resultado){
            case OK:
                mCallback.onComplete();
                break;
            default:
                mCallback.onError(resultado);
                break;
        }
    }
}
