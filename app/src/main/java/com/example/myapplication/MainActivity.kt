package com.example.myapplication

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DbHelper

    private lateinit var txtAllData: TextView
    private lateinit var editEntry: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = DbHelper(this)
        txtAllData = findViewById(R.id.txt_all_data)
        editEntry = findViewById(R.id.edit_entry)
        loadData()
    }

    private fun loadData() {
        var result = ""
        val cursor = dbHelper.writableDatabase.query(DbHelper.DATABASE_TABLE, DbHelper.RESULT_COLUMNS,
            null, null, null, null, DbHelper.KEY_ID)
        val indexColumnName = cursor.getColumnIndexOrThrow(DbHelper.KEY_NAME)
        val indexColumnValue = cursor.getColumnIndexOrThrow(DbHelper.KEY_VALUE)
        while (cursor != null && cursor.moveToNext()) {
            result += "${cursor.getString(indexColumnName)}(${cursor.getInt(indexColumnValue)})-"
        }
        txtAllData.text = result
    }

    fun insertData(view: View) {
        if (!TextUtils.isEmpty(editEntry.text)) {
            val newValue = ContentValues()
            val strValue = editEntry.text.toString()
            newValue.put(DbHelper.KEY_NAME, strValue)
            newValue.put(DbHelper.KEY_VALUE, strValue.length)
            dbHelper.writableDatabase.insert(DbHelper.DATABASE_TABLE, null, newValue)
            editEntry.text.clear()
            loadData()
        }
    }

    fun clearData(view: View) {
        dbHelper.clearDb()
        loadData()
    }
}