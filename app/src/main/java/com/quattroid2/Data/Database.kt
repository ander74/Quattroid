/*
 * AnderSoft - Open Source Software
 * Licencia GPL 3.0 - 2025
 * Visite https://www.gnu.org/licenses/gpl-3.0.html para más detalles.
 */

package com.quattroid2.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.quattroid2.Data.Daos.DiaDao
import com.quattroid2.Data.Daos.LineaDao
import com.quattroid2.Data.Models.DiaModel
import com.quattroid2.Data.Models.LineaModel

@Database(entities = [DiaModel::class, LineaModel::class], version = 6)
abstract class Database : RoomDatabase() {

    abstract fun diaDao(): DiaDao

    abstract fun lineaDao(): LineaDao
}