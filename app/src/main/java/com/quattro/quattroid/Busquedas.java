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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import BaseDatos.BaseDatos;
import BaseDatos.DatosDia;
import BaseDatos.Relevo;
import Objetos.Hora;

public class Busquedas extends Activity implements View.OnFocusChangeListener, View.OnLongClickListener, AdapterView.OnItemClickListener{

    // CONSTANTES
    private final int BOTONES = 0;
    private final int POR_SERVICIO = 1;
    private final int POR_MATRICULA = 2;
    private final int POR_INCIDENCIA = 3;
    private final int POR_BUS = 4;

    private final int ACCION_INCIDENCIA = 1;
    private final int ACCION_DIA_CALENDARIO = 2;
    private final int ACCION_EDITA_AJENA = 3;
    private final int ACCION_EDITA_RELEVO = 4;
    private final int ACCION_LISTA_RELEVOS = 5;

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    Cursor cursor = null;
    AdaptadorBusquedas adaptador = null;
    int grupoActivo = 0;
    String where = "";
    Vibrator vibrador = null;
    SharedPreferences opciones = null;


    // ELEMENTOS DEL VIEW
    EditText linea = null;
    EditText servicio = null;
    EditText turno = null;
    EditText matricula = null;
    CheckBox deuda = null;
    EditText año = null;
    EditText bus = null;
    CheckBox limitarAño = null;
    LinearLayout grupoBotones = null;
    LinearLayout grupoServicio = null;
    LinearLayout grupoMatrícula = null;
    LinearLayout grupoBus = null;
    LinearLayout grupoVerListado = null;
    Button verListado = null;
    ListView listaBusquedas = null;

    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.busquedas);
        getActionBar().setTitle("Busquedas");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_busquedas);

        // Inicialización de los elementos
        context = this;

        linea = (EditText) findViewById(R.id.et_linea);
        servicio = (EditText) findViewById(R.id.et_servicio);
        turno = (EditText) findViewById(R.id.et_turno);
        matricula = (EditText) findViewById(R.id.et_matricula);
        deuda = (CheckBox) findViewById(R.id.ch_deuda);
        año = (EditText) findViewById(R.id.et_año);
        bus = (EditText) findViewById(R.id.et_bus);
        limitarAño = (CheckBox) findViewById(R.id.ch_limitarAño);
        grupoBotones = (LinearLayout) findViewById(R.id.ly_botones);
        grupoServicio = (LinearLayout) findViewById(R.id.ly_porServicio);
        grupoMatrícula = (LinearLayout) findViewById(R.id.ly_porMatricula);
        grupoBus = (LinearLayout) findViewById(R.id.ly_porBus);
        grupoVerListado = (LinearLayout) findViewById(R.id.ly_verListado);
        verListado = (Button) findViewById(R.id.bt_verlistado);
        listaBusquedas = (ListView) findViewById(R.id.lw_listaBusquedas);

        // Ocultar los bloques inicialmente
        grupoServicio.setVisibility(View.GONE);
        grupoMatrícula.setVisibility(View.GONE);
        grupoVerListado.setVisibility(View.GONE);
        grupoBus.setVisibility(View.GONE);
        listaBusquedas.setVisibility(View.GONE);

        // Registrar los listeners
        servicio.setOnFocusChangeListener(this);
        turno.setOnFocusChangeListener(this);
        matricula.setOnFocusChangeListener(this);
        matricula.setOnLongClickListener(this);
        listaBusquedas.setOnItemClickListener(this);
        servicio.setOnLongClickListener(this);
        linea.setOnLongClickListener(this);

        // Iniciar la base de datos
        datos = new BaseDatos(this);
        opciones = PreferenceManager.getDefaultSharedPreferences(this);

        // Definimos el vibrador.
        vibrador = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        // Activamos el teclado numérico en los campos habituales.
        if (opciones.getBoolean("ActivarTecladoNumerico", false)){
            linea.setInputType(InputType.TYPE_CLASS_PHONE);
            servicio.setInputType(InputType.TYPE_CLASS_PHONE);
        }


        // Iniciar el adaptador y el cursor
        cursor = datos.cursorBusqueda("Dia=0");
        adaptador = new AdaptadorBusquedas(context, cursor);

        // Vincular la lista con el adaptador
        listaBusquedas.setAdapter(adaptador);

    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (grupoActivo == BOTONES) {
                finish();
            } else {
                grupoServicio.setVisibility(View.GONE);
                grupoMatrícula.setVisibility(View.GONE);
                grupoVerListado.setVisibility(View.GONE);
                grupoBus.setVisibility(View.GONE);
                listaBusquedas.setVisibility(View.GONE);
                grupoBotones.setVisibility(View.VISIBLE);
                grupoActivo = BOTONES;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL PULSAR UN BOTON
    public void botonPulsado(View v){
        switch (v.getId()){
            case R.id.bt_servicio:
                grupoActivo = POR_SERVICIO;
                grupoServicio.setVisibility(View.VISIBLE);
                grupoMatrícula.setVisibility(View.GONE);
                grupoBus.setVisibility(View.GONE);
                grupoVerListado.setVisibility(View.VISIBLE);
                listaBusquedas.setVisibility(View.VISIBLE);
                grupoBotones.setVisibility(View.GONE);
                break;
            case R.id.bt_matricula:
                grupoActivo = POR_MATRICULA;
                grupoServicio.setVisibility(View.GONE);
                grupoBus.setVisibility(View.GONE);
                grupoMatrícula.setVisibility(View.VISIBLE);
                grupoVerListado.setVisibility(View.VISIBLE);
                listaBusquedas.setVisibility(View.VISIBLE);
                grupoBotones.setVisibility(View.GONE);
                break;
            case R.id.bt_incidencia:
                grupoActivo = POR_INCIDENCIA;
                Intent intent = new Intent(context, Incidencias.class);
                startActivityForResult(intent, ACCION_INCIDENCIA);
                break;
            case R.id.bt_bus:
                grupoActivo = POR_BUS;
                grupoServicio.setVisibility(View.GONE);
                grupoMatrícula.setVisibility(View.GONE);
                grupoBus.setVisibility(View.VISIBLE);
                grupoVerListado.setVisibility(View.VISIBLE);
                listaBusquedas.setVisibility(View.VISIBLE);
                grupoBotones.setVisibility(View.GONE);
                break;
            case R.id.bt_verlistado:
                verLista();
                break;
        }
    }

    // AL CAMBIAR EL FOCO UN CAMPO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.et_turno:
                    String s = turno.getText().toString().trim();
                    switch (s){
                        case "1":case "2":
                            turno.setText(s);
                            break;
                        default:
                            turno.setText("");
                    }
                    break;
                case R.id.et_servicio:
                    servicio.setText(Hora.validarServicio(servicio.getText().toString()));
                    break;
                case R.id.et_matricula:
                    int i = -1;
                    try {
                        i = Integer.valueOf(matricula.getText().toString());
                    } catch (NumberFormatException e) {
                        i = -1;
                    }
                    if (i == -1) {
                        matricula.setText("");
                    }
                    break;
            }
        }
    }


    // AL HACER UN CLICK LARGO EN LA MATRÍCULA


    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()){
            case R.id.et_matricula:
                // Creamos el intent.
                Intent intent = new Intent(context, Relevos.class);
                // Lanzamos la activity
                startActivityForResult(intent, ACCION_LISTA_RELEVOS);
                break;
            case R.id.et_linea:
            case R.id.et_servicio:
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

    // AL PULSAR EL BOTON VER LISTADO
    public void verLista(){
        int añoLimite = 0;
        try{
            añoLimite = Integer.valueOf(año.getText().toString().trim());
            if (añoLimite < 2000 || añoLimite > 2050) añoLimite = 0;
        } catch (NumberFormatException e){
            añoLimite = 0;
        }
        switch (grupoActivo){
            case POR_SERVICIO:
                boolean y = false;
                where = "";
                if (!linea.getText().toString().trim().equals("")){
                    where = "Linea='" + linea.getText().toString().trim() + "' ";
                    y = true;
                }
                if (!servicio.getText().toString().trim().equals("")){
                    if (y){
                        where += "AND ";
                    }
                    where += "Servicio='" + servicio.getText().toString().trim() + "' ";
                    y = true;
                }
                if (!turno.getText().toString().trim().equals("")){
                    if (y){
                        where += "AND ";
                    }
                    where += "Turno=" + turno.getText().toString().trim();
                }
                if (where.equals("")) where = "Dia=0";
                if (limitarAño.isChecked() && añoLimite != 0) where += " AND Año=" + String.valueOf(añoLimite);
                cursor = datos.cursorBusqueda(where);
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case POR_MATRICULA:
                if (matricula.getText().toString().trim().equals("")){
                    if (deuda.isChecked()){
                        where = "CodigoIncidencia=11 OR CodigoIncidencia=12";
                    } else {
                        where = "Dia=0";
                    }
                } else {
                    if (deuda.isChecked()) {
                        where = "(MatriculaSusti=" + matricula.getText().toString().trim() +
                                " AND CodigoIncidencia=11) OR" +
                                "(MatriculaSusti=" + matricula.getText().toString().trim() +
                                " AND CodigoIncidencia=12)";
                    } else {
                        where = "Matricula=" + matricula.getText().toString().trim();
                    }
                }
                if (limitarAño.isChecked() && añoLimite != 0) where += " AND Año=" + String.valueOf(añoLimite);
                cursor = datos.cursorBusqueda(where);
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case POR_BUS:
                if (bus.getText().toString().trim().equals("")) break;
                where = "Bus='" + bus.getText().toString().trim() + "' ";
                if (limitarAño.isChecked() && añoLimite != 0) where += " AND Año=" + String.valueOf(añoLimite);
                cursor = datos.cursorBusqueda(where);
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
        }
    }

    // AL VOLVER DE UNA SUBACTIVITY
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Evaluamos el codigo de petición.
        switch (requestCode){
            case ACCION_INCIDENCIA:
                if (resultCode == RESULT_OK){
                    // Extraemos los datos
                    grupoActivo = POR_INCIDENCIA;
                    grupoServicio.setVisibility(View.GONE);
                    grupoMatrícula.setVisibility(View.GONE);
                    grupoVerListado.setVisibility(View.GONE);
                    listaBusquedas.setVisibility(View.VISIBLE);
                    grupoBotones.setVisibility(View.GONE);
                    int cod = data.getIntExtra("Codigo", -1);
                    int añoLimite = 0;
                    try{
                        añoLimite = Integer.valueOf(año.getText().toString().trim());
                        if (añoLimite < 2000 || añoLimite > 2050) añoLimite = 0;
                    } catch (NumberFormatException e){
                        añoLimite = 0;
                    }
                    if (cod < 1){
                        where = "Dia=0";
                    } else {
                        where = "CodigoIncidencia=" + String.valueOf(cod);
                    }
                    if (limitarAño.isChecked() && añoLimite != 0) where += " AND Año=" + String.valueOf(añoLimite);
                    cursor = datos.cursorBusqueda(where);
                    adaptador.changeCursor(cursor);
                    adaptador.notifyDataSetChanged();
                    break;
                }
                break;
            case ACCION_DIA_CALENDARIO:
                cursor = datos.cursorBusqueda(where);
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case ACCION_LISTA_RELEVOS:
                if (resultCode == RESULT_OK){
                    int matr = data.getIntExtra("Matricula", 0);
                    if (matr > 0){
                        matricula.setText(String.valueOf(matr));
                    }
                }
                break;
            default:
                break;
        }
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

        // Extraemos la fecha del cursor.
        Cursor c = adaptador.getCursor();
        int dia = c.getInt(c.getColumnIndex("Dia"));
        int mes = c.getInt(c.getColumnIndex("Mes"));
        int año = c.getInt(c.getColumnIndex("Año"));

        // Creamos el intent con los datos de la fecha.
        Intent intent = new Intent(context, DiaCalendario.class);
        intent.putExtra("Dia", dia);
        intent.putExtra("Mes", mes);
        intent.putExtra("Año", año);

        // Lanzamos la activity
        startActivityForResult(intent, ACCION_DIA_CALENDARIO);

    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
