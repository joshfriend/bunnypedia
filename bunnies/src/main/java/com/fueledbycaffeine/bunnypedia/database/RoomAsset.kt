/*
 * Copyright (C) 2017 Ibrahim Eid
 * https://github.com/humazed/RoomAsset/blob/master/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fueledbycaffeine.bunnypedia.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.preference.PreferenceManager
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class RoomAsset {
  companion object {
    private const val INSTANTIATED = "INSTANTIATED"

    /**
     * Creates a RoomDatabase.Builder for a  persistent database. Once a database is built, you
     * should keep a reference to it and re-use it.
     *
     * @param context The context for the database. This is usually the Application context.
     * @param klass The abstract class which is annotated with @[Database] and extends [RoomDatabase].
     * @param name The name of the database file.
     * @param storageDirectory To store the database file upon creation; caller must ensure that the specified absolute
     * path is available and can be written to; not needed if the database is the default location :assets/database.
     * @param factory to use for creating cursor objects, or null for the default.
     * @return A [RoomDatabase.Builder<T>] which you can use to create the database.
     */
    @JvmStatic
    @JvmOverloads
    fun <T : RoomDatabase> databaseBuilder(
      context: Context,
      klass: Class<T>,
      name: String,
      storageDirectory: String? = null,
      factory: SQLiteDatabase.CursorFactory? = null
    ): RoomDatabase.Builder<T> {
      prepareDatabase(context, name, storageDirectory, factory)

      return Room.databaseBuilder(context, klass, name)
        .addMigrations(object : Migration(1, 2) {
          override fun migrate(database: SupportSQLiteDatabase) {}
        })
    }

    fun deleteDatabase(context: Context) {
      context.getDatabasePath(AppDatabase.DATABASE_NAME).delete()

      val prefs = PreferenceManager.getDefaultSharedPreferences(context)
      prefs.edit()
        .remove(INSTANTIATED)
        .apply()
    }

    /**
     * Open the database and copy it to data folder using [SQLiteAssetHelper]
     */
    private fun prepareDatabase(
      context: Context,
      name: String,
      storageDirectory: String?,
      factory: SQLiteDatabase.CursorFactory?
    ) {
      val prefs = PreferenceManager.getDefaultSharedPreferences(context)

      if (!prefs.getBoolean(INSTANTIATED, false)) {
        SQLiteAssetHelper(context, name, storageDirectory, factory, 1).writableDatabase.close()
        prefs.edit()
          .putBoolean(INSTANTIATED, true)
          .apply()
      }
    }
  }
}
