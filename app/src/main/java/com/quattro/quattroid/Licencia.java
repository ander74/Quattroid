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
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Licencia extends Activity {

    TextView Licencia = null;

    // AL CREARSE UNA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_licencia);
        Licencia = (TextView) findViewById(R.id.tv_licencia);
        Licencia.setText(leeLicencia());
    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // AL PULSAR UN BOTON
    public void BotonPulsado (View v){
        switch (v.getId()){
            case R.id.bt_Aceptar:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.bt_Cancelar:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    // LEE EL ARCHIVO LICENCIA Y LO DEVUELVE EN UNA CADENA
    private String leeLicencia() {

        // Cadenas de texto que se usarán
        String texto = "";
        String linea = "";

        // Manager de la carpeta Assets
        AssetManager am = getAssets();

        // Lectores de flujo para leer el archivo
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            is = am.open("Licencia/licencia.txt");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            // Leemos la primera línea del archivo.
            linea = br.readLine();
            // Mientras las líneas existan, las leemos
            while (linea != null){
                texto += linea + "\n";
                linea = br.readLine();
            }
            // Cerramos los lectores
            br.close();
            isr.close();
            is.close();

        } catch (IOException e){
            // Intentamos cerrar los lectores que han quedado abiertos
            try {
                if (br != null) br.close();
                if (isr != null) isr.close();
                if (is != null) is.close();
            } catch (IOException ee){
                // No emitimos mensaje alguno.
            }
        }
        return texto;
    }

}
