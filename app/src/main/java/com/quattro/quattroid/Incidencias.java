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
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import BaseDatos.Incidencia;
import BaseDatos.BaseDatos;

public class Incidencias extends Activity implements AdapterView.OnItemClickListener{

    // CONSTANTES
    final static int ACCION_EDITA_INCIDENCIA = 1;

    // VARIABLES
    Context context = null;
    Cursor cursor = null;
    BaseDatos datos = null;
    AdaptadorIncidencia adaptador = null;
    Bundle datosIntent = null;
    boolean llamadaDesdeMenu = false;

    ListView listaIncidencias = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.incidencias);
        getActionBar().setTitle("Incidencias");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_incidencias);

        // Inicialización de los elementos
        context = this;
        listaIncidencias = (ListView) findViewById(R.id.lw_Incidencias);

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
        cursor = datos.cursorIncidencias();
        adaptador = new AdaptadorIncidencia(this, cursor);
        listaIncidencias.setAdapter(adaptador);

        // Establecemos el listener de item pulsado.
        listaIncidencias.setOnItemClickListener(this);
    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_incidencias, menu);
        return true;
    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.bt_nuevo:
                Intent intent = new Intent(context, EditarIncidencia.class);
                startActivityForResult(intent, ACCION_EDITA_INCIDENCIA);
                return true;
            case R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // CREAR MENU CONTEXTUAL
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contexto_incidencias, menu);
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
            case R.id.bt_edita:
                // Si es una de las incidencias protegidas (0 al 16) avisa y sale.
                if (elementoPulsado < 1){
                    Toast.makeText(this, getResources().getText(R.string.error_incidenciaProtegida),
                                   Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent = new Intent(context, EditarIncidencia.class);
                intent.putExtra("Codigo", elementoPulsado);
                startActivityForResult(intent, ACCION_EDITA_INCIDENCIA);
                return true;
            case R.id.bt_borra:
                // Si es una de las incidencias protegidas (0 al 16) avisa y sale.
                if (elementoPulsado < 17){
                    Toast.makeText(this, getResources().getText(R.string.error_incidenciaProtegida2),
                                   Toast.LENGTH_SHORT).show();
                    return true;
                }
                borrarIncidencia(elementoPulsado);
                actualizarCursor();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

        if (llamadaDesdeMenu){
            Cursor c = adaptador.getCursor();
            int cod = c.getInt(c.getColumnIndex("Codigo"));
            if (cod < 1){
                Toast.makeText(this, getResources().getText(R.string.error_incidenciaProtegida),
                               Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(context, EditarIncidencia.class);
            intent.putExtra("Codigo", cod);
            startActivityForResult(intent, ACCION_EDITA_INCIDENCIA);

        } else {
            // Extraemos los datos del cursor

            Cursor c = adaptador.getCursor();
            int cod = c.getInt(c.getColumnIndex("Codigo"));
            int tip = c.getInt(c.getColumnIndex("Tipo"));
            String inc = c.getString(c.getColumnIndex("Incidencia"));

            // Creamos un intent para devolver los datos de la incidencia
            Intent intent = new Intent(context, Incidencias.class);
            intent.putExtra("Codigo", cod);
            intent.putExtra("Tipo", tip);
            intent.putExtra("Incidencia", inc);
            setResult(RESULT_OK, intent);
            finish();
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Evaluamos el codigo de petición.
        switch (requestCode){
            case ACCION_EDITA_INCIDENCIA:
                if (resultCode == RESULT_OK){
                    actualizarCursor();
                }
                break;
            default:
                break;
        }
    }

    // ACTUALIZA LA LISTA
    private void actualizarCursor(){
        cursor = datos.cursorIncidencias();
        adaptador.changeCursor(cursor);
        adaptador.notifyDataSetChanged();
    }

    // BORRAR INCIDENCIA
    private void borrarIncidencia(int codigo){
        datos.borraIncidencia(codigo);
        Cursor c = datos.cursorIncidenciasEditables();
        if (c.getCount() == 0) return;
        int num = c.getCount();
        for (int i = 0; i < num; i++) {
            c.moveToNext();
            int cod = c.getInt(c.getColumnIndex("Codigo"));
            Incidencia inc = datos.getIncidencia(cod);
            if (inc.getCodigo() != 17 + i) {
                datos.borraIncidencia(inc.getCodigo());
                inc.setCodigo(17 + i);
                datos.setIncidencia(inc);
            }
        }
        c.close();
    }



}
