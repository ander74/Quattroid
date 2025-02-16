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
import android.net.Uri;
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

import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;

import BaseDatos.BaseDatos;
import BaseDatos.Relevo;
import Objetos.Colores;

public class Relevos extends Activity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener{

    // CONSTANTES
    final static int ACCION_EDITA_RELEVO = 1;
    private final ArrayList<Integer> listaIds = new ArrayList<>();

    // VARIABLES
    Context context;
    ArrayList<Relevo> relevos = null;
    AdaptadorRelevos adaptador = null;
    BaseDatos datos = null;
    int orden = BaseDatos.RELEVOS_POR_MATRICULA;
    int ordenRelevos = BaseDatos.RELEVOS_POR_MATRICULA;
    Bundle datosIntent = null;
    boolean llamadaDesdeMenu = false;


    // ELEMENTOS DEL VIEW
    ListView listaRelevos = null;
    Button botonAddRelevo = null;
    Button botonOrdenar = null;
    Button botonBorrarRelevo = null;
    Button botonRelevoFijo = null;
    Button botonLlamar = null;


    //******************************************************************************************
    //region Métodos oveeride

    // AL CREAR LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.usuario);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("Compañeros/as");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_relevos);
        context = this;

        // Instanciar los elementos
        listaRelevos = findViewById(R.id.lw_relevos);
        botonAddRelevo = findViewById(R.id.bt_barra_addRelevo);
        botonOrdenar = findViewById(R.id.bt_barra_ordenar);
        botonBorrarRelevo = findViewById(R.id.bt_barra_borrar_relevo);
        botonRelevoFijo = findViewById(R.id.bt_barra_relevo_fijo);
        botonLlamar = findViewById(R.id.bt_barra_llamar);

        // Poner el nombre del botón ordenar
        botonOrdenar.setText("Matrícula");

        // Instanciar la base de datos
        datos = new BaseDatos(context);

        // Llenar la lista de los relevos
        relevos = datos.getRelevos(ordenRelevos);
        adaptador = new AdaptadorRelevos(context, relevos);
        listaRelevos.setAdapter(adaptador);

        // Recogemos el intent, si existe
        datosIntent = getIntent().getExtras();
        if (datosIntent != null) {
            llamadaDesdeMenu = getIntent().getExtras().getBoolean("Menu", false);
        }

        // Registrar los listeners
        listaRelevos.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaRelevos.setDivider(null);
        listaRelevos.setDividerHeight(0);
        listaRelevos.setMultiChoiceModeListener(this);// MULTI-SELECCION
        listaRelevos.setOnItemClickListener(this);
        registerForContextMenu(listaRelevos);
        botonAddRelevo.setOnClickListener(this::botonAddRelevoPulsado);
        botonOrdenar.setOnClickListener(this::botonOrdenarPulsado);
        botonBorrarRelevo.setOnClickListener(this::botonBorrarRelevoPulsado);
        botonRelevoFijo.setOnClickListener(this::botonRelevoFijoPulsado);
        botonLlamar.setOnClickListener(this::botonLlamarPulsado);

    }

    // AL PULSAR EN EL MENU SUPERIOR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

        // Extraemos el relevo pulsado
        Relevo relevoPulsado = relevos.get(position);

        if (llamadaDesdeMenu) {
            // Creamos un intent para devolver los datos de la incidencia
            Intent intent = new Intent(context, EditarRelevo.class);
            intent.putExtra("Matricula", relevoPulsado.getMatricula());
            intent.putExtra("Deuda", relevoPulsado.getDeuda());
            intent.putExtra("Calificacion", relevoPulsado.getCalificacion());
            intent.putExtra("Nombre", relevoPulsado.getNombre());
            intent.putExtra("Apellidos", relevoPulsado.getApellidos());
            intent.putExtra("Telefono", relevoPulsado.getTelefono());
            intent.putExtra("Notas", relevoPulsado.getNotas());
            startActivityForResult(intent, ACCION_EDITA_RELEVO);
        } else {
            Intent intent = new Intent(context, Relevos.class);
            intent.putExtra("Matricula", relevoPulsado.getMatricula());
            intent.putExtra("Apellidos", relevoPulsado.getApellidos());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // AL VOLVER DE UNA PANTALLA
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Evaluamos el codigo de petición.
        switch (requestCode){
            case ACCION_EDITA_RELEVO:
                if (resultCode == RESULT_OK){
                    actualizarLista(true);
                }
                break;
            default:
                break;
        }
    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
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

    //endregion
    // ******************************************************************************************


    //******************************************************************************************
    //region Multi selección

    // MULTI-SELECCION: Al seleccionar un día del calendario.
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked){
        int checkedCount = listaRelevos.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selec.");

        if (checked){
            listaIds.add(position);
            relevos.get(position).setSeleccionado(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            relevos.get(position).setSeleccionado(false);
        }

        if (checkedCount > 0){
            activarBoton(botonBorrarRelevo);
            desactivarBoton(botonAddRelevo);
            desactivarBoton(botonOrdenar);
        } else {
            desactivarBoton(botonBorrarRelevo);
            activarBoton(botonAddRelevo);
            activarBoton(botonOrdenar);
        }

        if (checkedCount == 1){
            activarBoton(botonRelevoFijo);
            Relevo relevoSeleccionado = relevos.get(listaIds.get(0));
            if (!relevoSeleccionado.getTelefono().isEmpty()) {
                activarBoton(botonLlamar);
            }
        } else {
            desactivarBoton(botonRelevoFijo);
            desactivarBoton(botonLlamar);
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
        activarBoton(botonAddRelevo);
        activarBoton(botonOrdenar);
        desactivarBoton(botonBorrarRelevo);
        desactivarBoton(botonRelevoFijo);
        desactivarBoton(botonLlamar);
        //Refrescar la lista.
        for (Relevo d : relevos){
            d.setSeleccionado(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    //endregion
    // ******************************************************************************************


    //******************************************************************************************
    //region Métodos privados

    // ACTUALIZA LA LISTA
    private void actualizarLista(boolean completo){
        if (completo){
            relevos = datos.getRelevos(ordenRelevos);
            adaptador = new AdaptadorRelevos(this, relevos);
            listaRelevos.setAdapter(adaptador);
        }
        adaptador.notifyDataSetChanged();
    }

    // Al pulsar el botón de añadir relevo
    private void botonAddRelevoPulsado(View view){
        Intent intent = new Intent(this, EditarRelevo.class);
        startActivityForResult(intent, ACCION_EDITA_RELEVO);
    }

    // Al pulsar el botón de ordenar
    private void botonOrdenarPulsado(View view){
        ordenRelevos++;
        if (ordenRelevos == 7) ordenRelevos = 1;
        ponerTextoBotonOrdenar();
        orden = ordenRelevos;
        actualizarLista(true);
    }

    // Al pulsar el botón de borrar relevo
    private void botonBorrarRelevoPulsado(View view){
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle("ATENCION");
        aviso.setMessage("Vas a borrar a los compañeros seleccionados\n\n¿Estás seguro?");
        aviso.setPositiveButton("SI", (dialog, which) -> {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.addAll(listaIds);
            for (int id : ids) {
                Relevo relevoSeleccionado = relevos.get(id);
                listaRelevos.setItemChecked(relevos.indexOf(relevoSeleccionado), false);
                datos.borrarRelevo(relevoSeleccionado.getMatricula());
            }
            actualizarLista(true);        });
        aviso.setNegativeButton("NO", (dialog, which) -> {});
        aviso.show();
    }

    // Al pulsar el botón de relevo fijo
    private void botonRelevoFijoPulsado(View view){
        Relevo relevoSeleccionado = relevos.get(listaIds.get(0));
        datos.opciones.setRelevoFijo(relevoSeleccionado.getMatricula());
        datos.guardarOpciones();
    }

    // Poner el texto del boton ordenar.
    private void ponerTextoBotonOrdenar(){
        switch (ordenRelevos){
            case BaseDatos.RELEVOS_POR_MATRICULA:
            case BaseDatos.RELEVOS_POR_MATRICULA_DESC:
                botonOrdenar.setText("Matrícula");
                break;
            case BaseDatos.RELEVOS_POR_NOMBRE:
            case BaseDatos.RELEVOS_POR_NOMBRE_DESC:
                botonOrdenar.setText("Nombre");
                break;
            case BaseDatos.RELEVOS_POR_APELLIDOS:
            case BaseDatos.RELEVOS_POR_APELLIDOS_DESC:
                botonOrdenar.setText("Apellidos");
        }
    }

    private void botonLlamarPulsado(View view){
        Relevo relevoSeleccionado = relevos.get(listaIds.get(0));
        String telefono = relevoSeleccionado.getTelefono();
        String uri = "tel:" + telefono ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }


    private void activarBoton(Button boton){
        boton.setEnabled(true);
        boton.setTextColor(0XFF000099);
        DrawableCompat.setTint(boton.getCompoundDrawables()[1], 0XFF000099);
    }

    private void desactivarBoton(Button boton){
        boton.setEnabled(false);
        boton.setTextColor(Colores.GRIS_OSCURO);
        DrawableCompat.setTint(boton.getCompoundDrawables()[1], Colores.GRIS_OSCURO);
    }



    //endregion
    //******************************************************************************************
}
