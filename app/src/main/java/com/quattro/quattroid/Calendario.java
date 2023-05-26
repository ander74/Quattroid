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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.quattro.helpers.DiaHelper;
import org.joda.time.LocalDate;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import BaseDatos.BaseDatos;
import BaseDatos.HoraAjena;
import BaseDatos.Relevo;
import BaseDatos.DatosDia;
import BaseDatos.ServicioDia;
import BaseDatos.Servicio;
import BaseDatos.ServicioAuxiliar;
import BaseDatos.Linea;
import BaseDatos.Incidencia;
import Objetos.Calculos;
import Objetos.Colores;
import Objetos.Hora;

@SuppressLint({"NonConstantResourceId", "Range", "SetTextI18n", "DefaultLocale"})
public class Calendario extends Activity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    // AUTOCLASE
    @SuppressLint("StaticFieldLeak")
    public static Activity activityCalendario;

    // PORTAPAPELES
    private DatosDia portapapeles;
    private int diaPortapapeles;
    private int mesPortapapeles;
    private int añoPortapapeles;

    // CONSTANTES
    public static final int ACCION_DIA_CALENDARIO = 1;
    public static final int ACCION_EDITA_AJENA = 2;
    public static final int ACCION_EDITA_RELEVO = 3;

    // VARIABLES
    Context context = null;
    SharedPreferences opciones = null;
    ArrayList<DatosDia> listaDias = null;
    BaseDatos datos = null;
    AdaptadorDiaCalendario adaptador = null;
    double jornadaMedia = 0d;

    int mesActual;
    int añoActual;
    int primerAño;
    int primerMes;
    Calendar fecha;

    // ELEMENTOS DEL LAYOUT
    ListView listaCalendario = null;
    TextView acumuladas = null;
    TextView titulo = null;
    TextView acumuladasMes = null;
    TextView nocturnas = null;
    TextView tomaDeje = null;
    TextView euros = null;
    TextView trabajadasReales = null;
    TextView trabajadasConvenio = null;

    // PERTENECE A MULTI-SELECCIÓN
    private final ArrayList<Integer> listaIds = new ArrayList<>();

    // AL CREAR LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityCalendario = this;
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.calendario);
        getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_calendario);
        context = this;
        // Inicializar los elementos del view
        listaCalendario = findViewById(R.id.lw_listaCalendario);
        acumuladas = findViewById(R.id.tv_acumuladas);
        titulo = findViewById(R.id.tv_titulo);
        acumuladasMes = findViewById(R.id.tv_acumuladasMes);
        nocturnas = findViewById(R.id.tv_nocturnas);
        tomaDeje = findViewById(R.id.tv_tomaDeje);
        euros = findViewById(R.id.tv_euros);
        trabajadasReales = findViewById(R.id.tv_trabajadasReales);
        trabajadasConvenio = findViewById(R.id.tv_trabajadasConvenio);
        // Ocultar TomaDeje y Euros
        titulo.setVisibility(View.GONE);
        acumuladasMes.setVisibility(View.GONE);
        nocturnas.setVisibility(View.GONE);
        tomaDeje.setVisibility(View.GONE);
        euros.setVisibility(View.GONE);
        trabajadasReales.setVisibility(View.GONE);
        trabajadasConvenio.setVisibility(View.GONE);
        // Inicializar las opciones y la base de datos
        opciones = PreferenceManager.getDefaultSharedPreferences(this);
        datos = new BaseDatos(context);
        // Cambiamos el tipo de las incidencias protegidas que han cambiado.
        datos.actualizarTiposIncidencias();
        // Inicializar variables
        fecha = Calendar.getInstance();
        primerAño = datos.opciones.getPrimerAño();
        primerMes = datos.opciones.getPrimerMes();
        if ( datos.opciones.isVerMesActual()){
            mesActual = fecha.get(Calendar.MONTH) + 1;
            añoActual = fecha.get(Calendar.YEAR);
        } else {
            mesActual = opciones.getInt("UltimoMesMostrado", fecha.get(Calendar.MONTH) + 1);
            añoActual = opciones.getInt("UltimoAñoMostrado", fecha.get(Calendar.YEAR));
        }
        jornadaMedia = datos.opciones.getJornadaMedia();
        // Registrar Listeners y menús contextuales y configuraciones
        listaCalendario.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaCalendario.setDivider(null);
        listaCalendario.setDividerHeight(0);
        listaCalendario.setMultiChoiceModeListener(this);// MULTI-SELECCION
        listaDias = datos.datosMes(mesActual, añoActual);
        if(listaDias.isEmpty()) {
            datos.crearMes(mesActual, añoActual);
            listaDias = datos.datosMes(mesActual, añoActual);
        }
        inferirTurnos();
        adaptador = new AdaptadorDiaCalendario(this, listaDias);
        listaCalendario.setAdapter(adaptador);
        listaCalendario.setOnItemClickListener(this);
        if (datos.opciones.isVerMesActual()) {
            listaCalendario.setSelection(fecha.get(Calendar.DAY_OF_MONTH) - 1);
        } else {
            listaCalendario.setSelection(opciones.getInt("PosicionCalendario", 0));
        }
        // Escribir horas y título
        escribeHoras();
        escribeTitulo();
    }

    // AL CREAR EL MENU SUPERIOR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendario, menu);
        return true;
    }

    // AL PULSAR EN EL MENU SUPERIOR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.bt_anterior:
                retrocedeMes();
                return true;
            case R.id.bt_siguiente:
                avanzaMes();
                return true;
            case R.id.bt_pdf:
                crearPDF();
                return true;
            case android.R.id.home:
                pedirMes();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    // AL PULSAR UN ÍTEM DEL LISTVIEW
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
        DatosDia dia = listaDias.get(position);
        // Creamos el intent con los datos de la fecha.
        Intent intent = new Intent(context, DiaCalendario.class);
        intent.putExtra("Dia", dia.getDia());
        intent.putExtra("Mes", dia.getMes());
        intent.putExtra("Año", dia.getAño());
        intent.putExtra("Posicion", position);
        // Guardamos la posición de la lista
	    opciones.edit().putInt("PosicionCalendario", listaCalendario.getFirstVisiblePosition()).apply();
	    // Lanzamos la activity
        startActivityForResult(intent, ACCION_DIA_CALENDARIO);
    }

    // AL TERMINAR EL PROCESO DE LA ACTIVITY.
    @Override
    public void onDestroy(){
        datos.close();
        super.onDestroy();
    }

    // AL PAUSARSE LA APLICACION
    @Override
    public void onPause(){
        opciones.edit().putInt("PosicionCalendario", listaCalendario.getFirstVisiblePosition()).apply();
        opciones.edit().putInt("UltimoMesMostrado", mesActual).apply();
        opciones.edit().putInt("UltimoAñoMostrado", añoActual).apply();
        super.onPause();
    }

    // AL VOLVER DE UNA PAUSA
    @Override
    public void onRestart(){
        super.onRestart();
        primerAño = datos.opciones.getPrimerAño();
        primerMes = datos.opciones.getPrimerMes();
        datos = new BaseDatos(context);
        escribeHoras();
        actualizaLista(false);
	    listaCalendario.setSelection(opciones.getInt("PosicionCalendario", 0));
    }

    // AL VOLVER DE UNA SUBACTIVITY
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Evaluamos el codigo de petición.
        switch (requestCode){
            case ACCION_DIA_CALENDARIO:
                if (resultCode == RESULT_OK){
                    int posicion = data != null ? data.getIntExtra("Posicion", -1) : -1;
                    if (posicion > -1) {
                        DatosDia diaActual = listaDias.get(posicion);
                        DatosDia diaNuevo = datos.servicioDia(diaActual.getDia(), mesActual, añoActual);
                        diaActual.copiarDe(diaNuevo);
                    }
                    actualizaLista(true);
	                listaCalendario.setSelection(opciones.getInt("PosicionCalendario", 0));
                    escribeHoras();
                }
                break;
            case ACCION_EDITA_AJENA:
                if (resultCode == RESULT_OK){
                    HoraAjena horaAjena = new HoraAjena();
                    horaAjena.setDia(data.getIntExtra("Dia", 0));
                    horaAjena.setMes(data.getIntExtra("Mes", 0));
                    horaAjena.setAño(data.getIntExtra("Año", 0));
                    horaAjena.setHoras(data.getDoubleExtra("Horas", 0));
                    horaAjena.setMotivo(data.getStringExtra("Motivo"));
                    datos.setAjena(horaAjena);
                    Toast.makeText(context, R.string.mensaje_ajenaCreada, Toast.LENGTH_SHORT).show();
                    escribeHoras();
                }
                break;
            case ACCION_EDITA_RELEVO:
                if (resultCode == RESULT_OK){
                    int matricula = data != null ? data.getIntExtra("Matricula", -1) : -1;
                    String apellidos = data != null ? data.getStringExtra("Apellidos") : "";
                    for (DatosDia dia : listaDias){
                        if(dia.getMatricula() == matricula) dia.setApellidos(apellidos);
                    }
                    actualizaLista(true);
	                listaCalendario.setSelection(opciones.getInt("PosicionCalendario", 0));
                }
        }
    }

    // MULTI-SELECCION: Al seleccionar un día del calendario.
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked){
        int checkedCount = listaCalendario.getCheckedItemCount();
        Menu menu = mode.getMenu();
        mode.setTitle(checkedCount + " Selec.");
        if (checkedCount > 1){
            menu.findItem(R.id.bt_repetirAnterior).setVisible(false);
            menu.findItem(R.id.bt_copiar).setVisible(false);
            menu.findItem(R.id.bt_guardarServicio).setVisible(false);
            menu.findItem(R.id.bt_ajenas).setVisible(false);
            menu.findItem(R.id.bt_verRelevo).setVisible(false);
        } else {
            menu.findItem(R.id.bt_repetirAnterior).setVisible(true);
            menu.findItem(R.id.bt_copiar).setVisible(true);
            menu.findItem(R.id.bt_guardarServicio).setVisible(true);
            menu.findItem(R.id.bt_ajenas).setVisible(true);
            menu.findItem(R.id.bt_verRelevo).setVisible(true);
        }
        if (checked){
            listaIds.add(position);
            listaDias.get(position).setSeleccionado(true);
        } else {
            Integer pos = position;
            listaIds.remove(pos);
            listaDias.get(position).setSeleccionado(false);
        }
        adaptador.notifyDataSetChanged();
    }

    // MULTI-SELECCION: Al crearse el menú para los días seleccionados.
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.contexto_calendario, menu);
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
        int itemId = menuItem.getItemId();
        switch (itemId){
            case R.id.bt_repetirAnterior:
                repiteDiaAnterior(listaDias.get(listaIds.get(0)));
                escribeHoras();
                return true;
            case R.id.bt_marcarFranqueo:
                for (int id : listaIds) {
                    marcaFranqueo(listaDias.get(id));
                }
                return true;
            case R.id.bt_marcarFestivo:
                for (int id : listaIds) {
                    marcarFestivo(listaDias.get(id));
                }
                return true;
            case R.id.bt_copiar:
                copiar(listaDias.get(listaIds.get(0)));
                Toast.makeText(context, R.string.mensaje_diaCopiado, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.bt_pegar:
                if (portapapeles == null) return true;
                for (int id : listaIds) {
                    DatosDia dia = listaDias.get(id);
                    dia.setCodigoIncidencia(portapapeles.getCodigoIncidencia());
                    dia.setTextoIncidencia(portapapeles.getTextoIncidencia());
                    dia.setTipoIncidencia(portapapeles.getTipoIncidencia());
                    dia.setLinea(portapapeles.getLinea());
                    dia.setServicio(portapapeles.getServicio());
                    dia.setTurno(portapapeles.getTurno());
                    dia.setTextoLinea(portapapeles.getTextoLinea());
                    dia.setInicio(portapapeles.getInicio());
                    dia.setLugarInicio(portapapeles.getLugarInicio());
                    dia.setFinal(portapapeles.getFinal());
                    dia.setLugarFinal(portapapeles.getLugarFinal());
                    dia.setBus(portapapeles.getBus());
                    dia.setTomaDeje(portapapeles.getTomaDeje());
                    dia.setTomaDejeDecimal(portapapeles.getTomaDejeDecimal());
                    dia.setEuros(portapapeles.getEuros());
                    dia.setAcumuladas(portapapeles.getAcumuladas());
                    dia.setNocturnas(portapapeles.getNocturnas());
                    dia.setTrabajadas(portapapeles.getTrabajadas());
                    dia.setMatricula(portapapeles.getMatricula());
                    dia.setApellidos(portapapeles.getApellidos());
                    dia.setMatriculaSusti(portapapeles.getMatriculaSusti());
                    dia.setApellidosSusti(portapapeles.getApellidosSusti());
                    dia.setCalificacion(portapapeles.getCalificacion());
                    dia.setNotas(portapapeles.getNotas());
                    datos.guardaDia(dia);
                    // Copiamos los servicios del día
                    datos.vaciarServiciosDia(dia.getDia(), mesActual, añoActual);
                    Cursor cursor1 = datos.cursorServiciosDia(diaPortapapeles, mesPortapapeles, añoPortapapeles);
                    if (cursor1.getCount() > 0) {
                        while (cursor1.moveToNext()) {
                            ServicioDia sd = new ServicioDia();
                            sd.setDia(dia.getDia());
                            sd.setMes(mesActual);
                            sd.setAño(añoActual);
                            sd.setLinea(cursor1.getString(cursor1.getColumnIndex("Linea")));
                            sd.setServicio(cursor1.getString(cursor1.getColumnIndex("Servicio")));
                            sd.setTurno(cursor1.getInt(cursor1.getColumnIndex("Turno")));
                            sd.setInicio(cursor1.getString(cursor1.getColumnIndex("Inicio")));
                            sd.setLugarInicio(cursor1.getString(cursor1.getColumnIndex("LugarInicio")));
                            sd.setFinal(cursor1.getString(cursor1.getColumnIndex("Final")));
                            sd.setLugarFinal(cursor1.getString(cursor1.getColumnIndex("LugarFinal")));
                            datos.guardaServicioDia(sd);
                        }
                    }
                    cursor1.close();
                }
                actualizaLista(false);
                escribeHoras();
	            return true;
            case R.id.bt_ajenas:
                DatosDia dia = listaDias.get(listaIds.get(0));
                // Creamos un intent para devolver los datos de la incidencia
                Intent intent = new Intent(context, EditarHorasAjenas.class);
                intent.putExtra("Dia", dia.getDia());
                intent.putExtra("Mes", dia.getMes());
                intent.putExtra("Año", dia.getAño());
                intent.putExtra("Nuevo", true);
                startActivityForResult(intent, ACCION_EDITA_AJENA);
                return true;
            case R.id.bt_vaciar:
                AlertDialog.Builder aviso = new AlertDialog.Builder(context);
                aviso.setTitle("ATENCION");
                aviso.setMessage("Vas a vaciar los días seleccionados\n\n¿Estás seguro?");
                aviso.setPositiveButton("SI", (dialog, which) -> {
                    Boolean vaciado = false;
                    for (int id : listaIds) {
                        vaciado = vaciarDia(listaDias.get(id));
                    }
                    if (vaciado) {
                        Toast.makeText(context, R.string.mensaje_diaVaciado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.error_diaVaciado, Toast.LENGTH_SHORT).show();
                    }
                    actualizaLista(false);
                });
                aviso.setNegativeButton("NO", (dialog, which) -> {});
                aviso.show();
                //TODO: Eliminar los servicios auxiliares.
                return true;
            case R.id.bt_recalcular:
                for (int id : listaIds) {
                    DatosDia datosDia = listaDias.get(id);
                    Cursor cursor = datos.cursorServiciosDia(datosDia.getDia(), datosDia.getMes(), datosDia.getAño());
                    DiaHelper.CalcularHorasDia(datosDia, cursor, datos);
                    datos.guardaDia(datosDia);
                }
                escribeHoras();
                actualizaLista(false);
                return true;
            case R.id.bt_verRelevo:
                DatosDia datosD = listaDias.get(listaIds.get(0));
                Relevo r = datos.getRelevo(datosD.getMatricula());
                if (r == null) return true;
                Intent i = new Intent(context, EditarRelevo.class);
                i.putExtra("Matricula", r.getMatricula());
                i.putExtra("Deuda", r.getDeuda());
                i.putExtra("Calificacion", r.getCalificacion());
                i.putExtra("Nombre", r.getNombre());
                i.putExtra("Apellidos", r.getApellidos());
                i.putExtra("Telefono", r.getTelefono());
                i.putExtra("Notas", r.getNotas());
                startActivityForResult(i, ACCION_EDITA_RELEVO);
                return true;
            case R.id.bt_guardarServicio:
                DatosDia datosDia = listaDias.get(listaIds.get(0));
                if (datosDia.getLinea().equals("") ||
                        datosDia.getServicio().equals("") ||
                        datosDia.getTurno() == 0 ||
                        datosDia.getInicio().equals("") ||
                        datosDia.getFinal().equals("")) {
                    Toast.makeText(context, "Servicio Incompleto", Toast.LENGTH_SHORT).show();
                    return true;
                }
                // Se crea el servicio que será guardado. Si el servicio ya existe, se sale.
                Servicio servicio = datos.getServicio(datosDia.getLinea(), datosDia.getServicio(), datosDia.getTurno());
                if (servicio != null) {
                    Toast.makeText(context, "El servicio ya existe", Toast.LENGTH_SHORT).show();
                    return true;
                }
                // Si la línea no existe, se crea.
                Linea linea = datos.getLinea(datosDia.getLinea());
                if (linea == null){
                    linea = new Linea();
                    linea.setLinea(datosDia.getLinea());
                    linea.setTexto("Sin descripción");
                    datos.setLinea(linea);
                }
                // Se llena el servicio.
                servicio = new Servicio();
                servicio.setLinea(datosDia.getLinea());
                servicio.setServicio(datosDia.getServicio());
                servicio.setTurno(datosDia.getTurno());
                servicio.setInicio(datosDia.getInicio());
                servicio.setFinal(datosDia.getFinal());
                servicio.setLugarInicio(datosDia.getLugarInicio());
                servicio.setLugarFinal(datosDia.getLugarFinal());
                servicio.setTomaDeje(datosDia.getTomaDeje());
                servicio.setEuros(datosDia.getEuros());
                // Se guarda el servicio
                datos.setServicio(-1, servicio);
                // Si hay servicios auxiliares, se copian.
                datos.vaciarServiciosAuxiliares(datosDia.getLinea(), datosDia.getServicio(), datosDia.getTurno());
                Cursor cur = datos.cursorServiciosDia(datosDia.getDia(), datosDia.getMes(), datosDia.getAño());
                if (cur.getCount() > 0){
                    while (cur.moveToNext()){
                        ServicioAuxiliar sa = new ServicioAuxiliar();
                        sa.setLinea(datosDia.getLinea());
                        sa.setServicio(datosDia.getServicio());
                        sa.setTurno(datosDia.getTurno());
                        sa.setLineaAuxiliar(cur.getString(cur.getColumnIndex("Linea")));
                        sa.setServicioAuxiliar(cur.getString(cur.getColumnIndex("Servicio")));
                        sa.setTurnoAuxiliar(cur.getInt(cur.getColumnIndex("Turno")));
                        sa.setInicio(cur.getString(cur.getColumnIndex("Inicio")));
                        sa.setLugarInicio(cur.getString(cur.getColumnIndex("LugarInicio")));
                        sa.setFinal(cur.getString(cur.getColumnIndex("Final")));
                        sa.setLugarFinal(cur.getString(cur.getColumnIndex("LugarFinal")));
                        datos.setServicioAuxiliar(sa);
                    }
                }
                cur.close();
                Toast.makeText(context, "Se ha creado el servicio", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }

    // MULTI-SELECCION: Al quitarse todos los días seleccionados.
    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        //Refrescar la lista.
        for (DatosDia d : listaDias){
            d.setSeleccionado(false);
        }
        listaIds.clear();
        adaptador.notifyDataSetChanged();
    }

    // ESCRIBE EL TÍTULO DEL MENÚ SUPERIOR, PONIENDO MES Y AÑO ACTUALES.
    private void escribeTitulo(){
        getActionBar().setTitle(Hora.MESES_MAY[mesActual]);
        getActionBar().setSubtitle(String.valueOf(añoActual));
    }

    // ESCRIBE LAS HORAS ACUMULADAS Y NOCTURNAS DEL MES
    private void escribeHoras(){
        /*
            DEFINIMOS ACUMULADAS HASTA MES
         */
        // Extraemos la acumuladas hasta el mes actual
        double acumHastaMes = datos.acumuladasHastaMes(mesActual, añoActual);
        // Sumamos las acumuladas anteriores desde las opciones
        acumHastaMes += datos.opciones.getAcumuladasAnteriores();
        // Sumamos las horas ajenas al servicio anteriores.
        acumHastaMes += datos.ajenasHastaMes(mesActual, añoActual);
        // Sumamos las horas del toma y deje si la opcion lo dice
        if (datos.opciones.isSumarTomaDeje()) {
            acumHastaMes += datos.tomaDejeHastaMes(mesActual, añoActual);
        }
        /*
            DEFINIMOS LOS DEMÁS VALORES DEL MES
         */
        // Extraemos las acumuladas del mes actual.
        double acumMesActual = datos.acumuladasMes(mesActual, añoActual);
        // Extraemos las nocturnas del mes actual.
        double noctMes = datos.nocturnasMes(mesActual, añoActual);
        // Extraemos las horas de toma y deje del mes actual.
        double tomaDejeMes = datos.tomaDejeMes(mesActual, añoActual);
        // Extraemos los euros por servicio del mes actual
        double eurosMes = datos.eurosMes(mesActual, añoActual);
        // Extraemos las trabajadas del mes actual.
        double trabMes = datos.trabajadasMes(mesActual, añoActual);
        // Extraemos las trabajadas por convenio del mes actual.
        int diasTrabConv = datos.diasTrabajadosConvenio(mesActual, añoActual);
        double trabConvMes = diasTrabConv * jornadaMedia;
        // Escribimos las acumuladas, poniendo el color correspondiente.
        if (acumHastaMes > -0.01) {
            acumuladas.setTextColor(Colores.VERDE_OSCURO);
        } else {
            acumuladas.setTextColor(Colores.ROJO);
        }
        acumuladas.setText("Acum. hasta mes : " + Hora.textoDecimal(acumHastaMes));
        // Escribimos las acumuladas del mes
        if (acumMesActual > -0.01) {
            acumuladasMes.setTextColor(Colores.VERDE_OSCURO);
        } else {
            acumuladasMes.setTextColor(Colores.ROJO);
        }
        acumuladasMes.setText("Horas Acumuladas : " + Hora.textoDecimal(acumMesActual));
        // Escribimos las nocturnas
        nocturnas.setTextColor(Colores.AZUL_CLARO);
        nocturnas.setText("Horas Nocturnas : " + Hora.textoDecimal(noctMes));
        // Escribimos las horas del toma y deje.
        tomaDeje.setTextColor(Colores.MARRON_CLARO);
        tomaDeje.setText("Toma y Deje : " + Hora.textoDecimal(tomaDejeMes));
        // Escribimos los euros por servicio.
        euros.setTextColor(Colores.VIOLETA);
        euros.setText("Euros por Servicio : " + Hora.textoDecimal(eurosMes));
        // Escribimos las trabajadas reales.
        trabajadasReales.setTextColor(Colores.AZUL_CLARO);
        trabajadasReales.setText("Trabajadas : " + Hora.textoDecimal(trabMes));
        // Escribimos las trabajadas por convenio
        trabajadasConvenio.setTextColor(Colores.AZUL_CLARO);
        trabajadasConvenio.setText("Trabajadas Convenio : " + Hora.textoDecimal(trabConvMes));
    }

    // ACTUALIZA LA LISTA DEL CALENDARIO
    private void actualizaLista(boolean completo){
	    if (completo){
            listaDias = datos.datosMes(mesActual, añoActual);
            adaptador = new AdaptadorDiaCalendario(this, listaDias);
            listaCalendario.setAdapter(adaptador);
        }
	    adaptador.notifyDataSetChanged();
    }

    // AL PULSAR EN LAS HORAS ACUMULADAS O NOCTURNAS TOTALES
    public void horasPulsadas(View view){
        if (nocturnas.getVisibility() == View.GONE){
            titulo.setVisibility(View.VISIBLE);
            acumuladasMes.setVisibility(View.VISIBLE);
            nocturnas.setVisibility(View.VISIBLE);
            tomaDeje.setVisibility(View.VISIBLE);
            euros.setVisibility(View.VISIBLE);
            trabajadasReales.setVisibility(View.VISIBLE);
            trabajadasConvenio.setVisibility(View.VISIBLE);
        } else{
            titulo.setVisibility(View.GONE);
            acumuladasMes.setVisibility(View.GONE);
            nocturnas.setVisibility(View.GONE);
            tomaDeje.setVisibility(View.GONE);
            euros.setVisibility(View.GONE);
            trabajadasReales.setVisibility(View.GONE);
            trabajadasConvenio.setVisibility(View.GONE);
        }
    }

    // AVANZAR EL CALENDARIO EN UN MES.
    private void avanzaMes(){
        mesActual++;
        if (mesActual == 13){
            mesActual = 1;
            añoActual++;
        }
        listaDias.clear();
        listaDias = datos.datosMes(mesActual, añoActual);
        if (listaDias.isEmpty()) {
            datos.crearMes(mesActual, añoActual);
            listaDias = datos.datosMes(mesActual, añoActual);
        }
        inferirTurnos();
        adaptador = new AdaptadorDiaCalendario(this, listaDias);
        adaptador.notifyDataSetChanged();
        listaCalendario.setAdapter(adaptador);
        listaCalendario.setSelection(0);
        escribeTitulo();
        escribeHoras();
    }

    // RETROCEDER EL CALENDARIO EN UN MES.
    private void retrocedeMes(){
        if (añoActual == primerAño && mesActual == primerMes) {
            Toast.makeText(this, getResources().getText(R.string.error_fechaLimite), Toast.LENGTH_SHORT).show();
            return;
        }
        mesActual--;
        if (mesActual == 0){
            mesActual = 12;
            añoActual--;
        }
        listaDias.clear();
        listaDias = datos.datosMes(mesActual, añoActual);
        if(listaDias.isEmpty()) {
            datos.crearMes(mesActual, añoActual);
            listaDias = datos.datosMes(mesActual, añoActual);
        }
        inferirTurnos();
        adaptador = new AdaptadorDiaCalendario(this, listaDias);
        adaptador.notifyDataSetChanged();
        listaCalendario.setAdapter(adaptador);
        listaCalendario.setSelection(0);
        escribeTitulo();
        escribeHoras();
    }

    // VA A UNA FECHA DETERMINADA
    private void irAFecha(int mes, int año){
        if (año < primerAño || (año == primerAño && mes < primerMes)) {
            Toast.makeText(this, getResources().getText(R.string.error_fechaInvalida), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mes < 1 || mes > 12 || año < 1995 || año > 2099) return;
        mesActual = mes;
        añoActual = año;
        listaDias.clear();
        listaDias = datos.datosMes(mesActual, añoActual);
        if(listaDias.isEmpty()) {
            datos.crearMes(mesActual, añoActual);
            listaDias = datos.datosMes(mesActual, añoActual);
        }
        inferirTurnos();
        adaptador = new AdaptadorDiaCalendario(this, listaDias);
        adaptador.notifyDataSetChanged();
        listaCalendario.setAdapter(adaptador);
        listaCalendario.setSelection(0);
        escribeTitulo();
        escribeHoras();
    }

    // REPITE DÍA ANTERIOR
    private void repiteDiaAnterior(DatosDia dia){
        int day;
        int mes;
        int año;
        // Evaluamos que el día no sea el primer día que se muestra.
        if (dia.getDia() == 1 && dia.getMes() == primerMes && dia.getAño() == primerAño){
            Toast.makeText(this, R.string.mensaje_primerDia, Toast.LENGTH_SHORT).show();
            return;
        } else {
            Calendar fecha = Calendar.getInstance();
            fecha.set(dia.getAño(), dia.getMes()-1, dia.getDia());
            fecha.add(Calendar.DAY_OF_MONTH, -1);
            day = fecha.get(Calendar.DAY_OF_MONTH);
            mes = fecha.get(Calendar.MONTH) + 1;
            año = fecha.get(Calendar.YEAR);
        }
        DatosDia datosDia = datos.servicioDia(day, mes, año);
        if (datosDia.getDia() == 0){
            Toast.makeText(this, R.string.mensaje_mesNoExiste, Toast.LENGTH_SHORT).show();
            return;
        }
        dia.setCodigoIncidencia(datosDia.getCodigoIncidencia());
        dia.setTextoIncidencia(datosDia.getTextoIncidencia());
        dia.setTipoIncidencia(datosDia.getTipoIncidencia());
        dia.setLinea(datosDia.getLinea());
        dia.setServicio(datosDia.getServicio());
        dia.setTurno(datosDia.getTurno());
        dia.setTextoLinea(datosDia.getTextoLinea());
        dia.setInicio(datosDia.getInicio());
        dia.setLugarInicio(datosDia.getLugarInicio());
        dia.setFinal(datosDia.getFinal());
        dia.setLugarFinal(datosDia.getLugarFinal());
        dia.setBus(datosDia.getBus());
        dia.setTomaDeje(datosDia.getTomaDeje());
        dia.setTomaDejeDecimal(datosDia.getTomaDejeDecimal());
        dia.setEuros(datosDia.getEuros());
        dia.setAcumuladas(datosDia.getAcumuladas());
        dia.setNocturnas(datosDia.getNocturnas());
        dia.setTrabajadas(datosDia.getTrabajadas());
        dia.setMatricula(datosDia.getMatricula());
        dia.setApellidos(datosDia.getApellidos());
        dia.setMatriculaSusti(datosDia.getMatriculaSusti());
        dia.setApellidosSusti(datosDia.getApellidosSusti());
        dia.setCalificacion(datosDia.getCalificacion());
        dia.setNotas(datosDia.getNotas());
        // Guardamos el día
        datos.guardaDia(dia);
        // Copiamos los servicios del día
        datos.vaciarServiciosDia(dia.getDia(), dia.getMes(), dia.getAño());
        Cursor c = datos.cursorServiciosDia(day, mes, año);
        if (c.getCount() > 0){
            while (c.moveToNext()){
                ServicioDia sd = new ServicioDia();
                sd.setDia(dia.getDia());
                sd.setMes(dia.getMes());
                sd.setAño(dia.getAño());
                sd.setLinea(c.getString(c.getColumnIndexOrThrow("Linea")));
                sd.setServicio(c.getString(c.getColumnIndexOrThrow("Servicio")));
                sd.setTurno(c.getInt(c.getColumnIndexOrThrow("Turno")));
                sd.setInicio(c.getString(c.getColumnIndexOrThrow("Inicio")));
                sd.setFinal(c.getString(c.getColumnIndexOrThrow("Final")));
                datos.guardaServicioDia(sd);
            }
        }
        c.close();
        actualizaLista(false);
    }

    // MARCAR/DESMARCAR DIA COMO FRANQUEO
    private void marcaFranqueo(DatosDia dia){
        if (dia.isEsFranqueo()){
            dia.setEsFranqueo(false);
            if (dia.getCodigoIncidencia() == 2) {
                datos.guardaDia(dia);
                actualizaLista(false);
                return;
            }
        } else {
            dia.setEsFranqueo(true);
            if (dia.getCodigoIncidencia() == 0) {
                Incidencia i = datos.getIncidencia(2);
                dia.setCodigoIncidencia(2);
                dia.setTextoIncidencia(i.getTexto());
                dia.setTipoIncidencia(4);
            }
        }
        datos.guardaDia(dia);
        actualizaLista(false);
    }

    // MARCAR/DESMARCAR DIA COMO FESTIVO
    private void marcarFestivo(DatosDia dia){
        dia.setEsFestivo(!dia.isEsFestivo());
        datos.guardaDia(dia);
        actualizaLista(false);
    }

    // VACIAR DIA
    public Boolean vaciarDia(DatosDia dia){
        dia.setCodigoIncidencia(0);
        dia.setTextoIncidencia("");
        dia.setTipoIncidencia(0);
        dia.setEsFranqueo(false);
        dia.setEsFestivo(false);
        dia.setLinea("");
        dia.setServicio("");
        dia.setTurno(0);
        dia.setTextoLinea("");
        dia.setInicio("");
        dia.setLugarInicio("");
        dia.setFinal("");
        dia.setLugarFinal("");
        dia.setBus("");
        dia.setTomaDeje("");
        dia.setTomaDejeDecimal(0d);
        dia.setEuros(0d);
        dia.setAcumuladas(0d);
        dia.setNocturnas(0d);
        dia.setTrabajadas(0d);
        dia.setDesayuno(false);
        dia.setComida(false);
        dia.setCena(false);
        dia.setMatricula(0);
        dia.setApellidos("");
        dia.setMatriculaSusti(0);
        dia.setApellidosSusti("");
        dia.setCalificacion(0);
        dia.setNotas("");
        if (datos.guardaDia(dia)){
            datos.vaciarServiciosDia(dia.getDia(), dia.getMes(), dia.getAño());
            setResult(RESULT_OK, null);
            escribeHoras();
            return true;
        }
        return false;
    }

    // COPIAR EN EL PORTAPAPELES
    public void copiar(DatosDia datosDia){
        diaPortapapeles = datosDia.getDia();
        mesPortapapeles = datosDia.getMes();
        añoPortapapeles = datosDia.getAño();
        portapapeles = new DatosDia();
        portapapeles.setCodigoIncidencia(datosDia.getCodigoIncidencia());
        portapapeles.setTextoIncidencia(datosDia.getTextoIncidencia());
        portapapeles.setTipoIncidencia(datosDia.getTipoIncidencia());
        portapapeles.setServicio(datosDia.getServicio());
        portapapeles.setTurno(datosDia.getTurno());
        portapapeles.setLinea(datosDia.getLinea());
        portapapeles.setTextoLinea(datosDia.getTextoLinea());
        portapapeles.setInicio(datosDia.getInicio());
        portapapeles.setLugarInicio(datosDia.getLugarInicio());
        portapapeles.setFinal(datosDia.getFinal());
        portapapeles.setLugarFinal(datosDia.getLugarFinal());
        portapapeles.setAcumuladas(datosDia.getAcumuladas());
        portapapeles.setNocturnas(datosDia.getNocturnas());
        portapapeles.setTrabajadas(datosDia.getTrabajadas());
        portapapeles.setDesayuno(datosDia.isDesayuno());
        portapapeles.setComida(datosDia.isComida());
        portapapeles.setCena(datosDia.isCena());
        portapapeles.setTomaDeje(datosDia.getTomaDeje());
        portapapeles.setTomaDejeDecimal(datosDia.getTomaDejeDecimal());
        portapapeles.setEuros(datosDia.getEuros());
        portapapeles.setMatricula(datosDia.getMatricula());
        portapapeles.setApellidos(datosDia.getApellidos());
        portapapeles.setCalificacion(datosDia.getCalificacion());
        portapapeles.setMatriculaSusti(datosDia.getMatriculaSusti());
        portapapeles.setApellidosSusti(datosDia.getApellidosSusti());
        portapapeles.setBus(datosDia.getBus());
        portapapeles.setNotas(datosDia.getNotas());
    }

    // CREAR PDF DEL CALENDARIO.
    public void crearPDF(){
        if (listaDias.isEmpty()) return;
        String estadoSD = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(estadoSD)) return;
        // Definimos el path de destino y lo creamos si no existe.
        String destino = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        destino = destino + "/Quattroid/PDF";
        File d = new File(destino);
        if (!d.exists()){
            d.mkdirs();
        }
        String Ruta = destino + "/" + añoActual + "-" + String.format("%02d",mesActual) + " - Calendario " + Hora.MESES_MIN[mesActual] + ".pdf";
        Document doc = null;
        try {
            PdfWriter writer = new PdfWriter(Ruta);
            PdfDocument pdf = new PdfDocument(writer);
            boolean horizontal = datos.opciones.isPdfHorizontal();
            if (horizontal) {
                doc = new Document(pdf, PageSize.A4.rotate());
            } else {
                doc = new Document(pdf, PageSize.A4);
            }
            doc.setMargins(25,25,25,25);
            // Insertamos la tabla del calendario.
            doc.add(crearTablaCalendario(horizontal));
            // Insertamos la tabla del resumen.
            doc.add(crearTablaResumen());
        } catch (IOException ex){
            Toast.makeText(this, "Se produjo un error al crear el PDF.\n", Toast.LENGTH_LONG).show();
            return;
        } finally {
            if (doc != null) doc.close();
            MediaScannerConnection.scanFile(this, new String[] { Ruta }, null, null);
        }
        Toast.makeText(this, "Se ha creado " + añoActual + "-" + Hora.MESES_MIN[mesActual] + ".pdf" + ".", Toast.LENGTH_SHORT).show();
    }

    // CREAR LA TABLA DEL PDF USANDO EL CURSOR ACTUAL
    public Table crearTablaCalendario(boolean horizontal) throws IOException{
        // FUENTE VERDANA
        PdfFont Fuente = PdfFontFactory.createFont(FontConstants.HELVETICA);
        // ESTILO TABLA
        Style estiloTabla = new Style()
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMargins(0,0,0,0)
                .setPaddings(0,0,0,0)
                .setWidth(UnitValue.createPercentValue(100))
                .setKeepTogether(true)
                .setFont(Fuente)
                .setFontSize(9);
        // ESTILO TÍTULO
        Style estiloTitulo = new Style()
                .setBorder(Border.NO_BORDER)
                .setFontSize(16);
        // ESTILO CABECERA
        Style estiloCabecera = new Style()
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFontColor(Color.BLACK)
                .setMarginBottom(1)
                .setBold()
                .setBorderTop(new SolidBorder(1))
                .setBorderBottom(new SolidBorder(1))
                .setBackgroundColor(new DeviceRgb(169,208,142));
        // ESTILO CELDAS
        Style estiloCeldas = new Style()
                .setKeepTogether(true)
                .setMarginTop(1)
                .setPadding(2)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(0.5f))
                .setBackgroundColor(Color.WHITE);
        // ESTILO NOTAS
        Style estiloNotas = new Style()
                .setPaddingLeft(15)
                .setPaddingRight(15)
                .setTextAlignment(TextAlignment.LEFT);
        // ESTILO CELDAS PARES
        Style estiloCeldasPares = new Style()
                .setBackgroundColor(new DeviceRgb(226,239,218));
        // ESTILO FRANQUEOS
        Style estiloFranqueos = new Style()
                .setFontColor(Color.BLUE);
        // ESTILO SÁBADOS
        Style estiloSabados = new Style()
                .setFontColor(new DeviceRgb(237,125,49));
        // ESTILO FESTIVOS
        Style estiloFestivos = new Style()
                .setFontColor(Color.RED);
        // ESTILOS BORDES
        Style estiloBordeSup = new Style().setBorderTop(new SolidBorder(1));
        Style estiloBordeDer = new Style().setBorderRight(new SolidBorder(1));
        Style estiloBordeVer = new Style().setBorderLeft(new SolidBorder(1)).setBorderRight(new SolidBorder(1));
        // CREAMOS LA TABLA
        Table tabla;
        if (horizontal){
            tabla = new Table(new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}); // 17 columnas
        } else {
            tabla = new Table(new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}); // 14 columnas
        }
        tabla.addStyle(estiloTabla);
        // AÑADIMOS LOS TÍTULOS
        int colSpan = horizontal ? 9 : 7;
        Cell celda = new Cell(1,colSpan);
        celda.add(getString(R.string.nombreAplicacion)).addStyle(estiloTitulo).setTextAlignment(TextAlignment.LEFT);
        tabla.addHeaderCell(celda);
        if (horizontal) colSpan -= 1;
        celda = new Cell(1,colSpan);
        celda.add(Hora.MESES_MIN[mesActual] + " - " + añoActual).addStyle(estiloTitulo).setTextAlignment(TextAlignment.RIGHT);
        tabla.addHeaderCell(celda);
        // AÑADIMOS LOS ENCABEZADOS
        tabla.addHeaderCell(new Cell().add("Día").addStyle(estiloCabecera).addStyle(estiloBordeVer));
        tabla.addHeaderCell(new Cell().add("Incidencia").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Línea").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Servicio").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Turno").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Inicio").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Final").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Trab.").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Acum.").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Noct.").addStyle(estiloCabecera));
        if (horizontal){
            tabla.addHeaderCell(new Cell().add("Des.").addStyle(estiloCabecera));
            tabla.addHeaderCell(new Cell().add("Com.").addStyle(estiloCabecera));
            tabla.addHeaderCell(new Cell().add("Cena").addStyle(estiloCabecera));
        }
        tabla.addHeaderCell(new Cell().add("T.Deje").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Euros").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Relevo").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Bus").addStyle(estiloCabecera).addStyle(estiloBordeDer));
        // RECORREMOS EL CURSOR DE LOS DÍAS
        if (!listaDias.isEmpty()){
            int fila = 1;
            for (DatosDia dia : listaDias) {
                //TODO: Filtrar por tipo de incidencia para no mostrar los datos de servicio.
                // Inicializamos las filas que ocupa el día
                int filas = 1;
                Cursor servicios = datos.cursorServiciosDia(dia.getDia(), dia.getMes(), dia.getAño());
                // Añadimos filas al día en función de las opciones.
                if (!dia.getNotas().trim().equals("") && datos.opciones.isPdfIncluirNotas()) filas ++;
                if (datos.opciones.isPdfIncluirServicios()) filas += servicios.getCount();
                // Celda Día.
                celda = new Cell(filas,1).addStyle(estiloCeldas).addStyle(estiloBordeVer).addStyle(estiloBordeSup);
                celda.setKeepTogether(true);
                celda.setFontSize(11).setBold();
                if(fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                if(dia.isEsFestivo() || dia.getDiaSemana() == 1) celda.addStyle(estiloFestivos);
                if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                celda.add(String.format("%02d", dia.getDia()));
                tabla.addCell(celda);
                // Celda Incidencia
                celda = new Cell(filas,1).addStyle(estiloCeldas).addStyle(estiloBordeSup);
                if(fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                if(dia.isEsFestivo() || dia.getDiaSemana() == 1) celda.addStyle(estiloFestivos);
                if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                celda.add(dia.getTextoIncidencia());
                tabla.addCell(celda);
                // SI EL CÓDIGO DE LA INCIDENCIA ES DE TRABAJO, SE RELLENAN LOS CAMPOS.
                if (dia.getTipoIncidencia() == 1 || dia.getTipoIncidencia() == 2 || dia.getTipoIncidencia() == 5) {
                    // Celda Línea
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(dia.getLinea());
                    tabla.addCell(celda);
                    // Celda Servicio
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(dia.getServicio());
                    tabla.addCell(celda);
                    // Celda Turno.
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(String.valueOf(dia.getTurno()));
                    tabla.addCell(celda);
                    // Celda Inicio
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(dia.getInicio());
                    tabla.addCell(celda);
                    // Celda Final
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(dia.getFinal());
                    tabla.addCell(celda);
                    // Celda Trabajadas.
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(redondeaDecimales(dia.getTrabajadas()));
                    tabla.addCell(celda);
                    // Celda Acumuladas.
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(redondeaDecimales(dia.getAcumuladas()));
                    tabla.addCell(celda);
                    // Celda Nocturnas.
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(redondeaDecimales(dia.getNocturnas()));
                    tabla.addCell(celda);
                    if (horizontal){
                        // Celda Desayuno.
                        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                        if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                        if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                            celda.addStyle(estiloFestivos);
                        if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                        if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                        celda.add(dia.isDesayuno() ? "1" : "0");
                        tabla.addCell(celda);
                        // Celda Comida.
                        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                        if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                        if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                            celda.addStyle(estiloFestivos);
                        if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                        if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                        celda.add(dia.isComida() ? "1" : "0");
                        tabla.addCell(celda);
                        // Celda Cena.
                        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                        if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                        if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                            celda.addStyle(estiloFestivos);
                        if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                        if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                        celda.add(dia.isCena() ? "1" : "0");
                        tabla.addCell(celda);
                    }
                    // Celda Toma Deje
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(dia.getTomaDeje());
                    tabla.addCell(celda);
                    // Celda Euros.
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(redondeaDecimales(dia.getEuros()));
                    tabla.addCell(celda);
                    // Celda Relevo.
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(String.format("%04d", dia.getMatricula()));
                    tabla.addCell(celda);
                    // Celda Bus.
                    celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeDer).addStyle(estiloBordeSup);
                    if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if (dia.isEsFestivo() || dia.getDiaSemana() == 1)
                        celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    celda.add(String.valueOf(dia.getBus()));
                    tabla.addCell(celda);
                } else {
                    colSpan = horizontal ? 15 : 12;
                    for (int i=1; i <= colSpan; i++) {
                        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeSup);
                        if (i == colSpan) celda.addStyle(estiloBordeDer);
                        if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                        celda.add("\n");
                        tabla.addCell(celda);
                    }
                }
                // SI HAY SERVICIOS COMPLEMENTARIOS SE AÑADEN
                if (datos.opciones.isPdfIncluirServicios() && servicios.getCount() > 0) {
                    if (servicios.moveToFirst()) {
                        do {
                            // Celda Línea
                            celda = new Cell().addStyle(estiloCeldas).setItalic();
                            if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                            if(dia.isEsFestivo() || dia.getDiaSemana() == 1) celda.addStyle(estiloFestivos);
                            if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                            if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                            celda.add(servicios.getString(servicios.getColumnIndex("Linea")));
                            tabla.addCell(celda);
                            // Celda Servicio
                            celda = new Cell().addStyle(estiloCeldas).setItalic();
                            if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                            if(dia.isEsFestivo() || dia.getDiaSemana() == 1) celda.addStyle(estiloFestivos);
                            if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                            if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                            celda.add(servicios.getString(servicios.getColumnIndex("Servicio")));
                            tabla.addCell(celda);
                            // Celda Turno.
                            celda = new Cell().addStyle(estiloCeldas).setItalic();
                            if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                            if(dia.isEsFestivo() || dia.getDiaSemana() == 1) celda.addStyle(estiloFestivos);
                            if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                            if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                            celda.add(String.valueOf(servicios.getInt(servicios.getColumnIndex("Turno"))));
                            tabla.addCell(celda);
                            // Celda Inicio
                            celda = new Cell().addStyle(estiloCeldas).setItalic();
                            if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                            if(dia.isEsFestivo() || dia.getDiaSemana() == 1) celda.addStyle(estiloFestivos);
                            if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                            if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                            celda.add(servicios.getString(servicios.getColumnIndex("Inicio")));
                            tabla.addCell(celda);
                            // Celda Final
                            celda = new Cell().addStyle(estiloCeldas).setItalic();
                            if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                            if(dia.isEsFestivo() || dia.getDiaSemana() == 1) celda.addStyle(estiloFestivos);
                            if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                            if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                            celda.add(servicios.getString(servicios.getColumnIndex("Final")));
                            tabla.addCell(celda);
                            // Celdas restantes
                            colSpan = horizontal ? 10 : 7;
                            for (int i=1; i <= colSpan; i++) {
                                celda = new Cell().addStyle(estiloCeldas).setItalic();
                                if (i == colSpan) celda.addStyle(estiloBordeDer);
                                if (fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                                tabla.addCell(celda);
                            }
                        } while (servicios.moveToNext());
                    }
                }
                // SI HAY NOTAS SE AÑADEN
                if(!dia.getNotas().trim().equals("") && datos.opciones.isPdfIncluirNotas()){
                    colSpan = horizontal ? 15 : 12;
                    celda = new Cell(1,colSpan).addStyle(estiloCeldas).addStyle(estiloNotas).addStyle(estiloBordeDer).setItalic();
                    if(fila % 2 == 0) celda.addStyle(estiloCeldasPares);
                    if(dia.isEsFestivo() || dia.getDiaSemana() == 1) celda.addStyle(estiloFestivos);
                    if(dia.getDiaSemana() == 7) celda.addStyle(estiloSabados);
                    if(dia.isEsFranqueo()) celda.addStyle(estiloFranqueos);
                    if (datos.opciones.isPdfAgruparNotas()){
                        dia.setNotas(dia.getNotas().replace("\n\n", " "));
                        dia.setNotas(dia.getNotas().replace("\n", " "));
                    }
                    celda.add(dia.getNotas());
                    tabla.addCell(celda);
                }
                // Incrementamos el número de línea
                fila ++;
            }
        }
        tabla.setBorderBottom(new SolidBorder(1));
        tabla.getHeader().setBorderBottom(new SolidBorder(1));
        // DEVOLVEMOS LA TABLA
        return tabla;
    }

    // CREAR LA TABLA RESUMEN
    public Table crearTablaResumen() throws IOException{
        // FUENTE VERDANA
        PdfFont Fuente = PdfFontFactory.createFont(FontConstants.HELVETICA);
        // ESTILO TABLA
        Style estiloTabla = new Style()
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMargins(5,0,0,0)
                .setPaddings(0,0,0,0)
                .setWidth(UnitValue.createPercentValue(100))
                .setKeepTogether(true)
                .setFont(Fuente)
                .setFontSize(9);
        // ESTILO CABECERA
        Style estiloCabecera = new Style()
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFontColor(Color.BLACK)
                .setMarginBottom(1)
                .setBold()
                .setBorderTop(new SolidBorder(1))
                .setBorderBottom(new SolidBorder(1))
                .setBackgroundColor(new DeviceRgb(169,208,142));
        // ESTILO CELDAS
        Style estiloCeldas = new Style()
                .setKeepTogether(true)
                .setMarginTop(1)
                .setPadding(2)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(0.5f))
                .setBackgroundColor(Color.WHITE);
        // ESTILO NEGATIVAS
        Style estiloNegativas = new Style()
                .setFontColor(Color.RED);
        // ESTILO POSITIVAS
        Style estiloPositivas = new Style()
                .setFontColor(new DeviceRgb(0,144,81));
        // ESTILOS BORDES
        Style estiloBordeIzq = new Style().setBorderLeft(new SolidBorder(1));
        Style estiloBordeInf = new Style().setBorderBottom(new SolidBorder(1));
        Style estiloBordeDer = new Style().setBorderRight(new SolidBorder(1));
        // CREAMOS LA TABLA
        Table tabla = new Table(new float[]{1,1,1,1,1,1,1,1,1}); // 9 columnas
        tabla.addStyle(estiloTabla);
        // AÑADIMOS LOS ENCABEZADOS
        tabla.addHeaderCell(new Cell().add("Trabajadas").addStyle(estiloCabecera).addStyle(estiloBordeIzq));
        tabla.addHeaderCell(new Cell().add("Acumuladas").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Nocturnas").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Desayunos").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Comidas").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Cenas").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Toma Deje").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Euros").addStyle(estiloCabecera));
        tabla.addHeaderCell(new Cell().add("Acum. Hasta Mes").addStyle(estiloCabecera).addStyle(estiloBordeDer));
        // TRABAJADAS
        Cell celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeIzq).addStyle(estiloBordeInf);
        double horas = datos.trabajadasMes(mesActual, añoActual);
        celda.add(redondeaDecimales(horas));
        tabla.addCell(celda);
        // ACUMULADAS
        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeInf);
        horas = datos.acumuladasMes(mesActual, añoActual);
        celda.add(redondeaDecimales(horas));
        tabla.addCell(celda);
        // NOCTURNAS
        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeInf);
        horas = datos.nocturnasMes(mesActual, añoActual);
        celda.add(redondeaDecimales(horas));
        tabla.addCell(celda);
        // DESAYUNOS
        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeInf);
        int dietas = datos.DesayunosMes(mesActual, añoActual);
        celda.add(String.format("%02d", dietas));
        tabla.addCell(celda);
        // COMIDAS
        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeInf);
        dietas = datos.ComidasMes(mesActual, añoActual);
        celda.add(String.format("%02d", dietas));
        tabla.addCell(celda);
        // CENAS
        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeInf);
        dietas = datos.CenasMes(mesActual, añoActual);
        celda.add(String.format("%02d", dietas));
        tabla.addCell(celda);
        // TOMA DEJES
        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeInf);
        horas = datos.tomaDejeMes(mesActual, añoActual);
        celda.add(redondeaDecimales(horas));
        tabla.addCell(celda);
        // EUROS
        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeInf);
        horas = datos.eurosMes(mesActual, añoActual);
        celda.add(redondeaDecimales(horas));
        tabla.addCell(celda);
        // ACUMULADAS HASTA MES
        horas = datos.acumuladasHastaMes(mesActual, añoActual);
        // Sumamos las acumuladas anteriores desde las opciones
        horas += datos.opciones.getAcumuladasAnteriores();
        // Sumamos las horas ajenas al servicio anteriores.
        horas += datos.ajenasHastaMes(mesActual, añoActual);
        // Sumamos las horas del toma y deje si la opcion lo dice
        if (datos.opciones.isSumarTomaDeje()){
            horas += datos.tomaDejeHastaMes(mesActual, añoActual);
        }
        celda = new Cell().addStyle(estiloCeldas).addStyle(estiloBordeInf).addStyle(estiloBordeDer);
        if (horas > -0.01) {
            celda.addStyle(estiloPositivas);
        } else {
            celda.addStyle(estiloNegativas);
        }
        celda.add(redondeaDecimales(horas));
        tabla.addCell(celda);
        // DEVOLVEMOS LA TABLA
        return tabla;
    }

    // REDONDEA EL NUMERO A DOS DECIMALES Y LO DEVUELVE COMO UN STRING
    private String redondeaDecimales(double horas){
        if (horas < 0 && horas > -0.01) return "0,00";
        return String.format("%.2f", horas);
    }

    // INFIERE EL TURNO DE LA LISTA EN FUNCIÓN DEL DIA BASE ESTABLECIDO
    private void inferirTurnos () {
        if (datos.opciones.isInferirTurnos()){
            int dia = datos.opciones.getDiaBaseTurnos();
            int mes = datos.opciones.getMesBaseTurnos();
            int año = datos.opciones.getAñoBaseTurnos();
            LocalDate fechaReferencia = new LocalDate(año, mes, dia);
            listaDias.stream().filter(d -> d.getTurno() == 0).forEach(d -> {
                LocalDate fechaDia = new LocalDate(d.getAño(), d.getMes(), d.getDia());
                d.setTurno(Calculos.InferirTurno(fechaDia, fechaReferencia, 1));
                datos.guardaDia(d);
            });
        }
    }

    // MUESTRA UN DIÁLOGO PIDIENDO UN MES Y AÑO Y VA AL MES EN CONCRETO.
    private void pedirMes() {
        NumberPicker mesesPicker;
        NumberPicker añosPicker;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ir a mes");
        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        minDate.set(Calendar.MONTH, 0);
        minDate.set(Calendar.YEAR, 1995);
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, 31);
        maxDate.set(Calendar.MONTH, 11);
        maxDate.set(Calendar.YEAR, 2099);
        // Creamos los parámetros que van a guardarse en los pickers.
        NumberPicker.LayoutParams pickerParams =
                new NumberPicker.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pickerParams.setMargins(20, 10, 20, 10);
        // Creamos el linear layout que albergará los spinners.
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(20, 20, 20, 20);
        linearLayout.setGravity(Gravity.CENTER);
        // Creamos el picker de los meses.
        mesesPicker = new NumberPicker(this);
        mesesPicker.setLayoutParams(pickerParams);
        mesesPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mesesPicker.setMinValue(0);
        mesesPicker.setMaxValue(11);
        mesesPicker.setDisplayedValues(new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre",});
        // Creamos el picker de los años.
        añosPicker = new NumberPicker(this);
        añosPicker.setLayoutParams(pickerParams);
        añosPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        añosPicker.setWrapSelectorWheel(false);
        añosPicker.setPadding(10, 10, 10, 10);
        añosPicker.setMinValue(2000);
        añosPicker.setMaxValue(2100);
        // Hacemos que el año se incremente si el mes cambia...
        mesesPicker.setOnValueChangedListener((picker, oldValue, newValue) -> {
            if (oldValue == 11 && newValue == 0 && añosPicker.getValue() < añosPicker.getMaxValue())
                añosPicker.setValue(añosPicker.getValue() + 1);
            if (oldValue == 0 && newValue == 11 && añosPicker.getValue() > añosPicker.getMinValue())
                añosPicker.setValue(añosPicker.getValue() - 1);
        });
        // Añadimos los pickers al LinearLayout
        linearLayout.addView(mesesPicker);
        linearLayout.addView(añosPicker);
        // Establecemos el valor de los pickers.
        mesesPicker.setValue(mesActual - 1);
        añosPicker.setValue(añoActual);
        builder.setView(linearLayout);
        builder.setPositiveButton("Aceptar", (s, v) -> irAFecha(mesesPicker.getValue() + 1, añosPicker.getValue()));
        builder.setNeutralButton("Hoy", (dialogInterface, i) -> irAFecha(LocalDate.now().getMonthOfYear(), LocalDate.now().getYear()));
        builder.setNegativeButton("Cancelar", (dialogInterface, v) -> { });
        builder.create().show();
    }

}
