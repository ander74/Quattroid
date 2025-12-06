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
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.quattroid.Adapters.AdaptadorHorasAjenas;

import java.util.ArrayList;

import BaseDatos.BaseDatos;
import BaseDatos.HoraAjena;

public class HorasAjenas extends Activity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    // CONSTANTES
    final static int ACCION_EDITA_AJENA = 1;
    final static int ACCION_NUEVA_AJENA = 2;
    private final ArrayList<Integer> listaIds = new ArrayList<>();

    // VARIABLES
    Context context;
    AdaptadorHorasAjenas adaptador = null;
    ArrayList<HoraAjena> horasAjenas = new ArrayList<>();
    BaseDatos datos = null;

    // ELEMENTOS DEL VIEW
    ListView listaAjenas = null;
    Button botonAddHoraAjena = null;
    Button botonBorrarHoraAjena = null;


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
        listaAjenas = findViewById(R.id.lw_ajenas);
        botonAddHoraAjena = findViewById(R.id.bt_addHoraAjena);
        botonBorrarHoraAjena = findViewById(R.id.bt_borrarHoraAjena);

        datos = new BaseDatos(context);
        horasAjenas = datos.getHorasAjenas();
        adaptador = new AdaptadorHorasAjenas(context, horasAjenas);
        listaAjenas.setAdapter(adaptador);

        // Registrar los listeners
        listaAjenas.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaAjenas.setDivider(null);
        listaAjenas.setDividerHeight(0);
        listaAjenas.setMultiChoiceModeListener(this);// MULTI-SELECCION
        listaAjenas.setOnItemClickListener(this);
        registerForContextMenu(listaAjenas);
        botonAddHoraAjena.setOnClickListener(this::botonAddHoraAjenaPulsado);
        botonBorrarHoraAjena.setOnClickListener(this::botonBorrarHoraAjenaPulsado);

    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        // Extraemos los datos del cursor
        HoraAjena ajena = horasAjenas.get(position);

        // Creamos un intent para devolver los datos de la incidencia
        Intent intent = new Intent(context, EditarHorasAjenas.class);
        intent.putExtra("Id", ajena.getId());
        intent.putExtra("Dia", ajena.getDia());
        intent.putExtra("Mes", ajena.getMes());
        intent.putExtra("Año", ajena.getAño());
        intent.putExtra("Horas", ajena.getHoras());
        intent.putExtra("Motivo", ajena.getMotivo());
        intent.putExtra("Nuevo", false);
        startActivityForResult(intent, ACCION_EDITA_AJENA);

    }

    // AL VOLVER DE UNA PANTALLA
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Evaluamos el codigo de petición.
        switch (requestCode) {
            case ACCION_EDITA_AJENA:
                if (resultCode == RESULT_OK) {
                    HoraAjena horaAjena = new HoraAjena();
                    horaAjena.setDia(data.getIntExtra("Dia", 0));
                    horaAjena.setMes(data.getIntExtra("Mes", 0));
                    horaAjena.setAño(data.getIntExtra("Año", 0));
                    horaAjena.setHoras(data.getDoubleExtra("Horas", 0));
                    horaAjena.setMotivo(data.getStringExtra("Motivo"));
                    if (data.getBooleanExtra("Editar", false)) {
                        datos.borrarAjena(data.getIntExtra("Id", -1));
                    }
                    datos.setAjena(horaAjena);
                    actualizarLista();
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
                    actualizarLista();
                }
                break;
            default:
                break;
        }
    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
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


    //******************************************************************************************
    //region Multi selección

    // MULTI-SELECCION: Al seleccionar un día del calendario.
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        int checkedCount = listaAjenas.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selec.");

        if (checked) {
            listaIds.add(position);
            horasAjenas.get(position).setSeleccionada(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            horasAjenas.get(position).setSeleccionada(false);
        }

        if (checkedCount > 0) {
            activarBoton(botonBorrarHoraAjena);
            desactivarBoton(botonAddHoraAjena);
        } else {
            activarBoton(botonAddHoraAjena);
            desactivarBoton(botonBorrarHoraAjena);
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
        activarBoton(botonAddHoraAjena);
        desactivarBoton(botonBorrarHoraAjena);
        //Refrescar la lista.
        for (HoraAjena d : horasAjenas) {
            d.setSeleccionada(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    //endregion
    // ******************************************************************************************


    // ACTUALIZA LA LISTA
    private void actualizarLista() {
        horasAjenas = datos.getHorasAjenas();
        adaptador = new AdaptadorHorasAjenas(this, horasAjenas);
        listaAjenas.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }

    private void botonAddHoraAjenaPulsado(View view) {
        Intent intent = new Intent(context, EditarHorasAjenas.class);
        intent.putExtra("Id", -1);
        startActivityForResult(intent, ACCION_NUEVA_AJENA);
    }

    private void botonBorrarHoraAjenaPulsado(View view) {
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle("ATENCION");
        aviso.setMessage("Vas a borrar las horas seleccionadas\n\n¿Estás seguro?");
        aviso.setPositiveButton("SI", (dialog, which) -> {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.addAll(listaIds);
            for (int id : ids) {
                HoraAjena horaSeleccionada = horasAjenas.get(id);
                listaAjenas.setItemChecked(horasAjenas.indexOf(horaSeleccionada), false);
                datos.borrarAjena(horaSeleccionada.getId());
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
