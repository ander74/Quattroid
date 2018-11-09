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
import android.widget.Toast;

import BaseDatos.BaseDatos;
import BaseDatos.HoraAjena;

public class HorasAjenas extends Activity implements AdapterView.OnItemClickListener {

    // CONSTANTES
    final static int ACCION_EDITA_AJENA = 1;
    final static int ACCION_NUEVA_AJENA = 2;

    // VARIABLES
    Context context;
    Cursor cursor = null;
    AdaptadorHorasAjenas adaptador = null;
    BaseDatos datos = null;

    // ELEMENTOS DEL VIEW
    ListView listaAjenas = null;


    // AL CREAR LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.ajenas);
        getActionBar().setTitle("Horas Ajenas");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_horasajenas);
        context = this;

        // Instanciar los elementos
        listaAjenas = (ListView) findViewById(R.id.lw_ajenas);

        datos = new BaseDatos(context);
        cursor = datos.cursorAjenas();
        adaptador = new AdaptadorHorasAjenas(context, cursor);
        listaAjenas.setAdapter(adaptador);

        // Registrar los listeners
        listaAjenas.setOnItemClickListener(this);
        registerForContextMenu(listaAjenas);

    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ajenas, menu);
        return true;
    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.bt_nuevo:
                intent = new Intent(context, EditarHorasAjenas.class);
                intent.putExtra("Id", (int) -1);
                startActivityForResult(intent, ACCION_NUEVA_AJENA);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contexto_horasajenas, menu);
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
            case R.id.bt_borrar:
                // Extraemos los datos del cursor
                Cursor c = adaptador.getCursor();
                int i = c.getInt(c.getColumnIndex("_id"));
                int d = c.getInt(c.getColumnIndex("Dia"));
                int m = c.getInt(c.getColumnIndex("Mes"));
                int a = c.getInt(c.getColumnIndex("Año"));
                double h = c.getDouble(c.getColumnIndex("Horas"));
                String mot = c.getString(c.getColumnIndex("Motivo"));
                HoraAjena horaAjena = new HoraAjena();
                horaAjena.setDia(d);
                horaAjena.setMes(m);
                horaAjena.setAño(a);
                horaAjena.setHoras(h);
                horaAjena.setMotivo(mot);
                if (datos.borrarAjena(i)){
                    Toast.makeText(context, R.string.mensaje_ajenaBorrada, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.error_ajenaBorrada, Toast.LENGTH_SHORT).show();
                }
                actualizarCursor();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

        // Extraemos los datos del cursor
        Cursor c = adaptador.getCursor();
        int i = c.getInt(c.getColumnIndex("_id"));
        int d = c.getInt(c.getColumnIndex("Dia"));
        int m = c.getInt(c.getColumnIndex("Mes"));
        int a = c.getInt(c.getColumnIndex("Año"));
        double h = c.getDouble(c.getColumnIndex("Horas"));
        String mot = c.getString(c.getColumnIndex("Motivo"));

        // Creamos un intent para devolver los datos de la incidencia
        Intent intent = new Intent(context, EditarHorasAjenas.class);
        intent.putExtra("Id", i);
        intent.putExtra("Dia", d);
        intent.putExtra("Mes", m);
        intent.putExtra("Año", a);
        intent.putExtra("Horas", h);
        intent.putExtra("Motivo", mot);
        intent.putExtra("Nuevo", false);
        startActivityForResult(intent, ACCION_EDITA_AJENA);

    }

    // AL VOLVER DE UNA PANTALLA
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Evaluamos el codigo de petición.
        switch (requestCode){
            case ACCION_EDITA_AJENA:
                if (resultCode == RESULT_OK){
                    HoraAjena horaAjena = new HoraAjena();
                    horaAjena.setDia(data.getIntExtra("Dia", 0));
                    horaAjena.setMes(data.getIntExtra("Mes", 0));
                    horaAjena.setAño(data.getIntExtra("Año", 0));
                    horaAjena.setHoras(data.getDoubleExtra("Horas", 0));
                    horaAjena.setMotivo(data.getStringExtra("Motivo"));
                    if (data.getBooleanExtra("Editar", false)){
                        datos.borrarAjena(data.getIntExtra("Id", -1));
                    }
                    datos.setAjena(horaAjena);
                    actualizarCursor();
                }
                break;
            case ACCION_NUEVA_AJENA:
                if (resultCode == RESULT_OK) {
                    HoraAjena horaAjena = new HoraAjena();
                    horaAjena.setDia(data.getIntExtra("Dia", 0));
                    horaAjena.setMes(data.getIntExtra("Mes", 0));
                    horaAjena.setAño(data.getIntExtra("Año", 0));
                    horaAjena.setHoras(data.getDoubleExtra("Horas", 0));
                    horaAjena.setMotivo(data.getStringExtra("Motivo"));
                    datos.setAjena(horaAjena);
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
        cursor = datos.cursorAjenas();
        adaptador.changeCursor(cursor);
        adaptador.notifyDataSetChanged();
    }

}
