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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quattro.dropbox.SincronizarTask;
import com.quattro.dropbox.Soporte;

import BaseDatos.BaseDatos;
import Objetos.Hora;
import Objetos.Utils;


public class Principal extends Activity implements View.OnLongClickListener {

    // CONSTANTES
    final static int ACCION_LICENCIA = 1;
    final static int PEDIR_PERMISO_ESCRITURA = 2;
    public static boolean ACTUALIZAR;

    // VARIABLES
    Context context;
    SharedPreferences opciones = null;
    Boolean TienePermisoEscritura = false;

    // ELEMENTOS
	LinearLayout lyTitulo = null;
	TextView textTitulo = null;
    TextView tvActualizando = null;


    // AL CREARSE LA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_principal);

        // Establecemos el contexto
        context = this;

        // Declaramos los elementos visuales
        tvActualizando = (TextView) findViewById(R.id.tv_actualizando);
        textTitulo = (TextView) findViewById(R.id.textTitulo);
        lyTitulo = (LinearLayout) findViewById(R.id.ly_titulo);
        
        // Establecemos los listeners
	    lyTitulo.setOnLongClickListener(this);

        // Flag para actualizar
        ACTUALIZAR = true;

        // Evaluamos si es la primera vez que se ejecuta la aplicación.
        opciones = PreferenceManager.getDefaultSharedPreferences(context);
        if (opciones.getBoolean("PrimerInicio", true)){
            Intent intent = new Intent(this, Licencia.class);
            startActivityForResult(intent, ACCION_LICENCIA);
        }

        // Evaluamos el permiso de escritura para Android 6 y superior.
        TienePermisoEscritura = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!TienePermisoEscritura){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PEDIR_PERMISO_ESCRITURA);
        }

        if (!opciones.contains("JorMedia")) {
            // Creamos las nuevas preferencias de jornada media y minima.
            long jorMedia = Double.doubleToLongBits(Hora.horaToDecimal(opciones.getInt("JornadaMedia", 465)));
            opciones.edit().putLong("JorMedia", jorMedia).apply();

            long jorMinima = Double.doubleToLongBits(Hora.horaToDecimal(opciones.getInt("JornadaMinima", 420)));
            opciones.edit().putLong("JorMinima", jorMinima).apply();
        }


        if (opciones.getBoolean("IniciarCalendario", false)){
            Intent intent = new Intent(this, Calendario.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

    }

    // AL VOLVER DE LA PETICIÓN DE PERMISOS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PEDIR_PERMISO_ESCRITURA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Recargar la activity
                } else {
                    Toast.makeText(Principal.this, "La aplicación no tiene permiso para acceder al almacenamiento." +
                            "\nSu funcionamiento estará limitado.", Toast.LENGTH_LONG).show();
                }
        }
    }

    // AL REGRESAR DE UNA SUBACTIVITY
    @Override
    public void onActivityResult(int codigo, int resultado, Intent datos){
        super.onActivityResult(codigo, resultado, datos);
        switch (codigo){
            case ACCION_LICENCIA:
                if (resultado == RESULT_OK) {
                    // Establecer la opcion PrimerInicio en false
                    opciones.edit().putBoolean("PrimerInicio", false).commit();
                } else {
                    finish();
                }
                break;
        }
    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            // Intentar apagar todas las activities.
            try{
                Calendario.activityCalendario.finish();
            } catch (NullPointerException e){

            }

        }
        return super.onKeyDown(keyCode, event);
    }

    // AL INICIARSE
    @Override
    protected void onStart() {
        super.onStart();

        // Borramos el texto Actualizando
        tvActualizando.setText("");

        // Si no hay cambios, salimos.
        if (!BaseDatos.hayCambios && !ACTUALIZAR) return;
        // Si no hay que sincronizar, salimos.
        if (!opciones.getBoolean("SincronizarDropBox", false)) return;
        // Si internet no está disponible, salimos
        if (!Utils.hayInternet(this)) return;

        // Si sólo conectamos con wifi...
        if (opciones.getBoolean("SincronizarSoloWifi", false)) {
            // Si hay wifi
            if (Utils.hayWifi(this)) {
                tvActualizando.setText("Actualizando...");
                SincronizarDropBox();
            }
        // Si no sólo conectamos con wifi...
        } else {
            tvActualizando.setText("Actualizando...");
            SincronizarDropBox();
        }
    }

    // AL FINALIZAR
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // AL PULSAR UN BOTON
    public void BotonPulsado(View view){

        Intent intent = null;

        // Evaluar el view que ha llamado al método.
        switch (view.getId()){

            // Botón Calendario Pulsado
            case R.id.bt_calendario:
                intent = new Intent(this, Calendario.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;

            // Botón Relevos Pulsado
            case R.id.bt_relevos:
                intent = new Intent(this, Relevos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("Menu", true);
                startActivity(intent);
                break;

            // Botón Servicios Pulsado
            case R.id.bt_servicios:
                intent = new Intent(this, Lineas.class);
                startActivity(intent);
                break;

            // Botón Horas Ajenas Pulsado
            case R.id.bt_ajenas:
                intent = new Intent(this, HorasAjenas.class);
                startActivity(intent);
                break;

            // Botón Herramientas Pulsado
            case R.id.bt_herramientas:
                intent = new Intent(this, Herramientas.class);
                startActivity(intent);
                break;
        }

    }

    // AL PULSAR EL TITULO
    public void tituloPulsado(View view){
        Intent intent = new Intent(this, Creditos.class);
        startActivity(intent);
    }

    // SINCRONIZAR CON DROPBOX
    private void SincronizarDropBox(){

        final String dropboxCredential = opciones.getString("DropboxCredential", null);

        if (dropboxCredential != null) {

            new SincronizarTask(new SincronizarTask.Callback() {
                @Override
                public void onComplete() {
                    tvActualizando.setText("Actualizado");
                    BaseDatos.hayCambios = false;
                    ACTUALIZAR = false;
                }

                @Override
                public void onError(Soporte.Resultado resultado) {
                    tvActualizando.setText("Error al actualizar");
                }
            }, this).execute();

        }

    }
	
	
    // AL PULSAR LARGO EN EL TÍTULO
	@Override
	public boolean onLongClick(View v)
	{
		if (v.getId() != R.id.ly_titulo) return false;
		
		//TODO: Aquí es donde irá la llamada a alguna activity que nos muestre los datos que se requieran.
		Toast.makeText(this,"Has pulsado el título" , Toast.LENGTH_SHORT).show();
		
		return true;
	}
	
	
}
