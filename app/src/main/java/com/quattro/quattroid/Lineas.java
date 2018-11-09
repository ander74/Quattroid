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
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import BaseDatos.BaseDatos;

public class Lineas extends Activity implements AdapterView.OnItemClickListener {

    // CONSTANTES
    final static int ACCION_EDITA_LINEA = 1;

    // VARIABLES
    Context context = null;
    Cursor cursor = null;
    BaseDatos datos = null;
    AdaptadorLinea adaptador = null;

    ListView listaLineas = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.servicios);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("Lineas");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_lineas);

        // Inicialización de los elementos
        context = this;
        listaLineas = (ListView) findViewById(R.id.lw_lineas);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);

        // Registrar el menu contextual
        registerForContextMenu(listaLineas);

        // Llenado de la lista
        cursor = datos.cursorLineas();
        adaptador = new AdaptadorLinea(this, cursor);
        listaLineas.setAdapter(adaptador);

        // Establecemos el listener de item pulsado.
        listaLineas.setOnItemClickListener(this);
    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lineas, menu);
        return true;
    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.bt_nuevo:
                intent = new Intent(context, EditarLinea.class);
                intent.putExtra("Id", (int) -1);
                startActivityForResult(intent, ACCION_EDITA_LINEA);
                return true;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                intent = new Intent(this, Principal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // CREAR MENU CONTEXTUAL
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contexto_lineas, menu);
    }

    // AL PULSAR UNA OPCION DEL MENU CONTEXTUAL
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Guardar el elemento que ha provocado el menu contextual
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int elementoPulsado = acmi.position;

        Cursor c = null;
        // Determinar la opción pulsada
        switch (item.getItemId()) {
            case R.id.bt_editar:
                Intent intent = new Intent(context, EditarLinea.class);
                c = adaptador.getCursor();
                intent.putExtra("Id", c.getInt(c.getColumnIndex("_id")));
                startActivityForResult(intent, ACCION_EDITA_LINEA);
                return true;
            case R.id.bt_borrar:
                c = adaptador.getCursor();
                datos.borrarLinea(c.getString(c.getColumnIndex("Linea")));
                actualizarCursor();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        // Extraemos los datos del cursor
        Cursor c = adaptador.getCursor();

        // Creamos un intent para enviar la línea pulsada.
        Intent intent = new Intent(context, Servicios.class);
        intent.putExtra("Linea", c.getString(c.getColumnIndex("Linea")));
        startActivity(intent);

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
            case ACCION_EDITA_LINEA:
                if (resultCode == RESULT_OK) {
                    actualizarCursor();
                }
                break;
            default:
                break;
        }
    }

    // ACTUALIZA LA LISTA
    private void actualizarCursor() {
        cursor = datos.cursorLineas();
        adaptador.changeCursor(cursor);
        adaptador.notifyDataSetChanged();
    }

}
