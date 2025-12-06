/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;

import com.quattroid.Helpers.DiaHelper;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import BaseDatos.BaseDatos;
import BaseDatos.Helpers;

public class Utilidades extends Activity {

    // CONSTANTES


    // VARIABLES
    BaseDatos datos = null;
    DateTime fechaParaFestivos = DateTime.now();

    // ELEMENTOS DEL VIEW
    TextView botonFindesComoFranqueo = null;
    TextView botonMarcarFestivos = null;
    TextView botonPegarDiaEnAño = null;
    TextView botonVaciarMes = null;
    TextView botonMarcaEnfermoMes = null;
    TextView botonMostrarAyuda = null;
    ScrollView scrollBotones = null;
    ScrollView scrollAyuda = null;
    TextView textoAyuda = null;


    //******************************************************************************************
    //region MÉTODOS OVERRIDE DE ACTIVITY

    // Al crear la activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_utilidades);

        // Instanciamos la base de datos
        datos = new BaseDatos(this);

        // Instanciamos los elementos.
        botonFindesComoFranqueo = findViewById(R.id.bt_findesComoFranqueo);
        botonMarcarFestivos = findViewById(R.id.bt_marcarFestivos);
        botonPegarDiaEnAño = findViewById(R.id.bt_pegarDiaEnAño);
        botonVaciarMes = findViewById(R.id.bt_vaciarMes);
        botonMarcaEnfermoMes = findViewById(R.id.bt_marcarEnfermoMes);
        botonMostrarAyuda = findViewById(R.id.bt_mostrarAyuda);
        scrollBotones = findViewById(R.id.scrollBotones);
        scrollAyuda = findViewById(R.id.scrollAyuda);
        textoAyuda = findViewById(R.id.textoAyuda);

        // Ocultar el scroll ayuda
        scrollAyuda.setVisibility(View.GONE);

        // Llenar el texto de ayuda con el contenido del archivo
        textoAyuda.setText(HtmlCompat.fromHtml(leeTexto(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Registrar los listeners
        botonFindesComoFranqueo.setOnClickListener(this::botonFindesComoFranqueoPulsado);
        botonMarcarFestivos.setOnClickListener(this::botonMarcarFestivosPulsado);
        botonPegarDiaEnAño.setOnClickListener(this::botonPegarDiaEnAñoPulsado);
        botonVaciarMes.setOnClickListener(this::botonVaciarMesPulsado);
        botonMarcaEnfermoMes.setOnClickListener(this::botonMarcaEnfermoMesPulsado);
        botonMostrarAyuda.setOnClickListener(this::botonMostrarAyudaPulsado);

    }


    // Al pulsar una tecla
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    // Al finalizar
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //endregion
    //******************************************************************************************


    //******************************************************************************************
    //region HANDLERS DE BOTONES


    private void botonFindesComoFranqueoPulsado(View view) {
        int año = DateTime.now().getYear();
        AlertDialog.Builder pedirAño = new AlertDialog.Builder(this);
        final LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);
        final TextView texto = new TextView(this);
        texto.setTextSize(16);
        texto.setText("Introduce el año");
        texto.setPadding(10, 10, 10, 10);
        final EditText input = new EditText(this);
        input.setBackgroundResource(R.drawable.fondo_dialogo);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        pedirAño.setPositiveButton("Aceptar", (dialog, which) -> {
            int year = Integer.valueOf(input.getText().toString());
            AlertDialog.Builder aviso = new AlertDialog.Builder(this);
            aviso.setTitle("ATENCION");
            String mensaje = String.format("Vas a poner como franqueo todos los sábados y domingos de %s.\n\n¿Estás seguro?", year);
            aviso.setMessage(mensaje);
            aviso.setPositiveButton("SI", (dialog2, which2) -> {
                Helpers.SetFindesComoFranqueos(year, this);
                Toast.makeText(this, "Franqueos establecidos.", Toast.LENGTH_SHORT).show();
            });
            aviso.setNegativeButton("NO", (dialog2, which2) -> {
            });
            aviso.show();
        });
        pedirAño.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        input.setText(String.valueOf(año));
        input.selectAll();
        linear.addView(texto);
        linear.addView(input);
        pedirAño.setView(linear);
        pedirAño.show();
    }


    private void botonMarcarFestivosPulsado(View view) {
        AlertDialog.Builder pedirAño = new AlertDialog.Builder(this);
        View vista = getLayoutInflater().inflate(R.layout.alert_seleccion_fecha, null);
        DatePicker selectorFecha = vista.findViewById(R.id.dp_fecha);
        selectorFecha.updateDate(fechaParaFestivos.getYear(), fechaParaFestivos.getMonthOfYear() - 1, fechaParaFestivos.getDayOfMonth());
        pedirAño.setView(vista);
        pedirAño.setTitle("Elige fecha de festivo");
        pedirAño.setPositiveButton("Fija festivo", (dialog, which) -> {
            DateTime fecha = new DateTime(selectorFecha.getYear(), selectorFecha.getMonth() + 1, selectorFecha.getDayOfMonth(), 0, 0);
            Helpers.MarcarFestivo(fecha, this);
            fechaParaFestivos = fecha;
            Toast.makeText(this, "Festivo fijado correctamente.", Toast.LENGTH_SHORT).show();
            botonMarcarFestivosPulsado(view);
        });
        pedirAño.setNegativeButton("Salir", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = pedirAño.create();
        dialog.show();

    }


    private void botonPegarDiaEnAñoPulsado(View view) {
        if (DiaHelper.DiaEnPortapapeles == null) {
            Toast.makeText(this, "No hay ningún día en el portapapeles..", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder pedirAño = new AlertDialog.Builder(this);
        View vista = getLayoutInflater().inflate(R.layout.alert_seleccion_fecha, null);
        DatePicker selectorFecha = vista.findViewById(R.id.dp_fecha);
        pedirAño.setView(vista);
        pedirAño.setTitle("Desde que fecha");
        pedirAño.setPositiveButton("Aceptar", (dialog, which) -> {
            DateTime fecha = new DateTime(selectorFecha.getYear(), selectorFecha.getMonth() + 1, selectorFecha.getDayOfMonth(), 0, 0);
            AlertDialog.Builder aviso = new AlertDialog.Builder(this);
            aviso.setTitle("ATENCION");
            String textoFecha = fecha.toString("dd/MM/yyyy");
            String mensaje = String.format("Vas a pegar el día que está en el portapapeles a todos los días a partir del %s.\n\nSe respetará el turno.\n\n¿Estás seguro?", textoFecha);
            aviso.setMessage(mensaje);
            aviso.setPositiveButton("SI", (dialog2, which2) -> {
                Helpers.PegarDiaEnAño(fecha, this);
                Toast.makeText(this, "Días pegados correctamente.", Toast.LENGTH_SHORT).show();
            });
            aviso.setNegativeButton("NO", (dialog2, which2) -> {
            });
            aviso.show();
        });
        pedirAño.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        pedirAño.show();
    }


    private void botonVaciarMesPulsado(View view) {
        AlertDialog.Builder pedirAño = new AlertDialog.Builder(this);
        View vista = getLayoutInflater().inflate(R.layout.alert_seleccion_fecha, null);
        DatePicker selectorFecha = vista.findViewById(R.id.dp_fecha);
        pedirAño.setView(vista);
        pedirAño.setTitle("Mes a borrar");
        pedirAño.setPositiveButton("Aceptar", (dialog, which) -> {
            DateTime fecha = new DateTime(selectorFecha.getYear(), selectorFecha.getMonth() + 1, selectorFecha.getDayOfMonth(), 0, 0);
            AlertDialog.Builder aviso = new AlertDialog.Builder(this);
            aviso.setTitle("ATENCION");
            String textoFecha = fecha.toString("MMMM - yyyy");
            String mensaje = String.format("Vas a vaciar todos los días del mes de %s.\n\n¿Estás seguro?", textoFecha);
            aviso.setMessage(mensaje);
            aviso.setPositiveButton("SI", (dialog2, which2) -> {
                Helpers.VaciarDiasMes(fecha, this);
                Toast.makeText(this, "Mes vaciado correctamente.", Toast.LENGTH_SHORT).show();
            });
            aviso.setNegativeButton("NO", (dialog2, which2) -> {
            });
            aviso.show();
        });
        pedirAño.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        pedirAño.show();
    }


    private void botonMarcaEnfermoMesPulsado(View view) {
        AlertDialog.Builder pedirAño = new AlertDialog.Builder(this);
        View vista = getLayoutInflater().inflate(R.layout.alert_seleccion_fecha, null);
        DatePicker selectorFecha = vista.findViewById(R.id.dp_fecha);
        pedirAño.setView(vista);
        pedirAño.setTitle("Mes a marcar");
        pedirAño.setPositiveButton("Aceptar", (dialog, which) -> {
            DateTime fecha = new DateTime(selectorFecha.getYear(), selectorFecha.getMonth() + 1, selectorFecha.getDayOfMonth(), 0, 0);
            AlertDialog.Builder aviso = new AlertDialog.Builder(this);
            aviso.setTitle("ATENCION");
            String textoFecha = fecha.toString("MMMM - yyyy");
            String mensaje = String.format("Vas a cambiar trabajo por enfermo todos los días del mes de %s.\n\n¿Estás seguro?", textoFecha);
            aviso.setMessage(mensaje);
            aviso.setPositiveButton("SI", (dialog2, which2) -> {
                Helpers.MarcarEnfermoMes(fecha, this);
                Toast.makeText(this, "Mes cambiado correctamente.", Toast.LENGTH_SHORT).show();
            });
            aviso.setNegativeButton("NO", (dialog2, which2) -> {
            });
            aviso.show();
        });
        pedirAño.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        pedirAño.show();
    }


    private void botonMostrarAyudaPulsado(View view) {
        if (scrollBotones.getVisibility() == View.VISIBLE) {
            scrollBotones.setVisibility(View.GONE);
            scrollAyuda.setVisibility(View.VISIBLE);
            botonMostrarAyuda.setText(R.string.bt_cerrarAyuda);
        } else {
            scrollAyuda.setVisibility(View.GONE);
            scrollBotones.setVisibility(View.VISIBLE);
            botonMostrarAyuda.setText(R.string.bt_mostrarAyuda);
        }
    }


    //endregion
    //******************************************************************************************


    //******************************************************************************************
    //region Métodos privados

    // LEE EL ARCHIVO DE AYUDA Y LO DEVUELVE EN UNA CADENA
    private String leeTexto() {
        // Cadenas de texto que se usarán
        StringBuilder texto = new StringBuilder();
        // Manager de la carpeta Assets
        AssetManager am = getAssets();
        try (InputStream is = am.open("ayuda_utilidades.html");
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            // Leemos la primera línea del archivo.
            String linea = br.readLine();
            // Mientras las líneas existan, las leemos
            while (linea != null) {
                texto.append(linea).append("\n");
                linea = br.readLine();
            }
        } catch (IOException e) {
            // No hacemos nada.
        }
        return texto.toString();
    }

    //endregion
    //******************************************************************************************


}
