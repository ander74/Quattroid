/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import BaseDatos.BaseDatos;
import BaseDatos.Relevo;
import Objetos.Colores;

public class EditarRelevo extends Activity implements View.OnFocusChangeListener {

    // VARIABLES
    Context context = null;
    BaseDatos datos = null;
    Bundle data = null;
    int deudaRelevo = 0;

    TextView titulo = null;
    TextView deudaTotal = null;
    EditText matricula = null;
    EditText nombre = null;
    EditText apellidos = null;
    EditText telefono = null;
    EditText deuda = null;
    EditText notas = null;
    RadioGroup grupo = null;
    RadioButton malo = null;
    RadioButton normal = null;
    RadioButton bueno = null;
    Button botonLlamar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.usuario);
        getActionBar().setTitle("Compañeros/as");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_editarrelevo);

        // Inicialización de los elementos
        context = this;
        titulo = findViewById(R.id.tv_titulo);
        deudaTotal = findViewById(R.id.tv_deudaTotal);
        matricula = findViewById(R.id.et_matricula);
        nombre = findViewById(R.id.et_nombre);
        apellidos = findViewById(R.id.et_apellidos);
        telefono = findViewById(R.id.et_telefono);
        deuda = findViewById(R.id.et_deuda);
        notas = findViewById(R.id.et_notas);
        grupo = findViewById(R.id.rg_calificacion);
        malo = findViewById(R.id.rb_malo);
        normal = findViewById(R.id.rb_normal);
        bueno = findViewById(R.id.rb_bueno);
        botonLlamar = findViewById(R.id.botonLlamar);

        // Inicialización de la base de datos
        datos = new BaseDatos(this);

        // Registrar los listeners
        deuda.setOnFocusChangeListener(this);
        matricula.setOnFocusChangeListener(this);
        botonLlamar.setOnClickListener(this::botonLlamarPulsado);
        telefono.setOnKeyListener((x, y, z) -> {
            if (telefono.getText().toString().isEmpty()) {
                botonLlamar.setVisibility(View.GONE);
            } else {
                botonLlamar.setVisibility(View.VISIBLE);
            }
            return false;
        });

        // Definir si vamos a editar una incidencia o crear una nueva.
        data = getIntent().getExtras();
        if (data == null) {
            // CREAMOS UN NUEVO RELEVO
            titulo.setText("NUEVO COMPAÑERO/A");
            matricula.setText("");
            nombre.setText("");
            apellidos.setText("");
            telefono.setText("");
            deuda.setText("0");
            notas.setText("");
            normal.setChecked(true);
            deudaTotal.setVisibility(View.GONE);
        } else {
            // EDITAMOS UN RELEVO
            titulo.setText("EDITAR COMPAÑERO/A");
            matricula.setText(String.valueOf(data.getInt("Matricula")));
            matricula.setTextColor(Colores.ROJO_OSCURO);
            matricula.setFocusable(false);
            deuda.setText(String.valueOf(data.getInt("Deuda")));
            nombre.setText(data.getString("Nombre"));
            apellidos.setText(data.getString("Apellidos"));
            telefono.setText(data.getString("Telefono"));
            notas.setText(data.getString("Notas"));
            switch (data.getInt("Calificacion")) {
                case 1:
                    bueno.setChecked(true);
                    break;
                case 2:
                    malo.setChecked(true);
                    break;
                default:
                    normal.setChecked(true);
            }
            deudaRelevo = datos.deudaRelevo(data.getInt("Matricula", 0));
            escribeDeuda(deudaRelevo);
            deudaRelevo -= data.getInt("Deuda");
        }

        // Activar o no el botón llamar si hay un teléfono metido.
        if (telefono.getText().toString().isEmpty()) {
            botonLlamar.setVisibility(View.GONE);
        } else {
            botonLlamar.setVisibility(View.VISIBLE);
        }

    }

    // CREAR EL MENÚ SUPERIOR.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editarrelevo, menu);
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

    // AL CAMBIAR EL FOCO DE UN CAMPO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.et_matricula:
                    try {
                        int x = Integer.valueOf(matricula.getText().toString());
                    } catch (NumberFormatException e) {
                        matricula.setText("");
                    }
                    break;
                case R.id.et_deuda:
                    try {
                        int x = Integer.valueOf(deuda.getText().toString());
                        escribeDeuda(deudaRelevo + x);
                    } catch (NumberFormatException e) {
                        deuda.setText("0");
                    }
                    break;
            }
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

    // ESCRIBE LA DEUDA
    private void escribeDeuda(int deuda) {

        if (deuda == 0) {
            deudaTotal.setVisibility(View.GONE);
            return;
        }
        String s = "";
        switch (deuda) {
            case 1:
                s = "Me debe un día.";
                deudaTotal.setTextColor(Colores.VERDE_OSCURO);
                break;
            case -1:
                s = "Le debo un día.";
                deudaTotal.setTextColor(Colores.ROJO_OSCURO);
                break;
            default:
                if (deuda > 1) {
                    s = "Me debe " + String.valueOf(deuda) + " días.";
                    deudaTotal.setTextColor(Colores.VERDE_OSCURO);
                }
                if (deuda < -1) {
                    deuda = deuda * -1;
                    s = "Le debo " + String.valueOf(deuda) + " días.";
                    deudaTotal.setTextColor(Colores.ROJO_OSCURO);
                }
        }
        deudaTotal.setText(s);
        deudaTotal.setVisibility(View.VISIBLE);
    }


    private void Guardar() {
        Relevo relevo = new Relevo();
        try {
            relevo.setMatricula(Integer.valueOf(matricula.getText().toString()));
        } catch (NumberFormatException e) {
            relevo.setMatricula(0);
        }
        relevo.setNombre(nombre.getText().toString());
        relevo.setApellidos(apellidos.getText().toString());
        relevo.setTelefono(telefono.getText().toString());
        relevo.setNotas(notas.getText().toString());
        try {
            relevo.setDeuda(Integer.valueOf(deuda.getText().toString()));
        } catch (NumberFormatException e) {
            relevo.setDeuda(0);
        }
        switch (grupo.getCheckedRadioButtonId()) {
            case R.id.rb_bueno:
                relevo.setCalificacion(1);
                break;
            case R.id.rb_malo:
                relevo.setCalificacion(2);
                break;
            default:
                relevo.setCalificacion(0);
        }
        if (datos.setRelevo(relevo)) {
            Toast.makeText(context, R.string.mensaje_nuevoRelevo, Toast.LENGTH_SHORT).show();
            datos.actualizarCalificacion(relevo.getMatricula(), relevo.getCalificacion());
            datos.actualizarRelevos(relevo.getMatricula(), relevo.getApellidos());
            Intent intent = new Intent();
            intent.putExtra("Matricula", relevo.getMatricula());
            intent.putExtra("Apellidos", relevo.getApellidos());
            setResult(RESULT_OK, intent);
        } else {
            Toast.makeText(context, R.string.error_nuevoRelevo, Toast.LENGTH_SHORT).show();
        }
    }


    private void botonLlamarPulsado(View v) {
        String telefono = data.getString("Telefono");
        String uri = "tel:" + telefono;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);

    }


}
