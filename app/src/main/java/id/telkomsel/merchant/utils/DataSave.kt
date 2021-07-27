package id.telkomsel.merchant.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.utils.Constant.reffUser
import com.google.gson.Gson
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.utils.Constant.reffMerchant
import id.telkomsel.merchant.utils.Constant.reffPelanggan

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

    fun getDataMerchant(): ModelMerchant? {
        return try {
            val gson = Gson()
            val json: String = preffs?.getString(reffMerchant, "")
                ?: throw Exception("Preffs Belum Di Inisialisasikan")
            gson.fromJson(json, ModelMerchant::class.java)
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            null
        }
    }

    fun getDataPelanggan(): ModelPelanggan? {
        return try {
            val gson = Gson()
            val json: String = preffs?.getString(reffPelanggan, "")
                ?: throw Exception("Preffs Belum Di Inisialisasikan")
            gson.fromJson(json, ModelPelanggan::class.java)
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