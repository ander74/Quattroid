/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Creditos extends Activity {

    // CONSTANTES
    final static int ACCION_LICENCIA = 1;

    // VARIABLES
    Context context;
    boolean creditos = true;

    // ELEMENTOS
    LinearLayout grupoCreditos = null;
    RelativeLayout grupoLicencia = null;
    TextView Licencia = null;


    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_creditos);

        // Establecemos el contexto
        context = this;

        // Inicializamos los elementos
        Licencia = findViewById(R.id.tv_licencia);
        grupoCreditos = findViewById(R.id.ly_creditos);
        grupoLicencia = findViewById(R.id.ry_licencia);

        // Rellenamos la licencia
        Licencia.setText(leeLicencia());

    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (creditos) {
                finish();
            } else {
                grupoCreditos.setVisibility(View.VISIBLE);
                grupoLicencia.setVisibility(View.GONE);
                creditos = true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL PULSAR UN BOTON
    public void botonPulsado(View view) {

        // Evaluar el view que ha llamado.
        switch (view.getId()) {
            case R.id.bt_licencia:
                grupoCreditos.setVisibility(View.GONE);
                grupoLicencia.setVisibility(View.VISIBLE);
                creditos = false;
                break;
            case R.id.bt_aceptar:
                grupoCreditos.setVisibility(View.VISIBLE);
                grupoLicencia.setVisibility(View.GONE);
                creditos = true;
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
            while (linea != null) {
                texto += linea + "\n";
                linea = br.readLine();
            }
            // Cerramos los lectores
            br.close();
            isr.close();
            is.close();

        } catch (IOException e) {
            // Intentamos cerrar los lectores que han quedado abiertos
            try {
                if (br != null) br.close();
                if (isr != null) isr.close();
                if (is != null) is.close();
            } catch (IOException ee) {
                // No emitimos mensaje alguno.
            }
        }
        return texto;
    }


}
