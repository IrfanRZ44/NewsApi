package id.telkomsel.merchandise.ui.sales.detailSales

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.response.ModelResponse
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.telkomsel.merchandise.utils.adapter.onClickFoto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DetailSalesAdminViewModel(
    private val context: Context?,
    private val navController: NavController
) : BaseViewModel() {
    val dataSales = MutableLiveData<ModelSales>()

    fun clickFotoDepan(){
        dataSales.value?.foto_depan_kendaraan?.let { onClickFoto(it, navController) }
    }

    fun clickFotoBelakang(){
        dataSales.value?.foto_belakang_kendaraan?.let { onClickFoto(it, navController) }
    }

    fun clickFotoWajah(){
        dataSales.value?.foto_wajah?.let { onClickFoto(it, navController) }
    }

    fun clickFotoBadan(){
        dataSales.value?.foto_seluruh_badan?.let { onClickFoto(it, navController) }
    }

    fun onClickAccept(){
        dataSales.value?.id?.let { updateStatus(it, Constant.statusActive, "") }
    }

    fun onClickDecline(){
        dataSales.value?.id?.let {
            context?.let {
                    it1 -> showDialogTolak(it1, it)
            }
        }
    }

    fun onClickMaps(){
        val gmmIntentUri = Uri.parse("google.navigation:q=${dataSales.value?.latitude},${dataSales.value?.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context?.startActivity(mapIntent)
    }

    private fun showDialogTolak(context: Context, id: Int){
        val alert = AlertDialog.Builder(context)
        alert.setMessage("Mohon masukkan alasan merchandise ini ditolak :")

        val editText = EditText(context)
        val linearLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayout.setPadding(20, 0, 20, 0)
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(editText)
        alert.setView(linearLayout)

        alert.setPositiveButton(
            "Konfirmasi"
        ) { dialog, _ ->
            val comment = editText.text.toString()
            dialog.dismiss()
            if (comment.isEmpty()){
                message.value = "Error, mohon masukkan komentar"
            }
            else{
                updateStatus(id, Constant.statusDeclined, comment)
            }
        }
        alert.setNegativeButton(
            Constant.batal
        ) { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    private fun updateStatus(id: Int, status: String, comment: String){
        isShowLoading.value = true

        RetrofitUtils.updateStatusSales(id, status, comment,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        if (status == Constant.statusActive){
                            message.value = "Berhasil konfirmasi merchandise"
                            Toast.makeText(context, "Berhasil konfirmasi", Toast.LENGTH_LONG).show()
                        }
                        else{
                            message.value = "Berhasil menolak permintaan merchandise"
                            Toast.makeText(context, "Berhasil menolak permintaan", Toast.LENGTH_LONG).show()
                        }
                        val navOption = NavOptions.Builder().setPopUpTo(R.id.adminFragment, true).build()
                        navController.navigate(R.id.adminFragment, null, navOption)
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = "Error ${t.message}"
                }
            })
    }
}