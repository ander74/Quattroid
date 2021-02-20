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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.JsonWriter;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.http.OkHttpRequestor;
import com.dropbox.core.http.StandardHttpRequestor;
import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.oauth.DbxRefreshResult;
import com.dropbox.core.v2.DbxClientV2;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.quattro.quattroid.BuildConfig;
import com.quattro.quattroid.DropBox;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Locale;


public class DropBoxFactory {

    private static DbxClientV2 Cliente;
    private static String emailCuenta;


    public static DbxClientV2 GetCliente(Context context){
        // Extraemos las credenciales de las opciones
        SharedPreferences Opciones = PreferenceManager.getDefaultSharedPreferences(context);
        String credentialJson = Opciones.getString("DropboxCredential", null);
        DbxCredential credential = null;
        try{
            if (credentialJson != null) credential = DbxCredential.Reader.readFully(credentialJson);
        } catch (JsonReadException ex){
            credential = null;
        }

        // Si no hay credenciales, devolvemos null.
        if (credential == null) return null;

        if (credential.aboutToExpire()){
            try{
                DbxRequestConfig requestConfig = new DbxRequestConfig("Quattroid");
                credential.refresh(requestConfig);
                credentialJson = DbxCredential.Writer.writeToString(credential);
                Opciones.edit().putString("DropboxCredential", credentialJson).apply();
                Cliente = null;
            } catch (DbxException ignored){ }
        }

        // Si no hay cliente
        if (Cliente == null) {
            DbxRequestConfig requestConfig = new DbxRequestConfig("Quattroid");
            Cliente = new DbxClientV2(requestConfig, credential);
            try {
                emailCuenta = Cliente.users().getCurrentAccount().getEmail();
            } catch (DbxException ex){
                emailCuenta = "Fallo en el GetCliente";
            }
        }

        return Cliente;
    }




}
