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
package com.quattroid.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.quattroid.activities.databinding.ActivityAjustesBinding;
import com.quattroid.activityHelpers.AjustesHelper;
import com.quattroid.helpers.HoraHelper;

import BaseDatos.BaseDatos;

public class Ajustes extends Activity implements View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {

    // CONSTANTES

    // VARIABLES
    private ActivityAjustesBinding binding;
    Context context = null;
    BaseDatos datos = null;
    AjustesHelper whatchers;

    String pMes, pAño, hAnteriores, rFijo, jMedia, jMinima, lServicios, cierreMes, jAnual, iNocturnas, fNocturnas, dDesayuno, dComida1,
            dComida2, dCena, dTurnos, mTurnos, aTurnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAjustesBinding.inflate(getLayoutInflater());
        getActionBar().setLogo(R.drawable.ajustes);
        getActionBar().setTitle("Ajustes");
        getActionBar().setSubtitle(null);
        setContentView(binding.getRoot());

        // Inicialización de los elementos
        context = this;
        datos = new BaseDatos(this);
        whatchers = new AjustesHelper(datos);

        // Llenamos las opciones con sus valores desde la base de datos
        binding.etPrimerMes.setText(String.valueOf(datos.opciones.getPrimerMes()));
        binding.etPrimerAO.setText(String.valueOf(datos.opciones.getPrimerAño()));
        binding.swVerMesActual.setChecked(datos.opciones.isVerMesActual());
        binding.etAcumuladasAnteriores.setText(HoraHelper.textoDecimal(datos.opciones.getAcumuladasAnteriores()));
        binding.etRelevoFijo.setText(String.valueOf(datos.opciones.getRelevoFijo()));
        binding.swModoBasico.setChecked(datos.opciones.isModoBasico());
        binding.swRellenarSemana.setChecked(datos.opciones.isRellenarSemana());
        binding.etJornadaMedia.setText(HoraHelper.textoDecimal(datos.opciones.getJornadaMedia()));
        binding.etJornadaMinima.setText(HoraHelper.textoDecimal(datos.opciones.getJornadaMinima()));
        binding.etLimiteServicios.setText(HoraHelper.horaToString(datos.opciones.getLimiteEntreServicios()));
        binding.etCierreMes.setText(String.valueOf(datos.opciones.getDiaCierreMes()));
        binding.etJornadaAnual.setText(String.valueOf(datos.opciones.getJornadaAnual()));
        binding.swRegularJornada.setChecked(datos.opciones.isRegularJornadaAnual());
        binding.swRegularBisiestos.setChecked(datos.opciones.isRegularBisiestos());
        binding.etInicioNocturnas.setText(HoraHelper.horaToString(datos.opciones.getInicioNocturnas()));
        binding.etFinalNocturnas.setText(HoraHelper.horaToString(datos.opciones.getFinalNocturnas()));
        binding.etDesayuno.setText(HoraHelper.horaToString(datos.opciones.getLimiteDesayuno()));
        binding.etComida1.setText(HoraHelper.horaToString(datos.opciones.getLimiteComida1()));
        binding.etComida2.setText(HoraHelper.horaToString(datos.opciones.getLimiteComida2()));
        binding.etCena.setText(HoraHelper.horaToString(datos.opciones.getLimiteCena()));
        binding.swPdfHorizontal.setChecked(datos.opciones.isPdfHorizontal());
        binding.swPdfIncluirServicios.setChecked(datos.opciones.isPdfIncluirServicios());
        binding.swPdfIncluirNotas.setChecked(datos.opciones.isPdfIncluirNotas());
        binding.swPdfAgruparNotas.setChecked(datos.opciones.isPdfAgruparNotas());
        binding.swSumarTomaDeje.setChecked(datos.opciones.isSumarTomaDeje());
        binding.swIniarCalendario.setChecked(datos.opciones.isIniciarCalendario());
        binding.swTecladoNumerico.setChecked(datos.opciones.isActivarTecladoNumerico());
        binding.swInferirTurnos.setChecked(datos.opciones.isInferirTurnos());
        binding.etDiaBaseTurnos.setText(String.valueOf(datos.opciones.getDiaBaseTurnos()));
        binding.etMesBaseTurnos.setText(String.valueOf(datos.opciones.getMesBaseTurnos()));
        binding.etAOBaseTurnos.setText(String.valueOf(datos.opciones.getAñoBaseTurnos()));
        binding.swGuardarSiempre.setChecked(datos.opciones.isGuardarSiempre());

        // Registrar los listeners
        binding.etPrimerMes.setOnFocusChangeListener(this);
        binding.etPrimerAO.setOnFocusChangeListener(this);
        binding.etAcumuladasAnteriores.setOnFocusChangeListener(this);
        binding.etRelevoFijo.setOnFocusChangeListener(this);
        binding.etJornadaMedia.setOnFocusChangeListener(this);
        binding.etJornadaMinima.setOnFocusChangeListener(this);
        binding.etLimiteServicios.setOnFocusChangeListener(this);
        binding.etCierreMes.setOnFocusChangeListener(this);
        binding.etJornadaAnual.setOnFocusChangeListener(this);
        binding.etInicioNocturnas.setOnFocusChangeListener(this);
        binding.etFinalNocturnas.setOnFocusChangeListener(this);
        binding.etDesayuno.setOnFocusChangeListener(this);
        binding.etComida1.setOnFocusChangeListener(this);
        binding.etComida2.setOnFocusChangeListener(this);
        binding.etCena.setOnFocusChangeListener(this);
        binding.etDiaBaseTurnos.setOnFocusChangeListener(this);
        binding.etMesBaseTurnos.setOnFocusChangeListener(this);
        binding.etAOBaseTurnos.setOnFocusChangeListener(this);

        binding.swRellenarSemana.setOnCheckedChangeListener(this);
        binding.swModoBasico.setOnCheckedChangeListener(this);
        binding.swVerMesActual.setOnCheckedChangeListener(this);
        binding.swSumarTomaDeje.setOnCheckedChangeListener(this);
        binding.swIniarCalendario.setOnCheckedChangeListener(this);
        binding.swRegularJornada.setOnCheckedChangeListener(this);
        binding.swRegularBisiestos.setOnCheckedChangeListener(this);
        binding.swTecladoNumerico.setOnCheckedChangeListener(this);
        binding.swPdfHorizontal.setOnCheckedChangeListener(this);
        binding.swPdfIncluirServicios.setOnCheckedChangeListener(this);
        binding.swPdfIncluirNotas.setOnCheckedChangeListener(this);
        binding.swPdfAgruparNotas.setOnCheckedChangeListener(this);
        binding.swInferirTurnos.setOnCheckedChangeListener(this);
        binding.swGuardarSiempre.setOnCheckedChangeListener(this);

        binding.etPrimerMes.addTextChangedListener(whatchers.primerMesWhatcher);
        binding.etPrimerAO.addTextChangedListener(whatchers.primerAñoWhatcher);
        binding.etAcumuladasAnteriores.addTextChangedListener(whatchers.acumuladasAnterioresWhatcher);
        binding.etRelevoFijo.addTextChangedListener(whatchers.relevoFijoWhatcher);
        binding.etJornadaMedia.addTextChangedListener(whatchers.jornadaMediaWhatcher);
        binding.etJornadaMinima.addTextChangedListener(whatchers.jornadaMinimaWhatcher);
        binding.etLimiteServicios.addTextChangedListener(whatchers.limiteEntreServiciosWhatcher);
        binding.etCierreMes.addTextChangedListener(whatchers.diaCierreMesWhatcher);
        binding.etJornadaAnual.addTextChangedListener(whatchers.jornadaAnualWhatcher);
        binding.etInicioNocturnas.addTextChangedListener(whatchers.inicioNocturnasWhatcher);
        binding.etFinalNocturnas.addTextChangedListener(whatchers.finalNocturnasWhatcher);
        binding.etDesayuno.addTextChangedListener(whatchers.desayunoWhatcher);
        binding.etComida1.addTextChangedListener(whatchers.comida1Whatcher);
        binding.etComida2.addTextChangedListener(whatchers.comida2Whatcher);
        binding.etCena.addTextChangedListener(whatchers.cenaWhatcher);
        binding.etDiaBaseTurnos.addTextChangedListener(whatchers.diaBaseTurnosWhatcher);
        binding.etMesBaseTurnos.addTextChangedListener(whatchers.mesBaseTurnosWhatcher);
        binding.etAOBaseTurnos.addTextChangedListener(whatchers.añoBaseTurnosWhatcher);

    }

    // AL PULSAR UNA TECLA
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Al pulsar la tecla retroceso
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (binding.etPrimerMes.hasFocus()) {
                binding.etPrimerAO.requestFocus();
            } else {
                binding.etPrimerMes.requestFocus();
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // AL CAMBIAR EL FOCO UN CAMPO
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.et_primerMes:
                    int mes = datos.opciones.getPrimerMes();
                    binding.etPrimerMes.setText(Integer.toString(datos.opciones.getPrimerMes()));
                    break;
                case R.id.et_primerAño:
                    binding.etPrimerMes.setText(Integer.toString(datos.opciones.getPrimerAño()));
                    break;
                case R.id.et_acumuladasAnteriores:
                    binding.etAcumuladasAnteriores.setText(Double.toString(datos.opciones.getAcumuladasAnteriores()));
                    break;
                case R.id.et_relevoFijo:
                    binding.etPrimerMes.setText(Integer.toString(datos.opciones.getRelevoFijo()));
                    break;
                case R.id.et_jornadaMedia:
                    binding.etAcumuladasAnteriores.setText(Double.toString(datos.opciones.getJornadaMedia()));
                    break;
                case R.id.et_jornadaMinima:
                    binding.etAcumuladasAnteriores.setText(Double.toString(datos.opciones.getJornadaMinima()));
                    break;
                case R.id.et_limiteServicios:
                    binding.etLimiteServicios.setText(HoraHelper.horaToString(datos.opciones.getLimiteEntreServicios()));
                    break;
                case R.id.et_cierreMes:
                    binding.etPrimerMes.setText(Integer.toString(datos.opciones.getDiaCierreMes()));
                    break;
                case R.id.et_jornadaAnual:
                    binding.etPrimerMes.setText(Integer.toString(datos.opciones.getJornadaAnual()));
                    break;
                case R.id.et_inicioNocturnas:
                    binding.etLimiteServicios.setText(HoraHelper.horaToString(datos.opciones.getInicioNocturnas()));
                    break;
                case R.id.et_finalNocturnas:
                    binding.etLimiteServicios.setText(HoraHelper.horaToString(datos.opciones.getFinalNocturnas()));
                    break;
                case R.id.et_desayuno:
                    binding.etLimiteServicios.setText(HoraHelper.horaToString(datos.opciones.getLimiteDesayuno()));
                    break;
                case R.id.et_comida1:
                    binding.etLimiteServicios.setText(HoraHelper.horaToString(datos.opciones.getLimiteComida1()));
                    break;
                case R.id.et_comida2:
                    binding.etLimiteServicios.setText(HoraHelper.horaToString(datos.opciones.getLimiteComida2()));
                    break;
                case R.id.et_cena:
                    binding.etLimiteServicios.setText(HoraHelper.horaToString(datos.opciones.getLimiteCena()));
                    break;
                case R.id.et_diaBaseTurnos:
                    binding.etPrimerMes.setText(Integer.toString(datos.opciones.getDiaBaseTurnos()));
                    break;
                case R.id.et_mesBaseTurnos:
                    binding.etPrimerMes.setText(Integer.toString(datos.opciones.getMesBaseTurnos()));
                    break;
                case R.id.et_añoBaseTurnos:
                    binding.etPrimerMes.setText(Integer.toString(datos.opciones.getAñoBaseTurnos()));
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // AL CAMBIAR UN SWITCH
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_modoBasico:
                datos.opciones.setModoBasico(binding.swModoBasico.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_rellenarSemana:
                datos.opciones.setRellenarSemana(binding.swRellenarSemana.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_verMesActual:
                datos.opciones.setVerMesActual(binding.swVerMesActual.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_sumarTomaDeje:
                datos.opciones.setSumarTomaDeje(binding.swSumarTomaDeje.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_iniarCalendario:
                datos.opciones.setIniciarCalendario(binding.swIniarCalendario.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_regularJornada:
                datos.opciones.setRegularJornadaAnual(binding.swRegularJornada.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_RegularBisiestos:
                datos.opciones.setRegularBisiestos(binding.swRegularBisiestos.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_TecladoNumerico:
                datos.opciones.setActivarTecladoNumerico(binding.swTecladoNumerico.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_pdfHorizontal:
                datos.opciones.setPdfHorizontal(binding.swPdfHorizontal.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_pdfIncluirServicios:
                datos.opciones.setPdfIncluirServicios(binding.swPdfIncluirServicios.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_pdfIncluirNotas:
                datos.opciones.setPdfIncluirNotas(binding.swPdfIncluirNotas.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_pdfAgruparNotas:
                datos.opciones.setPdfAgruparNotas(binding.swPdfAgruparNotas.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_inferirTurnos:
                datos.opciones.setInferirTurnos(binding.swInferirTurnos.isChecked());
                datos.guardarOpciones();
                break;
            case R.id.sw_guardarSiempre:
                datos.opciones.setGuardarSiempre(binding.swGuardarSiempre.isChecked());
                datos.guardarOpciones();
                break;
        }

    }

}
