/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid2.Data.Daos

import androidx.room.Query
import com.quattroid2.Data.Models.LineaModel

interface LineaDao {

    @Query("SELECT * FROM Lineas ")
    fun getLineas(): MutableList<LineaModel>
}