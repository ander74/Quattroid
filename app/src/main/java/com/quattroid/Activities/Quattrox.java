/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.Activities;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Quattrox extends Activity {

    TextView Texto = null;

    // AL CREARSE UNA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_quattrox);
        Texto = findViewById(R.id.texto);
        Texto.setText(Html.fromHtml(leeTexto()));
    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // AL PULSAR UN BOTON
    public void BotonPulsado(View v) {
        switch (v.getId()) {
            case R.id.bt_Aceptar:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    // LEE EL ARCHIVO QUATTROX Y LO DEVUELVE EN UNA CADENA
    private String leeTexto() {
        // Cadenas de texto que se usarán
        StringBuilder texto = new StringBuilder();
        // Manager de la carpeta Assets
        AssetManager am = getAssets();

        try (InputStream is = am.open("quattrox.txt");
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            // Leemos la primera línea del archivo.
            String linea = br.readLine();
            // Mientras las líneas existan, las leemos
            while (linea != null) {
                texto.append(linea).append("\n");
                linea = br.readLine();
            }
        } catch (IOException e) {
            // No hacemos nada.
        }
        return texto.toString();
    }

}
