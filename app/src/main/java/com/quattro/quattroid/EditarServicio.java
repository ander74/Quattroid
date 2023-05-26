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
import android.os.Vibrator;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import BaseDatos.BaseDatos;
import BaseDatos.Servicio;
import BaseDatos.ServicioAuxiliar;
import Objetos.Calculos;
import Objetos.Colores;
import Objetos.Hora;

public class EditarServicio extends Activity implements AdapterView.OnItemClickListener,
                                                        View.OnFocusChangeListener,
                                                        View.OnLongClickListener {

    // CONSTANTES
    private static final int ACCION_EDITA_SERVICIO_AUXILIAR = 1;

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    Bundle datosIntent = null;
    Cursor cursor = null;
    AdaptadorServiciosAuxiliares adaptador = null;
    Servicio serv = null;
    String lineatext = "";
    int id = -1;
    Vibrator vibrador = null;
    //SharedPreferences opciones = null;


    // ELEMENTOS DEL VIEW
    TextView titulo = null;
    TextView complementarios = null;
    EditText servicio = null;
    EditText turno = null;
    EditText inicio = null;
    EditText fin = null;
    EditText lugarInicio = null;
    EditText lugarFinal = null;
    EditText tomaDeje = null;
    EditText euros = null;
    ListView listaServicios = null;

    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.servicios);
        getActionBar().setTitle("Servicios");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_editarservicio);

        // Inicialización de los elementos
        context = this;
        titulo = findViewById(R.id.titulo);
        complementarios = findViewById(R.id.tv_complementarios);
	    servicio = findViewById(R.id.et_servicio);
        turno = findViewById(R.id.et_turno);
        inicio = findViewById(R.id.et_inicio);
        fin = findViewById(R.id.et_final);
        lugarInicio = findViewById(R.id.et_lugarInicio);
        lugarFinal = findViewById(R.id.et_lugarFinal);
        tomaDeje = findViewById(R.id.et_tomaDeje);
        euros = findViewById(R.id.et_euros);
        listaServicios = findViewById(R.id.lw_listaServicios);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);
        //opciones = PreferenceManager.getDefaultSharedPreferences(this);

        // Definimos el vibrador.
        vibrador = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        // Activamos el teclado numérico en los campos habituales.
        if (datos.opciones.isActivarTecladoNumerico()){ //opciones.getBoolean("ActivarTecladoNumerico", false)){
            servicio.setInputType(InputType.TYPE_CLASS_PHONE);
        }


        // Registrar los listeners
        servicio.setOnFocusChangeListener(this);
        servicio.setOnLongClickListener(this);
        turno.setOnFocusChangeListener(this);
        inicio.setOnFocusChangeListener(this);
        fin.setOnFocusChangeListener(this);
        tomaDeje.setOnFocusChangeListener(this);
        euros.setOnFocusChangeListener(this);
        listaServicios.setOnItemClickListener(this);
        registerForContextMenu(listaServicios);

        // Recogemos los datos del intent
        datosIntent = getIntent().getExtras();
        lineatext = datosIntent.getString("Linea");
        if (lineatext == null) lineatext = "";
        id = datosIntent.getInt("Id", -1);

        // Definir si vamos a editar un servicio o crear una nuevo.
        if (id == -1){
            //SERVICIO NUEVO
            if (lineatext.equals("")) finish();
            serv = new Servicio();
            serv.setLinea(lineatext);
            servicio.setText("");
            turno.setText("");
            inicio.setText("");
            fin.setText("");
            lugarInicio.setText("");
            lugarFinal.setText("");
            tomaDeje.setText("");
            euros.setText("");
            euros.setNextFocusDownId(R.id.et_servicio);
        } else {
            //EDITAR SERVICIO
            serv = datos.getServicio(id);
            servicio.setText(serv.getServicio());
            turno.setText(String.valueOf(serv.getTurno()));
            inicio.setText(serv.getInicio());
            fin.setText(serv.getFinal());
            lugarInicio.setText(serv.getLugarInicio());
            lugarFinal.setText(serv.getLugarFinal());
            tomaDeje.setText(String.valueOf(serv.getTomaDeje()));
            if (serv.getEuros() == 0){
                euros.setText("");
            } else {
                euros.setText(Hora.textoDecimal(serv.getEuros()));
            }
            bloquearServicio();
        }
        // Ponemos el subtítulo de la ActionBar
        getActionBar().setSubtitle("Línea " + serv.getLinea());
        // Llenar la lista con los servicios auxiliares.
        cursor = datos.cursorServiciosAuxiliares(serv.getLinea(), serv.getServicio(), serv.getTurno());
        adaptador = new AdaptadorServiciosAuxiliares(context, cursor);
        listaServicios.setAdapter(adaptador);
        if (cursor.getCount() == 0){
            complementarios.setText("No hay servicios complementarios");
        } else {
            complementarios.setText("Servicios complementarios");
        }
    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editarservicio, menu);
        return true;
    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = null;
        int idd = item.getItemId();
        switch (idd){
            case R.id.bt_guardar:
                Guardar();
                finish();
                return true;
            case R.id.bt_nuevo:
                validarCampos();
                if (!serv.getServicio().equals("") && serv.getTurno() != 0){
                    intent = new Intent(context, EditarServiciosDia.class);
                    startActivityForResult(intent, ACCION_EDITA_SERVICIO_AUXILIAR);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // CREAR MENU CONTEXTUAL
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contexto_editarservicio, menu);
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
                Cursor c = adaptador.getCursor();
                datos.borraServicioAuxiliar(c.getInt(c.getColumnIndexOrThrow("_id")));
                actualizarCursor();
                return true;
            case R.id.bt_vaciar:
                validarCampos();
                datos.vaciarServiciosAuxiliares(serv.getLinea(), serv.getServicio(), serv.getTurno());
                actualizarCursor();
                return true;
            default:
                return super.onContextItemSelected(item);
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


    public boolean onLongClick(View v) {

        switch (v.getId()){
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

    // AL HACER CLIC EN UN ITEM
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Extraemos los datos del cursor
        Cursor c = adaptador.getCursor();

        // Creamos un intent para editar el servicio
        Intent intent = new Intent(context, EditarServiciosDia.class);
        intent.putExtra("Linea", c.getString(c.getColumnIndexOrThrow("LineaAuxiliar")));
        intent.putExtra("Servicio", c.getString(c.getColumnIndexOrThrow("ServicioAuxiliar")));
        intent.putExtra("Turno", c.getInt(c.getColumnIndexOrThrow("TurnoAuxiliar")));
        intent.putExtra("Inicio", c.getString(c.getColumnIndexOrThrow("Inicio")));
        intent.putExtra("Final", c.getString(c.getColumnIndexOrThrow("Final")));
        intent.putExtra("LugarInicio", c.getString(c.getColumnIndexOrThrow("LugarInicio")));
        intent.putExtra("LugarFinal", c.getString(c.getColumnIndexOrThrow("LugarFinal")));
        intent.putExtra("Id", c.getInt(c.getColumnIndexOrThrow("_id")));
        startActivityForResult(intent, ACCION_EDITA_SERVICIO_AUXILIAR);
    }

    // AL CAMBIAR EL FOCO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) return;
        switch (v.getId()){
            case R.id.et_servicio:
                servicio.setText(Hora.validarServicio(servicio.getText().toString()));
                break;
            case R.id.et_turno:
                String s = turno.getText().toString().trim();
                switch (s){
                    case "1":case "01":
                        turno.setText("1");
                        break;
                    case "2":case "02":
                        turno.setText("2");
                        break;
                    default:
                        turno.setText("");
                }
                break;
            case R.id.et_inicio:
                inicio.setText(Hora.horaToString(inicio.getText().toString()));
                break;
            case R.id.et_final:
                fin.setText(Hora.horaToString(fin.getText().toString()));
                break;
            case R.id.et_tomaDeje:
                tomaDeje.setText(Hora.horaToString(tomaDeje.getText().toString()));
                break;
            case R.id.et_euros:
                euros.setText(Hora.validaHoraDecimal(euros.getText().toString()));
                if (euros.getText().toString().equals("0,00")) euros.setText("");
                break;
            default:

        }
    }

    // AL VOLVER DE UNA SUBACTIVITY
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Evaluamos el codigo de petición.
        switch (requestCode){
            case ACCION_EDITA_SERVICIO_AUXILIAR:
                if (resultCode == RESULT_OK) {
                    // Recogemos los datos del intent
                    String l = data.getStringExtra("Linea");
                    String s = data.getStringExtra("Servicio");
                    int t = data.getIntExtra("Turno", 0);
                    String i = data.getStringExtra("Inicio");
                    String f = data.getStringExtra("Final");
                    String li = data.getStringExtra("LugarInicio");
                    String lf = data.getStringExtra("LugarFinal");
                    int idd = data.getIntExtra("Id", -1);
                    // Creamos el servicio a guardar y lo llenamos.
                    ServicioAuxiliar saux = new ServicioAuxiliar();
                    saux.setLinea(serv.getLinea());
                    saux.setServicio(serv.getServicio());
                    saux.setTurno(serv.getTurno());
                    saux.setLineaAuxiliar(l);
                    saux.setServicioAuxiliar(s);
                    saux.setTurnoAuxiliar(t);
                    saux.setInicio(i);
                    saux.setFinal(f);
                    saux.setLugarInicio(li);
                    saux.setLugarFinal(lf);
                    // Borramos el servicio editado, si existiera.
                    if (idd != -1) datos.borraServicioAuxiliar(idd);
                    // Guardamos el servicio auxiliar.
                    datos.setServicioAuxiliar(saux);
                    actualizarCursor();
                }
                break;
            default:
                break;
        }
    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // ACTUALIZAMOS LA LISTA
    private void actualizarCursor(){
        cursor = datos.cursorServiciosAuxiliares(serv.getLinea(), serv.getServicio(), serv.getTurno());
        adaptador.changeCursor(cursor);
        adaptador.notifyDataSetChanged();
        Calculos.setAlturaLista(listaServicios);
        if (cursor.getCount() == 0){
            complementarios.setText("No hay servicios complementarios");
            if (id == -1){
                desbloquearServicio();
            } else {
                bloquearServicio();
            }
        } else {
            complementarios.setText("Servicios complementarios");
            bloquearServicio();
        }
    }

    // BLOQUEAR EL SERVICIO Y EL TURNO
    private void bloquearServicio(){
        servicio.setTextColor(Colores.ROJO_OSCURO);
        servicio.setFocusable(false);
        turno.setTextColor(Colores.ROJO_OSCURO);
        turno.setFocusable(false);
        euros.setNextFocusDownId(R.id.et_inicio);
    }

    // DESBLOQUEAR EL SERVICIO Y EL TURNO
    private void desbloquearServicio(){
        servicio.setTextColor(Colores.NEGRO);
        servicio.setFocusable(true);
        turno.setTextColor(Colores.NEGRO);
        turno.setFocusable(true);
        euros.setNextFocusDownId(R.id.et_servicio);
    }

    // VALIDAR CAMPOS
    private void validarCampos(){
        servicio.setText(Hora.validarServicio(servicio.getText().toString()));
        serv.setServicio(servicio.getText().toString());
        inicio.setText(Hora.horaToString(inicio.getText().toString()));
        serv.setInicio(inicio.getText().toString());
        fin.setText(Hora.horaToString(fin.getText().toString()));
        serv.setFinal(fin.getText().toString());
        String s = turno.getText().toString().trim();
        switch (s){
            case "1":case "01":
                turno.setText("1");
                serv.setTurno(1);
                break;
            case "2":case "02":
                turno.setText("2");
                serv.setTurno(2);
                break;
            default:
                turno.setText("");
                serv.setTurno(0);
        }
        serv.setLugarInicio(lugarInicio.getText().toString().trim());
        serv.setLugarFinal(lugarFinal.getText().toString().trim());

        s = Hora.horaToString(tomaDeje.getText().toString().trim());
        if (s.equals("")){
            serv.setTomaDeje("");
        } else {
            serv.setTomaDeje(s);
        }

        s = Hora.validaHoraDecimal(euros.getText().toString().trim());
        if (s.equals("")){
            serv.setEuros(0d);
        } else {
            serv.setEuros(Double.valueOf(s.replace(",", ".")));
        }

    }


    private void Guardar(){
        validarCampos();
        if (!serv.getServicio().equals("") && serv.getTurno() != 0){
            datos.setServicio(id, serv);
        }
        setResult(RESULT_OK);
    }

}
