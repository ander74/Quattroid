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

import android.content.Context;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadUploader;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class Soporte {

    // Constantes.
    public static final String ARCHIVO_DATOS = "/Quattroid.db";
    public static final String ARCHIVO_OPCIONES = "/Opciones.bak";
    public static final String ARCHIVO_BASEDATOS = "/data/data/com.quattro.quattroid/databases/Quattro";
    public enum Resultado{NULL, OK, ERROR_DROPBOX, ERROR_ARCHIVOS}


    // Metodo estático que descarga la base de datos.
    public static Resultado DescargarBaseDatos(Context context){

        DbxDownloader<FileMetadata> descargador;
        FileMetadata metadatos;
        File archivo = new File(ARCHIVO_BASEDATOS);

        try {
            DbxClientV2 cliente = DropBoxFactory.GetCliente(context);
            if (cliente == null) return Soporte.Resultado.ERROR_DROPBOX;
            descargador = cliente.files().download(ARCHIVO_DATOS);
        } catch (DbxException dbe) {
            return Resultado.ERROR_DROPBOX;
        }
        if(descargador != null){
            try {
                FileOutputStream outputStream = new FileOutputStream(archivo);
                metadatos = descargador.download(outputStream);
                archivo.setLastModified(metadatos.getClientModified().getTime());
                outputStream.close();
            } catch (IOException ioe){
                return Resultado.ERROR_ARCHIVOS;
            } catch (DbxException dbe){
                return Resultado.ERROR_DROPBOX;
            }
        } else {
            return Resultado.ERROR_DROPBOX;
        }
        return Resultado.OK;

    }


    // Métodos estático que sube la base de datos.
    public static Resultado SubirBaseDatos(Context context){

        UploadUploader subidor;
        File archivo = new File(ARCHIVO_BASEDATOS);

        try {
            DbxClientV2 cliente = DropBoxFactory.GetCliente(context);
            if (cliente == null) return Soporte.Resultado.ERROR_DROPBOX;
            subidor = cliente.files().uploadBuilder(ARCHIVO_DATOS)
                    .withMode(WriteMode.OVERWRITE)
                    .withClientModified(new Date(archivo.lastModified()))
                    .start();
        } catch (DbxException dbe) {
            return Resultado.ERROR_DROPBOX;
        }

        if (subidor != null){
            try{
                subidor.uploadAndFinish(new FileInputStream(archivo));
            } catch (IOException ioe) {
                return Resultado.ERROR_ARCHIVOS;
            } catch (DbxException dbe) {
                return Resultado.ERROR_DROPBOX;
            }
        } else {
            return Resultado.ERROR_DROPBOX;
        }

        return Resultado.OK;
    }


}
