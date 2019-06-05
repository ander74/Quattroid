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

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.util.Locale;


public class DropBoxFactory {

    private static DbxClientV2 Cliente;
    private static String emailCuenta;

    public static DbxClientV2 GetCliente(String token){
        if (Cliente == null) {
            String userLocale = Locale.getDefault().toString();
            DbxRequestConfig requestConfig = new DbxRequestConfig("Quattroid/1.6.2", userLocale);
            Cliente = new DbxClientV2(requestConfig, token);
            try {
                emailCuenta = Cliente.users().getCurrentAccount().getEmail();
            } catch (DbxException ex){
                emailCuenta = "Fallo en el GetCliente";
            }
        }
        return Cliente;
    }

//    public static String GetEmailCuenta(){
//        if (Cliente == null) return "No hay cliente Dropbox";
//        return emailCuenta;
//    }
}
