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
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import Objetos.Utils;

public class Lineas extends Activity implements AdapterView.OnItemClickListener {

    // CONSTANTES
    final static int ACCION_EDITA_LINEA = 1;
    final static int ACCION_PIDE_ARCHIVO = 2;

    // VARIABLES
    Context context = null;
    Cursor cursor = null;
    BaseDatos datos = null;
    AdaptadorLinea adaptador = null;
    String NombreArchivo = "Lineas";
    Boolean IgnorarRepetidos = false;

    ListView listaLineas = null;
    Button botonAddLinea = null;
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
        botonImportar = findViewById(R.id.bt_barra_importar);
        botonExportar = findViewById(R.id.bt_barra_exportar);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);

        // Registrar el menu contextual
        registerForContextMenu(listaLineas);

        // Llenado de la lista
        cursor = datos.cursorLineas();
        adaptador = new AdaptadorLinea(this, cursor);
        listaLineas.setAdapter(adaptador);

        // Establecemos el listener de item pulsado.
        listaLineas.setOnItemClickListener(this);
        botonAddLinea.setOnClickListener(this::botonAddLineaPulsado);
        botonImportar.setOnClickListener(this::botonImportarPulsado);
        botonExportar.setOnClickListener(this::botonExportarPulsado);
    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lineas, menu);
        return true;
    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();
        switch (id) {
//            case R.id.bt_nuevo:
//                intent = new Intent(context, EditarLinea.class);
//                intent.putExtra("Id", -1);
//                startActivityForResult(intent, ACCION_EDITA_LINEA);
//                return true;
//            case R.id.bt_importar:
//                return importarLineas();
//            case R.id.bt_exportar:
//                return exportarLineas();
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // CREAR MENU CONTEXTUAL
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contexto_lineas, menu);
    }

    // AL PULSAR UNA OPCION DEL MENU CONTEXTUAL
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Guardar el elemento que ha provocado el menu contextual
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int elementoPulsado = acmi.position;

        Cursor c = null;
        // Determinar la opción pulsada
        switch (item.getItemId()) {
            case R.id.bt_editar:
                Intent intent = new Intent(context, EditarLinea.class);
                c = adaptador.getCursor();
                intent.putExtra("Id", c.getInt(c.getColumnIndexOrThrow("_id")));
                startActivityForResult(intent, ACCION_EDITA_LINEA);
                return true;
            case R.id.bt_borrar:
                c = adaptador.getCursor();
                datos.borrarLinea(c.getString(c.getColumnIndexOrThrow("Linea")));
                actualizarCursor();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        // Extraemos los datos del cursor
        Cursor c = adaptador.getCursor();

        // Creamos un intent para enviar la línea pulsada.
        Intent intent = new Intent(context, Servicios.class);
        intent.putExtra("Linea", c.getString(c.getColumnIndexOrThrow("Linea")));
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
                    actualizarCursor();
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
                        actualizarCursor();
                    }
                }
                break;
            default:
                break;
        }
    }

    // ACTUALIZA LA LISTA
    private void actualizarCursor() {
        cursor = datos.cursorLineas();
        adaptador.changeCursor(cursor);
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

    private void botonImportarPulsado(View view) {
        importarLineas();
    }

    private void botonExportarPulsado(View view) {
        exportarLineas();
    }


}
