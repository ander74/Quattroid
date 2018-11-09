package com.quattro.dropbox;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.dropbox.core.DbxException;

public class EmailTask extends AsyncTask<Void, Void, Soporte.Resultado> {

    private final String Token;
    private final EmailTask.Callback mCallback;
    private final SharedPreferences Opciones;

    public interface Callback{
        void onComplete();
        void onError(Soporte.Resultado resultado);
    }

    public EmailTask(String token, SharedPreferences opciones, EmailTask.Callback callback){
        Token = token;
        Opciones = opciones;
        mCallback = callback;
    }


    @Override
    protected Soporte.Resultado doInBackground(Void... voids) {

        String emailDropbox;
        Soporte.Resultado resultado;
        try {
            emailDropbox = DropBoxFactory.GetCliente(Token).users().getCurrentAccount().getEmail();
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

        switch (resultado){
            case OK:
                mCallback.onComplete();
                break;
            default:
                mCallback.onError(resultado);
                break;
        }
    }



}
