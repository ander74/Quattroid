package com.quattroid.dropbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

public class EmailTask extends AsyncTask<Void, Void, Soporte.Resultado> {

    private final EmailTask.Callback mCallback;
    private final Context context;

    public interface Callback {
        void onComplete();

        void onError(Soporte.Resultado resultado);
    }

    public EmailTask(EmailTask.Callback callback, Context c) {
        mCallback = callback;
        context = c;
    }


    @Override
    protected Soporte.Resultado doInBackground(Void... voids) {

        // Extraemos las opciones
        SharedPreferences Opciones = PreferenceManager.getDefaultSharedPreferences(context);

        String emailDropbox;
        Soporte.Resultado resultado;
        try {
            DbxClientV2 cliente = DropBoxFactory.GetCliente(context);
            if (cliente == null) {
                emailDropbox = "Sin cuenta registrada.";
                return Soporte.Resultado.ERROR_DROPBOX;
            }
            emailDropbox = cliente.users().getCurrentAccount().getEmail();
            resultado = Soporte.Resultado.OK;
        } catch (DbxException dbe) {
            emailDropbox = "Sin cuenta registrada.";
            resultado = Soporte.Resultado.ERROR_DROPBOX;
        }
        Opciones.edit().putString("EmailDropbox", emailDropbox).apply();
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
