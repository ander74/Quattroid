/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import BaseDatos.BaseDatos;
import BaseDatos.Incidencia;

public class EditarIncidencia extends Activity {

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    Incidencia incidencia = null;

    TextView Titulo = null;
    EditText textoIncidencia = null;
    RadioGroup Grupo = null;
    RadioButton Tipo1 = null;
    RadioButton Tipo2 = null;
    RadioButton Tipo3 = null;
    RadioButton Tipo4 = null;
    RadioButton Tipo5 = null;
    RadioButton Tipo6 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.calendario);
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_editarincidencia);

        // Inicialización de los elementos
        context = this;
        Titulo = findViewById(R.id.tv_nuevaIncidencia);
        textoIncidencia = findViewById(R.id.et_nuevaIncidencia);
        Grupo = findViewById(R.id.grupoTipos);
        Tipo1 = findViewById(R.id.tipo1);
        Tipo2 = findViewById(R.id.tipo2);
        Tipo3 = findViewById(R.id.tipo3);
        Tipo4 = findViewById(R.id.tipo4);
        Tipo5 = findViewById(R.id.tipo5);
        Tipo6 = findViewById(R.id.tipo6);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);

        // Definir si vamos a editar una incidencia o crear una nueva.
        Bundle data = getIntent().getExtras();
        if (data == null) {
            // CREAMOS UNA NUEVA

            Titulo.setText("NUEVA INCIDENCIA");
            // Creamos la incidencia
            incidencia = new Incidencia();
            incidencia.setCodigo(datos.codigoNuevaIncidencia());
            // Escribimos el título
            getActionBar().setTitle("Incidencia " + String.valueOf(incidencia.getCodigo()));
            getActionBar().setSubtitle(null);
        } else {
            // EDITAMOS UNA INCIDENCIA

            Titulo.setText("EDITAR INCIDENCIA");
            int cod = data.getInt("Codigo", 0);
            // Si no hay datos, salimos.
            if (cod == 0) finish();
            // Si la incidencia esta protegida, bloquear el grupo.
            if (cod < 17) {
                Grupo.setEnabled(false);
                Tipo1.setEnabled(false);
                Tipo2.setEnabled(false);
                Tipo3.setEnabled(false);
                Tipo4.setEnabled(false);
                Tipo5.setEnabled(false);
                Tipo6.setEnabled(false);
            }
            // Traemos la incidencia.
            incidencia = datos.getIncidencia(cod);
            // Si no existe salimos.
            if (incidencia.getCodigo() == 0) finish();
            // Llenamos los campos.
            getActionBar().setTitle("Incidencia " + String.valueOf(incidencia.getCodigo()));
            getActionBar().setSubtitle(null);
            textoIncidencia.setText(incidencia.getTexto());
            switch (incidencia.getTipo()) {
                case 1:
                    Tipo1.setChecked(true);
                    break;
                case 2:
                    Tipo2.setChecked(true);
                    break;
                case 3:
                    Tipo3.setChecked(true);
                    break;
                case 4:
                    Tipo4.setChecked(true);
                    break;
                case 5:
                    Tipo5.setChecked(true);
                    break;
                case 6:
                    Tipo6.setChecked(true);
                    break;
            }
        }

    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editarincidencia, menu);
        return true;
    }

    // AL PULSAR UNA OPCION DEL MENÚ SUPERIOR.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.bt_guardar:
                Guardar();
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
            if (datos.opciones.isGuardarSiempre()) {
                Guardar();
            } else {
                setResult(RESULT_CANCELED);
            }
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

    public void ModificarIncidencias() {
        datos.modificarIncidencias(incidencia.getCodigo(), incidencia.getTexto());
    }


    private void Guardar() {
        incidencia.setTexto(textoIncidencia.getText().toString());
        int tip;
        switch (Grupo.getCheckedRadioButtonId()) {
            case R.id.tipo1:
                tip = 1;
                break;
            case R.id.tipo2:
                tip = 2;
                break;
            case R.id.tipo3:
                tip = 3;
                break;
            case R.id.tipo4:
                tip = 4;
                break;
            case R.id.tipo5:
                tip = 5;
                break;
            case R.id.tipo6:
                tip = 6;
                break;
            default:
                tip = 0;
        }
        incidencia.setTipo(tip);
        datos.setIncidencia(incidencia);
        if (incidencia.getCodigo() < 17) ModificarIncidencias();
        setResult(RESULT_OK, null);

    }

}
