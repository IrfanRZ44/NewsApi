package id.exomatik.news.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.gson.Gson

class DataSave(private val context: Context?) {
    private val preffs: SharedPreferences? = context?.getSharedPreferences("UserPref", 0)

    fun setDataObject(any: Any?, key: String) {
        try {
            val prefsEditor: SharedPreferences.Editor = preffs?.edit()
                ?: throw Exception("Preffs Belum Di Inisialisasikan")
            val gson = Gson()
            val json: String = gson.toJson(any)
            prefsEditor.putString(key, json)
            prefsEditor.apply()
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun setDataString(value: String?, key: String) {
        try {
            val prefsEditor: SharedPreferences.Editor =
                preffs?.edit() ?: throw Exception("Preffs Belum Di Inisialisasikan")
            prefsEditor.putString(key, value)
            prefsEditor.apply()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun getKeyString(key: String): String? {
        return try {
            preffs?.getString(key, "") ?: throw Exception("Data Kosong")
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            null
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun setDataBoolean(value: Boolean, key: String) {
        try {
            val prefsEditor: SharedPreferences.Editor =
                preffs?.edit() ?: throw Exception("Preffs Belum Di Inisialisasikan")
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun getKeyBoolean(key: String): Boolean {
        return preffs?.getBoolean(key, false) ?: false
    }
}