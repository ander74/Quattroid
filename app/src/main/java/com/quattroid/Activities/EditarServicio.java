/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.quattroid.Adapters.AdaptadorServiciosAuxiliares;
import com.quattroid.Models.ServicioAuxiliarModel;

import java.util.ArrayList;

import BaseDatos.BaseDatos;
import BaseDatos.Servicio;
import BaseDatos.ServicioAuxiliar;
import Objetos.Calculos;
import Objetos.Colores;
import Objetos.Hora;

public class EditarServicio extends Activity implements AdapterView.OnItemClickListener,
        View.OnFocusChangeListener,
        View.OnLongClickListener,
        AbsListView.MultiChoiceModeListener {

    // CONSTANTES
    private static final int ACCION_EDITA_SERVICIO_AUXILIAR = 1;
    private final ArrayList<Integer> listaIds = new ArrayList<>();

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    Bundle datosIntent = null;
    AdaptadorServiciosAuxiliares adaptador = null;
    ArrayList<ServicioAuxiliarModel> serviciosAuxiliares = null;
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
    Button botonAddServicioAuxiliar = null;
    Button botonBorrarServicioAuxiliar = null;

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
        botonAddServicioAuxiliar = findViewById(R.id.bt_addServicio);
        botonBorrarServicioAuxiliar = findViewById(R.id.bt_borrarServicio);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);

        // Definimos el vibrador.
        vibrador = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        // Activamos el teclado numérico en los campos habituales.
        if (datos.opciones.isActivarTecladoNumerico()) { //opciones.getBoolean("ActivarTecladoNumerico", false)){
            servicio.setInputType(InputType.TYPE_CLASS_PHONE);
        }

        // Registrar los listeners
        listaServicios.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaServicios.setDivider(null);
        listaServicios.setDividerHeight(0);
        listaServicios.setMultiChoiceModeListener(this);// MULTI-SELECCION
        servicio.setOnFocusChangeListener(this);
        servicio.setOnLongClickListener(this);
        turno.setOnFocusChangeListener(this);
        inicio.setOnFocusChangeListener(this);
        fin.setOnFocusChangeListener(this);
        tomaDeje.setOnFocusChangeListener(this);
        euros.setOnFocusChangeListener(this);
        listaServicios.setOnItemClickListener(this);
        registerForContextMenu(listaServicios);
        botonAddServicioAuxiliar.setOnClickListener(this::botonAddServicioAuxiliarPulsado);
        botonBorrarServicioAuxiliar.setOnClickListener(this::botonBorrarServicioAuxiliarPulsado);

        // Recogemos los datos del intent
        datosIntent = getIntent().getExtras();
        lineatext = datosIntent.getString("Linea");
        if (lineatext == null) lineatext = "";
        id = datosIntent.getInt("Id", -1);

        // Definir si vamos a editar un servicio o crear uno nuevo.
        if (id == -1) {
            //SERVICIO NUEVO
            if (lineatext.equals("")) finish();
            titulo.setText("NUEVO SERVICIO");
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
            titulo.setText("EDITAR SERVICIO");
            serv = datos.getServicio(id);
            servicio.setText(serv.getServicio());
            turno.setText(String.valueOf(serv.getTurno()));
            inicio.setText(serv.getInicio());
            fin.setText(serv.getFinal());
            lugarInicio.setText(serv.getLugarInicio());
            lugarFinal.setText(serv.getLugarFinal());
            tomaDeje.setText(String.valueOf(serv.getTomaDeje()));
            if (serv.getEuros() == 0) {
                euros.setText("");
            } else {
                euros.setText(Hora.textoDecimal(serv.getEuros()));
            }
            bloquearServicio();
        }
        // Ponemos el subtítulo de la ActionBar
        getActionBar().setSubtitle("Línea " + serv.getLinea());
        // Llenar la lista con los servicios auxiliares.
        serviciosAuxiliares = datos.getServiciosAuxiliares(serv.getLinea(), serv.getServicio(), serv.getTurno());
        adaptador = new AdaptadorServiciosAuxiliares(context, serviciosAuxiliares);
        listaServicios.setAdapter(adaptador);
        if (serviciosAuxiliares.isEmpty()) {
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
        int idd = item.getItemId();
        switch (idd) {
            case R.id.bt_guardar:
                Guardar();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // CREAR MENU CONTEXTUAL
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getMenuInflater().inflate(R.menu.contexto_editarservicio, menu);
//    }

    // AL PULSAR UNA OPCION DEL MENU CONTEXTUAL
//    @Override
//    public boolean onContextItemSelected(MenuItem item){
//
//        // Guardar el elemento que ha provocado el menu contextual
//        AdapterView.AdapterContextMenuInfo acmi =
//                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int elementoPulsado = acmi.position;
//
//        // Determinar la opción pulsada
//        switch (item.getItemId()){
//            case R.id.bt_borrar:
//                Cursor c = adaptador.getCursor();
//                datos.borraServicioAuxiliar(c.getInt(c.getColumnIndexOrThrow("_id")));
//                actualizarCursor();
//                return true;
//            case R.id.bt_vaciar:
//                validarCampos();
//                datos.vaciarServiciosAuxiliares(serv.getLinea(), serv.getServicio(), serv.getTurno());
//                actualizarCursor();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (datos.opciones.isGuardarSiempre()) {
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

        switch (v.getId()) {
            case R.id.et_servicio:
                vibrador.vibrate(50);
                if (((EditText) v).getInputType() == android.text.InputType.TYPE_CLASS_TEXT) {
                    ((EditText) v).setInputType(InputType.TYPE_CLASS_PHONE);
                } else {
                    ((EditText) v).setInputType(InputType.TYPE_CLASS_TEXT);
                }
                break;
        }
        return false;
    }

    // AL HACER CLIC EN UN ITEM
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Extraemos los datos del cursor
        ServicioAuxiliarModel servicioPulsado = serviciosAuxiliares.get(position);

        // Creamos un intent para editar el servicio
        Intent intent = new Intent(context, EditarServiciosDia.class);
        intent.putExtra("Linea", servicioPulsado.getLineaAuxiliar());
        intent.putExtra("Servicio", servicioPulsado.getServicioAuxiliar());
        intent.putExtra("Turno", servicioPulsado.getTurnoAuxiliar());
        intent.putExtra("Inicio", servicioPulsado.getInicio());
        intent.putExtra("Final", servicioPulsado.getFinal());
        intent.putExtra("LugarInicio", servicioPulsado.getLugarInicio());
        intent.putExtra("LugarFinal", servicioPulsado.getLugarFinal());
        intent.putExtra("Id", servicioPulsado.getId());
        startActivityForResult(intent, ACCION_EDITA_SERVICIO_AUXILIAR);
    }

    // AL CAMBIAR EL FOCO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) return;
        switch (v.getId()) {
            case R.id.et_servicio:
                servicio.setText(Hora.validarServicio(servicio.getText().toString()));
                break;
            case R.id.et_turno:
                String s = turno.getText().toString().trim();
                switch (s) {
                    case "1":
                    case "01":
                        turno.setText("1");
                        break;
                    case "2":
                    case "02":
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Evaluamos el codigo de petición.
        switch (requestCode) {
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
                    actualizarLista();
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


    //******************************************************************************************
    //region Multi selección

    // MULTI-SELECCION: Al seleccionar un día del calendario.
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        int checkedCount = listaServicios.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selec.");

        if (checked) {
            listaIds.add(position);
            serviciosAuxiliares.get(position).setSeleccionado(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            serviciosAuxiliares.get(position).setSeleccionado(false);
        }

        if (checkedCount > 0) {
            activarBoton(botonBorrarServicioAuxiliar);
            desactivarBoton(botonAddServicioAuxiliar);
        } else {
            activarBoton(botonAddServicioAuxiliar);
            desactivarBoton(botonBorrarServicioAuxiliar);
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
        activarBoton(botonAddServicioAuxiliar);
        desactivarBoton(botonBorrarServicioAuxiliar);
        //Refrescar la lista.
        for (ServicioAuxiliarModel d : serviciosAuxiliares) {
            d.setSeleccionado(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    //endregion
    // ******************************************************************************************


    // ACTUALIZAMOS LA LISTA
    private void actualizarLista() {
        serviciosAuxiliares = datos.getServiciosAuxiliares(serv.getLinea(), serv.getServicio(), serv.getTurno());
        adaptador = new AdaptadorServiciosAuxiliares(this, serviciosAuxiliares);
        listaServicios.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
        Calculos.setAlturaLista(listaServicios);
        if (serviciosAuxiliares.isEmpty()) {
            complementarios.setText("No hay servicios complementarios");
            if (id == -1) {
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
    private void bloquearServicio() {
        servicio.setTextColor(Colores.ROJO_OSCURO);
        servicio.setFocusable(false);
        turno.setTextColor(Colores.ROJO_OSCURO);
        turno.setFocusable(false);
        euros.setNextFocusDownId(R.id.et_inicio);
    }

    // DESBLOQUEAR EL SERVICIO Y EL TURNO
    private void desbloquearServicio() {
        servicio.setTextColor(Colores.NEGRO);
        servicio.setFocusable(true);
        turno.setTextColor(Colores.NEGRO);
        turno.setFocusable(true);
        euros.setNextFocusDownId(R.id.et_servicio);
    }

    // VALIDAR CAMPOS
    private void validarCampos() {
        servicio.setText(Hora.validarServicio(servicio.getText().toString()));
        serv.setServicio(servicio.getText().toString());
        inicio.setText(Hora.horaToString(inicio.getText().toString()));
        serv.setInicio(inicio.getText().toString());
        fin.setText(Hora.horaToString(fin.getText().toString()));
        serv.setFinal(fin.getText().toString());
        String s = turno.getText().toString().trim();
        switch (s) {
            case "1":
            case "01":
                turno.setText("1");
                serv.setTurno(1);
                break;
            case "2":
            case "02":
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
        if (s.equals("")) {
            serv.setTomaDeje("");
        } else {
            serv.setTomaDeje(s);
        }

        s = Hora.validaHoraDecimal(euros.getText().toString().trim());
        if (s.equals("")) {
            serv.setEuros(0d);
        } else {
            serv.setEuros(Double.valueOf(s.replace(",", ".")));
        }

    }


    private void Guardar() {
        validarCampos();
        if (!serv.getServicio().equals("") && serv.getTurno() != 0) {
            datos.setServicio(id, serv);
        }
        setResult(RESULT_OK);
    }


    private void botonAddServicioAuxiliarPulsado(View view) {
        validarCampos();
        if (!serv.getServicio().equals("") && serv.getTurno() != 0) {
            Intent intent = new Intent(context, EditarServiciosDia.class);
            startActivityForResult(intent, ACCION_EDITA_SERVICIO_AUXILIAR);
        }
    }


    private void botonBorrarServicioAuxiliarPulsado(View view) {
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle("ATENCION");
        aviso.setMessage("Vas a borrar los servicios seleccionadas\n\n¿Estás seguro?");
        aviso.setPositiveButton("SI", (dialog, which) -> {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.addAll(listaIds);
            for (int id : ids) {
                ServicioAuxiliarModel servSeleccionado = serviciosAuxiliares.get(id);
                listaServicios.setItemChecked(serviciosAuxiliares.indexOf(servSeleccionado), false);
                datos.borraServicioAuxiliar(servSeleccionado.getId());
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
