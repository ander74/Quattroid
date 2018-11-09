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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import BaseDatos.BaseDatos;
import BaseDatos.Estadistica;
import BaseDatos.EstadisticasMes;
import BaseDatos.EstadisticasAño;
import Objetos.Hora;

public class Estadisticas extends Activity{

    // CONSTANTES
    private final int MODO_BOTONES = 0;
    private final int MODO_MES = 1;
    private final int MODO_AÑO = 2;
    private final int MODO_FECHA = 3;

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    AdaptadorEstadisticas adaptador = null;
    ArrayList<Estadistica> datosEstadisticas = null;
    SharedPreferences opciones = null;
    Calendar fecha = null;
    int mesActual = 0;
    int añoActual = 0;
    int mesLimite = 0;
    int añoLimite = 0;
    int modo = MODO_BOTONES;
    double jMedia = 0d;

    // ELEMENTOS DEL VIEW
    LinearLayout grupoBotones = null;
    LinearLayout grupoTitulo = null;
    TextView titulo = null;
    ListView lista = null;
    NumberPicker selectorMes = null;
    NumberPicker selectorAño = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActionBar().setIcon(R.drawable.graficobarras);
        getActionBar().setTitle("Estadisticas");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        context = this;

        // Inicializamos los elementos del view
        instanciarElementos();

        // Inicializamos base de datos y opciones
        datos = new BaseDatos(context);
        opciones = PreferenceManager.getDefaultSharedPreferences(context);
        mesLimite = opciones.getInt("PrimerMes", 10);
        añoLimite = opciones.getInt("PrimerAño", 2014);
        jMedia = opciones.getFloat("", 0);
        long jorMedia = opciones.getLong("JorMedia", 0);
        jMedia = Double.longBitsToDouble(jorMedia);
        fecha = Calendar.getInstance();
        mesActual = fecha.get(Calendar.MONTH) + 1;
        añoActual = fecha.get(Calendar.YEAR);

        // Llenamos los selectores
        selectorMes.setMinValue(1);
        selectorMes.setMaxValue(12);
        selectorAño.setMinValue(2000);
        selectorAño.setMaxValue(2050);
        selectorMes.setValue(mesActual);
        selectorAño.setValue(añoActual);

        // Iniciamos el adaptador
        datosEstadisticas = datos.Estadisticas("Año=" + String.valueOf(añoActual), jMedia);
        adaptador = new AdaptadorEstadisticas(context, datosEstadisticas);
        lista.setAdapter(adaptador);

        // Inicamos en el modo botones
        grupoBotones.setVisibility(View.VISIBLE);
        grupoTitulo.setVisibility(View.GONE);
        lista.setVisibility(View.GONE);

    }

    // AL CREAR EL MENU SUPERIOR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_estadisticas, menu);
        return true;
    }

    // AL PULSAR EN EL MENU SUPERIOR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.bt_anterior:
                if (modo == MODO_MES) retrocedeMes();
                if (modo == MODO_AÑO) retrocedeAño();
                return true;
            case R.id.bt_siguiente:
                if (modo == MODO_MES) avanzaMes();
                if (modo == MODO_AÑO) avanzaAño();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (grupoBotones.getVisibility() == View.VISIBLE) {
                finish();
            } else {
                modo = MODO_BOTONES;
                grupoBotones.setVisibility(View.VISIBLE);
                grupoTitulo.setVisibility(View.GONE);
                lista.setVisibility(View.GONE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // INSTANCIAR LOS ELEMENTOS
    private void instanciarElementos(){

        // Grupos
        grupoBotones = (LinearLayout) findViewById(R.id.ly_botones);
        grupoTitulo = (LinearLayout) findViewById(R.id.ly_titulo);
        // Titulo
        titulo = (TextView) findViewById(R.id.tv_titulo);
        // Estadisticas
        lista = (ListView) findViewById(R.id.lw_estadisticas);
        // Selectores
        selectorMes = (NumberPicker) findViewById(R.id.np_mes);
        selectorAño = (NumberPicker) findViewById(R.id.np_año);

    }

    public void botonPulsado(View v){

        switch (v.getId()){

            case R.id.bt_mes:
                grupoBotones.setVisibility(View.GONE);
                grupoTitulo.setVisibility(View.VISIBLE);
                lista.setVisibility(View.VISIBLE);
                modo = MODO_MES;
                rellenarMes();
                break;
            case R.id.bt_año:
                grupoBotones.setVisibility(View.GONE);
                grupoTitulo.setVisibility(View.VISIBLE);
                lista.setVisibility(View.VISIBLE);
                modo = MODO_AÑO;
                rellenarAño();
                break;
            case R.id.bt_fecha:
                // Comprobamos que la fecha no es anterior a los límites
                int mesInicio = selectorMes.getValue();
                int añoInicio = selectorAño.getValue();
                int primerMes = opciones.getInt("PrimerMes", 10);
                int primerAño = opciones.getInt("PrimerAño", 2014);
                if ((añoInicio < primerAño) || (añoInicio == primerAño && mesInicio < primerMes)){
                    Toast.makeText(this, R.string.error_fueraLimite, Toast.LENGTH_SHORT).show();
                    break;
                }
                grupoBotones.setVisibility(View.GONE);
                grupoTitulo.setVisibility(View.VISIBLE);
                lista.setVisibility(View.VISIBLE);
                modo = MODO_FECHA;
                rellenarFecha();
                break;
        }
    }

    private void rellenarMes(){
        titulo.setText(Hora.MESES_MIN[mesActual] + " de " + String.valueOf(añoActual));
        String where = "Año=" + String.valueOf(añoActual) + " AND Mes=" + String.valueOf(mesActual);
        datosEstadisticas = datos.Estadisticas(where, jMedia);
        // Variables generales
        Estadistica e;
        long jorMedia = opciones.getLong("JorMedia", 0);
        double jornadaMedia = Hora.redondeaDecimal(Double.longBitsToDouble(jorMedia));
        double horas;
        double dias;
        double acumuladas;
        double trabajadas;
        // Contador inicializado en la posicion necesaria.
        int contador = 2;
        // Añadimos las acumuladas
        long acumAnteriores = opciones.getLong("AcumuladasAnteriores", 0);
        acumuladas = datos.acumuladasHastaMes(mesActual, añoActual) + Double.longBitsToDouble(acumAnteriores);
        acumuladas += datos.ajenasHastaMes(mesActual, añoActual);
        if (opciones.getBoolean("SumarTomaDeje", false)){
            acumuladas = acumuladas + datos.tomaDejeHastaMes(12, añoActual);
        }
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = Hora.textoDecimal(acumuladas);
        datosEstadisticas.add(e);
        // Añadimos los toma y dejes
        horas = datos.tomaDejeHastaMes(mesActual, añoActual);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Toma y Deje hasta Final";
        e.Valor = Hora.textoDecimal(horas);
        datosEstadisticas.add(e);

        // RESUMEN EN DIAS
        e = new Estadistica();
        e.Texto = "Resumen en Días";
        e.Tipo = 1;
        contador = 1;
        datosEstadisticas.add(e);

        // Añadimos trabajadas en días.
        trabajadas = datos.trabajadasMes(mesActual, añoActual);
        dias = Hora.redondeaDecimal(trabajadas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Trabajadas";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas en días.
        horas = datos.acumuladasMes(mesActual, añoActual);
        dias = Hora.redondeaDecimal(horas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas hasta el final en días.
        dias = Hora.redondeaDecimal(acumuladas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);

        // ESPACIADOR FINAL
        e = new Estadistica();
        e.Tipo = 1;
        e.Texto = "";
        datosEstadisticas.add(e);

        // Creamos y añadimos el adaptador.
        adaptador = new AdaptadorEstadisticas(context, datosEstadisticas);
        lista.setAdapter(adaptador);
    }

    private void rellenarAño(){
        titulo.setText("Año " + String.valueOf(añoActual));
        String where = "Año=" + String.valueOf(añoActual);
        datosEstadisticas = datos.Estadisticas(where, jMedia);
        Estadistica e;
        // Variables que se usarán
        long jorMedia = opciones.getLong("JorMedia", 0);
        double jornadaMedia = Hora.redondeaDecimal(Double.longBitsToDouble(jorMedia));
        double horas;
        double dias;
        double acumuladas;
        double trabajadas;
        // Contador inicializado en la posicion necesaria.
        int contador = 2;
        // Añadimos las acumuladas
        long acumAnteriores = opciones.getLong("AcumuladasAnteriores", 0);
        acumuladas = datos.acumuladasHastaMes(12, añoActual) + Double.longBitsToDouble(acumAnteriores);;
        acumuladas += datos.ajenasHastaMes(12, añoActual);
        if (opciones.getBoolean("SumarTomaDeje", false)){
            acumuladas = acumuladas + datos.tomaDejeHastaMes(12, añoActual);
        }
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = Hora.textoDecimal(acumuladas);
        datosEstadisticas.add(e);

        // Añadimos los toma y dejes
        horas = datos.tomaDejeHastaMes(12, añoActual);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Toma y Deje hasta Final";
        e.Valor = Hora.textoDecimal(horas);
        datosEstadisticas.add(e);

        // RESUMEN EN DIAS
        e = new Estadistica();
        e.Texto = "Resumen en Días";
        e.Tipo = 1;
        contador = 1;
        datosEstadisticas.add(e);

        // Añadimos trabajadas en días.
        trabajadas = datos.trabajadasAño(añoActual);
        dias = Hora.redondeaDecimal(trabajadas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Trabajadas";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas en días.
        horas = datos.acumuladasAño(añoActual);
        dias = Hora.redondeaDecimal(horas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas hasta el final en días.
        dias = Hora.redondeaDecimal(acumuladas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);


        // ESPACIADOR FINAL
        e = new Estadistica();
        e.Tipo = 1;
        e.Texto = "";
        datosEstadisticas.add(e);

        // Creamos y añadimos el adaptador.
        adaptador = new AdaptadorEstadisticas(context, datosEstadisticas);
        lista.setAdapter(adaptador);
    }

    private void rellenarFecha(){
        // Definimos las fechas de inicio y final
        int mesInicio = selectorMes.getValue();
        int añoInicio = selectorAño.getValue();
        int mesFinal = mesInicio + 11;
        int añoFinal = añoInicio;
        if (mesFinal > 12) {
            mesFinal = mesFinal - 12;
            añoFinal++;
        }
        // Ponemos el título
        titulo.setText("Anual Desde " + String.valueOf(mesInicio) + "/" + String.valueOf(añoInicio));

        String where = "((Mes >= " + String.valueOf(mesInicio) + " AND Año=" + String.valueOf(añoInicio) + ") OR ";
        where = where + "(Mes <= " + String.valueOf(mesFinal) + " AND Año=" + String.valueOf(añoFinal) + "))";
        datosEstadisticas = datos.Estadisticas(where, jMedia);
        // Variables generales
        Estadistica e;
        long jorMedia = opciones.getLong("JorMedia", 0);
        double jornadaMedia = Hora.redondeaDecimal(Double.longBitsToDouble(jorMedia));
        double trabajadas = datos.trabajadasAño(añoActual);
        double horas;
        double dias;
        double acumuladas;

        // Contador inicializado en la posicion necesaria.
        int contador = 2;
        // Añadimos las acumuladas
        long acumAnteriores = opciones.getLong("AcumuladasAnteriores", 0);
        acumuladas = datos.acumuladasHastaMes(mesFinal, añoFinal) + Double.longBitsToDouble(acumAnteriores);;
        acumuladas += datos.ajenasHastaMes(mesFinal, añoFinal);
        if (opciones.getBoolean("SumarTomaDeje", false)){
            acumuladas = acumuladas + datos.tomaDejeHastaMes(mesFinal, añoFinal);
        }
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = Hora.textoDecimal(acumuladas);
        datosEstadisticas.add(e);
        // Añadimos los toma y dejes
        horas = datos.tomaDejeHastaMes(mesFinal, añoFinal);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Toma y Deje hasta Final";
        e.Valor = Hora.textoDecimal(horas);
        datosEstadisticas.add(e);

        // RESUMEN EN DIAS
        e = new Estadistica();
        e.Texto = "Resumen en Días";
        e.Tipo = 1;
        contador = 1;
        datosEstadisticas.add(e);

        // Añadimos trabajadas en días.
        trabajadas = datos.trabajadasFecha(where);
        dias = Hora.redondeaDecimal(trabajadas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Trabajadas";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas en días.
        horas = datos.acumuladasFecha(where);
        dias = Hora.redondeaDecimal(horas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas hasta el final en días.
        dias = Hora.redondeaDecimal(acumuladas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = Hora.textoDecimal(dias);
        datosEstadisticas.add(e);


        // ESPACIADOR FINAL
        e = new Estadistica();
        e.Tipo = 1;
        contador++;
        e.Texto = "";
        datosEstadisticas.add(e);

        // Creamos y añadimos el adaptador.
        adaptador = new AdaptadorEstadisticas(context, datosEstadisticas);
        lista.setAdapter(adaptador);
    }

    private void retrocedeMes(){
        if (añoActual == añoLimite && mesActual == mesLimite) {
            Toast.makeText(this, R.string.error_fechaLimite, Toast.LENGTH_SHORT).show();
            return;
        }
        int m = mesActual;
        int a = añoActual;
        mesActual--;
        if (mesActual == 0){
            mesActual = 12;
            añoActual--;
        }
        Cursor c = datos.cursorMes(mesActual, añoActual);
        if (c.getCount() == 0) {
            mesActual = m;
            añoActual = a;
            Toast.makeText(context, R.string.error_mesNoExiste, Toast.LENGTH_SHORT).show();
        } else {
            rellenarMes();
        }
        c.close();
    }

    private void avanzaMes(){
        int m = mesActual;
        int a = añoActual;
        mesActual++;
        if (mesActual == 13){
            mesActual = 1;
            añoActual++;
        }
        Cursor c = datos.cursorMes(mesActual, añoActual);
        if (c.getCount() == 0){
            mesActual = m;
            añoActual = a;
            Toast.makeText(context, R.string.error_mesNoExiste, Toast.LENGTH_SHORT).show();
        } else {
            rellenarMes();
        }
        c.close();
    }

    private void retrocedeAño(){

        if (añoActual == añoLimite){
            Toast.makeText(context, R.string.error_fechaLimite, Toast.LENGTH_SHORT).show();
        } else {
            añoActual--;
            rellenarAño();
        }

    }

    private void avanzaAño(){
        Cursor c = datos.cursorMes(1, añoActual + 1);
        if (c.getCount() == 0){
            Toast.makeText(context, R.string.error_añoNoExiste, Toast.LENGTH_SHORT).show();
        } else {
            añoActual++;
            rellenarAño();
        }
        c.close();
    }

}
