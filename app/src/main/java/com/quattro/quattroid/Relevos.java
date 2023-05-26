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

public class Relevos extends Activity implements AdapterView.OnItemClickListener {

    // CONSTANTES
    final static int ACCION_EDITA_RELEVO = 1;

    // VARIABLES
    Context context;
    Cursor cursor = null;
    AdaptadorRelevos adaptador = null;
    BaseDatos datos = null;
    int orden = BaseDatos.RELEVOS_POR_MATRICULA;
    Bundle datosIntent = null;
    boolean llamadaDesdeMenu = false;

    // ELEMENTOS DEL VIEW
    ListView listaRelevos = null;


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

        datos = new BaseDatos(context);
        cursor = datos.cursorRelevos(orden);
        adaptador = new AdaptadorRelevos(context, cursor);
        listaRelevos.setAdapter(adaptador);

        // Recogemos el intent, si existe
        datosIntent = getIntent().getExtras();
        if (datosIntent != null) {
            llamadaDesdeMenu = getIntent().getExtras().getBoolean("Menu", false);
        }

        // Registrar los listeners
        listaRelevos.setOnItemClickListener(this);
        registerForContextMenu(listaRelevos);

    }

    // AL CREAR EL MENU SUPERIOR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_relevos, menu);
        return true;
    }

    // AL PULSAR EN EL MENU SUPERIOR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.bt_nuevo:
                // Crear nuevo relevo.
                intent = new Intent(this, EditarRelevo.class);
                startActivityForResult(intent, ACCION_EDITA_RELEVO);
                return true;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // CREAR MENU CONTEXTUAL
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contexto_relevos, menu);
    }

    // AL PULSAR UNA OPCION DEL MENU CONTEXTUAL
    @Override
    public boolean onContextItemSelected(MenuItem item){

        // Guardar el elemento que ha provocado el menu contextual
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int elementoPulsado = acmi.position;

        // Determinar la opción pulsada
        switch (item.getItemId()){
            case R.id.bt_porMatricula:
                orden = BaseDatos.RELEVOS_POR_MATRICULA;
                actualizarCursor();
                return true;
            case R.id.bt_porNombre:
                orden = BaseDatos.RELEVOS_POR_NOMRE;
                actualizarCursor();
                return true;
            case R.id.bt_porApellidos:
                orden = BaseDatos.RELEVOS_POR_APELLIDOS;
                actualizarCursor();
                return true;
            case R.id.bt_borrar:
                Cursor c = adaptador.getCursor();
                datos.borrarRelevo(c.getInt(c.getColumnIndexOrThrow("Matricula")));
                actualizarCursor();
                return true;
            case R.id.bt_relevoFijo:
                c = adaptador.getCursor();
                datos.opciones.setRelevoFijo(c.getInt(c.getColumnIndexOrThrow("Matricula")));
                datos.guardarOpciones();
            default:
                return super.onContextItemSelected(item);
        }
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

        // Extraemos los datos del cursor
        Cursor c = adaptador.getCursor();

        if (llamadaDesdeMenu) {
            // Creamos un intent para devolver los datos de la incidencia
            Intent intent = new Intent(context, EditarRelevo.class);
            intent.putExtra("Matricula", c.getInt(c.getColumnIndexOrThrow("Matricula")));
            intent.putExtra("Deuda", c.getInt(c.getColumnIndexOrThrow("Deuda")));
            intent.putExtra("Calificacion", c.getInt(c.getColumnIndexOrThrow("Calificacion")));
            intent.putExtra("Nombre", c.getString(c.getColumnIndexOrThrow("Nombre")));
            intent.putExtra("Apellidos", c.getString(c.getColumnIndexOrThrow("Apellidos")));
            intent.putExtra("Telefono", c.getString(c.getColumnIndexOrThrow("Telefono")));
            intent.putExtra("Notas", c.getString(c.getColumnIndexOrThrow("Notas")));
            startActivityForResult(intent, ACCION_EDITA_RELEVO);
        } else {
            Intent intent = new Intent(context, Relevos.class);
            intent.putExtra("Matricula", c.getInt(c.getColumnIndexOrThrow("Matricula")));
            intent.putExtra("Apellidos", c.getString(c.getColumnIndexOrThrow("Apellidos")));
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
                    actualizarCursor();
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

    // ACTUALIZA LA LISTA
    private void actualizarCursor(){
        cursor = datos.cursorRelevos(orden);
        adaptador.changeCursor(cursor);
        adaptador.notifyDataSetChanged();
    }

}
