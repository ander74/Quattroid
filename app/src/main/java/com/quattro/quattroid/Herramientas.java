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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;


public class Herramientas extends Activity {

    // CONSTANTES

    // VARIABLES
    Context context;

    // ELEMENTOS
    LinearLayout btConsultas = null;
    LinearLayout btBusquedas = null;
    LinearLayout btAjustes = null;


    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_herramientas);

        // Establecemos el contexto
        context = this;

    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // AL PULSAR UN BOTON
    public void BotonPulsado(View view){

        Intent intent = null;

        // Evaluar el view que ha llamado.
        switch (view.getId()){

            // Botón Consultas Pulsado
            case R.id.bt_estadisticas:
                intent = new Intent(this, Estadisticas.class);
                startActivity(intent);
                break;

            // Botón Búsquedas Pulsado
            case R.id.bt_busquedas:
                intent = new Intent(this, Busquedas.class);
                startActivity(intent);
                break;

            // Botón Incidencias Pulsado
            case R.id.bt_incidencias:
                intent = new Intent(this, Incidencias.class);
                intent.putExtra("Menu", true);
                startActivity(intent);
                break;

            // Botón Copia Seguridad Pulsado
            case R.id.bt_copiaSeguridad:
                intent = new Intent(this, CopiaSeguridad.class);
                startActivity(intent);
                break;

            // Botón Dropbox pulsado
            case R.id.bt_dropBox:
                intent = new Intent(this, DropBox.class);
                startActivity(intent);
                break;

            // Botón Ajustes Pulsado
            case R.id.bt_ajustes:
                intent = new Intent(this, Ajustes.class);
                startActivity(intent);
                break;

        }

    }

}
