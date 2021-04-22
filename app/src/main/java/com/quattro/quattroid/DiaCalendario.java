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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;

import BaseDatos.Linea;
import BaseDatos.Servicio;
import BaseDatos.ServicioDia;
import BaseDatos.Relevo;
import BaseDatos.BaseDatos;
import BaseDatos.DatosDia;
import Objetos.Calculos;
import Objetos.Colores;
import Objetos.EstadoDia;
import Objetos.Hora;
import Objetos.HorasServicio;


public class DiaCalendario extends Activity implements View.OnFocusChangeListener,
                                                       AdapterView.OnItemClickListener,
                                                       View.OnLongClickListener,
                                                       View.OnClickListener {

	//region DECLARACIONES INICIALES
	
    // CONSTANTES
    static final int ACCION_LISTA_INCIDENCIA = 1;
    static final int ACCION_SERVICIOS_DIA = 2;
    static final int ACCION_LISTA_RELEVOS = 3;
    static final int ACCION_LISTA_SUSTI = 4;

    static final int TRABAJADAS = 1;
    static final int ACUMULADAS = 2;
    static final int NOCTURNAS = 3;

    // VARIABLES
    Context context;
    SharedPreferences opciones = null;
    BaseDatos datos = null;
    DatosDia datosDia = null;
    Cursor cursor = null;
    AdaptadorServiciosDia adaptador = null;
    Vibrator vibrador = null;
    int horaSeleccionada = 0;

    boolean noCambiar = false;

    int diaActual;
    int mesActual;
    int añoActual;
    int diaSemanaActual = 0;
    Calendar fecha;
    int posicion = 0;

    // ELEMENTOS DEL VIEW
    LinearLayout bloqueSusti = null;
    LinearLayout bloqueHuelga = null;
    LinearLayout bloqueFocus = null;

    TextView inputIncidencia = null;
    EditText inputMatriculaSusti = null;
    EditText inputApellidosSusti = null;
    EditText inputHuelga = null;
    EditText inputLinea = null;
    EditText inputServicio = null;
    EditText inputTurno = null;
    EditText inputTextoLinea = null;
    EditText inputInicio = null;
    EditText inputFinal = null;
    EditText inputBus = null;
    EditText inputMatriculaRelevo = null;
    EditText inputApellidosRelevo = null;
    EditText inputNotas = null;
    EditText inputLugarInicio = null;
    EditText inputLugarFinal = null;
    EditText inputTomaDeje = null;
    EditText inputEuros = null;

    CheckBox checkHuelga = null;

    TextView cabeceraComplementarios = null;

    String cadenaMatriculaSusti = "";
    String cadenaApellidosSusti = "";
    String cadenaLinea = "";
    String cadenaServicio = "";
    String cadenaTurno = "";
    String cadenaTextoLinea = "";
    String cadenaInicio = "";
    String cadenaFinal = "";
    String cadenaMatriculaRelevo = "";
    String cadenaApellidosRelevo = "";
    String cadenaTomaDeje = "";
    String cadenaEuros = "";
    String cadenaHorasHuelga = "";

    TextView textoTrabajadas = null;
    TextView textoAcumuladas = null;
    TextView textoNocturnas = null;

    ListView listaServiciosDia = null;

    ImageView iconoDesayuno = null;
    ImageView iconoComida = null;
    ImageView iconoCena = null;

    //endregion

    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.calendario);
	    // Instanciar las opciones
	    opciones = PreferenceManager.getDefaultSharedPreferences(this);
	    if (opciones.getBoolean("ModoBasico", false)) {
		    setContentView(R.layout.activity_diacalendario_basico);
	    } else {
		    setContentView(R.layout.activity_diacalendario);
	    }
	    
        // Instanciamos los elementos de la activity
        context = this;
        datosDia = new DatosDia();

        // Instanciar los elementos del view
        instanciarElementos();

        // Instanciar las opciones y la base de datos.
        //opciones = PreferenceManager.getDefaultSharedPreferences(this);
        datos = new BaseDatos(this);

        // Definimos el vibrador.
        vibrador = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        // Accedemos a los datos del intent llamador
        diaActual = getIntent().getExtras().getInt("Dia");
        mesActual = getIntent().getExtras().getInt("Mes");
        añoActual = getIntent().getExtras().getInt("Año");
        posicion = getIntent().getExtras().getInt("Posicion");

        // Calculamos el día de la semana
        fecha = Calendar.getInstance();
        fecha.set(añoActual, mesActual - 1, diaActual);
        diaSemanaActual = fecha.get(Calendar.DAY_OF_WEEK);

        // Llenamos el DatosDia con los datos.
        datosDia = datos.servicioDia(diaActual, mesActual, añoActual);

        // Si la incidencia es cero, ponemos trabajo.
        if (datosDia.getCodigoIncidencia() == 0) {
            datosDia.setCodigoIncidencia(1);
            datosDia.setTextoIncidencia("Trabajo");
            datosDia.setTipoIncidencia(1);
        }

        // Llenar el cursor con los servicios del día.
        cursor = datos.cursorServiciosDia(diaActual, mesActual, añoActual);

        // Crear y vincular el adaptador a la lista.
        adaptador = new AdaptadorServiciosDia(context, cursor);
        listaServiciosDia.setAdapter(adaptador);
        Calculos.setAlturaLista(listaServiciosDia);

        // Registramos los listeners
        listaServiciosDia.setOnItemClickListener(this);
        registerForContextMenu(listaServiciosDia);
        inputIncidencia.setOnLongClickListener(this);
//        textoTrabajadas.setOnLongClickListener(this);
//        textoAcumuladas.setOnLongClickListener(this);
//        textoNocturnas.setOnLongClickListener(this);
        textoTrabajadas.setOnClickListener(this);
        textoAcumuladas.setOnClickListener(this);
        textoNocturnas.setOnClickListener(this);
        inputMatriculaRelevo.setOnLongClickListener(this);
        inputMatriculaSusti.setOnLongClickListener(this);
        inputServicio.setOnLongClickListener(this);
        inputLinea.setOnLongClickListener(this);
        inputBus.setOnLongClickListener(this);
        iconoDesayuno.setOnClickListener(this);
        iconoComida.setOnClickListener(this);
        iconoCena.setOnClickListener(this);

        // Activamos el teclado numérico en los campos habituales.
        if (opciones.getBoolean("ActivarTecladoNumerico", false)){
            inputLinea.setInputType(InputType.TYPE_CLASS_PHONE);
            inputServicio.setInputType(InputType.TYPE_CLASS_PHONE);
            inputBus.setInputType(InputType.TYPE_CLASS_PHONE);
        }

        // Escribimos el título de la activity
        escribirTitulo();

        // Rellenar el bus y las notas.
        inputBus.setText(datosDia.getBus());
        inputNotas.setText(datosDia.getNotas());

        // Llenamos el día con datosDia
        noCambiar = true;
        rellenarDia();

        // Desactivar el foco
        bloqueFocus.requestFocus();
        noCambiar = false;

        // AL CAMBIAR EL ESTADO DEL CHECKBOX DE HUELGA
        checkHuelga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                datosDia.setHuelgaParcial(checkHuelga.isChecked());
                cambiaHorasHuelga();
                rellenarDia();
            }
        });

    }

    // AL CREARSE EL MENU SUPERIOR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_diacalendario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //AL PULSAR EL MENU SUPERIOR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();
        switch (id){
            case R.id.bt_guardar:
                validarCampos();
                if (datosDia.getMatricula() != 0){
                    datosDia.setCalificacion(datos.calificacionRelevo(datosDia.getMatricula()));
                }
                if (datos.guardaDia(datosDia)){
                    if (opciones.getBoolean("RellenarSemana", false) && datosDia.getDiaSemana() == 2){
                        rellenarSemana();
                    }
                    regularAño();
                    Toast.makeText(this, R.string.mensaje_diaGuardado, Toast.LENGTH_SHORT).show();
                    intent = new Intent();
                    intent.putExtra("Posicion", posicion);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, R.string.error_diaGuardado, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.bt_nuevo:
                intent = new Intent(context, EditarServiciosDia.class);
                startActivityForResult(intent, ACCION_SERVICIOS_DIA);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // CREAR MENU CONTEXTUAL
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contexto_diacalendario, menu);
    }

    // AL PULSAR UNA OPCION DEL MENU CONTEXTUAL
    @Override
    public boolean onContextItemSelected(MenuItem item){

        // Guardar el elemento que ha provocado el menu contextual
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int elementoPulsado = acmi.position;

        // recuperamos el cursor del adaptador.
        Cursor c = adaptador.getCursor();

        // Determinar la opción pulsada
        switch (item.getItemId()){
            case R.id.bt_borrar:
                ServicioDia servicioDia = new ServicioDia();
                servicioDia.setDia(diaActual);
                servicioDia.setMes(mesActual);
                servicioDia.setAño(añoActual);
                servicioDia.setLinea(c.getString(c.getColumnIndex("Linea")));
                servicioDia.setServicio(c.getString(c.getColumnIndex("Servicio")));
                servicioDia.setTurno(c.getInt(c.getColumnIndex("Turno")));
                datos.borraServicioDia(servicioDia);
                actualizarCursor();
                calcularHoras();
                escribirHoras();
                return true;
            case R.id.bt_vaciar:
                // Si no hay servicios auxiliares, salimos.
                if (cursor.getCount() == 0) return true;
                // Creamos el dialogo que preguntará si estamos seguros.
                AlertDialog.Builder aviso = new AlertDialog.Builder(context);
                aviso.setTitle("ATENCION");
                aviso.setMessage("¿Quieres borrar todos los servicios de la lista?");
                aviso.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Borramos los servicios
                        datos.vaciarServiciosDia(diaActual, mesActual, añoActual);
                        // Actualizamos la lista.
                        actualizarCursor();
                        calcularHoras();
                        escribirHoras();
                    }
                });
                aviso.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Salimos del método
                        return;
                    }
                });
                aviso.show();
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
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL VOLVER DE UNA SUBACTIVITY
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Evaluamos el codigo de petición.
        switch (requestCode){
            case ACCION_LISTA_INCIDENCIA:
                if (resultCode == RESULT_OK){
                    // Extraemos los datos
                    int cod = data.getIntExtra("Codigo", -1);
                    int tip = data.getIntExtra("Tipo", 0);
                    String inc = data.getStringExtra("Incidencia");
                    // Rellenamos la incidencia con el codigo
                    datosDia.setCodigoIncidencia(cod);
                    datosDia.setTipoIncidencia(tip);
                    datosDia.setTextoIncidencia(inc);
                    cambiaIncidencia();
                    break;
                }
                break;
            case ACCION_SERVICIOS_DIA:
                if (resultCode == RESULT_OK) {
                    // Extraemos los datos del intent
                    String l = data.getStringExtra("Linea");
                    String s = data.getStringExtra("Servicio");
                    int t = data.getIntExtra("Turno", 0);
                    String i = data.getStringExtra("Inicio");
                    String f = data.getStringExtra("Final");
                    String li = data.getStringExtra("LugarInicio");
                    String lf = data.getStringExtra("LugarFinal");
                    // Evaluamos que los datos no esten vacíos.
                    if (l.equals("") && s.equals("") && t == 0 && i.equals("") && f.equals("")) return;
                    // Creamos un servicio complementario o lo actualizamos.
                    ServicioDia servicioDia = new ServicioDia();
                    servicioDia.setDia(diaActual);
                    servicioDia.setMes(mesActual);
                    servicioDia.setAño(añoActual);
                    servicioDia.setLinea(l);
                    servicioDia.setServicio(s);
                    servicioDia.setTurno(t);
                    servicioDia.setInicio(i);
                    servicioDia.setFinal(f);
                    servicioDia.setLugarInicio(li);
                    servicioDia.setLugarFinal(lf);
                    if (data.getBooleanExtra("Nuevo", true)) {
                        datos.guardaServicioDia(servicioDia);
                        actualizarCursor();
                        calcularHoras();
                        escribirHoras();
                    } else {
                        datos.borraServicioDia(servicioDia);
                        datos.guardaServicioDia(servicioDia);
                        actualizarCursor();
                        calcularHoras();
                        escribirHoras();
                    }
                }
                break;
            case ACCION_LISTA_RELEVOS:
                if (resultCode == RESULT_OK){
                    // Extraemos los datos
                    int matr = data.getIntExtra("Matricula", 0);
                    String apell = data.getStringExtra("Apellidos");
                    // Rellenamos la matrícula del sustituto
                    inputMatriculaRelevo.setText(String.valueOf(matr));
                    inputApellidosRelevo.setText(String.valueOf(apell));
                    datosDia.setMatricula(matr);
                    datosDia.setApellidos(apell);
                }
                break;
            case ACCION_LISTA_SUSTI:
                if (resultCode == RESULT_OK){
                    // Extraemos los datos
                    int matr = data.getIntExtra("Matricula", 0);
                    String apell = data.getStringExtra("Apellidos");
                    // Rellenamos la matrícula del susti
                    inputMatriculaSusti.setText(String.valueOf(matr));
                    inputApellidosSusti.setText(String.valueOf(apell));
                    datosDia.setMatriculaSusti(matr);
                    datosDia.setApellidosSusti(apell);
                }
                break;
            default:
                break;
        }
    }

    // AL CAMBIAR EL FOCO DE UN CAMPO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // Comprueba si se admiten cambios o no
        if (noCambiar) return;

        EditText texto = null;
        // Si coge el foco
        if (hasFocus){
            switch (v.getId()){
                case R.id.et_matricula_susti:
                    texto = (EditText) v;
                    cadenaMatriculaSusti = texto.getText().toString();
                    break;
                case R.id.et_apellidos_susti:
                    texto = (EditText) v;
                    cadenaApellidosSusti = texto.getText().toString();
                    break;
                case R.id.et_linea:
                    texto = (EditText) v;
                    cadenaLinea = texto.getText().toString();
                    break;
                case R.id.et_servicio:
                    texto = (EditText) v;
                    cadenaServicio = texto.getText().toString();
                    break;
                case R.id.et_turno:
                    texto = (EditText) v;
                    cadenaTurno = texto.getText().toString();
                    break;
                case R.id.et_texto_linea:
                    texto = (EditText) v;
                    cadenaTextoLinea = texto.getText().toString();
                    break;
                case R.id.et_inicio:
                    texto = (EditText) v;
                    cadenaInicio = texto.getText().toString();
                    break;
                case R.id.et_final:
                    texto = (EditText) v;
                    cadenaFinal = texto.getText().toString();
                    break;
                case R.id.et_matricula_relevo:
                    texto = (EditText) v;
                    cadenaMatriculaRelevo = texto.getText().toString();
                    break;
                case R.id.et_apellidos_relevo:
                    texto = (EditText) v;
                    cadenaApellidosRelevo = texto.getText().toString();
                    break;
                case R.id.et_tomaDeje:
                    texto = (EditText) v;
                    cadenaTomaDeje = texto.getText().toString();
                    break;
                case R.id.et_euros:
                    texto = (EditText) v;
                    cadenaEuros = texto.getText().toString();
                    break;
                case R.id.et_huelga:
                    texto = (EditText) v;
                    cadenaHorasHuelga = texto.getText().toString();
                    break;
            }
        } else {
            // Si pierde el foco
            switch (v.getId()){
                case R.id.et_matricula_susti:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaMatriculaSusti)){
                        cambiaMatriculaSusti();
                        cadenaMatriculaSusti = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_apellidos_susti:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaApellidosSusti)){
                        cambiaApellidosSusti();
                        cadenaApellidosSusti = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_linea:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaLinea)){
                        cambiaLinea();
                        cadenaLinea = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_servicio:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaServicio)){
                        cambiaServicio();
                        cadenaServicio = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_turno:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaTurno)){
                        cambiaTurno();
                        cadenaTurno = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_texto_linea:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaTextoLinea)){
                        cambiaTextoLinea();
                        cadenaTextoLinea = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_inicio:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaInicio)){
                        cambiaInicio();
                        cadenaInicio = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_final:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaFinal)){
                        cambiaFinal();
                        cadenaFinal = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_matricula_relevo:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaMatriculaRelevo)){
                        cambiaMatriculaRelevo();
                        cadenaMatriculaRelevo = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_apellidos_relevo:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaApellidosRelevo)){
                        cambiaApellidosRelevo();
                        cadenaApellidosRelevo = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_tomaDeje:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaTomaDeje)){
                        String td = Hora.horaToString(inputTomaDeje.getText().toString().trim());
                        if (td.equals("")){
                            datosDia.setTomaDeje("");
                            datosDia.setTomaDejeDecimal(0d);
                        } else {
                            datosDia.setTomaDeje(td);
                            datosDia.setTomaDejeDecimal(Hora.horaToDecimal(td));
                        }
                        cadenaTomaDeje = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_euros:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaEuros)){
                        String e = Hora.validaHoraDecimal(inputEuros.getText().toString().trim());
                        if (e.equals("")){
                            datosDia.setEuros(0d);
                        } else {
                            datosDia.setEuros(Double.valueOf(e.replace(",", ".")));
                        }
                        cadenaEuros = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_huelga:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaHorasHuelga)){
                        String e = Hora.validaHoraDecimal(inputHuelga.getText().toString().trim());
                        if (e.equals("")){
                            datosDia.setHorasHuelga(0d);
                        } else {
                            datosDia.setHorasHuelga(Double.valueOf(e.replace(",", ".")));
                        }
                        cambiaHorasHuelga();
                        cadenaHorasHuelga = "";
                        rellenarDia();
                    }
                    break;

            }
        }
    }

    // AL HACER CLIC EN UN SERVICIO COMPLEMENTARIO
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Extraemos los datos del cursor
        Cursor c = adaptador.getCursor();

        // Creamos un intent para devolver los datos de la incidencia
        Intent intent = new Intent(context, EditarServiciosDia.class);
        intent.putExtra("Linea", c.getString(c.getColumnIndex("Linea")));
        intent.putExtra("Servicio", c.getString(c.getColumnIndex("Servicio")));
        intent.putExtra("Turno", c.getInt(c.getColumnIndex("Turno")));
        intent.putExtra("Inicio", c.getString(c.getColumnIndex("Inicio")));
        intent.putExtra("Final", c.getString(c.getColumnIndex("Final")));
        intent.putExtra("LugarInicio", c.getString(c.getColumnIndex("LugarInicio")));
        intent.putExtra("LugarFinal", c.getString(c.getColumnIndex("LugarFinal")));
        startActivityForResult(intent, ACCION_SERVICIOS_DIA);

    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // AL TOCAR LARGO EN UNA HORA
    @Override
    public boolean onLongClick(View v) {

        Intent intent;

/*
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setBackgroundResource(R.drawable.fondo_dialogo);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        //dialogo.setView(input);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String td = Hora.validaHoraDecimal(input.getText().toString());
                if (!td.equals("")){
                    switch (horaSeleccionada){
                        case TRABAJADAS:
                            datosDia.setTrabajadas(Double.valueOf(td.replace(",", ".")));
                            break;
                        case ACUMULADAS:
                            datosDia.setAcumuladas(Double.valueOf(td.replace(",", ".")));
                            break;
                        case NOCTURNAS:
                            datosDia.setNocturnas(Double.valueOf(td.replace(",", ".")));
                            break;
                    }
                    escribirHoras();
                }
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
*/

        switch (v.getId()){
/*
            case R.id.tv_trabajadas:
                vibrador.vibrate(50);
                input.setText(Hora.textoDecimal(datosDia.getTrabajadas()));
                input.selectAll();
                dialogo.setView(input);
                dialogo.setTitle("Horas Trabajadas");
                horaSeleccionada = TRABAJADAS;
                dialogo.show();
                break;
            case R.id.tv_acumuladas:
                vibrador.vibrate(50);
                input.setText(Hora.textoDecimal(datosDia.getAcumuladas()));
                input.selectAll();
                dialogo.setView(input);
                dialogo.setTitle("Horas Acumuladas");
                horaSeleccionada = ACUMULADAS;
                dialogo.show();
                break;
            case R.id.tv_nocturnas:
                vibrador.vibrate(50);
                input.setText(Hora.textoDecimal(datosDia.getNocturnas()));
                input.selectAll();
                dialogo.setView(input);
                dialogo.setTitle("Horas Nocturnas");
                horaSeleccionada = NOCTURNAS;
                dialogo.show();
                break;
*/
            case R.id.et_matricula_relevo:
                // Creamos el intent.
                intent = new Intent(context, Relevos.class);
                // Lanzamos la activity
                startActivityForResult(intent, ACCION_LISTA_RELEVOS);
                break;
            case R.id.et_matricula_susti:
                // Creamos el intent.
                intent = new Intent(context, Relevos.class);
                // Lanzamos la activity
                startActivityForResult(intent, ACCION_LISTA_SUSTI);
                break;
            case R.id.et_linea:
            case R.id.et_servicio:
            case R.id.et_bus:
                vibrador.vibrate(50);
                if (((EditText)v).getInputType() == android.text.InputType.TYPE_CLASS_TEXT){
                    ((EditText)v).setInputType(InputType.TYPE_CLASS_PHONE);
                } else {
                    ((EditText)v).setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
	        case R.id.tv_incidencia:
	        	boolean modoBasico = opciones.getBoolean("ModoBasico", false);
	        	opciones.edit().putBoolean("ModoBasico", !modoBasico).apply();
	        	finish();
	        	startActivity(getIntent());
	        	break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setBackgroundResource(R.drawable.fondo_dialogo);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String td = Hora.validaHoraDecimal(input.getText().toString());
                if (!td.equals("")){
                    switch (horaSeleccionada){
                        case TRABAJADAS:
                            datosDia.setTrabajadas(Double.valueOf(td.replace(",", ".")));
                            break;
                        case ACUMULADAS:
                            datosDia.setAcumuladas(Double.valueOf(td.replace(",", ".")));
                            break;
                        case NOCTURNAS:
                            datosDia.setNocturnas(Double.valueOf(td.replace(",", ".")));
                            break;
                    }
                    escribirHoras();
                }
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        switch (v.getId()){
            case R.id.tv_trabajadas:
                vibrador.vibrate(50);
                input.setText(Hora.textoDecimal(datosDia.getTrabajadas()));
                input.selectAll();
                dialogo.setView(input);
                dialogo.setTitle("Horas Trabajadas");
                horaSeleccionada = TRABAJADAS;
                dialogo.show();
                break;
            case R.id.tv_acumuladas:
                vibrador.vibrate(50);
                input.setText(Hora.textoDecimal(datosDia.getAcumuladas()));
                input.selectAll();
                dialogo.setView(input);
                dialogo.setTitle("Horas Acumuladas");
                horaSeleccionada = ACUMULADAS;
                dialogo.show();
                break;
            case R.id.tv_nocturnas:
                vibrador.vibrate(50);
                input.setText(Hora.textoDecimal(datosDia.getNocturnas()));
                input.selectAll();
                dialogo.setView(input);
                dialogo.setTitle("Horas Nocturnas");
                horaSeleccionada = NOCTURNAS;
                dialogo.show();
                break;
            case R.id.desayuno:
                vibrador.vibrate(50);
                datosDia.setDesayuno(!datosDia.isDesayuno());
                rellenarDia();
                break;
            case R.id.comida:
                vibrador.vibrate(50);
                datosDia.setComida(!datosDia.isComida());
                rellenarDia();
                break;
            case R.id.cena:
                vibrador.vibrate(50);
                datosDia.setCena(!datosDia.isCena());
                rellenarDia();
                break;
        }

    }


    // AL PULSAR EL BOTON INCIDENCIA
    public void botonPulsado(View view){

        // Creamos el intent.
        Intent intent = new Intent(context, Incidencias.class);
        // Lanzamos la activity
        startActivityForResult(intent, ACCION_LISTA_INCIDENCIA);

    }

    // INSTANCIA TODOS LOS ELEMENTOS DE LA ACTIVITY
    private void instanciarElementos(){
        bloqueSusti = (LinearLayout) findViewById(R.id.ly_susti);
        bloqueHuelga = (LinearLayout) findViewById(R.id.ly_huelga);
        bloqueFocus = (LinearLayout) findViewById(R.id.ly_focus);

        inputIncidencia = (TextView) findViewById(R.id.tv_incidencia);
        inputMatriculaSusti = (EditText) findViewById(R.id.et_matricula_susti);
        inputApellidosSusti = (EditText) findViewById(R.id.et_apellidos_susti);
        inputHuelga = (EditText) findViewById(R.id.et_huelga);
        inputLinea = (EditText) findViewById(R.id.et_linea);
        inputServicio = (EditText) findViewById(R.id.et_servicio);
        inputTurno = (EditText) findViewById(R.id.et_turno);
        inputTextoLinea = (EditText) findViewById(R.id.et_texto_linea);
        inputInicio = (EditText) findViewById(R.id.et_inicio);
        inputFinal = (EditText) findViewById(R.id.et_final);
        inputBus = (EditText) findViewById(R.id.et_bus);
        inputMatriculaRelevo = (EditText) findViewById(R.id.et_matricula_relevo);
        inputApellidosRelevo = (EditText) findViewById(R.id.et_apellidos_relevo);
        inputNotas = (EditText) findViewById(R.id.et_notas);
        inputLugarInicio = (EditText) findViewById(R.id.et_lugarInicio);
        inputLugarFinal = (EditText) findViewById(R.id.et_lugarFinal);
        inputTomaDeje = (EditText) findViewById(R.id.et_tomaDeje);
        inputEuros = (EditText) findViewById(R.id.et_euros);

        checkHuelga = (CheckBox) findViewById(R.id.cb_huelga);

        cabeceraComplementarios = (TextView) findViewById(R.id.tv_complementarios);

        textoTrabajadas = (TextView) findViewById(R.id.tv_trabajadas);
        textoAcumuladas = (TextView) findViewById(R.id.tv_acumuladas);
        textoNocturnas = (TextView) findViewById(R.id.tv_nocturnas);

        listaServiciosDia = (ListView) findViewById(R.id.listaAuxiliares);

        iconoDesayuno = (ImageView) findViewById(R.id.desayuno);
        iconoComida = (ImageView) findViewById(R.id.comida);
        iconoCena = (ImageView) findViewById(R.id.cena);

        inputMatriculaSusti.setOnFocusChangeListener(this);
        inputApellidosSusti.setOnFocusChangeListener(this);
        inputLinea.setOnFocusChangeListener(this);
        inputTextoLinea.setOnFocusChangeListener(this);
        inputServicio.setOnFocusChangeListener(this);
        inputTurno.setOnFocusChangeListener(this);
        inputInicio.setOnFocusChangeListener(this);
        inputFinal.setOnFocusChangeListener(this);
        inputMatriculaRelevo.setOnFocusChangeListener(this);
        inputApellidosRelevo.setOnFocusChangeListener(this);
        inputTomaDeje.setOnFocusChangeListener(this);
        inputEuros.setOnFocusChangeListener(this);
        inputHuelga.setOnFocusChangeListener(this);
    }

    // ESCRIBE EL DIA EN EL MENU SUPERIOR
    private void escribirTitulo(){
        String s = (diaActual > 9) ? String.valueOf(diaActual) : "0" + String.valueOf(diaActual);
        s += " - " + Hora.DIAS_SEMANA[diaSemanaActual];
        String ss = Hora.MESES_MIN[mesActual] + " - " + String.valueOf(añoActual);
        getActionBar().setTitle(s);
        getActionBar().setSubtitle(ss);
    }

    // COMPRUEBA QUE LAS HORAS ESTEN VACIAS
    private boolean isHorasVacio(){
        if (datosDia.getInicio().trim().equals("")){
            return true;
        } else if (datosDia.getFinal().trim().equals("")){
            return true;
        } else
            //TODO: En la inferencia de turnos, comprobar que esto no nos borra la incidencia.
        	return datosDia.getTurno() == 0;
    }

    // ESCRIBIR LAS HORAS EN SUS HUECOS
    private void escribirHoras(){
        String txt = "";
        txt = "Tra. : ";
        txt += Hora.textoDecimal(datosDia.getTrabajadas());
        textoTrabajadas.setText(txt);
        txt = "Noc. : ";
        txt += Hora.textoDecimal(datosDia.getNocturnas());
        textoNocturnas.setText(txt);
        txt = "Acu. : ";
        if (datosDia.getAcumuladas() > -0.01) {
            textoAcumuladas.setTextColor(Colores.VERDE_OSCURO);
        } else {
            textoAcumuladas.setTextColor(Colores.ROJO);
        }
        txt += Hora.textoDecimal(datosDia.getAcumuladas());
        textoAcumuladas.setText(txt);
    }

    // BORRA TODOS LOS CAMPOS DEL SERVICIO
    private void borrarCamposServicio(){
        datosDia.setLinea("");
        datosDia.setTextoLinea("");
        datosDia.setServicio("");
        datosDia.setTurno(0);
        datosDia.setInicio("");
        datosDia.setFinal("");
        datosDia.setBus("");
        datosDia.setMatricula(0);
        datosDia.setApellidos("");
        datosDia.setMatriculaSusti(0);
        datosDia.setApellidosSusti("");
        datos.vaciarServiciosDia(diaActual, mesActual, añoActual);
        datosDia.setTrabajadas(0d);
        datosDia.setAcumuladas(0d);
        datosDia.setNocturnas(0d);
        datosDia.setLugarInicio("");
        datosDia.setLugarFinal("");
        datosDia.setTomaDeje("");
        datosDia.setTomaDejeDecimal(0d);
        datosDia.setEuros(0d);
        datosDia.setHuelgaParcial(false);
        datosDia.setHorasHuelga(0d);
    }

    // COMPRUEBA QUE EL SERVICIO ESTE VACIO
    private boolean isServicioVacio(){
	    return datosDia.getLinea().trim().equals("") ||
			    datosDia.getServicio().equals("") ||
			    datosDia.getTurno() == 0;
    }

    // ACTUALIZA EL CURSOR CON LOS SERVICIOS DEL DIA
    private void actualizarCursor(){
        cursor = datos.cursorServiciosDia(diaActual, mesActual, añoActual);
        adaptador.changeCursor(cursor);
        adaptador.notifyDataSetChanged();
        // Definir encabezado de Servicios del día.
        if (cursor.getCount() == 0){
            cabeceraComplementarios.setText("No hay servicios complementarios");
        } else {
            cabeceraComplementarios.setText("Servicios complementarios");
        }
        // Calcular las horas
        Calculos.setAlturaLista(listaServiciosDia);

    }

    // CALCULAR LAS HORAS DEL SERVICIO Y METERLAS EN LOS DATOS DEL DIA
    private void calcularHoras(){

        // Determinamos cuantos servicios hay en total
        int num = cursor.getCount() + 1;

        HorasServicio[] servs = new HorasServicio[num];
        servs[0] = new HorasServicio();
        servs[0].Inicio = Hora.horaToInt(datosDia.getInicio());
        servs[0].Final = Hora.horaToInt(datosDia.getFinal());
        for (int m=1; m <num; m++){
            cursor.moveToPosition(m-1);
            servs[m] = new HorasServicio();
            servs[m].Inicio = Hora.horaToInt(cursor.getString(cursor.getColumnIndex("Inicio")));
            servs[m].Final = Hora.horaToInt(cursor.getString(cursor.getColumnIndex("Final")));
        }
        EstadoDia resultado = Calculos.TiempoTrabajado(servs, context, datosDia.getTurno(), datosDia.getTipoIncidencia());
        if (resultado == null){
            datosDia.setTrabajadas(0d);
            datosDia.setAcumuladas(0d);
            datosDia.setNocturnas(0d);
            datosDia.setDesayuno(false);
            datosDia.setComida(false);
            datosDia.setCena(false);
        } else {
            datosDia.setTrabajadas(resultado.getTrabajadas());
            datosDia.setAcumuladas(resultado.getAcumuladas());
            datosDia.setNocturnas(resultado.getNocturnas());
            datosDia.setDesayuno(resultado.isDesayuno());
            datosDia.setComida(resultado.isComida());
            datosDia.setCena(resultado.isCena());
        }

        // Si la incidencia es Huelga, calculamos los datos de la huelga
        if (datosDia.getCodigoIncidencia() == 15){

            // Recuperamos los datos del convenio que necesitamos.
            long jMedia = opciones.getLong("JorMedia", Double.doubleToRawLongBits(7.75d));
            double Jornada = Double.longBitsToDouble(jMedia);
            long jMinima = opciones.getLong("JorMinima", Double.doubleToRawLongBits(7d));
            double JornadaMinima = Double.longBitsToDouble(jMinima);
            // Si la huelga es parcial...
            if (datosDia.isHuelgaParcial()){
                datosDia.setTrabajadas(Jornada);
                //TODO Comprobar que las horas trabajadas son la jornada media y no la jornada mínima.
                Double acum = ((Jornada - JornadaMinima) * datosDia.getHorasHuelga())/Jornada;
                datosDia.setAcumuladas(-acum);
            // Si la huelga es completa...
            } else {
                datosDia.setTrabajadas(Jornada);
                //TODO Comprobar que las horas trabajadas son la jornada media y no la jornada mínima.
                datosDia.setAcumuladas(JornadaMinima - Jornada);
                datosDia.setNocturnas(0d);
            }

        }


    }

    // RELLENAR TODOS LOS CAMPOS CON LOS DATOS DEL SERVICIO
    public void rellenarDia(){

        // Mostrar el bloque susti si es necesario
        bloqueSusti.setVisibility(View.GONE);
        if (datosDia.getCodigoIncidencia() == 11 || datosDia.getCodigoIncidencia() == 12){
            // Rellenar el bloque Compañero.
            inputMatriculaSusti.setText((datosDia.getMatriculaSusti() == 0) ? "" :
                                         String.valueOf(datosDia.getMatriculaSusti()));
            inputApellidosSusti.setText(datosDia.getApellidosSusti());
            bloqueSusti.setVisibility(View.VISIBLE);
        }

        //Mostrar el bloque huelga si es necesario
        bloqueHuelga.setVisibility(View.GONE);
        if (datosDia.getCodigoIncidencia() == 15){
            if (datosDia.getHorasHuelga() == 0){
                inputHuelga.setText("");
            } else {
                inputHuelga.setText(Hora.textoDecimal(datosDia.getHorasHuelga()));
            }
            checkHuelga.setChecked(datosDia.isHuelgaParcial());
            bloqueHuelga.setVisibility(View.VISIBLE);
        }

        // Escribir la incidencia.
        inputIncidencia.setText(
                (datosDia.getTextoIncidencia().equals("")) ? "Incidencia Desconocida" :
                                                             datosDia.getTextoIncidencia());

        // Rellenar el bloque Linea y TextoLinea
        inputLinea.setText(datosDia.getLinea());
        inputServicio.setText(datosDia.getServicio());
        inputTurno.setText((datosDia.getTurno() == 0) ? "" : String.valueOf(datosDia.getTurno()));
        inputTextoLinea.setText(datosDia.getTextoLinea());

        // Rellenar el bloque Horario.
        inputInicio.setText(datosDia.getInicio());
        inputFinal.setText(datosDia.getFinal());
        inputLugarInicio.setText(datosDia.getLugarInicio());
        inputLugarFinal.setText(datosDia.getLugarFinal());

        inputTomaDeje.setText(datosDia.getTomaDeje());
        if (datosDia.getEuros() == 0){
            inputEuros.setText("");
        } else {
            inputEuros.setText(Hora.textoDecimal(datosDia.getEuros()));
        }

        // Rellenar el bloque Relevo.
        inputMatriculaRelevo.setText((datosDia.getMatricula() == 0) ? "" :
                                      String.valueOf(datosDia.getMatricula()));
        inputApellidosRelevo.setText(datosDia.getApellidos());


        // Definir encabezado de Servicios del día.
        if (cursor.getCount() == 0){
            cabeceraComplementarios.setText("No hay servicios complementarios");
        } else {
            cabeceraComplementarios.setText("Servicios complementarios");
        }

        // Rellenar el bloque Horas
        escribirHoras();

        // Rellenar las dietas.
        if (datosDia.isDesayuno()){
            iconoDesayuno.clearColorFilter();
        } else {
            iconoDesayuno.setColorFilter(Colores.GRIS_OSCURO);
        }
        if (datosDia.isComida()){
            iconoComida.clearColorFilter();
        } else {
            iconoComida.setColorFilter(Colores.GRIS_OSCURO);
        }
        if (datosDia.isCena()){
            iconoCena.clearColorFilter();
        } else {
            iconoCena.setColorFilter(Colores.GRIS_OSCURO);
        }

    }

    // VALIDAR LOS CAMPOS DEL VIEW
    public void validarCampos(){

        // LINEA
        String l = inputLinea.getText().toString().trim().toUpperCase();
        if (l.equals("")){
            datosDia.setLinea("");
        }
        datosDia.setLinea(l);

        // SERVICIO
        String s = Hora.validarServicio(inputServicio.getText().toString().trim());
        if (s.equals("")) {
            datosDia.setServicio("");
        }
        datosDia.setServicio(s);

        // TURNO
        String st = inputTurno.getText().toString().trim();
        switch (st){
            case "1":case "01":
                datosDia.setTurno(1);
                break;
            case "2":case "02":
                datosDia.setTurno(2);
                break;
            default:
                datosDia.setTurno(0);
        }

        // INICIO
        String i = Hora.horaToString(inputInicio.getText().toString().trim());
        if (i.equals("")){
            datosDia.setInicio("");
        }
        datosDia.setInicio(i);

        // FINAL
        String f = Hora.horaToString(inputFinal.getText().toString().trim());
        if (f.equals("")){
            datosDia.setFinal("");
        }
        datosDia.setFinal(f);

        // TOMA Y DEJE
        String td = Hora.horaToString(inputTomaDeje.getText().toString().trim());
        if (td.equals("")){
            datosDia.setTomaDeje("");
            datosDia.setTomaDejeDecimal(0d);
        } else {
            datosDia.setTomaDeje(td);
            datosDia.setTomaDejeDecimal(Hora.horaToDecimal(td));
        }

        // EUROS
        String e = Hora.validaHoraDecimal(inputEuros.getText().toString().trim());
        if (e.equals("")){
            datosDia.setEuros(0d);
        } else {
            datosDia.setEuros(Double.valueOf(e.replace(",", ".")));
        }

        // HORAS HUELGA
        String hh = Hora.validaHoraDecimal(inputHuelga.getText().toString().trim());
        if (hh.equals("")){
            datosDia.setHorasHuelga(0d);
        } else {
            datosDia.setHorasHuelga(Double.valueOf(hh.replace(",", ".")));
        }

        // CAMPOS DE TEXTO
        datosDia.setTextoLinea(inputTextoLinea.getText().toString().trim());
        datosDia.setBus(inputBus.getText().toString().trim());
        datosDia.setNotas(inputNotas.getText().toString());
        datosDia.setApellidos(inputApellidosRelevo.getText().toString().trim());
        datosDia.setApellidosSusti(inputApellidosSusti.getText().toString().trim());
        datosDia.setLugarInicio(inputLugarInicio.getText().toString().trim());
        datosDia.setLugarFinal(inputLugarFinal.getText().toString().trim());

    }

    // REGULA LAS HORAS DE FIN DE AÑO Y LOS AÑOS BISIESTOS.
    public void regularAño(){

        // Declaramos las variables.
        MathContext mathContext = new MathContext(4, RoundingMode.HALF_UP);
        double jAnual = (double) opciones.getInt("JornadaAnual", 1592);
        long jorMedia = opciones.getLong("JorMedia", 0);
        double jMedia = Double.longBitsToDouble(jorMedia);

        // Si es el día 31 de diciembre...
        if (datosDia.getDia() == 31 && datosDia.getMes() == 12 && opciones.getBoolean("RegularJornadaAnual", true)) {
            double trabajadas = datos.diasTrabajadosConvenio(datosDia.getAño()) * jMedia;
            trabajadas += datos.diasEnfermoComputables(datosDia.getAño()) * jMedia;
            double diff = trabajadas - jAnual;
            datos.setAjenaFinAño(diff, datosDia.getAño());
        }

        // Si es el día 29 de febrero
        if (datosDia.getDia() == 29 && datosDia.getMes() == 2 && opciones.getBoolean("RegularBisiestos", false)) {
            double jBisiestos = jAnual / 365 * 366;
            double diff = Hora.redondeaDecimal4(jAnual - jBisiestos);
            datos.setAjenaBisiestos(diff, datosDia.getAño());
        }
    }

    //region  ACCIONES AL CAMBIAR EL FOCO DE UN CAMPO CONCRETO

    // AL CAMBIAR INCIDENCIA
    public void cambiaIncidencia(){
        // Si el código de la incidencia es -1 salimos
        if (datosDia.getCodigoIncidencia() == -1){
            datosDia.setCodigoIncidencia(0);
            return;
        }

        // Si el código de la incidencia es cero, repetimos el día anterior.
        if (datosDia.getCodigoIncidencia() == 0){
            // Evaluamos que el día no sea el primer día que se muestra.
            int primerMes = opciones.getInt("PrimerMes", 10);
            int primerAño = opciones.getInt("PrimerAño", 2014);
            int dia = datosDia.getDia();
            int mes = datosDia.getMes();
            int año = datosDia.getAño();
            int diaOriginal = dia;
            int mesOriginal = mes;
            int añoOriginal = año;
            int diaSemanaOriginal = datosDia.getDiaSemana();
            if (dia == 1 && mes == primerMes && año == primerAño){
                // No se puede repetir el día
                Toast.makeText(this, R.string.mensaje_primerDia, Toast.LENGTH_SHORT).show();
            } else {
                // Evaluamos qué dia es el anterior al día que tenemos.
                dia--;
                if (dia == 0){
                    mes--;
                    if (mes == 0){
                        mes = 12;
                        año--;
                    }
                    Calendar fecha = Calendar.getInstance();
                    fecha.set(año, mes-1, 1);
                    dia = fecha.getActualMaximum(Calendar.DAY_OF_MONTH);
                }
            }
            datosDia = datos.servicioDia(dia, mes, año);
            if (datosDia.getDia() == 0) {
                Toast.makeText(this, R.string.mensaje_mesNoExiste, Toast.LENGTH_SHORT).show();
                return;
            }
            datosDia.setDia(diaOriginal);
            datosDia.setMes(mesOriginal);
            datosDia.setAño(añoOriginal);
            datosDia.setDiaSemana(diaSemanaOriginal);

            // Copiamos los servicios del día
            datos.vaciarServiciosDia(diaOriginal, mesOriginal, añoOriginal);
            Cursor c = datos.cursorServiciosDia(dia, mes, año);
            if (c.getCount() > 0){
                while (c.moveToNext()){
                    ServicioDia sd = new ServicioDia();
                    sd.setDia(diaOriginal);
                    sd.setMes(mesOriginal);
                    sd.setAño(añoOriginal);
                    sd.setLinea(c.getString(c.getColumnIndex("Linea")));
                    sd.setServicio(c.getString(c.getColumnIndex("Servicio")));
                    sd.setTurno(c.getInt(c.getColumnIndex("Turno")));
                    sd.setInicio(c.getString(c.getColumnIndex("Inicio")));
                    sd.setFinal(c.getString(c.getColumnIndex("Final")));
                    sd.setLugarInicio(c.getString(c.getColumnIndex("LugarInicio")));
                    sd.setLugarFinal(c.getString(c.getColumnIndex("LugarFinal")));
                    datos.guardaServicioDia(sd);
                    actualizarCursor();
                }
            }
            c.close();

            rellenarDia();
            return;
        }

        // Evaluamos el tipo de incidencia
        switch (datosDia.getTipoIncidencia()){

            // TRABAJO
            case 1:
                if (isHorasVacio()) {
                    rellenarDia();
                    return;
                }
                calcularHoras();
                rellenarDia();
                break;
            // FRANQUEO A TRABAJAR
            case 2:
                if (isHorasVacio()) {
                    rellenarDia();
                    return;
                }
                calcularHoras();
                rellenarDia();
                break;
            // FIESTA POR OTRO DIA
            case 3:
                borrarCamposServicio();
                datosDia.setTrabajadas(0);
                long jorMedia = opciones.getLong("JorMedia", 0);
                datosDia.setAcumuladas(-Double.longBitsToDouble(jorMedia));
                //datosDia.setAcumuladas(-Hora.horaToDecimal(opciones.getInt("JornadaMedia", 465)));
                datosDia.setNocturnas(0);
                inputBus.setText("");
                rellenarDia();
                actualizarCursor();
                break;
            // FRANQUEO
            case 4:
                borrarCamposServicio();
                datosDia.setTrabajadas(0);
                datosDia.setAcumuladas(0);
                datosDia.setNocturnas(0);
                inputBus.setText("");
                rellenarDia();
                actualizarCursor();
                break;
            // TRABAJO SIN ACUMULAR
            case 5:
                datosDia.setTrabajadas(0);
                datosDia.setAcumuladas(0);
                datosDia.setNocturnas(0);
                if (datosDia.isHuelgaParcial()) calcularHoras();
                rellenarDia();
                break;
            // JORNADA MEDIA
            case 6:
                //borrarCamposServicio();
                datosDia.setTrabajadas(0);
                long jorMed = opciones.getLong("JorMedia", 0);
                datosDia.setTrabajadas(Double.longBitsToDouble(jorMed));
                datosDia.setAcumuladas(0);
                datosDia.setNocturnas(0);
                inputBus.setText("");
                rellenarDia();
                actualizarCursor();
                break;
        }
    }

    // AL CAMBIAR LINEA
    private void cambiaLinea(){
        // Validamos los campos
        validarCampos();
        // Si la línea existe, ponemos el texto.
        Linea linea = datos.getLinea(datosDia.getLinea());
        if (linea != null) {
            datosDia.setTextoLinea(linea.getTexto());
        }
        // Si algún dato del servicio falta, se sale.
        if (!isServicioVacio()) {
            // Recuperamos el servicio
            Servicio servicio = datos.getServicio(datosDia.getLinea(),
                    datosDia.getServicio(),
                    datosDia.getTurno());
            // Si el servicio no existe se sale.
            if (servicio != null) {
                // Introducimos inicio y final.
                datosDia.setInicio(servicio.getInicio());
                datosDia.setFinal(servicio.getFinal());
                datosDia.setLugarInicio(servicio.getLugarInicio());
                datosDia.setLugarFinal(servicio.getLugarFinal());
                datosDia.setTomaDeje(servicio.getTomaDeje());
                datosDia.setEuros(servicio.getEuros());
                // Añadimos los servicios auxiliares asociados al servicio
                datos.vaciarServiciosDia(diaActual, mesActual, añoActual);
                Cursor c = datos.cursorServiciosAuxiliares(datosDia.getLinea(), datosDia.getServicio(), datosDia.getTurno());
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        ServicioDia sd = new ServicioDia();
                        sd.setDia(diaActual);
                        sd.setMes(mesActual);
                        sd.setAño(añoActual);
                        sd.setLinea(c.getString(c.getColumnIndex("LineaAuxiliar")));
                        sd.setServicio(c.getString(c.getColumnIndex("ServicioAuxiliar")));
                        sd.setTurno(c.getInt(c.getColumnIndex("TurnoAuxiliar")));
                        sd.setInicio(c.getString(c.getColumnIndex("Inicio")));
                        sd.setFinal(c.getString(c.getColumnIndex("Final")));
                        sd.setLugarInicio(c.getString(c.getColumnIndex("LugarInicio")));
                        sd.setLugarFinal(c.getString(c.getColumnIndex("LugarFinal")));
                        datos.guardaServicioDia(sd);
                    }
                }
                actualizarCursor();
            }
            // Añadimos el relevo, si no está metido.
            if (datosDia.getMatricula() == 0 && opciones.getInt("RelevoFijo", 0) != 0) {
                // Introducimos la matrícula
                datosDia.setMatricula(opciones.getInt(("RelevoFijo"), 0));
                // Si no existe el relevo se sale.
                Relevo r = datos.getRelevo(datosDia.getMatricula());
                if (r == null) return;
                // Introducimos los apellidos
                datosDia.setApellidos(r.getApellidos());
                datosDia.setCalificacion(r.getCalificacion());

            }

        }
        if (!isHorasVacio()) {
            // Si el tipo de incidencia es de calcular horas, se calculan y se escriben.
            if (datosDia.getTipoIncidencia() == 1 || datosDia.getTipoIncidencia() == 2) {
                calcularHoras();
            }
        }
    }

    // AL CAMBIAR SERVICIO
    public void cambiaServicio() {
        // Validamos los campos
        validarCampos();
        // Si algún dato del servicio falta, se sale.
        if (!isServicioVacio()) {
            // Recuperamos el servicio
            Servicio servicio = datos.getServicio(datosDia.getLinea(),
                    datosDia.getServicio(),
                    datosDia.getTurno());
            // Si el servicio no existe se sale.
            if (servicio != null) {
                // Introducimos inicio y final.
                datosDia.setInicio(servicio.getInicio());
                datosDia.setFinal(servicio.getFinal());
                datosDia.setLugarInicio(servicio.getLugarInicio());
                datosDia.setLugarFinal(servicio.getLugarFinal());
                datosDia.setTomaDeje(servicio.getTomaDeje());
                datosDia.setEuros(servicio.getEuros());
                // Añadimos los servicios auxiliares asociados al servicio
                datos.vaciarServiciosDia(diaActual, mesActual, añoActual);
                Cursor c = datos.cursorServiciosAuxiliares(datosDia.getLinea(), datosDia.getServicio(), datosDia.getTurno());
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        ServicioDia sd = new ServicioDia();
                        sd.setDia(diaActual);
                        sd.setMes(mesActual);
                        sd.setAño(añoActual);
                        sd.setLinea(c.getString(c.getColumnIndex("LineaAuxiliar")));
                        sd.setServicio(c.getString(c.getColumnIndex("ServicioAuxiliar")));
                        sd.setTurno(c.getInt(c.getColumnIndex("TurnoAuxiliar")));
                        sd.setInicio(c.getString(c.getColumnIndex("Inicio")));
                        sd.setFinal(c.getString(c.getColumnIndex("Final")));
                        sd.setLugarInicio(c.getString(c.getColumnIndex("LugarInicio")));
                        sd.setLugarFinal(c.getString(c.getColumnIndex("LugarFinal")));
                        datos.guardaServicioDia(sd);
                    }
                }
                actualizarCursor();
            }
            // Añadimos el relevo, si no está metido.
            if (datosDia.getMatricula() == 0 && opciones.getInt("RelevoFijo", 0) != 0){
                // Introducimos la matrícula
                datosDia.setMatricula(opciones.getInt(("RelevoFijo"), 0));
                // Si no existe el relevo se sale.
                Relevo r = datos.getRelevo(datosDia.getMatricula());
                if (r == null) return;
                // Introducimos los apellidos
                datosDia.setApellidos(r.getApellidos());
                datosDia.setCalificacion(r.getCalificacion());

            }

        }
        if (!isHorasVacio()) {
            // Si el tipo de incidencia es de calcular horas, se calculan y se escriben.
            if (datosDia.getTipoIncidencia() == 1 || datosDia.getTipoIncidencia() == 2) {
                calcularHoras();
            }
        }
    }

    // AL CAMBIAR TURNO
    public void cambiaTurno(){
        // Validamos los campos
        validarCampos();
        // Si el servicio estan vacíos
        if (!isServicioVacio()) {
            // Recuperamos el servicio
            Servicio servicio = datos.getServicio(datosDia.getLinea(),
                    datosDia.getServicio(),
                    datosDia.getTurno());
            // Si el servicio existe se escribe.
            if (servicio != null) {
                // Introducimos inicio y final.
                datosDia.setInicio(servicio.getInicio());
                datosDia.setFinal(servicio.getFinal());
                datosDia.setLugarInicio(servicio.getLugarInicio());
                datosDia.setLugarFinal(servicio.getLugarFinal());
                datosDia.setTomaDeje(servicio.getTomaDeje());
                datosDia.setEuros(servicio.getEuros());
                // Añadimos los servicios auxiliares asociados al servicio
                datos.vaciarServiciosDia(diaActual, mesActual, añoActual);
                Cursor c = datos.cursorServiciosAuxiliares(datosDia.getLinea(), datosDia.getServicio(), datosDia.getTurno());
                if (c.getCount() > 0){
                    while (c.moveToNext()){
                        ServicioDia sd = new ServicioDia();
                        sd.setDia(diaActual);
                        sd.setMes(mesActual);
                        sd.setAño(añoActual);
                        sd.setLinea(c.getString(c.getColumnIndex("LineaAuxiliar")));
                        sd.setServicio(c.getString(c.getColumnIndex("ServicioAuxiliar")));
                        sd.setTurno(c.getInt(c.getColumnIndex("TurnoAuxiliar")));
                        sd.setInicio(c.getString(c.getColumnIndex("Inicio")));
                        sd.setFinal(c.getString(c.getColumnIndex("Final")));
                        sd.setLugarInicio(c.getString(c.getColumnIndex("LugarInicio")));
                        sd.setLugarFinal(c.getString(c.getColumnIndex("LugarFinal")));
                        datos.guardaServicioDia(sd);
                    }
                }
                actualizarCursor();
            }
            // Añadimos el relevo, si no está metido.
            if (datosDia.getMatricula() == 0 && opciones.getInt("RelevoFijo", 0) != 0){
                // Introducimos la matrícula
                datosDia.setMatricula(opciones.getInt(("RelevoFijo"), 0));
                // Si no existe el relevo se sale.
                Relevo r = datos.getRelevo(datosDia.getMatricula());
                if (r == null) return;
                // Introducimos los apellidos
                datosDia.setApellidos(r.getApellidos());
                datosDia.setCalificacion(r.getCalificacion());

            }
        }
        // Si el tipo de incidencia es de calcular horas, se calculan y se escriben.
        if (!isHorasVacio()){
            if (datosDia.getTipoIncidencia() == 1 || datosDia.getTipoIncidencia() == 2) {
                calcularHoras();
            }
        }

    }

    // AL CAMBIAR TEXTO LINEA
    public void cambiaTextoLinea(){

        // Extraemos el texto escrito
        String tl = inputTextoLinea.getText().toString().trim();
        // Si está vacío se sale.
        if (tl.equals("")) {
            datosDia.setTextoLinea("");
            return;
        }
        // Introducimos el texto de la línea
        datosDia.setTextoLinea(tl);
        // Si lineatext está vacío, se sale.
        if (datosDia.getLinea().equals("")) return;
        // Si existe la línea, se sale.
        Linea l = datos.getLinea(datosDia.getLinea());
        if (l != null) return;
        // Se crea una nueva línea.
        l = new Linea();
        l.setLinea(datosDia.getLinea());
        l.setTexto(datosDia.getTextoLinea());
        datos.setLinea(l);
    }

    // AL CAMBIAR INICIO
    public void cambiaInicio(){
        // Validamos los campos
        validarCampos();
        // Si algun inicio, final o turno estan vacíos se sale.
        if (isHorasVacio()) {
            datosDia.setTrabajadas(0);
            datosDia.setAcumuladas(0);
            datosDia.setNocturnas(0);
            datosDia.setDesayuno(false);
            datosDia.setComida(false);
            datosDia.setCena(false);
            return;
        }
        // Si el tipo de incidencia es de calcular horas, se calculan y se escriben.
        if (datosDia.getTipoIncidencia() == 1 || datosDia.getTipoIncidencia() == 2) {
            calcularHoras();
        }
    }

    // AL CAMBIAR FINAL
    public void cambiaFinal(){
        // Validamos los campos
        validarCampos();
        // Si algun inicio, final o turno estan vacíos se sale.
        if (isHorasVacio()) {
            datosDia.setTrabajadas(0);
            datosDia.setAcumuladas(0);
            datosDia.setNocturnas(0);
            datosDia.setDesayuno(false);
            datosDia.setComida(false);
            datosDia.setCena(false);
            return;
        }
        // Si el tipo de incidencia es de calcular horas, se calculan y se escriben.
        if (datosDia.getTipoIncidencia() == 1 || datosDia.getTipoIncidencia() == 2) {
            calcularHoras();
        }
    }

    // AL CAMBIAR MATRICULA RELEVO
    public void cambiaMatriculaRelevo(){
        // Extraemos el texto escrito y lo validamos
        String mt = inputMatriculaRelevo.getText().toString().trim();
        int m;
        try{
            m = Integer.valueOf(mt);
        } catch (NumberFormatException e){
            m = 0;
        }
        // Si la matrícula es cero, salimos.
        if (m == 0){
            datosDia.setMatricula(0);
            datosDia.setCalificacion(0);
            return;
        }
        // Introducimos la matrícula
        datosDia.setMatricula(m);
        // Si no existe el relevo se sale.
        Relevo r = datos.getRelevo(m);
        if (r == null) return;
        // Introducimos los apellidos
        datosDia.setApellidos(r.getApellidos());
        datosDia.setCalificacion(r.getCalificacion());
    }

    // AL CAMBIAR APELLIDOS RELEVO
    public void cambiaApellidosRelevo(){
        // Extraemos el texto escrito
        String a = inputApellidosRelevo.getText().toString().trim();
        // Si está vacío, se sale.
        if (a.equals("")){
            datosDia.setApellidos("");
            return;
        }
        // Introducimos los apellidos
        datosDia.setApellidos(a);
        // Si matrícula está vacío se sale.
        if (datosDia.getMatricula() == 0) return;
        // Si existe el relevo, se sale.
        Relevo r = datos.getRelevo(datosDia.getMatricula());
        if (r != null) return;
        // Creamos el relevo
        r = new Relevo();
        r.setMatricula(datosDia.getMatricula());
        r.setApellidos(datosDia.getApellidos());
        datos.setRelevo(r);
    }

    // AL CAMBIAR MATRICULA SUSTI
    public void cambiaMatriculaSusti(){
        // Extraemos el texto escrito y lo validamos
        String mt = inputMatriculaSusti.getText().toString().trim();
        int m;
        try{
            m = Integer.valueOf(mt);
        } catch (NumberFormatException e){
            m = 0;
        }
        // Si la matrícula es cero, salimos.
        if (m == 0){
            datosDia.setMatriculaSusti(0);
            return;
        }
        datosDia.setMatriculaSusti(m);
        Relevo r = datos.getRelevo(m);
        if (r == null) return;
        datosDia.setApellidosSusti(r.getApellidos());
    }

    // AL CAMBIAR APELLIDOS SUSTI
    public void cambiaApellidosSusti(){
        // Extraemos el texto escrito
        String a = inputApellidosSusti.getText().toString().trim();
        // Si está vacío, se sale.
        if (a.equals("")){
            datosDia.setApellidosSusti("");
            return;
        }
        // Introducimos los apellidos
        datosDia.setApellidosSusti(a);
        // Si matrícula está vacío se sale.
        if (datosDia.getMatriculaSusti() == 0) return;
        // Si existe el relevo, se sale.
        Relevo r = datos.getRelevo(datosDia.getMatriculaSusti());
        if (r != null) return;
        // Creamos el relevo
        r = new Relevo();
        r.setMatricula(datosDia.getMatriculaSusti());
        r.setApellidos(datosDia.getApellidosSusti());
        datos.setRelevo(r);
    }

    // AL CAMBIAR LAS HORAS DE HUELGA
    public void cambiaHorasHuelga(){
        validarCampos();
        // Si la incidencia es la 15:Huelga, calculamos las horas igualmente.
        //if (datosDia.getCodigoIncidencia() == 15){
            calcularHoras();
        //}
    }

    //endregion


    // RELLENAR SEMANA SI ESTA ACTIVADA LA OPCION
    public void rellenarSemana(){

        DatosDia datosDia2 = null;
        Calendar fecha2 = null;
        int dia = 0;
        int mes = 0;
        int año = 0;

        // Definimos la fecha
        fecha2 = Calendar.getInstance();
        fecha2.set(añoActual, mesActual - 1, diaActual);

        // RELLENAMOS
        for(int i = 1; i < 6; i++) {
            fecha2.add(Calendar.DAY_OF_MONTH, 1);
            // Cargamos el día.
            dia = fecha2.get(Calendar.DAY_OF_MONTH);
            mes = fecha2.get(Calendar.MONTH) + 1;
            año = fecha2.get(Calendar.YEAR);
            datosDia2 = datos.servicioDia(dia, mes, año);
            // Si el día no existe, llenamos el mes y volvemos a cargar el día.
            if (datosDia2.getDia() == 0) {
                datos.crearMes(mes, año);
                datosDia2 = datos.servicioDia(dia, mes, año);
            }
            // Replicamos el día, si está vacío.
            if (datosDia2.getCodigoIncidencia() == 0) {
                datosDia2.setCodigoIncidencia(datosDia.getCodigoIncidencia());
                datosDia2.setTextoIncidencia(datosDia.getTextoIncidencia());
                datosDia2.setTipoIncidencia(datosDia.getTipoIncidencia());
                datosDia2.setServicio(datosDia.getServicio());
                datosDia2.setTurno(datosDia.getTurno());
                datosDia2.setLinea(datosDia.getLinea());
                datosDia2.setTextoLinea(datosDia.getTextoLinea());
                datosDia2.setInicio(datosDia.getInicio());
                datosDia2.setFinal(datosDia.getFinal());
                datosDia2.setAcumuladas(datosDia.getAcumuladas());
                datosDia2.setNocturnas(datosDia.getNocturnas());
                datosDia2.setTrabajadas(datosDia.getTrabajadas());
                datosDia2.setDesayuno(datosDia.isDesayuno());
                datosDia2.setComida(datosDia.isComida());
                datosDia2.setCena(datosDia.isCena());
                datosDia2.setMatricula(datosDia.getMatricula());
                datosDia2.setApellidos(datosDia.getApellidos());
                datosDia2.setCalificacion(datosDia.getCalificacion());
                datosDia2.setMatriculaSusti(datosDia.getMatriculaSusti());
                datosDia2.setApellidosSusti(datosDia.getApellidosSusti());
                datosDia2.setBus(datosDia.getBus());
                datosDia2.setNotas(datosDia.getNotas());
                // Guardamos el día.
                datos.guardaDia(datosDia2);
                // Copiamos los servicios del día
                datos.vaciarServiciosDia(dia, mes, año);
                if (cursor.getCount() > 0 && cursor.moveToFirst()){
                    do {
                        ServicioDia sd = new ServicioDia();
                        sd.setDia(dia);
                        sd.setMes(mes);
                        sd.setAño(año);
                        sd.setLinea(cursor.getString(cursor.getColumnIndex("Linea")));
                        sd.setServicio(cursor.getString(cursor.getColumnIndex("Servicio")));
                        sd.setTurno(cursor.getInt(cursor.getColumnIndex("Turno")));
                        sd.setInicio(cursor.getString(cursor.getColumnIndex("Inicio")));
                        sd.setLugarInicio(cursor.getString(cursor.getColumnIndex("LugarInicio")));
                        sd.setFinal(cursor.getString(cursor.getColumnIndex("Final")));
                        sd.setLugarFinal(cursor.getString(cursor.getColumnIndex("LugarFinal")));
                        datos.guardaServicioDia(sd);
                    } while (cursor.moveToNext());
                }
            }
        }
    }


}
