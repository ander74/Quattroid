/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.quattroid.Activities.databinding.ActivityBusquedasBinding;
import com.quattroid.Adapters.AdaptadorBusquedas;

import java.util.Calendar;

import BaseDatos.BaseDatos;
import Objetos.Hora;

public class Busquedas extends Activity implements View.OnFocusChangeListener, View.OnLongClickListener, AdapterView.OnItemClickListener {

    // CONSTANTES
    private final int BOTONES = 0;
    private final int POR_SERVICIO = 1;
    private final int POR_MATRICULA = 2;
    private final int POR_INCIDENCIA = 3;
    private final int POR_BUS = 4;
    private final int POR_NOTAS = 5;

    private final int ACCION_INCIDENCIA = 1;
    private final int ACCION_DIA_CALENDARIO = 2;
    private final int ACCION_EDITA_AJENA = 3;
    private final int ACCION_EDITA_RELEVO = 4;
    private final int ACCION_LISTA_RELEVOS = 5;

    // VARIABLES
    private ActivityBusquedasBinding binding;
    Context context = null;
    BaseDatos datos = null;
    Cursor cursor = null;
    AdaptadorBusquedas adaptador = null;
    int grupoActivo = 0;
    String where = "";
    Vibrator vibrador = null;

    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusquedasBinding.inflate(getLayoutInflater());
        getActionBar().setLogo(R.drawable.busquedas);
        getActionBar().setTitle("Busquedas");
        getActionBar().setSubtitle(null);
        setContentView(binding.getRoot());

        // Inicialización de los elementos
        context = this;

        // Ocultar los bloques inicialmente
        binding.lyPorServicio.setVisibility(View.GONE);
        binding.lyPorMatricula.setVisibility(View.GONE);
        binding.lyVerListado.setVisibility(View.GONE);
        binding.lyPorBus.setVisibility(View.GONE);
        binding.lyPorNotas.setVisibility(View.GONE);
        binding.lwListaBusquedas.setVisibility(View.GONE);

        // Registrar los listeners
        binding.etServicio.setOnFocusChangeListener(this);
        binding.etTurno.setOnFocusChangeListener(this);
        binding.etMatricula.setOnFocusChangeListener(this);
        binding.etMatricula.setOnLongClickListener(this);
        binding.lwListaBusquedas.setOnItemClickListener(this);
        binding.etServicio.setOnLongClickListener(this);
        binding.etLinea.setOnLongClickListener(this);

        // Iniciar la base de datos
        datos = new BaseDatos(this);

        // Definimos el vibrador.
        vibrador = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        // Activamos el teclado numérico en los campos habituales.
        if (datos.opciones.isActivarTecladoNumerico()) {
            binding.etLinea.setInputType(InputType.TYPE_CLASS_PHONE);
            binding.etServicio.setInputType(InputType.TYPE_CLASS_PHONE);
        }

        // Definir el picker del año
        binding.npAO.setMinValue(2000);
        binding.npAO.setMaxValue(2050);
        binding.npAO.setWrapSelectorWheel(false);
        Calendar ahora = Calendar.getInstance();
        binding.npAO.setValue(ahora.get(Calendar.YEAR));
        binding.npAO.setOnValueChangedListener((picker, oldValue, newValue) -> binding.chLimitarAO.setChecked(true));


        // Iniciar el adaptador y el cursor
        cursor = datos.cursorBusqueda("Dia=0");
        adaptador = new AdaptadorBusquedas(context, cursor);

        // Vincular la lista con el adaptador
        binding.lwListaBusquedas.setAdapter(adaptador);

    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (grupoActivo == BOTONES) {
                finish();
            } else {
                binding.lyPorServicio.setVisibility(View.GONE);
                binding.lyPorMatricula.setVisibility(View.GONE);
                binding.lyVerListado.setVisibility(View.GONE);
                binding.lyPorBus.setVisibility(View.GONE);
                binding.lyPorNotas.setVisibility(View.GONE);
                binding.lwListaBusquedas.setVisibility(View.GONE);
                binding.lyBotones.setVisibility(View.VISIBLE);
                grupoActivo = BOTONES;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL PULSAR UN BOTON
    public void botonPulsado(View v) {
        switch (v.getId()) {
            case R.id.bt_servicio:
                grupoActivo = POR_SERVICIO;
                binding.lyPorServicio.setVisibility(View.VISIBLE);
                binding.lyPorMatricula.setVisibility(View.GONE);
                binding.lyPorBus.setVisibility(View.GONE);
                binding.lyVerListado.setVisibility(View.VISIBLE);
                binding.lwListaBusquedas.setVisibility(View.VISIBLE);
                binding.lyBotones.setVisibility(View.GONE);
                cursor = datos.cursorBusqueda("Dia=0");
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case R.id.bt_matricula:
                grupoActivo = POR_MATRICULA;
                binding.lyPorServicio.setVisibility(View.GONE);
                binding.lyPorBus.setVisibility(View.GONE);
                binding.lyPorMatricula.setVisibility(View.VISIBLE);
                binding.lyVerListado.setVisibility(View.VISIBLE);
                binding.lwListaBusquedas.setVisibility(View.VISIBLE);
                binding.lyBotones.setVisibility(View.GONE);
                cursor = datos.cursorBusqueda("Dia=0");
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case R.id.bt_incidencia:
                grupoActivo = POR_INCIDENCIA;
                Intent intent = new Intent(context, Incidencias.class);
                startActivityForResult(intent, ACCION_INCIDENCIA);
                break;
            case R.id.bt_bus:
                grupoActivo = POR_BUS;
                binding.lyPorServicio.setVisibility(View.GONE);
                binding.lyPorMatricula.setVisibility(View.GONE);
                binding.lyPorBus.setVisibility(View.VISIBLE);
                binding.lyVerListado.setVisibility(View.VISIBLE);
                binding.lwListaBusquedas.setVisibility(View.VISIBLE);
                binding.lyBotones.setVisibility(View.GONE);
                cursor = datos.cursorBusqueda("Dia=0");
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case R.id.bt_notas:
                grupoActivo = POR_NOTAS;
                binding.lyPorServicio.setVisibility(View.GONE);
                binding.lyPorMatricula.setVisibility(View.GONE);
                binding.lyPorBus.setVisibility(View.GONE);
                binding.lyPorNotas.setVisibility(View.VISIBLE);
                binding.lyVerListado.setVisibility(View.VISIBLE);
                binding.lwListaBusquedas.setVisibility(View.VISIBLE);
                binding.lyBotones.setVisibility(View.GONE);
                cursor = datos.cursorBusqueda("Dia=0");
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case R.id.bt_verlistado:
                verLista();
                break;
        }
    }

    // AL CAMBIAR EL FOCO UN CAMPO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.et_turno:
                    String s = binding.etTurno.getText().toString().trim();
                    switch (s) {
                        case "1":
                        case "2":
                            binding.etTurno.setText(s);
                            break;
                        default:
                            binding.etTurno.setText("");
                    }
                    break;
                case R.id.et_servicio:
                    binding.etServicio.setText(Hora.validarServicio(binding.etServicio.getText().toString()));
                    break;
                case R.id.et_matricula:
                    int i = -1;
                    try {
                        i = Integer.valueOf(binding.etMatricula.getText().toString());
                    } catch (NumberFormatException e) {
                        i = -1;
                    }
                    if (i == -1) {
                        binding.etMatricula.setText("");
                    }
                    break;
            }
        }
    }


    // AL HACER UN CLICK LARGO EN LA MATRÍCULA


    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.et_matricula:
                // Creamos el intent.
                Intent intent = new Intent(context, Relevos.class);
                // Lanzamos la activity
                startActivityForResult(intent, ACCION_LISTA_RELEVOS);
                break;
            case R.id.et_linea:
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

    // AL PULSAR EL BOTON VER LISTADO
    public void verLista() {
        int añoLimite = 0;
        try {
            añoLimite = binding.npAO.getValue();
            if (añoLimite < 2000 || añoLimite > 2050) añoLimite = 0;
        } catch (NumberFormatException e) {
            añoLimite = 0;
        }
        switch (grupoActivo) {
            case POR_SERVICIO:
                boolean y = false;
                where = "";
                if (!binding.etLinea.getText().toString().trim().equals("")) {
                    where = "Linea LIKE '" + binding.etLinea.getText().toString().trim() + "' ";
                    y = true;
                }
                if (!binding.etServicio.getText().toString().trim().equals("")) {
                    if (y) {
                        where += "AND ";
                    }
                    where += "Servicio LIKE '" + binding.etServicio.getText().toString().trim() + "' ";
                    y = true;
                }
                if (!binding.etTurno.getText().toString().trim().equals("")) {
                    if (y) {
                        where += "AND ";
                    }
                    where += "Turno=" + binding.etTurno.getText().toString().trim();
                }
                if (where.equals("")) where = "Dia=0";
                if (binding.chLimitarAO.isChecked() && añoLimite != 0)
                    where += " AND Año=" + añoLimite;
                cursor = datos.cursorBusqueda(where);
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case POR_MATRICULA:
                if (binding.etMatricula.getText().toString().trim().equals("")) {
                    if (binding.chDeuda.isChecked()) {
                        where = "CodigoIncidencia=11 OR CodigoIncidencia=12";
                    } else {
                        where = "Dia=0";
                    }
                } else {
                    if (binding.chDeuda.isChecked()) {
                        where = "(MatriculaSusti=" + binding.etMatricula.getText().toString().trim() +
                                " AND CodigoIncidencia=11) OR" +
                                "(MatriculaSusti=" + binding.etMatricula.getText().toString().trim() +
                                " AND CodigoIncidencia=12)";
                    } else {
                        where = "Matricula=" + binding.etMatricula.getText().toString().trim();
                    }
                }
                if (binding.chLimitarAO.isChecked() && añoLimite != 0)
                    where += " AND Año=" + añoLimite;
                cursor = datos.cursorBusqueda(where);
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case POR_BUS:
                if (binding.etBus.getText().toString().trim().equals("")) break;
                where = "Bus='" + binding.etBus.getText().toString().trim() + "' ";
                if (binding.chLimitarAO.isChecked() && añoLimite != 0)
                    where += " AND Año=" + añoLimite;
                cursor = datos.cursorBusqueda(where);
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
            case POR_NOTAS:
                if (binding.etNotas.getText().toString().trim().equals("")) break;
                where = "Notas LIKE '%" + binding.etNotas.getText().toString().trim() + "%'";
                if (binding.chLimitarAO.isChecked() && añoLimite != 0)
                    where += " AND Año=" + añoLimite;
                cursor = datos.cursorBusqueda(where);
                adaptador.changeCursor(cursor);
                adaptador.notifyDataSetChanged();
                break;
        }

        // Ocultar el teclado.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.etLinea.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etServicio.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etTurno.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etMatricula.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etBus.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etNotas.getWindowToken(), 0);


    }

    // AL RECIBIR UN RESULTADO DE OTRA ACTIVITY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACCION_INCIDENCIA:
                    int añoLimite = 0;
                    try {
                        añoLimite = binding.npAO.getValue();
                        if (añoLimite < 2000 || añoLimite > 2050) añoLimite = 0;
                    } catch (NumberFormatException e) {
                        añoLimite = 0;
                    }
                    int codigo = data.getIntExtra("codigo", 0);
                    where = "CodigoIncidencia=" + codigo;
                    if (binding.chLimitarAO.isChecked() && añoLimite != 0)
                        where += " AND Año=" + añoLimite;
                    cursor = datos.cursorBusqueda(where);
                    adaptador.changeCursor(cursor);
                    adaptador.notifyDataSetChanged();
                    binding.lyPorServicio.setVisibility(View.GONE);
                    binding.lyPorMatricula.setVisibility(View.GONE);
                    binding.lyPorBus.setVisibility(View.GONE);
                    binding.lyVerListado.setVisibility(View.VISIBLE);
                    binding.lwListaBusquedas.setVisibility(View.VISIBLE);
                    binding.lyBotones.setVisibility(View.GONE);
                    break;
                case ACCION_LISTA_RELEVOS:
                    binding.etMatricula.setText(data.getStringExtra("matricula"));
                    break;
            }
        }
        if (resultCode == RESULT_CANCELED) {
            if (requestCode == ACCION_INCIDENCIA) {
                grupoActivo = BOTONES;
                binding.lyPorServicio.setVisibility(View.GONE);
                binding.lyPorMatricula.setVisibility(View.GONE);
                binding.lyVerListado.setVisibility(View.GONE);
                binding.lyPorBus.setVisibility(View.GONE);
                binding.lwListaBusquedas.setVisibility(View.GONE);
                binding.lyBotones.setVisibility(View.VISIBLE);
            }
        }
    }

    // AL PULSAR UN ITEM DE LA LISTA
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cursor.moveToPosition(position);
        Intent intent = new Intent(context, Calendario.class);
        intent.putExtra("Año", cursor.getInt(cursor.getColumnIndexOrThrow("Año")));
        intent.putExtra("Mes", cursor.getInt(cursor.getColumnIndexOrThrow("Mes")));
        intent.putExtra("Dia", cursor.getInt(cursor.getColumnIndexOrThrow("Dia")));
        startActivityForResult(intent, ACCION_DIA_CALENDARIO);
    }

    @Override
    protected void onDestroy() {
        cursor.close();
        datos.close();
        super.onDestroy();
    }
}
