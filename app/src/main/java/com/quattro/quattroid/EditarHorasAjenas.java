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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import BaseDatos.BaseDatos;
import Objetos.Hora;

public class EditarHorasAjenas extends Activity implements View.OnFocusChangeListener {


    // VARIABLES
    Context context = null;
    Bundle data = null;
    boolean editar = false;
    boolean nuevaConDia = false;
    BaseDatos datos = null;

    // ELEMENTOS
    TextView titulo = null;
    EditText horas = null;
    EditText motivo = null;
    LinearLayout grupoFecha = null;
    DatePicker selectorFecha = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.ajenas);
        getActionBar().setTitle("Horas Ajenas");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_editarhorasajenas);

        // Inicialización de los elementos
        context = this;
        titulo = findViewById(R.id.tv_titulo);
        horas = findViewById(R.id.et_horas);
        motivo = findViewById(R.id.et_motivo);
        grupoFecha = findViewById(R.id.ly_seleccionFecha);
        selectorFecha = findViewById(R.id.dp_fecha);

        // Registramos los listeners
        horas.setOnFocusChangeListener(this);

        // Recuperar los datos del intent
        data = getIntent().getExtras();

        // Inicializar la base de datos
        datos = new BaseDatos(this);

        // Poner la fecha de la hora ajena
        if (data.getInt("Dia", 0) != 0) {
            String fecha = (data.getInt("Dia", 0) > 9) ? String.valueOf(data.getInt("Dia", 0)) :
                    "0" + String.valueOf(data.getInt("Dia", 0));
            fecha += " de " + Hora.MESES_MIN[data.getInt("Mes")] + " de " + String.valueOf(data.getInt("Año"));
            titulo.setText(fecha);
            grupoFecha.setVisibility(View.GONE);
            nuevaConDia = true;
        } else {
            titulo.setText("Nueva Hora Ajena");
            grupoFecha.setVisibility(View.VISIBLE);
            nuevaConDia = false;
        }

        // Definir si vamos a editar una hora ajena o crear una nueva.
        if (data.getBoolean("Nuevo", true)){
            // CREAMOS UNA NUEVA
            editar = false;
            horas.setText("0");
            motivo.setText("");
        } else {
            // EDITAMOS UNA INCIDENCIA
            editar = true;
            horas.setText(Hora.textoDecimal(data.getDouble("Horas", 0d)));
            motivo.setText(data.getString("Motivo"));
        }

    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editarhorasajenas, menu);
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

    // AL CAMBIAR EL FOCO DE UN CAMPO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // Si pierde el foco
        if (!hasFocus){
            switch (v.getId()){
                case R.id.et_horas:
                    String s = Hora.validaHoraDecimal(horas.getText().toString());
                    if (s.equals("")){
                        horas.setText("0");
                    } else {
                        horas.setText(Hora.textoDecimal(Double.parseDouble(s.replace(",", "."))));
                    }
                    break;
            }
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


    private void Guardar(){
        Intent intent = new Intent(this, EditarHorasAjenas.class);
        double horasD = 0;
        String s = Hora.validaHoraDecimal(horas.getText().toString());
        if (s.equals("")){
            horasD = 0;
        } else {
            horasD = Double.parseDouble(s.replace(",", "."));
        }
        intent.putExtra("Horas", Hora.redondeaDecimal(horasD));
        intent.putExtra("Motivo", motivo.getText().toString());
        if (nuevaConDia) {
            intent.putExtra("Id", data.getInt("Id"));
            intent.putExtra("Dia", data.getInt("Dia"));
            intent.putExtra("Mes", data.getInt("Mes"));
            intent.putExtra("Año", data.getInt("Año"));
        } else {
            intent.putExtra("Id", data.getInt("Id"));
            intent.putExtra("Dia", selectorFecha.getDayOfMonth());
            intent.putExtra("Mes", selectorFecha.getMonth() + 1);
            intent.putExtra("Año", selectorFecha.getYear());
        }
        if (editar) intent.putExtra("Editar", true);
        setResult(RESULT_OK, intent);
    }



}
