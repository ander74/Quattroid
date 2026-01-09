/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid.activityHelpers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class Dialogs {

    var dialogSiNoResult: Boolean = false;


    fun showDialogSiNo(context: Context, titulo: String, mensaje: String): Boolean {
        val aviso = AlertDialog.Builder(context)
        aviso.setTitle(titulo)
        aviso.setMessage(mensaje)
        aviso.setPositiveButton("SI") { dialog: DialogInterface, which: Int -> dialogSiNoResult = true }
        aviso.setNegativeButton("NO") { dialog: DialogInterface, which: Int -> dialogSiNoResult = false }
        aviso.show()
        return dialogSiNoResult
    }

    companion object {


    }
}