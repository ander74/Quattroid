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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;


import org.joda.time.LocalDate;

import BaseDatos.BaseDatos;
import Objetos.Hora;

public class Ajustes extends Activity implements View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {

    // CONSTANTES

    // VARIABLES
    Context context = null;
    //SharedPreferences opciones = null;
    BaseDatos datos = null;

    String pMes, pAño, hAnteriores, rFijo, jMedia, jMinima, lServicios, jAnual, iNocturnas, fNocturnas, dDesayuno, dComida1,
            dComida2, dCena, dTurnos, mTurnos, aTurnos;

    // ELEMENTOS DEL VIEW
    EditText primerMes = null;
    EditText primerAño = null;
    Switch verMesActual = null;
    EditText horasAnteriores = null;
    EditText relevoFijo = null;
    Switch rellenarSemana = null;
    Switch modoBasico = null;
    EditText jornadaMedia = null;
    EditText jornadaMinima = null;
    EditText limiteServicios = null;
    EditText jornadaAnual = null;
    Switch regularJornada = null;
    Switch regularBisiestos = null;
    EditText inicioNocturnas = null;
    EditText finalNocturnas = null;
    EditText desayuno = null;
    EditText comida1 = null;
    EditText comida2 = null;
    EditText cena = null;
    Switch pdfHorizontal = null;
    Switch pdfIncluirServicios = null;
    Switch pdfIncluirNotas = null;
    Switch pdfAgruparNotas = null;
    Switch sumarTomaDeje = null;
    Switch iniciarCalendario = null;
    Switch activarTecladoNumerico = null;
    Switch swInferirTurnos = null;
    EditText diaBaseTurnos = null;
    EditText mesBaseTurnos = null;
    EditText añoBaseTurnos = null;
    Switch swGuardarSiempre = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setLogo(R.drawable.ajustes);
        getActionBar().setTitle("Ajustes");
        getActionBar().setSubtitle(null);
        setContentView(R.layout.activity_ajustes);

        // Inicialización de los elementos
        context = this;
        datos = new BaseDatos(this);

        primerMes = findViewById(R.id.et_primerMes);
        primerAño = findViewById(R.id.et_primerAño);
        verMesActual = findViewById(R.id.sw_verMesActual);
        horasAnteriores = findViewById(R.id.et_acumuladasAnteriores);
        relevoFijo = findViewById(R.id.et_relevoFijo);
        rellenarSemana = findViewById(R.id.sw_rellenarSemana);
        modoBasico = findViewById(R.id.sw_modoBasico);
        jornadaMedia = findViewById(R.id.et_jornadaMedia);
        jornadaMinima = findViewById(R.id.et_jornadaMinima);
        limiteServicios = findViewById(R.id.et_limiteServicios);
        jornadaAnual = findViewById(R.id.et_jornadaAnual);
        regularJornada = findViewById(R.id.sw_regularJornada);
        regularBisiestos = findViewById(R.id.sw_RegularBisiestos);
        inicioNocturnas = findViewById(R.id.et_inicioNocturnas);
        finalNocturnas = findViewById(R.id.et_finalNocturnas);
        desayuno = findViewById(R.id.et_desayuno);
        comida1 = findViewById(R.id.et_comida1);
        comida2 = findViewById(R.id.et_comida2);
        cena = findViewById(R.id.et_cena);
        pdfHorizontal = findViewById(R.id.sw_pdfHorizontal);
        pdfIncluirServicios = findViewById(R.id.sw_pdfIncluirServicios);
        pdfIncluirNotas = findViewById(R.id.sw_pdfIncluirNotas);
        pdfAgruparNotas = findViewById(R.id.sw_pdfAgruparNotas);
        sumarTomaDeje = findViewById(R.id.sw_sumarTomaDeje);
        iniciarCalendario = findViewById(R.id.sw_iniarCalendario);
        activarTecladoNumerico = findViewById(R.id.sw_TecladoNumerico);
        swInferirTurnos = findViewById(R.id.sw_inferirTurnos);
        diaBaseTurnos = findViewById(R.id.et_diaBaseTurnos);
        mesBaseTurnos = findViewById(R.id.et_mesBaseTurnos);
        añoBaseTurnos = findViewById(R.id.et_añoBaseTurnos);
        swGuardarSiempre = findViewById(R.id.sw_guardarSiempre);

        // Inicializamos las opciones
        //opciones = PreferenceManager.getDefaultSharedPreferences(this);

//        // Llenamos las opciones con sus valores
//        primerMes.setText(String.valueOf(opciones.getInt("PrimerMes", 10)));
//        primerAño.setText(String.valueOf(opciones.getInt("PrimerAño", 2014)));
//
//        verMesActual.setChecked(opciones.getBoolean("VerMesActual", true));
//
//        long acumAnteriores = opciones.getLong("AcumuladasAnteriores", 0);
//        horasAnteriores.setText(Hora.textoDecimal(Double.longBitsToDouble(acumAnteriores)));
//
//        relevoFijo.setText(String.valueOf(opciones.getInt("RelevoFijo", 0)));
//	    modoBasico.setChecked(opciones.getBoolean("ModoBasico", false));
//        rellenarSemana.setChecked(opciones.getBoolean("RellenarSemana", false));
//
//        long jorMedia = opciones.getLong("JorMedia", 0);
//        jornadaMedia.setText(Hora.textoDecimal(Double.longBitsToDouble(jorMedia)));
//
//        long jorMinima = opciones.getLong("JorMinima", 0);
//        jornadaMinima.setText(Hora.textoDecimal(Double.longBitsToDouble(jorMinima)));
//
//        limiteServicios.setText(Hora.horaToString(opciones.getInt("LimiteEntreServicios", 60)));
//
//        jornadaAnual.setText(String.valueOf(opciones.getInt("JornadaAnual", 1592)));
//        regularJornada.setChecked(opciones.getBoolean("RegularJornadaAnual", true));
//        regularBisiestos.setChecked(opciones.getBoolean("RegularBisiestos", true));
//
//        inicioNocturnas.setText(Hora.horaToString(opciones.getInt("InicioNocturnas", 1320)));
//        finalNocturnas.setText(Hora.horaToString(opciones.getInt("FinalNocturnas", 390)));
//
//        desayuno.setText(Hora.horaToString(opciones.getInt("LimiteDesayuno", 270)));
//        comida1.setText(Hora.horaToString(opciones.getInt("LimiteComida1", 930)));
//        comida2.setText(Hora.horaToString(opciones.getInt("LimiteComida2", 810)));
//        cena.setText(Hora.horaToString(opciones.getInt("LimiteCena", 30)));
//
//        pdfHorizontal.setChecked(opciones.getBoolean("PdfHorizontal", false));
//        pdfIncluirServicios.setChecked(opciones.getBoolean("PdfIncluirServicios", false));
//        pdfIncluirNotas.setChecked(opciones.getBoolean("PdfIncluirNotas", false));
//        pdfAgruparNotas.setChecked(opciones.getBoolean("PdfAgruparNotas", false));
//
//        sumarTomaDeje.setChecked(opciones.getBoolean("SumarTomaDeje", false));
//        iniciarCalendario.setChecked(opciones.getBoolean("IniciarCalendario", false));
//        activarTecladoNumerico.setChecked(opciones.getBoolean("ActivarTecladoNumerico", false));
//
//        swInferirTurnos.setChecked(opciones.getBoolean("InferirTurnos", false));
//        diaBaseTurnos.setText(String.valueOf(opciones.getInt("DiaBaseTurnos", 3)));
//        mesBaseTurnos.setText(String.valueOf(opciones.getInt("MesBaseTurnos", 1)));
//        añoBaseTurnos.setText(String.valueOf(opciones.getInt("AñoBaseTurnos", 2021)));


        // Llenamos las opciones con sus valores desde la base de datos
        primerMes.setText(String.valueOf(datos.opciones.getPrimerMes()));
        primerAño.setText(String.valueOf(datos.opciones.getPrimerAño()));
        verMesActual.setChecked(datos.opciones.isVerMesActual());
        horasAnteriores.setText(Hora.textoDecimal(datos.opciones.getAcumuladasAnteriores()));
        relevoFijo.setText(String.valueOf(datos.opciones.getRelevoFijo()));
        modoBasico.setChecked(datos.opciones.isModoBasico());
        rellenarSemana.setChecked(datos.opciones.isRellenarSemana());
        jornadaMedia.setText(Hora.textoDecimal(datos.opciones.getJornadaMedia()));
        jornadaMinima.setText(Hora.textoDecimal(datos.opciones.getJornadaMinima()));
        limiteServicios.setText(Hora.horaToString(datos.opciones.getLimiteEntreServicios()));
        jornadaAnual.setText(String.valueOf(datos.opciones.getJornadaAnual()));
        regularJornada.setChecked(datos.opciones.isRegularJornadaAnual());
        regularBisiestos.setChecked(datos.opciones.isRegularBisiestos());
        inicioNocturnas.setText(Hora.horaToString(datos.opciones.getInicioNocturnas()));
        finalNocturnas.setText(Hora.horaToString(datos.opciones.getFinalNocturnas()));
        desayuno.setText(Hora.horaToString(datos.opciones.getLimiteDesayuno()));
        comida1.setText(Hora.horaToString(datos.opciones.getLimiteComida1()));
        comida2.setText(Hora.horaToString(datos.opciones.getLimiteComida2()));
        cena.setText(Hora.horaToString(datos.opciones.getLimiteCena()));
        pdfHorizontal.setChecked(datos.opciones.isPdfHorizontal());
        pdfIncluirServicios.setChecked(datos.opciones.isPdfIncluirServicios());
        pdfIncluirNotas.setChecked(datos.opciones.isPdfIncluirNotas());
        pdfAgruparNotas.setChecked(datos.opciones.isPdfAgruparNotas());
        sumarTomaDeje.setChecked(datos.opciones.isSumarTomaDeje());
        iniciarCalendario.setChecked(datos.opciones.isIniciarCalendario());
        activarTecladoNumerico.setChecked(datos.opciones.isActivarTecladoNumerico());
        swInferirTurnos.setChecked(datos.opciones.isInferirTurnos());
        diaBaseTurnos.setText(String.valueOf(datos.opciones.getDiaBaseTurnos()));
        mesBaseTurnos.setText(String.valueOf(datos.opciones.getMesBaseTurnos()));
        añoBaseTurnos.setText(String.valueOf(datos.opciones.getAñoBaseTurnos()));
        swGuardarSiempre.setChecked(datos.opciones.isGuardarSiempre());

        // Registrar los listeners
        primerMes.setOnFocusChangeListener(this);
        primerAño.setOnFocusChangeListener(this);
        horasAnteriores.setOnFocusChangeListener(this);
        relevoFijo.setOnFocusChangeListener(this);
        jornadaMedia.setOnFocusChangeListener(this);
        jornadaMinima.setOnFocusChangeListener(this);
        limiteServicios.setOnFocusChangeListener(this);
        jornadaAnual.setOnFocusChangeListener(this);
        inicioNocturnas.setOnFocusChangeListener(this);
        finalNocturnas.setOnFocusChangeListener(this);
        desayuno.setOnFocusChangeListener(this);
        comida1.setOnFocusChangeListener(this);
        comida2.setOnFocusChangeListener(this);
        cena.setOnFocusChangeListener(this);
        diaBaseTurnos.setOnFocusChangeListener(this);
        mesBaseTurnos.setOnFocusChangeListener(this);
        añoBaseTurnos.setOnFocusChangeListener(this);

        rellenarSemana.setOnCheckedChangeListener(this);
        modoBasico.setOnCheckedChangeListener(this);
        verMesActual.setOnCheckedChangeListener(this);
        sumarTomaDeje.setOnCheckedChangeListener(this);
        iniciarCalendario.setOnCheckedChangeListener(this);
        regularJornada.setOnCheckedChangeListener(this);
        regularBisiestos.setOnCheckedChangeListener(this);
        activarTecladoNumerico.setOnCheckedChangeListener(this);
        pdfHorizontal.setOnCheckedChangeListener(this);
        pdfIncluirServicios.setOnCheckedChangeListener(this);
        pdfIncluirNotas.setOnCheckedChangeListener(this);
        pdfAgruparNotas.setOnCheckedChangeListener(this);
        swInferirTurnos.setOnCheckedChangeListener(this);
        swGuardarSiempre.setOnCheckedChangeListener(this);



    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (primerMes.hasFocus()) {
                primerAño.requestFocus();
            } else {
                primerMes.requestFocus();
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL CAMBIAR EL FOCO UN CAMPO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        BaseDatos.hayCambios = true;

        if (hasFocus) {

            switch (v.getId()) {
                case R.id.et_primerMes:
                    pMes = primerMes.getText().toString();
                    break;
                case R.id.et_primerAño:
                    pAño = primerAño.getText().toString();
                    break;
                case R.id.et_acumuladasAnteriores:
                    hAnteriores = horasAnteriores.getText().toString();
                    break;
                case R.id.et_relevoFijo:
                    rFijo = relevoFijo.getText().toString();
                    break;
                case R.id.et_jornadaMedia:
                    jMedia = jornadaMedia.getText().toString();
                    break;
                case R.id.et_jornadaMinima:
                    jMinima = jornadaMinima.getText().toString();
                    break;
                case R.id.et_limiteServicios:
                    lServicios = limiteServicios.getText().toString();
                    break;
                case R.id.et_jornadaAnual:
                    jAnual = jornadaAnual.getText().toString();
                    break;
                case R.id.et_inicioNocturnas:
                    iNocturnas = inicioNocturnas.getText().toString();
                    break;
                case R.id.et_finalNocturnas:
                    fNocturnas = finalNocturnas.getText().toString();
                    break;
                case R.id.et_desayuno:
                    dDesayuno = desayuno.getText().toString();
                    break;
                case R.id.et_comida1:
                    dComida1 = comida1.getText().toString();
                    break;
                case R.id.et_comida2:
                    dComida2 = comida2.getText().toString();
                    break;
                case R.id.et_cena:
                    dCena = cena.getText().toString();
                    break;
                case R.id.et_diaBaseTurnos:
                    dTurnos = diaBaseTurnos.getText().toString();
                    break;
                case R.id.et_mesBaseTurnos:
                    mTurnos = mesBaseTurnos.getText().toString();
                    break;
                case R.id.et_añoBaseTurnos:
                    aTurnos = añoBaseTurnos.getText().toString();
                    break;
            }
        } else {

            Integer i = 0;
            switch (v.getId()) {
                case R.id.et_primerMes:
                    i = entero(primerMes.getText().toString());
                    if (i == null) {
                        primerMes.setText(pMes);
                        i = Integer.valueOf(pMes);
                    } else {
                        if (i < 1 || i > 12) {
                            primerMes.setText(pMes);
                            i = Integer.valueOf(pMes);
                        }
                    }
                    //opciones.edit().putInt("PrimerMes", i).apply();
                    datos.opciones.setPrimerMes(i);
                    datos.guardarOpciones();
                    break;
                case R.id.et_primerAño:
                    i = entero(primerAño.getText().toString());
                    if (i == null) {
                        primerAño.setText(pAño);
                        i = Integer.valueOf(pAño);
                    } else {
                        if (i < 2000 || i > 2050) {
                            i = Integer.valueOf(pAño);
                            primerAño.setText(pAño);
                        }
                    }
                    //opciones.edit().putInt("PrimerAño", i).apply();
                    datos.opciones.setPrimerAño(i);
                    datos.guardarOpciones();
                    break;
                case R.id.et_acumuladasAnteriores:
                    if (Hora.validaHoraDecimal(horasAnteriores.getText().toString()).equals("")) {
                        horasAnteriores.setText(hAnteriores);
                    }
                    //long l = Double.doubleToRawLongBits(Double.valueOf(horasAnteriores.getText().toString().replace(",", ".")));
                    //opciones.edit().putLong("AcumuladasAnteriores", l).apply();
                    datos.opciones.setAcumuladasAnteriores(Double.valueOf(horasAnteriores.getText().toString().replace(",", ".")));
                    datos.guardarOpciones();
                    horasAnteriores.setText(Hora.validaHoraDecimal(horasAnteriores.getText().toString()).replace(".", ","));
                    break;
                case R.id.et_relevoFijo:
                    i = entero(relevoFijo.getText().toString());
                    if (i == null) {
                        relevoFijo.setText(rFijo);
                        i = Integer.valueOf(rFijo);
                    }
                    //opciones.edit().putInt("RelevoFijo", i).apply();
                    datos.opciones.setRelevoFijo(i);
                    datos.guardarOpciones();
                    break;
                case R.id.et_jornadaMedia:
                    if (Hora.validaHoraDecimal(jornadaMedia.getText().toString()).equals("")) {
                        jornadaMedia.setText(jMedia);
                    }
                    //long jmed = Double.doubleToRawLongBits(Double.valueOf(jornadaMedia.getText().toString().replace(",", ".")));
                    //opciones.edit().putLong("JorMedia", jmed).apply();
                    datos.opciones.setJornadaMedia(Double.valueOf(jornadaMedia.getText().toString().replace(",", ".")));
                    datos.guardarOpciones();
                    jornadaMedia.setText(Hora.validaHoraDecimal(jornadaMedia.getText().toString()).replace(".", ","));
                    break;
                case R.id.et_jornadaMinima:
                    if (Hora.validaHoraDecimal(jornadaMinima.getText().toString()).equals("")) {
                        jornadaMinima.setText(jMinima);
                    }
                    //long jmin = Double.doubleToRawLongBits(Double.valueOf(jornadaMinima.getText().toString().replace(",", ".")));
                    //opciones.edit().putLong("JorMinima", jmin).apply();
                    datos.opciones.setJornadaMinima(Double.valueOf(jornadaMinima.getText().toString().replace(",", ".")));
                    datos.guardarOpciones();
                    jornadaMinima.setText(Hora.validaHoraDecimal(jornadaMinima.getText().toString()).replace(".", ","));
                    break;
                case R.id.et_limiteServicios:
                    if (Hora.horaToString(limiteServicios.getText().toString()).equals("")) {
                        limiteServicios.setText(lServicios);
                    }
                    limiteServicios.setText(Hora.horaToString(limiteServicios.getText().toString()));
                    //opciones.edit().putInt("LimiteEntreServicios", Hora.horaToInt(limiteServicios.getText().toString())).apply();
                    datos.opciones.setLimiteEntreServicios(Hora.horaToInt(limiteServicios.getText().toString()));
                    datos.guardarOpciones();
                    break;
                case R.id.et_jornadaAnual:
                    i = entero(jornadaAnual.getText().toString());
                    if (i == null) {
                        jornadaAnual.setText(jAnual);
                        i = Integer.valueOf(jAnual);
                    } else {
                        if (i < 0 || i > 8760) {
                            i = Integer.valueOf(jAnual);
                        }
                    }
                    //opciones.edit().putInt("JornadaAnual", i).apply();
                    datos.opciones.setJornadaAnual(i);
                    datos.guardarOpciones();
                    break;
                case R.id.et_inicioNocturnas:
                    if (Hora.horaToString(inicioNocturnas.getText().toString()).equals("")) {
                        inicioNocturnas.setText(iNocturnas);
                    }
                    inicioNocturnas.setText(Hora.horaToString(inicioNocturnas.getText().toString()));
                    //opciones.edit().putInt("InicioNocturnas", Hora.horaToInt(inicioNocturnas.getText().toString())).apply();
                    datos.opciones.setInicioNocturnas(Hora.horaToInt(inicioNocturnas.getText().toString()));
                    datos.guardarOpciones();
                    break;
                case R.id.et_finalNocturnas:
                    if (Hora.horaToString(finalNocturnas.getText().toString()).equals("")) {
                        finalNocturnas.setText(fNocturnas);
                    }
                    finalNocturnas.setText(Hora.horaToString(finalNocturnas.getText().toString()));
                    //opciones.edit().putInt("FinalNocturnas", Hora.horaToInt(finalNocturnas.getText().toString())).apply();
                    datos.opciones.setFinalNocturnas(Hora.horaToInt(finalNocturnas.getText().toString()));
                    datos.guardarOpciones();
                    break;
                case R.id.et_desayuno:
                    if (Hora.horaToString(desayuno.getText().toString()).equals("")) {
                        desayuno.setText(dDesayuno);
                    }
                    desayuno.setText(Hora.horaToString(desayuno.getText().toString()));
                    //opciones.edit().putInt("LimiteDesayuno", Hora.horaToInt(desayuno.getText().toString())).apply();
                    datos.opciones.setLimiteDesayuno(Hora.horaToInt(desayuno.getText().toString()));
                    datos.guardarOpciones();
                    break;
                case R.id.et_comida1:
                    if (Hora.horaToString(comida1.getText().toString()).equals("")) {
                        comida1.setText(dComida1);
                    }
                    comida1.setText(Hora.horaToString(comida1.getText().toString()));
                    //opciones.edit().putInt("LimiteComida1", Hora.horaToInt(comida1.getText().toString())).apply();
                    datos.opciones.setLimiteComida1(Hora.horaToInt(comida1.getText().toString()));
                    datos.guardarOpciones();
                    break;
                case R.id.et_comida2:
                    if (Hora.horaToString(comida2.getText().toString()).equals("")) {
                        comida2.setText(dComida2);
                    }
                    comida2.setText(Hora.horaToString(comida2.getText().toString()));
                    //opciones.edit().putInt("LimiteComida2", Hora.horaToInt(comida2.getText().toString())).apply();
                    datos.opciones.setLimiteComida2(Hora.horaToInt(comida2.getText().toString()));
                    datos.guardarOpciones();
                    break;
                case R.id.et_cena:
                    if (Hora.horaToString(cena.getText().toString()).equals("")) {
                        cena.setText(dCena);
                    }
                    cena.setText(Hora.horaToString(cena.getText().toString()));
                    //opciones.edit().putInt("LimiteCena", Hora.horaToInt(cena.getText().toString())).apply();
                    datos.opciones.setLimiteCena(Hora.horaToInt(cena.getText().toString()));
                    datos.guardarOpciones();
                    break;
                case R.id.et_diaBaseTurnos:
                    i = entero(diaBaseTurnos.getText().toString());
                    if (i == null) {
                        diaBaseTurnos.setText(dTurnos);
                        i = Integer.valueOf(dTurnos);
                    } else {
                        if (i < 1 || i > 31) {
                            diaBaseTurnos.setText(dTurnos);
                            i = Integer.valueOf(dTurnos);
                        } else {
                            Integer mes = entero(mesBaseTurnos.getText().toString());
                            Integer año = entero(añoBaseTurnos.getText().toString());
                            if (mes != null && año != null) {
                                LocalDate fecha = new LocalDate(año, mes, 1);
                                int diasMes = fecha.dayOfMonth().getMaximumValue();
                                if (i > diasMes) {
                                    i = diasMes;
                                    diaBaseTurnos.setText(i.toString());
                                }
                            }
                        }
                    }
                    //opciones.edit().putInt("DiaBaseTurnos", i).apply();
                    datos.opciones.setDiaBaseTurnos(i);
                    datos.guardarOpciones();
                    break;
                case R.id.et_mesBaseTurnos:
                    i = entero(mesBaseTurnos.getText().toString());
                    if (i == null) {
                        mesBaseTurnos.setText(mTurnos);
                        i = Integer.valueOf(mTurnos);
                    } else {
                        if (i < 1 || i > 12) {
                            mesBaseTurnos.setText(mTurnos);
                            i = Integer.valueOf(mTurnos);
                        } else {
                            Integer dia = entero(diaBaseTurnos.getText().toString());
                            Integer año = entero(añoBaseTurnos.getText().toString());
                            if (dia != null && año != null) {
                                LocalDate fecha = new LocalDate(año, i, 1);
                                int diasMes = fecha.dayOfMonth().getMaximumValue();
                                if (dia > diasMes) {
                                    diaBaseTurnos.setText(String.valueOf(diasMes));
                                }
                            }
                        }
                    }
                    //opciones.edit().putInt("MesBaseTurnos", i).apply();
                    datos.opciones.setMesBaseTurnos(i);
                    datos.guardarOpciones();
                    break;
                case R.id.et_añoBaseTurnos:
                    i = entero(añoBaseTurnos.getText().toString());
                    if (i == null) {
                        añoBaseTurnos.setText(aTurnos);
                        i = Integer.valueOf(aTurnos);
                    } else {
                        if (i < 2000 || i > 2050) {
                            añoBaseTurnos.setText(aTurnos);
                            i = Integer.valueOf(aTurnos);
                        }
                    }
                    //opciones.edit().putInt("AñoBaseTurnos", i).apply();
                    datos.opciones.setAñoBaseTurnos(i);
                    datos.guardarOpciones();
                    break;
            }
        }
    }

    // SI ES UN ENTERO LO DEVUELVE Y SI NO DEVUELVE NULO
    private Integer entero(String s) {
        Integer i;
        try {
            i = Integer.valueOf(s);
        } catch (NumberFormatException e) {
            i = null;
        }
        return i;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // AL CAMBIAR UN SWITCH
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        BaseDatos.hayCambios = true;

        switch (buttonView.getId()) {
            case R.id.sw_modoBasico:
                //opciones.edit().putBoolean("ModoBasico", modoBasico.isChecked()).apply();
                datos.opciones.setModoBasico(modoBasico.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_rellenarSemana:
                //opciones.edit().putBoolean("RellenarSemana", rellenarSemana.isChecked()).apply();
                datos.opciones.setRellenarSemana(rellenarSemana.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_verMesActual:
                //opciones.edit().putBoolean("VerMesActual", verMesActual.isChecked()).apply();
                datos.opciones.setVerMesActual(verMesActual.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_sumarTomaDeje:
                //opciones.edit().putBoolean("SumarTomaDeje", sumarTomaDeje.isChecked()).apply();
                datos.opciones.setSumarTomaDeje(sumarTomaDeje.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_iniarCalendario:
                //opciones.edit().putBoolean("IniciarCalendario", iniciarCalendario.isChecked()).apply();
                datos.opciones.setIniciarCalendario(iniciarCalendario.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_regularJornada:
                //opciones.edit().putBoolean("RegularJornadaAnual", regularJornada.isChecked()).apply();
                datos.opciones.setRegularJornadaAnual(regularJornada.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_RegularBisiestos:
                //opciones.edit().putBoolean("RegularBisiestos", regularBisiestos.isChecked()).apply();
                datos.opciones.setRegularBisiestos(regularBisiestos.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_TecladoNumerico:
                //opciones.edit().putBoolean("ActivarTecladoNumerico", activarTecladoNumerico.isChecked()).apply();
                datos.opciones.setActivarTecladoNumerico(activarTecladoNumerico.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_pdfHorizontal:
                //opciones.edit().putBoolean("PdfHorizontal", pdfHorizontal.isChecked()).apply();
                datos.opciones.setPdfHorizontal(pdfHorizontal.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_pdfIncluirServicios:
                //opciones.edit().putBoolean("PdfIncluirServicios", pdfIncluirServicios.isChecked()).apply();
                datos.opciones.setPdfIncluirServicios(pdfIncluirServicios.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_pdfIncluirNotas:
                //opciones.edit().putBoolean("PdfIncluirNotas", pdfIncluirNotas.isChecked()).apply();
                datos.opciones.setPdfIncluirNotas(pdfIncluirNotas.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_pdfAgruparNotas:
                //opciones.edit().putBoolean("PdfAgruparNotas", pdfAgruparNotas.isChecked()).apply();
                datos.opciones.setPdfAgruparNotas(pdfAgruparNotas.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_inferirTurnos:
                //opciones.edit().putBoolean("InferirTurnos", swInferirTurnos.isChecked()).apply();
                datos.opciones.setInferirTurnos(swInferirTurnos.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_guardarSiempre:
                datos.opciones.setGuardarSiempre(swGuardarSiempre.isChecked());
                datos.guardarOpciones();
                break;
        }

    }

}
