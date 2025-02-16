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
import android.database.Cursor;
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
import android.widget.Toast;

import java.util.ArrayList;

import BaseDatos.BaseDatos;
import BaseDatos.Incidencia;

public class Incidencias extends Activity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    // CONSTANTES
    final static int ACCION_EDITA_INCIDENCIA = 1;
    private final ArrayList<Integer> listaIds = new ArrayList<>();

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    AdaptadorIncidencia adaptador = null;
    ArrayList<Incidencia> incidencias = null;
    Bundle datosIntent = null;
    boolean llamadaDesdeMenu = false;

    ListView listaIncidencias = null;
    Button botonAddIncidencia = null;
    Button botonBorrarIncidencia = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.incidencias);
        getActionBar().setTitle("Incidencias");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_incidencias);

        // Inicialización de los elementos
        context = this;
        listaIncidencias = findViewById(R.id.lw_Incidencias);
        botonAddIncidencia = findViewById(R.id.bt_addIncidencia);
        botonBorrarIncidencia = findViewById(R.id.bt_borrarIncidencia);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);

        // Recogemos el intent, si existe
        datosIntent = getIntent().getExtras();
        if (datosIntent != null) {
            llamadaDesdeMenu = getIntent().getExtras().getBoolean("Menu", false);
        }

        // Registrar el menu contextual
        registerForContextMenu(listaIncidencias);

        // Llenado de la lista
        incidencias = datos.getIncidencias();
        adaptador = new AdaptadorIncidencia(this, incidencias);
        listaIncidencias.setAdapter(adaptador);

        // Si se ha llamado desde un menú, anulamos los botones.
        if (!llamadaDesdeMenu) botonAddIncidencia.setVisibility(View.GONE);

        // Establecemos el listener de item pulsado.
        listaIncidencias.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaIncidencias.setDivider(null);
        listaIncidencias.setDividerHeight(0);
        listaIncidencias.setMultiChoiceModeListener(this);// MULTI-SELECCION
        listaIncidencias.setOnItemClickListener(this);
        botonAddIncidencia.setOnClickListener(this::botonAddIncidenciaPulsado);
        botonBorrarIncidencia.setOnClickListener(this::botonBorrarIncidenciaPulsado);
    }

    // CREAR EL MENÚ SUPERIOR.
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_incidencias, menu);
//        return true;
//    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // CREAR MENU CONTEXTUAL
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getMenuInflater().inflate(R.menu.contexto_incidencias, menu);
//    }

    // AL PULSAR UNA OPCION DEL MENU CONTEXTUAL
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        // Guardar el elemento que ha provocado el menu contextual
//        AdapterView.AdapterContextMenuInfo acmi =
//                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int elementoPulsado = acmi.position;
//        // Determinar la opción pulsada
//        switch (item.getItemId()) {
//            case R.id.bt_edita:
//                // Si es una de las incidencias protegidas (0 al 16) avisa y sale.
//                if (elementoPulsado < 1) {
//                    Toast.makeText(this, getResources().getText(R.string.error_incidenciaProtegida),
//                            Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//                Intent intent = new Intent(context, EditarIncidencia.class);
//                intent.putExtra("Codigo", elementoPulsado);
//                startActivityForResult(intent, ACCION_EDITA_INCIDENCIA);
//                return true;
//            case R.id.bt_borra:
//                // Si es una de las incidencias protegidas (0 al 16) avisa y sale.
//                if (elementoPulsado < 17) {
//                    Toast.makeText(this, getResources().getText(R.string.error_incidenciaProtegida2),
//                            Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//                borrarIncidencia(elementoPulsado);
//                actualizarCursor();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Incidencia incidenciaPulsada = incidencias.get(position);
        if (llamadaDesdeMenu) {
            if (incidenciaPulsada.getCodigo() < 1) {
                Toast.makeText(this, getResources().getText(R.string.error_incidenciaProtegida),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(context, EditarIncidencia.class);
            intent.putExtra("Codigo", incidenciaPulsada.getCodigo());
            startActivityForResult(intent, ACCION_EDITA_INCIDENCIA);

        } else {
            // Creamos un intent para devolver los datos de la incidencia
            Intent intent = new Intent(context, Incidencias.class);
            intent.putExtra("Codigo", incidenciaPulsada.getCodigo());
            intent.putExtra("Tipo", incidenciaPulsada.getTipo());
            intent.putExtra("Incidencia", incidenciaPulsada.getTexto());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
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
            case ACCION_EDITA_INCIDENCIA:
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
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        int checkedCount = listaIncidencias.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selec.");


        if (checked) {
            listaIds.add(position);
            incidencias.get(position).setSeleccionada(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            incidencias.get(position).setSeleccionada(false);
        }

        if (llamadaDesdeMenu) {

            if (checkedCount > 0) {
                activarBoton(botonBorrarIncidencia);
                desactivarBoton(botonAddIncidencia);
            } else {
                activarBoton(botonAddIncidencia);
                desactivarBoton(botonBorrarIncidencia);
            }
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
        if (llamadaDesdeMenu) {
            activarBoton(botonAddIncidencia);
            desactivarBoton(botonBorrarIncidencia);
        }
        //Refrescar la lista.
        for (Incidencia d : incidencias) {
            d.setSeleccionada(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    //endregion
    // ******************************************************************************************


    // ACTUALIZA LA LISTA
    private void actualizarLista() {
        incidencias = datos.getIncidencias();
        adaptador = new AdaptadorIncidencia(this, incidencias);
        listaIncidencias.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }

    // BORRAR INCIDENCIA
    private void borrarIncidencia(int codigo) {
        datos.borraIncidencia(codigo);
        Cursor c = datos.cursorIncidenciasEditables();
        if (c.getCount() == 0) return;
        int num = c.getCount();
        for (int i = 0; i < num; i++) {
            c.moveToNext();
            int cod = c.getInt(c.getColumnIndexOrThrow("Codigo"));
            Incidencia inc = datos.getIncidencia(cod);
            if (inc.getCodigo() != 17 + i) {
                datos.borraIncidencia(inc.getCodigo());
                inc.setCodigo(17 + i);
                datos.setIncidencia(inc);
            }
        }
        c.close();
    }

    private void botonAddIncidenciaPulsado(View view) {
        Intent intent = new Intent(context, EditarIncidencia.class);
        startActivityForResult(intent, ACCION_EDITA_INCIDENCIA);
    }

    private void botonBorrarIncidenciaPulsado(View view) {
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle("ATENCION");
        aviso.setMessage("Vas a borrar las incidencias seleccionadas\n\n¿Estás seguro?\n\n(Las incidencias protegidas no se borrarán)");
        aviso.setPositiveButton("SI", (dialog, which) -> {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.addAll(listaIds);
            for (int id : ids) {
                Incidencia incidenciaSeleccionada = incidencias.get(id);
                listaIncidencias.setItemChecked(incidencias.indexOf(incidenciaSeleccionada), false);
                if (incidenciaSeleccionada.getCodigo() > 16) {
                    datos.borraIncidencia(incidenciaSeleccionada.getCodigo());
                }
            }
            actualizarLista();
        });
        aviso.setNegativeButton("NO", (dialog, which) -> {
        });
        aviso.show();
    }


    private void activarBoton(Button boton) {
        boton.setEnabled(true);
        boton.setVisibility(View.VISIBLE);
    }


    private void desactivarBoton(Button boton) {
        boton.setEnabled(false);
        boton.setVisibility(View.GONE);
    }

}
