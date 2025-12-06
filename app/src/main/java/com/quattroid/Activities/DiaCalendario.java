/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quattroid.Adapters.AdaptadorServiciosDia;
import com.quattroid.Helpers.DiaHelper;
import com.quattroid.Models.ServicioAuxiliarModel;
import com.quattroid.Models.ServicioModel;

import java.util.ArrayList;
import java.util.Calendar;

import BaseDatos.BaseDatos;
import BaseDatos.DatosDia;
import BaseDatos.Linea;
import BaseDatos.Relevo;
import BaseDatos.Servicio;
import BaseDatos.ServicioDia;
import Objetos.Calculos;
import Objetos.Colores;
import Objetos.Hora;

@SuppressLint({"Range", "SetTextI18n", "NonConstantResourceId"})
public class DiaCalendario extends Activity implements View.OnFocusChangeListener,
        AdapterView.OnItemClickListener,
        View.OnLongClickListener,
        View.OnClickListener,
        AbsListView.MultiChoiceModeListener {

    //region DECLARACIONES INICIALES

    // CONSTANTES
    static final int ACCION_LISTA_INCIDENCIA = 1;
    static final int ACCION_SERVICIOS_DIA = 2;
    static final int ACCION_LISTA_RELEVOS = 3;
    static final int ACCION_LISTA_SUSTI = 4;

    static final int TRABAJADAS = 1;
    static final int ACUMULADAS = 2;
    static final int NOCTURNAS = 3;
    private final ArrayList<Integer> listaIds = new ArrayList<>();

    // VARIABLES
    Context context;
    SharedPreferences opciones = null;
    BaseDatos datos = null;
    DatosDia datosDia = null;
    AdaptadorServiciosDia adaptador = null;
    ArrayList<ServicioDia> serviciosDia = null;
    Vibrator vibrador = null;
    int horaSeleccionada = 0;
    boolean hayCambios = false;

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

    Button botonAddServicioDia = null;
    Button botonBorrarServicioDia = null;

    //endregion

    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.calendario);
        // Instanciar las opciones y la base de datos.
        opciones = PreferenceManager.getDefaultSharedPreferences(this);
        datos = new BaseDatos(this);

        if (datos.opciones.isModoBasico()) { //opciones.getBoolean("ModoBasico", false)) {
            setContentView(R.layout.activity_diacalendario_basico);
        } else {
            setContentView(R.layout.activity_diacalendario);
        }

        // Instanciamos los elementos de la activity
        context = this;
        datosDia = new DatosDia();

        // Instanciar los elementos del view
        instanciarElementos();

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
        serviciosDia = datos.getServiciosDia(diaActual, mesActual, añoActual);

        // Crear y vincular el adaptador a la lista.
        adaptador = new AdaptadorServiciosDia(context, serviciosDia);
        listaServiciosDia.setAdapter(adaptador);
        Calculos.setAlturaLista(listaServiciosDia);

        // Registramos los listeners
        listaServiciosDia.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaServiciosDia.setDivider(null);
        listaServiciosDia.setDividerHeight(0);
        listaServiciosDia.setMultiChoiceModeListener(this);// MULTI-SELECCION
        listaServiciosDia.setOnItemClickListener(this);
        registerForContextMenu(listaServiciosDia);
        inputIncidencia.setOnLongClickListener(this);
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
        if (datos.opciones.isActivarTecladoNumerico()) { //opciones.getBoolean("ActivarTecladoNumerico", false)){
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
        checkHuelga.setOnCheckedChangeListener((buttonView, isChecked) -> {
            datosDia.setHuelgaParcial(checkHuelga.isChecked());
            cambiaHorasHuelga();
            rellenarDia();
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
        Intent intent;
        int id = item.getItemId();
        switch (id) {
            case R.id.bt_guardar:
                GuardarDia();
                return true;
//            case R.id.bt_nuevo:
//                hayCambios = true;
//                intent = new Intent(context, EditarServiciosDia.class);
//                startActivityForResult(intent, ACCION_SERVICIOS_DIA);
//                return true;
            case R.id.bt_recalcular:
                DiaHelper.CalcularHorasDia(datosDia, serviciosDia, datos);
                rellenarDia();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // CREAR MENU CONTEXTUAL
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getMenuInflater().inflate(R.menu.contexto_diacalendario, menu);
//    }

    // AL PULSAR UNA OPCION DEL MENU CONTEXTUAL
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        // recuperamos el cursor del adaptador.
//        Cursor c = adaptador.getCursor();
//        // Determinar la opción pulsada
//        switch (item.getItemId()) {
//            case R.id.bt_borrar:
//                hayCambios = true;
//                ServicioDia servicioDia = new ServicioDia();
//                servicioDia.setDia(diaActual);
//                servicioDia.setMes(mesActual);
//                servicioDia.setAño(añoActual);
//                servicioDia.setLinea(c.getString(c.getColumnIndexOrThrow("Linea")));
//                servicioDia.setServicio(c.getString(c.getColumnIndexOrThrow("Servicio")));
//                servicioDia.setTurno(c.getInt(c.getColumnIndexOrThrow("Turno")));
//                datos.borraServicioDia(servicioDia);
//                actualizarCursor();
//                DiaHelper.CalcularHorasDia(datosDia, cursor, datos);
//                escribirHoras();
//                return true;
//            case R.id.bt_vaciar:
//                hayCambios = true;
//                // Si no hay servicios auxiliares, salimos.
//                if (cursor.getCount() == 0) return true;
//                // Creamos el dialogo que preguntará si estamos seguros.
//                AlertDialog.Builder aviso = new AlertDialog.Builder(context);
//                aviso.setTitle("ATENCION");
//                aviso.setMessage("¿Quieres borrar todos los servicios de la lista?");
//                aviso.setPositiveButton("SI", (dialog, which) -> {
//                    // Borramos los servicios
//                    datos.vaciarServiciosDia(diaActual, mesActual, añoActual);
//                    // Actualizamos la lista.
//                    actualizarCursor();
//                    DiaHelper.CalcularHorasDia(datosDia, cursor, datos);
//                    escribirHoras();
//                });
//                aviso.setNegativeButton("NO", (dialog, which) -> {
//                    // No hacemos nada.
//                });
//                aviso.show();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//
//    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (datos.opciones.isGuardarSiempre() && hayCambios) {
                GuardarDia();
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL VOLVER DE UNA SUBACTIVITY
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Evaluamos el codigo de petición.
        switch (requestCode) {
            case ACCION_LISTA_INCIDENCIA:
                if (resultCode == RESULT_OK) {
                    hayCambios = true;
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
                    hayCambios = true;
                    // Extraemos los datos del intent
                    String l = data.getStringExtra("Linea");
                    String s = data.getStringExtra("Servicio");
                    int t = data.getIntExtra("Turno", 0);
                    String i = data.getStringExtra("Inicio");
                    String f = data.getStringExtra("Final");
                    String li = data.getStringExtra("LugarInicio");
                    String lf = data.getStringExtra("LugarFinal");
                    // Evaluamos que los datos no esten vacíos.
                    if (l.equals("") && s.equals("") && t == 0 && i.equals("") && f.equals(""))
                        return;
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
                    if (!data.getBooleanExtra("Nuevo", true)) {
                        datos.borraServicioDia(servicioDia);
                    }
                    datos.guardaServicioDia(servicioDia);
                    actualizarLista();
                    DiaHelper.CalcularHorasDia(datosDia, serviciosDia, datos);
                    escribirHoras();
                }
                break;
            case ACCION_LISTA_RELEVOS:
                if (resultCode == RESULT_OK) {
                    hayCambios = true;
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
                if (resultCode == RESULT_OK) {
                    hayCambios = true;
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

        EditText texto;
        // Si coge el foco
        if (hasFocus) {
            switch (v.getId()) {
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
            switch (v.getId()) {
                case R.id.et_matricula_susti:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaMatriculaSusti)) {
                        hayCambios = true;
                        cambiaMatriculaSusti();
                        cadenaMatriculaSusti = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_apellidos_susti:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaApellidosSusti)) {
                        hayCambios = true;
                        cambiaApellidosSusti();
                        cadenaApellidosSusti = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_linea:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaLinea)) {
                        hayCambios = true;
                        cambiaLinea();
                        cadenaLinea = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_servicio:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaServicio)) {
                        hayCambios = true;
                        cambiaServicioTurno();
                        cadenaServicio = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_turno:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaTurno)) {
                        hayCambios = true;
                        cambiaServicioTurno();
                        cadenaTurno = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_texto_linea:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaTextoLinea)) {
                        hayCambios = true;
                        cambiaTextoLinea();
                        cadenaTextoLinea = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_inicio:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaInicio)) {
                        hayCambios = true;
                        cambiaInicioFinal();
                        cadenaInicio = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_final:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaFinal)) {
                        hayCambios = true;
                        cambiaInicioFinal();
                        cadenaFinal = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_matricula_relevo:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaMatriculaRelevo)) {
                        hayCambios = true;
                        cambiaMatriculaRelevo();
                        cadenaMatriculaRelevo = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_apellidos_relevo:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaApellidosRelevo)) {
                        hayCambios = true;
                        cambiaApellidosRelevo();
                        cadenaApellidosRelevo = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_tomaDeje:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaTomaDeje)) {
                        hayCambios = true;
                        String td = Hora.horaToString(inputTomaDeje.getText().toString().trim());
                        if (td.equals("")) {
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
                    if (!texto.getText().toString().equals(cadenaEuros)) {
                        hayCambios = true;
                        String e = Hora.validaHoraDecimal(inputEuros.getText().toString().trim());
                        if (e.equals("")) {
                            datosDia.setEuros(0d);
                        } else {
                            datosDia.setEuros(Double.parseDouble(e.replace(",", ".")));
                        }
                        cadenaEuros = "";
                        rellenarDia();
                    }
                    break;
                case R.id.et_huelga:
                    texto = (EditText) v;
                    if (!texto.getText().toString().equals(cadenaHorasHuelga)) {
                        hayCambios = true;
                        String e = Hora.validaHoraDecimal(inputHuelga.getText().toString().trim());
                        if (e.equals("")) {
                            datosDia.setHorasHuelga(0d);
                        } else {
                            datosDia.setHorasHuelga(Double.parseDouble(e.replace(",", ".")));
                        }
                        cambiaHorasHuelga();
                        cadenaHorasHuelga = "";
                        rellenarDia();
                    }
                    break;

            }
        }
    }

    // AL HACER CLIC EN UN SERVICIO
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hayCambios = true;

        // Extraemos los datos del cursor
        ServicioDia servicioPulsado = serviciosDia.get(position);

        // Creamos un intent para devolver los datos de la incidencia
        Intent intent = new Intent(context, EditarServiciosDia.class);
        intent.putExtra("Linea", servicioPulsado.getLinea());
        intent.putExtra("Servicio", servicioPulsado.getServicio());
        intent.putExtra("Turno", servicioPulsado.getTurno());
        intent.putExtra("Inicio", servicioPulsado.getInicio());
        intent.putExtra("Final", servicioPulsado.getFinal());
        intent.putExtra("LugarInicio", servicioPulsado.getLugarInicio());
        intent.putExtra("LugarFinal", servicioPulsado.getLugarFinal());
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
        switch (v.getId()) {
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
                if (((EditText) v).getInputType() == android.text.InputType.TYPE_CLASS_TEXT) {
                    ((EditText) v).setInputType(InputType.TYPE_CLASS_PHONE);
                } else {
                    ((EditText) v).setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
            case R.id.tv_incidencia:
                datos.opciones.setModoBasico(!datos.opciones.isModoBasico());
                datos.guardarOpciones();
                finish();
                startActivity(getIntent());
                break;
        }
        return false;
    }

    // AL TOCAR EN UNA HORA O DIETA
    @Override
    public void onClick(View v) {
        hayCambios = true;
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setBackgroundResource(R.drawable.fondo_dialogo);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        dialogo.setPositiveButton("Aceptar", (dialog, which) -> {
            String td = Hora.validaHoraDecimal(input.getText().toString());
            if (!td.equals("")) {
                switch (horaSeleccionada) {
                    case TRABAJADAS:
                        datosDia.setTrabajadas(Double.parseDouble(td.replace(",", ".")));
                        break;
                    case ACUMULADAS:
                        datosDia.setAcumuladas(Double.parseDouble(td.replace(",", ".")));
                        break;
                    case NOCTURNAS:
                        datosDia.setNocturnas(Double.parseDouble(td.replace(",", ".")));
                        break;
                }
                escribirHoras();
            }
        });
        dialogo.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        switch (v.getId()) {
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
    public void botonPulsado(View view) {
        hayCambios = true;
        // Creamos el intent.
        Intent intent = new Intent(context, Incidencias.class);
        // Lanzamos la activity
        startActivityForResult(intent, ACCION_LISTA_INCIDENCIA);
    }


    //******************************************************************************************
    //region Multi selección

    // MULTI-SELECCION: Al seleccionar un día del calendario.
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        int checkedCount = listaServiciosDia.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selec.");

        if (checked) {
            listaIds.add(position);
            serviciosDia.get(position).setSeleccionado(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            serviciosDia.get(position).setSeleccionado(false);
        }

        if (checkedCount > 0) {
            activarBoton(botonBorrarServicioDia);
            desactivarBoton(botonAddServicioDia);
        } else {
            activarBoton(botonAddServicioDia);
            desactivarBoton(botonBorrarServicioDia);
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
        activarBoton(botonAddServicioDia);
        desactivarBoton(botonBorrarServicioDia);
        //Refrescar la lista.
        for (ServicioDia d : serviciosDia) {
            d.setSeleccionado(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    //endregion
    // ******************************************************************************************





    /*
    ----------------------------------------------------------------------------------------------------
    MÉTODOS PRIVADOS
    ----------------------------------------------------------------------------------------------------
    */

    // INSTANCIA TODOS LOS ELEMENTOS DE LA ACTIVITY
    private void instanciarElementos() {
        bloqueSusti = findViewById(R.id.ly_susti);
        bloqueHuelga = findViewById(R.id.ly_huelga);
        bloqueFocus = findViewById(R.id.ly_focus);

        inputIncidencia = findViewById(R.id.tv_incidencia);
        inputMatriculaSusti = findViewById(R.id.et_matricula_susti);
        inputApellidosSusti = findViewById(R.id.et_apellidos_susti);
        inputHuelga = findViewById(R.id.et_huelga);
        inputLinea = findViewById(R.id.et_linea);
        inputServicio = findViewById(R.id.et_servicio);
        inputTurno = findViewById(R.id.et_turno);
        inputTextoLinea = findViewById(R.id.et_texto_linea);
        inputInicio = findViewById(R.id.et_inicio);
        inputFinal = findViewById(R.id.et_final);
        inputBus = findViewById(R.id.et_bus);
        inputMatriculaRelevo = findViewById(R.id.et_matricula_relevo);
        inputApellidosRelevo = findViewById(R.id.et_apellidos_relevo);
        inputNotas = findViewById(R.id.et_notas);
        inputLugarInicio = findViewById(R.id.et_lugarInicio);
        inputLugarFinal = findViewById(R.id.et_lugarFinal);
        inputTomaDeje = findViewById(R.id.et_tomaDeje);
        inputEuros = findViewById(R.id.et_euros);
        checkHuelga = findViewById(R.id.cb_huelga);
        cabeceraComplementarios = findViewById(R.id.tv_complementarios);
        textoTrabajadas = findViewById(R.id.tv_trabajadas);
        textoAcumuladas = findViewById(R.id.tv_acumuladas);
        textoNocturnas = findViewById(R.id.tv_nocturnas);
        listaServiciosDia = findViewById(R.id.listaAuxiliares);
        iconoDesayuno = findViewById(R.id.desayuno);
        iconoComida = findViewById(R.id.comida);
        iconoCena = findViewById(R.id.cena);
        botonAddServicioDia = findViewById(R.id.bt_addServicio);
        botonBorrarServicioDia = findViewById(R.id.bt_borrarServicio);

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

        botonAddServicioDia.setOnClickListener(this::botonAddServicioDiaPulsado);
        botonBorrarServicioDia.setOnClickListener(this::botonBorrarServicioDiaPulsado);
    }

    // ESCRIBE EL DIA EN EL MENU SUPERIOR
    private void escribirTitulo() {
        String s = (diaActual > 9) ? String.valueOf(diaActual) : "0" + diaActual;
        s += " - " + Hora.DIAS_SEMANA[diaSemanaActual];
        String ss = Hora.MESES_MIN[mesActual] + " - " + añoActual;
        getActionBar().setTitle(s);
        getActionBar().setSubtitle(ss);
    }

    // COMPRUEBA QUE LAS HORAS ESTEN VACIAS
    private boolean isHorasVacio() {
        if (datosDia.getInicio().trim().equals("")) {
            return true;
        } else if (datosDia.getFinal().trim().equals("")) {
            return true;
        } else
            return datosDia.getTurno() == 0;
    }

    // ESCRIBIR LAS HORAS EN SUS HUECOS
    private void escribirHoras() {
        String txt;
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
    private void borrarCamposServicio() {
        datosDia.setLinea("");
        datosDia.setTextoLinea("");
        datosDia.setServicio("");
        // Si no se infieren los turnos, el turno desaparece.
        //if (!opciones.getBoolean("InferirTurnos", false)) datosDia.setTurno(0);
        if (!datos.opciones.isInferirTurnos()) datosDia.setTurno(0);
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
    private boolean isServicioVacio() {
        return !datosDia.getLinea().trim().equals("") &&
                !datosDia.getServicio().trim().equals("") &&
                datosDia.getTurno() != 0;
    }

    // ACTUALIZA EL CURSOR CON LOS SERVICIOS DEL DIA
    private void actualizarLista() {
        serviciosDia = datos.getServiciosDia(diaActual, mesActual, añoActual);
        adaptador = new AdaptadorServiciosDia(this, serviciosDia);
        listaServiciosDia.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
        if (serviciosDia.isEmpty()) {
            cabeceraComplementarios.setText("No hay servicios complementarios");
        } else {
            cabeceraComplementarios.setText("Servicios complementarios");
        }
        // Calcular la altura de la lista.
        Calculos.setAlturaLista(listaServiciosDia);
    }

    // RELLENAR TODOS LOS CAMPOS CON LOS DATOS DEL SERVICIO
    public void rellenarDia() {
        // Mostrar el bloque susti si es necesario
        bloqueSusti.setVisibility(View.GONE);
        if (datosDia.getCodigoIncidencia() == 11 || datosDia.getCodigoIncidencia() == 12) {
            // Rellenar el bloque Compañero.
            inputMatriculaSusti.setText((datosDia.getMatriculaSusti() == 0) ? "" :
                    String.valueOf(datosDia.getMatriculaSusti()));
            inputApellidosSusti.setText(datosDia.getApellidosSusti());
            bloqueSusti.setVisibility(View.VISIBLE);
        }
        //Mostrar el bloque huelga si es necesario
        bloqueHuelga.setVisibility(View.GONE);
        if (datosDia.getCodigoIncidencia() == 15) {
            if (datosDia.getHorasHuelga() == 0) {
                inputHuelga.setText("");
            } else {
                inputHuelga.setText(Hora.textoDecimal(datosDia.getHorasHuelga()));
            }
            checkHuelga.setChecked(datosDia.isHuelgaParcial());
            bloqueHuelga.setVisibility(View.VISIBLE);
        }
        // Escribir la incidencia.
        inputIncidencia.setText((datosDia.getTextoIncidencia().equals("")) ? "Incidencia Desconocida" : datosDia.getTextoIncidencia());
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
        if (datosDia.getEuros() == 0) {
            inputEuros.setText("");
        } else {
            inputEuros.setText(Hora.textoDecimal(datosDia.getEuros()));
        }
        // Rellenar el bloque Relevo.
        inputMatriculaRelevo.setText((datosDia.getMatricula() == 0) ? "" :
                String.valueOf(datosDia.getMatricula()));
        inputApellidosRelevo.setText(datosDia.getApellidos());
        // Definir encabezado de Servicios del día.
        if (serviciosDia.isEmpty()) {
            cabeceraComplementarios.setText("No hay servicios complementarios");
        } else {
            cabeceraComplementarios.setText("Servicios complementarios");
        }
        // Rellenar el bloque Horas
        escribirHoras();
        // Rellenar las dietas.
        if (datosDia.isDesayuno()) {
            iconoDesayuno.clearColorFilter();
        } else {
            iconoDesayuno.setColorFilter(Colores.GRIS_OSCURO);
        }
        if (datosDia.isComida()) {
            iconoComida.clearColorFilter();
        } else {
            iconoComida.setColorFilter(Colores.GRIS_OSCURO);
        }
        if (datosDia.isCena()) {
            iconoCena.clearColorFilter();
        } else {
            iconoCena.setColorFilter(Colores.GRIS_OSCURO);
        }
    }

    // VALIDAR LOS CAMPOS DEL VIEW
    public void validarCampos() {
        // LINEA
        String l = inputLinea.getText().toString().trim().toUpperCase();
        if (l.equals("")) {
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
        switch (st) {
            case "1":
            case "01":
                datosDia.setTurno(1);
                break;
            case "2":
            case "02":
                datosDia.setTurno(2);
                break;
            default:
                datosDia.setTurno(0);
        }
        // INICIO
        String i = Hora.horaToString(inputInicio.getText().toString().trim());
        if (i.equals("")) {
            datosDia.setInicio("");
        }
        datosDia.setInicio(i);
        // FINAL
        String f = Hora.horaToString(inputFinal.getText().toString().trim());
        if (f.equals("")) {
            datosDia.setFinal("");
        }
        datosDia.setFinal(f);
        // TOMA Y DEJE
        String td = Hora.horaToString(inputTomaDeje.getText().toString().trim());
        if (td.equals("")) {
            datosDia.setTomaDeje("");
            datosDia.setTomaDejeDecimal(0d);
        } else {
            datosDia.setTomaDeje(td);
            datosDia.setTomaDejeDecimal(Hora.horaToDecimal(td));
        }
        // EUROS
        String e = Hora.validaHoraDecimal(inputEuros.getText().toString().trim());
        if (e.equals("")) {
            datosDia.setEuros(0d);
        } else {
            datosDia.setEuros(Double.parseDouble(e.replace(",", ".")));
        }
        // HORAS HUELGA
        String hh = Hora.validaHoraDecimal(inputHuelga.getText().toString().trim());
        if (hh.equals("")) {
            datosDia.setHorasHuelga(0d);
        } else {
            datosDia.setHorasHuelga(Double.parseDouble(hh.replace(",", ".")));
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
    public void regularAño() {
        // Declaramos las variables.
        double jAnual = datos.opciones.getJornadaAnual();
        double jMedia = datos.opciones.getJornadaMedia();
        // Si es el día 31 de diciembre...
        if (datosDia.getDia() == 31 && datosDia.getMes() == 12 && datos.opciones.isRegularJornadaAnual()) {
            double trabajadas = datos.diasTrabajadosConvenio(datosDia.getAño()) * jMedia;
            trabajadas += datos.diasEnfermoComputables(datosDia.getAño()) * jMedia;
            double diff = trabajadas - jAnual;
            datos.setAjenaFinAño(diff, datosDia.getAño());
        }
        // Si es el día 29 de febrero
        if (datosDia.getDia() == 29 && datosDia.getMes() == 2 && datos.opciones.isRegularBisiestos()) {
            double jBisiestos = jAnual / 365 * 366;
            double diff = Hora.redondeaDecimal4(jAnual - jBisiestos);
            datos.setAjenaBisiestos(diff, datosDia.getAño());
        }
    }

    // RELLENAR SEMANA SI ESTA ACTIVADA LA OPCION
    public void rellenarSemana() {
        DatosDia datosDia2;
        Calendar fecha2;
        int dia;
        int mes;
        int año;
        // Definimos la fecha
        fecha2 = Calendar.getInstance();
        fecha2.set(añoActual, mesActual - 1, diaActual);
        // RELLENAMOS
        for (int i = 1; i < 6; i++) {
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
                datosDia2.setLugarInicio(datosDia.getLugarInicio());
                datosDia2.setFinal(datosDia.getFinal());
                datosDia2.setLugarFinal(datosDia.getLugarFinal());
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
                if (!serviciosDia.isEmpty()) {
                    for (ServicioDia serv : serviciosDia) {
                        ServicioDia newServ = new ServicioDia();
                        newServ.setDia(dia);
                        newServ.setMes(mes);
                        newServ.setAño(año);
                        newServ.setLinea(serv.getLinea());
                        newServ.setServicio(serv.getServicio());
                        newServ.setTurno(serv.getTurno());
                        newServ.setInicio(serv.getInicio());
                        newServ.setLugarInicio(serv.getLugarInicio());
                        newServ.setFinal(serv.getFinal());
                        newServ.setLugarFinal(serv.getLugarFinal());
                        datos.guardaServicioDia(newServ);
                    }
                }
            }
        }
    }

    // GUARDA EL DÍA EN LA BASE DE DATOS
    private void GuardarDia() {
        validarCampos();
        if (datosDia.getMatricula() != 0) {
            datosDia.setCalificacion(datos.calificacionRelevo(datosDia.getMatricula()));
        }
        if (datos.guardaDia(datosDia)) {
            if (datos.opciones.isRellenarSemana() && datosDia.getDiaSemana() == 2) {
                rellenarSemana();
            }
            regularAño();
            if (datosDia.isServicioCompleto()) {
                Servicio servicioDia = datos.getServicio(datosDia.getLinea(), datosDia.getServicio(), datosDia.getTurno());
                // Si el servicio no existe, lo creamos.
                if (servicioDia == null) {
                    ServicioModel nuevoServicio = new ServicioModel();
                    nuevoServicio.setLinea(datosDia.getLinea());
                    nuevoServicio.setServicio(datosDia.getServicio());
                    nuevoServicio.setTurno(datosDia.getTurno());
                    nuevoServicio.setInicio(datosDia.getInicio());
                    nuevoServicio.setFinal(datosDia.getFinal());
                    nuevoServicio.setLugarInicio(datosDia.getLugarInicio());
                    nuevoServicio.setLugarFinal(datosDia.getLugarFinal());
                    nuevoServicio.setTomaDeje(datosDia.getTomaDeje());
                    nuevoServicio.setEuros(datosDia.getEuros());
                    if (!serviciosDia.isEmpty()) {
                        nuevoServicio.setServiciosAuxiliares(new ArrayList<>());
                        for (ServicioDia serv : serviciosDia) {
                            ServicioAuxiliarModel newServ = new ServicioAuxiliarModel();
                            newServ.setLinea(serv.getLinea());
                            newServ.setServicio(serv.getServicio());
                            newServ.setTurno(serv.getTurno());
                            newServ.setInicio(serv.getInicio());
                            newServ.setLugarInicio(serv.getLugarInicio());
                            newServ.setFinal(serv.getFinal());
                            newServ.setLugarFinal(serv.getLugarFinal());
                            nuevoServicio.getServiciosAuxiliares().add(newServ);
                        }
                    }
                    ArrayList<ServicioModel> lista = new ArrayList<ServicioModel>();
                    lista.add(nuevoServicio);
                    datos.guardarServicios(lista);
                }
            }
            Toast.makeText(this, R.string.mensaje_diaGuardado, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("Posicion", posicion);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, R.string.error_diaGuardado, Toast.LENGTH_SHORT).show();
        }
    }

    /*
    ----------------------------------------------------------------------------------------------------
    ACCIONES AL CAMBIAR EL FOCO DE UN CAMPO CONCRETO
    ----------------------------------------------------------------------------------------------------
    */

    // AL CAMBIAR INCIDENCIA
    public void cambiaIncidencia() {
        // Si el código de la incidencia es -1 salimos
        if (datosDia.getCodigoIncidencia() == -1) {
            datosDia.setCodigoIncidencia(0);
            return;
        }
        // Si el código de la incidencia es cero, repetimos el día anterior.
        if (datosDia.getCodigoIncidencia() == 0) {
            // Evaluamos que el día no sea el primer día que se muestra.
            int primerMes = datos.opciones.getPrimerMes();
            int primerAño = datos.opciones.getPrimerAño();
            int dia = datosDia.getDia();
            int mes = datosDia.getMes();
            int año = datosDia.getAño();
            int diaOriginal = dia;
            int mesOriginal = mes;
            int añoOriginal = año;
            int diaSemanaOriginal = datosDia.getDiaSemana();
            if (dia == 1 && mes == primerMes && año == primerAño) {
                // No se puede repetir el día
                Toast.makeText(this, R.string.mensaje_primerDia, Toast.LENGTH_SHORT).show();
            } else {
                // Evaluamos qué dia es el anterior al día que tenemos.
                dia--;
                if (dia == 0) {
                    mes--;
                    if (mes == 0) {
                        mes = 12;
                        año--;
                    }
                    Calendar fecha = Calendar.getInstance();
                    fecha.set(año, mes - 1, 1);
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
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    ServicioDia sd = new ServicioDia();
                    sd.setDia(diaOriginal);
                    sd.setMes(mesOriginal);
                    sd.setAño(añoOriginal);
                    sd.setLinea(c.getString(c.getColumnIndexOrThrow("Linea")));
                    sd.setServicio(c.getString(c.getColumnIndexOrThrow("Servicio")));
                    sd.setTurno(c.getInt(c.getColumnIndexOrThrow("Turno")));
                    sd.setInicio(c.getString(c.getColumnIndexOrThrow("Inicio")));
                    sd.setFinal(c.getString(c.getColumnIndexOrThrow("Final")));
                    sd.setLugarInicio(c.getString(c.getColumnIndexOrThrow("LugarInicio")));
                    sd.setLugarFinal(c.getString(c.getColumnIndexOrThrow("LugarFinal")));
                    datos.guardaServicioDia(sd);
                    actualizarLista();
                }
            }
            c.close();
            rellenarDia();
            return;
        }
        // Evaluamos el tipo de incidencia
        switch (datosDia.getTipoIncidencia()) {
            // TRABAJO Y FRANQUEO A TRABAJAR
            case 1:
            case 2:
                if (isHorasVacio()) {
                    rellenarDia();
                    return;
                }
                DiaHelper.CalcularHorasDia(datosDia, serviciosDia, datos);
                rellenarDia();
                break;
            // FIESTA POR OTRO DIA
            case 3:
                borrarCamposServicio();
                datosDia.setTrabajadas(0);
                datosDia.setAcumuladas(-datos.opciones.getJornadaMedia());
                datosDia.setNocturnas(0);
                inputBus.setText("");
                rellenarDia();
                actualizarLista();
                break;
            // FRANQUEO
            case 4:
                borrarCamposServicio();
                datosDia.setTrabajadas(0);
                datosDia.setAcumuladas(0);
                datosDia.setNocturnas(0);
                inputBus.setText("");
                rellenarDia();
                actualizarLista();
                break;
            // TRABAJO SIN ACUMULAR
            case 5:
                datosDia.setTrabajadas(0);
                datosDia.setAcumuladas(0);
                datosDia.setNocturnas(0);
                if (datosDia.isHuelgaParcial())
                    DiaHelper.CalcularHorasDia(datosDia, serviciosDia, datos);
                rellenarDia();
                break;
            // JORNADA MEDIA
            case 6:
                datosDia.setTrabajadas(0);
                datosDia.setTrabajadas(datos.opciones.getJornadaMedia());
                datosDia.setAcumuladas(0);
                datosDia.setNocturnas(0);
                inputBus.setText("");
                rellenarDia();
                actualizarLista();
                break;
        }
    }

    // AL CAMBIAR LINEA
    private void cambiaLinea() {
        // Validamos los campos
        validarCampos();
        // Si la línea existe, ponemos el texto.
        Linea linea = datos.getLinea(datosDia.getLinea());
        if (linea != null) {
            datosDia.setTextoLinea(linea.getTexto());
        }
        // Si algún dato del servicio falta, se sale.
        if (isServicioVacio()) {
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
                        sd.setLinea(c.getString(c.getColumnIndexOrThrow("LineaAuxiliar")));
                        sd.setServicio(c.getString(c.getColumnIndexOrThrow("ServicioAuxiliar")));
                        sd.setTurno(c.getInt(c.getColumnIndexOrThrow("TurnoAuxiliar")));
                        sd.setInicio(c.getString(c.getColumnIndexOrThrow("Inicio")));
                        sd.setFinal(c.getString(c.getColumnIndexOrThrow("Final")));
                        sd.setLugarInicio(c.getString(c.getColumnIndexOrThrow("LugarInicio")));
                        sd.setLugarFinal(c.getString(c.getColumnIndexOrThrow("LugarFinal")));
                        datos.guardaServicioDia(sd);
                    }
                }
                actualizarLista();
            }
            // Añadimos el relevo, si no está metido.
            if (datosDia.getMatricula() == 0 && datos.opciones.getRelevoFijo() != 0) {
                // Introducimos la matrícula
                datosDia.setMatricula(datos.opciones.getRelevoFijo());
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
                DiaHelper.CalcularHorasDia(datosDia, serviciosDia, datos);
            }
        }
    }

    // AL CAMBIAR SERVICIO O TURNO
    public void cambiaServicioTurno() {
        // Validamos los campos
        validarCampos();
        // Si algún dato del servicio falta, se sale.
        if (isServicioVacio()) {
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
                        sd.setLinea(c.getString(c.getColumnIndexOrThrow("LineaAuxiliar")));
                        sd.setServicio(c.getString(c.getColumnIndexOrThrow("ServicioAuxiliar")));
                        sd.setTurno(c.getInt(c.getColumnIndexOrThrow("TurnoAuxiliar")));
                        sd.setInicio(c.getString(c.getColumnIndexOrThrow("Inicio")));
                        sd.setFinal(c.getString(c.getColumnIndexOrThrow("Final")));
                        sd.setLugarInicio(c.getString(c.getColumnIndexOrThrow("LugarInicio")));
                        sd.setLugarFinal(c.getString(c.getColumnIndexOrThrow("LugarFinal")));
                        datos.guardaServicioDia(sd);
                    }
                }
                actualizarLista();
            }
            // Añadimos el relevo, si no está metido.
            if (datosDia.getMatricula() == 0 && datos.opciones.getRelevoFijo() != 0) {
                // Introducimos la matrícula
                datosDia.setMatricula(datos.opciones.getRelevoFijo());
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
                DiaHelper.CalcularHorasDia(datosDia, serviciosDia, datos);
            }
        }
    }

    // AL CAMBIAR TEXTO LINEA
    public void cambiaTextoLinea() {
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

    // AL CAMBIAR INICIO O FINAL
    public void cambiaInicioFinal() {
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
            DiaHelper.CalcularHorasDia(datosDia, serviciosDia, datos);
        }
    }

    // AL CAMBIAR MATRICULA RELEVO
    public void cambiaMatriculaRelevo() {
        // Extraemos el texto escrito y lo validamos
        String mt = inputMatriculaRelevo.getText().toString().trim();
        int m;
        try {
            m = Integer.parseInt(mt);
        } catch (NumberFormatException e) {
            m = 0;
        }
        // Si la matrícula es cero, salimos.
        if (m == 0) {
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
    public void cambiaApellidosRelevo() {
        // Extraemos el texto escrito
        String a = inputApellidosRelevo.getText().toString().trim();
        // Si está vacío, se sale.
        if (a.equals("")) {
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
    public void cambiaMatriculaSusti() {
        // Extraemos el texto escrito y lo validamos
        String mt = inputMatriculaSusti.getText().toString().trim();
        int m;
        try {
            m = Integer.parseInt(mt);
        } catch (NumberFormatException e) {
            m = 0;
        }
        // Si la matrícula es cero, salimos.
        if (m == 0) {
            datosDia.setMatriculaSusti(0);
            return;
        }
        datosDia.setMatriculaSusti(m);
        Relevo r = datos.getRelevo(m);
        if (r == null) return;
        datosDia.setApellidosSusti(r.getApellidos());
    }

    // AL CAMBIAR APELLIDOS SUSTI
    public void cambiaApellidosSusti() {
        // Extraemos el texto escrito
        String a = inputApellidosSusti.getText().toString().trim();
        // Si está vacío, se sale.
        if (a.equals("")) {
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
    public void cambiaHorasHuelga() {
        validarCampos();
        DiaHelper.CalcularHorasDia(datosDia, serviciosDia, datos);
    }


    private void botonAddServicioDiaPulsado(View view) {
        hayCambios = true;
        Intent intent = new Intent(context, EditarServiciosDia.class);
        startActivityForResult(intent, ACCION_SERVICIOS_DIA);
    }


    private void botonBorrarServicioDiaPulsado(View view) {
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle("ATENCION");
        aviso.setMessage("Vas a borrar los servicios seleccionadas\n\n¿Estás seguro?");
        aviso.setPositiveButton("SI", (dialog, which) -> {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.addAll(listaIds);
            for (int id : ids) {
                ServicioDia servSeleccionado = serviciosDia.get(id);
                listaServiciosDia.setItemChecked(serviciosDia.indexOf(servSeleccionado), false);
                datos.borraServicioDia(servSeleccionado);
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
