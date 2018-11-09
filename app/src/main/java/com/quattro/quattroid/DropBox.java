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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.android.Auth;
import com.quattro.dropbox.DescargarTask;
import com.quattro.dropbox.EmailTask;
import com.quattro.dropbox.Soporte;
import com.quattro.dropbox.SubirTask;

import BaseDatos.BaseDatos;
import Objetos.Utils;

public class DropBox extends Activity {

    // ENUMERACION MODO
    public enum ModoVista {
        AUTORIZACION, PETICION_COPIA, PRIMERA_OPERACION, PRINCIPAL,
    }

    // CONSTANTES

    // ELEMENTOS GRÁFICOS
    TextView TxtCuentaDropbox;
    LinearLayout LyAutorizacion;
    LinearLayout LyPeticionCopia;
    LinearLayout LyPrimeraOperación;
    LinearLayout LyPrincipal;
    CheckBox CbAutoSincronizar;
    CheckBox CbSoloWifi;

    // PREFERENCIAS
    SharedPreferences Opciones = null;

    /**
     * AL CREARSE LA ACTIVITY
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.dropbox);
        getActionBar().setTitle("Dropbox");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_dropbox);

        // Establecemos las opciones.
        Opciones = PreferenceManager.getDefaultSharedPreferences(this);

        // Establcemos los elementos visuales.
        TxtCuentaDropbox = (TextView) findViewById(R.id.txtCuentaDropbox);
        LyAutorizacion = (LinearLayout) findViewById(R.id.ly_autorizacion);
        LyPeticionCopia = (LinearLayout) findViewById(R.id.ly_peticionCopia);
        LyPrimeraOperación = (LinearLayout) findViewById(R.id.ly_primeraOperacion);
        LyPrincipal = (LinearLayout) findViewById(R.id.ly_principal);
        CbAutoSincronizar = (CheckBox) findViewById(R.id.cb_autoSincronizar);
        CbSoloWifi = (CheckBox) findViewById(R.id.cb_soloWifi);

        // Establecemos el estado de los checkboxes
        CbAutoSincronizar.setChecked(Opciones.getBoolean("SincronizarDropBox", false));
        CbSoloWifi.setChecked(Opciones.getBoolean("SincronizarSoloWifi", false));
        CbSoloWifi.setEnabled(CbAutoSincronizar.isChecked());

        // Al cambiar el estado del checkbox Autosincronizar.
        CbAutoSincronizar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Ponemos la opción de autosincronizar según el estado del checkbox.
                Opciones.edit().putBoolean("SincronizarDropBox", CbAutoSincronizar.isChecked()).apply();
                // Activamos o no el checkbox SoloWifi.
                CbSoloWifi.setEnabled(CbAutoSincronizar.isChecked());
            }
        });

        // Al cambiar el estado del checkbox SoloWifi.
        CbSoloWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Ponemos la opción de SoloWifi según el estado del checkbox.
                Opciones.edit().putBoolean("SincronizarSoloWifi", CbSoloWifi.isChecked()).apply();
            }
        });

    }

    /**
     * AL VOLVER DE UNA PAUSA
     */
    @Override
    protected void onResume() {
        super.onResume();
        ProcesosOnResume();
    }

    /**
     * PONER EL MODO DE VISION
     */
    private void setModo(ModoVista modo){
        switch (modo){

            case AUTORIZACION:
                LyAutorizacion.setVisibility(View.VISIBLE);
                LyPeticionCopia.setVisibility(View.GONE);
                LyPrimeraOperación.setVisibility(View.GONE);
                LyPrincipal.setVisibility(View.GONE);
                break;
            case PETICION_COPIA:
                LyAutorizacion.setVisibility(View.GONE);
                LyPeticionCopia.setVisibility(View.VISIBLE);
                LyPrimeraOperación.setVisibility(View.GONE);
                LyPrincipal.setVisibility(View.GONE);
                break;
            case PRIMERA_OPERACION:
                LyAutorizacion.setVisibility(View.GONE);
                LyPeticionCopia.setVisibility(View.GONE);
                LyPrimeraOperación.setVisibility(View.VISIBLE);
                LyPrincipal.setVisibility(View.GONE);
                break;
            case PRINCIPAL:
                LyAutorizacion.setVisibility(View.GONE);
                LyPeticionCopia.setVisibility(View.GONE);
                LyPrimeraOperación.setVisibility(View.GONE);
                LyPrincipal.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * PROCESOS EN EL ONRESUME
     */
    private void ProcesosOnResume(){
        // Si no nos hemos autenticado en DropBox...
        if (!Opciones.getBoolean("Logueado", false)){
            setModo(ModoVista.AUTORIZACION);
            return;
        }
        // Si es la primera vez que conectamos...
        if (Opciones.getBoolean("PrimerAccesoLogin", true)){
            setModo(ModoVista.PETICION_COPIA);
            return;
        }
        // Recuperamos el token de las opciones.
        String Token = Opciones.getString("Token", null);

        // Si el token no existe, lo pedimos
        if (Token == null){
            Token = Auth.getOAuth2Token();
            if (Token != null) {
                Opciones.edit().putString("Token", Token).apply();
            } else {
                Opciones.edit().putBoolean("Logueado", false).apply();
                setModo(ModoVista.AUTORIZACION);
                return;
            }
        }
        // Si es la primera vez que accedemos a DropBox...
        if (Opciones.getBoolean("PrimerAccesoDropBox", true)){
            setModo(ModoVista.PRIMERA_OPERACION);
        } else {
            setModo(ModoVista.PRINCIPAL);
            CbAutoSincronizar.setChecked(Opciones.getBoolean("SincronizarDropBox", false));
        }

        new EmailTask(Token, Opciones, new EmailTask.Callback() {
            @Override
            public void onComplete() {
                TxtCuentaDropbox.setText(Opciones.getString("EmailDropbox", "Sin cuenta habilitada"));
            }

            @Override
            public void onError(Soporte.Resultado resultado) {
                Toast.makeText(DropBox.this, "Se ha producido un problema con la cuenta.", Toast.LENGTH_LONG).show();
            }
        }).execute();

        // Introducimos el email de la cuenta de dropbox, si la hubiera.
        TxtCuentaDropbox.setText(Opciones.getString("EmailDropbox", "Sin cuenta habilitada"));

    }

    /**
     * AL PULSAR EL BOTON AUTORIZAR
     */
    public void BtAutorizarPulsado(View view){
        // Lanzamos la autorización por medio de Auth si hay internet.
        if (!Utils.hayInternet(this)) return;
        Auth.startOAuth2Authentication(getApplicationContext(), BuildConfig.DropboxAppKey);
        Opciones.edit().putBoolean("Logueado", true).apply();
    }

    /**
     * AL PULSAR EL BOTON CREAR CUENTA
     */
    public void BtCrearCuentaPulsado(View view){
        if (!Utils.hayInternet(this)) return;
        String enlace = "https://db.tt/3LzYtu3r";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(enlace));
        startActivity(intent);
    }

    /**
     * AL PULSAR EL BOTON SI HACER COPIA
     */
    public void BtSiHacerCopiaPulsado(View view){
        Opciones.edit().putBoolean("PrimerAccesoLogin", false).apply();
        Intent intent = new Intent(this, CopiaSeguridad.class);
        startActivity(intent);
        ProcesosOnResume();
    }

    /**
     * AL PULSAR EL BOTON NO HACER COPIA
     */
    public void BtNoHacerCopiaPulsado(View view){
        Opciones.edit().putBoolean("PrimerAccesoLogin", false).apply();
        ProcesosOnResume();
    }

    /**
     * AL PULSAR EL BOTON DESCARGAR PRIMERA
     */
    public void BtDescargarPrimeraPulsado(View view){
        if (Opciones.getString("Token", null) == null || !Utils.hayInternet(this)) return;
        Opciones.edit().putBoolean("PrimerAccesoDropBox", false).apply();
        BtDescargarPulsado(null);
        ProcesosOnResume();
    }

    /**
     * AL PULSAR EL BOTON SUBIR PRIMERA
     */
    public void BtSubirPrimeraPulsado(View view){
        if (Opciones.getString("Token", null) == null || !Utils.hayInternet(this)) return;
        Opciones.edit().putBoolean("PrimerAccesoDropBox", false).apply();
        BtSubirPulsado(null);
        ProcesosOnResume();
    }

    /**
     * AL PULSAR EL BOTON NO HACER NADA
     */
    public void BtNoHacerNadaPulsado(View view){
        Opciones.edit().putBoolean("PrimerAccesoDropBox", false).apply();
        ProcesosOnResume();
    }

    /**
     * AL PULSAR EL BOTON DESCARGAR
     */
    public void BtDescargarPulsado(View view){

        final String Token = Opciones.getString("Token", null);
        if (Token == null || !Utils.hayInternet(this)) return;

        Toast.makeText(DropBox.this, "Descargando...", Toast.LENGTH_SHORT).show();
        BaseDatos.hayCambios = false;

        new DescargarTask(Token, Opciones, new DescargarTask.Callback() {
            @Override
            public void onComplete() {
                Opciones.edit().putString("Token", Token).apply();
                Toast.makeText(DropBox.this, "Descarga completada.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Soporte.Resultado resultado) {
                Opciones.edit().putString("Token", Token).apply();
                Toast.makeText(DropBox.this, "Se ha producido un problema con la descarga.", Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    /**
     * AL PULSAR EL BOTON SUBIR
     */
    public void BtSubirPulsado(View view){

        String Token = Opciones.getString("Token", null);
        if (Token == null || !Utils.hayInternet(this)) return;

        Toast.makeText(DropBox.this, "Subiendo...", Toast.LENGTH_SHORT).show();
        BaseDatos.hayCambios = false;

        new SubirTask(Token, Opciones, new SubirTask.Callback() {
            @Override
            public void onComplete() {
                Toast.makeText(DropBox.this, "Subida completada.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Soporte.Resultado resultado) {
                Toast.makeText(DropBox.this, "Se ha producido un problema con la subida.", Toast.LENGTH_LONG).show();
            }
        }).execute();

    }

    /**
     * AL PULSAR EL BOTON REVOCAR
     */
    public void BtRevocarPulsado(View view){
        // Creamos el cuadro de alerta para confirmar.
        AlertDialog.Builder Confirmar = new AlertDialog.Builder(this);
        Confirmar.setTitle("ATENCION");
        Confirmar.setMessage("Vas a revocar el certificado de Dropbox\n¿Estás seguro?");
        Confirmar.setPositiveButton("SI", (dialog, which) ->
        {
            Opciones.edit().putString("Token", null).apply();
            Opciones.edit().putBoolean("Logueado", false).apply();
            Opciones.edit().putBoolean("PrimerAccesoDropBox", true).apply();
            Opciones.edit().putString("EmailDropbox", null).apply();
            ProcesosOnResume();
        });

        Confirmar.setNegativeButton("NO", (dialog, which) ->
        {
            // No hacemos nada más que cerrar el diálogo.
        });
        Confirmar.show();

    }

}
