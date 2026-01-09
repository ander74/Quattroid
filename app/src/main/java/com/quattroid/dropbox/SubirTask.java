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

package com.quattroid.dropbox;

import android.content.Context;
import android.os.AsyncTask;


public class SubirTask extends AsyncTask<Void, Void, Soporte.Resultado> {

    private final Callback mCallback;
    private final Context context;

    public interface Callback {
        void onComplete();

        void onError(Soporte.Resultado resultado);
    }

    public SubirTask(Callback callback, Context c) {
        mCallback = callback;
        context = c;
    }

    @Override
    protected Soporte.Resultado doInBackground(Void... params) {

        Soporte.Resultado resultado;

        resultado = Soporte.SubirBaseDatos(context);

//        if (resultado == Soporte.Resultado.OK){
//            resultado = Soporte.SubirOpciones(context);
//        }

        return resultado;
    }

    @Override
    protected void onPostExecute(Soporte.Resultado resultado) {
        super.onPostExecute(resultado);

        if (resultado == Soporte.Resultado.OK) {
            mCallback.onComplete();
        } else {
            mCallback.onError(resultado);
        }
    }
}
