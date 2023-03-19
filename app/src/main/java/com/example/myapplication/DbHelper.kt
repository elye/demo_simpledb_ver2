package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DbHelper(context: Context) : SQLiteOpenHelper(context, DbHelper.DATABASE_NAME, null, DbHelper.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w(TAG, "Upgrade from version $oldVersion to $newVersion")

        if (oldVersion <  2) {
            upgradeVersion2(db)

        }
    }

    private fun upgradeVersion2(db: SQLiteDatabase) {
        db.execSQL("ALTER TABLE $DATABASE_TABLE ADD COLUMN $KEY_VALUE INTEGER DEFAULT 0;")

        val cursor = db.query(DATABASE_TABLE, RESULT_COLUMNS, null, null, null, null, null, null)
        if (cursor != null) {
            var hasItem = cursor.moveToFirst()
            while (hasItem) {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val nameStr = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val values = ContentValues()
                values.put(KEY_VALUE, nameStr.length)
                db.update(DATABASE_TABLE, values, "$KEY_ID=?", arrayOf(id.toString()))
                hasItem = cursor.moveToNext()
            }
            cursor.close()
        }
    }

    fun clearDb() {
        writableDatabase.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLE")
        onCreate(writableDatabase)
    }

    companion object {

        const val KEY_ID = "_ID"
        const val KEY_NAME = "NAME"
        const val KEY_VALUE = "VALUE"
        const val DATABASE_TABLE = "simpletable"
        var RESULT_COLUMNS = arrayOf(KEY_ID, KEY_NAME, KEY_VALUE)

        private val TAG = DbHelper::class.java.simpleName

        private const val DATABASE_NAME = "simpledatabase.sqlite"
        private const val DATABASE_VERSION = 2

        private const val DATABASE_CREATE =
            "CREATE TABLE $DATABASE_TABLE ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$KEY_NAME TEXT NOT NULL, $KEY_VALUE INTEGER DEFAULT 0);"
    }
}
