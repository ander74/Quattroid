/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */
package com.quattroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.quattroid.adapters.AdaptadorEstadisticas;
import com.quattroid.helpers.HoraHelper;

import java.util.ArrayList;
import java.util.Calendar;

import BaseDatos.BaseDatos;
import BaseDatos.Estadistica;

public class Estadisticas extends Activity {

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
    int diaCierre = 0;
    int modo = MODO_BOTONES;
    double jMedia = 0d;
    String textoDiaCierreMes = "";

    // ELEMENTOS DEL VIEW
    LinearLayout grupoBotones = null;
    LinearLayout grupoTitulo = null;
    TextView titulo = null;
    TextView subTitulo = null;
    TextView diaCierreMes = null;
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
        mesLimite = datos.opciones.getPrimerMes(); //opciones.getInt("PrimerMes", 10);
        añoLimite = datos.opciones.getPrimerAño(); //opciones.getInt("PrimerAño", 2014);
        //long jorMedia = opciones.getLong("JorMedia", 0);
        jMedia = datos.opciones.getJornadaMedia(); //Double.longBitsToDouble(jorMedia);
        fecha = Calendar.getInstance();
        mesActual = fecha.get(Calendar.MONTH) + 1;
        añoActual = fecha.get(Calendar.YEAR);

        // Rellenamos la etiqueta del día de cierre de mes.
        diaCierre = datos.opciones.getDiaCierreMes();
        textoDiaCierreMes = "Se cierra el mes el día " + String.valueOf(diaCierre);
        diaCierreMes.setText(textoDiaCierreMes);
        if (diaCierre == 1) diaCierreMes.setVisibility(View.GONE);

        // Llenamos los selectores
        selectorMes.setMinValue(1);
        selectorMes.setMaxValue(12);
        selectorMes.setDisplayedValues(new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre",});
        selectorAño.setMinValue(2000);
        selectorAño.setMaxValue(2050);
        selectorAño.setWrapSelectorWheel(false);
        selectorMes.setValue(mesActual);
        selectorAño.setValue(añoActual);
        // Hacemos que el año se incremente si el mes cambia...
        selectorMes.setOnValueChangedListener((picker, oldValue, newValue) -> {
            if (oldValue == 12 && newValue == 1 && selectorAño.getValue() < selectorAño.getMaxValue())
                selectorAño.setValue(selectorAño.getValue() + 1);
            if (oldValue == 1 && newValue == 12 && selectorAño.getValue() > selectorAño.getMinValue())
                selectorAño.setValue(selectorAño.getValue() - 1);
        });


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
        switch (item.getItemId()) {
            case R.id.bt_anterior:
                if (modo == MODO_MES) retrocedeMes();
                if (modo == MODO_AÑO) retrocedeAño();
                return true;
            case R.id.bt_siguiente:
                if (modo == MODO_MES) avanzaMes();
                if (modo == MODO_AÑO) avanzaAño();
                return true;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
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
    private void instanciarElementos() {

        // Grupos
        grupoBotones = findViewById(R.id.ly_botones);
        grupoTitulo = findViewById(R.id.ly_titulo);
        // Titulo
        titulo = findViewById(R.id.tv_titulo);
        subTitulo = findViewById(R.id.tv_subTitulo);
        // Día de cierre de mes
        diaCierreMes = findViewById(R.id.tv_diaCierreMes);
        // Estadisticas
        lista = findViewById(R.id.lw_estadisticas);
        // Selectores
        selectorMes = findViewById(R.id.np_mes);
        selectorAño = findViewById(R.id.np_año);

    }

    public void botonPulsado(View v) {

        switch (v.getId()) {

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
                int primerMes = datos.opciones.getPrimerMes(); //opciones.getInt("PrimerMes", 10);
                int primerAño = datos.opciones.getPrimerAño(); //opciones.getInt("PrimerAño", 2014);
                if ((añoInicio < primerAño) || (añoInicio == primerAño && mesInicio < primerMes)) {
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

    private void rellenarMes() {
        titulo.setText(HoraHelper.MESES_MIN[mesActual] + " de " + String.valueOf(añoActual));
        subTitulo.setText("");
        subTitulo.setVisibility(View.GONE);
        int mesAnterior = mesActual == 1 ? 12 : mesActual - 1;
        if (diaCierre != 1) {
            String textoSubTitulo = "Del " + String.valueOf(diaCierre + 1) + " de " +
                    HoraHelper.MESES_MIN[mesAnterior].toLowerCase() + " al " + String.valueOf(diaCierre) +
                    " de " + HoraHelper.MESES_MIN[mesActual].toLowerCase();
            subTitulo.setVisibility(View.VISIBLE);
            subTitulo.setText(textoSubTitulo);
        }
        String where = "Año=" + añoActual + " AND Mes=" + String.valueOf(mesActual);
        if (diaCierre != 1) {
            int añoAnterior = mesActual == 1 ? añoActual - 1 : añoActual;
            int diaAnterior = diaCierre + 1;
            where = "((Año=" + añoAnterior + " AND Mes=" + mesAnterior + " AND Dia>= " + diaAnterior + ") OR (" +
                    "Año=" + añoActual + " AND Mes=" + mesActual + " AND Dia<=" + diaCierre + "))";
        }
        datosEstadisticas = datos.Estadisticas(where, jMedia);
        // Variables generales
        Estadistica e;
        //long jorMedia = opciones.getLong("JorMedia", 0);
        double jornadaMedia = HoraHelper.redondeaDecimal(datos.opciones.getJornadaMedia());//Double.longBitsToDouble(jorMedia));
        double horas;
        double dias;
        double acumuladas;
        double trabajadas;
        // Contador inicializado en la posicion necesaria.
        int contador = 2;
        // Añadimos las acumuladas
        //long acumAnteriores = opciones.getLong("AcumuladasAnteriores", 0);
        acumuladas = datos.acumuladasHastaMes(diaCierre, mesActual, añoActual) + datos.opciones.getAcumuladasAnteriores();//Double.longBitsToDouble(acumAnteriores);
        acumuladas += datos.ajenasHastaMes(diaCierre, mesActual, añoActual);
        if (datos.opciones.isSumarTomaDeje()) { //opciones.getBoolean("SumarTomaDeje", false)){
            acumuladas = acumuladas + datos.tomaDejeHastaMes(diaCierre, 12, añoActual);
        }
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = HoraHelper.textoDecimal(acumuladas);
        datosEstadisticas.add(e);
        // Añadimos los toma y dejes
        horas = datos.tomaDejeHastaMes(diaCierre, mesActual, añoActual);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Toma y Deje hasta Final";
        e.Valor = HoraHelper.textoDecimal(horas);
        datosEstadisticas.add(e);

        // RESUMEN EN DIAS
        e = new Estadistica();
        e.Texto = "Resumen en Días";
        e.Tipo = 1;
        contador = 1;
        datosEstadisticas.add(e);

        // Añadimos trabajadas en días.
        trabajadas = datos.trabajadasMes(mesActual, añoActual, diaCierre);
        dias = HoraHelper.redondeaDecimal(trabajadas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Trabajadas";
        e.Valor = HoraHelper.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas en días.
        horas = datos.acumuladasMes(mesActual, añoActual, diaCierre);
        dias = HoraHelper.redondeaDecimal(horas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas";
        e.Valor = HoraHelper.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas hasta el final en días.
        dias = HoraHelper.redondeaDecimal(acumuladas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = HoraHelper.textoDecimal(dias);
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

    private void rellenarAño() {
        titulo.setText("Año " + String.valueOf(añoActual));
        String where = "Año=" + String.valueOf(añoActual);
        datosEstadisticas = datos.Estadisticas(where, jMedia);
        Estadistica e;
        // Variables que se usarán
        //long jorMedia = opciones.getLong("JorMedia", 0);
        double jornadaMedia = HoraHelper.redondeaDecimal(datos.opciones.getJornadaMedia()); //Double.longBitsToDouble(jorMedia));
        double horas;
        double dias;
        double acumuladas;
        double trabajadas;
        // Contador inicializado en la posicion necesaria.
        int contador = 2;
        // Añadimos las acumuladas
        //long acumAnteriores = opciones.getLong("AcumuladasAnteriores", 0);
        acumuladas = datos.acumuladasHastaMes(12, añoActual) + datos.opciones.getAcumuladasAnteriores(); //Double.longBitsToDouble(acumAnteriores);;
        acumuladas += datos.ajenasHastaMes(12, añoActual);
        if (datos.opciones.isSumarTomaDeje()) { //opciones.getBoolean("SumarTomaDeje", false)){
            acumuladas = acumuladas + datos.tomaDejeHastaMes(12, añoActual);
        }
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = HoraHelper.textoDecimal(acumuladas);
        datosEstadisticas.add(e);

        // Añadimos los toma y dejes
        horas = datos.tomaDejeHastaMes(12, añoActual);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Toma y Deje hasta Final";
        e.Valor = HoraHelper.textoDecimal(horas);
        datosEstadisticas.add(e);

        // RESUMEN EN DIAS
        e = new Estadistica();
        e.Texto = "Resumen en Días";
        e.Tipo = 1;
        contador = 1;
        datosEstadisticas.add(e);

        // Añadimos trabajadas en días.
        trabajadas = datos.trabajadasAño(añoActual);
        dias = HoraHelper.redondeaDecimal(trabajadas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Trabajadas";
        e.Valor = HoraHelper.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas en días.
        horas = datos.acumuladasAño(añoActual);
        dias = HoraHelper.redondeaDecimal(horas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas";
        e.Valor = HoraHelper.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas hasta el final en días.
        dias = HoraHelper.redondeaDecimal(acumuladas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = HoraHelper.textoDecimal(dias);
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

    private void rellenarFecha() {
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
        //long jorMedia = opciones.getLong("JorMedia", 0);
        double jornadaMedia = HoraHelper.redondeaDecimal(datos.opciones.getJornadaMedia()); //Double.longBitsToDouble(jorMedia));
        double trabajadas = datos.trabajadasAño(añoActual);
        double horas;
        double dias;
        double acumuladas;

        // Contador inicializado en la posicion necesaria.
        int contador = 2;
        // Añadimos las acumuladas
        //long acumAnteriores = opciones.getLong("AcumuladasAnteriores", 0);
        acumuladas = datos.acumuladasHastaMes(mesFinal, añoFinal) + datos.opciones.getAcumuladasAnteriores(); //Double.longBitsToDouble(acumAnteriores);;
        acumuladas += datos.ajenasHastaMes(mesFinal, añoFinal);
        if (datos.opciones.isSumarTomaDeje()) { //opciones.getBoolean("SumarTomaDeje", false)){
            acumuladas = acumuladas + datos.tomaDejeHastaMes(mesFinal, añoFinal);
        }
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = HoraHelper.textoDecimal(acumuladas);
        datosEstadisticas.add(e);
        // Añadimos los toma y dejes
        horas = datos.tomaDejeHastaMes(mesFinal, añoFinal);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Toma y Deje hasta Final";
        e.Valor = HoraHelper.textoDecimal(horas);
        datosEstadisticas.add(e);

        // RESUMEN EN DIAS
        e = new Estadistica();
        e.Texto = "Resumen en Días";
        e.Tipo = 1;
        contador = 1;
        datosEstadisticas.add(e);

        // Añadimos trabajadas en días.
        trabajadas = datos.trabajadasFecha(where);
        dias = HoraHelper.redondeaDecimal(trabajadas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Trabajadas";
        e.Valor = HoraHelper.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas en días.
        horas = datos.acumuladasFecha(where);
        dias = HoraHelper.redondeaDecimal(horas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas";
        e.Valor = HoraHelper.textoDecimal(dias);
        datosEstadisticas.add(e);

        // Añadimos acumuladas hasta el final en días.
        dias = HoraHelper.redondeaDecimal(acumuladas / jornadaMedia);
        e = new Estadistica();
        e.Contador = contador;
        contador++;
        e.Texto = "Acumuladas hasta Final";
        e.Valor = HoraHelper.textoDecimal(dias);
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

    private void retrocedeMes() {
        if (añoActual == añoLimite && mesActual == mesLimite) {
            Toast.makeText(this, R.string.error_fechaLimite, Toast.LENGTH_SHORT).show();
            return;
        }
        int m = mesActual;
        int a = añoActual;
        mesActual--;
        if (mesActual == 0) {
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

    private void avanzaMes() {
        int m = mesActual;
        int a = añoActual;
        mesActual++;
        if (mesActual == 13) {
            mesActual = 1;
            añoActual++;
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

    private void retrocedeAño() {

        if (añoActual == añoLimite) {
            Toast.makeText(context, R.string.error_fechaLimite, Toast.LENGTH_SHORT).show();
        } else {
            añoActual--;
            rellenarAño();
        }

    }

    private void avanzaAño() {
        Cursor c = datos.cursorMes(1, añoActual + 1);
        if (c.getCount() == 0) {
            Toast.makeText(context, R.string.error_añoNoExiste, Toast.LENGTH_SHORT).show();
        } else {
            añoActual++;
            rellenarAño();
        }
        c.close();
    }

}
