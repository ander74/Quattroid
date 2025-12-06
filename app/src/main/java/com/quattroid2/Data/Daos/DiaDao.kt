/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid2.Data.Daos

import androidx.room.Query
import com.quattroid2.Data.Models.DiaModel

interface DiaDao {

    @Query("SELECT * FROM Calendario WHERE Mes = :mes AND Año = :año")
    fun getMes(mes: Int, año: Int): MutableList<DiaModel>
}