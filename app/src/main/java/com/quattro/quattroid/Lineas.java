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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.InputType;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quattro.models.LineaModel;
import com.quattro.models.ServicioAuxiliarModel;
import com.quattro.models.ServicioModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import BaseDatos.BaseDatos;
import Objetos.Colores;
import Objetos.Utils;

public class Lineas extends Activity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    // CONSTANTES
    final static int ACCION_EDITA_LINEA = 1;
    final static int ACCION_PIDE_ARCHIVO = 2;
    private final ArrayList<Integer> listaIds = new ArrayList<>();

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    AdaptadorLinea adaptador = null;
    ArrayList<LineaModel> lineas = null;
    String NombreArchivo = "Lineas";
    Boolean IgnorarRepetidos = false;

    ListView listaLineas = null;
    Button botonAddLinea = null;
    Button botonEditarLinea = null;
    Button botonBorrarLinea = null;
    Button botonImportar = null;
    Button botonExportar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.servicios);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("Lineas");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_lineas);

        // Inicialización de los elementos
        context = this;
        listaLineas = findViewById(R.id.lw_lineas);
        botonAddLinea = findViewById(R.id.bt_barra_addLinea);
        botonEditarLinea = findViewById(R.id.bt_barra_editarLinea);
        botonBorrarLinea = findViewById(R.id.bt_barra_borrarLinea);
        botonImportar = findViewById(R.id.bt_barra_importar);
        botonExportar = findViewById(R.id.bt_barra_exportar);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);

        // Registrar el menu contextual
        registerForContextMenu(listaLineas);

        // Llenado de la lista
        lineas = datos.getAllLineas();
        adaptador = new AdaptadorLinea(this, lineas);
        listaLineas.setAdapter(adaptador);

        // Establecemos el listener de item pulsado.
        listaLineas.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaLineas.setDivider(null);
        listaLineas.setDividerHeight(0);
        listaLineas.setMultiChoiceModeListener(this);// MULTI-SELECCION
        listaLineas.setOnItemClickListener(this);
        botonAddLinea.setOnClickListener(this::botonAddLineaPulsado);
        botonEditarLinea.setOnClickListener(this::botonEditarLineaPulsado);
        botonBorrarLinea.setOnClickListener(this::botonBorrarLineaPulsado);
        botonImportar.setOnClickListener(this::botonImportarPulsado);
        botonExportar.setOnClickListener(this::botonExportarPulsado);
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
        LineaModel lineaPulsada = lineas.get(position);

        // Creamos un intent para enviar la línea pulsada.
        Intent intent = new Intent(context, Servicios.class);
        intent.putExtra("Linea", lineaPulsada.getLinea());
        startActivity(intent);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Evaluamos el codigo de petición.
        switch (requestCode) {
            case ACCION_EDITA_LINEA:
                if (resultCode == RESULT_OK) {
                    actualizarLista();
                }
                break;
            case ACCION_PIDE_ARCHIVO:
                if (resultCode == RESULT_OK){
                    Uri uri = null;
                    if (data != null) uri = data.getData();
                    String lineasJson = leerLineasJson(uri);
                    if (lineasJson.equals("")){
                        Toast.makeText(this, "No se ha podido importar el contenido", Toast.LENGTH_SHORT).show();
                    } else {
                        Gson gson = new Gson();

                        try{
                            Type tipo = new TypeToken<ArrayList<LineaModel>>(){}.getType();
                            ArrayList<LineaModel> lineas = gson.fromJson(lineasJson, tipo);
                            actualizarLineas(lineas);
                        } catch (Exception e){
                            Toast.makeText(this, "Archivo no válido", Toast.LENGTH_SHORT).show();
                        }
                        actualizarLista();
                    }
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
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked){
        int checkedCount = listaLineas.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selec.");

        if (checked){
            listaIds.add(position);
            lineas.get(position).setSeleccionada(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            lineas.get(position).setSeleccionada(false);
        }

        if (checkedCount > 0){
            activarBoton(botonBorrarLinea);
            desactivarBoton(botonAddLinea);
            desactivarBoton(botonImportar);
            desactivarBoton(botonExportar);
        } else {
            desactivarBoton(botonBorrarLinea);
            activarBoton(botonAddLinea);
            activarBoton(botonImportar);
            activarBoton(botonExportar);
        }

        if (checkedCount == 1){
            activarBoton(botonEditarLinea);
        } else {
            desactivarBoton(botonEditarLinea);
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
        activarBoton(botonAddLinea);
        activarBoton(botonExportar);
        activarBoton(botonImportar);
        desactivarBoton(botonEditarLinea);
        desactivarBoton(botonBorrarLinea);
        //Refrescar la lista.
        for (LineaModel d : lineas){
            d.setSeleccionada(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    //endregion
    // ******************************************************************************************


    // ACTUALIZA LA LISTA
    private void actualizarLista() {
        lineas = datos.getAllLineas();
        adaptador = new AdaptadorLinea(this, lineas);
        listaLineas.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }


    // IMPORTAR LÍNEAS
    private Boolean importarLineas() {
        // Creamos un diálogo que nos permita elegir qué hacer con los repetidos.
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,15,15,15);
        layout.setLayoutParams(params);
        final RadioGroup grupo = new RadioGroup(this);
        grupo.setOrientation(LinearLayout.VERTICAL);
        grupo.setLayoutParams(params);
        final RadioButton radioSobreescribir = new RadioButton(this);
        radioSobreescribir.setId(R.id.valorTrabajadasConvenio);
        radioSobreescribir.setText("Sobreescribir");
        radioSobreescribir.setLayoutParams(params);
        radioSobreescribir.setChecked(true);
        final RadioButton radioIgnorar = new RadioButton(this);
        radioIgnorar.setId(R.id.tv_trabajadas);
        radioIgnorar.setText("Ignorar");
        radioSobreescribir.setLayoutParams(params);
        radioIgnorar.setChecked(false);
        grupo.addView(radioSobreescribir);
        grupo.addView(radioIgnorar);
        layout.addView(grupo);
        dialogo.setView(layout);
        dialogo.setTitle("¿Como gestionamos los repetidos?");
        dialogo.setPositiveButton("Buscar Archivo", (dialog, which) -> {
            IgnorarRepetidos = radioIgnorar.isChecked();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/json");
            // Definimos la ubicacion donde buscar.
            String destino = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
            destino = destino + "/Quattroid/Lineas";
            File d = new File(destino);
            if (!d.exists()){
                d.mkdir();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse(destino));
            }
            startActivityForResult(intent, ACCION_PIDE_ARCHIVO);
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogo.show();
        return true;
    }


    // EXPORTAR LÍNEAS
    private Boolean exportarLineas() {
        ArrayList<LineaModel> lineas = datos.getAllLineas();
        Gson gson = new Gson();
        String lineasJson = gson.toJson(lineas);
        // Pedimos el nombre del archivo.
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,5,5,5);
        layout.setLayoutParams(params);
        final TextView textView = new TextView(this);
        textView.setText("Nombre de archivo:");
        final EditText input = new EditText(this);
        input.setBackgroundResource(R.drawable.fondo_dialogo);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(NombreArchivo);
        input.selectAll();
        layout.addView(textView);
        layout.addView(input);
        dialogo.setView(layout);
        dialogo.setTitle("Exportar Líneas");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NombreArchivo = input.getText().toString();
                if (Utils.guardarLineasJson(NombreArchivo + ".json", lineasJson)) {
                    Toast.makeText(context, "Líneas exportadas correctamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No se han podido exportar las líneas.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogo.show();
        return true;
    }


    // LEER LÍNEAS
    private String leerLineasJson(Uri uri){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
            String linea;
            while ((linea = reader.readLine()) != null){
                stringBuilder.append(linea);
            }
        } catch (Exception e){
            return "";
        }
        return stringBuilder.toString();
    }


    // ACTUALIZAR LÍNEAS
    private void actualizarLineas(ArrayList<LineaModel> lineasNuevas){
        ArrayList<LineaModel> lineasLocales = datos.getAllLineas();
        for (LineaModel linea : lineasNuevas){
            // Comprobamos línea.
            LineaModel local = lineasLocales.stream().filter(l -> l.getLinea().equals(linea.getLinea())).findFirst().orElse(null);
            if (local == null){
                local = new LineaModel(linea);
                lineasLocales.add(local);
            } else {
                if (!IgnorarRepetidos) {
                    local.FromModel(linea);
                    local.Modificado = true;
                }
            }
            // Comprobamos Servicios
            if (linea.getServicios() != null){
                if (local.getServicios() == null) local.setServicios(new ArrayList<>());
                for (ServicioModel servicio : linea.getServicios()) {
                    ServicioModel servicioLocal = local.getServicios().stream().filter(s -> s.EsIgual(servicio)).findFirst().orElse(null);
                    if (servicioLocal == null) {
                        servicioLocal = new ServicioModel(servicio);
                        local.getServicios().add(servicioLocal);
                    } else {
                        if (!IgnorarRepetidos) {
                            servicioLocal.FromModel(servicio);
                            servicioLocal.Modificado = true;
                        }
                    }
                    // Comprobamos Servicios Auxiliares
                    if (servicio.getServiciosAuxiliares() != null){
                        if (servicioLocal.getServiciosAuxiliares() == null) servicioLocal.setServiciosAuxiliares(new ArrayList<>());
                        for(ServicioAuxiliarModel auxiliar : servicio.getServiciosAuxiliares()){
                            ServicioAuxiliarModel auxLocal = servicioLocal.getServiciosAuxiliares().stream().filter(s -> s.EsIgual(auxiliar)).findFirst().orElse(null);
                            if (auxLocal == null){
                                auxLocal = new ServicioAuxiliarModel(auxiliar);
                                servicioLocal.getServiciosAuxiliares().add(auxLocal);
                            } else {
                                if (!IgnorarRepetidos){
                                    auxLocal.FromModel(auxiliar);
                                    auxLocal.Modificado = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        datos.guardarAllLineas(lineasLocales);
    }


    private void botonAddLineaPulsado(View view) {
        Intent intent = new Intent(context, EditarLinea.class);
        intent.putExtra("Id", -1);
        startActivityForResult(intent, ACCION_EDITA_LINEA);
    }


    private void botonEditarLineaPulsado(View view) {
        LineaModel lineaSeleccionada = lineas.get(listaIds.get(0));
        Intent intent = new Intent(context, EditarLinea.class);
        intent.putExtra("Id", lineaSeleccionada.getId());
        startActivityForResult(intent, ACCION_EDITA_LINEA);
    }


    private void botonBorrarLineaPulsado(View view) {
        AlertDialog.Builder aviso = new AlertDialog.Builder(context);
        aviso.setTitle("ATENCION");
        aviso.setMessage("Vas a borrar las líneas seleccionadas\n\n¿Estás seguro?");
        aviso.setPositiveButton("SI", (dialog, which) -> {
            ArrayList<Integer> ids = new ArrayList<>();
            ids.addAll(listaIds);
            for (int id : ids) {
                LineaModel lineaSeleccionada = lineas.get(id);
                listaLineas.setItemChecked(lineas.indexOf(lineaSeleccionada), false);
                datos.borrarLinea(lineaSeleccionada.getLinea());
            }
            actualizarLista();
        });
        aviso.setNegativeButton("NO", (dialog, which) -> {});
        aviso.show();
    }


    private void botonImportarPulsado(View view) {
        importarLineas();
    }


    private void botonExportarPulsado(View view) {
        exportarLineas();
    }


    private void activarBoton(Button boton){
        boton.setEnabled(true);
        boton.setTextColor(0XFF000099);
        DrawableCompat.setTint(boton.getCompoundDrawables()[1], 0XFF000099);
    }


    private void desactivarBoton(Button boton){
        boton.setEnabled(false);
        boton.setTextColor(Colores.GRIS_OSCURO);
        DrawableCompat.setTint(boton.getCompoundDrawables()[1], Colores.GRIS_OSCURO);
    }


}
