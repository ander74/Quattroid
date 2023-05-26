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
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import BaseDatos.BaseDatos;
import Objetos.Colores;
import Objetos.Hora;

public class EditarServiciosDia extends Activity implements View.OnFocusChangeListener{

    // ELEMENTOS DEL VIEW
    TextView titulo = null;
    EditText linea = null;
    EditText servicio = null;
    EditText turno = null;
    EditText inicio = null;
    EditText fin = null;
    EditText lugarInicio = null;
    EditText lugarFinal = null;

    // VARIABLES
    boolean isNuevo = false;
    int id = -1;
    BaseDatos datos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.calendario);
        getActionBar().setTitle("Servicios");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_editarserviciosdia);

        // Instanciar los elementos del view
        titulo = findViewById(R.id.titulo);
        linea = findViewById(R.id.et_linea);
        servicio = findViewById(R.id.et_servicio);
        turno = findViewById(R.id.et_turno);
        inicio = findViewById(R.id.et_inicio);
        fin = findViewById(R.id.et_final);
        lugarInicio = findViewById(R.id.et_lugarInicio);
        lugarFinal = findViewById(R.id.et_lugarFinal);

        // Registramos los listeners
	    linea.setOnFocusChangeListener(this);
        servicio.setOnFocusChangeListener(this);
        turno.setOnFocusChangeListener(this);
        inicio.setOnFocusChangeListener(this);
        fin.setOnFocusChangeListener(this);

        // Accedemos a los datos del intent
        Bundle datosIntent = getIntent().getExtras();

        // Inicializamos la base de datos.
        datos = new BaseDatos(this);

        if (datosIntent == null){
            // Nuevo servicio
            titulo.setText("NUEVO SERVICIO");
            isNuevo = true;
            // Vacía los campos de servicio
            linea.setText("");
            servicio.setText("");
            turno.setText("");
            inicio.setText("");
            fin.setText("");
            lugarInicio.setText("");
            lugarFinal.setText("");
            lugarFinal.setNextFocusDownId(R.id.et_linea);
        } else {
            // Edita Servicio
            titulo.setText("EDITAR SERVICIO");
            isNuevo = false;
            linea.setFocusable(false);
            servicio.setFocusable(false);
            turno.setFocusable(false);
            linea.setTextColor(Colores.ROJO_OSCURO);
            servicio.setTextColor(Colores.ROJO_OSCURO);
            turno.setTextColor(Colores.ROJO_OSCURO);
            inicio.setSelectAllOnFocus(true);
            fin.setSelectAllOnFocus(true);
            // Ponemos los datos del intent en los campos.
            linea.setText(datosIntent.getString("Linea"));
            servicio.setText(datosIntent.getString("Servicio"));
            turno.setText(String.valueOf(datosIntent.getInt("Turno", 0)));
            inicio.setText(datosIntent.getString("Inicio"));
            fin.setText(datosIntent.getString("Final"));
            lugarInicio.setText(datosIntent.getString("LugarInicio"));
            lugarFinal.setText(datosIntent.getString("LugarFinal"));
            id = datosIntent.getInt("Id", -1);
            lugarFinal.setNextFocusDownId(R.id.et_inicio);
        }

    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editarserviciosdia, menu);
        return true;
    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.bt_guardar:
                Guardar();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (hasFocus) return;
        switch (v.getId()){
            case R.id.et_linea:
                linea.setText(linea.getText().toString().trim().toUpperCase());
                break;
            case R.id.et_servicio:
                servicio.setText(Hora.validarServicio(servicio.getText().toString()));
                break;
            case R.id.et_turno:
                String s = turno.getText().toString().trim();
                if (!s.equals("1") && !s.equals("2")){
                    turno.setText("");
                }
                break;
            case R.id.et_inicio:
                inicio.setText(Hora.horaToString(inicio.getText().toString()));
                break;
            case R.id.et_final:
                fin.setText(Hora.horaToString(fin.getText().toString()));
                break;
            default:

        }
    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // VALIDAR CAMPOS
    private void validarCampos(){
        servicio.setText(Hora.validarServicio(servicio.getText().toString()));
        inicio.setText(Hora.horaToString(inicio.getText().toString()));
        fin.setText(Hora.horaToString(fin.getText().toString()));
        String s = turno.getText().toString().trim();
        if (!s.equals("1") && !s.equals("2")){
            turno.setText("");
        }

    }


    private void Guardar(){
        validarCampos();
        Intent intent = new Intent(this, EditarServiciosDia.class);
        intent.putExtra("Linea", linea.getText().toString().trim());
        intent.putExtra("Servicio", servicio.getText().toString().trim());
        switch (turno.getText().toString().trim()){
            case "1":case "01":
                intent.putExtra("Turno", 1);
                break;
            case "2":case "02":
                intent.putExtra("Turno", 2);
                break;
            default:
                intent.putExtra("Turno", 0);
        }
        intent.putExtra("Inicio", inicio.getText().toString().trim());
        intent.putExtra("Final", fin.getText().toString().trim());
        intent.putExtra("LugarInicio", lugarInicio.getText().toString().trim());
        intent.putExtra("LugarFinal", lugarFinal.getText().toString().trim());
        intent.putExtra("Nuevo", isNuevo);
        intent.putExtra("Id", id);
        setResult(RESULT_OK, intent);
    }

}
