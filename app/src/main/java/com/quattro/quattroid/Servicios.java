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
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.quattro.models.ServicioModel;

import java.util.ArrayList;

import BaseDatos.BaseDatos;

public class Servicios extends Activity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    // CONSTANTES
    final static int ACCION_EDITA_SERVICIO = 1;
    private final ArrayList<Integer> listaIds = new ArrayList<>();

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    AdaptadorServicio adaptador = null;
    ArrayList<ServicioModel> servicios = null;
    String linea = "";

    ListView listaServicios = null;
    Button botonAddServicio = null;
    Button botonBorrarServicio = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.servicios);
        getActionBar().setTitle("Servicios");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_servicios);

        // Inicialización de los elementos
        context = this;
        listaServicios = findViewById(R.id.lw_servicios);
        botonAddServicio = findViewById(R.id.bt_addServicio);
        botonBorrarServicio = findViewById(R.id.bt_borrarServicio);

        // Recoger los datos del intent
        linea = getIntent().getExtras().getString("Linea");

        // Ponemos el título de la ActionBar
        getActionBar().setSubtitle("Línea " + linea);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);

        // Registrar el menu contextual
        registerForContextMenu(listaServicios);

        // Llenado de la lista
        servicios = datos.getServicios(linea);
        if (servicios == null) servicios = new ArrayList<>();
        adaptador = new AdaptadorServicio(this, servicios);
        listaServicios.setAdapter(adaptador);

        // Establecemos el listener de item pulsado.
        listaServicios.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaServicios.setDivider(null);
        listaServicios.setDividerHeight(0);
        listaServicios.setMultiChoiceModeListener(this);// MULTI-SELECCION
        listaServicios.setOnItemClickListener(this);
        botonAddServicio.setOnClickListener(this::botonAddServicioPulsado);
        botonBorrarServicio.setOnClickListener(this::botonBorrarServicioPulsado);
    }

    // AL PULSAR UNA OPCION DEL MENU CONTEXTUAL
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        // Guardar el elemento que ha provocado el menu contextual
//        AdapterView.AdapterContextMenuInfo acmi =
//                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int elementoPulsado = acmi.position;
//
//        // Determinar la opción pulsada
//        switch (item.getItemId()) {
//            case R.id.bt_borrar:
//                Cursor c = adaptador.getCursor();
//                datos.borrarServicio(c.getInt(c.getColumnIndexOrThrow("_id")));
//                actualizarCursor();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        // Extraemos los datos del cursor
        ServicioModel servicioPulsado = servicios.get(position);

        // Creamos un intent para devolver los datos de la incidencia
        Intent intent = new Intent(context, EditarServicio.class);
        intent.putExtra("Id", servicioPulsado.getId());
        startActivityForResult(intent, ACCION_EDITA_SERVICIO);
    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // AL VOLVER DE UNA PANTALLA
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Evaluamos el codigo de petición.
        switch (requestCode) {
            case ACCION_EDITA_SERVICIO:
                if (resultCode == RESULT_OK) {
                    actualizarLista();
                }
                break;
            default:
                break;
        }
    }


    //******************************************************************************************
    //region Multi selección

    // MULTI-SELECCION: Al seleccionar un día del calendario.
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked){
        int checkedCount = listaServicios.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selec.");

        if (checked){
            listaIds.add(position);
            servicios.get(position).setSeleccionado(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            servicios.get(position).setSeleccionado(false);
        }

        if (checkedCount > 0){
            activarBoton(botonBorrarServicio);
            desactivarBoton(botonAddServicio);
        } else {
            desactivarBoton(botonBorrarServicio);
            activarBoton(botonAddServicio);
        }
        adaptador.notifyDataSetChanged();
    }

    // MULTI-SELECCION: Al crearse el menú para los días seleccionados.
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return true;
    }

    // MULTI-SELECCION: Al prepararse el menú para los días seleccionados.
    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    // MULTI-SELECCION: Al hacer click en un elemento del menú para los días seleccionados.
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        return true;
    }

    // MULTI-SELECCION: Al quitarse todos los días seleccionados.
    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        activarBoton(botonAddServicio);
        desactivarBoton(botonBorrarServicio);
        //Refrescar la lista.
        for (ServicioModel d : servicios){
            d.setSeleccionado(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    //endregion
    // ******************************************************************************************



    // ACTUALIZA LA LISTA
    private void actualizarLista() {
        servicios = datos.getServicios(linea);
        if (servicios == null) servicios = new ArrayList<>();
        adaptador = new AdaptadorServicio(this, servicios);
        listaServicios.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }


    private void botonAddServicioPulsado(View view){
        Intent intent = new Intent(context, EditarServicio.class);
        intent.putExtra("Id", -1);
        intent.putExtra("Linea", linea);
        startActivityForResult(intent, ACCION_EDITA_SERVICIO);
    }


    private void botonBorrarServicioPulsado(View view){
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle("ATENCION");
        aviso.setMessage("Vas a borrar los servicios seleccionados\n\n¿Estás seguro?");
        aviso.setPositiveButton("SI", (dialog, which) -> {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.addAll(listaIds);
            for (int id : ids) {
                ServicioModel servicioSeleccionado = servicios.get(id);
                listaServicios.setItemChecked(servicios.indexOf(servicioSeleccionado), false);
                datos.borrarServicio(servicioSeleccionado.getId());
            }
            actualizarLista();
        });
        aviso.setNegativeButton("NO", (dialog, which) -> {});
        aviso.show();
    }


    private void activarBoton(Button boton){
        boton.setEnabled(true);
        boton.setVisibility(View.VISIBLE);
    }


    private void desactivarBoton(Button boton){
        boton.setEnabled(false);
        boton.setVisibility(View.GONE);
    }


}
