package id.telkomsel.merchandise.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import id.telkomsel.merchandise.model.ModelSales
import com.google.gson.Gson
import id.telkomsel.merchandise.model.ModelInfoApps
import id.telkomsel.merchandise.model.ModelStore
import id.telkomsel.merchandise.utils.Constant.reffInfoApps
import id.telkomsel.merchandise.utils.Constant.reffSales
import id.telkomsel.merchandise.utils.Constant.reffPelanggan

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

    fun getDataSales(): ModelSales? {
        return try {
            val gson = Gson()
            val json: String = preffs?.getString(reffSales, "")
                ?: throw Exception("Preffs Belum Di Inisialisasikan")
            gson.fromJson(json, ModelSales::class.java)
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            null
        }
    }

    fun getDataPelanggan(): ModelStore? {
        return try {
            val gson = Gson()
            val json: String = preffs?.getString(reffPelanggan, "")
                ?: throw Exception("Preffs Belum Di Inisialisasikan")
            gson.fromJson(json, ModelStore::class.java)
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            null
        }
    }

    fun getDataApps(): ModelInfoApps? {
        return try {
            val gson = Gson()
            val json: String = preffs?.getString(reffInfoApps, "")
                ?: throw Exception("Preffs Belum Di Inisialisasikan")
            gson.fromJson(json, ModelInfoApps::class.java)
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            null
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