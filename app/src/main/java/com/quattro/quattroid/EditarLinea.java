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
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Vibrator;
import BaseDatos.BaseDatos;
import BaseDatos.Linea;
import Objetos.Colores;

public class EditarLinea extends Activity implements View.OnFocusChangeListener,
                                                     View.OnLongClickListener {

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    Bundle datosIntent = null;
    Linea lin = null;
    Vibrator vibrador = null;
    //SharedPreferences opciones = null;



    // ELEMENTOS DEL VIEW
    TextView titulo = null;
    EditText linea = null;
    EditText texto = null;

    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.servicios);
        getActionBar().setTitle("Lineas");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_editarlinea);

        // Inicialización de los elementos
        context = this;
        titulo = findViewById(R.id.tv_titulo);
        linea = findViewById(R.id.et_linea);
        texto = findViewById(R.id.et_texto);

        // Definir los listeners
        linea.setOnFocusChangeListener(this);
        linea.setOnLongClickListener(this);

        // Inicialización de la base de datos
        //opciones = PreferenceManager.getDefaultSharedPreferences(this);
        datos = new BaseDatos(this);

        // Definimos el vibrador.
        vibrador = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        // Activamos el teclado numérico en los campos habituales.
        if (datos.opciones.isActivarTecladoNumerico()){ //opciones.getBoolean("ActivarTecladoNumerico", false)){
            linea.setInputType(InputType.TYPE_CLASS_PHONE);
        }

        // Definir si vamos a editar una línea o crear una nueva.
        datosIntent = getIntent().getExtras();

        if (datosIntent.getInt("Id", -1) == -1){
            // NUEVA LINEA
            titulo.setText("NUEVA LINEA");
            lin = new Linea();
            linea.setText("");
            texto.setText("");
        } else {
            // EDITAR LINEA
            titulo.setText("EDITAR LINEA");
            lin = datos.getLinea(datosIntent.getInt("Id"));
            if (lin == null) finish();
            linea.setText(lin.getLinea());
            linea.setTextColor(Colores.ROJO_OSCURO);
            linea.setFocusable(false);
            texto.setText(lin.getTexto());
        }

    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editarlinea, menu);
        return true;
    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.bt_guardar:
                Guardar();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (datos.opciones.isGuardarSiempre()){
                Guardar();
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL CAMBIAR EL FOCO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!hasFocus){
            switch (v.getId()){
                case R.id.et_linea:
                    linea.setText(linea.getText().toString().trim().toUpperCase());
            }
        }

    }

    public boolean onLongClick(View v) {

        switch (v.getId()){
            case R.id.et_linea:
                vibrador.vibrate(50);
                if (((EditText)v).getInputType() == android.text.InputType.TYPE_CLASS_TEXT){
                    ((EditText)v).setInputType(InputType.TYPE_CLASS_PHONE);
                } else {
                    ((EditText)v).setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
        }
        return false;
    }



    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void Guardar(){
        if (!linea.getText().toString().trim().equals("")){
            lin.setLinea(linea.getText().toString());
            lin.setTexto(texto.getText().toString());
            datos.setLinea(lin);
        }
        setResult(RESULT_OK);
    }

}
