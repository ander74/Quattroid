/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.ActivityHelpers;

import android.text.Editable;
import android.text.TextWatcher;

import com.quattroid.Helpers.Converters;
import com.quattroid.Helpers.DateHelper;

import BaseDatos.BaseDatos;
import Objetos.Hora;

public class AjustesTextWhatchers {

    private BaseDatos datos;

    public AjustesTextWhatchers(BaseDatos db) {
        datos = db;
    }

    // Primer mes mostrado
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher primerMesWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            Integer mes = Converters.getMesOrNull(editable.toString());
            if (mes == null) return;
            datos.opciones.setPrimerMes(mes);
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Primer año mostrado
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher primerAñoWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            Integer año = Converters.getEnteroOrNull(editable.toString());
            if (año == null) return;
            datos.opciones.setPrimerAño(año);
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Relevo fijo
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher relevoFijoWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            Integer relevo = Converters.getEnteroOrNull(editable.toString());
            if (relevo == null) return;
            datos.opciones.setRelevoFijo(relevo);
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Acumuladas anteriores
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher acumuladasAnterioresWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String horaDecimal = Hora.validaHoraDecimal(editable.toString());
            if (horaDecimal.isEmpty()) return;
            horaDecimal = horaDecimal.replace(",", ".");
            datos.opciones.setAcumuladasAnteriores(Double.parseDouble(horaDecimal));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Jornada media
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher jornadaMediaWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String horaDecimal = Hora.validaHoraDecimal(editable.toString());
            if (horaDecimal.isEmpty()) return;
            horaDecimal = horaDecimal.replace(",", ".");
            datos.opciones.setJornadaMedia(Double.parseDouble(horaDecimal));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Jornada mínima
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher jornadaMinimaWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String horaDecimal = Hora.validaHoraDecimal(editable.toString());
            if (horaDecimal.isEmpty()) return;
            horaDecimal = horaDecimal.replace(",", ".");
            datos.opciones.setJornadaMinima(Double.parseDouble(horaDecimal));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Límite entre servicios
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher limiteEntreServiciosWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String hora = Hora.horaToString(editable.toString());
            if (hora.isEmpty()) return;
            datos.opciones.setLimiteEntreServicios(Hora.horaToInt(hora));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Día de cierre del mes
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher diaCierreMesWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            Integer dia = Converters.getEnteroOrNull(editable.toString());
            if (dia == null) return;
            if (dia < 1 || dia > 28) return;
            datos.opciones.setDiaCierreMes(dia);
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Jornada anual
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher jornadaAnualWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            Integer jornada = Converters.getEnteroOrNull(editable.toString());
            if (jornada == null) return;
            datos.opciones.setJornadaAnual(jornada);
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Inicio nocturnas
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher inicioNocturnasWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String hora = Hora.horaToString(editable.toString());
            if (hora.isEmpty()) return;
            datos.opciones.setInicioNocturnas(Hora.horaToInt(hora));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Final nocturnas
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher finalNocturnasWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String hora = Hora.horaToString(editable.toString());
            if (hora.isEmpty()) return;
            datos.opciones.setFinalNocturnas(Hora.horaToInt(hora));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Desayuno
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher desayunoWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String hora = Hora.horaToString(editable.toString());
            if (hora.isEmpty()) return;
            datos.opciones.setLimiteDesayuno(Hora.horaToInt(hora));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Comida 1
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher comida1Whatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String hora = Hora.horaToString(editable.toString());
            if (hora.isEmpty()) return;
            datos.opciones.setLimiteComida1(Hora.horaToInt(hora));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Comida 2
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher comida2Whatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String hora = Hora.horaToString(editable.toString());
            if (hora.isEmpty()) return;
            datos.opciones.setLimiteComida2(Hora.horaToInt(hora));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Cena
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher cenaWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String hora = Hora.horaToString(editable.toString());
            if (hora.isEmpty()) return;
            datos.opciones.setLimiteCena(Hora.horaToInt(hora));
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Día base turnos
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher diaBaseTurnosWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            Integer dia = Converters.getEnteroOrNull(editable.toString());
            if (dia == null) return;
            if (!DateHelper.isDateValid(datos.opciones.getAñoBaseTurnos(), datos.opciones.getMesBaseTurnos(), dia)) return;
            datos.opciones.setDiaBaseTurnos(dia);
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };

    // Mes base turnos
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher mesBaseTurnosWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            Integer mes = Converters.getMesOrNull(editable.toString());
            if (mes == null) return;
            if (!DateHelper.isDateValid(datos.opciones.getAñoBaseTurnos(), mes, datos.opciones.getDiaBaseTurnos())) return;
            datos.opciones.setMesBaseTurnos(mes);
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


    // Año base turnos
    // ----------------------------------------------------------------------------------------------------
    public TextWatcher añoBaseTurnosWhatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            Integer año = Converters.getEnteroOrNull(editable.toString());
            if (año == null) return;
            if (!DateHelper.isDateValid(año, datos.opciones.getMesBaseTurnos(), datos.opciones.getDiaBaseTurnos())) return;
            datos.opciones.setAñoBaseTurnos(año);
            datos.guardarOpciones();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    };


}
