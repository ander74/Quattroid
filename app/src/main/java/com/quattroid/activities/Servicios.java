/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.activities;

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

import com.quattroid.activities.databinding.ActivityServiciosBinding;
import com.quattroid.adapters.AdaptadorServicio;
import com.quattroid.data.models.ServicioModel;
import com.quattroid.data.repositories.LineaRepository;

import java.util.ArrayList;

public class Servicios extends Activity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    // CONSTANTES
    final static int ACCION_EDITA_SERVICIO = 1;
    private final ArrayList<Integer> listaIds = new ArrayList<>();
    private final LineaRepository lineaRepository = new LineaRepository(this);

    // VARIABLES
    private ActivityServiciosBinding binding;
    Context context = null;
    //BaseDatos datos = null;
    AdaptadorServicio adaptador = null;
    ArrayList<ServicioModel> servicios = null;
    String linea = "";
    int lineaId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiciosBinding.inflate(getLayoutInflater());
        getActionBar().setLogo(R.drawable.servicios);
        getActionBar().setTitle("Servicios");
        getActionBar().setSubtitle(null);
        setContentView(binding.getRoot());

        // Inicialización de los elementos
        context = this;

        // Recoger los datos del intent
        linea = getIntent().getExtras().getString("Linea");
        lineaId = getIntent().getExtras().getInt("LineaId");

        // Ponemos el título de la ActionBar
        getActionBar().setSubtitle("Línea " + linea);

        // Inicialización de la base de datos
        //datos = new BaseDatos(this);

        // Registrar el menu contextual
        registerForContextMenu(binding.lwServicios);

        // Llenado de la lista
        servicios = lineaRepository.getServicios(lineaId);
        adaptador = new AdaptadorServicio(this, servicios);
        binding.lwServicios.setAdapter(adaptador);

        // Establecemos el listener de item pulsado.
        binding.lwServicios.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        binding.lwServicios.setDivider(null);
        binding.lwServicios.setDividerHeight(0);
        binding.lwServicios.setMultiChoiceModeListener(this);// MULTI-SELECCION
        binding.lwServicios.setOnItemClickListener(this);
        binding.btAddServicio.setOnClickListener(this::botonAddServicioPulsado);
        binding.btBorrarServicio.setOnClickListener(this::botonBorrarServicioPulsado);
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        // Extraemos los datos del cursor
        ServicioModel servicioPulsado = servicios.get(position);

        // Creamos un intent para devolver los datos de la incidencia
        Intent intent = new Intent(context, EditarServicio.class);
        intent.putExtra("Id", servicioPulsado.getId());
        startActivityForResult(intent, ACCION_EDITA_SERVICIO);
    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
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
            case ACCION_EDITA_SERVICIO:
                if (resultCode == RESULT_OK) {
                    actualizarLista();
                }
                break;
            default:
                break;
        }
    }


    //******************************************************************************************
    //region Multi selección

    // MULTI-SELECCION: Al seleccionar un día del calendario.
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        int checkedCount = binding.lwServicios.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selec.");

        if (checked) {
            listaIds.add(position);
            servicios.get(position).setSelected(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            servicios.get(position).setSelected(false);
        }

        if (checkedCount > 0) {
            activarBoton(binding.btBorrarServicio);
            desactivarBoton(binding.btAddServicio);
        } else {
            desactivarBoton(binding.btBorrarServicio);
            activarBoton(binding.btAddServicio);
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
        activarBoton(binding.btAddServicio);
        desactivarBoton(binding.btBorrarServicio);
        //Refrescar la lista.
        for (ServicioModel d : servicios) {
            d.setSelected(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    //endregion
    // ******************************************************************************************


    // ACTUALIZA LA LISTA
    private void actualizarLista() {
        servicios = lineaRepository.getServicios(lineaId);
        adaptador = new AdaptadorServicio(this, servicios);
        binding.lwServicios.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }


    private void botonAddServicioPulsado(View view) {
        Intent intent = new Intent(context, EditarServicio.class);
        intent.putExtra("Id", -1);
        intent.putExtra("Linea", linea);
        startActivityForResult(intent, ACCION_EDITA_SERVICIO);
    }


    private void botonBorrarServicioPulsado(View view) {
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle("ATENCION");
        aviso.setMessage("Vas a borrar los servicios seleccionados\n\n¿Estás seguro?");
        aviso.setPositiveButton("SI", (dialog, which) -> {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.addAll(listaIds);
            for (int id : ids) {
                ServicioModel servicioSeleccionado = servicios.get(id);
                binding.lwServicios.setItemChecked(servicios.indexOf(servicioSeleccionado), false);
                lineaRepository.deleteServicio(servicioSeleccionado.getId());
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
